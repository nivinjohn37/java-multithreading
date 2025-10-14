package examples;

class Process{
    public void produce() throws InterruptedException{
        synchronized (this){
            System.out.println("Starting to produce");
            wait();
            System.out.println("Ending the production");
        }
    }

    public void consume() throws InterruptedException{
        Thread.sleep(1000);
        synchronized (this){
            System.out.println("Starting to consume");
            notify();
            System.out.println("After notify in consume");
        }
    }
}
public class CustomTest602 {
    public static void main(String[] args) {
        Process process = new Process();
        Thread t1 = new Thread(() ->{
            try {
                process.produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t2 = new Thread(() ->{
            try {
                process.consume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t2.start();
    }




}
