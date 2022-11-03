package task11;

import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore1 = new Semaphore(1);
        Semaphore semaphore2 = new Semaphore(1);

        semaphore1.acquire();
        Thread thread = new Thread(new PrintLine(semaphore1, semaphore2));
        thread.start();
        for (int i = 0; i < 10; ++i) {
            semaphore2.acquire();
            System.out.println("Main thread: " + (i + 1));
            semaphore1.release();
        }
    }
}