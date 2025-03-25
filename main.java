import java.nio.file.FileSystemNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
       // hello.writeToFile(riverData);

        List<List<Double>> trainData = riverData.subList(0, 853); // Including skelton
        List<Double> trainLabel = hello.separateData(trainData, 3);// Only skelton labels
        List<List<Double>> validationData = riverData.subList(854, 1136);
        List<Double> validationLabel = hello.separateData(validationData, 3);
        List<List<Double>> testData = riverData.subList(1136, 1422);
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

        System.out.println(validationData);
        System.out.println(validationLabel);


        Activation sigm = new Sigmoid();
        Activation reloo = new Relu();

        int[] hiddenLayer = {8};

     //   System.out.println(trainData.get(0).size());

        Network onlyRiversAI = new Network(trainData.get(0).size(), hiddenLayer, 1,0.1, sigm, sigm);

        int count = 50000;
  //      onlyRiversAI.loop(count, trainData, trainLabel, 1000);


        for (int i = 0; i < validationData.size(); i++)
        {
  //          onlyRiversAI.test(validationData.get(i), validationLabel.get(i));
        }

   //     onlyRiversAI.saveNet();

    List<String> fileInfo = hello.loadNetworkFile("savedNetworks.txt");
        List<List<String>> networkInfo = hello.getNetworkInfo(fileInfo, 1);
      //  System.out.println(networkInfo);
        List<List<Double>> hiddenLayerInfo = hello.getHiddenLayerInfo(networkInfo);
        List<Double> outputLayerInfo = hello.getOutputLayerInfo(networkInfo);







        /* Date, Crakehill, Skip Bridge, Westwick, Skelton, Arkengarth, East Cowton, Malham Tarn, Snaizeholme
        Need to predict mean flow in skelton one day ahead
        Everything before skelton is in mean daily flow, including skelton in cumecs
        Everything after skelton is daily rainfall total in mm
        */
    }

}



