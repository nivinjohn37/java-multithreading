package SelfAssesment;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPoolImp {
    private final int poolSize;
    private final BlockingQueue<Runnable> taskQueue;
    private PoolWorker[] workers;

    public ThreadPoolImp(int poolSize) {
        this.poolSize = poolSize;
        this.taskQueue = new LinkedBlockingQueue<Runnable>(poolSize);
        this.workers = new PoolWorker[poolSize];
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
        ThreadPoolImp pool = new ThreadPoolImp(3);
        for(int i= 0; i < 10; i++) {
            pool.submit(() ->{
                System.out.println("Current Thread: " + Thread.currentThread().getName());

                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " has finished task.");

            });
        }
    }
}
