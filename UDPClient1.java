package MITM303;

import java.io.*;
import java.net.*;

public class UDPClient1 {
    public static void main(String[] args) {
        try {
            // Input the client's name and number
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter your name: ");
            String clientName = reader.readLine();

            System.out.print("Enter an integer between 1 and 100: ");
            int clientInteger = Integer.parseInt(reader.readLine());

            // Validate the number input
            if (clientInteger < 1 || clientInteger > 100) {
                System.out.println("Error: The number must be between 1 and 100.");
                return;
            }

            // Create a DatagramSocket to communicate with the server
            DatagramSocket clientSocket = new DatagramSocket();

            // Prepare the message to send
            String message = "Client of " + clientName + " " + clientInteger;
            byte[] sendData = message.getBytes();

            // Send the message to the server (localhost, port 6789)
            InetAddress serverAddress = InetAddress.getByName("localhost");
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 6789);
            clientSocket.send(sendPacket);

            // Prepare to receive server's response
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            // Parse the server's response
            String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String[] responseParts = serverResponse.split(" ");
            String serverName = responseParts[0];
            int serverInteger = Integer.parseInt(responseParts[1]);

            // Display the server's response
            System.out.println("Server response: ");
            System.out.println("Server name: " + serverName);
            System.out.println("Server chosen number: " + serverInteger);
            System.out.println("Sum of client number and server number: " + (clientInteger + serverInteger));

            // Close the socket
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
