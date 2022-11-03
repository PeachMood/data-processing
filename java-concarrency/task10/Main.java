package task10;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();
        Lock lock3 = new ReentrantLock();

        lock1.lock();
        Thread thread = new Thread(new PrintLine(lock1, lock2, lock3));
        thread.start();
        for (int i = 0; i < 10; ++i) {
            lock3.lock();
            System.out.println("Main thread: " + (i + 1));
            lock1.unlock();
            lock2.lock();
            lock3.unlock();
            lock1.lock();
            lock2.unlock();
        }
    }
}