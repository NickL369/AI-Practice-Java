import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<String[]> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("hw02/src/iris.data"));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            lines.add(line.split(","));
        }
        reader.close();

        int targetIndex = 4;
        double[][] data = new double[lines.size()][4];
        for (int i = 0; i < lines.size(); i++) {
            String[] row = lines.get(i);
            int colIdx = 0;
            for (int j = 0; j < row.length; j++) {
                if (j == targetIndex) continue;
                data[i][colIdx++] = Double.parseDouble(row[j]);
            }
        }

        double s = 0;
        double[] c1 = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            s += data[i][0];
            c1[i] = data[i][0];
        }
        double mean = s / data.length;
        Arrays.sort(c1);
        double median = c1[c1.length / 2];
        double v = 0;
        for (double val : c1) v += Math.pow(val - mean, 2);
        System.out.println("Mean: " + mean + " Med: " + median + " Std: " + Math.sqrt(v / data.length));

        Random rd = new Random();
        int nans = 0;
        while (nans < 20) {
            int r = rd.nextInt(data.length);
            int c = rd.nextInt(4);
            if (!Double.isNaN(data[r][c])) {
                data[r][c] = Double.NaN;
                nans++;
            }
        }

        System.out.print("NaNs in col 1: ");
        for (int i = 0; i < data.length; i++) {
            if (data[i][0] != data[i][0]) System.out.print(i + " ");
        }
        System.out.println();

        ArrayList<double[]> filteredList = new ArrayList<>();
        for (double[] row : data) {
            if (row[2] > 1.5 && row[0] < 5.0) {
                filteredList.add(row.clone());
            }
        }
        double[][] filteredArray = filteredList.toArray(new double[0][]);

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < 4; j++) {
                if (data[i][j] != data[i][j]) data[i][j] = 0.0;
            }
        }

        HashMap<Double, Integer> counts = new HashMap<>();
        for (double[] row : data) {
            for (double val : row) counts.put(val, counts.getOrDefault(val, 0) + 1);
        }
        for (Map.Entry<Double, Integer> entry : counts.entrySet()) {
            System.out.println("Val: " + entry.getKey() + " -> Count: " + entry.getValue());
        }

        double[][] part1 = new double[data.length][4];
        double[][] part2 = new double[data.length][4];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < 2; j++) part1[i][j] = data[i][j];
            for (int j = 2; j < 4; j++) part2[i][j] = data[i][j];
        }

        Arrays.sort(part1, (a, b) -> Double.compare(a[0], b[0]));
        Arrays.sort(part2, (a, b) -> Double.compare(b[0], a[0]));

        double[][] finalMerged = new double[data.length][4];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < 4; j++) {
                if (j < 2) finalMerged[i][j] = part1[i][j];
                else finalMerged[i][j] = part2[i][j];
            }
        }

        double mode = 0;
        int max = -1;
        for (double key : counts.keySet()) {
            if (counts.get(key) > max) {
                max = counts.get(key);
                mode = key;
            }
        }
        System.out.println("Mode: " + mode);

        transformColumn(finalMerged, 2);
    }

    public static void transformColumn(double[][] arr, int col) {
        double sum = 0;
        for (int i = 0; i < arr.length; i++) sum += arr[i][col];
        double avg = sum / arr.length;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i][col] < avg) arr[i][col] *= 2;
            else arr[i][col] /= 4;
        }
    }
}