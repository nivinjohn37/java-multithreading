package examples;

class VolatileFix {
    private static volatile boolean running = true;

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            while (running) {
                System.out.println(Thread.currentThread().getId());
            }  // sees latest value always
        });
        t.start();

        Thread.sleep(1000);
        running = false;
        System.out.println("Flag changed to false");
    }
}