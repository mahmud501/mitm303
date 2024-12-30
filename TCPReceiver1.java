package MITM303;

import java.io.*;
import java.net.*;

public class TCPReceiver1 {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(12345);
		System.out.println("Server is listening on port 12345...");

		// Accept the client connection
		while (true) {
			try (Socket socket = serverSocket.accept()) {
				System.out.println("New Connection: ");
				System.out.println("Client connected: " + socket.getInetAddress());

				// Create input and output streams for communication
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

				String receivedPacket;
				while ((receivedPacket = input.readLine()) != null) {
					System.out.println("Received packet: " + receivedPacket);

					// Simulate packet loss for packet number 5
//					if (receivedPacket.equals("5")) {
//						System.out.println("Simulating packet loss: ACK for packet " + receivedPacket + " not sent.");
//						break; // Skip sending an ACK for packet 5
//					} else {
						// Send acknowledgment back to client for all other packets
						output.println("ACK " + receivedPacket);
//					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}