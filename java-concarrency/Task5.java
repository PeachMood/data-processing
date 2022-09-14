import java.util.concurrent.TimeUnit;

public class Task5 {
    public static void main(String[] args) {
        Runnable task = () -> {
            while(!Thread.currentThread().isInterrupted()) {
                System.out.println("I must not tell lies");
            }
            System.out.println("Thread has finished");
        };
        Thread thread = new Thread(task);
        thread.start();
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(2));
        } catch (InterruptedException exception) {
            System.out.println("Main thread has been interrupted");
        }
        thread.interrupt();
    }
}
