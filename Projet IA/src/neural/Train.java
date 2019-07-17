package neural;

import data.ReadWriteFile;

import java.util.ArrayList;

public class Train {

    private Network network;
    private ArrayList<TrainingSet> trainingSets;

    public Train() {
        this.network = new Network();
        this.network.addNeurons(10);
        this.trainingSets = ReadWriteFile.readTrainingSets();
    }

    public void train(long count) {
        for (long i = 0; i < count; i++) {
            int index = ((int) (Math.random() * trainingSets.size()));
            System.out.println("index : " + index);
            TrainingSet set = trainingSets.get(index);
            network.setInputs(set.getInputs());
            network.adjustWages(set.getGoodOutput());
        }
        System.out.println(trainingSets.size());
    }

    public void setInputs(ArrayList<Integer> inputs) {
        network.setInputs(inputs);
    }

    public void addTrainingSet(TrainingSet newSet) {
        trainingSets.add(newSet);
    }

    public ArrayList<Double> getOutputs() {
        return network.getOutputs();
    }

}
