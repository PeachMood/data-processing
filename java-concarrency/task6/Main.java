package task6;

public class Main {
    public static void main(String[] args) {
        Company company = new Company(10);
        Founder founder = new Founder(company);
        founder.start();
    }
}
