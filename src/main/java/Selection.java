import Utils.Chart;

import java.util.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static Utils.ColorfulString.*;

public class Selection {
    private final double[] data;
    private double[] variationRange;
    private double leftExtremeValue;
    private double rightExtremeValue;
    private double range;
    private double expectedValue;
    private double standardDeviation;
    private SortedMap<Double, Double> empiricalDistributionFunction;

    public Selection(double[] data) {
        this.data = data;
    }

    public void printInfo() {
        colorfulPrint("Исходная выборка:\n");
        for (double datum : data) {
            System.out.print(datum + " ");
        }
        System.out.println();
        calculateVariationRange();
        colorfulPrint("Вариационный ряд:\n");
        for (double datum : variationRange) {
            System.out.print(datum + " ");
        }
        System.out.println();
        calculateExtremeValues();
        colorfulPrint("Минимальное значение: ");
        System.out.print(leftExtremeValue);
        colorfulPrint("\nМаксимальное значение: ");
        System.out.println(rightExtremeValue);
        calculateRange();
        colorfulPrint("Размах выборки: ");
        System.out.println(range);
        calculateExpectedValue();
        colorfulPrint("Математическое ожидание: ");
        System.out.println(expectedValue);
        calculateStandardDeviation();
        colorfulPrint("Среднеквадратическое отклонение: ");
        System.out.println(standardDeviation);
        calculateEmpiricalDistributionFunction();
        colorfulPrint("Эмпирическая функция распределения: \n");
        printEmpiricalDistributionFunction();
    }

    public void calculateVariationRange() {
        variationRange = data.clone();
        Arrays.sort(variationRange);
    }

    public void calculateExtremeValues() {
        leftExtremeValue = variationRange[0];
        rightExtremeValue = variationRange[variationRange.length - 1];
    }

    public void calculateRange() {
        range = rightExtremeValue - leftExtremeValue;
    }

    public void calculateExpectedValue() {
        double sum = 0;
        for (double datum : data) {
            sum += datum;
        }
        expectedValue = sum / data.length;
    }

    public void calculateStandardDeviation() {
        double sum = 0;
        for (double datum : data) {
            sum += pow((datum - expectedValue), 2);
        }
        standardDeviation = sqrt(sum / data.length);
    }

    public void calculateEmpiricalDistributionFunction() {
        empiricalDistributionFunction = new TreeMap<>();
        double n = data.length;
        for (int i = 0; i < variationRange.length; i++) {
            double v = variationRange[i];
            if (empiricalDistributionFunction.containsKey(v)) {
                empiricalDistributionFunction.replace(v, i / n);
            } else {
                empiricalDistributionFunction.put(v, i / n);
            }
        }
    }

    public void printEmpiricalDistributionFunction() {
        double prev = 0;
        boolean flag = true;
        for (Map.Entry<Double, Double> entry : empiricalDistributionFunction.entrySet()) {
            Double key = entry.getKey();
            Double value = entry.getValue();
            if (flag) {
                System.out.println("F(x)=" + value + ", при x<=" + key);
            } else {
                System.out.println("F(x)=" + value + ", при " + prev + "<x<=" + key);
            }
            flag = false;
            prev = key;
        }
        System.out.println("F(x)=" + 1 + ", при " + "x>" + prev);
    }

    public void drawInfo() {

        Chart chart = new Chart(this.empiricalDistributionFunction, calculateStatisticsSeries());
        chart.drawEmpiricalDistributionFunctionChart();
        chart.drawFrequencyHistogram();
        chart.drawFrequencyPolygon();
    }

    public SortedMap<Double, Double> calculateStatisticsSeries() {
        SortedMap<Double, Double> statisticsSeries = new TreeMap<>();
        for (double datum : data) {
            if (statisticsSeries.containsKey(datum)) {
                statisticsSeries.replace(datum, statisticsSeries.get(datum) + 1);
            } else {
                statisticsSeries.put(datum, 1.0);
            }
        }
        return statisticsSeries;
    }
}