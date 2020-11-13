package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WTA implements GaHelper<int[]> {
    private final int numberOfTargets;
    private final int numberOfWeapons;
    private final List<Double> targetsThreatCoefficient;
    private final double[][] matrix;
    private double maxThreatValue;
    private final List<Weapons> rowsInMatrix;

    public WTA(int numberOfTargets, int numberOfWeapons, List<Double> targetsThreatCoefficient, double[][] matrix, List<Weapons> rowsInMatrix) {
        this.numberOfTargets = numberOfTargets;
        this.numberOfWeapons = numberOfWeapons;
        this.targetsThreatCoefficient = targetsThreatCoefficient;
        this.matrix = matrix;
        this.rowsInMatrix = rowsInMatrix;
        calculateMaxThreatValue();
    }

    private void calculateMaxThreatValue() {
        double sumOfThreats = 0;
        for (Double aDouble : targetsThreatCoefficient) {
            sumOfThreats += aDouble;
        }
        maxThreatValue = sumOfThreats / 2;
    }

    @Override
    public List<int[]> generatePopulation(int populationSize) {
        Random random = new Random();
        List<int[]> chromosomes = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int[] chromosome = new int[numberOfTargets * numberOfWeapons];
            for (int j = 0; j < chromosome.length; j++) {
                chromosome[j] = random.nextInt(2);
            }
            checkAndFix(chromosome);
            chromosomes.add(chromosome);
        }
        return chromosomes;
    }

    @Override
    public void checkAndFix(int[] chromosome) {
        List<Integer> indexes = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < chromosome.length; i++) {
            if (chromosome[i] == 1) {
                indexes.add(i);
            }
            counter++;
            if (counter == numberOfTargets) {
                if (indexes.size() > 1) {
                    while (indexes.size() > 1) {
                        Random random = new Random();
                        int number = random.nextInt(indexes.size());
                        chromosome[indexes.get(number)] = 0;
                        indexes.remove(number);
                    }
                } else if (indexes.isEmpty()) {
                    Random random = new Random();
                    int number = random.nextInt(numberOfTargets);
                    chromosome[i - number] = 1;
                }
                counter = 0;
                indexes.clear();
            }
        }
    }

    @Override
    public List<Double> calculatePopulationFitness(List<int[]> chromosomes) {
        List<Double> fitness = new ArrayList<>();
        for (int[] chromosome : chromosomes) {
            fitness.add(calculateFitnessForChromosome(chromosome));
        }
        return fitness;
    }

    private double calculateFitnessForChromosome(int[] chromosome) {
        int counter = 0;
        int weapon = 0;
        double[] copyOfTargetCoefficient = new double[numberOfTargets];
        for (int j = 0; j < numberOfTargets; j++) {
            copyOfTargetCoefficient[j] = targetsThreatCoefficient.get(j);
        }
        for (int j : chromosome) {
            counter++;
            if (j == 1) {
                copyOfTargetCoefficient[counter - 1] = copyOfTargetCoefficient[counter - 1] - (
                        copyOfTargetCoefficient[counter - 1] * matrix[rowsInMatrix.get(weapon).getIndexInMatrix()][counter - 1]
                );
            }

            if (counter == numberOfTargets) {
                counter = 0;
                weapon++;
            }
        }
        float fitness = 0;
        for (double v : copyOfTargetCoefficient) {
            fitness += v;
        }
        return fitness;
    }

    @Override
    public void mutation(List<int[]> chromosomes, double mutationProb) {

        for (int[] chromosome : chromosomes) {
            for (int j = 0; j < chromosome.length; j++) {
                if (Math.random() < mutationProb) {
                    if (chromosome[j] == 1) {
                        chromosome[j] = 0;
                    } else {
                        chromosome[j] = 1;
                    }
                }
            }
            checkAndFix(chromosome);
        }
    }

    @Override
    public int stoppingCondition(List<Double> populationFitness) {
        double minFitness = Float.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < populationFitness.size(); i++) {
            if (populationFitness.get(i) < minFitness) {
                index = i;
                minFitness = populationFitness.get(i);
            }
        }
        if (minFitness < maxThreatValue) {
            return index;
        } else {
            return -1;
        }
    }

    @Override
    public void printPhenotype(int[] chromosome, double fitness) {
        int counter = 0;
        int index = 0;
        for (int j : chromosome) {
            counter++;
            if (j == 1) {
                System.out.println(rowsInMatrix.get(index).getName() + " is assigned to target #" + counter);
            }
            if (counter == numberOfTargets) {
                counter = 0;
                index++;
            }
        }
        System.out.println("The expected total threat of the surviving targets is " + fitness);
    }

    @Override
    public void printPhenotypeAfterIterations(List<int[]> chromosomes) {
        List<Double> fitness = calculatePopulationFitness(chromosomes);
        int index = 0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < fitness.size(); i++) {
            if (fitness.get(i) < min) {
                min = fitness.get(i);
                index = i;
            }
        }
        printPhenotype(chromosomes.get(index), fitness.get(index));
    }
}
