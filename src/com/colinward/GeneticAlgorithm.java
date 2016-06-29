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
    public final double MUTATION = 0.01;

    public final int NUM_INPUT = 3;
    public final int NUM_HIDDEN = 16;
    public final int NUM_OUTPUT = 4;

    public Random rand;
    public int generation;
    public int averageFitness = 1;

    public NeuralNet chosenOne1;
    public NeuralNet chosenOne2;

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
        if(genePool.size() > 50)
            commitGenocide();
        NeuralNet child1 = roulette();
        NeuralNet child2 = roulette();

        chosenOne1 = child1;
        chosenOne2 = child2;

        if(rand.nextDouble() <= CROSSOVER)
            child1 = crossover(child1, child2);
        if(rand.nextDouble() <= MUTATION)
            child1 = mutate(child1);
        genePool.add(0, child1);
        currentGeneration = genePool.get(0);
        generation++;
        return genePool.get(0);
    }

    public NeuralNet roulette(){
        double[] fitnessScores = new double[genePool.size()];
        double totalFit = 0;
        ArrayList<Integer> weighted = new ArrayList<Integer>();
        for(int i = 0; i < genePool.size(); i++){
            fitnessScores[i] = genePool.get(i).getFitness();
            for(int k = 0; k < fitnessScores[i]; k++)
                weighted.add(i);
        }
        averageFitness = (int)totalFit/fitnessScores.length;
        return new NeuralNet(genePool.get(weighted.get(rand.nextInt(weighted.size()))));
    }

    public NeuralNet crossover(NeuralNet ch1, NeuralNet ch2){
        Neuron[] childsDNA = ch2.getDNA(rand.nextDouble());
        ch1.goGoGadgetCrossover(childsDNA);
        return ch1;
    }

    public NeuralNet mutate(NeuralNet nn){
        nn.goGoGadgetMutate(MUTATION);
        return nn;
    }

    public double[] updateCurrentGen(double[] input){
        return currentGeneration.getOutput(input);
    }


    public double currentGenFitness(int timeInMillis, int points){
        currentGeneration.calcFitness(timeInMillis, points);
        return currentGeneration.getFitness();
    }

    public String printStats(){
        double maxFit = 0;
        for(int i = 0; i < genePool.size(); i++){
            if(genePool.get(i).getFitness() > maxFit)
                maxFit = genePool.get(i).getFitness();
        }
        return "Generation: " + generation + "\n" +
                "Last Gen Fitness: " + genePool.get(1).getFitness() + "\n" +
                "Average Fitness: " + averageFitness + "\n" +
                "Pool Size: " + genePool.size() + "\n" +
                "First Child Fitness: " + chosenOne1.getFitness() + "\n" +
                "Second Child Fitness: " + chosenOne2.getFitness() + "\n" +
                "MAXFITNESS: " + maxFit + "\n##########";

    }

    public void commitGenocide(){
        for(int i = 0; i < genePool.size(); i++){
            if(genePool.get(i).getFitness() < 50)
                genePool.remove(i);
        }
    }
}
