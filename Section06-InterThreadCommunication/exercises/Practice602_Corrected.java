package exercises;

public class Practice602_Corrected {
    boolean isPingTurn = true;
    int maxTurns = 20; // For 10 pairs (20 total prints)
    int currentTurn = 0;

    public synchronized void ping() {
        while(currentTurn < maxTurns) {
            while (!isPingTurn) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Best practice
                    return; // Exit if interrupted
                }
            }
            
            // CRITICAL: Check the termination condition immediately after acquiring the lock/waking up.
            if (currentTurn >= maxTurns) {
                // If the Pong thread has finished the last turn,
                // Ping must notify Pong to let it exit its outer loop, then exit itself.
                notify(); 
                break;
            }
            
            // Execution for a valid turn
            System.out.println("Ping");
            isPingTurn = false;
            currentTurn++;
            notify(); // Wake up the Pong thread

        }
    }

    public synchronized void pong() {
        while(currentTurn < maxTurns){
            while (isPingTurn) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Best practice
                    return; // Exit if interrupted
                }
            }

            // CRITICAL: Check the termination condition immediately after acquiring the lock/waking up.
            if (currentTurn >= maxTurns) {
                // If the Ping thread has finished the last turn,
                // Pong must notify Ping to let it exit its outer loop, then exit itself.
                notify(); 
                break;
            }

            // Execution for a valid turn
            System.out.println("Pong");
            isPingTurn = true;
            currentTurn++;
            notify(); // Wake up the Ping thread
        }
    }


    public static void main(String[] args) {
        Practice602_Corrected p = new Practice602_Corrected();

        Thread t1 = new Thread(p::ping);
        Thread t2 = new Thread(p::pong);
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
            System.out.println("\n--- Program finished. Total turns: " + p.currentTurn + " ---");
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}