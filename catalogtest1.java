import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class catalogtest1 {

    public static void main(String[] args) {
        // Manual input of points
        Map<Integer, BigInteger> points = new HashMap<>();
        points.put(1, decodeBase("10", "4"));     // x = 1, y decoded from base 10
        points.put(2, decodeBase("2", "111"));    // x = 2, y decoded from base 2
        points.put(3, decodeBase("10", "12"));    // x = 3, y decoded from base 10
        points.put(6, decodeBase("4", "213"));    // x = 6, y decoded from base 4

        // Find the constant term 'c'
        double c = findConstantTerm(points);

        // Output the result
        System.out.println("The constant term c is: " + c);
    }

    // Method to decode a value from a given base to a decimal integer using BigInteger
    private static BigInteger decodeBase(String baseStr, String value) {
        int base = Integer.parseInt(baseStr);
        return new BigInteger(value, base);
    }

    private static double findConstantTerm(Map<Integer, BigInteger> points) {
        int k = points.size();
        if (k < 2) throw new IllegalArgumentException("At least two points are required");

        double[][] matrix = new double[k][k];
        double[] constants = new double[k];

        int i = 0;
        for (Map.Entry<Integer, BigInteger> entry : points.entrySet()) {
            int x = entry.getKey();
            BigInteger y = entry.getValue();

            for (int j = 0; j < k; j++) {
                matrix[i][j] = Math.pow(x, k - 1 - j);
            }
            constants[i] = -y.doubleValue();
            i++;
        }

        return gaussianElimination(matrix, constants);
    }

    private static double gaussianElimination(double[][] matrix, double[] constants) {
        int n = matrix.length;

        // Forward elimination
        for (int i = 0; i < n; i++) {
            // Find the pivot row
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(matrix[k][i]) > Math.abs(matrix[maxRow][i])) {
                    maxRow = k;
                }
            }

            // Swap rows
            double[] tempRow = matrix[i];
            matrix[i] = matrix[maxRow];
            matrix[maxRow] = tempRow;

            double tempValue = constants[i];
            constants[i] = constants[maxRow];
            constants[maxRow] = tempValue;

            // Eliminate columns
            for (int k = i + 1; k < n; k++) {
                double factor = matrix[k][i] / matrix[i][i];
                for (int j = i; j < n; j++) {
                    matrix[k][j] -= factor * matrix[i][j];
                }
                constants[k] -= factor * constants[i];
            }
        }

        // Back substitution
        double[] solution = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            solution[i] = constants[i];
            for (int j = i + 1; j < n; j++) {
                solution[i] -= matrix[i][j] * solution[j];
            }
            solution[i] /= matrix[i][i];
        }

        return solution[n - 1]*-1; // Return the constant term (c)
        
    }
}
