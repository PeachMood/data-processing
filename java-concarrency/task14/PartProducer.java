package task14;

import java.util.concurrent.Semaphore;

public class PartProducer implements Runnable {
    private final String partName;
    private final Semaphore semaphore;
    private final long productionTime;

    public PartProducer(String partName, Semaphore semaphore, int productionTime) {
        this.partName = partName;
        this.semaphore = semaphore;
        this.productionTime = productionTime;
    }

    public void run() {
        try {
            for (int index = 1; true; ++index) {
                Thread.sleep(productionTime);
                System.out.println("Part " + partName + " №" + index + " was produced");
                semaphore.release();
            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
