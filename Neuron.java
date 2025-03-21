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

    public double getBias()
    {
        return bias;
    }

}
