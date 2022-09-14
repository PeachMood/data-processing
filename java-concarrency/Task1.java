public class Task1 {

    public static void main(String[] args) {
        Runnable task = () -> {
            for (int i = 0; i < 10; ++i) {
                System.out.println("String №" + (i + 1));
            }
            System.out.println("Task was completed\n");
        };
        Thread thread = new Thread(task);
        thread.start();
        task.run();
    }
}