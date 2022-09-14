package Task6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

public final class Founder {
    private final CyclicBarrier barrier;
    private final List<Worker> workers;

    public Founder(final Company company) {
        int count = company.getDepartmentsCount();
        barrier = new CyclicBarrier(count, () -> company.showCollaborativeResult());
        workers = new ArrayList<>();
        IntStream.range(0, count).forEach(index -> workers.add(new Worker(company.getFreeDepartment(index), barrier)));
    }

    public void start() {
        for (final Worker worker : workers) {
            new Thread(worker).start();
        }
    }
}
