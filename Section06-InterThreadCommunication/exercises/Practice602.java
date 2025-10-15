package exercises;

/**
 * 🟡 Medium Level (wait / notify Coordination)
 * <p>
 * Practice 602 — Ping-Pong Printer
 * <p>
 * Concept: wait(), notify(), thread coordination.
 * Task:
 * Two threads alternate printing:
 * Ping
 * Pong
 * Ping
 * Pong
 * ...
 * <p>
 * until 10 pairs are printed.
 * <p>
 * Use a shared monitor object, wait() / notify(), and a boolean flag (isPingTurn).
 * <p>
 * Expected Learning:
 * •	How threads take turns using wait/notify.
 * •	Importance of checking conditions in a while loop to avoid spurious wakeups.
 */
public class Practice602 {
    boolean isPingTurn = true;
    int maxTurns = 20;
    int currentTurn = 0;

    public synchronized void ping() {
        while(currentTurn < maxTurns) {
            while (!isPingTurn) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (currentTurn < maxTurns) {
                System.out.println("Ping");
                isPingTurn = false;
                currentTurn++;
                notify();

            }
        }



    }

    public synchronized void pong() {
        while(currentTurn < maxTurns){
            while (isPingTurn) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (currentTurn < maxTurns) {
                System.out.println("Pong");
                isPingTurn = true;
                currentTurn++;
                notify();
            }
        }
    }


    public static void main(String[] args) {
        Practice602 p = new Practice602();

        Thread t1 = new Thread(p::ping);
        Thread t2 = new Thread(p::pong);
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
