package task13;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher implements Runnable {
    private String name;
    private ReentrantLock forks;
    private Condition condition;
    private ReentrantLock leftFork;
    private ReentrantLock rightFork;

    Philosopher(String name, ReentrantLock forks, Condition condition, ReentrantLock leftFork, ReentrantLock rightFork) {
        this.name = name;
        this.forks = forks;
        this.condition = condition;
        this.leftFork = leftFork;
        this.rightFork = rightFork;

    }

    private void randomSleep() {
        long millis = ThreadLocalRandom.current().nextLong(1000, 6000);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            randomSleep();
            forks.lock();
            try {
                while(!leftFork.tryLock() || !rightFork.tryLock()) {
                    if (leftFork.isHeldByCurrentThread()) {
                        leftFork.unlock();
                    }
                    if (rightFork.isHeldByCurrentThread()) {
                        rightFork.unlock();
                    }
                    condition.await();
                }
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            } finally {
                forks.unlock();
            }
            System.out.println(name + " picked up forks");
            randomSleep();
            forks.lock();
            rightFork.unlock();
            leftFork.unlock();
            System.out.println(name + " put down forks");
            condition.signalAll();
            forks.unlock();
        }
    }
}
