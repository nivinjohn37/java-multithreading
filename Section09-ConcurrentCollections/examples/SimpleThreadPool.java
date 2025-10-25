package examples;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class SimpleThreadPool {
    private final int poolSize;
    private final PoolWorker[] workers;
    private final BlockingQueue<Runnable> taskQueue;

    public SimpleThreadPool(int poolSize) {
        this.poolSize = poolSize;
        taskQueue = new LinkedBlockingQueue<>();
        workers = new PoolWorker[poolSize];

        for (int i = 0; i < poolSize; i++) {
            workers[i] = new PoolWorker();
            workers[i].start();
        }
    }

    public void submit(Runnable task) throws InterruptedException {
        taskQueue.put(task);
    }

    private class PoolWorker extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    Runnable task = taskQueue.take();
                    task.run();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleThreadPool threadPool = new SimpleThreadPool(3);

        Runnable task = () -> {
            String name = Thread.currentThread().getName();
            System.out.println(name + " is executing task.");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(name + " has finished task.");
        };

        for (int i = 0; i < 10; i++) {
            threadPool.submit(task);
        }
    }
}