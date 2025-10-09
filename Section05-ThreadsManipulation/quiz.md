🧩 🔬 Hard Level
Practice 508 — File Downloader Simulation

Concept: join(), coordination, daemon helper.

Task:

Create threads representing Downloaders for 3 files.

Each thread simulates a download (loop 1–10 %, sleep 200 ms).

Start a ProgressMonitor daemon thread that prints overall % completed every 300 ms.

Main waits for all downloaders with join().

Expected:
Monitor shows progress while downloads are active.
After all finish, JVM ends → monitor stops automatically.

Practice 509 — Manual Round-Robin Scheduling

Concept: Controlled time-slicing, fairness.

Task:

Create 5 threads printing messages in steps (1–5).

Each thread sleeps 100 ms after each step.

Observe rotation pattern of threads getting CPU time.

Learning:
Mimics OS round-robin scheduling.

Practice 510 — Coordinated Startup

Concept: join(), dependency management.

Scenario:
You have three services:

Database Service

Cache Service

Application Service (must start only after DB & Cache are up)

Task:

Each service is a thread simulating startup time (sleep).

Use join() so that Application starts only after both others complete.

Expected Output:

Starting Database Service...
Starting Cache Service...
Database ready!
Cache ready!
Starting Application Service...

Practice 511 — Measuring Multithreading Overhead

Concept: Benchmarking performance.

Task:

Compute sum of 1 → 1 billion twice:
a) Sequentially
b) Using 4 threads summing chunks in parallel

Compare timings.

Learning:
Multithreading doesn’t always help — CPU overhead & memory contention can make parallel slower for small tasks.

Practice 512 — Priority Starvation Demo

Concept: Scheduler unfairness.

Task:

Start 10 threads; set 9 with low priority (1), one with high (10).

Each prints 1–1000 with small sleep.

Observation:
Some JVMs let the high-priority thread dominate → starvation.
Then explain why OS-level schedulers may ignore this.

Practice 513 — Graceful Shutdown vs Daemon Termination

Concept: Proper cleanup strategy.

Task:

Create a daemon thread doing periodic cleanup (sleep(1000)).

Use Runtime.getRuntime().addShutdownHook() to log a message when JVM exits.

Observe that the daemon thread may not complete its cleanup on shutdown.

Then modify to use an ExecutorService and proper shutdown() instead.

Learning:
Daemon threads end abruptly → always prefer structured termination for critical tasks.

Practice 514 — Thread Priority and Join Chain

Concept: Combining priority and coordination.

Task:

Create 3 threads A → B → C.

A starts B, waits (join()), B starts C, waits (join()).

Assign A = MAX, B = NORM, C = MIN priority.

Observe execution order; discuss why priority doesn’t override join.

Expected:
Always sequential (A→B→C) regardless of priority.

💡 Optional Capstone Challenge
Practice 515 — Task Pipeline Simulation

Concept: Thread coordination and life-cycle control.

Scenario:
Build a mini “ETL” pipeline using threads:

ReaderThread → reads chunks of data (simulate I/O delay)

ProcessorThread → transforms data (sleep 50 ms)

WriterThread → writes to file (sleep 30 ms)

Use BlockingQueues or simple shared lists with synchronization (wait()/notify()) to hand data between stages.

Goal:
Show a controlled flow of data through multiple threads — mimicking production multi-thread pipelines.