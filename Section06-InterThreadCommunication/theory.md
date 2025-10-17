# 🧩 **Section 6 — Inter-Thread Communication (Theory + Interview Q&A)**

---

## 🎯 **1️⃣ Concept Foundations**

### 🔹 Q1. What is inter-thread communication?

> It’s the mechanism that allows multiple threads to coordinate and share work safely.
> Instead of running independently, threads communicate using shared objects and synchronization primitives like `wait()`, `notify()`, and `notifyAll()`.

✅ Example use-case:
Producer and Consumer threads sharing a queue — producer fills it, consumer empties it.

---

### 🔹 Q2. Why is inter-thread communication needed?

> Without communication, threads either:

* Work on stale data,
* Cause race conditions, or
* Waste CPU cycles through busy waiting.

Communication ensures **cooperation** rather than competition between threads.

---

### 🔹 Q3. What are `wait()`, `notify()`, and `notifyAll()` used for?

| Method        | Purpose                                                    | When Called                                             |
| ------------- | ---------------------------------------------------------- | ------------------------------------------------------- |
| `wait()`      | Temporarily releases lock and puts thread in waiting state | Inside synchronized block, when condition not satisfied |
| `notify()`    | Wakes up one waiting thread                                | After condition changes                                 |
| `notifyAll()` | Wakes up all waiting threads                               | Safer when multiple threads are waiting                 |

---

### 🔹 Q4. What are the key rules for using `wait()` and `notify()`?

1. Must be called **inside a synchronized block or method**
2. Both caller and waiter must **synchronize on the same monitor object**
3. Always use `while` around `wait()`
4. Avoid calling `notify()` before any thread is waiting
5. Prefer `notifyAll()` when multiple conditions exist

---

### 🔹 Q5. What happens internally when a thread calls `wait()`?

1. It releases the lock on the monitor object.
2. The thread enters the **WAITING** (or TIMED_WAITING) state.
3. It moves to that object’s **wait set**.
4. It stays blocked until another thread calls `notify()` or `notifyAll()` on the same object.
5. When awakened, it **reacquires the lock** before resuming.

---

### 🔹 Q6. What is a “spurious wakeup”?

> A thread can sometimes wake up from `wait()` **without any explicit `notify()`**.
> Therefore, always wrap `wait()` in a **while loop** that re-checks the condition.

---

### 🔹 Q7. Why use `while` instead of `if` before `wait()`?

* `while` re-checks condition after waking up (handles spurious wakeups).
* `if` checks once → can lead to incorrect execution if woken prematurely.

---

### 🔹 Q8. What happens if you call `wait()` or `notify()` outside a synchronized block?

> The thread does not own the monitor lock → JVM throws `IllegalMonitorStateException`.

---

## 🔐 **2️⃣ Daemon, User, and Worker Threads**

### 🔹 Q9. What is a daemon thread?

> A background thread that supports user threads and terminates automatically when all user threads finish.

**Examples:** Garbage collector, background monitor threads.

### 🔹 Q10. How is a daemon thread different from a user thread?

| Type              | Terminates when                | Example                     |
| ----------------- | ------------------------------ | --------------------------- |
| **User thread**   | Its `run()` method finishes    | Main thread, worker threads |
| **Daemon thread** | All user threads have finished | GC, JVM heartbeat threads   |

---

### 🔹 Q11. How to make a thread a daemon?

```java
Thread t = new Thread(task);
t.setDaemon(true);  // Must be called before start()
t.start();
```

---

### 🔹 Q12. What happens if we call `setDaemon(true)` after `start()`?

> JVM throws `IllegalThreadStateException` because thread state is already RUNNABLE.

---

## ⚙️ **3️⃣ Synchronization Details**

### 🔹 Q13. What are synchronized blocks and methods used for?

> To ensure **mutual exclusion** — only one thread can execute the block at a time.

```java
synchronized (lockObject) {
    // critical section
}
```

---

### 🔹 Q14. Difference between intrinsic lock and ReentrantLock?

| Feature             | Intrinsic (`synchronized`) | `ReentrantLock`           |
| ------------------- | -------------------------- | ------------------------- |
| Lock acquisition    | Automatic                  | Manual                    |
| Fairness            | No guarantee               | Can specify fair order    |
| Try-lock / timeout  | ❌                          | ✅ `tryLock(timeout)`      |
| Multiple conditions | ❌                          | ✅ via `Condition` objects |
| Recommended for     | Simpler cases              | Advanced coordination     |

---

### 🔹 Q15. What are condition variables (`await()` / `signal()`)?

> In `ReentrantLock`, `Condition` objects replace `wait()` / `notify()`.
> They allow more precise control:

```java
lock.lock();
try {
    while (!conditionMet)
        condition.await();
    condition.signalAll();
} finally {
    lock.unlock();
}
```

---

### 🔹 Q16. Difference between `wait()`/`notify()` and `await()`/`signal()`?

| Feature                      | `wait()` / `notify()` | `await()` / `signal()`    |
| ---------------------------- | --------------------- | ------------------------- |
| Works with                   | Intrinsic lock        | `ReentrantLock`           |
| Need synchronized?           | Yes                   | No (uses lock explicitly) |
| Multiple conditions per lock | No                    | Yes                       |
| Readability & flexibility    | Simpler               | More control              |

---

### 🔹 Q17. What happens if a thread holding a lock calls `sleep()`?

> It **does not release the lock** — other threads still block.
> `sleep()` only pauses the current thread’s execution, not synchronization.

---

### 🔹 Q18. Why can thread priority be ignored by the OS?

> Java thread priorities are hints to the scheduler.
> The OS may choose to ignore them depending on:

* Platform scheduling policy
* JVM implementation
* Number of cores

So priority is **not a reliable synchronization or ordering mechanism**.

---

## ⚙️ **4️⃣ Practical Design and Debug Concepts**

### 🔹 Q19. Why do we call `notifyAll()` instead of `notify()`?

> `notify()` wakes one arbitrary thread → others may starve.
> `notifyAll()` wakes all threads → everyone re-checks condition safely.

---

### 🔹 Q20. Why must we re-acquire the lock after `wait()`?

> `wait()` releases the lock when entering the wait state,
> but when resumed, the thread must reacquire the lock before continuing —
> to ensure **mutual exclusion consistency**.

---

### 🔹 Q21. What does “synchronized is non-deterministic” mean?

> The exact order in which threads acquire locks is unpredictable.
> Even with synchronization, thread scheduling is determined by the OS, not your code.

---

### 🔹 Q22. What is a missed signal problem?

> If `notify()` is called **before** the corresponding thread calls `wait()`,
> that signal is lost — the waiting thread may hang indefinitely.
> That’s why high-level constructs (like `CountDownLatch`) are safer for such cases.

---

### 🔹 Q23. What are spurious wakeups and how to prevent them?

> Threads can wake up without being notified due to OS-level issues or signals.
> Always recheck conditions inside a loop to ensure correctness:

```java
while (!condition) wait();
```

---

### 🔹 Q24. What’s the difference between cooperative and preemptive thread communication?

| Type            | Description                                      | Example                          |
| --------------- | ------------------------------------------------ | -------------------------------- |
| **Cooperative** | Threads yield control voluntarily (via `wait()`) | `wait()`/`notify()` coordination |
| **Preemptive**  | Scheduler forcibly interrupts threads            | Thread scheduling by OS          |

---

### 🔹 Q25. What is thread starvation?

> A thread never gets CPU time or lock access due to others monopolizing resources.
> Can happen with `notify()` (wrong thread woken) or unfair `ReentrantLock`.

---

### 🔹 Q26. How does `join()` work internally?

> It makes one thread wait for another to finish —
> internally implemented using `wait()`/`notify()` on the thread’s monitor.

---

### 🔹 Q27. What happens if `notify()` is called but no threads are waiting?

> Nothing happens — the signal is lost.
> Threads that call `wait()` later will not see that signal.

---

### 🔹 Q28. Can `wait()` and `sleep()` be used interchangeably?

> ❌ No.
> | Aspect | `wait()` | `sleep()` |
> |---------|-----------|-----------|
> | Releases lock? | ✅ Yes | ❌ No |
> | Needs synchronized? | ✅ Yes | ❌ No |
> | Purpose | Coordination | Pausing execution |

---

## ⚙️ **5️⃣ Real-World Interview-Style Questions**

1. **What’s the difference between polling and waiting?**
   Polling keeps checking repeatedly (wastes CPU);
   waiting (`wait()`) suspends the thread efficiently until notified.

2. **Can `wait()` be interrupted? What happens then?**
   Yes — it throws `InterruptedException`.
   Best practice: restore interrupt status via `Thread.currentThread().interrupt()`.

3. **Can a thread call `wait()` on itself?**
   Yes, but it will block forever unless another thread calls `notify()` on the same object.

4. **How does `notifyAll()` prevent deadlocks?**
   It ensures all waiting threads wake up and re-evaluate their conditions,
   preventing a single thread from starving others.

5. **Can you use `wait()` with `Lock` and `Condition`?**
   No — they belong to different synchronization mechanisms.
   Use `await()`/`signal()` instead.

---

## 🧠 **6️⃣ Conceptual Summary**

| Concept              | Description                                         |
| -------------------- | --------------------------------------------------- |
| **wait()**           | Thread gives up monitor lock and waits              |
| **notify()**         | Wakes up one waiting thread                         |
| **notifyAll()**      | Wakes up all waiting threads                        |
| **join()**           | Makes one thread wait for another to complete       |
| **yield()**          | Hints scheduler to pause current thread             |
| **sleep()**          | Pauses thread without releasing lock                |
| **Daemon thread**    | Runs in background, ends when no user threads exist |
| **Synchronized**     | Ensures mutual exclusion and visibility             |
| **Condition (Lock)** | Advanced equivalent of wait/notify                  |
| **Spurious wakeup**  | Thread wakes up without notify() — must use `while` |

