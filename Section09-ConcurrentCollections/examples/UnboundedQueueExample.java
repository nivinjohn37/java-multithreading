package examples;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class UnboundedQueueExample {

    // 1. Unbounded Queue
    // LinkedBlockingQueue is unbounded when no capacity is specified in the constructor.
    private static final LinkedBlockingQueue<String> REQUEST_QUEUE = new LinkedBlockingQueue<>();

    // 2. Simulated External Throttle/Rate Limiter
    // We only allow 5 requests per second (simulated) to enter the system.
    private static final int THROTTLE_LIMIT = 5;
    private static final AtomicInteger requestsProcessedThisSecond = new AtomicInteger(0);
    private static long startTime = System.currentTimeMillis();


    public static void main(String[] args) throws InterruptedException {
        // Executor for workers that process the queue (API processing threads)
        ExecutorService workerPool = Executors.newFixedThreadPool(3);

        // Start a few workers to constantly pull from the queue
        for (int i = 0; i < 3; i++) {
            workerPool.submit(new RequestProcessor(i));
        }

        System.out.println("Starting request simulation. Queue is UNBOUNDED.");
        System.out.println("External Throttle Limit: " + THROTTLE_LIMIT + " requests per second.\n");

        // Simulate incoming requests (Fast influx of 20 requests)
        for (int i = 1; i <= 20; i++) {
            String requestId = "Req-" + i;
            
            // Check the external throttle *before* adding to the queue
            if (isThrottled()) {
                System.out.println("⛔ THROTTLED: " + requestId + " rejected by external limit.");
                // In a real system, you'd return a 429 Too Many Requests here.
            } else {
                REQUEST_QUEUE.put(requestId);
                System.out.println("-> Added: " + requestId + ". Queue size: " + REQUEST_QUEUE.size());
            }

            // A tiny delay to make the simulation readable
            Thread.sleep(50);
        }

        // Wait for workers to finish the queued items
        TimeUnit.SECONDS.sleep(5);

        workerPool.shutdownNow();
    }

    /**
     * Simulates an external API gateway or rate limiter.
     * This is the "back pressure" stage implemented elsewhere.
     */
    private static boolean isThrottled() {
        long currentTime = System.currentTimeMillis();
        
        // Reset the counter every second
        if (currentTime - startTime >= 1000) {
            requestsProcessedThisSecond.set(0);
            startTime = currentTime;
        }

        // Check if we hit the limit
        if (requestsProcessedThisSecond.get() >= THROTTLE_LIMIT) {
            return true;
        }

        requestsProcessedThisSecond.incrementAndGet();
        return false;
    }

    /**
     * Worker thread that processes requests from the queue.
     */
    static class RequestProcessor implements Runnable {
        private final int workerId;

        public RequestProcessor(int workerId) {
            this.workerId = workerId;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // Block and wait for a request to arrive
                    String request = REQUEST_QUEUE.take(); 
                    
                    System.out.println("✅ Worker " + workerId + " processing: " + request + "...");
                    // Simulate work (e.g., calling a database)
                    TimeUnit.MILLISECONDS.sleep(500); 
                    System.out.println("  -- Finished: " + request + ". Queue size: " + REQUEST_QUEUE.size());
                }
            } catch (InterruptedException e) {
                // Thread was interrupted (on shutdown)
                Thread.currentThread().interrupt();
            }
        }
    }
}