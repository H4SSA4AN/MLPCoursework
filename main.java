import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main {
    public static void main(String[] args) {
        /*
        cleaner hello = new cleaner();
        hello.check("ouseDataCSV.csv");

         */

        /* Date, Crakehill, Skip Bridge, Westwick, Skelton, Arkengarth, East Cowton, Malham Tarn, Snaizeholme
        Need to predict mean flow in skelton one day ahead
        Everything before skelton is in mean daily flow, including skelton in cumecs
        Everything after skelton is daily rainfall total in mm
         */

        Activation sigm = new Sigmoid();
        Activation reloo = new Relu();
        int[] hiddenLayers = {5};

        Network n = new Network(2, hiddenLayers, 1, 0.1, sigm, sigm);
        List<List<Double>> inputs = Arrays.asList(
                Arrays.asList(0.193, 0.345),
                Arrays.asList(0.3, 0.7),
                Arrays.asList(0.769, 0.4),
                Arrays.asList(0.1, 0.233),
                Arrays.asList(0.564, 0.8),
                Arrays.asList(0.407, 0.833),
                Arrays.asList(0.818, 0.2),
                Arrays.asList(0.693, 0.316)
        );
        List<Double> labels = Arrays.asList(0.0, 1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0);

        int count = 50000;
        n.loop(count, inputs, labels);

        System.out.println(("After " + count + " iterations"));

        for (int i = 0; i < inputs.size(); i++) {
            n.test(inputs.get(i), labels.get(i));
        }



    }
}
