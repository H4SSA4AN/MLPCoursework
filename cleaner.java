import java.io.FileWriter;
import java.io.IOException;
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
                    System.out.println("Invalid input");
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
            newData.add(fileData.get(i).subList(1,13));
        }

        return newData;
    }

    // Function to separate the 2D array into a 1D array of each column, so i can find outliers easier, as well as standardise
    // 0-7, with 3 being skelton.
    public List<Double> separateData (List<List<Double>> data, int index)
    {
        List<Double> column = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            try {
                column.add(data.get(i).get(index));
            } catch (IndexOutOfBoundsException e) {
                System.out.println(i + " You fucked ");
            }
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

    public List<List<Double>> getRivers(List<List<Double>> data) {
        List<List<Double>> rivers = new ArrayList<>();

        for (List<Double> row : data) {
            rivers.add(row.subList(4,8));
        }

        return rivers;
    }

    public List<List<Double>> shuffle(List<List<Double>> data) {
        List<List<Double>> shuffled = data;

        Collections.shuffle(shuffled);

        return shuffled;
    }


    public void writeToFile(List<List<Double>> data) {
        try {
            FileWriter myWriter = new FileWriter("shuffledRecords.txt");
            for (List<Double> row : data) {
                for (int i = 4; i < row.size(); i++) {
                    if (i < row.size() - 1) {
                        {
                            myWriter.write(row.get(i) + ",");
                        }
                    }
                    else{
                        myWriter.write(row.get(i) + "");
                    }
                }
                myWriter.write("\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public List<List<Double>> getOnlyRivers(String name) {

        List<List<String>> data = readFile(name);
        //System.out.println(data);
      //  data = keepOnlyNumbers(data);
        List<List<Double>> onlyNums = stringToDouble(data);
      //  System.out.println(onlyNums);
    //    List<List<Double>> rivers = new ArrayList<>();
    //    rivers = getRivers(onlyNums);

        return onlyNums;

    }


    public void check(String name)
    {
        List<List<String>> data = readFile(name);
       // data = removeFirstTwoLines(data);
        data = keepOnlyNumbers(data);
        List<List<Double>> onlyNums = stringToDouble(data);
        List<Double> crakeHill = separateData(onlyNums, 0);
        double deviate = sd(crakeHill);

        List<List<Double>> rivers = new ArrayList<>();
        rivers = getRivers(onlyNums);

        System.out.println(data);
        System.out.println(data.size());
        System.out.println(onlyNums);
        System.out.println(onlyNums.size());
        System.out.println(rivers);
        System.out.println(rivers.size());
    }


    public List<String> loadNetworkFile(String filename)
    {
        List<String> fileInfo = new ArrayList<>();

        try{
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileInfo.add(line);
            }
            scanner.close();
            // fileinfo in format: layer weights, output weights, hidden func, output func, learnrate
            System.out.println(fileInfo.get(3));
        }catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return fileInfo;
    }

    // Break down the file into list of layers, output layer, make the network
    public List<List<String>> getNetworkInfo(List<String> fileInfo, int index)
    {
        int dashCount = 0;
        List<List<String>> networkInfo = new ArrayList<>();
        for (int i = 0; i < fileInfo.size(); i++) {
            if (fileInfo.get(i).contains("-"))
            {
                dashCount++;
                if(dashCount == index)
                {
                    // at the specified index, read the data
                    networkInfo.add(fileInfo.subList(i+1, i+4));
                }
            }
        }
        return networkInfo;
    }

    public List<List<Double>> getHiddenLayerInfo (List<List<String>> networkInfo) {
        List<String> hiddenLayer = new ArrayList<>();
        List<List<Double>> hiddenLayerInfo = new ArrayList<>();

        // get only the hidden layer information
         hiddenLayer = (networkInfo.get(0).subList(0,1));
         for (String line : hiddenLayer) {
             line = line.trim().substring(3, line.length()-3);
             for (String x : line.split("\\], \\["))
             {
                 List<Double> neuronWeight = new ArrayList<>();
                for (String y : x.split(", "))
                {
                    neuronWeight.add(Double.parseDouble(y));
                }
                hiddenLayerInfo.add(neuronWeight);
             }
         }

        return hiddenLayerInfo;
    }

    public List<Double> getOutputLayerInfo (List<List<String>> networkInfo) {
        String outputLayer;
        List<Double> outputLayerInfo = new ArrayList<>();

        // get only outputlayer information
        outputLayer = String.valueOf((networkInfo.get(0).subList(1,2)));
        outputLayer = outputLayer.substring(3,outputLayer.length() - 3);
        for (String line : outputLayer.split(",")) {
            outputLayerInfo.add(Double.parseDouble(line));
        }

        return outputLayerInfo;
    }


}
