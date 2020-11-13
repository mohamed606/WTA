package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GA {
    private List<int[]> chromosomes;
    private List<Double> populationFitness;
    private final List<Integer> matingPool;
    private int populationSize;
    private double crossOverProb;
    private final List<int[]> newGeneration;
    private double mutationProb;
    private int iterationNumber;
    private final GaHelper helper;

    public GA(int populationSize, double crossOverProb, double mutationProb, int iterationNumber, GaHelper gaHelper) {
        chromosomes = new ArrayList<>();
        populationFitness = new ArrayList<>();
        matingPool = new ArrayList<>();
        newGeneration = new ArrayList<>();
        this.populationSize = populationSize;
        this.crossOverProb = crossOverProb;
        this.mutationProb = mutationProb;
        this.iterationNumber = iterationNumber;
        this.helper = gaHelper;
    }

    private void startTournament() {
        int counter = 0;
        Random random = new Random();
        int prev = -1;
        while (counter < populationSize * 2) {
            int index1 = random.nextInt(populationSize);
            int index2 = random.nextInt(populationSize);
            if (index1 == index2) {
                continue;
            }
            if (populationFitness.get(index1) < populationFitness.get(index2)) {
                if (prev == index1) {
                    continue;
                }
                matingPool.add(index1);
                prev = index1;
            } else {
                if (index2 == prev) {
                    continue;
                }
                matingPool.add(index2);
                prev = index2;
            }
            counter++;
            if (matingPool.size() % 2 == 0) {
                prev = -1;
            }
        }
    }

    private void startMating() {
        Random random = new Random();
        while (!matingPool.isEmpty()) {
            int[] parent1 = chromosomes.get(matingPool.remove(0));
            int[] parent2 = chromosomes.get(matingPool.remove(0));
            if (Math.random() <= crossOverProb) {
                int crossOverPoint = random.nextInt(parent1.length - 2) + 1;
                int[] child1 = parent1;
                int[] child2 = parent2;
                for (int i = crossOverPoint; i < parent1.length; i++) {
                    child1[i] = parent2[i];
                    child2[i] = parent1[i];
                }
                newGeneration.add(child1);
                newGeneration.add(child2);
            } else {
                newGeneration.add(parent1);
                newGeneration.add(parent2);
            }
        }
    }

    public void start() {
        chromosomes = helper.generatePopulation(populationSize);
        populationFitness = helper.calculatePopulationFitness(chromosomes);
        for (int i = 0; i < iterationNumber; i++) {
            startTournament();
            startMating();
            helper.mutation(newGeneration, mutationProb);
            chromosomes = newGeneration;
            newGeneration.clear();
            populationFitness = helper.calculatePopulationFitness(chromosomes);
            int solutionIndex = helper.stoppingCondition(populationFitness);
            if (solutionIndex != -1) {
                helper.printPhenotype(chromosomes.get(solutionIndex), populationFitness.get(solutionIndex));
                break;
            }
        }
        helper.printPhenotypeAfterIterations(chromosomes);
    }
}
