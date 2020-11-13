package com.company;

import java.util.List;

public interface GaHelper<T> {
    List<T> generatePopulation(int populationSize);

    void checkAndFix(T chromosome);

    List<Double> calculatePopulationFitness(List<T> chromosomes);

    void mutation(List<T> chromosomes, double mutationProb);

    int stoppingCondition(List<Double> populationFitness);

    void printPhenotype(T chromosome, double fitness);

    void printPhenotypeAfterIterations(List<T> chromosomes, List<Double>fitness);
}
