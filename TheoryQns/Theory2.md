📘 **Java Multithreading — Complete Theory & Interview Question Bank**

---

### 🧭 PURPOSE

A comprehensive repository of all conceptual and interview-style questions related to **Java Multithreading** — from basics to advanced concurrency utilities.

---

## 🧩 SECTION 1 — Core Concepts

1. **What is a thread?**
   A thread is the smallest unit of a process that can be scheduled and executed by the operating system.

2. **What is multithreading?**
   Multithreading is the ability of a CPU or a single core to execute multiple threads concurrently within a process.

3. **What is the difference between a process and a thread?**
   A process is an independent program in execution with its own memory. A thread is a subset of that process, sharing resources but executing independently.

4. **What are the advantages of multithreading?**

    * Improved CPU utilization
    * Faster I/O and responsiveness
    * Easier modeling of concurrent systems
    * Scalability on multi-core processors

5. **What are common problems with multithreading?**
   Race conditions, deadlocks, livelocks, thread starvation, and context switching overhead.

6. **What is a race condition? How can it be avoided?**
   It occurs when multiple threads modify shared data simultaneously. Avoid using synchronization, locks, or atomic variables.

---

## 🧩 SECTION 2 — Thread Lifecycle & States

7. **Thread lifecycle states:**

    * NEW
    * RUNNABLE
    * BLOCKED
    * WAITING
    * TIMED_WAITING
    * TERMINATED

8. **RUN state**
   Refers to the execution of the `run()` method — the thread’s task logic.

9. **RUNNABLE state**
   After `start()` is called, the thread is ready or running depending on CPU scheduling.

10. **WAITING and TIMED_WAITING**
    WAITING — waits indefinitely for another thread (e.g. `wait()`, `join()`).
    TIMED_WAITING — waits for a fixed time (`sleep()`, `wait(time)`).

11. **TERMINATED**
    Thread completes or terminates due to exception.

12. **What are the states in a thread lifecycle?**
    NEW → RUNNABLE → (BLOCKED/WAITING/TIMED_WAITING) → TERMINATED

---

## 🧩 SECTION 3 — Thread Creation & Execution

13. **How to create a thread in Java?**

    * Extend `Thread` class
    * Implement `Runnable` interface

14. **Thread class vs Runnable interface**

| Aspect      | Thread                     | Runnable                   |
| ----------- | -------------------------- | -------------------------- |
| Inheritance | Extends Thread             | Implements Runnable        |
| Flexibility | Can't extend other classes | More flexible              |
| Separation  | Combines task & worker     | Separates task from worker |
| Preferred   | ❌                          | ✅                          |

15. **Can we call `run()` instead of `start()`?**
    Yes, but it runs on the same thread. `start()` creates a new thread.

16. **What is `join()` method?**
    Makes the current thread wait until another thread finishes execution.

17. **What is `sleep()` method?**
    Pauses the current thread for a specific time without releasing locks.

18. **Difference between `wait()` and `sleep()`**

| Feature      | `wait()`     | `sleep()` |
| ------------ | ------------ | --------- |
| Lock release | Yes          | No        |
| Defined in   | Object       | Thread    |
| Purpose      | Coordination | Delay     |
| Needs sync   | Yes          | No        |

19. **Why must `wait()` be in a synchronized block?**
    Because it works on an object monitor — thread must own that monitor.

20. **What is `yield()`?**
    Suggests that the current thread is willing to yield CPU to others (non-deterministic).

---

## 🧩 SECTION 4 — Synchronization & Communication

21. **What is synchronization?**
    Ensures mutual exclusion so only one thread accesses shared data at a time.

22. **What is inter-thread communication?**
    Mechanism allowing threads to coordinate via `wait()`, `notify()`, `notifyAll()`.

23. **Functions for inter-thread communication:**

    * `wait()`
    * `notify()`
    * `notifyAll()`

24. **Purpose of `wait()`**
    Releases the monitor and waits for a signal from another thread.

25. **Difference between `notify()` and `notifyAll()`**
    `notify()` wakes one thread; `notifyAll()` wakes all waiting threads.

26. **Synchronized method vs synchronized block**
    Block is preferred (finer control, better performance).

---

## 🧩 SECTION 5 — Thread Coordination & Locking

27. **What is ReentrantLock?**
    A lock that allows a thread to acquire it multiple times.

28. **What is a Semaphore?**
    A synchronization aid controlling access via permits (binary or counting).

29. **What is a Mutex?**
    A binary semaphore — one thread owns the lock at a time.

30. **What is Deadlock?**
    Two or more threads wait on each other’s locks forever.

31. **How to detect deadlocks?**
    Use `ThreadMXBean.findDeadlockedThreads()` or `jstack`.

32. **How to avoid deadlocks?**
    Lock ordering, tryLock with timeout, minimal nested locks.

33. **What is Livelock?**
    Threads keep changing states but make no progress (e.g., retry loops).

34. **What is Thread Starvation?**
    A low-priority thread never gets CPU time.

35. **What is BlockingQueue?**
    A queue that blocks producers/consumers when full/empty.

36. **What are CountDownLatch and CyclicBarrier?**
    CountDownLatch — waits for N tasks to finish (one-shot).
    CyclicBarrier — waits for N threads to reach a point (reusable).

37. **What is Phaser?**
    Advanced barrier supporting dynamic registration (replacement for CyclicBarrier + CountDownLatch).

---

## 🧩 SECTION 6 — Thread Scheduling & Performance

38. **What is Thread Scheduler?**
    OS component deciding which thread runs next based on priority and time slicing.

39. **What is Time Slicing?**
    CPU time is divided into small slices; each runnable thread gets a share.

40. **What is Thread Priority?**
    Integer 1–10; higher value hints scheduler preference (not guaranteed).

41. **What is Context Switching?**
    CPU saves current thread state and loads another’s. Enables concurrency.

42. **What is Busy Spinning?**
    Thread continuously checks a condition without yielding CPU — used in low-latency designs.

---

## 🧩 SECTION 7 — Thread Utilities & Modern Concurrency

43. **What is ThreadLocal?**
    Provides thread-confined variables (each thread has its own copy).

44. **What is a Shutdown Hook?**
    A cleanup thread triggered when JVM shuts down.

45. **What are Daemon Threads?**
    Background threads (like GC). JVM exits when only daemon threads remain.

46. **What is a Thread Group? Why avoid it?**
    Legacy grouping of threads — replaced by Executor frameworks.

47. **What is a Thread Pool?**
    A set of pre-created threads managed by an Executor for efficient task execution.

48. **Describe different types of Locks.**

    * Mutex — exclusive access
    * ReadWriteLock — multiple readers, one writer
    * SpinLock — busy waiting

49. **What is ConcurrentHashMap? How is it different from Hashtable?**
    ConcurrentHashMap uses fine-grained locking (buckets) — faster than Hashtable’s full lock.

50. **What happens if you don’t override `run()`?**
    Thread runs but executes no logic — default `run()` is empty.

---

## 🧩 SECTION 8 — Advanced Topics

51. **What is a Thread Pool and why use it?**
    A managed pool of reusable threads to avoid repeated creation overhead.

52. **Explain the Producer–Consumer problem.**
    Producers add data; consumers remove data — coordinated via BlockingQueue or wait/notify.

53. **How do threads communicate with each other?**
    Using shared objects and `wait()`, `notify()` mechanisms.

54. **Can static and non-static synchronized methods run concurrently?**
    Yes, they use different locks (class vs instance).

55. **What is the purpose of `finalize()` method?**
    Called by GC before object deletion (deprecated since Java 9).

56. **What is a Deadlock and how can it be avoided?**
    Two threads waiting for each other — avoided by lock ordering, tryLock, or concurrent utilities.

57. **Explain Thread Synchronization.**
    Ensures controlled access to shared resources via `synchronized`, Locks, or atomic classes.

58. **Explain different types of Locks.**
    Mutex, ReentrantLock, ReadWriteLock, SpinLock.

59. **How to improve performance of multithreaded apps?**
    Use minimal synchronization, avoid blocking I/O, use thread pools, and balance workloads.

60. **How do you test multithreaded code?**
    Use stress tests, Awaitility, and tools like `ThreadMXBean` or profilers.

61. **How to handle exceptions in multithreading?**
    Try/catch inside `run()`, use `UncaughtExceptionHandler`, or propagate via `Future.get()`.

62. **What are some multithreading design patterns?**
    Producer–Consumer, Future, Thread Pool, Immutable, Singleton.

63. **What is ThreadLocal variable in Java?**
    Provides thread-specific data storage.

64. **What is Busy Spinning and when to use it?**
    Loop-check condition instead of blocking — used in high-performance code.

65. **What is the difference between reactive and multithreaded programming?**
    Reactive is non-blocking, event-driven; multithreading is parallel execution using multiple threads.

---

✅ **Coverage Summary:**

* Core multithreading & states
* Synchronization & communication
* Deadlock, livelock, starvation
* Locks, semaphores, barriers
* Thread pools & concurrency utilities
* Modern concurrency (ThreadLocal, Busy Spin, Shutdown Hook)

⚙️ **Next Extension (Set 4+):**

* Executor Framework & Future
* CompletableFuture & Async pipelines
* ForkJoinPool & Parallel Streams
* Java Memory Model (happens-before)
* Virtual Threads (Project Loom)
* Debugging & testing multithreading