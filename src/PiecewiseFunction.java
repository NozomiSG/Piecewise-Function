import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PiecewiseFunction {

    public static double[] x = {-2, -1, 0, 1, 2, 3, 4, 5, 6};
    public static double[] y = {-28, 1, 12, 11, 4, -3, 4, 7, 36};
    public static double[] splitPoint = {-1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5};
    public static double sp1, sp2, sp3;
    public static List<Double> part1, part2, part3, part4;
    public static double m1, m2, m3, m4;
    public static double minMAE = Double.MAX_VALUE;
    public static double[] min_MeanArray;
    public static double[] min_SplitPoint;

    public static void buildTree() {
        // Recursively choose three splitPoint to calculate the MAE value
        for (int a=0; a<splitPoint.length; a++) {
            for (int b=a+1; b<splitPoint.length; b++) {
                for (int c=b+1; c<splitPoint.length; c++) {
                    splitData(a, b, c);
                    calculateMean();
                    double[] meanArray = new double[]{m1, m2, m3, m4};
                    double[] splitPoints = new double[]{sp1, sp2, sp3};
                    double result = calculateMAE(splitPoints, meanArray);
                    if (minMAE > result) {
                        System.out.println("Update function");
                        minMAE = result;
                        min_MeanArray = meanArray;
                        min_SplitPoint = splitPoints;
                    }
                }
            }
        }
        System.out.printf("The minimum MAE is: %.3f\n", minMAE);
        System.out.println("The result split points array: "+ Arrays.toString(min_SplitPoint));
    }

    public static void splitData(int a, int b, int c) {
        initValue();
        // Divide value into four parts
        sp1 = splitPoint[a];
        sp2 = splitPoint[b];
        sp3 = splitPoint[c];
        System.out.printf("Current split point is: %.1f, %.1f, %.1f\n", sp1, sp2, sp3);
        for (int i=0; i<x.length; i++) {
            if (x[i] < sp1)
                part1.add(y[i]);
            else if (x[i] < sp2)
                part2.add(y[i]);
            else if (x[i] < sp3)
                part3.add(y[i]);
            else
                part4.add(y[i]);
        }
        System.out.println("Part1: "+part1);
        System.out.println("Part2: "+part2);
        System.out.println("Part3: "+part3);
        System.out.println("Part4: "+part4);
    }

    public static void initValue() {
        sp1 = sp2 = sp3 = 0;
        m1 = m2 = m3 = m4 = 0;
        part1 = new ArrayList<>();
        part2 = new ArrayList<>();
        part3 = new ArrayList<>();
        part4 = new ArrayList<>();
    }

    public static void calculateMean() {
        for (int p1=0; p1<part1.size(); p1++)
            m1 += part1.get(p1);
        for (int p2=0; p2<part2.size(); p2++)
            m2 += part2.get(p2);
        for (int p3=0; p3<part3.size(); p3++)
            m3 += part3.get(p3);
        for (int p4=0; p4<part4.size(); p4++)
            m4 += part4.get(p4);
        m1 /= part1.size();
        m2 /= part2.size();
        m3 /= part3.size();
        m4 /= part4.size();
        System.out.println("Mean values of four parts:");
        System.out.printf("part1: %.3f ", m1);
        System.out.printf("part2: %.3f ", m2);
        System.out.printf("part3: %.3f ", m3);
        System.out.printf("part4: %.3f\n", m4);
    }

    public static double calculateMAE(double[] splitPoints, double[] meanArray) {
        double count = 0;
        for (int j=0; j<y.length; j++) {
            if (x[j] < splitPoints[0])
                count += Math.abs(y[j] - meanArray[0]);
            else if (x[j] < splitPoints[1])
                count += Math.abs(y[j] - meanArray[1]);
            else if (x[j] < splitPoints[2])
                count += Math.abs(y[j] - meanArray[2]);
            else
                count += Math.abs(y[j] - meanArray[3]);
        }
        System.out.printf("Current MAE: %.3f", count/9);
        System.out.println("\n");
        return count / 9;
    }

    public static void predictValue(double[] splitPoints, double[] meanArray, double x) {
        if (x<splitPoints[0]) {
            System.out.printf("The predicted value for x = %f is %.3f\n", x, meanArray[0]);
        }
        else if (x<splitPoints[1]) {
            System.out.printf("The predicted value for x = %f is %.3f\n", x, meanArray[1]);
        }
        else if (x<splitPoints[2]) {
            System.out.printf("The predicted value for x = %f is %.3f\n", x, meanArray[2]);
        }
        else {
            System.out.printf("The predicted value for x = %f is %.3f\n", x, meanArray[3]);
        }
    }

    public static void main(String[] args) {
        buildTree();
        predictValue(min_SplitPoint, min_MeanArray, -0.2);
        predictValue(min_SplitPoint, min_MeanArray, 3.4);
    }
}

