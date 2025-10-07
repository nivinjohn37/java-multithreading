package examples;

public class CustomTest401 {
    public static void main(String[] args) {
        boolean terminated = false;
        Thread a = new Thread(() -> {
            while(!terminated){
                System.out.println("print something");
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        });
        a.start();
    }
}
