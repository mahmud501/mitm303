package MITM303;

import java.io.*;
import java.net.*;
import java.util.Random;

public class Server {
	public static void main(String[] args) {
		try {
			// Fetching dynamic device Name and IP
			String serverDeviceName = InetAddress.getLocalHost().getHostName();
			String serverIPAddress = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Server's device name: " + serverDeviceName);
			System.out.println("Server's IP address: " + serverIPAddress);

			// Listening port 6789
			ServerSocket serverSocket = new ServerSocket(6789);
			System.out.println("Server is waiting for client connection...");

			// Server chooses a random integer between 1 and 100;
			Random rand = new Random();
			int serverNumber = rand.nextInt(100) + 5 ;

			while (true) {
				// Accept incoming client connections
				Socket clientSocket = serverSocket.accept();
				System.out.println("Client connected: " + clientSocket.getInetAddress());

				// Create input and output streams
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

				// Read client's message
				String clientMessage = in.readLine();
				int clientNumber = Integer.parseInt(in.readLine());

				if (clientNumber < 1 || clientNumber > 100) {
					System.out.println("Received out of range integer. Server will terminate.");
					clientSocket.close();
					serverSocket.close();
					return;
				}

				// Display client and server informations
				System.out.println("Client name: " + clientMessage);
				System.out.println("Client number: " + clientNumber);
				System.out.println("Server name: " + serverDeviceName);
				System.out.println("Server number: " + serverNumber);
				System.out.println("Sum: " + (clientNumber + serverNumber));

				// Send server's name and chosen number back to the client
				out.println(serverDeviceName);
				out.println(serverNumber);

				clientSocket.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}