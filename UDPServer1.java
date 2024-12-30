package MITM303;

import java.io.*;
import java.net.*;
import java.util.Random;

public class UDPServer1 {
    public static void main(String[] args) {
        try {
            // Fetching dynamic device Name and IP
            String serverDeviceName = InetAddress.getLocalHost().getHostName();
            String serverIPAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Server's device name: " + serverDeviceName);
            System.out.println("Server's IP address: " + serverIPAddress);

            // Listening port 6789
            DatagramSocket serverSocket = new DatagramSocket(6789);
            System.out.println("Server is waiting for client connection...");

            byte[] receiveData = new byte[1024];  // Buffer to receive data from the client

            while (true) {
                // Receive client message
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Parse the message from the client
                String[] messageParts = clientMessage.split(" ");
                if (messageParts.length != 4) {
                    System.out.println("Invalid message format received. Expecting: 'Client of <name> <number>'");
                    continue;
                }

                String clientName = messageParts[2];  // Extract client name
                int clientInteger = Integer.parseInt(messageParts[3]);  // Extract client number

                // Check if client number is in the valid range (1 to 100)
                if (clientInteger < 1 || clientInteger > 100) {
                    System.out.println("Received out of range integer. Server will terminate.");
                    serverSocket.close();
                    return;
                }

                // Server chooses a random integer between 1 and 100
                Random rand = new Random();
                int serverInteger = rand.nextInt(100) + 1;

                // Display client and server information
                System.out.println("Client name: " + clientName);
                System.out.println("Server name: " + serverDeviceName);
                System.out.println("Client number: " + clientInteger);
                System.out.println("Server number: " + serverInteger);
                System.out.println("Sum: " + (clientInteger + serverInteger));

                // Prepare response data
                String response = serverDeviceName + " " + serverInteger;
                byte[] sendData = response.getBytes();

                // Send the response back to the client
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
