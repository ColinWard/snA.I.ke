package com.colinward;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Colin Ward on 6/28/2016.
 */
public class Neuron {
    double[] inputs;
    double[] weights;
    double output;

    public Neuron(int numInputs){
        inputs = new double[numInputs];
        weights = new double[numInputs];
        output = 0;

        initWeights();
    }

    public void setInputs(double[] inputs){
        this.inputs = inputs;
    }

    public void setInput(double input){
        inputs[0] = input;
    }

    public double getOutput(){
        calcOutput();
        return output;
    }

    public void calcOutput(){
        double tempOutput = 0;
        for(int i = 0; i < inputs.length; i++){
            tempOutput += inputs[i]*weights[i];
        }
        output = tempOutput;
    }

    public void mutate(double chance){
        Random rand = new Random();
        for(int i = 0; i < weights.length; i++){
            if(rand.nextDouble() <= chance*4)
                weights[i] = (2*rand.nextDouble())-1;
        }
    }

    public double[] getWeights(){
        return weights;
    }

    public void setWeights(double[] w){
        weights = w;
    }


    public void initWeights(){
        Random rand = new Random();
        for(int i = 0; i < weights.length; i++){
            weights[i] = (2*rand.nextDouble())-1;
        }
    }

}
