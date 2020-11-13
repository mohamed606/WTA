package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // tank 2
        // tank, 2
        //w =  new Weopens(tank)
        // w.setIndex(index)
        /* for(int i=0 ; i<2 ; i++){
            rowInMatrix.add(w)
        */
        int numberOfTargets;
        String inputString = "ok";
        List<Double> threatCoefficient = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        List<String> inputs = new ArrayList<>();
        List<Weapons> weapons = new ArrayList<>();
        int index = 0;
        int numberOfWeapons =0 ;
        double[][] matrix;
        while (true) {
            inputString = input.nextLine();
            if(inputString.equals("x")){
                break;
            }
            String[] split = inputString.split(" ");
            int quantity = Integer.parseInt(split[1]);
            numberOfWeapons += quantity;
            for (int i = 0; i < quantity; i++) {
                Weapons weapon = new Weapons(split[0]);
                weapon.setIndexInMatrix(index);
                weapons.add(weapon);
            }
            index++;
        }
        System.out.println("Enter the number of targets: ");
        numberOfTargets = input.nextInt();
        System.out.println("Enter the threat coefficient: ");
        for (int i = 0; i < numberOfTargets; i++) {
            threatCoefficient.add(input.nextDouble());
        }
        System.out.println("Enter the weapon\'s success probabilities matrix: ");
        matrix = new double[index][numberOfTargets];
        for (int i = 0; i < index; i++) {
            for (int j = 0; j < numberOfTargets; j++) {
                matrix[i][j] = input.nextDouble();
            }
        }
        System.out.println("Please wait while running the GA ");
        GA ga = new GA(numberOfTargets, numberOfWeapons, threatCoefficient, matrix, weapons);
        ga.start();

    }
    /*
    t 2
a 1
g 2
x
     */
    /*
     */
    /*
    0.3 0.6 0.5
0.4 0.5 0.4
0.1 0.2 0.2
     */
}
