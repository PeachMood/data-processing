package task9;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String args[]) {
        Fork[] forks = new Fork[5];
        for (int i = 0; i < 5; ++i) {
            forks[i] = new Fork(i);
        }

        String[] names = new String[]{"Socrates", "Plato", "Aristotle", "Thales", "Pythagoras"};
        // List<Philosopher> philosophers = new ArrayList<>();
        List<WellFedPhilosopher> philosophers = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            // philosophers.add(new Philosopher(names[i], forks[i], i == 4 ? forks[0] : forks[i + 1]));
            philosophers.add(new WellFedPhilosopher(names[i], forks[i], i == 4 ? forks[0] : forks[i + 1]));
        }
        philosophers.forEach(philosopher -> new Thread(philosopher).start());
    }
}
