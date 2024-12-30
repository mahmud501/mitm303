package MITM303;

//class Sender {
//    private static final int MAX_WINDOW_SIZE = 4;  // Window size (fixed)
//    private int nextSeqNum = 0;  // Sequence number for sending packets
//    private int windowStart = 0;  // Start of the window
//    private int windowEnd = MAX_WINDOW_SIZE - 1;  // End of the window (fixed)
//
//    // Sending packets one by one with the sliding window approach
//    public void sendPackets() {
//        while (nextSeqNum < 50) { // Simulate sending 50 packets
//            // Send the packets in the current window range (windowStart to windowEnd)
//            System.out.print("Sending packets: ");
//            for (int i = windowStart; i <= windowEnd && nextSeqNum < 50; i++) {
//                System.out.print(nextSeqNum + " ");
//                nextSeqNum++;
//            }
//            System.out.println();
//
//            // Simulate waiting for ACKs (simulating perfect network without packet loss)
//            simulateReceiverAcks();
//
//            // Slide the window: The window slides forward by 1 after receiving an ACK for the sent packet
//            slideWindow();
//
//            // Simulate network delay between sending packets
//            try {
//                Thread.sleep(1000);  // Simulate network delay of 1 second
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // Simulate acknowledgment from the receiver
//    private void simulateReceiverAcks() {
//        System.out.println("Receiver sending ACKs for the packets.");
//    }
//
//    // Slide the window: After receiving ACK for a packet, move the window forward by 1
//    private void slideWindow() {
//        if (nextSeqNum <= 50) {
//            windowStart++;
//            windowEnd++;
//            System.out.println("Sliding window: [" + windowStart + " -> " + windowEnd + "]");
//        }
//    }
//}
//
//class Receiver {
//    public void receivePackets() {
//        // Simulates receiver behavior (just for acknowledgment)
//    }
//}
//
//public class TCPCongestionControl {
//    public static void main(String[] args) {
//        Sender sender = new Sender();
//        Receiver receiver = new Receiver();
//
//        // Start sender and receiver
//        new Thread(() -> sender.sendPackets()).start();
//        new Thread(() -> receiver.receivePackets()).start();
//    }
//}
class Sender {
    private static final int MAX_WINDOW_SIZE = 4;  // Max window size (constant)
    private int nextSeqNum = 0;  // Sequence number for sending packets
    private int windowStart = 0;  // Start of the window (fixed)
    private int windowEnd = MAX_WINDOW_SIZE - 1;  // End of the window (fixed)

    // Send packets one by one and slide the window
    public void sendPackets() {
        while (nextSeqNum < 50) { // Simulating sending 50 packets
            // Only send the packet at windowStart position if the sequence number is within the window
            if (nextSeqNum >= windowStart && nextSeqNum <= windowEnd) {
                System.out.print("Sending packet: " + nextSeqNum);
                nextSeqNum++;
            }

            // Simulate waiting for ACK (perfect network without packet loss)
            simulateReceiverAcks();

            // Slide the window by one position: Move windowStart and windowEnd forward by 1
            slideWindow();

            // Simulate network delay between sending packets
            try {
                Thread.sleep(1000);  // Simulate network delay of 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Simulate acknowledgment from the receiver
    private void simulateReceiverAcks() {
        System.out.println(" - Receiver sending ACK for the packet.");
    }

    // Slide the window: Move the window forward by 1 after receiving an ACK
    private void slideWindow() {
        if (nextSeqNum <= 50) {
            windowStart++;  // Slide the window by moving the windowStart forward
            windowEnd++;    // Move the windowEnd forward as well
            System.out.println("Sliding window: [" + windowStart + " -> " + windowEnd + "]");
        }
    }
}

class Receiver {
    public void receivePackets() {
        // Simulates receiver behavior (just for acknowledgment)
    }
}

public class TCPCongestionControl {
    public static void main(String[] args) {
        Sender sender = new Sender();
        Receiver receiver = new Receiver();

        // Start sender and receiver
        new Thread(() -> sender.sendPackets()).start();
        new Thread(() -> receiver.receivePackets()).start();
    }
}
