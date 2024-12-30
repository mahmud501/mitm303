package MITM303;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SlidingWindowTCPClient {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Get frame size (window size) and total number of frames from the user
        System.out.print("Enter the frame size (window size): ");
        int frameSize = scanner.nextInt();
        System.out.print("Enter the total number of frames to send: ");
        int totalFrames = scanner.nextInt();

        // Create socket to connect to the server
        Socket serverSocket = new Socket("localhost", 9876);
        System.out.println("Connected to server.");

        // Input and Output streams for communication
        BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);

        int currentSeqNum = 0;

        // Send frames to the server
        while (currentSeqNum < totalFrames) {
            // Send frames within the window size
            for (int i = 0; i < frameSize && currentSeqNum < totalFrames; i++) {
                String frameData = currentSeqNum + ":FrameData_" + currentSeqNum;
                out.println(frameData);  // Send frame to server
                System.out.println("Sent frame: " + frameData);
                currentSeqNum++;
            }

            // Wait for acknowledgment from the server for all frames sent in this window
            for (int i = currentSeqNum - frameSize; i < currentSeqNum; i++) {
                String ackMessage = in.readLine();
                System.out.println("Received ACK/NACK: " + ackMessage);

                // Process the acknowledgment
                if (ackMessage.startsWith("ACK")) {
                    int ackSeqNum = Integer.parseInt(ackMessage.substring(3));
                    if (ackSeqNum >= i) {
                        // Slide the window forward based on the acknowledgment
                        System.out.println("Sliding window forward: Expected frame is now " + currentSeqNum);
                    }
                } else {
                    // If NACK received, retransmit the frame that was NACKed
                    int nackSeqNum = Integer.parseInt(ackMessage.substring(4));
                    System.out.println("Retransmitting frame with seqNum: " + nackSeqNum);
                    currentSeqNum = nackSeqNum + 1;  // Move back to the last acknowledged frame
                    break;  // Restart the window due to retransmission
                }
            }
        }

        // Send a message to signal end of communication if needed (e.g., "END")
        System.out.println("All frames sent. Closing connection.");

        // Close the connection
        in.close();
        out.close();
        serverSocket.close();
        scanner.close();
    }
}
