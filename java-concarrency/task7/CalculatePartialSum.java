package task7;

import java.util.concurrent.Phaser;

public class CalculatePartialSum implements Runnable {
    private final Phaser phaser;
    private final int from;
    private final int length;
    private final int step;
    private volatile double partialSum = 0;

    public CalculatePartialSum(Phaser phaser, int from, int length, int step) {
        this.phaser = phaser;
        this.from = from;
        this.length = length;
        this.step = step;
        phaser.register();
    }

    public double getResult() {
        return partialSum;
    }

    @Override
    public void run() {
        while (!phaser.isTerminated()) {
            int step = this.step * phaser.getPhase();
            for (int i = this.from + step; i < this.from + length + step; ++i) {
                double summand = 1.0 / (2.0 * i + 1.0);
                partialSum += i % 2 != 0 ? (-1.0) * summand : summand;
            }
            phaser.arriveAndAwaitAdvance();
        }
    }
}
