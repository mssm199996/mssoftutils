package mssoftutils.scalehandlers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

public class UDPPacketsTransferer {
	private List<String> packets;
	private String ip;
	private int port;
	private final int timeout = 10_000;

	public UDPPacketsTransferer(List<String> packets, String ip, int port) {
		this.packets = packets;
		this.ip = ip;
		this.port = port;
	}

	public void send() throws SocketException, UnknownHostException, IOException {
		InetAddress address = InetAddress.getByName(this.ip);

		try (DatagramSocket socket = new DatagramSocket()) {
			this.packets.forEach((String packetContent) -> {
				try {
					byte[] buf = DatatypeConverter.parseHexBinary(packetContent);

					DatagramPacket packet = new DatagramPacket(buf, buf.length, address, this.port);
					socket.setSoTimeout(this.timeout);
					socket.send(packet);

					packet = new DatagramPacket(buf, buf.length);
					socket.receive(packet);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			});
		}
	}
}
