package task7;

import java.util.concurrent.Phaser;

public class Main {
    public static void main(String[] args) {
        int threadsNumber = Runtime.getRuntime().availableProcessors();
        if (args.length > 0) {
            threadsNumber = Math.min(threadsNumber, Integer.parseInt(args[0]));
        }

        Phaser phaser = new Phaser() {
            protected boolean onAdvance(int phase, int parties) {
                return phase >= 1000;
            }
        };
        CalculatePi task = new CalculatePi(phaser, threadsNumber, 100000);
        task.run();
        System.out.println("Library pi = " + Math.PI);
        System.out.println("Calculated pi = " + task.getResult());
    }
}
