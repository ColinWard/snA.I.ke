package com.colinward;

/**
 * Created by Colin Ward on 6/28/2016.
 */
public class NeuralNet {
    Neuron[] inputLayer;
    Neuron[] hiddenLayer;
    Neuron[] outputLayer;
    double[] initInputs;
    double[] hiddenInputs;
    double[] outputInputs;
    double[] finalOutput;

    double fitness;
    boolean setup = true;

    public NeuralNet(int numInput, int numHidden, int numOutput, double[] inputs) {
        inputLayer = new Neuron[numInput];
        hiddenLayer = new Neuron[numHidden];
        outputLayer = new Neuron[numOutput];

        this.initInputs = inputs;
        hiddenInputs = new double[numInput];
        outputInputs = new double[numHidden];
        finalOutput = new double[numOutput];
        updateLayers(inputs);
        fitness = 1;
        setup = false;
    }

    public NeuralNet(NeuralNet copy){
        inputLayer = copy.inputLayer.clone();
        hiddenLayer = copy.hiddenLayer.clone();
        outputLayer = copy.outputLayer.clone();
        initInputs = copy.initInputs.clone();
        hiddenInputs = copy.hiddenInputs.clone();
        outputInputs = copy.outputInputs.clone();
        finalOutput = copy.finalOutput.clone();

        fitness = copy.fitness;
        setup = true;
    }


    public void updateLayers(double[] inputs) {
        //input layer
        for(int i = 0; i < inputLayer.length; i++){
            if(setup)
                inputLayer[i] = new Neuron(1);
            inputLayer[i].setInput(inputs[i]);
            hiddenInputs[i] = inputLayer[i].getOutput();
        }
        //hidden layer
        for(int i = 0; i < hiddenLayer.length; i++){
            if(setup)
                hiddenLayer[i] = new Neuron(inputLayer.length);
            hiddenLayer[i].setInputs(hiddenInputs);
            outputInputs[i] = hiddenLayer[i].getOutput();
        }
        //output layer
        for(int i = 0; i < outputLayer.length; i++){
            if(setup)
                outputLayer[i] = new Neuron(hiddenLayer.length);
            outputLayer[i].setInputs(outputInputs);
            finalOutput[i] = outputLayer[i].getOutput();
        }
    }

    public double[] getOutput(double[] input){
        updateLayers(input);
        return finalOutput;
    }

    public void goGoGadgetMutate(double chance){
        for(int i = 0; i < inputLayer.length; i++)
            inputLayer[i].mutate(chance);
        for(int i = 0; i < hiddenLayer.length; i++)
            hiddenLayer[i].mutate(chance);
        for(int i = 0; i < outputLayer.length; i++)
            outputLayer[i].mutate(chance);
    }

    public void goGoGadgetCrossover(Neuron[] newInfo){
        for(int i = 0; i < newInfo.length; i++){
            hiddenLayer[i] = newInfo[i];
        }
    }

    public Neuron[] getDNA(double crossoverChance){
        Neuron[] dna = new Neuron[(int)((hiddenLayer.length)*crossoverChance)];
        for(int i = 0; i < dna.length; i++){
            dna[i] = hiddenLayer[i];
        }
        return dna;
    }


    public double getFitness(){
        return fitness;
    }

    public void setFitness(double fit){
        fitness = fit;
    }


    public void calcFitness(int timeInMillis, int numPoints){
        fitness = 1;
        fitness += numPoints*50;
    }
}
