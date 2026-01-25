package logic;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MagicWriter {

    private static final String RESOURCE_PATH = "src/main/resources/";

    /**
     * Writes the long array to the resources folder as plain text.
     */
    public static void writeLongArrayBishop(long[] data) {
        File file = new File(RESOURCE_PATH+"bishop.txt");

        // Ensure parent directories exist
        file.getParentFile().mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (long val : data) {
                writer.println(val);
            }
            System.out.println("Successfully wrote " + data.length + " items to " + RESOURCE_PATH + "bishop.txt");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Reads the file from the resources folder and returns a long[].
     */
    public static long[] readLongArrayBishop() {
        List<Long> tempStore = new ArrayList<>();
        File file = new File(RESOURCE_PATH+"/bishop.txt");

        if (!file.exists()) {
            System.err.println("Read failed: File does not exist at " + file.getAbsolutePath());
            return new long[0];
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLong()) {
                tempStore.add(scanner.nextLong());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Convert List<Long> to primitive long[]
        return tempStore.stream().mapToLong(l -> l).toArray();
    }

    /**
     * Writes the long array to the resources folder as plain text.
     */
    public static void writeLongArrayRook(long[][] data) {
        File file = new File(RESOURCE_PATH+"rook.txt");

        // Ensure parent directories exist
        file.getParentFile().mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (long[] line : data) {
                for(long val: line)
                    writer.println(val);
            }
            System.out.println("Successfully wrote " + data.length + " items to " + RESOURCE_PATH + "bishop.txt");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Reads the file from the resources folder and returns a long[].
     */
    public static long[][] readLongArrayRook() {
        List<Long> tempStore = new ArrayList<>();
        File file = new File(RESOURCE_PATH);

        if (!file.exists()) {
            System.out.println("File not found at " + RESOURCE_PATH+ "bishop.txt");
            return new long[0][];
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLong()) {
                tempStore.add(scanner.nextLong());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Convert List<Long> to primitive long[][]
        long[][] matrix = new long[3][91];
        for (int i = 0; i < 273; i++) {
            matrix[i / 91][i % 91] = tempStore.get(i);
        }
        return matrix;
    }
}

