package task12;

import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        Scanner input = new Scanner(System.in);
        Thread thread = new Thread(new SortList(list));
        thread.start();
        while (true) {
            String nextLine = input.nextLine();
            String[] lines = nextLine.split("(?<=\\G.{80})");
            for (String line : lines) {
                synchronized (list) {
                    if (line.isEmpty()) {
                        list.forEach(string -> System.out.println(string));
                    } else {
                        list.addFirst(line);
                    }
                }
            }
        }
    }
}
