import smile.data.DataFrame;
import smile.io.Read;
import smile.math.MathEx;
import java.util.*;
import java.net.URL;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {

        URL url = Objects.requireNonNull(
                Main.class.getClassLoader().getResource("iris.data")
        );

        DataFrame df = Read.csv(Paths.get(url.toURI()));
        double[][] data = df.drop(4).toArray();

        double[] c1 = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            c1[i] = data[i][0];
        }

        System.out.println(
                MathEx.mean(c1) + " " +
                        MathEx.median(c1) + " " +
                        MathEx.sd(c1)
        );

        Random r = new Random();
        Set<Integer> pos = new HashSet<>();
        while (pos.size() < 20) {
            int row = r.nextInt(data.length);
            int col = r.nextInt(data[0].length);
            int id = row * 1000 + col;
            if (pos.add(id)) data[row][col] = Double.NaN;
        }

        for (int i = 0; i < data.length; i++) {
            if (Double.isNaN(data[i][0])) System.out.print(i + " ");
        }
        System.out.println();

        List<double[]> f = new ArrayList<>();
        for (double[] row : data) {
            if (row[2] > 1.5 && row[0] < 5.0) f.add(row.clone());
        }

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if (Double.isNaN(data[i][j])) data[i][j] = 0.0;
            }
        }

        int mid = data[0].length / 2;
        double[][] part1 = new double[data.length][mid];
        double[][] part2 = new double[data.length][mid];

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < mid; j++) {
                part1[i][j] = data[i][j];
                part2[i][j] = data[i][j + mid];
            }
        }

        Arrays.sort(part1, (a, b) -> Double.compare(a[0], b[0]));
        Arrays.sort(part2, (a, b) -> Double.compare(b[0], a[0]));

        double[][] merged = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            System.arraycopy(part1[i], 0, merged[i], 0, mid);
            System.arraycopy(part2[i], 0, merged[i], mid, mid);
        }

        Map<Double, Integer> counts = new HashMap<>();
        for (double[] row : merged) {
            for (double v : row) {
                counts.put(v, counts.getOrDefault(v, 0) + 1);
            }
        }

        double mode = 0;
        int max = -1;
        for (Map.Entry<Double, Integer> e : counts.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                mode = e.getKey();
            }
        }

        System.out.println("Mode: " + mode);
        doTransform(merged, 2);
    }

    static void doTransform(double[][] arr, int c) {
        double s = 0;
        for (double[] row : arr) s += row[c];
        double avg = s / arr.length;
        for (double[] row : arr) {
            if (row[c] < avg) row[c] *= 2;
            else row[c] /= 4;
        }
    }
}
