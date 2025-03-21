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

        /*
        int[] hiddenLayers = {10};

        Network n = new Network(2, hiddenLayers, 1, 0.1, reloo, sigm);
        List<List<Double>> inputs = Arrays.asList(
                Arrays.asList(0.193, 0.345),
                Arrays.asList(0.769, 0.4),
                Arrays.asList(0.1, 0.233),
                Arrays.asList(0.564, 0.8),
                Arrays.asList(0.407, 0.833),
                Arrays.asList(0.818, 0.2),
                Arrays.asList(0.693, 0.316)
        );
        List<Double> labels = Arrays.asList(0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0);

        int count = 10000;
        n.loop(count, inputs, labels, 100);

        System.out.println(("After " + count + " iterations"));

        for (int i = 0; i < inputs.size(); i++) {
            n.test(inputs.get(i), labels.get(i));
        }


         */
        List<Double> u3Weights = Arrays.asList(new Double[] {3.0, 4.0});
        Neuron u3 = new Neuron(sigm, u3Weights, 1.0);
        List<Double> u4Weights = Arrays.asList(new Double[] {6.0, 5.0});
        Neuron u4 = new Neuron(sigm, u4Weights, -6.0);
        List<Double> u5Weights = Arrays.asList(new Double[] {2.0, 4.0});
        Neuron u5 = new Neuron(sigm, u5Weights, -3.92);

        List<Neuron> hiddenList = Arrays.asList(u3, u4);
        List<Neuron> outputList = Arrays.asList(u5);

        Layer hiddenLayer = new Layer(hiddenList, sigm, 0.1);
        Layer outputLayer = new Layer(outputList, sigm, 0.1);

        List<Double> inputs  = Arrays.asList(new Double[] {1.0, 0.0});
        double desired = 1.0;

        hiddenLayer.setInputs(inputs);
        hiddenLayer.forward();
        outputLayer.setInputs(hiddenLayer.getOutputs());
        outputLayer.forward();
        outputLayer.printOuts();
        outputLayer.backward(desired);
        hiddenLayer.backward(outputLayer);
        hiddenLayer.updateWeights();
        outputLayer.updateWeights();
        outputLayer.printWeights();
        System.out.println("Hidden wieghts now: ");
        hiddenLayer.printWeights();

    }
}
