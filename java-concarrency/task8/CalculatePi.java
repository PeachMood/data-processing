package task8;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class CalculatePi implements Runnable {
    private final Phaser phaser;
    private final List<CalculatePartialSum> tasks;
    private volatile double pi = 0.0;

    public CalculatePi(Phaser phaser, long threadsNumber, long partialSumLength) {
        this.phaser = phaser;
        this.tasks = new ArrayList<>();
        for (int i = 0; i < threadsNumber; ++i) {
            tasks.add(new CalculatePartialSum(phaser, i * partialSumLength, partialSumLength, threadsNumber * partialSumLength));
        }
    }

    public double getResult() {
        pi = 0.0;
        for (int i = 0; i < tasks.size(); ++i) {
            pi += 4.0 * tasks.get(i).getResult();
        }
        return pi;
    }

    @Override
    public void run() {
        phaser.register();
        tasks.forEach(task -> new Thread(task).start());
        do {
            phaser.arriveAndAwaitAdvance();
        } while (!phaser.isTerminated());
    }
}
