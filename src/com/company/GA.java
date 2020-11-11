package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GA {
    int numberOfTargets;
    int numberOfWeapons;
    int[] targetsThreatCoefficient;
    float[][]matrix;
    float maxThreatValue;
    ArrayList<int[]> chromosomes;
    List<Float>populationFitness;
    List<Weopens> rowsInMatrix;

    public GA(int numberOfTargets, int numberOfWeapons, int[] targetsThreatCoefficient, float[][]matrix, List<Weopens> rowsInMatrix) {
        this.numberOfTargets = numberOfTargets;
        this.numberOfWeapons = numberOfWeapons;
        this.targetsThreatCoefficient = targetsThreatCoefficient;
        this.matrix = matrix;
        calculateMaxThreatValue();
        chromosomes = new ArrayList<>();
        populationFitness = new ArrayList<>();
        this.rowsInMatrix = rowsInMatrix;
    }
    private void calculateMaxThreatValue(){
        float sumOfThreats = 0F;
        for(int i=0 ; i<targetsThreatCoefficient.length ; i++){
            sumOfThreats+=targetsThreatCoefficient[i];
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
                    chromosome[i+1-number]= 1;
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
        float [] copyOfTargetCoefficent = new float[numberOfTargets];
        for(int j=0; j<numberOfTargets ; j++){
            copyOfTargetCoefficent[j] = targetsThreatCoefficient[j];
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

}
