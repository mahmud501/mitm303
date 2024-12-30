package MITM303;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {
		try {

			// Manually enter server IP address
			Scanner scanner = new Scanner(System.in);
			System.out.print("Enter the server IP address to connect to: ");
			String serverIP = scanner.nextLine(); // Dynamic input from user for server IP

			// Get integer input from user
			int clientInteger = 0;
			while (true) {
				System.out.print("Enter an integer between 1 and 100: ");
				clientInteger = scanner.nextInt();
				if (clientInteger >= 1 && clientInteger <= 100) {
					break;
				} else {
					System.out.println("Invalid input! Please enter a number between 1 and 100.");
				}
			}

			// Open socket connection to server
			Socket socket = new Socket(serverIP, 6789);
			System.out.println("Connected to the server...");

			// Create input and output streams
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Send client name and integer to the server
			String clientDeviceName = InetAddress.getLocalHost().getHostName();
			out.println(clientDeviceName);
			out.println(clientInteger);

			// Read server's response
			String serverName = in.readLine();
			int serverInteger = Integer.parseInt(in.readLine());

			// Display received data and calculate the sum
			System.out.println("Client name: " + clientDeviceName);
			System.out.println("Server name: " + serverName);
			System.out.println("Client number: " + clientInteger);
			System.out.println("Server number: " + serverInteger);
			System.out.println("Sum: " + (clientInteger + serverInteger));

			socket.close();
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}