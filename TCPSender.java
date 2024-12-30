package MITM303;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class TCPSender {

	static String DataPacket;
	static int windowSize;
	int windowStart, windowEnd;
	int nextSeq, ackedSeq, sentSeq;
	static Scanner UserInput = new Scanner(System.in);

	public static void main(String[] args) {

		System.out.println("Enter the Data Packet you want to Send: ");
		DataPacket = UserInput.nextLine();
		System.out.println("Enter the Window Size: ");
		windowSize = UserInput.nextInt();
		System.out.println("Data to Send: " + DataPacket);
		System.out.println("Window Size: " + windowSize);
		TCPSender TCPClient = new TCPSender();
		TCPClient.TCPConnect();
	}

	public void TCPConnect() {
		try (Socket socket = new Socket("localhost", 9678)) {

			BufferedReader Input = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader ServerInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter ServerOutput = new PrintWriter(socket.getOutputStream(), true);
			nextSeq = 0;
			windowStart = 0;
			windowEnd = windowSize - 1;

			while (nextSeq < DataPacket.length()) {
				for (int i = nextSeq; i <= windowEnd; i++) {
					String packet = Character.toString(DataPacket.charAt(i));
					System.out.println("Sending packet: " + i + " Data: " + packet);
					ServerOutput.println(packet);
					sentSeq = i;
					if (sentSeq == (DataPacket.length() - 1)) {
						break;
					}
				}
				// System.out.println(sentSeq);
				// boolean ackreceived = false;

				long startTime = System.currentTimeMillis();
				while (((System.currentTimeMillis() - startTime) < 300000) && nextSeq <= sentSeq) {
					String ack = ServerInput.readLine();
					if (ack != null && ack.contains("ACK")) {
						System.out.println("Recived: " + ack);
						ackedSeq = Integer.parseInt((ack.toString()).substring(4));
						// System.out.println(ackedSeq);
						// ackreceived=true;
						nextSeq = ackedSeq + 1;
						windowEnd = ackedSeq + windowSize;
						// System.out.println(nextSeq+" "+windowEnd);
					} else {
						System.out.println("Ack not Received...");
					}
				}
			}
			System.out.println("All Data Has been Sent & Acked!");
			System.out.println("Closing TCP Connection... .. .");
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}