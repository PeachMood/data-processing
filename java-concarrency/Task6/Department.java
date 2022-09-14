package Task6;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Department {
    private final int identifier;
    private final int workingSeconds;
    private int calculationResult = 0;

    public Department(final int identifier) {
        this.identifier = identifier;
        this.workingSeconds = ThreadLocalRandom.current().nextInt(1, 6);
    }

    public void performCalculations() {
        for (int i = 0; i < workingSeconds; i++) {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            } catch (final InterruptedException ignored) {

            }
            calculationResult += i;
        }
    }

    public int getIdentifier() {
        return identifier;
    }

    public int getCalculationResult() {
        return calculationResult;
    }
}
