package examples;

public class CustomTest601 {
    private int counter1 = 0;
    private int counter2 = 0;

    public void incrementCounter1() {
        synchronized (this) {
            counter1++;
        }

    }

    public void incrementCounter2() {
        synchronized (this) {
            counter2++;
        }
    }

    public static void main(String[] args) {
        CustomTest601 c = new CustomTest601();

        //Just to test the syntax
        Runnable r = c ::incrementCounter1;
        Thread testThread = new Thread(r);
        Runnable r2 = () ->{
            System.out.println("Test runnable");
        };

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                c.incrementCounter1();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                c.incrementCounter2();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Counter 1: " + c.counter1);
        System.out.println("Counter 2: " + c.counter2);
    }
}
