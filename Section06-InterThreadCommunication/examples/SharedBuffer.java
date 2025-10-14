package examples;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class SharedBuffer {
    private List<Integer> buffer = new LinkedList<>();
    private int capacity = 5;

    private synchronized void produce() throws InterruptedException {
        if (buffer.size() == capacity) {
            System.out.println("Buffer is full");
            wait();
        }

        IntStream.range(0, capacity).forEach(i -> {
                    buffer.add(i);
                    System.out.println("Produced " + i);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        notify();
    }

    private synchronized void consume() throws InterruptedException {
        if (buffer.size() < capacity) {
            System.out.println("Buffer is not full");
            wait();
        }

        while (!buffer.isEmpty()) {
            int item = buffer.remove(0);
            System.out.println("Consumed " + item);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        /*IntStream.range(0, capacity).forEach(i -> {
                    buffer.remove(buffer.size() - 1);
                    System.out.println("Consumed " + i);
                }
        );*/
        notify();

    }
    static class Producer implements Runnable {
        private SharedBuffer sharedBuffer;

        public Producer(SharedBuffer sharedBuffer) {
            this.sharedBuffer = sharedBuffer;
        }

        @Override
        public void run() {
            try {
                while(true) {
                    this.sharedBuffer.produce();
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Consumer implements Runnable {
        private SharedBuffer sharedBuffer;

        public Consumer(SharedBuffer sharedBuffer) {
            this.sharedBuffer = sharedBuffer;
        }

        @Override
        public void run() {
            try {
                while(true) {
                    this.sharedBuffer.consume();
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        var sharedBuffer = new SharedBuffer();
        Thread t1 = new Thread(new Producer(sharedBuffer));
        Thread t2 = new Thread(new Consumer(sharedBuffer));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
