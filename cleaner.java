import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class cleaner {

    // Contains methods that take the data from the excel sheet
    // Randomises data, finds anomalies
    // Need to turn every row into an array
    // Whole dataset into a 2D array


    private List<List<String>> cleanData;
    private List<List<String>> rawList = new ArrayList<>();

    cleaner () {}


    // Reads excel sheet and puts all rows into raw array
    public void getRawData()
    {
        try {
            File file = new File("ouseDataCSV.csv");  // Specify the file path
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                List<String> row = List.of((scanner.nextLine().split(",")));
                rawList.add(row);
            }

            scanner.close();  // Close the scanner to free resources
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        cleanData();

    }

    public void cleanData() {
        // First 2 rows are useless, remove them
        // Third row has unnecessary commas, remove them
        // Also erroneous data needs to go
        cleanData = rawList;
        cleanData.remove(0);
        cleanData.remove(0);
        System.out.println(cleanData);
        List<String> data = cleanData.get(0);
        data = data.subList(0,9);
        System.out.println(data);
        cleanData.remove(0);
        cleanData.addFirst(data);

        System.out.println(cleanData);
    }

}
