package MITM303;
import java.net.*;
import java.io.*;

public class SlidingWindowServer {
    private static final int PORT = 9876;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("Server started. Waiting for frames...");

            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket); // Receive data

                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received frame: " + receivedMessage);

                // Send back an ACK
                String ackMessage = "ACK " + receivedMessage;
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                DatagramPacket sendPacket = new DatagramPacket(ackMessage.getBytes(), ackMessage.length(), clientAddress, clientPort);
                socket.send(sendPacket); // Send ACK
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
