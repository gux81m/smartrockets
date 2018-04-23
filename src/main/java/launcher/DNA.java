package launcher;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class DNA {
    private ArrayList<THRUSTER> thruster;
    private ArrayList<Double> force;
    private ArrayList<Double> time;

    public DNA(int length) {
        thruster = new ArrayList<>(length);
        force = new ArrayList<>(length);
        time = new ArrayList<>(length);
        initDNA(length);
    }

    private void initDNA(int length) {
        for (int i = 0; i < length; i++) {
            thruster.add(THRUSTER.values()[new Random().nextInt(THRUSTER.values().length)]);
            force.add(new Random().nextDouble());
            time.add(new Random().nextDouble());
        }
    }

    public THRUSTER getThruster(int i) {
        return thruster.get(i);
    }

    public double getForce(int i) {
        return force.get(i);
    }

    public double getTime(int i) {
        return time.get(i);
    }

    public enum THRUSTER {
        FORWARD, LEFT, RIGHT, NONE;
    }
}
