package exercises;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ManualScheduler {
    public static void main(String[] args) {
        List<Thread> threads = IntStream.range(0, 3)
                .mapToObj(i -> new Thread(() -> {
                    for (int j = 1; j <= 5; j++) {
                        System.out.println(Thread.currentThread().getName() + " executing " + j);
                        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                    }
                }, "T-" + i))
                .peek(Thread::start)
                .collect(Collectors.toList());

        // Manual "supervisor"
        while (threads.stream().anyMatch(Thread::isAlive)) {
            for (Thread t : threads) {
                System.out.println("Scheduler check: " + t.getName() + " state = " + t.getState());
            }
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }

        System.out.println("All threads done ✅");
    }
}
