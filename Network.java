import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Network {
    // Collection of hidden layers and output layer
    // Should only be calling this in the main

    List<Layer> hiddenLayers;
    Layer outputLayer;
    double loss;

    Network (int inputLen, int[] hiddenLayerSizes, int outputLayerSize, double learningRate, Activation hiddenFunc, Activation outputFunc) {
        hiddenLayers = new ArrayList<>();
        hiddenLayers.add(new Layer(hiddenLayerSizes[0], inputLen, learningRate, hiddenFunc));
        if (hiddenLayerSizes.length > 1) {
            for (int i = 1; i < hiddenLayerSizes.length; i++) {
                hiddenLayers.add(new Layer(hiddenLayerSizes[i], hiddenLayers.get(i - 1).getNeurons().size(), learningRate, hiddenFunc));
            }
        }
        outputLayer = new Layer(outputLayerSize, hiddenLayers.getLast().getNeurons().size(), learningRate, outputFunc);
    }

    public void setInputs(List<Double> inputs) {
        for (Neuron n : hiddenLayers.get(0).getNeurons()) {
            n.setInputs(inputs);
        }
    }

    // Need to do first layer, then loop through rest
    public void forwardPass() {
        hiddenLayers.get(0).forward();
        for (int i = 1; i < hiddenLayers.size(); i++) {
            hiddenLayers.get(i).setInputs(hiddenLayers.get(i-1).getOutputs());
            hiddenLayers.get(i).forward();
        }
        // Put the final hidden layer outputs into a list, pass the list into the output layer as inputs
        List<Double> hiddenOut = new ArrayList<>();
        for (int i = 0; i < hiddenLayers.getLast().getNeurons().size(); i++) {
            hiddenOut.add(hiddenLayers.getLast().getNeurons().get(i).getOutput());
        }
        outputLayer.setInputs(hiddenOut);
        outputLayer.forward();
    }

    public void backPass(double desired) {
        // Do output layer first, then put the deltas into the previous hidden layer, and so on
        outputLayer.backward(desired);
        hiddenLayers.getLast().backward(outputLayer);
        for (int i = hiddenLayers.size() - 2; i >= 0; i--) {
            hiddenLayers.get(i).backward(hiddenLayers.get(i+1));
        }
    }

    public void updateWeights() {
        for (Layer l : hiddenLayers) {
            l.updateWeights();
        }
        outputLayer.updateWeights();
    }


    public void epoch(List<List<Double>> inputs, List<Double> desiredOutputs)
    {
        List<Double> predicteds = new ArrayList<>();
        List<Double> trues = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            hiddenLayers.getFirst().setInputs(inputs.get(i));
            forwardPass();
            predicteds.add(outputLayer.getOutputs().get(0));
            backPass(desiredOutputs.get(i));
            trues.add(desiredOutputs.get(i));
            updateWeights();
        }
        loss = MSE(predicteds, trues);
    }

    // This is where we do showing the loss
    public void loop(int count, List<List<Double>> inputs, List<Double> desiredOutputs, int printLoss)
    {
        for (int i = 0; i < count; i++) {
            epoch(inputs, desiredOutputs);
            if (i % printLoss == 0)
            {
                System.out.println(i + "\t" + loss);
            }
        }
    }

    public void test(List<Double> inputs, double desiredOutput) {
        hiddenLayers.get(0).setInputs(inputs);
        forwardPass();
        getFinalOut();
        System.out.println(" Looking For : " + desiredOutput);
    }


    // MSE for a single output node, as each list is 1D
    public double MSE (List<Double> predicteds, List<Double> actuals) {
        double sum = 0.0;
        for (int i = 0; i < predicteds.size(); i++) {
            sum += Math.pow(actuals.get(i) - predicteds.get(i), 2);
        }
        return sum / predicteds.size();
    }

    public double multipleOutMSE (List<List<Double>> predicteds, List<List<Double>> actuals) {
        double sum = 0.0;

        for (int i = 0; i < predicteds.size(); i++) {
            for (int j=0; j< predicteds.get(i).size(); j++) {
                sum += Math.pow(actuals.get(i).get(j) - predicteds.get(i).get(j), 2);
            }
        }

        return sum / predicteds.size();
    }



    public void printOutputs() {
        for (Layer l : hiddenLayers) {
            for (Neuron n : l.getNeurons()) {
                System.out.println(n.getOutput());
            }
        }
        for (Neuron n : outputLayer.getNeurons()) {
            System.out.println(n.getOutput());
        }
    }

    public void getFinalOut() {
        for (Neuron n : outputLayer.getNeurons()) {
            System.out.println(n.getOutput());
        }
    }


}
