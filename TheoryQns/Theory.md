## 🧠 MULTITHREADING THEORY — SET 1

---

### **1️⃣ RUN**

* “RUN” isn’t a thread *state* — it refers to the **`run()` method** inside a `Thread` or `Runnable`.
* It contains the **task logic** that the thread executes once started via `start()`.

✅ Example:

```java
public void run() {
    System.out.println("Thread running...");
}
```

> ❗Calling `run()` directly does **not** start a new thread; it just executes in the current thread.

---

### **2️⃣ RUNNABLE (State)**

* A thread enters **RUNNABLE** state **after calling `start()`**.
* In this state, the thread is **ready to run** or **currently running**, depending on CPU scheduling.
* JVM and OS decide which thread from the runnable pool gets CPU time.

---

### **3️⃣ NEW (State)**

* Thread object created but **not yet started** (`Thread t = new Thread()`).
* Thread has not begun execution.
* Transitions to **RUNNABLE** after calling `start()`.

---

### **4️⃣ WAITING (State)**

* Thread is **waiting indefinitely** for another thread to perform a specific action.
* Common causes:

    * `wait()` (object monitor)
    * `join()` without timeout
    * `LockSupport.park()`

> Example: Thread A waits for Thread B to complete.

---

### **5️⃣ TIMED_WAITING (State)**

* Similar to WAITING, but with a **timeout**.
* Common methods:

    * `sleep(time)`
    * `wait(time)`
    * `join(time)`
    * `parkNanos()`, `parkUntil()`

> Thread automatically returns to RUNNABLE after timeout expires.

---

### **6️⃣ TERMINATED (State)**

* Thread has **completed execution** or **terminated abnormally** due to an exception.
* Cannot restart a terminated thread; calling `start()` again throws `IllegalThreadStateException`.

---

## 🧩 2. Process vs Thread

| Feature           | **Process**                                    | **Thread**                                           |
| ----------------- | ---------------------------------------------- | ---------------------------------------------------- |
| **Definition**    | Independent program in execution               | Smallest unit of CPU execution inside a process      |
| **Memory**        | Has its own memory space                       | Shares memory with other threads in the same process |
| **Communication** | Via Inter-Process Communication (IPC) — slower | Uses shared memory — faster                          |
| **Crash impact**  | Crash affects only that process                | Crash can affect entire process (shared space)       |
| **Creation cost** | Heavyweight                                    | Lightweight                                          |
| **Example**       | Chrome browser, VS Code                        | Tabs in Chrome, or parallel tasks within app         |

---

## 🧩 3. What is Inter-Thread Communication?

It’s the mechanism by which **threads coordinate and share information safely** to achieve a common task.

💡 Example: Producer–Consumer problem

* Producer adds data to a buffer.
* Consumer removes data.
* Both use `wait()` and `notify()` to signal each other when the buffer is full/empty.

---

## 🧩 4. Purpose of `wait()` Method

* Used to **pause** a thread’s execution until another thread signals it via `notify()` or `notifyAll()`.
* Releases the object’s monitor (lock) and enters the WAITING state.
* Commonly used in synchronized blocks for communication.

✅ Example:

```java
synchronized (lock) {
    while (list.isEmpty()) {
        lock.wait();
    }
    consume(list.remove(0));
}
```

---

## 🧩 5. Why must `wait()` be called from a synchronized block?

Because:

* `wait()` operates on the **monitor lock** of the object.
* The calling thread must **own that monitor** before waiting.
* Otherwise, `IllegalMonitorStateException` is thrown.

✅ Correct:

```java
synchronized (obj) {
    obj.wait();
}
```

❌ Wrong:

```java
obj.wait(); // No synchronization
```

---

## 🧩 6. Advantages of Multithreading

1. **Better CPU utilization** — multiple tasks run concurrently.
2. **Improved performance** for I/O-bound programs.
3. **Responsiveness** — UI thread stays active while background threads work.
4. **Resource sharing** — threads share memory, reducing overhead.
5. **Scalability** — helps in parallelism on multi-core processors.

---

## 🧩 7. States in Thread Lifecycle

| State             | Trigger / Description                            |
| ----------------- | ------------------------------------------------ |
| **NEW**           | Thread created, not started                      |
| **RUNNABLE**      | Ready to run or running                          |
| **BLOCKED**       | Waiting to acquire a monitor lock                |
| **WAITING**       | Waiting indefinitely for another thread’s signal |
| **TIMED_WAITING** | Waiting with a timeout                           |
| **TERMINATED**    | Execution finished or exception occurred         |

🧠 **Note:** `BLOCKED` and `WAITING` are different — BLOCKED is waiting for **lock**, WAITING is waiting for **signal**.

---

## 🧩 8. Preemptive Scheduling vs Time Slicing

| Type                           | Description                                                                              | Example                   |
| ------------------------------ | ---------------------------------------------------------------------------------------- | ------------------------- |
| **Preemptive Scheduling**      | OS scheduler decides which thread runs next; higher priority threads pre-empt lower ones | Thread priorities in JVM  |
| **Time Slicing (Round Robin)** | CPU time divided into small slices; each thread gets a turn                              | Common in fair scheduling |

✅ JVM usually uses a **mix of both** depending on OS.

---

## 🧩 9. What is Context Switching?

* The process of **saving the state** of a running thread (registers, stack pointer, program counter) and **restoring** another thread’s state to resume it.
* Allows CPU to switch execution between threads.
* Context switching introduces **overhead**, but provides concurrency.

---

## 🧩 10. Thread Class vs Runnable Interface

| Aspect          | `Thread` class                       | `Runnable` interface                               |
| --------------- | ------------------------------------ | -------------------------------------------------- |
| **How**         | Extend `Thread` and override `run()` | Implement `Runnable` and pass instance to `Thread` |
| **Flexibility** | Can’t extend any other class         | Can implement multiple interfaces                  |
| **Reusability** | Tight coupling                       | Better reusability                                 |
| **Preferred**   | Rarely (for simple cases)            | ✅ Always preferred (cleaner separation)            |

✅ Example:

```java
class MyRunnable implements Runnable {
    public void run() { System.out.println("Running..."); }
}

Thread t = new Thread(new MyRunnable());
t.start();
```

---

Excellent — this is your **Multithreading Theory – Set 2 (Core + Advanced Interview Concepts)**.
Below are crisp, interviewer-style answers with clarity and real-world reasoning.

---

## 🧠 MULTITHREADING THEORY — SET 2

---

### **11. Differentiate between the Thread class and Runnable interface**

| Aspect                     | `Thread` Class                                          | `Runnable` Interface                                               |
| -------------------------- | ------------------------------------------------------- | ------------------------------------------------------------------ |
| **Definition**             | A concrete class that represents a thread of execution. | A functional interface representing a task to run inside a thread. |
| **Usage**                  | Extend `Thread` and override `run()`.                   | Implement `Runnable` and pass the instance to a `Thread` object.   |
| **Inheritance Limitation** | Cannot extend another class.                            | Allows extending another class.                                    |
| **Decoupling**             | Thread = worker + task.                                 | Runnable = task, Thread = worker. Cleaner design.                  |
| **Preferred**              | For quick testing.                                      | ✅ Best practice in real-world applications.                        |

---

### **12. What does `join()` method do?**

* Causes the **current thread** to **wait** until the thread on which it is called **finishes execution**.
* Useful for sequencing tasks.

✅ Example:

```java
t1.start();
t1.join();  // main waits until t1 completes
System.out.println("t1 finished!");
```

---

### **13. Purpose and working of `sleep()`**

* Temporarily pauses the **current thread** for a given time, without releasing any locks it holds.
* Thread transitions to **TIMED_WAITING** state, then resumes automatically.

✅ Example:

```java
Thread.sleep(1000); // pauses 1 second
```

---

### **14. Difference between `wait()` and `sleep()`**

| Feature                         | `wait()`                       | `sleep()`                        |
| ------------------------------- | ------------------------------ | -------------------------------- |
| **Lock release**                | Releases the object lock.      | Does **not** release any lock.   |
| **Defined in**                  | `java.lang.Object`             | `java.lang.Thread`               |
| **Used for**                    | Inter-thread communication     | Pausing execution                |
| **Requires synchronized block** | ✅ Yes                          | ❌ No                             |
| **Wake-up**                     | Via `notify()` / `notifyAll()` | Automatically after time expires |

---

### **15. Can we call `run()` instead of `start()`?**

* Yes, but it won’t start a **new thread**.
* It will execute `run()` like a normal method on the current thread.

✅ `start()` → new call stack → true multithreading
❌ `run()` → same call stack → sequential execution

---

### **16. What about daemon threads?**

* **Background service threads** that run to support user threads (e.g., garbage collector).
* JVM exits when **all user threads** finish — daemon threads terminate automatically.

✅ Example:

```java
Thread t = new Thread(task);
t.setDaemon(true);
t.start();
```

---

### **17. Two ways to implement threads in Java**

1. **By extending `Thread`**

   ```java
   class MyThread extends Thread {
       public void run() { ... }
   }
   new MyThread().start();
   ```

2. **By implementing `Runnable`**

   ```java
   class MyRunnable implements Runnable {
       public void run() { ... }
   }
   new Thread(new MyRunnable()).start();
   ```

---

### **18. Difference between `notify()` and `notifyAll()`**

| Method          | Effect                                                                            |
| --------------- | --------------------------------------------------------------------------------- |
| **notify()**    | Wakes up **one random waiting thread** on that object's monitor.                  |
| **notifyAll()** | Wakes up **all waiting threads**, but only one proceeds after acquiring the lock. |

> ✅ Use `notifyAll()` in most production code to prevent starvation.

---

### **19. What is a "thread" in Java programming?**

A **thread** is the smallest independent unit of execution within a process.
Each thread has its own **stack** but shares **heap memory** and system resources with other threads in the same process.

---

### **20. Fundamental advantages of multithreading in Java**

1. **Improved performance** through parallelism
2. **Better CPU utilization**
3. **Faster I/O operations**
4. **Responsive UI and background processing**
5. **Efficient resource sharing**

---

### **21. Fundamental difference between a process and a thread**

| Aspect            | Process                     | Thread                         |
| ----------------- | --------------------------- | ------------------------------ |
| **Memory**        | Own memory space            | Shared memory space            |
| **Creation cost** | High                        | Low                            |
| **Crash impact**  | Isolated                    | Affects all threads in process |
| **Communication** | Inter-Process Communication | Shared objects / memory        |
| **Speed**         | Slower                      | Faster                         |

---

### **22. What is inter-thread communication?**

It’s the mechanism that allows threads to **cooperate and coordinate** their actions using shared resources safely.

> Achieved using `wait()`, `notify()`, and `notifyAll()`.

---

### **23. Functions used for inter-thread communication**

1. `wait()` – thread waits and releases monitor lock
2. `notify()` – wakes up one waiting thread
3. `notifyAll()` – wakes up all waiting threads

All are defined in `java.lang.Object`.

---

### **24. What do you understand about the `wait()` function?**

* Causes the current thread to release the object’s monitor and **wait** until another thread calls `notify()` or `notifyAll()` on the same object.
* Must be called from a **synchronized context**.

---

### **25. What is context switching?**

The process of **saving** the state of the current thread (registers, stack pointer, PC) and **loading** another thread’s state so CPU can resume it.

> Allows concurrency but incurs a performance cost.

---

### **26. Function of the `join()` method**

Same as Q 12 — makes one thread **wait for another** to complete before continuing.
Useful for dependent thread execution.

---

### **27. Function of the `sleep()` method**

Same as Q 13 — pauses execution for a fixed time **without releasing locks**, enabling timed waiting or simulation of delay.

---

### **28. What is a Deadlock situation in Java Multithreading?**

A state where **two or more threads** are waiting for each other to release resources, causing all of them to be stuck permanently.

✅ Example:

```java
Thread 1: lock A → wait for B
Thread 2: lock B → wait for A
```

---

### **29. How to detect deadlocks in Java**

1. **Thread Dump** — using `jstack <pid>` or `jconsole`.
   JVM highlights *“Found one Java-level deadlock”*.
2. **ThreadMXBean** API:

   ```java
   ManagementFactory.getThreadMXBean().findDeadlockedThreads();
   ```

---

### **30. How can deadlocks be avoided?**

✅ **Prevention Strategies:**

1. Acquire locks in a **fixed global order**.
2. Use **tryLock(timeout)** to avoid indefinite waiting.
3. Prefer **higher-level concurrency utilities** (`java.util.concurrent` — `ReentrantLock`, `Semaphore`, `ExecutorService`).
4. Minimize **nested locking** and long lock holding.
5. Apply **lock hierarchy** and proper design (no circular dependencies).

---
Brilliant — you’re systematically covering the **entire multithreading theory spectrum** up to advanced concurrency utilities.
Here’s your **Multithreading Theory – Set 3 (Advanced + JVM + Concurrency Utilities)** with professional-grade, interview-style explanations.

---

## 🧠 MULTITHREADING THEORY — SET 3

---

### **31. How do threads communicate with each other?**

Threads communicate via **shared objects in heap memory**.
They coordinate by using:

* `wait()` → makes a thread wait
* `notify()` / `notifyAll()` → wakes up waiting threads

These methods enable **inter-thread communication** by signaling between producer–consumer, reader–writer, etc.

---

### **32. Can two threads execute two methods (static and non-static) concurrently?**

✅ **Yes, they can — if the methods use different locks.**

* A **non-static synchronized** method locks the *object instance* (`this`).
* A **static synchronized** method locks the *class object* (`ClassName.class`).

Hence, both locks are **independent** — threads can execute them concurrently.

---

### **33. Purpose of the `finalize()` method**

* Defined in `java.lang.Object`.
* Called by the **Garbage Collector** before reclaiming the object’s memory.
* Used to perform cleanup (close resources, release handles).

⚠️ Deprecated from Java 9 — replaced by `Cleaner` and `try-with-resources`.

---

### **34. Synchronized Method vs Synchronized Block**

| Aspect               | Synchronized Method            | Synchronized Block                   |
| -------------------- | ------------------------------ | ------------------------------------ |
| **Scope**            | Locks the entire method        | Locks only specific code section     |
| **Lock granularity** | Coarse                         | Fine                                 |
| **Performance**      | Lower (locks the whole method) | Higher (locks minimal region)        |
| **Preferred**        | ❌ Only for simplicity          | ✅ For performance and better control |

✅ **Preferred:** Synchronized block, e.g.:

```java
synchronized(lock) {
   // critical section
}
```

---

### **35. What is Livelock? What happens when it occurs?**

A **livelock** occurs when threads are **not blocked** but keep **changing their state in response to each other**, preventing progress.

💡 Think of two people trying to pass each other in a hallway — both move aside repeatedly, never getting through.

✅ Detected via thread monitoring tools.
✅ Avoided by introducing **random back-off** or **retries with delay**.

---

### **36. What is `BlockingQueue`?**

A **thread-safe queue** that **blocks** when:

* The queue is full (producers wait)
* The queue is empty (consumers wait)

✅ Used in producer–consumer designs.
✅ Found in `java.util.concurrent` package.
Examples:

* `ArrayBlockingQueue`
* `LinkedBlockingQueue`
* `PriorityBlockingQueue`
* `SynchronousQueue`

---

### **40. What are `CyclicBarrier` and `CountDownLatch`?**

| Feature      | **CountDownLatch**                                         | **CyclicBarrier**                                                           |
| ------------ | ---------------------------------------------------------- | --------------------------------------------------------------------------- |
| **Purpose**  | Waits for *N* threads/tasks to complete before continuing. | Waits for *N* threads to reach a *common barrier point* before all proceed. |
| **Reusable** | ❌ One-time use                                             | ✅ Reusable after barrier trips                                              |
| **Package**  | `java.util.concurrent`                                     | `java.util.concurrent`                                                      |

✅ Example:

```java
CountDownLatch latch = new CountDownLatch(3);
latch.countDown(); latch.await();
```

```java
CyclicBarrier barrier = new CyclicBarrier(3);
barrier.await();
```

---

### **41. What do you mean by inter-thread communication?**

Same as Q22 — It’s the **mechanism enabling threads to cooperate** safely using `wait()`, `notify()`, and `notifyAll()` on shared monitors.

---

### **42. What is Thread Scheduler and Time Slicing?**

* **Thread Scheduler:** Part of JVM + OS that decides **which thread to run** next based on:

    * Priority
    * OS-level scheduling algorithm
* **Time Slicing:** CPU time is divided into small slices; each thread gets a fair share.

💡 JVM’s scheduling behavior depends on the OS — not strictly defined by Java.

---

### **43. What is a Shutdown Hook?**

A **shutdown hook** is a thread that executes when:

* The JVM shuts down normally or abnormally.

Used for cleanup operations (closing files, saving logs).

✅ Example:

```java
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    System.out.println("Cleanup before exit...");
}));
```

---

### **44. What is Busy Spinning?**

Busy spinning means a thread **repeatedly checks a condition in a loop** without releasing CPU — instead of blocking or sleeping.

✅ Used in low-latency systems to **avoid context-switch overhead**.
⚠️ Not CPU efficient — use only in performance-critical scenarios.

Example:

```java
while (!conditionMet) {
    // spin
}
```

---

### **45. ConcurrentHashMap vs Hashtable**

| Feature               | **ConcurrentHashMap**                                        | **Hashtable**                         |
| --------------------- | ------------------------------------------------------------ | ------------------------------------- |
| **Locking mechanism** | Uses **segment-based / bucket-level** locking (fine-grained) | Entire map synchronized (coarse lock) |
| **Concurrency**       | Multiple threads can read/write simultaneously               | Only one thread at a time             |
| **Nulls allowed**     | ❌ No null keys or values                                     | ❌ No nulls                            |
| **Performance**       | ✅ High, non-blocking reads                                   | ❌ Low, full lock on map               |
| **Introduced in**     | Java 5 (as part of `java.util.concurrent`)                   | JDK 1.0                               |

✅ **ConcurrentHashMap is faster** because it reduces contention using **lock striping** and non-blocking reads.

---

### **46. Explain Thread Priority**

* Each thread has a **priority (1–10)** that hints the scheduler which thread to prefer.
* Default priority = `Thread.NORM_PRIORITY (5)`.
* Higher priority threads may get more CPU time (not guaranteed).

✅ Example:

```java
t1.setPriority(Thread.MAX_PRIORITY);
```

---

### **47. What is a ThreadLocal variable?**

* Provides **thread-confined variables** — each thread gets its **own copy**.
* Used for user sessions, transaction contexts, etc.

✅ Example:

```java
ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);
counter.set(counter.get() + 1);
```

⚠️ Must call `remove()` to avoid memory leaks (especially in thread pools).

---

### **48. What is a Semaphore?**

A **synchronization aid** that controls access to a resource through **permits**.

* `acquire()` → takes a permit (may block if none available)
* `release()` → returns a permit

✅ Example:

```java
Semaphore sem = new Semaphore(3); // max 3 threads
sem.acquire();
try { /* work */ } finally { sem.release(); }
```

Types:

* **Binary Semaphore (1 permit)** — similar to mutex
* **Counting Semaphore (n permits)** — allows multiple access

---

### **49. What is Thread Group? Why not use it?**

* `ThreadGroup` is a legacy class for grouping threads hierarchically.
* Provides APIs for managing threads as a unit.

⚠️ **Not recommended** because:

* Lacks modern synchronization control.
* Unsafe for concurrent modification.
* Replaced by `ExecutorService`, `ForkJoinPool`, etc.

✅ Better alternatives: `ThreadPoolExecutor`, `Executors`, `CompletableFuture`.

---

### **50. What happens if we don’t override the `run()` method?**

* The thread runs, but nothing happens — `run()` in `Thread` is empty by default.
* No task logic will be executed.

✅ Example:

```java
Thread t = new Thread();
t.start(); // Thread runs but does nothing
```

If you override `run()` → custom code executes in the thread.

---

## 🧩 BONUS SUMMARY TABLE

| Concept         | Key Class / Method                             | Purpose                |
| --------------- | ---------------------------------------------- | ---------------------- |
| Communication   | `wait()`, `notify()`, `notifyAll()`            | Signal coordination    |
| Synchronization | `synchronized`, `Lock`, `Condition`            | Mutual exclusion       |
| Coordination    | `CountDownLatch`, `CyclicBarrier`, `Semaphore` | Task sequencing        |
| Thread Control  | `sleep()`, `join()`, `yield()`                 | Execution control      |
| Thread Safety   | `volatile`, `Atomic*`, `ThreadLocal`           | Memory & visibility    |
| Concurrency     | `ExecutorService`, `ConcurrentHashMap`         | High-level parallelism |

---

