package exercises;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Practice503 {
    /**
     * 🧩 🔬 Hard Level
     * Practice 508 — File Downloader Simulation
     * <p>
     * Concept: join(), coordination, daemon helper.
     * <p>
     * Task:
     * <p>
     * Create threads representing Downloaders for 3 files.
     * <p>
     * Each thread simulates a download (loop 1–10 %, sleep 200 ms).
     * <p>
     * Start a ProgressMonitor daemon thread that prints overall % completed every 300 ms.
     * <p>
     * Main waits for all downloaders with join().
     * <p>
     * Expected:
     * Monitor shows progress while downloads are active.
     * After all finish, JVM ends → monitor stops automatically.
     */

    public static void main(String[] args) {

        int fileCount = 3;
        Thread[] threads = new Thread[fileCount];
        AtomicInteger totalProgress = new AtomicInteger(0);
        int totalSteps = fileCount * 10;

        Thread monitor = new Thread(() -> {
            while (true) {
                int percent = (int)((totalProgress.get()*100.0)/totalSteps);
                System.out.println("Overall Progress: " + percent + "%");
                try{
                    Thread.sleep(300);
                }catch(InterruptedException e){
                    return;
                }
            }
        }, "Monitor");
        monitor.setDaemon(true);
        monitor.start();


        IntStream.range(0, fileCount)
                .forEach(i -> threads[i] = new Thread(() -> {
                    for(int j = 0; j < 10; j++) {
                        try{
                            Thread.sleep(200);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        totalProgress.incrementAndGet();
                        System.out.println(Thread.currentThread().getName() + ": " + totalProgress + "%");
                    }
                    System.out.println(Thread.currentThread().getName() + " finished downloading.");
                },"Thread-" + (i+1)));
        Arrays.stream(threads).forEach(Thread::start);

        Arrays.stream(threads).forEach(t -> {
            try{
                t.join();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        });
        System.out.println("✅ All downloads completed. Exiting main thread...");


    }


}
