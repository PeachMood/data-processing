package task8;

import java.util.concurrent.Phaser;

public class CalculatePartialSum implements Runnable {
    private final Phaser phaser;
    private final long from;
    private final long length;
    private final long step;
    private double partialSum = 0;

    public CalculatePartialSum(Phaser phaser, long from, long length, long step) {
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
        do {
            long step = this.step * phaser.getPhase();
            for (long i = this.from + step; i < this.from + length + step; ++i) {
                double summand = 1.0 / (2.0 * i + 1.0);
                partialSum += i % 2 != 0 ? (-1.0) * summand : summand;
            }
            phaser.arriveAndAwaitAdvance();
        } while (!phaser.isTerminated());
    }
}
