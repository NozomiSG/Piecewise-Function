import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassificationDecisionTree {
    public static double[] x = {-2, -1, 0, 1, 2, 3, 4, 5, 6};
    public static int[] y = {1, 0, 0, 1, 0, 1, 1, 1, 0};
    public static double[] splitPoint = {-1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5};
    public static double sp1, sp2, sp3;
    public static List<Integer> part1, part2, part3, part4;
    public static double m1, m2, m3, m4;
    public static double minEntropy = Double.MAX_VALUE;
    public static double[] min_SplitPoint;

    public static void buildTree() {
        // Recursively choose three splitPoint to calculate the MAE value
        for (int a=0; a<splitPoint.length; a++) {
            for (int b=a; b<splitPoint.length; b++) {
                for (int c=b; c<splitPoint.length; c++) {
                    splitData(a, b, c);
                    double[] splitPoints = new double[]{sp1, sp2, sp3};
                    double result = calculateIG();
                    if (minEntropy > result) {
                        System.out.println("Function updated!");
                        minEntropy = result;
                        min_SplitPoint = splitPoints;
                    }
                    System.out.println("\n");
                }
            }
        }
        System.out.printf("The minimum MAE is: %.3f\n", minEntropy);
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

    public static double calculateIG() {
        double IG1, IG2, IG3, IG4;
        double IG1_0, IG2_0, IG3_0, IG4_0;
        IG1 = IG2 = IG3 = IG4 = 0;
        IG1_0 = IG2_0 = IG3_0 = IG4_0 = 0;

        for (int i1=0; i1<part1.size(); i1++) if (part1.get(i1) == 0) IG1_0++;
        for (int i2=0; i2<part2.size(); i2++) if (part2.get(i2) == 0) IG2_0++;
        for (int i3=0; i3<part3.size(); i3++) if (part3.get(i3) == 0) IG3_0++;
        for (int i4=0; i4<part4.size(); i4++) if (part4.get(i4) == 0) IG4_0++;

        if (part1.size() != 0) {
            IG1 = (part1.size() / 9.0) * I(IG1_0, part1.size() - IG1_0);
        }
        if (part2.size() != 0) {
            IG2 = (part2.size() / 9.0) * I(IG2_0, part2.size() - IG2_0);
        }
        if (part3.size() != 0) {
            IG3 = (part3.size() / 9.0) * I(IG3_0, part3.size() - IG3_0);
        }
        if (part4.size() != 0) {
            IG4 = (part4.size() / 9.0) * I(IG4_0, part4.size() - IG4_0);
        }
        double totalIG = IG1+IG2+IG3+IG4;
        System.out.printf("Current IG: %.3f\n", totalIG);
        return totalIG;
    }

    public static double I(double p, double n) {
        return -p/(p+n)*log(p/(p+n), 2.0) - n/(p+n)*log(n/(p+n), 2.0);
    }

    public static double log(double value, double base) {
        if (value == 0)
            return 0;
        return Math.log(value) / Math.log(base);
        }

    public static void predictValue(double[] splitPoints, double num) {
        initValue();
        sp1 = splitPoints[0];
        sp2 = splitPoints[1];
        sp3 = splitPoints[2];
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
        int IG_0 = 0;
        if (num<splitPoints[0]) {
            for (int i1=0; i1<part1.size(); i1++) if (part1.get(i1) == 0) IG_0++;
            if (2*IG_0 > part1.size())
                System.out.printf("The result for %.1f should be 0\n", num);
            else if (2*IG_0 == part1.size())
                System.out.printf("The result for %.1f should be either 0 or 1\n", num);
            else
                System.out.printf("The result for %.1f should be 1\n", num);
        }
        else if (num<splitPoints[1]) {
            for (int i2=0; i2<part2.size(); i2++) if (part2.get(i2) == 0) IG_0++;
            if (2*IG_0 > part2.size())
                System.out.printf("The result for %.1f should be 0\n", num);
            else if (2*IG_0 == part2.size())
                System.out.printf("The result for %.1f should be either 0 or 1\n", num);
            else
                System.out.printf("The result for %.1f should be 1\n", num);
        }
        else if (num<splitPoints[2]) {
            for (int i3=0; i3<part3.size(); i3++) if (part3.get(i3) == 0) IG_0++;
            if (2*IG_0 > part3.size())
                System.out.printf("The result for %.1f should be 0\n", num);
            else if (2*IG_0 == part3.size())
                System.out.printf("The result for %.1f should be either 0 or 1\n", num);
            else
                System.out.printf("The result for %.1f should be 1\n", num);
        }
        else {
            for (int i4 = 0; i4 < part4.size(); i4++) if (part4.get(i4) == 0) IG_0++;
            if (2 * IG_0 > part4.size())
                System.out.printf("The result for %.1f should be 0\n", num);
            else if (2 * IG_0 == part4.size())
                System.out.printf("The result for %.1f should be either 0 or 1\n", num);
            else
                System.out.printf("The result for %.1f should be 1\n", num);
        }
    }

    public static void main(String[] args) {
        buildTree();
        predictValue(min_SplitPoint, -0.2);
        predictValue(min_SplitPoint, 3.4);

    }
}
