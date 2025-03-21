import java.util.List;
import java.util.ArrayList;


public class Layer {
    List<Neuron> neurons;
    List<Double> outputs;
    double learnRate;
    Activation func;

    Layer (int layerLen, int inputLen, double learnRate, Activation func) {
        this.learnRate = learnRate;
        for (int i = 0; i < layerLen; i++) {
            neurons.add(new Neuron(inputLen, func));
        }
        outputs = new ArrayList<>();
        this.func = func;
    }

    public void forward() {
        outputs.clear();
        for (Neuron n : neurons) {
            n.forward();
            outputs.add(n.getOutput());
        }
    }

    // Back pass for output node(s)
    // Set the deltas list to whatever we get here
    public void backward(int desired) {
        for (int i =0; i < neurons.size(); i++) {
            double delta = desired - neurons.get(i).getOutput();
            delta *= func.deriv(neurons.get(i).getOutput());
            neurons.get(i).setDelta(delta);
        }
    }

    // Need to find the weight connecting current node and node in the ahead layer
    public void backward(Layer aheadLayer) {
        List<Neuron> aheadNeurons = aheadLayer.neurons;
        for (int i=0; i < aheadNeurons.size(); i++) {
            // loop through neurons, take output of current neuron and delta of ahead neuron, and use the weight
            for (int j = 0; j < neurons.size(); j++) {
                double delta = func.deriv(neurons.get(j).getOutput());
                delta *= aheadNeurons.get(i).getOutput() * aheadNeurons.get(i).getWeight(j);
                neurons.get(j).setDelta(delta);
            }
        }
    }

    public List<Double> getOutputs() {
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

}
