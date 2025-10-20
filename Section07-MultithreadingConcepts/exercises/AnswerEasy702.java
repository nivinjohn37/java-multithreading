package exercises;

import java.util.concurrent.Semaphore;

public class AnswerEasy702 {

    private static final Semaphore semaphore = new Semaphore(3);
    private static final int TOTAL_CARS = 10;

    public static void main(String[] args) {

        for (int i = 1; i <= TOTAL_CARS; i++) {
            int carId = i; // capture for lambda
            Thread car = new Thread(() -> {
                try {
                    // Check first if immediate access possible
                    if (!semaphore.tryAcquire()) {
                        System.out.printf("Car %d waiting...%n", carId);
                        semaphore.acquire(); // block until available
                    }

                    System.out.printf("Car %d entered (spots left: %d)%n",
                            carId, semaphore.availablePermits());

                    // Simulate parking duration
                    Thread.sleep((int) (Math.random() * 3000 + 1000));

                    System.out.printf("Car %d leaving (spots left: %d)%n",
                            carId, semaphore.availablePermits() + 1);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release();
                }
            }, "Car-" + carId);

            car.start();
        }
    }
}
