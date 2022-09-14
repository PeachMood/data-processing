package Task6;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker implements Runnable {
    private final Department department;
    private final CyclicBarrier barrier;

    public Worker(Department department, CyclicBarrier barrier) {
        this.department = department;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        department.performCalculations();
        try {
            barrier.await();
        } catch (InterruptedException exception) {
            return;
        } catch (BrokenBarrierException exception) {
            return;
        }
    }
}
