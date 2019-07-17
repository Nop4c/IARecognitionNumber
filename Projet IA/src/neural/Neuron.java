package neural;

import java.util.ArrayList;

public class Neuron {

    private static final double LEARNING_RATIO = 0.1;

    private ArrayList<Integer> inputs;
    private ArrayList<Double> weights;
    private double output;

    public Neuron() {
        this.inputs = new ArrayList<>();
        this.weights = new ArrayList<>();
    }

    public void setInputs(ArrayList<Integer> inputs) {
        if (this.inputs.size() == 0) {
            this.inputs = new ArrayList<>(inputs);
            generateWeights();
        }

        this.inputs = new ArrayList<>(inputs);
    }

    private void generateWeights() {
        for (int i = 0; i < inputs.size(); i++) {
            weights.add(Math.random());
        }
    }
// S(WiXi)
    public void calculateOutput() {
        double sum = 0;

        for (int i = 0; i < inputs.size(); i++) {
            sum += inputs.get(i) * weights.get(i);
        }
//        sum += BIAS * biasWeight;
// sigmoid
        output = (1 / (1 + Math.exp(-sum)));
    }
// retropropagation calcul correction des poids
    public void adjustWeights(double delta) {
    	// 400 inputs
        for (int i = 0; i < inputs.size(); i++) {
            double d = weights.get(i);
            d += LEARNING_RATIO * delta * inputs.get(i);
            weights.set(i, d);
        }

//        biasWeight += LEARNING_RATIO * delta * BIAS;
    }

    public double getOutput() {
        calculateOutput();

        return output;
    }

}
