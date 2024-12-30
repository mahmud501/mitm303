package MITM303;

import java.io.*;
import java.net.*;

public class TCPSender1 {
	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 12345)) {
			// Create input and output streams for communication
			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true);

			String packet;
			int nextSeqNum = 0;
			int windowStart = 0;
			int windowEnd = 3; // Fixed window size of 4

			while (nextSeqNum < 16) { // Simulating sending 10 packets
				// Send packets within the window range
				if (nextSeqNum >= windowStart && nextSeqNum <= windowEnd) {
					packet = Integer.toString(nextSeqNum);
					System.out.println("Window Position: "+windowStart+"->"+windowEnd);
					System.out.println("Sending packet: " + packet);
					serverOutput.println(packet); // Send packet to server

					// Wait for ACK with a timeout
					boolean ackReceived = false;

					long startTime = System.currentTimeMillis();
					while (!ackReceived && (System.currentTimeMillis() - startTime) < 3000000) { // 3-second timeout
						
						String ack = serverInput.readLine();
						if (ack != null && ack.contains("ACK")) {
							System.out.println("Received: " + ack);
							// Slide the window after receiving ACK
							nextSeqNum++;
							windowStart++;
							windowEnd++;
							ackReceived = true;
						}

						if (!ackReceived) {
							nextSeqNum++;
							System.out.println("ACK not received for packet: " + packet);
						}
					}
				}

				// Simulate network delay between sending packets
				try {
					Thread.sleep(1000); // Simulate network delay of 1 second
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
