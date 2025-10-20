package exercises;

public class Practice603_Custom {

    private int current = 1;
    private final int MAX = 30;

    public synchronized void printOddNotDiv3() {
        while (current <= MAX) {
            while (current <= MAX && !(current % 2 != 0 && current % 3 != 0)) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (current <= MAX) {
                System.out.println(Thread.currentThread().getName() + ": " + current);
                current++;
                notifyAll();
            }
        }
    }

    public synchronized void printEven() {
        while (current <= MAX) {
            while (current <= MAX && current % 2 != 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (current <= MAX) {
                System.out.println(Thread.currentThread().getName() + ": " + current);
                current++;
                notifyAll();
            }
        }
    }

    public synchronized void printDivisibleBy3() {
        while (current <= MAX) {
            while (current <= MAX && current % 3 != 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (current <= MAX) {
                System.out.println(Thread.currentThread().getName() + ": " + current);
                current++;
                notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        Practice603_Custom printer = new Practice603_Custom();

        Thread t1 = new Thread(printer::printOddNotDiv3, "OddNotDiv3");
        Thread t2 = new Thread(printer::printEven, "Even");
        Thread t3 = new Thread(printer::printDivisibleBy3, "Div3");

        t1.start();
        t2.start();
        t3.start();
    }
}