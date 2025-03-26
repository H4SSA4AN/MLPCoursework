import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

public class Network {
    // Collection of hidden layers and output layer
    // Should only be calling this in the main

    List<Layer> hiddenLayers;
    Layer outputLayer;
    double loss;
    double prevLoss = Double.MAX_VALUE;
    Activation hiddenFunc;
    Activation outputFunc;
    double learningRate;
    boolean boldDrive = false;
    boolean annealing = false;

    Network (int inputLen, int[] hiddenLayerSizes, int outputLayerSize, double learningRate, Activation hiddenFunc, Activation outputFunc, boolean momentum, boolean boldDrive, boolean annealing) {
        hiddenLayers = new ArrayList<>();
        hiddenLayers.add(new Layer(hiddenLayerSizes[0], inputLen, learningRate, hiddenFunc, momentum));
        if (hiddenLayerSizes.length > 1) {
            for (int i = 1; i < hiddenLayerSizes.length; i++) {
                hiddenLayers.add(new Layer(hiddenLayerSizes[i], hiddenLayers.get(i - 1).getNeurons().size(), learningRate, hiddenFunc, momentum));
            }
        }
        outputLayer = new Layer(outputLayerSize, hiddenLayers.getLast().getNeurons().size(), learningRate, outputFunc, momentum);

        // only keeping track of these values so they can be written to a file later, to recreate the network
        this.hiddenFunc = hiddenFunc;
        this.outputFunc = outputFunc;
        this.learningRate = learningRate;
        this.boldDrive = boldDrive;
        this.annealing = annealing;
    }

    Network (List<Layer> hiddenLayers, Layer outputLayer) {
        this.hiddenLayers = hiddenLayers;
        this.outputLayer = outputLayer;
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

    // Backpropagation

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
        prevLoss = loss;
    }

    /* BOLD DRIVER */
    public void bold()
    {
        if ((loss/prevLoss * 100) > 104)
        {
            System.out.println("Bold");
            learningRate *= 1.05;
        }
        else
        {
            learningRate *= 0.7;
            for (Layer l : hiddenLayers)
            {
                for (Neuron n : l.getNeurons())
                {
                    n.rollBack();
                }
            }
            for (Neuron n : outputLayer.getNeurons())
            {
                n.rollBack();
            }
        }

        prevLoss = loss;

        if (learningRate < 0.01)
        {
            learningRate = 0.01;
        }
        if (learningRate > 0.5)
        {
            learningRate = 0.5;
        }

        for (Layer l : hiddenLayers)
        {
            l.setLearnRate(learningRate);
        }
        outputLayer.setLearnRate(learningRate);
    }

    /* ANNEALING */
    private void anneal(double currentEpoch, double maxEpoch)
    {
        double p = 0.01;
        double q = 0.1;
        learningRate = p;
        learningRate += (q-p) * (1 - (1 / (1 + Math.exp(10-((20*currentEpoch) / maxEpoch)))));
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
        for (int i = 0; i <= count; i++) {
            epoch(inputs, desiredOutputs);
            if (annealing)
            {
                anneal(count, i);
            }
            if (i % printLoss == 0)
            {
                System.out.println(i + "\t" + loss);
                if (boldDrive)
                {
                    bold();
                }
            }
        }
    }

    public void validate(List<List<Double>> validationData, List<Double> desiredOutputs)
    {
        List<Double> predicteds = new ArrayList<>();
        List<Double> trues = new ArrayList<>();
        for (int i = 0; i < validationData.size(); i++) {
            hiddenLayers.getFirst().setInputs(validationData.get(i));
            forwardPass();
            predicteds.add(outputLayer.getOutputs().get(0));
            trues.add(desiredOutputs.get(i));
        }
        loss = MSE(predicteds, trues);
        System.out.println("LOSS: " + loss);
    }

    public void test(List<List<Double>> testData, List<Double> testLabel) {

        for (int i = 0; i < testData.size(); i++) {
            hiddenLayers.get(0).setInputs(testData.get(i));
            forwardPass();
            getFinalOut();
            System.out.println("\t" + testLabel.get(i));
        }
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

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        // SET EVERY NODE IN EVERY LAYER
        for (Layer l : hiddenLayers)
        {
            l.setLearnRate(learningRate);
        }
        outputLayer.setLearnRate(learningRate);
    }

    public void setAlpha(double alpha) {
        for (Layer l : hiddenLayers)
        {
            l.setAlpha(alpha);
        }
        outputLayer.setAlpha(alpha);
    }

    public double getAlpha() {
        return outputLayer.getAlpha();
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
            System.out.print(n.getOutput() + " ");
        }
    }

    public void saveNet() {
        // Write the weights to 2D list for each layer
        // Multiple hidden layers, only one output layer
        List<List<List<Double>>> hiddenWeights = new ArrayList<>();
        List<List<Double>> outWeights = new ArrayList<>();

        for (Layer l : hiddenLayers) {
            List<List<Double>> neuronWeight = new ArrayList<>();
            for (Neuron n : l.getNeurons()) {
                neuronWeight.add(n.getWeights());
            }
            hiddenWeights.add(neuronWeight);
        }

        for (Neuron n : outputLayer.getNeurons()) {
            outWeights.add(n.getWeights());
        }

        try {
            FileWriter myWriter = new FileWriter("savedNetworks.txt",true);
            myWriter.write(" - \n");
            myWriter.write(hiddenWeights.toString() + "\n");
            myWriter.write(outWeights.toString());
            myWriter.write("\n " + hiddenFunc.asWord() + " + " + outputFunc.asWord() + " + " + learningRate);
            myWriter.write("\n  LOSS : " + loss + " \n - \n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }



        System.out.println("Saved!");


    }


}
