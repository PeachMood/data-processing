package task14;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        Semaphore module = new Semaphore(0);
        Semaphore partA = new Semaphore(0);
        Semaphore partB = new Semaphore(0);
        Semaphore partC = new Semaphore(0);

        List<Thread> producers = new ArrayList<>();
        producers.add(new Thread(new WidgetProducer(module, partC)));
        producers.add(new Thread(new ModuleProducer(module, partA, partB)));
        producers.add(new Thread(new PartProducer("A", partA, 1000)));
        producers.add(new Thread(new PartProducer("B", partB, 2000)));
        producers.add(new Thread(new PartProducer("C", partC, 3000)));

        producers.forEach(producer -> producer.start());
    }
}
