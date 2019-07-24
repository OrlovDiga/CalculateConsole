import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Calculate calculate = new Calculate();
        while (true) {
            System.out.println("Answer: " + calculate.parser(in.nextLine()));
        }
    }
}