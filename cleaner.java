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
    private List<List<Double>> numberList = new ArrayList<>();

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
        System.out.println(cleanData.size());
    }
    public void makeNumList()
    {
        for (int i = 0; i < cleanData.size(); i++) {
            List<Double> numbers = new ArrayList<>();
            for (int j = 1; j < cleanData.get(i).size(); j++) {
                try {
                    numbers.add(Double.parseDouble(cleanData.get(i).get(j)));
                }
                catch (NumberFormatException e) {
                    numbers.clear();
                    continue;
                }
            }
            if (!numbers.isEmpty()) {
                numberList.add(numbers);
            }
        }

        for (List<Double> data : numberList) {
            if (data.isEmpty()) {
                numberList.remove(data);
            }
        }

        System.out.println(numberList);
        System.out.println(numberList.size());
    }

    public List<List<Double>> getOutliers() {
        // Need to find any non numeric values, as well as outrageous values, over 300 for example
        List<List<Double>> outliers = new ArrayList<>();

        for (int i = 0; i< numberList.size(); i++) {
            List<Double> erroneous = new ArrayList<>();
            for (int j = 1; j < numberList.get(i).size(); j++) {
                // Skip first value as its date
            }
        }


        return outliers;
    }

}
