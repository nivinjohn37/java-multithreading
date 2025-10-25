package SelfAssesment;

public class BoundedBufferImp<T> {
    private final T[] buffer;
    private int front;
    private int rear;
    private int count = 0;

    public BoundedBufferImp(int capacity) {
        buffer = (T[]) new Object[capacity];
    }

    public synchronized void put(T element) throws InterruptedException {
        while (count == buffer.length) {
            wait();
        }
        buffer[rear] = element;
        rear = (rear + 1) % buffer.length;
        count++;
        System.out.println("Put " + element + " at " + rear);
        Thread.sleep(2000);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (count == 0) {
            wait();
        }
        T element = buffer[front];
        front = (front + 1) % buffer.length;
        count--;
        System.out.println("Take " + element + " at " + front);
        Thread.sleep(1000);//simulating a task
        notifyAll();
        return element;
    }

    public static void main(String[] args) {
        BoundedBufferImp<Integer> boundedBuffer = new BoundedBufferImp<Integer>(10);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try{
                    boundedBuffer.put(i);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        });


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try{
                    boundedBuffer.take();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        });

        t1.start();
        t2.start();
    }

}
