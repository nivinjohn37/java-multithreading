package exercises;

public class Practice510 {
    /**
     * Practice 510 — Coordinated Startup
     *
     * Concept: join(), dependency management.
     *
     * Scenario:
     * You have three services:
     *
     * Database Service
     *
     * Cache Service
     *
     * Application Service (must start only after DB & Cache are up)
     *
     * Task:
     *
     * Each service is a thread simulating startup time (sleep).
     *
     * Use join() so that Application starts only after both others complete.
     */

    public static void main(String[] args) {
        Thread databaseThread = startThreadAndSleep("database");
        Thread cacheThread = startThreadAndSleep("cache");
        Thread appThread = startThreadAndSleep("application");
        databaseThread.start();
        cacheThread.start();
        try {
            databaseThread.join();
            cacheThread.join();
            appThread.start();
            appThread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Application Started with all the services boot up");
    }

    private static Thread startThreadAndSleep(String threadName) {
        final int SLEEP = 3000;

        return new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " is starting");
            try{
                Thread.sleep(SLEEP);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " is running");
        }, threadName);
    }
}
