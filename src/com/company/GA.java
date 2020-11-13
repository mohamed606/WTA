package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GA {
    int numberOfTargets;
    int numberOfWeapons;
    List<Double> targetsThreatCoefficient;
    double[][]matrix;
    double maxThreatValue;
    List<int[]> chromosomes;
    List<Float>populationFitness;
    List<Weapons> rowsInMatrix;
    List<Integer> matingPool;
    int population = 8;
    double crossOverProb = 0.56;
    List<int []> newGeneration ;
    double mutationProb = 0.1;
    int iterationNumber = 10;
    public GA(int numberOfTargets, int numberOfWeapons, List<Double> targetsThreatCoefficient, double[][]matrix, List<Weapons> rowsInMatrix) {
        this.numberOfTargets = numberOfTargets;
        this.numberOfWeapons = numberOfWeapons;
        this.targetsThreatCoefficient = targetsThreatCoefficient;
        this.matrix = matrix;
        calculateMaxThreatValue();
        chromosomes = new ArrayList<>();
        populationFitness = new ArrayList<>();
        this.rowsInMatrix = rowsInMatrix;
        matingPool = new ArrayList<>();
        newGeneration = new ArrayList<>();
    }
    private void calculateMaxThreatValue(){
        double sumOfThreats = 0;
        for(int i=0 ; i<targetsThreatCoefficient.size(); i++){
            sumOfThreats+=targetsThreatCoefficient.get(i);
        }
        maxThreatValue = sumOfThreats/2;
    }
    private void generatePopulation(){
        Random random = new Random();
        for(int i=0 ; i<8 ; i++){
            int [] chromosome = new int[numberOfTargets*numberOfWeapons];
            for(int j=0 ; j< chromosome.length; j++){
                chromosome[j] = random.nextInt(2);
            }
            checkAndFixChromosome(chromosome);
            chromosomes.add(chromosome);
        }
    }
    private void checkAndFixChromosome(int[] chromosome) {
        List<Integer> indexes = new ArrayList<>();
        int counter=0;
        for(int i=0 ; i<chromosome.length;i++){
            if(chromosome[i]==1){
                indexes.add(i);
            }
            counter ++;
            if( counter == numberOfTargets ){
                if(indexes.size() > 1){
                    while (indexes.size() > 1){
                        Random random = new Random();
                        int number = random.nextInt(indexes.size());
                        chromosome[indexes.get(number)]=0;
                        indexes.remove(number);
                    }
                }
                else if(indexes.isEmpty()){
                    Random random = new Random();
                    int number = random.nextInt(numberOfTargets);
                    int reverse = numberOfTargets - number + 1;
                    chromosome[i-number]= 1;
                }
                counter = 0;
                indexes.clear();
            }
        }
    }
    private void calculateFitnessForChromosomes(){
        for(int i=0;i<chromosomes.size();i++){
            populationFitness.add(calculateFitnessForChromosome(chromosomes.get(i)));
        }
    }
    private float calculateFitnessForChromosome(int [] chromosome){
        int counter = 0;
        int weopeon = 0;
        double [] copyOfTargetCoefficent = new double[numberOfTargets];
        for(int j=0; j<numberOfTargets ; j++){
            copyOfTargetCoefficent[j] = targetsThreatCoefficient.get(j);
        }
        for(int i=0; i< chromosome.length; i++){
            counter++;
            if(chromosome[i]==1){
                copyOfTargetCoefficent[counter-1] = copyOfTargetCoefficent[counter-1] - (
                        copyOfTargetCoefficent[counter-1] * matrix[rowsInMatrix.get(weopeon).getIndexInMatrix()][counter-1]
                        );
            }

            if(counter == numberOfTargets){
                counter = 0;
                 weopeon ++;
            }
        }
        float fitness = 0;
        for(int i=0 ; i<copyOfTargetCoefficent.length ; i++){
            fitness += copyOfTargetCoefficent[i];
        }
        return fitness;
    }
    private void startTournament(){
        int counter = 0;
        Random random = new Random();
        int prev = -1;
        while (counter < population*2){
            int index1 = random.nextInt(population);
            int index2 = random.nextInt(population);
            if(index1 == index2){
                continue;
            }
            if(populationFitness.get(index1)<populationFitness.get(index2)){
                if(prev == index1){
                    continue;
                }
                matingPool.add(index1);
                prev = index1;
            }else {
                if(index2 == prev){
                    continue;
                }
                matingPool.add(index2);
                prev = index2;
            }
            counter++;
            if(matingPool.size()%2 == 0){
                prev = -1;
            }
        }
    }
    private void startMating(){
        Random random = new Random();
        while(!matingPool.isEmpty()){
            int[] parent1 = chromosomes.get(matingPool.remove(0));
            int[] parent2 = chromosomes.get(matingPool.remove(0));
            if(Math.random() <= crossOverProb){
                int crossOverPoint = random.nextInt(parent1.length-2)+1;
                int [] child1 = parent1;
                int [] child2 = parent2;
                for(int i = crossOverPoint ; i<parent1.length ; i++){
                    child1[i] = parent2[i];
                    child2[i] = parent1[i];
                }
                newGeneration.add(child1);
                newGeneration.add(child2);
            }
            else {
                newGeneration.add(parent1);
                newGeneration.add(parent2);
            }
        }
    }
    private void mutation(){

        for(int i=0 ; i<newGeneration.size() ; i++){
            int [] chromosome = newGeneration.get(i);
            for(int j=0 ; j<chromosome.length ; j++){
                if(Math.random()<mutationProb){
                    if(chromosome[j] == 1){
                        chromosome[j] = 0;
                    }else {
                        chromosome[j] = 1;
                    }
                }
            }
            checkAndFixChromosome(chromosome);
        }
        chromosomes = newGeneration;
    }
    private int checkStoppingCondition(){
        double minFitness = Float.MAX_VALUE;
        int index = -1;
        for(int i=0 ; i<populationFitness.size() ; i++){
            if(populationFitness.get(i) < minFitness){
                index = i;
                minFitness = populationFitness.get(i);
            }
        }
        if(minFitness < maxThreatValue){
            return index;
        }
        else {
            return -1;
        }
    }
    public void start(){
        generatePopulation();
        calculateFitnessForChromosomes();
        for(int i=0 ; i<iterationNumber ; i++){
            startTournament();
            startMating();
            mutation();
            calculateFitnessForChromosomes();
            int solutionIndex = checkStoppingCondition();
            if(solutionIndex != -1){
                System.out.println(populationFitness.get(solutionIndex));
                break;
            }else {
                System.out.println("not yet");
            }
        }
    }
}
