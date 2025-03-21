import java.util.ArrayList;
import java.util.List;

public class Neuron {
    private List<Double> weights;
    private double bias;
    private Activation func;
    private double delta;
    private List<Double> inputs;
    private double output;


    Neuron(int inputLen, Activation func) {
        weights = new ArrayList<>();
        bias = Math.random() * 2 - 1;
        for (int i = 0; i < inputLen; i++) {
            weights.add(Math.random() * 2 - 1);
        }
        this.func = func;
    }

    Neuron ( Activation func, List<Double> weights, double bias) {
        this.func = func;
        this.weights = weights;
        this.bias = bias;
    }

    // Weighted sum of inputs + bias, with activation function applied
    public void forward() {
        output = bias;
        for (int i = 0; i < weights.size(); i++) {
            output += weights.get(i) * inputs.get(i);
        }
        output = func.apply(output);
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta (double delta) {
        this.delta = delta;
    }

    public List<Double> getWeights() {
        return weights;
    }

    public double getOutput() {
        return output;
    }

    public void setInputs(List<Double> inputs)
    {
        this.inputs = inputs;
    }

    public double getWeight(int index)
    {
        return weights.get(index);
    }

    public void setWeight(int index, double newWeight) {
        weights.set(index, newWeight);
    }

    public double getBias()
    {
        return bias;
    }

    public void setBias(double newBias) {
        bias = newBias;
    }

}
