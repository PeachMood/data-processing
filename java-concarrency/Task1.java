public class Task1 {

    public static void main(String[] args) {
        Runnable task = () -> {
            for (int i = 0; i < 10; ++i) {
                System.out.println("String №" + (i + 1) + " from created thread");
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        for (int i = 0; i < 10; ++i) {
            System.out.println("String №" + (i + 1) + " from main");
        }
    }
}