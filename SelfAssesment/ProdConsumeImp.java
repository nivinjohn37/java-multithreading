package SelfAssesment;

import java.util.concurrent.BlockingQueue;

public class ProdConsumeImp {
    static class Produce implements Runnable {
        private BlockingQueue<String> queue;
        public Produce(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    queue.put("Hello " + i);
                    System.out.println("Produced: " + i);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
