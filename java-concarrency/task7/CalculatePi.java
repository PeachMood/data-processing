package task7;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class CalculatePi implements Runnable {
    private final Phaser phaser;
    private final int threadsNumber;
    private final int partialSumLength;
    private volatile double pi = 0.0;

    public CalculatePi(Phaser phaser, int threadsNumber, int partialSumLength) {
        this.phaser = phaser;
        this.threadsNumber = threadsNumber;
        this.partialSumLength = partialSumLength;
    }

    public double getResult() {
        return pi;
    }

    @Override
    public void run() {
        final List<CalculatePartialSum> tasks = new ArrayList<>(threadsNumber);
        for (int i = 0; i < threadsNumber; ++i) {
            tasks.add(new CalculatePartialSum(phaser, i * partialSumLength, partialSumLength, threadsNumber * partialSumLength));
        }

        phaser.register();
        tasks.forEach(task -> new Thread(task).start());
        while (!phaser.isTerminated()) {
            phaser.arriveAndAwaitAdvance();
        }

        for (int i = 0; i < threadsNumber; ++i) {
            pi += 4.0 * tasks.get(i).getResult();
        }
    }
}
