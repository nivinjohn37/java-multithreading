package exercises;

public class Practice6022 {
    private int turn = 0;
    private int count = 10;

    public synchronized void print(String letter, int myturn) {
        for (int j = 0; j < count; j++) {
            while (myturn != turn) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(letter + ": " + myturn + " turn" + ": Count" + j + " Thread name = " + Thread.currentThread().getName());
            turn = (turn + 1) % 3;
            notifyAll();
        }
    }

    public static void main(String[] args) {
        Practice6022 practice = new Practice6022();
        Thread t1 = new Thread(() -> {
            practice.print("A", 0);
        });
        Thread t2 = new Thread(() -> {
            practice.print("B", 1);
        });
        Thread t3 = new Thread(() -> {
            practice.print("C", 2);
        });

        t1.start();
        t2.start();
        t3.start();

        try{
            t1.join();
            t2.join();
            t3.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }


}
