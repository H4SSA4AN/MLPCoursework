import java.nio.file.FileSystemNotFoundException;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

public class main {
    public static void main(String[] args) {

        cleaner hello = new cleaner();
        // hello.check("ouseNoErroneousStandardised.csv");

        List<List<Double>> riverData = hello.getOnlyRivers("shuffledRecords.txt");
        //System.out.println(riverData.size());
        //   System.out.println(riverData);
        //  riverData = hello.shuffle(riverData);
        //   hello.writeToFile(riverData);

        List<List<Double>> trainData = riverData.subList(0, 840); // Including skelton
        List<Double> trainLabel = hello.separateData(trainData, 3);// Only skelton labels
        List<List<Double>> validationData = riverData.subList(840, 1121);
        List<Double> validationLabel = hello.separateData(validationData, 3);
        List<List<Double>> testData = riverData.subList(1121, 1401);
        List<Double> testLabel = hello.separateData(testData, 3);
        // Need to remove skelton from trainData
        for (List<Double> row : trainData) {
            row.remove(row.size() - 1);
        }
        for (List<Double> row : testData) {
            row.remove(row.size() - 1);
        }
        for (List<Double> row : validationData) {
            row.remove(row.size() - 1);
        }
        /*
        System.out.println(labels);
        System.out.println(trainData);
        System.out.println(trainData.size());
        */

        //  System.out.println(trainData);
        //  System.out.println(trainLabel);
        Scanner takeData = new Scanner(System.in);

        System.out.println("How many hidden layers? ");
        int layerLen = takeData.nextInt();
        System.out.println("Please enter the size of each layer");
        int[] layers = new int[layerLen];
        for (int i = 0; i < layerLen; i++) {
            layers[i] = takeData.nextInt();
        }
        String useMomentum = "";
        System.out.println("Use momentum? ");
        useMomentum = takeData.next();
        String useBold = "";
        System.out.println("Use bold driver? ");
        useBold = takeData.next();
        System.out.println("Use annealing? ");
        String useAnneal = takeData.next();
        boolean anneal = false;
        boolean bold = false;
        if (useBold.equals("yes")) {
            bold = true;
        }

        if (useAnneal.equals("yes")) {

            System.out.println("Building a network with " + layers.length + " layers");

        }
            Activation sigm = new Sigmoid();
            Activation reloo = new Relu();
            Network network = null;

            if (useMomentum.equals("yes")) {
                network = new Network(trainData.get(0).size(), layers, 1, 0.1, reloo, sigm, true, bold, anneal);
            } else {
                network = new Network(trainData.get(0).size(), layers, 1, 0.1, sigm, sigm, false, bold, anneal);
            }


            String option = "no";
            do {
                System.out.println("How many epochs for training?");
                int count = takeData.nextInt();
                System.out.println("TRAINING");
                network.loop(count, trainData, trainLabel, count / 50);
                System.out.println("VALIDATING");
                network.validate(validationData, validationLabel);
                System.out.println("What would you like to do \n testing \n tweaks \n train");
                option = takeData.next();
                switch (option) {
                    case "tweaks":
                        String next = "";
                        do {
                            System.out.println("Would you like to change learn rate or alpha? ");
                            String choice = takeData.next();
                            switch (choice) {
                                case "learn":

                                    System.out.println("Please enter new learn rate. Current is " + network.getLearningRate());
                                    double learnRate = takeData.nextDouble();
                                    network.setLearningRate(learnRate);
                                    break;
                                case "alpha":
                                    System.out.println("Please enter new alpha. Current is " + network.getAlpha());
                                    double alpha = takeData.nextDouble();
                                    network.setAlpha(alpha);
                                    break;
                                default:
                                    choice = "go";
                                    break;
                            }

                        } while (next != "go");
                        System.out.println("Back to training!");
                        break;
                    case "train":
                        continue;
                    default:
                        option = "testing";
                }
            } while (option != "testing");

            System.out.println("Testing time");
            network.test(testData, testLabel);











        /*
    List<String> fileInfo = hello.loadNetworkFile("savedNetworks.txt");
        List<List<String>> networkInfo = hello.getNetworkInfo(fileInfo, 1);
      //  System.out.println(networkInfo);
        List<List<Double>> hiddenLayerInfo = hello.getHiddenLayerInfo(networkInfo);
        List<Double> outputLayerInfo = hello.getOutputLayerInfo(networkInfo);
*/






        /* Date, Crakehill, Skip Bridge, Westwick, Skelton, Arkengarth, East Cowton, Malham Tarn, Snaizeholme
        Need to predict mean flow in skelton one day ahead
        Everything before skelton is in mean daily flow, including skelton in cumecs
        Everything after skelton is daily rainfall total in mm
        */
        }
    }




