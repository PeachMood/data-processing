import java.util.concurrent.TimeUnit;

public class Task4 {
    public static void main(String[] args) {
        Runnable task = () -> {
            while(true) {
                System.out.println("I must not tell lies");
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(2));
        } catch (InterruptedException exception) {
            System.out.println("Thread has been interrupted");
        }
        thread.interrupt();
    }
}
