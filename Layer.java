import java.util.List;
import java.util.ArrayList;


public class Layer {
    private List<Neuron> neurons;
    private double learnRate;
    Activation func;

    Layer (int layerLen, int inputLen, double learnRate, Activation func) {
        this.learnRate = learnRate;
        neurons = new ArrayList<>();
        for (int i = 0; i < layerLen; i++) {
            neurons.add(new Neuron(inputLen, func));
        }
        this.func = func;
    }

    // To make a custom network for testing against the lecture ANN
    Layer (List<Neuron> neurons, Activation func, double learnRate)
    {
        this.neurons = neurons;
        this.func = func;
        this.learnRate = learnRate;
    }

    public void forward() {
        for (Neuron n : neurons) {
            n.forward();
        }
    }

    // Back pass for output node(s)
    // Set the deltas list to whatever we get here
    public void backward(double desired) {
        for (int i =0; i < neurons.size(); i++) {
            double delta = desired - neurons.get(i).getOutput();
            delta *= func.deriv(neurons.get(i).getOutput());
            neurons.get(i).setDelta(delta);
        }
    }

    // Need to find the weight connecting current node and node in the ahead layer
    public void backward(Layer aheadLayer) {
        List<Neuron> aheadNeurons = aheadLayer.getNeurons();
        for (int i=0; i < aheadNeurons.size(); i++) {
            // loop through neurons, take output of current neuron and delta of ahead neuron, and use the weight
            for (int j = 0; j < neurons.size(); j++) {
                double delta = func.deriv(neurons.get(j).getOutput());
                delta *= aheadNeurons.get(i).getDelta() * aheadNeurons.get(i).getWeight(j);
                neurons.get(j).setDelta(delta);
            }
        }
    }

    public void updateWeights() {
        for (Neuron n : neurons) {
            double newBias = n.getBias();
            newBias += learnRate * n.getDelta() * 1.0;
            n.setBias(newBias);
            for (int i = 0; i < n.getWeights().size(); i++) {
                double newWeight = n.getWeight(i);
                newWeight += learnRate *  n.getDelta() * n.getOutput();
                // System.out.println(learnRate + " * " + n.getDelta() + " * " + n.getOutput()  + " = " + newWeight);
                n.setWeight(i, newWeight);
            }
        }
    }

    public void printWeights() {
        for (Neuron n : neurons) {
            System.out.println("BIAS : " + n.getBias());
            System.out.println(n.getWeights());
        }
    }

    public void printOuts()
    {
        for (Neuron n : neurons) {
            System.out.println(n.getOutput());
        }
    }

    public List<Double> getOutputs() {
        List<Double> outputs = new ArrayList<>();
        for (Neuron n : neurons) {
            outputs.add(n.getOutput());
        }
        return outputs;
    }

    public void setInputs (List<Double> inputs) {
        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).setInputs(inputs);
        }
    }

    public List<Neuron> getNeurons () {
        return neurons;
    }

    public void printDeltas()
    {
        for (Neuron n : neurons) {
            System.out.println(n.getDelta());
        }
    }

}
