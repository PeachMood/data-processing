package task10;

import java.util.concurrent.locks.Lock;

public class PrintLine implements Runnable {
    private final Lock lock1;
    private final Lock lock2;
    private final Lock lock3;

    public PrintLine(Lock lock1, Lock lock2, Lock lock3) {
        this.lock1 = lock1;
        this.lock2 = lock2;
        this.lock3 = lock3;
    }

    @Override
    public void run() {
        lock2.lock();
        for (int i = 0; i < 10; ++i) {
            lock1.lock();
            System.out.println("Child thread: " + (i + 1));
            lock2.unlock();
            lock3.lock();
            lock1.unlock();
            lock2.lock();
            lock3.unlock();
        }
    }
}