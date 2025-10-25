package SelfAssesment;

public class EvenOddPrinter {

    final int max_nums = 20;
    int current_num = 0;

    public synchronized void printEven(){
        while(current_num < max_nums){
            while(current_num < max_nums && current_num % 2 != 0){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if(current_num % 2 == 0){
                System.out.println("Thread name: " + Thread.currentThread().getName() +  " " + current_num);
                current_num++;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                notifyAll();
            }
        }
    }

    public synchronized void printOdd(){
        while(current_num < max_nums){
            while(current_num < max_nums && current_num % 2 == 0){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if(current_num % 2 != 0){
                System.out.println("Thread name: " + Thread.currentThread().getName() +  " " + current_num);
                current_num++;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        EvenOddPrinter e = new EvenOddPrinter();
        Thread evenPrinter = new Thread(e::printEven, "evenPrinter");
        Thread oddPrinter = new Thread(e::printOdd, "oddPrinter");

        evenPrinter.start();
        oddPrinter.start();

        try{
            evenPrinter.join();
            oddPrinter.join();
        }catch (InterruptedException e1){
            throw new RuntimeException(e1);
        }

        System.out.println("Completed all the prints!");
    }
}
