package task14;

import java.util.concurrent.Semaphore;

public class ModuleProducer implements Runnable {
    private final Semaphore module;
    private final Semaphore partA;
    private final Semaphore partB;

    public ModuleProducer(Semaphore module, Semaphore partA, Semaphore partB) {
        this.module = module;
        this.partA = partA;
        this.partB = partB;
    }

    public void run() {
        try {

            for (int index = 1; true; ++index) {
                partA.acquire();
                partB.acquire();
                System.out.println("Module №" + index +  " was produced");
                module.release();

            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
