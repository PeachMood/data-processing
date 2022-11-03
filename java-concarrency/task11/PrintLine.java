package task11;

import java.util.concurrent.Semaphore;

public class PrintLine implements Runnable {
    private final Semaphore semaphore1;
    private final Semaphore semaphore2;

    public PrintLine(Semaphore semaphore1, Semaphore semaphore2) {
        this.semaphore1 = semaphore1;
        this.semaphore2 = semaphore2;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; ++i)  {
            try {
                semaphore1.acquire();
            } catch (InterruptedException e) {}
            System.out.println("Child thread: " + (i + 1));
            semaphore2.release();
        }
    }
}