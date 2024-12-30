package MITM303;

import java.net.*;
import java.io.*;

public class TCPReceiver {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(9678);
		System.out.println("Server is listening on port 9678 ...");
		while (true) {
			try (Socket socket = serverSocket.accept()) {
				System.out.println("New Connection: ");
				System.out.println("Client connected: " + socket.getInetAddress());

				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

				String receivedPacket;
				String FinalPacket=new String("");
				int nextSeq = 0;
				while ((receivedPacket = input.readLine()) != null) {
					System.out.println("Received packet: " + receivedPacket);
					FinalPacket=FinalPacket.concat(receivedPacket);
					System.out.println("Sending Ack for Packet: "+nextSeq);
					output.println("ACK " + nextSeq);
					nextSeq++;
				}
				System.out.println("Full Data Received: "+FinalPacket);
				System.out.println("Waiting for new Connection. .. ...\nServer is listening on port 9678 ...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}