## 🧩 **Section 5 — Threads Manipulation**

### 🎯 **Objectives**

By the end of this section, you’ll master:

1. How to start and control threads using both **Runnable** and **Thread** classes.
2. How to use **join()**, **sleep()**, and **yield()** effectively.
3. The difference between **user threads** and **daemon threads**.
4. How **thread priorities** and the **scheduler** influence execution order.
5. How to view and analyze active threads in the JVM.
6. When multithreading is (and isn’t) faster than sequential execution.

---

## 🧠 **Key Concepts (Section Overview)**

| Concept                  | Description                                                                        |
| ------------------------ | ---------------------------------------------------------------------------------- |
| **Runnable interface**   | Functional interface used to define a thread’s task without subclassing `Thread`.  |
| **Thread class**         | Can be extended to define a custom thread behavior.                                |
| **start()** vs **run()** | `start()` launches a new thread; `run()` just calls the method in the same thread. |
| **join()**               | Makes one thread wait for another to finish.                                       |
| **sleep()**              | Temporarily pauses a thread.                                                       |
| **yield()**              | Suggests to the scheduler to let another thread run.                               |
| **Daemon Thread**        | Background thread; JVM does *not* wait for it on shutdown.                         |
| **User Thread**          | Foreground thread; JVM keeps running until these finish.                           |
| **Thread Priority**      | Hint to the OS scheduler (1–10), *not guaranteed* execution order.                 |

---

### ⚙️ **Code Example 1 — Sequential vs Multithreaded Execution**

```java
public class SequentialVsParallel {
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        // Sequential processing
        task("Task-1");
        task("Task-2");

        long end = System.currentTimeMillis();
        System.out.println("Sequential Time: " + (end - start) + "ms");

        // Parallel processing
        Thread t1 = new Thread(() -> task("Thread-1"));
        Thread t2 = new Thread(() -> task("Thread-2"));

        start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        end = System.currentTimeMillis();

        System.out.println("Parallel Time: " + (end - start) + "ms");
    }

    static void task(String name) {
        for (int i = 1; i <= 3; i++) {
            System.out.println(name + " - step " + i);
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }
    }
}
```

💡 **Key Insight:**
On multi-core CPUs, the parallel version usually finishes faster because both threads run concurrently.
But for very short tasks, **thread creation overhead** can make it slower.

---

### ⚙️ **Code Example 2 — Daemon vs User Threads**

```java
class NormalWorker implements Runnable {
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println("NormalWorker: working " + i);
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        }
        System.out.println("NormalWorker finished.");
    }
}

class DaemonWorker implements Runnable {
    public void run() {
        while (true) {
            System.out.println("DaemonWorker: running background cleanup...");
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }
    }
}

public class DaemonExample {
    public static void main(String[] args) {
        Thread t1 = new Thread(new NormalWorker());
        Thread t2 = new Thread(new DaemonWorker());

        t2.setDaemon(true); // must be set before start()

        t1.start();
        t2.start();

        System.out.println("Main thread finished!");
    }
}
```

🧩 **Expected Output (abridged):**

```
DaemonWorker: running background cleanup...
NormalWorker: working 0
NormalWorker: working 1
NormalWorker finished.
Main thread finished!
```

> 🧠 JVM will **terminate automatically** once all *user threads* finish,
> even if daemon threads are still running.

---

### ⚙️ **Code Example 3 — Thread Priority and Scheduler**

```java
public class ThreadPriorityDemo {
    public static void main(String[] args) {
        Runnable job = () -> {
            for (int i = 0; i < 3; i++) {
                System.out.println(Thread.currentThread().getName() + " priority: " +
                        Thread.currentThread().getPriority());
            }
        };

        Thread high = new Thread(job, "High-Priority");
        Thread low = new Thread(job, "Low-Priority");
        Thread normal = new Thread(job, "Normal-Priority");

        high.setPriority(Thread.MAX_PRIORITY);   // 10
        low.setPriority(Thread.MIN_PRIORITY);    // 1
        normal.setPriority(Thread.NORM_PRIORITY); // 5

        low.start();
        normal.start();
        high.start();
    }
}
```

🧠 **Note:**
Java *hints* thread priorities to the OS scheduler,
but the **execution order is not guaranteed**.
It depends on the JVM implementation and operating system.

---

### ⚙️ **Code Example 4 — Viewing All JVM Threads**

```java
public class ListAllThreads {
    public static void main(String[] args) {
        Thread.getAllStackTraces().keySet()
                .forEach(thread -> System.out.println(thread.getName() +
                        " (Daemon: " + thread.isDaemon() + ")"));
    }
}
```

You’ll see threads like:

```
main (Daemon: false)
Reference Handler (Daemon: true)
Finalizer (Daemon: true)
Signal Dispatcher (Daemon: true)
```

💡 These are **system-level threads** created by the JVM runtime.

---

## 🧠 Interview Q&A (Section 5)

| #  | Question                                                             | Answer                                                                                                             |
| -- | -------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------ |
| 1  | What are the two main ways to create a thread?                       | (a) Extending `Thread` class (b) Implementing `Runnable`.                                                          |
| 2  | Why use Runnable instead of Thread subclassing?                      | Runnable allows multiple threads to share the same task object and avoids single inheritance limitation.           |
| 3  | What’s the difference between calling `start()` and `run()`?         | `start()` launches a new thread; `run()` executes in the current thread (no concurrency).                          |
| 4  | What does `join()` do?                                               | Waits for the specified thread to complete execution.                                                              |
| 5  | What happens if you call `join()` inside `run()` of the same thread? | It causes a deadlock because the thread waits for itself.                                                          |
| 6  | What is a daemon thread?                                             | A background service thread that runs behind user threads (like GC). JVM doesn’t wait for them to finish.          |
| 7  | When should you use a daemon thread?                                 | For background monitoring, cleanup, or non-critical tasks.                                                         |
| 8  | Can you set a thread as daemon after starting it?                    | ❌ No. You must call `setDaemon(true)` **before** `start()`.                                                        |
| 9  | What’s the default thread priority in Java?                          | `Thread.NORM_PRIORITY` = 5 (range 1–10).                                                                           |
| 10 | Does a higher priority guarantee faster execution?                   | ❌ No, it’s just a hint; scheduling depends on the OS.                                                              |
| 11 | How can you list all active threads?                                 | Using `Thread.getAllStackTraces().keySet()`.                                                                       |
| 12 | Is multithreading always faster?                                     | ❌ No — thread creation and context switching add overhead. It’s faster mainly for I/O-bound or long-running tasks. |

---

## ⚠️ Common Mistakes

* Calling `setDaemon(true)` **after** `start()` → `IllegalThreadStateException`
* Assuming priority ensures order of execution
* Forgetting `join()` → main thread exits before others complete
* Using `sleep()` for synchronization (instead of `wait()`/`notify()`)
* Forgetting exception handling around `InterruptedException`

---

## 🧾 Quick Summary

| Topic                  | Summary                                                    |
| ---------------------- | ---------------------------------------------------------- |
| **Runnable vs Thread** | Runnable preferred (cleaner, reusable, avoids inheritance) |
| **start() vs run()**   | `start()` = new thread, `run()` = same thread              |
| **join()**             | Wait for thread to finish                                  |
| **sleep()**            | Pause thread for given time                                |
| **Daemon thread**      | Background helper; JVM won’t wait                          |
| **User thread**        | Main worker; JVM waits to finish                           |
| **Priority**           | 1–10, scheduler hint, no guarantee                         |

---
