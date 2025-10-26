
package com.onesnzeroes.clashnzeroes.logic.graphic;

import com.onesnzeroes.clashnzeroes.dao.PlayerDao;
import com.onesnzeroes.clashnzeroes.dao.WarDao;
import com.onesnzeroes.clashnzeroes.model.war.AttackEntity;

import java.awt.*;
        import java.util.Arrays;
import java.util.List;

public class WarGraphicManager extends GraphicManager {

    private final WarDao dao;
    private static final int NUM_COLS = 3;
    private static final int CHART_WIDTH = 520;
    private static final int CHART_HEIGHT = 320;
    private static final int COL_SPACING = 10;
    private static final int STAR_RECT_HEIGHT = 15;

    public WarGraphicManager(WarDao dao, PlayerDao pDao) {
        super(pDao);
        this.dao = dao;
    }

    @Override
    public void drawGraph(Graphics2D g) {
        drawDefensiveColumns(g);
        drawOffensiveColumns(g);
        drawComparisons(g);
    }

    protected void drawOffensiveColumns(Graphics2D g) {
        double areaX = 0;
        double areaY = 0;
        double areaW = CHART_WIDTH / 2.0;
        double areaH = CHART_HEIGHT / 2.0;

        g.setColor(new Color(230, 230, 230));
        g.fillRect(chartX(areaX), chartY(areaY), (int) areaW, (int) areaH);

        int totalSpacing = COL_SPACING * (NUM_COLS + 1);
        double colWidth = (areaW - totalSpacing) / NUM_COLS;
        double colHeightMax = areaH - 25;

        double[] stars = calculateAmounts(this.dao.findAttacksByTag(getTag()));
        double[] heights = calculateRatios(stars, colHeightMax);

        g.setColor(new Color(100, 150, 240));
        FontMetrics fm = g.getFontMetrics();
        for (int i = 1; i <= NUM_COLS; i++) {
            double localX = areaX + COL_SPACING + (i - 1) * (colWidth + COL_SPACING);
            double h = heights[i];
            double localY = areaY + areaH - h;
            g.fillRect(chartX(localX), chartY(localY), (int) colWidth, (int) h);

            int starRectY = (int) areaH + 20;
            g.setColor(Color.BLACK);
            g.fillRect(chartX(localX - (double) COL_SPACING /2), starRectY, (int) (colWidth + COL_SPACING)+1, STAR_RECT_HEIGHT);

            String numberText = String.valueOf((int) stars[i]);
            int numberWidth = fm.stringWidth(numberText);
            int numberX = (int) (localX + colWidth / 2.0 - numberWidth / 2.0);
            int numberY = chartY(areaH - 15);
            g.setColor(Color.BLACK);
            g.drawString(numberText, chartX(numberX), numberY);

            String starsText = "⭐".repeat(i);
            int starsWidth = fm.stringWidth(starsText);
            int starsX = (int) (localX + colWidth / 2.0 - starsWidth / 2.0);
            int starsY = starRectY + STAR_RECT_HEIGHT - 3;
            g.drawString(starsText, chartX(starsX), starsY);

            g.setColor(new Color(100, 150, 240));
        }
        g.setColor(Color.BLACK);
        g.drawLine(chartX(0), chartY(0), chartX(0), chartY(200));
        g.drawString("Offensive Stars", chartX(5), chartY(15));
    }

    protected void drawDefensiveColumns(Graphics2D g) {
        double areaX = 0;
        double areaY = CHART_HEIGHT / 2.0;
        double areaW = CHART_WIDTH / 2.0;
        double areaH = CHART_HEIGHT / 2.0;

        g.setColor(new Color(230, 230, 230));
        g.fillRect(chartX(areaX), chartY(areaY), (int) areaW, (int) areaH);

        double colWidth = (areaW - COL_SPACING * (NUM_COLS + 1)) / NUM_COLS;
        double colHeightMax = areaH - 25;

        double[] stars = calculateAmounts(this.dao.findDefencesByTag(getTag()));
        double[] heights = calculateRatios(stars, colHeightMax);

        FontMetrics fm = g.getFontMetrics();
        g.setColor(new Color(240, 100, 100));
        for (int i = 1; i <= NUM_COLS; i++) {
            double localX = areaX + COL_SPACING + (i - 1) * (colWidth + COL_SPACING);
            double h = heights[i];
            g.fillRect(chartX(localX), chartY(areaY), (int) colWidth, (int) h);

            String numberText = String.valueOf((int) stars[i]);
            int numberWidth = fm.stringWidth(numberText);
            int numberX = (int) (localX + colWidth / 2.0 - numberWidth / 2.0);
            int numberY = chartY(areaH + 20);
            g.setColor(Color.BLACK);
            g.drawString(numberText, chartX(numberX), numberY);
            g.setColor(new Color(240, 100, 100));

        }

        g.setColor(Color.BLACK);
        g.drawLine(chartX(0), chartY(areaY), chartX(0), chartY(areaY + areaH));
        g.drawString("Defensive Stars", chartX(5), chartY(CHART_HEIGHT - 5));
    }

    protected void drawComparisons(Graphics2D g) {
        double areaX = CHART_WIDTH / 2.0;
        double areaY = 0;
        double areaW = CHART_WIDTH / 2.0;
        double areaH = CHART_HEIGHT / 3.0;

        double[][] chartData1 = {this.dao.getAttackDurations(this.getTag()), this.dao.getDefenceDurations(this.getTag())};
        double[][] chartData2 = {this.dao.getAttackPercentages(this.getTag()), this.dao.getDefencePercentages(this.getTag())};
        double[][] chartData3 = {this.dao.getPlayerTownHalls(this.getTag()), this.dao.getAttackerTownHalls(this.getTag())};
        double[][][] allData = {chartData1, chartData2, chartData3};

        String[] titles = {"Attack/Defence Durations", "Attack/Defence Percentages", "Townhall Levels"};
        Color[] lineColors = {Color.BLUE, Color.RED};

        int leftPadding = 35;

        for (int c = 0; c < 3; c++) {
            double chartTopY = areaY + c * areaH;

            g.setColor(new Color(230, 230, 230));
            g.fillRect(chartX(areaX), chartY(chartTopY), (int) areaW, (int) areaH);

            g.setColor(Color.BLACK);
            g.drawLine(chartX(areaX), chartY(chartTopY + areaH), chartX(areaX + areaW), chartY(chartTopY + areaH)); // X-axis
            g.drawLine(chartX(areaX), chartY(chartTopY), chartX(areaX), chartY(chartTopY + areaH)); // Y-axis

            g.drawString(titles[c], chartX(areaX + 5), chartY(chartTopY + 15));

            double[] allValues = Arrays.stream(allData[c]).flatMapToDouble(Arrays::stream).toArray();
            double globalMax = Arrays.stream(allValues).max().orElse(1);
            double globalMin = Arrays.stream(allValues).min().orElse(0);

            double paddingFactor = 0.1;
            double paddedMin = globalMin - (globalMax - globalMin) * paddingFactor;
            double paddedMax = globalMax + (globalMax - globalMin) * paddingFactor;
            double paddedRange = paddedMax - paddedMin;

            g.drawString(String.format("%.0f", paddedMax), chartX(areaX + 5), chartY(chartTopY + 26));
            g.drawString(String.format("%.0f", paddedMin), chartX(areaX + 5), chartY(chartTopY + areaH - 5));

            for (int line = 0; line < 2; line++) {
                g.setColor(lineColors[line]);
                double[] data = allData[c][line];
                if (data.length == 0) continue;

                for (int i = 0; i < data.length - 1; i++) {
                    double norm1 = (data[i] - paddedMin) / paddedRange;
                    double norm2 = (data[i + 1] - paddedMin) / paddedRange;

                    double yOffset = 25;
                    double effectiveHeight = areaH - yOffset;
                    double y1 = chartTopY + yOffset + (1 - norm1) * effectiveHeight;
                    double y2 = chartTopY + yOffset + (1 - norm2) * effectiveHeight;
                    double x1 = areaX + leftPadding + (i / (double) (data.length - 1)) * (areaW - leftPadding);
                    double x2 = areaX + leftPadding + ((i + 1) / (double) (data.length - 1)) * (areaW - leftPadding);

                    g.drawLine(chartX(x1), chartY(y1), chartX(x2), chartY(y2));
                }
            }
        }
    }

    private double[] calculateAmounts(List<AttackEntity> attacks) {
        double[] stars = new double[4];
        for (AttackEntity ae : attacks) {
            stars[ae.getStars()]++;
        }
        return stars;
    }

    private double[] calculateRatios(double[] stars, double ratio) {
        double maxCount = Arrays.stream(stars).max().orElse(1);
        return Arrays.stream(stars)
                .map(s -> (s / maxCount) * ratio)
                .toArray();
    }
}