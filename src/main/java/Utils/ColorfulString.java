package Utils;

public class ColorfulString {
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void colorfulPrint(String q) {
        System.out.print(ANSI_PURPLE + q + ANSI_RESET);
    }
}