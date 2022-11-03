package task8;

import java.util.concurrent.Phaser;

public class Main {
    public static volatile boolean isRunning = true;
    public static void main(String[] args) throws InterruptedException {
        int threadsNumber = Runtime.getRuntime().availableProcessors();
        if (args.length > 0) {
            threadsNumber = Math.min(threadsNumber, Integer.parseInt(args[0]));
        }

        Phaser phaser = new Phaser() {
            protected boolean onAdvance(int phase, int parties) {
                return !isRunning;
            }
        };

        CalculatePi task = new CalculatePi(phaser, threadsNumber, 100000);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            phaser.register();
            isRunning = false;
            do {
                phaser.arriveAndAwaitAdvance();
            } while (!phaser.isTerminated());
            System.out.println("Library pi = " + Math.PI);
            System.out.println("Calculated pi = " + task.getResult());
        }));
        task.run();
    }
}
