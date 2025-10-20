package examples;

import java.util.concurrent.Semaphore;

public class SignalExample {
    private static final Semaphore signal = new Semaphore(0); // starts closed

    public static void main(String[] args) {
        Thread sender = new Thread(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("Sender: sending signal...");
                signal.release(); // signal that something is ready
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread receiver = new Thread(() -> {
            try {
                System.out.println("Receiver: waiting for signal...");
                signal.acquire(); // waits for the sender to release
                System.out.println("Receiver: got signal, proceeding...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        receiver.start();
        sender.start();
    }
}