import java.util.Arrays;
import java.util.List;

public class Task3 {
    public static class PrintList<T> implements Runnable {
        private List<T> list;

        public PrintList(List<T> list) {
            this.list = list;
        }

        @Override
        public void run() {
            list.forEach((string) -> System.out.println(string));
        }
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(new PrintList<String>(Arrays.asList("1", "11", "111", "1111")));
        Thread thread2 = new Thread(new PrintList<String>(Arrays.asList("2", "22", "222", "2222")));
        Thread thread3 = new Thread(new PrintList<String>(Arrays.asList("3", "33", "333", "3333")));
        Thread thread4 = new Thread(new PrintList<String>(Arrays.asList("4", "44", "444", "4444")));
        List<Thread> threads = Arrays.asList(thread1, thread2, thread3, thread4);
        threads.forEach(thread -> thread.start());
    }
}
