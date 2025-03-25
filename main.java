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

        List<List<Double>> riverData = hello.getOnlyRivers("ouseNoErroneousStandardised.csv");
       // System.out.println(riverData);
        List<List<Double>> trainData = riverData.subList(0, 870); // Including skelton
        List<Double> labels = hello.separateData(trainData, 3); // Only skelton
        // Need to remove skelton from trainData
        for (List<Double> row : trainData) {
            row.remove(row.size() - 1);
        }
        System.out.println(labels);
        System.out.println(trainData);

        Activation sigm = new Sigmoid();
        Activation reloo = new Relu();

        int[] hiddenLayer = {12, 10};

        System.out.println(trainData.get(0).size());

        Network onlyRiversAI = new Network(trainData.get(0).size(), hiddenLayer, 1,0.1, reloo, sigm);

        int count = 100000;
        onlyRiversAI.loop(count, trainData, labels, 10000);


        for (int i = 0; i < 30; i++)
        {
            onlyRiversAI.test(trainData.get(i), labels.get(i));
        }

        onlyRiversAI.saveNet();




        /* Date, Crakehill, Skip Bridge, Westwick, Skelton, Arkengarth, East Cowton, Malham Tarn, Snaizeholme
        Need to predict mean flow in skelton one day ahead
        Everything before skelton is in mean daily flow, including skelton in cumecs
        Everything after skelton is daily rainfall total in mm
        */
    }
}
