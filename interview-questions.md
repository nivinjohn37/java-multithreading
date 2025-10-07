Absolutely 💪 — let’s consolidate **all interview questions + detailed answers**
up to and including **Section 4: Multithreading Theory**.
This gives you a crisp, senior-level review for interview prep.

---

# 🧠 Java Multithreading — Interview Q&A (Sections 1 → 4)

---

## 🧩 **Section 1 — Introduction**

### **1. What is multithreading?**

> **Answer:**
> Multithreading is the ability of a program to execute **multiple threads concurrently** within the same process.
> Threads share memory and resources but run independently, improving throughput and responsiveness.

---

### **2. What is the difference between concurrency and parallelism?**

| Term            | Description                                  | Example                                              |
| --------------- | -------------------------------------------- | ---------------------------------------------------- |
| **Concurrency** | Doing *many things at once* (task-switching) | Multiple threads sharing one core                    |
| **Parallelism** | Doing *many things simultaneously*           | Threads running truly in parallel on multi-core CPUs |

---

### **3. How is Java multithreading achieved?**

> Through the **`java.lang.Thread`** class and the **`java.lang.Runnable`** interface.
> You create a `Thread` object and call `.start()` to run code concurrently.

---

### **4. What are the advantages of multithreading?**

* Better CPU utilization
* Faster response in interactive programs
* Easier modeling of asynchronous or independent tasks
* Scalability on multi-core systems

---

## ⚙️ **Section 2 — Environment Setup**

### **5. What JDK/JVM settings impact threading?**

* **JVM options** such as `-Xss` (thread-stack size)
* **OS thread limits** (number of native threads)
* Libraries like `Executors`, `ForkJoinPool` use JVM-managed worker threads

---

### **6. What are key thread APIs to know early?**

* `start()` — begin thread execution
* `run()` — thread task code (don’t call directly)
* `sleep(ms)` — pause current thread
* `join()` — wait for another thread to finish
* `interrupt()` — signal a thread to stop
* `setName()`, `setPriority()`, `isAlive()`

---

## 🧩 **Section 3 — Multithreading Overview**

### **7. What are the main ways to create a thread in Java?**

1. **Extend `Thread` class:**

   ```java
   class MyThread extends Thread {
       public void run() { /* task */ }
   }
   new MyThread().start();
   ```
2. **Implement `Runnable`:**

   ```java
   new Thread(() -> {/* task */}).start();
   ```
3. **Using `ExecutorService`:** (recommended for real apps)

   ```java
   Executors.newFixedThreadPool(5).submit(() -> {/* task */});
   ```

---

### **8. Why call `start()` and not `run()` directly?**

> `start()` creates a **new call stack** and executes `run()` in a new thread.
> Calling `run()` directly executes synchronously in the current thread (no concurrency).

---

### **9. What is the difference between a user thread and a daemon thread?**

| Type              | Description                                           | Example                            |
| ----------------- | ----------------------------------------------------- | ---------------------------------- |
| **User Thread**   | Keeps JVM alive until it finishes                     | `main`, business logic threads     |
| **Daemon Thread** | Background helper, JVM exits when only daemons remain | Garbage Collector, monitor threads |

Use `setDaemon(true)` before starting a thread.

---

### **10. What is thread priority in Java?**

> An **integer (1–10)** hint to the scheduler.
> Higher priority *may* get more CPU time, but scheduling is **not guaranteed**.
> It depends on the underlying OS scheduler.

---

## ⚙️ **Section 4 — Multithreading Theory**

### **11. What is a process?**

> A **program in execution** with its own memory space, file handles, and system resources.
> Processes are isolated from each other by the OS.

---

### **12. What is a thread?**

> The **smallest unit of CPU execution** within a process.
> All threads in the same process share heap memory and open resources.

---

### **13. Difference between process and thread?**

| Feature       | Process                  | Thread                             |
| ------------- | ------------------------ | ---------------------------------- |
| Memory        | Has its own memory space | Shares memory with process threads |
| Communication | Expensive (IPC)          | Cheap (shared memory)              |
| Overhead      | Heavy                    | Lightweight                        |
| Failure       | Independent              | May crash other threads            |

---

### **14. What is the time-slicing algorithm?**

> A **CPU scheduling** technique where each runnable thread gets a small fixed time-slice (quantum).
> After its slice, the scheduler switches to the next thread, creating an illusion of simultaneous execution.

---

### **15. Who controls time-slicing — JVM or OS?**

> The **Operating System** decides when to pause/resume threads.
> Java threads are mapped to **native OS threads** (1:1 model).

---

### **16. What are the benefits of multithreading?**

* Concurrency and responsiveness
* Efficient CPU utilization
* Scalability on multi-core processors
* Easier asynchronous design (background I/O, event handling)

---

### **17. What are the downsides of multithreading?**

* Race conditions & inconsistent data
* Deadlocks and livelocks
* Context-switch overhead
* Difficult debugging and testing
* Non-deterministic behavior (hard to reproduce issues)

---

### **18. What is a race condition?**

> A bug where **multiple threads access shared data** concurrently and at least one modifies it, causing unpredictable results.

Example:

```java
count++; // not atomic → race condition
```

Fix: use synchronization, locks, or atomic variables.

---

### **19. What are deadlock and livelock?**

* **Deadlock:** Threads wait on each other’s locks forever.
* **Livelock:** Threads keep changing states in response to each other but make no progress.

---

### **20. What are the thread states in Java?**

```
NEW → RUNNABLE → RUNNING → BLOCKED/WAITING → TERMINATED
```

Access via `Thread.getState()`.

---

### **21. Difference between `sleep()`, `wait()`, and `yield()`**

| Method      | Belongs to | Releases Lock? | Purpose                                         |
| ----------- | ---------- | -------------- | ----------------------------------------------- |
| `sleep(ms)` | `Thread`   | ❌ No           | Pause current thread for given time             |
| `wait()`    | `Object`   | ✅ Yes          | Pause until notified (`notify()`/`notifyAll()`) |
| `yield()`   | `Thread`   | ❌ No           | Hint to scheduler to switch to another thread   |

---

### **22. What is context switching?**

> The act of the CPU saving the state of one thread and loading another’s state to resume execution.
> Frequent switching increases overhead and reduces performance.

---

### **23. How do you simulate time-slicing manually in Java?**

> By dividing work into small chunks and calling `Thread.sleep(fixedInterval)` in each thread iteration to mimic CPU rotation.

---

### **24. Why does calling `sleep()` not delay `System.out.println()` output?**

> Because the `println` executes **before** the `sleep()` call.
> The sleep only pauses *subsequent* operations inside that thread.

---

### **25. What happens if you call `join()` on a thread?**

> The current thread waits until the target thread finishes execution.

---

## ⚡ Common Mistakes to Mention

* Calling `run()` instead of `start()`
* Forgetting synchronization on shared data
* Assuming thread scheduling order
* Overusing threads → performance drop
* Not handling `InterruptedException`

---

## 🧾 Quick Summary Sheet

| Topic           | Remember                                            |
| --------------- | --------------------------------------------------- |
| Thread creation | Extend Thread / Implement Runnable / Executors      |
| Start vs Run    | `start()` → new thread, `run()` → same thread       |
| Scheduler       | Controlled by OS                                    |
| Time-slicing    | Small CPU turns for fairness                        |
| Thread states   | NEW, RUNNABLE, RUNNING, WAITING/BLOCKED, TERMINATED |
| Communication   | `wait()/notify()`                                   |
| Sleep           | Suspends current thread only                        |
| Join            | Wait for another thread to finish                   |
