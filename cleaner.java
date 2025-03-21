import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class cleaner {

    private List<List<String>> readFile(String fileName) {
        List<List<String>> list = new ArrayList<>();

        try{
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                list.add(List.of(line.split(",")));
            }
            scanner.close();
        }catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return list;
    }

    private double findMax (List<Double> data) {
        double max = 0;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) > max) {
                max = data.get(i);
            }
        }
        return max;
    }

    private double findMin (List<Double> data) {
        double min = findMax(data);

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) < min) {
                min = data.get(i);
            }
        }
        return min;
    }

    private List<List<String>> removeFirstTwoLines(List<List<String>> fileData) {
        fileData.remove(0);
        fileData.remove(0);

        return fileData;
    }

    private List<List<Double>> stringToDouble (List<List<String>> inputs)
    {
        List<List<Double>> data = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            List<Double> stringToNum = new ArrayList<>();
            for (String value : inputs.get(i)) {
                try {
                    stringToNum.add(Double.parseDouble(value));
                } catch (NumberFormatException e) { // The second there is a letter, remove the whole row
                    stringToNum.clear();
                    i++;
                }
            }
            if (!stringToNum.isEmpty()) {
                data.add(stringToNum);
            }
        }

        return data;
    }

    // Arrays only have the relevant columns, no unneccesary dates or words
    private List<List<String>> keepOnlyNumbers(List<List<String>> fileData) {
        List<List<String>> newData = new ArrayList<>();
        for (int i = 0; i < fileData.size(); i++) {
            newData.add(fileData.get(i).subList(1,9));
        }

        return newData;
    }

    // Function to separate the 2D array into a 1D array of each column, so i can find outliers easier, as well as standardise
    // 0-7, with 3 being skelton.
    private List<Double> separateData (List<List<Double>> data, int index)
    {
        List<Double> column = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            column.add(data.get(i).get(index));
        }

        return column;
    }

    private double findMean (List<Double> data) {
        double mean = 0;

        for (double num : data) {
            mean += num;
        }

        mean /= data.size();

        return mean;
    }

    private double sd (List<Double> column)
    {
        // calculate standard deviation of a column
        double deviation = 0;
        double mean = findMean(column);

        for (double num : column) {
            deviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(deviation / column.size());
    }

    // Returns the indecies of outliers, to be removed later
    private List<Integer> findOutliers (List<Double> data)
    {
        // Use modified Z score
        List<Integer> outliers = new ArrayList<>();
        Collections.sort(data);
        double median = data.get(data.size()/2);
        List<Double> deviations = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            deviations.add(Math.abs(data.get(i) - median));
        }
        Collections.sort(deviations);
        double MAD = deviations.get((int) (deviations.size()/2.0));
        System.out.println(MAD);

        for (int i = 0; i < data.size(); i++) {
            double Z = Math.abs((0.6745 * (data.get(i) - median)) / MAD);
            if (Z > 3.5)
            {
                outliers.add(i);
            }
        }

        return outliers;
    }


    public void check(String name)
    {
        List<List<String>> data = readFile(name);
        data = removeFirstTwoLines(data);
        data = keepOnlyNumbers(data);
        List<List<Double>> onlyNums = stringToDouble(data);
        List<Double> crakeHill = separateData(onlyNums, 0);
        double deviate = sd(crakeHill);

        System.out.println(data);
        System.out.println(data.size());
        System.out.println(onlyNums);
        System.out.println(onlyNums.size());
        System.out.println(deviate);
        System.out.println(findOutliers(crakeHill).size());
    }


}
