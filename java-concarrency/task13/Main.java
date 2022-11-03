package task13;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String args[]) {
        ReentrantLock forks = new ReentrantLock();
        Condition condition = forks.newCondition();
        List<ReentrantLock> list = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            list.add(new ReentrantLock());
        }

        String[] names = new String[]{"Socrates", "Plato", "Aristotle", "Thales", "Pythagoras"};
        List<Philosopher> philosophers = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            philosophers.add(new Philosopher(names[i], forks, condition, list.get(i), i == 4 ? list.get(0) : list.get(i + 1)));
        }
        philosophers.forEach(philosopher -> new Thread(philosopher).start());
    }
}
