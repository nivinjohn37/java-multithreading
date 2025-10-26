package SelfAssesment.Practice;

public class BoundedBuffer<T> {
    private T[] buffer;
    private int front;
    private int rear;
    private int count;

    public BoundedBuffer(int capacity) {
        buffer = (T[]) new Object[capacity];
    }

    public synchronized void put(T element) throws InterruptedException {
        while (count == buffer.length) {
            wait();
        }
        buffer[rear] = element;
        rear = (rear + 1) % buffer.length;
        System.out.println("Put " + element + " at " + rear);
        count++;
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (count == 0) {
            wait();
        }

        T element = buffer[front];
        front = (front + 1) % buffer.length;
        System.out.println("Take " + element + " at " + front);
        count--;
        notifyAll();
        return element;
    }


    public static void main(String[] args) {
        BoundedBuffer<Integer> boundedBuffer = new BoundedBuffer<Integer>(10);
        Runnable task = () -> {
            try{
                for (int i = 0; i < 10; i++) {
                    boundedBuffer.put(i+10);
                }
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable task2 = () -> {
            try{
                for (int i = 0; i < 10; i++) {
                    int item = boundedBuffer.take();
                    System.out.println("Item - TAKE " + item + " at " + item);
                }
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread t1 = new Thread(task,"producer");
        Thread t2 = new Thread(task2,"consumer");

        t1.start();
        t2.start();
    }
}
