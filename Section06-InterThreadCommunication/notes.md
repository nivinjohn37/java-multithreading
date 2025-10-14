# 🧩 **Section 6 — Inter-Thread Communication (Full Theory)**

---

## 🔹 1. Why Threads Need to Communicate

In a multithreaded program, threads:

* often **share data** (e.g., same object in heap memory);
* must sometimes **coordinate actions** (e.g., one thread waits until another finishes some work);
* must ensure **consistency and visibility** of shared variables.

Without synchronization, two problems arise:

1. **Race condition** — two threads update shared data simultaneously → unpredictable results.
2. **Visibility issue** — one thread changes a variable, but another still sees the old value due to CPU caching.

Hence, we need mechanisms for:

* **Mutual exclusion** (only one thread modifies shared data at a time)
* **Communication / coordination** (threads can wait and notify each other safely)

---

## 🔹 2. Intrinsic Locks (Monitor Locks)

Every Java object implicitly has a **monitor lock** (or intrinsic lock) associated with it.

### How it works

* When a thread enters a `synchronized` block or `synchronized` method, it **acquires that object’s monitor lock**.
* While one thread holds the lock, **no other thread can enter any synchronized block or method** guarded by that same object.
* When the synchronized block/method finishes (or throws an exception), the lock is automatically **released**.

### Example

```java
class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}
```

✅ Both `increment()` and `getCount()` use the **same monitor** (`this` object).
If Thread-A is in `increment()`, Thread-B calling `getCount()` must wait.

---

## 🔹 3. Intrinsic Lock Facts

| Concept                         | Explanation                                                           |
| ------------------------------- | --------------------------------------------------------------------- |
| **One lock per object**         | Every object has exactly **one** monitor lock.                        |
| **Competes across methods**     | All synchronized instance methods on the same object share that lock. |
| **Static synchronized methods** | Use the **class-level lock**, i.e. `ClassName.class`.                 |
| **Lock acquisition**            | Thread must own the lock before entering the synchronized region.     |
| **Lock release**                | Automatic on exit or exception.                                       |
| **Nested locking**              | A thread that already holds a lock can reacquire it (→ *reentrant*).  |

---

## 🔹 4. Object-Level vs Class-Level Locking

### 🔸 Object-level (instance)

```java
public synchronized void increment() { ... }  // lock on `this`
```

→ only threads accessing the same object are blocked.

### 🔸 Class-level (static)

```java
public static synchronized void log() { ... }  // lock on Class object
```

→ all threads across all instances synchronize on `ClassName.class`.

---

## 🔹 5. Synchronized Blocks (fine-grained control)

You can synchronize **part** of a method and choose the lock object:

```java
synchronized (someObject) {
    // critical section
}
```

Useful for:

* reducing lock contention (only critical code is locked)
* synchronizing on a custom object instead of `this`

Example:

```java
Object fileLock = new Object();
void writeFile() {
    synchronized (fileLock) {
        // file I/O here
    }
}
```

---

## 🔹 6. Reentrant Locks (Java 5 +)

The term **reentrant** means a thread can acquire the same lock multiple times without deadlock.

```java
synchronized void methodA() {
    methodB(); // same thread already owns the lock
}
synchronized void methodB() { ... }
```

When `methodA()` calls `methodB()`, the same thread re-enters the lock safely — the JVM tracks the count of re-entries internally.

---

## 🔹 7. Common Synchronization Problems

### ❌ 1. Race conditions

Two threads modify shared state simultaneously — final value depends on timing.

### ❌ 2. Deadlocks

Two threads wait on each other’s locks → neither proceeds.

### ❌ 3. Starvation

A thread never gets CPU or lock access due to unfair scheduling.

### ❌ 4. Livelock

Threads keep changing state reacting to each other but never make progress.

---

## 🔹 8. wait(), notify(), notifyAll()

### 🧠 Purpose

Used for **inter-thread coordination** inside synchronized blocks.

They belong to `java.lang.Object` (not Thread) — because every object has a monitor lock.

### 🧩 Behavior

| Method        | Description                                                                                                             |
| ------------- | ----------------------------------------------------------------------------------------------------------------------- |
| `wait()`      | Releases the monitor and suspends the thread until another thread calls `notify()` or `notifyAll()` on the same object. |
| `notify()`    | Wakes up one waiting thread on the same object’s monitor.                                                               |
| `notifyAll()` | Wakes up *all* waiting threads on that object.                                                                          |

### 🔹 Rules

* Must be called **inside a synchronized block/method** on the same object.
* Otherwise, `IllegalMonitorStateException` is thrown.
* When a thread calls `wait()`, it:

    1. releases the monitor lock,
    2. enters the waiting set for that object,
    3. waits until notified,
    4. then re-acquires the lock before continuing.

---

### Example

```java
class SharedResource {
    private boolean ready = false;

    synchronized void waitForSignal() throws InterruptedException {
        while (!ready) {
            wait(); // releases lock and waits
        }
        System.out.println("Signal received, proceeding...");
    }

    synchronized void sendSignal() {
        ready = true;
        notify(); // wakes one waiting thread
    }
}
```

---

## 🔹 9. wait() vs sleep()

| Feature                | `wait()`                 | `sleep()`           |
| ---------------------- | ------------------------ | ------------------- |
| Package                | `Object` class           | `Thread` class      |
| Requires synchronized? | ✅ Yes                    | ❌ No                |
| Releases lock?         | ✅ Yes                    | ❌ No                |
| Used for               | Thread communication     | Pausing execution   |
| Who wakes it?          | `notify()`/`notifyAll()` | Timer / interrupt   |
| Scope                  | Shared monitor           | Current thread only |

---

## 🔹 10. Producer–Consumer Pattern (classic example)

Producer thread adds items to a buffer.
Consumer thread removes items.
Both must coordinate so:

* producer waits if buffer is full,
* consumer waits if buffer is empty.

### Example (using wait/notify)

```java
class SharedBuffer {
    private int data;
    private boolean hasData = false;

    public synchronized void produce(int value) throws InterruptedException {
        while (hasData) wait();
        data = value;
        hasData = true;
        System.out.println("Produced " + value);
        notify();
    }

    public synchronized int consume() throws InterruptedException {
        while (!hasData) wait();
        hasData = false;
        System.out.println("Consumed " + data);
        notify();
        return data;
    }
}
```

---

## 🔹 11. Why use `while` with `wait()`

Because a waiting thread might:

* be woken up by `notifyAll()` intended for another condition, or
* experience a *spurious wakeup* (allowed by the JVM spec).

Hence the best practice:

```java
while (!condition) {
    wait();
}
```

not:

```java
if (!condition) {
    wait();
}
```

---

## 🔹 12. ReentrantLock (java.util.concurrent.locks)

A modern alternative to `synchronized`.

### Advantages

* Explicit lock and unlock control.
* Try to acquire with timeout (`tryLock()`).
* Fairness policy.
* Can be unlocked in a different scope.

### Example

```java
Lock lock = new ReentrantLock(true); // fair

try {
    lock.lock();
    // critical section
} finally {
    lock.unlock();
}
```

---

## 🔹 13. ReentrantLock vs synchronized

| Feature             | `synchronized`                      | `ReentrantLock`               |
| ------------------- | ----------------------------------- | ----------------------------- |
| Simplicity          | Language keyword                    | Explicit object               |
| Lock release        | Automatic                           | Must call `unlock()` manually |
| Try with timeout    | ❌                                   | ✅ (`tryLock(long, TimeUnit)`) |
| Fairness            | No guarantee                        | Can choose fair policy        |
| Condition variables | ❌                                   | ✅ (via `newCondition()`)      |
| Performance         | Slightly slower on heavy contention | Better in complex scenarios   |

---

## 🔹 14. Conditions (Advanced)

`ReentrantLock.newCondition()` provides multiple waiting queues — more flexible than `wait()/notify()`, where each object has a single queue.

```java
Lock lock = new ReentrantLock();
Condition notFull = lock.newCondition();
Condition notEmpty = lock.newCondition();
```

Useful for implementing advanced producer–consumer systems with multiple states.

---

## 🔹 15. Synchronization vs Locks Summary

| Concept             | synchronized | Lock API                  |
| ------------------- | ------------ | ------------------------- |
| Language level      | Keyword      | Library class             |
| Automatic release   | Yes          | No (must manually unlock) |
| Fair scheduling     | No           | Yes (optional)            |
| Multiple conditions | No           | Yes                       |
| Read/Write variants | No           | Yes (`ReadWriteLock`)     |

---

## 🔹 16. Memory Visibility & Happens-Before

Java Memory Model (JMM) ensures that:

* Every write to a `volatile` or synchronized block is **visible** to other threads after lock release.
* Every read after acquiring the lock sees the **latest value**.

That’s the **happens-before relationship**:

> If A releases a lock, and B later acquires the same lock → A’s changes are visible to B.

Without synchronization or `volatile`, threads may see stale data due to CPU caching.

---

## 🔹 17. Best Practices for Thread Communication

✅ Always guard shared data with synchronization or locks.
✅ Use `while` with `wait()`.
✅ Minimize synchronized blocks — lock only what’s necessary.
✅ Use `notifyAll()` unless only one waiting thread must proceed.
✅ Prefer high-level concurrency utilities (`BlockingQueue`, `Semaphore`, `CountDownLatch`) for new code.
✅ Avoid holding locks during long operations or blocking I/O.

---

## 🔹 18. Java Concurrency Utilities (Next Level)

The `java.util.concurrent` package provides higher-level tools that internally use locks and conditions safely:

* `BlockingQueue` → producer–consumer
* `Semaphore` → permits
* `CountDownLatch` → coordination
* `CyclicBarrier` → multi-thread synchronization
* `ReadWriteLock` → concurrent read, exclusive write

They reduce manual use of `wait()`/`notify()`.

---

## 🧾 Quick Summary Cheat-Sheet

| Concept                | Purpose                                 |
| ---------------------- | --------------------------------------- |
| `synchronized`         | Mutual exclusion (one thread at a time) |
| `wait()`               | Temporarily releases lock and waits     |
| `notify()/notifyAll()` | Wakes up waiting threads                |
| `ReentrantLock`        | Explicit lock, advanced control         |
| `Condition`            | Multiple wait sets per lock             |
| `volatile`             | Lightweight visibility guarantee        |
| `join()`               | One thread waits for another to finish  |
| `yield()`              | Suggest scheduler switch                |
| `sleep()`              | Pause thread, keeps lock                |

---

## 🧠 Interview-Level Insights

* **Q:** Why can’t `wait()` be called outside synchronized?
  **A:** Because it needs to release and re-acquire the same object’s monitor; outside synchronized, no monitor is owned.

* **Q:** What’s a *spurious wakeup*?
  **A:** A thread wakes from `wait()` without being notified — JVM allows this; hence, always check condition in a loop.

* **Q:** Difference between intrinsic lock and ReentrantLock?
  **A:** Intrinsic locks are automatic and tied to objects; ReentrantLock is explicit and provides more features (fairness, tryLock).

* **Q:** Why is `notifyAll()` often preferred over `notify()`?
  **A:** To prevent lost notifications when multiple threads wait for different conditions.

* **Q:** How does synchronization ensure visibility?
  **A:** Lock release flushes thread-local changes to main memory; lock acquisition reads from main memory.

