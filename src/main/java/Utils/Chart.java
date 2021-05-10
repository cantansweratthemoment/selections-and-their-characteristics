package Utils;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;

import static java.lang.Math.*;

public class Chart {
    private final SortedMap<Double, Double> empiricalDistributionFunction;
    private final SortedMap<Double, Double> statisticsSeries;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private static final Color PINK_LACE = new Color(255, 211, 232);
    private static final Color WINTER_SKY = new Color(245, 0, 118);
    private static final Color NYANZA_LIGHT = new Color(243, 255, 225);
    private static final Color GREEN_YELLOW = new Color(190, 255, 92);
    private static final Color NYANZA_DARK = new Color(223, 255, 214);
    private static final Color NEON_GREEN = new Color(79, 255, 31);

    public Chart(SortedMap<Double, Double> empiricalDistributionFunction, SortedMap<Double, Double> statisticsSeries) {
        this.empiricalDistributionFunction = empiricalDistributionFunction;
        this.statisticsSeries = statisticsSeries;
    }

    public void drawEmpiricalDistributionFunctionChart() {
        XYChart empiricalDistributionFunctionChart = new XYChartBuilder().width(WIDTH).height(HEIGHT).title("Эмпирическая функция распределения")
                .xAxisTitle("x").yAxisTitle("F(x)").build();
        empiricalDistributionFunctionChart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        empiricalDistributionFunctionChart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Step);
        empiricalDistributionFunctionChart.getStyler().setChartBackgroundColor(PINK_LACE);
        empiricalDistributionFunctionChart.getStyler().setPlotGridLinesColor(PINK_LACE);
        ArrayList<Double> xs = new ArrayList<>();
        ArrayList<Double> ys = new ArrayList<>();
        for (Map.Entry<Double, Double> entry : empiricalDistributionFunction.entrySet()) {
            Double key = entry.getKey();
            Double value = entry.getValue();
            xs.add(key);
            ys.add(value);
        }
        xs.add(empiricalDistributionFunction.lastKey());
        ys.add(1.0);
        xs.add(empiricalDistributionFunction.lastKey()+0.5);
        ys.add(1.0);
        XYSeries series = empiricalDistributionFunctionChart.addSeries("F(x)", xs, ys);
        series.setMarkerColor(WINTER_SKY);
        series.setLineColor(WINTER_SKY);
        new SwingWrapper(empiricalDistributionFunctionChart).displayChart();
    }

    public void drawFrequencyHistogram() {
        CategoryChart frequencyHistogram = new CategoryChartBuilder().width(WIDTH).height(HEIGHT).title("Гистограмма частот")
                .xAxisTitle("x").yAxisTitle("n/h").build();
        frequencyHistogram.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        frequencyHistogram.getStyler().setChartBackgroundColor(NYANZA_LIGHT);
        frequencyHistogram.getStyler().setPlotGridLinesColor(NYANZA_LIGHT);
        double max = statisticsSeries.lastKey();
        double min = statisticsSeries.firstKey();
        double h = (max - min) / (1 + log(statisticsSeries.size()) / log(2));
        ArrayList<Double> xs = new ArrayList<>();
        ArrayList<Double> ys = new ArrayList<>();
        double current = min + h / 2;
        double counter = 0;
        double w = 1000;
        for (Map.Entry<Double, Double> entry : statisticsSeries.entrySet()) {
            double k = entry.getKey();
            double v = entry.getValue();
            if (k > current) {
                double q = current - h / 2;
                double result = ceil(q * w) / w;
                xs.add(result);
                ys.add(counter / h);
                current += h;
                counter = 0;
            }
            counter += v;
        }
        double s = current - h / 2;
        double result = ceil(s * w) / w;
        xs.add(result);
        ys.add(counter / h);
        CategorySeries series = frequencyHistogram.addSeries("histogram", xs, ys);
        series.setMarkerColor(GREEN_YELLOW);
        series.setLineColor(GREEN_YELLOW);
        series.setFillColor(GREEN_YELLOW);
        new SwingWrapper(frequencyHistogram).displayChart();
    }

    public void drawFrequencyPolygon() {
        XYChart frequencyPolygon = new XYChartBuilder().width(WIDTH).height(HEIGHT).title("Полигон частот")
                .xAxisTitle("x").yAxisTitle("n").build();
        frequencyPolygon.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        frequencyPolygon.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        frequencyPolygon.getStyler().setChartBackgroundColor(NYANZA_DARK);
        frequencyPolygon.getStyler().setPlotGridLinesColor(NYANZA_DARK);
        double max = statisticsSeries.lastKey();
        double min = statisticsSeries.firstKey();
        double h = (max - min) / (1 + log(statisticsSeries.size()) / log(2));
        ArrayList<Double> xs = new ArrayList<>();
        ArrayList<Double> ys = new ArrayList<>();
        double current = min + h / 2;
        int w = 1000;
        double counter = 0;
        for (Map.Entry<Double, Double> entry : statisticsSeries.entrySet()) {
            double k = entry.getKey();
            double v = entry.getValue();
            if (k > current) {
                double q = current - h / 2;
                double result = ceil(q * w) / w;
                xs.add(result);
                ys.add(counter);
                current += h;
                counter = 0;
            }
            counter += v;
        }
        double s = current - h / 2;
        double result = ceil(s * w) / w;
        xs.add(result);
        ys.add(counter);
        XYSeries series = frequencyPolygon.addSeries("p(x)", xs, ys);
        series.setMarkerColor(NEON_GREEN);
        series.setLineColor(NEON_GREEN);
        new SwingWrapper(frequencyPolygon).displayChart();
    }
}
