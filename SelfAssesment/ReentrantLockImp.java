package SelfAssesment;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockImp {
    class ReentrantLock{
        private boolean isLocked = false;
        private Thread lockingThread = null;
        private int lockCount = 0;


        public synchronized void lock() throws InterruptedException {
            while(isLocked && Thread.currentThread() != lockingThread) {
                wait();
            }
            isLocked = true;
            lockingThread = Thread.currentThread();
            lockCount++;
        }

        public synchronized void unlock() throws InterruptedException {
            while(Thread.currentThread() == lockingThread) {
                wait();
            }
            if(Thread.currentThread() == lockingThread) {
                lockCount--;
                if(lockCount == 0) {
                    isLocked = false;
                    lockingThread = null;
                    notify();
                }

            }


        }

        ReentrantLock lock = new ReentrantLock();
        public void perFormTask(){
            try {
                lock.lock();
                System.out.println("asdasdas");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                try {
                    lock.unlock();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public static void main(String[] args) {
            ReentrantLockImp lock = new ReentrantLockImp();

        }
    }


}
