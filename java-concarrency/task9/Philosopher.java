package task9;

import java.util.concurrent.ThreadLocalRandom;

public class Philosopher implements Runnable {
    private String name;
    private Fork left;
    private Fork right;

    public Philosopher(String name, Fork left, Fork right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    private void randomSleep() {
        long millis = ThreadLocalRandom.current().nextLong(1000, 6000);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            randomSleep();
            synchronized (left) {
                System.out.println("The " + name + " philosopher picked up the left fork");
                synchronized (right) {
                    System.out.println("The " + name + " philosopher picked up the right fork");
                    System.out.println("The " + name + " philosopher began the meal");
                    randomSleep();
                    System.out.println("The " + name + " philosopher put down the right fork");
                }
                System.out.println("The " + name + " philosopher put down the left fork");
                System.out.println("The " + name + " philosopher finished the meal");
            }
        }
    }
}
