package task14;

import java.util.concurrent.Semaphore;

public class WidgetProducer implements Runnable {
    private final Semaphore module;
    private final Semaphore partC;

    public WidgetProducer(Semaphore module, Semaphore partC) {
        this.module = module;
        this.partC = partC;
    }

    public void run() {
        try {
            for (int index = 0; true; ++index) {
                module.acquire();
                partC.acquire();
                System.out.println("Widget" + " №" + index + " was produced");
            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
