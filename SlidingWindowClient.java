package MITM303;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class SlidingWindowClient {
	private static final int PORT = 9876;
	private static final String SERVER_ADDRESS = "localhost"; // Can be changed if needed

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		// Step a: Get the frame size from the user
		System.out.print("Enter the window size: ");
		int windowSize = scanner.nextInt();

		System.out.print("Enter the total number of frames to send: ");
		int totalFrames = scanner.nextInt();

		try (DatagramSocket socket = new DatagramSocket()) {
			InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);

			int currentFrame = 0;

			// Send frames in sliding window
			while (currentFrame < totalFrames) {
				for (int i = 0; i < windowSize && currentFrame < totalFrames; i++) {
					String frameMessage = "Frame " + (currentFrame + 1);
					DatagramPacket sendPacket = new DatagramPacket(frameMessage.getBytes(), frameMessage.length(),
							serverAddress, PORT);
					socket.send(sendPacket);
					System.out.println("Sent: " + frameMessage);
					currentFrame++;
				}
				int j = windowSize;
				// Receive ACKs from server
				for (int i = 0; i < windowSize && currentFrame - i > 0; i++) {
					byte[] receiveData = new byte[1024];
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					socket.receive(receivePacket);
					String ackMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
					System.out.println("Received: " + ackMessage);
					if (currentFrame >= totalFrames) {
						System.out.println("All Packet Acked");
						break;
					} else {
						j--;
						System.out.println("Window sliding from: " + (currentFrame - j + 1) + " to: "
								+ (currentFrame - j + windowSize));
					}
				}
			}
			System.out.println("All frames sent and acknowledged.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
