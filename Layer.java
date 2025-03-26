import java.util.List;
import java.util.ArrayList;


public class Layer {
    private List<Neuron> neurons;
    private double learnRate;
    Activation func;
    // for momentum
    private double alpha = 0.9;
    private boolean useMomentum = false;
    // for bold driver
    private double previousError = Double.MAX_VALUE;

    Layer (int layerLen, int inputLen, double learnRate, Activation func, boolean useMomentum) {
        this.learnRate = learnRate;
        neurons = new ArrayList<>();
        for (int i = 0; i < layerLen; i++) {
            neurons.add(new Neuron(inputLen, func));
        }
        this.func = func;
        this.useMomentum = useMomentum;
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
    // Backpropagation for the output node
    public void backward(double desired) {
        for (int i =0; i < neurons.size(); i++) {
            double delta = desired - neurons.get(i).getOutput();
            delta *= func.deriv(neurons.get(i).getOutput());
            neurons.get(i).setDelta(delta);
        }
    }

    // Backpropagation for the hidden layers
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
            // bold driver
            n.setPreviousBias(newBias);
            if (useMomentum) {
                newBias += learnRate * n.getDelta() * 1.0 + (alpha*n.getPrevBiasChange());
            }
            else
            {
                newBias += learnRate * n.getDelta() * 1.0;
            }
            // momentum
            n.setPrevBiasChange(newBias - n.getBias());
            n.setBias(newBias);
            // bold driver
            n.setPreviousWeights(n.getWeights());
            for (int i = 0; i < n.getWeights().size(); i++) {
                double newWeight = n.getWeight(i);


                //System.out.println(n.getWeight(i) + " + " + learnRate + " * " + n.getDelta() + " * " + n.getInputs().get(i));
                if (useMomentum) {
                    newWeight += learnRate *  n.getDelta() * n.getInputs().get(i) + (alpha*n.getPrevChange(i));
                }
                else {
                    newWeight += learnRate * n.getDelta() * n.getInputs().get(i);
                }
                // momentum
                n.setPrevChange(i, newWeight - n.getWeight(i));
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

    public void setLearnRate (double learnRate) {
        this.learnRate = learnRate;
    }


    // For momentum
public double getAlpha () {
        return alpha;
}

    public void setAlpha (double alpha) {
        this.alpha = alpha;
    }

}
