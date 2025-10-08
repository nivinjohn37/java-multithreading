# 🧩 Section 5 — Threads Manipulation (Theory Deep Dive)

---

## ⚙️ 1. What Is a Thread?

A **thread** is the smallest unit of execution within a process.
Every Java program starts with at least **one thread — the main thread** — created automatically by the JVM.

Multiple threads in the same process **share**:

* Code section
* Heap memory (objects)
* Open files and sockets

…but each has its own:

* **Program counter (PC)**
* **Call stack**
* **Local variables**

Threads allow **concurrent** or even **parallel** execution of tasks.

---

## ⚙️ 2. Creating Threads — Two Main Approaches

### 🔹 a) Extending the `Thread` class

```java
class MyThread extends Thread {
    public void run() {
        System.out.println("Running in: " + Thread.currentThread().getName());
    }
}
new MyThread().start();
```

* Simpler for quick examples
* But restricts you (Java doesn’t support multiple inheritance)

---

### 🔹 b) Implementing `Runnable`

```java
class MyRunnable implements Runnable {
    public void run() {
        System.out.println("Running in: " + Thread.currentThread().getName());
    }
}
new Thread(new MyRunnable()).start();
```

✅ **Preferred way** — because:

* Decouples the **task** (Runnable) from the **Thread** (executor)
* Enables reusing the same task with different threads
* Fits well with `ExecutorService` and modern concurrency utilities

---

## ⚙️ 3. `start()` vs `run()`

| Method    | What it Does                                                    | Thread Behavior   |
| --------- | --------------------------------------------------------------- | ----------------- |
| `start()` | Creates a new call stack and runs `run()` in **another thread** | Runs concurrently |
| `run()`   | Executes the method in the **same thread**                      | No concurrency    |

**Rule:** Always call `start()` — calling `run()` directly just runs sequentially.

---

## ⚙️ 4. `sleep()`, `yield()`, and `join()`

### 💤 `sleep(long ms)`

Temporarily pauses **current thread** for given milliseconds.

* Doesn’t release locks
* Other threads continue to run
* Throws `InterruptedException` if interrupted

> Used to simulate delays or control execution timing.

---

### 🤝 `join()`

Waits for another thread to finish before continuing.

```java
Thread t = new Thread(task);
t.start();
t.join(); // main waits here until t completes
```

* Makes threads run *in order* when needed
* Used for sequencing or synchronization

---

### ⚖️ `yield()`

Hints to the scheduler that current thread is willing to pause
and let other threads of equal priority run.

> Note: It’s a **hint**, not a command — OS may ignore it.

---

## ⚙️ 5. User Threads vs Worker Threads vs Daemon Threads

Let’s clarify all three (they’re often conflated in interviews).

---

### 🧩 **User Threads (a.k.a. Foreground Threads)**

* Created by default when you call `new Thread(...)`
* JVM **waits** for all user threads to finish before exiting
* Represent **main tasks** of your program (business logic, computation, etc.)

---

### ⚙️ **Daemon Threads (a.k.a. Background Threads)**

* Created explicitly via `setDaemon(true)` before `start()`
* JVM **does not wait** for them when shutting down
* Used for background maintenance (GC, cleanup, monitoring)
* If **only daemon threads** remain, JVM exits immediately

**Example:**

```java
Thread cleaner = new Thread(() -> {
    while(true) {
        System.out.println("Cleaning temporary files...");
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }
});
cleaner.setDaemon(true);
cleaner.start();
```

🧠 When `main()` (user thread) ends → JVM terminates → daemon thread stops abruptly.

---

### 🧠 **Worker Threads**

* General term for threads that **perform actual work** in a system (e.g., thread pool workers).
* They are usually **user threads** created by an `ExecutorService`.
* Not a special JVM type — just a naming convention for threads that process tasks.

**Summary:**

| Type              | JVM Waits? | Purpose            | Creation                    |
| ----------------- | ---------- | ------------------ | --------------------------- |
| **User Thread**   | ✅ Yes      | Core app logic     | Default                     |
| **Worker Thread** | ✅ Yes      | Executes tasks     | Usually via ExecutorService |
| **Daemon Thread** | ❌ No       | Background helpers | `setDaemon(true)`           |

---

## ⚙️ 6. Thread Priority

Each thread has a **priority (1–10)**:

```java
Thread.MIN_PRIORITY  = 1
Thread.NORM_PRIORITY = 5
Thread.MAX_PRIORITY  = 10
```

Set it using:

```java
thread.setPriority(Thread.MAX_PRIORITY);
```

**Purpose:**
To *suggest* to the JVM/OS scheduler which threads are more important.

---

### 🧠 Why Priorities Might Be Ignored

| Reason                       | Explanation                                                                                               |
| ---------------------------- | --------------------------------------------------------------------------------------------------------- |
| 🧩 **OS-Level Scheduling**   | The JVM delegates actual scheduling to the OS kernel — it may ignore priorities for fairness.             |
| ⚙️ **CPU Core Availability** | On multicore systems, each thread can run on its own core — so priority differences don’t visibly matter. |
| 🔄 **JVM Implementation**    | Some JVMs normalize priorities internally (e.g., mapping 1–10 to fewer native levels).                    |
| 🧍 **User Threads Dominate** | If all threads are CPU-bound and no blocking occurs, higher priority doesn’t help much.                   |

📌 **Result:** Thread priority is a “hint”, not a guarantee.
The OS may still choose to run lower-priority threads first.

---

### 🧮 Example

```java
Thread high = new Thread(job, "High");
Thread low  = new Thread(job, "Low");

high.setPriority(Thread.MAX_PRIORITY);
low.setPriority(Thread.MIN_PRIORITY);

high.start();
low.start();
```

You might *expect* `high` to always finish first,
but output often alternates or is completely random.

---

## ⚙️ 7. The Java Thread Scheduler

* Java threads are **mapped to native OS threads** (1:1 model).
* The OS kernel (not JVM) decides **which thread runs when**.
* Scheduling policy depends on platform:

    * Windows → time-slicing + priority
    * Linux → preemptive round-robin (priority & fairness based)

You **can’t directly control** scheduling, but can influence it using:

* `yield()`
* `sleep()`
* Priorities

---

## ⚙️ 8. Sequential vs Multithreaded Execution

### 🔹 Sequential

All tasks execute one after another in the same thread.

```java
task1();
task2();
```

🕑 Slower for I/O-heavy or independent operations.

---

### 🔹 Multithreaded

Tasks execute concurrently in separate threads:

```java
new Thread(() -> task1()).start();
new Thread(() -> task2()).start();
```

🧠 **Faster** if:

* Tasks are independent
* CPU has multiple cores
* Or tasks are I/O-bound (waiting on network, disk, etc.)

🧱 **Slower** if:

* Tasks are small (thread creation overhead)
* CPU is already saturated
* Threads constantly contend for shared resources

---

## ⚙️ 9. Showing All JVM Threads

```java
Thread.getAllStackTraces().keySet()
    .forEach(t -> System.out.println(t.getName() + " (Daemon: " + t.isDaemon() + ")"));
```

This shows:

```
main (Daemon: false)
Reference Handler (Daemon: true)
Finalizer (Daemon: true)
Signal Dispatcher (Daemon: true)
```

✅ Useful for debugging what threads exist in your program.

---

## ⚙️ 10. When Does the JVM Exit?

The JVM shuts down when:

1. All **user threads** have finished,
2. Or `System.exit()` is called,
3. Or a fatal error/exception stops the process.

Daemon threads are **abruptly terminated** when the last user thread finishes —
no `finally` blocks or clean shutdown guaranteed.

---

## ⚙️ 11. Thread Groups (Legacy Concept)

Before `ExecutorService`, Java grouped threads using `ThreadGroup`.
It’s mostly obsolete now but still visible in some APIs.

```java
ThreadGroup group = new ThreadGroup("MyGroup");
Thread t = new Thread(group, new Task(), "Worker");
```

Used for:

* Monitoring multiple threads together
* Setting uncaught exception handlers

Modern approach → use `Executors` or structured concurrency.

---

## ⚙️ 12. Thread Lifecycle Recap

```
NEW → RUNNABLE → RUNNING → BLOCKED/WAITING → TERMINATED
```

| State           | Meaning                                | Example Trigger    |
| --------------- | -------------------------------------- | ------------------ |
| NEW             | Thread created but not started         | `new Thread()`     |
| RUNNABLE        | Eligible to run                        | `start()`          |
| RUNNING         | Currently executing                    | CPU-scheduled      |
| WAITING/BLOCKED | Waiting for resource or another thread | `join()`, `wait()` |
| TERMINATED      | Finished execution                     | `run()` completed  |

---

## 🧾 Quick Reference Summary

| Concept              | Key Points                                   |
| -------------------- | -------------------------------------------- |
| **Runnable**         | Preferred abstraction for tasks              |
| **Thread**           | Actual OS-level thread of execution          |
| **start()**          | Creates a new call stack                     |
| **join()**           | Waits for another thread to finish           |
| **sleep()**          | Pauses current thread without releasing lock |
| **yield()**          | Suggests scheduler to switch threads         |
| **Daemon Thread**    | Background, JVM doesn’t wait for it          |
| **User Thread**      | Foreground, JVM waits for it                 |
| **Priority**         | 1–10 (hint only, may be ignored)             |
| **Scheduler**        | OS-controlled, preemptive or time-sliced     |
| **Thread Group**     | Legacy thread management structure           |
| **Thread Exit Rule** | JVM ends when no user threads remain         |

---

## 🧠 Interview Nuggets

| Question                                                   | Short Answer                                                            |
| ---------------------------------------------------------- | ----------------------------------------------------------------------- |
| What’s the default thread type?                            | User thread                                                             |
| Can a daemon thread create user threads?                   | Yes, child inherits daemon status from parent unless explicitly changed |
| What if you call `setDaemon(true)` after start()?          | `IllegalThreadStateException`                                           |
| Why might thread priority be ignored?                      | Because OS scheduler ultimately decides execution order                 |
| How do you ensure JVM exits only after background cleanup? | Use `join()` or `ExecutorService.shutdown()` instead of daemon threads  |
| When is multithreading slower than sequential?             | When tasks are short, CPU-bound, or heavily synchronized                |