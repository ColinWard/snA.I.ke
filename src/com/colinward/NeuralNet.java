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
        fitness = 0;
        setup = false;
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

    public double getFitness(){
        return fitness;
    }

    public void calcFitness(int timeInMillis, int numPoints){
        fitness = 0;
        fitness += timeInMillis/10000;
        fitness += numPoints*20;
    }
}
