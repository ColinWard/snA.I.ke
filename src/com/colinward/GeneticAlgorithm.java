package com.colinward;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Colin Ward on 6/28/2016.
 */
public class GeneticAlgorithm {
    ArrayList<NeuralNet> genePool;
    NeuralNet currentGeneration;
    public final double CROSSOVER = 0.7;
    public final double MUTATION = 0.05;

    public final int NUM_INPUT = 3;
    public final int NUM_HIDDEN = 6;
    public final int NUM_OUTPUT = 4;

    public Random rand;
    public int generation;

    public GeneticAlgorithm(double[] input){
        genePool = new ArrayList<NeuralNet>();
        rand = new Random();
        generation = 0;
        genePool.add(new NeuralNet(NUM_INPUT, NUM_HIDDEN, NUM_OUTPUT, input));
        genePool.add(new NeuralNet(NUM_INPUT, NUM_HIDDEN, NUM_OUTPUT, input));
        nextGeneration();
        currentGeneration = genePool.get(0);
    }

    public NeuralNet nextGeneration(){
        NeuralNet child1 = roulette();
        NeuralNet child2 = roulette();

        if(rand.nextDouble() <= CROSSOVER)
            child1 = crossover(child1, child2);

        genePool.add(0, child1);
        currentGeneration = genePool.get(0);
        generation++;
        return genePool.get(0);
    }

    public NeuralNet roulette(){
        double[] fitnessScores = new double[genePool.size()];
        int maxFitness = Integer.MIN_VALUE;
        for(int i = 0; i < genePool.size(); i++){
            fitnessScores[i] = genePool.get(i).getFitness();
            if(fitnessScores[i] > maxFitness)
                maxFitness = i;
        }
        return genePool.get(maxFitness);
    }

    public NeuralNet crossover(NeuralNet ch1, NeuralNet ch2){
        //mix the two into one
        return ch1;
    }

    public double[] updateCurrentGen(double[] input){
        return currentGeneration.getOutput(input);
    }


    public double currentGenFitness(int timeInMillis, int points){
        currentGeneration.calcFitness(timeInMillis, points);
        return currentGeneration.getFitness();
    }

    public String printStats(){
        return "Generation: " + generation + "\n" +
                "Last gen fitness: " + genePool.get(1).getFitness();
    }
}
