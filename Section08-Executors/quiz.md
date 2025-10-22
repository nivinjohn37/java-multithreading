Absolutely ✅ — here’s your **complete curated problem set for Section 8: “Creating Threads with Executors”**, designed to lock in mastery of how thread pools, executors, and tasks actually work.

This follows your usual format — **Easy → Medium → Hard**, each one with a learning goal and hint for self-discovery.
All problems are based on *real-world concurrency design patterns* and can go directly into your `Section8/exercises/` folder.

---

## 🧩 **EASY LEVEL**

---

### **🟢 Practice 801 — Sequential Task Runner**

**🎯 Concept:** SingleThreadExecutor, ordered task execution
**🧠 Task:**
Create a `SingleThreadExecutor` that runs 3 tasks sequentially:

```text
Task 1: Print numbers 1-5  
Task 2: Print letters A-E  
Task 3: Print stars * five times
```

Expected output (always ordered):

```
1 2 3 4 5  
A B C D E  
* * * * *
```

**Hint:** submit all tasks to the same executor; shut down afterward.
**Learning:** Understand that tasks run one-after-another, even though they’re submitted asynchronously.

---

### **🟢 Practice 802 — Background Logger**

**🎯 Concept:** Decoupling submission vs execution
**🧠 Task:**
Implement a background logger using a `SingleThreadExecutor`.
Main thread accepts user input lines (simulate with random strings) and submits each line as a log task.
The logger thread writes them to console in order:

```
[Logger] Writing log: user 1
[Logger] Writing log: user 2
...
```

Stop after 10 logs, then gracefully shut down.
**Learning:** Asynchronous, ordered, thread-safe operations using a single worker.

---

## ⚙️ **MEDIUM LEVEL**

---

### **🟡 Practice 803 — Multi-Worker File Processor**

**🎯 Concept:** FixedThreadPool, task parallelism
**🧠 Task:**
Simulate 10 files being “processed” by 4 threads using `Executors.newFixedThreadPool(4)`.
Each task should:

```java
System.out.println(Thread.currentThread().getName() + " → Processing File " + id);
Thread.sleep(1000);
```

Verify that at most 4 threads run in parallel.
**Bonus:** Record the start/end time to see how thread reuse shortens total runtime.
**Learning:** Controlled concurrency & thread reuse.

---

### **🟡 Practice 804 — Rejected Task Handler**

**🎯 Concept:** Bounded queue + RejectedExecutionHandler
**🧠 Task:**
Create a `ThreadPoolExecutor` with:

```java
core=2, max=2,
queue capacity=3,
handler=CallerRunsPolicy
```

Submit 10 tasks (each sleeps 1 s).
Observe how, once the pool and queue are full, the submitting thread itself executes tasks — slowing down submission.
**Learning:** How `CallerRunsPolicy` applies natural back-pressure.

---

## 🔬 **HARD LEVEL**

---

### **🔴 Practice 805 — Dynamic API Simulator (Cached Thread Pool)**

**🎯 Concept:** CachedThreadPool, elastic scaling
**🧠 Task:**
Simulate 20 incoming API requests (threads submitting jobs).
Use a `CachedThreadPool` where each task sleeps 500 ms to represent network I/O.
Observe how new threads are spawned rapidly, and then reused if you add another batch after 5 seconds.
**Bonus:** Log the active thread count before and after idle time using:

```java
((ThreadPoolExecutor)executor).getPoolSize()
```

**Learning:** How Cached pools grow and shrink automatically.

---

### **🔴 Practice 806 — Periodic Metrics Collector (Scheduled Executor)**

**🎯 Concept:** ScheduledThreadPool, fixed-rate scheduling
**🧠 Task:**
Use a `ScheduledExecutorService` with 2 threads.
Schedule:
1️⃣ CPU usage simulation every 1 second.
2️⃣ Memory usage simulation every 3 seconds.
After 10 seconds, cancel all tasks and shut down.
Output example:

```
[Metrics-1] CPU Usage: 54%
[Metrics-2] Memory Usage: 71%
...
```

**Bonus:** Demonstrate the difference between `scheduleAtFixedRate()` and `scheduleWithFixedDelay()`.
**Learning:** Reliable periodic scheduling and graceful cancellation.

---

## 💡 **BONUS CHALLENGES**

### **⚡ Bonus 1 — Graceful Shutdown Test**

Start a FixedThreadPool with 5 tasks, each sleeping 2 seconds.
Call `shutdown()` immediately after submission and confirm that all tasks finish before JVM exit.
Then replace with `shutdownNow()` and see the difference.

---

### **⚡ Bonus 2 — Callable + Future Aggregator**

Use a `FixedThreadPool(3)` and submit 5 `Callable<Integer>` tasks that each return a random number.
Collect all `Future`s and compute their sum once all finish.

---

### **⚡ Bonus 3 — Custom ThreadFactory**

Implement a `ThreadFactory` that names threads like:

```
Worker-pool-A-thread-1
Worker-pool-A-thread-2
```

Use it in a FixedThreadPool and print each thread’s name during execution.
**Learning:** Control over thread identity and monitoring.

---

## 🧠 **Summary Table**

| Level      | Topic                    | Concept Focus                              |
| ---------- | ------------------------ | ------------------------------------------ |
| Easy 801   | SingleThreadExecutor     | Sequential background execution            |
| Easy 802   | Async Logger             | Task decoupling                            |
| Medium 803 | FixedThreadPool          | Controlled parallelism                     |
| Medium 804 | Bounded Pool + Rejection | Backpressure & flow control                |
| Hard 805   | CachedThreadPool         | Elastic scalability                        |
| Hard 806   | ScheduledThreadPool      | Timed/periodic execution                   |
| Bonus 1-3  | Advanced Patterns        | Graceful shutdown, Callable, ThreadFactory |

---

Would you like me to generate the **ready-to-run Java skeleton files** (`Practice801.java` … `Practice806.java`) in clean template format for your `Section8/exercises` folder next?
