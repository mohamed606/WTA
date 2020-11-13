package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int numberOfTargets;
        String inputString = "ok";
        List<Double> threatCoefficient = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        List<String> inputs = new ArrayList<>();
        List<Weapons> weapons = new ArrayList<>();
        int index = 0;
        int numberOfWeapons = 0;
        double[][] matrix;
        while (true) {
            inputString = input.nextLine();
            if (inputString.equals("x")) {
                break;
            }
            String[] split = inputString.split(" ");
            int quantity = Integer.parseInt(split[1]);
            numberOfWeapons += quantity;
            for (int i = 0; i < quantity; i++) {
                Weapons weapon = new Weapons(split[0] + " #" + (i + 1));
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
        WTA wta = new WTA(numberOfTargets, numberOfWeapons, threatCoefficient, matrix, weapons);
        GA ga = new GA(8, 0.56, 0.1, 10, wta);
        ga.start();
    }
}
