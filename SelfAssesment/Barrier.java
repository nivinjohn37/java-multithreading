package SelfAssesment;

import java.util.stream.IntStream;

public class Barrier {
    private final int totalThreads;
    private int waitingThreads;

    public Barrier(int totalThreads) {
        this.totalThreads = totalThreads;
    }

    public synchronized void await() throws InterruptedException {
        waitingThreads++;
        if (waitingThreads < totalThreads) {
            System.out.println("Waiting for " + waitingThreads + " threads" + " Thread :" + Thread.currentThread().getName());
            wait();
        } else {
            waitingThreads = 0;
            notifyAll();
        }
    }

    public static void main(String[] args) {
        Barrier barrier = new Barrier(5);
        Runnable runnable = () -> {
          try{
              System.out.println("Waiting for Barrier" + Thread.currentThread().getName());
              barrier.await();
              Thread.sleep(2000);
              System.out.println("Crossed the Barrier" + Thread.currentThread().getName());
          }catch(InterruptedException e){
              e.printStackTrace();
          }
        };

        IntStream.range(0, 10).forEach(i -> new Thread(runnable, "Thread" + i).start());
    }
}
