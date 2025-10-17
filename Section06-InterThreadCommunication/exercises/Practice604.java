package exercises;

/**
 * 🧩 Hard Problem 1 — Using synchronized + wait()/notifyAll()
 * Problem: Alternating File Writer and Logger
 * 💡 Concept
 * <p>
 * Classic producer–consumer, but with ordered dependencies —
 * the FileWriter thread should write a line first,
 * and then the Logger thread should log that write event immediately after.
 * They must strictly alternate:
 * <p>
 * FileWriter: Wrote line 1
 * Logger: Logged line 1
 * FileWriter: Wrote line 2
 * Logger: Logged line 2
 * ...
 * <p>
 * 🎯 Task
 * <p>
 * Use two threads: writer and logger
 * <p>
 * Use a shared monitor object with wait()/notifyAll()
 * <p>
 * Ensure perfect alternation between threads
 * <p>
 * Stop after, say, 10 iterations
 * <p>
 * 🔍 Requirements
 * <p>
 * Use a boolean isWriterTurn = true flag
 * <p>
 * Writer waits when it’s not its turn
 * <p>
 * Logger waits when it’s not its turn
 * <p>
 * Each prints its message, toggles the flag, and notifyAll()s the other thread
 * <p>
 * 🧠 What the interviewer is testing
 * <p>
 * Mastery of turn-based coordination
 * <p>
 * Understanding of condition-based waiting
 * <p>
 * Correct use of while around wait()
 * <p>
 * Awareness of spurious wakeups
 * <p>
 * Clean exit logic after the loop
 * <p>
 * ✨ Example Output
 * Writer: Wrote line 1
 * Logger: Logged line 1
 * Writer: Wrote line 2
 * Logger: Logged line 2
 * ...
 */
public class Practice604 {
    private boolean isWriterTurn = true;
    private int currentTurn = 1;
    private int maxTurns = 20;

    public synchronized void write() {
        while (currentTurn <= maxTurns) {
            while (!isWriterTurn) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (currentTurn <= maxTurns) {
                System.out.println("Writer: Wrote line " + currentTurn);
                isWriterTurn = false;
                notify();
            }
        }
    }

    public synchronized void log() {
        while (currentTurn <= maxTurns) {
            while (isWriterTurn) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (currentTurn <= maxTurns) {
                System.out.println("Logger: Logged line " + currentTurn);
                isWriterTurn = true;
                currentTurn++;
                notify();
            }
        }
    }

    public static void main(String[] args) {
        Practice604 practice604 = new Practice604();

        Thread t1 = new Thread(practice604::write);
        Thread t2 = new Thread(practice604::log);
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
