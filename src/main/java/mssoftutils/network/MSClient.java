package mssoftutils.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public abstract class MSClient implements Runnable {

	private DatagramSocket datagramSocket;
	private int port = 28955;
	private int bufferSize = 1024;
	private String discoveryRequest = "MSSERVER_DISCOVERY_REQUEST";
	private String discoveryResponse = "MSSERVER_DISCOVERY_RESPONSE";
	private Pauser pauser = null;
	private Thread launcher = null;

	public MSClient() {
		this(28955);
	}

	public MSClient(int port) {
		try {
			this.port = port;
			this.datagramSocket = new DatagramSocket();
			this.datagramSocket.setBroadcast(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void scanNetwork() {
		try {
			if (this.datagramSocket.isClosed())
				this.datagramSocket = new DatagramSocket();

			this.launcher = new Thread(this);
			this.launcher.start();
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		try {
			this.onScanStarted();

			try {
				this.sendDiscoveryRequest();
				this.listenToAvailableServers();
			} catch (SocketException e) {
				e.printStackTrace();
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void listenToAvailableServers() throws IOException {
		Set<RemoteMachine> discoveredRemoteMachines = new HashSet<>();

		byte[] recvBuf = new byte[this.bufferSize];

		DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);

		if (this.pauser != null)
			new Thread(new Pauser(this)).start();

		do {
			this.datagramSocket.receive(receivePacket);

			String message = new String(receivePacket.getData()).trim();

			if (message.startsWith(this.discoveryResponse)) {
				RemoteMachine remoteMachine = new RemoteMachine();
				remoteMachine.setIpAddress(receivePacket.getAddress().getHostAddress());
				remoteMachine.setHostName(message.substring(this.discoveryResponse.length() + 1));

				if (!discoveredRemoteMachines.add(remoteMachine)) {
					this.onRemoteMachineFound(remoteMachine);
				}
			}
		} while (true);
	}

	public void sendRequest(String request, RemoteMachine remoteMachine) {
		byte[] sendData = request.getBytes();

		try {
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
					InetAddress.getByName(remoteMachine.getIpAddress()), this.port);

			this.datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendDiscoveryRequest() throws SocketException {
		byte[] sendData = this.discoveryRequest.getBytes();

		try {
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
					InetAddress.getByName("255.255.255.255"), this.port);

			this.datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();

			if (networkInterface.isLoopback() || !networkInterface.isUp())
				continue;

			for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
				InetAddress broadcast = interfaceAddress.getBroadcast();

				if (broadcast == null)
					continue;

				try {
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, this.port);

					this.datagramSocket.send(sendPacket);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void stopScanning() {
		synchronized (this.launcher) {
			this.launcher.stop();
			this.datagramSocket.close();
			this.onScanFinished();
		}
	}

	public abstract void onScanStarted();

	public abstract void onScanFinished();

	public abstract void onRemoteMachineFound(RemoteMachine remoteMachine);

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Pauser getPauser() {
		return pauser;
	}

	public void setPauser(Pauser pauser) {
		this.pauser = pauser;
	}

	private class Pauser implements Runnable {
		private MSClient msclient;
		private long listeningPeriod = 30_000;

		public Pauser(MSClient msclient) {
			this.msclient = msclient;
		}

		@Override
		public void run() {
			try {
				synchronized (this) {
					this.wait(this.listeningPeriod);
					this.msclient.stopScanning();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new Thread((new MSClient() {
			@Override
			public void onScanFinished() {
				System.out.println("scan finished");
			}

			@Override
			public void onRemoteMachineFound(RemoteMachine remoteMachine) {
				this.stopScanning();
			}

			@Override
			public void onScanStarted() {
				System.out.println("scan started");
			}
		})).start();
	}
}
