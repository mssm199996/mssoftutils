package mssoftutils.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class MSServer implements Runnable {

	private DatagramSocket server;
	private int port;
	private int bufferSize = 1024;
	private String discoveryRequest = "MSSERVER_DISCOVERY_REQUEST";
	private String discoveryResponse = "MSSERVER_DISCOVERY_RESPONSE";
	private String okayResponse = "MSSERVER_OKAY_RESPONSE";
	private String noResponse = "MSSERVER_NO_RESPONSE";

	private Map<String, Runnable> handlers = new HashMap<>();

	public MSServer() throws SocketException {
		this(31532);
	}

	public MSServer(int port) throws SocketException {
		this.port = port;
		this.server = new DatagramSocket(this.port);
		this.server.setBroadcast(true);

		(new Thread(this)).start();

	}

	@Override
	public void run() {
		while (true) {
			try {
				byte[] recvBuf = new byte[this.bufferSize];

				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);

				this.server.receive(packet);

				String message = new String(packet.getData()).trim();
				System.out.println("received message: " + message);

				if (message.equals(this.discoveryRequest)) {
					byte[] sendData = (this.discoveryResponse + " " + InetAddress.getLocalHost().getHostName())
							.getBytes();

					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(),
							packet.getPort());

					this.server.send(sendPacket);
				} else {
					Runnable handler = this.handlers.get(message);

					byte[] dataToSend;

					if (handler != null) {
						(new Thread(handler)).start();

						dataToSend = this.okayResponse.getBytes();

					} else {
						dataToSend = this.noResponse.getBytes();
					}

					DatagramPacket packetToSend = new DatagramPacket(dataToSend, dataToSend.length, packet.getAddress(),
							packet.getPort());

					this.server.send(packetToSend);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void addHandler(String request, Runnable handler) {
		this.handlers.put(request, handler);
	}

	public static void main(String[] args) {
		try {
			new MSServer();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
