package MITM303;

import java.io.*;
import java.net.*;

public class SlidingWindowTCPServer {

    public static void main(String[] args) throws IOException {
        int port = 9876;
        int expectedSeqNum = 0;

        // Create server socket to listen for client connections
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is waiting for client frames...");

        Socket clientSocket = serverSocket.accept(); // Accept the incoming connection
        System.out.println("Client connected.");

        // Input and Output streams for communication
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String frame;
        while ((frame = in.readLine()) != null) {
            // Extract the sequence number from the frame
            int seqNum = Integer.parseInt(frame.split(":")[0]);
            System.out.println("Received frame: " + frame);

            // Check if the sequence number matches the expected sequence number
            if (seqNum == expectedSeqNum) {
                // Acknowledge the frame
                out.println("ACK" + seqNum);
                expectedSeqNum++;  // Slide the window
                System.out.println("Sent: ACK" + seqNum);
            } else {
                // Send NACK to the client for the last correctly received frame
                out.println("NACK" + (expectedSeqNum - 1));
                System.out.println("Sent: NACK" + (expectedSeqNum - 1));
            }

            // Optionally, print which frame the server is expecting next
            System.out.println("Server expects: " + expectedSeqNum);
        }

        // Close the connection
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
}
