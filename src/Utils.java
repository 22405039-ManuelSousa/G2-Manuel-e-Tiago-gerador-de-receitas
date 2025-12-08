
import java.util.Scanner;

public class Utils {

    static Scanner sc = new Scanner(System.in);

    static String readLineFromKeyboard() {
        System.out.print("> ");
        String input = sc.nextLine();
        return input;
    }

    static int readIntFromKeyboard() {
        System.out.print("> ");
        String input = sc.nextLine();
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static int readCharFromKeyboard() {
        System.out.print("> ");
        String input = sc.nextLine();

        if (!input.isEmpty()) {
            char c = input.charAt(0);
            return c - '0';
        }

        return -1;
    }
}