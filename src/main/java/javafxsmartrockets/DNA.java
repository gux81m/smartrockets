package javafxsmartrockets;

import java.util.ArrayList;
import java.util.Random;

import static javafxsmartrockets.SmartRocketsConstants.DNA_LENGTH;

public class DNA {
    private ArrayList<STEPS> steps;
    private double fitness;

    public DNA() {
        steps = new ArrayList<>(DNA_LENGTH);
        initDNA();
    }

    public void initDNA() {
        for (int i = 0; i < DNA_LENGTH; i++) {
            steps.add(STEPS.values()[new Random().nextInt(STEPS.values().length)]);
        }
    }

    public STEPS getSteps(int index) {
        return steps.get(index);
    }

    public void setSteps(int index, STEPS step) {
        steps.add(index, step);
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public static enum STEPS {
        LEFT, RIGHT;
    }

}
