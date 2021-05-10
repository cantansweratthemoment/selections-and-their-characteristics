public class Main {
    public static void main(String[] args) {
        double[] data = {0.34, -1.14, 0.73, 1.31, -1.55,
                -0.74, 1.32, 0.8, 1.12, -0.81,
                -1.38, 0.8, 0.38, 0.52, -0.9,
                -0.39, -0.53, 0.77, -0.77, 0.81};
        Selection selection = new Selection(data);
        selection.printInfo();
    }
}
