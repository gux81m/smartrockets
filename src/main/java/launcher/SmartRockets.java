package launcher;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import static launcher.SmartRocketsConstants.*;

public class SmartRockets extends SimulationFrame {
    private RocketWorld rocketWorld;
    private ArrayList<Rocket> rockets;
    private ArrayList<DNA> matingPool;
    private int generation;

    public SmartRockets() {
        super("SmartRockets", SCALE, CANVAS_SIZE);
    }

    protected void initializeWorld() {
        this.world.setGravity(GRAVITY);

        rocketWorld = new RocketWorld();
        rocketWorld.getWalls().forEach(wall -> this.world.addBody(wall));

        this.world.addBody(rocketWorld.getTarget());

        rockets = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_ROCKETS; i++) {
            rockets.add(new Rocket(rocketWorld.getTarget().getWorldCenter()));
        }

        rockets.forEach(rocket -> {
            this.world.addBody(rocket.getRocket());
            this.world.addListener(new HitTargetListener(rocket, rocketWorld.getTarget()));
            this.world.addListener(new HitWallListener(rocket, rocketWorld.getWalls()));
        });
        matingPool = new ArrayList<>();
        System.out.println("Generation: " + generation++);
    }

    @Override
    protected void update(Graphics2D g, double elapsedTime) {
        super.update(g, elapsedTime);

        for (int i = 0; i < NUMBER_OF_ROCKETS; i++) {
            Rocket rocket = rockets.get(i);
            rocket.makeNextMove(elapsedTime, g);
        }

        if (rockets.stream().map(Rocket::getRocket).map(SimulationBody::isActive).filter(aBoolean -> aBoolean.equals(true)).count() == 0) {
            createMatingPool();
            ArrayList<DNA> newPopulation = createNewPopulation();

            this.world.removeAllBodies();
            initializeWorld();
            for (int i = 0; i < NUMBER_OF_ROCKETS; i++) {
                rockets.get(i).setDNA(newPopulation.get(i));
            }
            mutateOneRocket();
            System.out.println("rockets: " + rockets.size());
        }
    }

    private void mutateOneRocket() {
        int index = new Random().nextInt(rockets.size());
        int dnaIndex = new Random().nextInt(DNA_LENGTH);
        DNA dna = rockets.get(index).getDNA();
        dna.setThruster(dnaIndex, DNA.THRUSTER.values()[new Random().nextInt(DNA.THRUSTER.values().length)]);
        dna.setForce(dnaIndex, new Random().nextDouble());
        dna.setTime(dnaIndex, new Random().nextDouble()*2);
    }

    private void createMatingPool() {
        matingPool.clear();
        rockets.forEach(rocket -> {
            int fitness = rocket.calculateFitness();
            System.out.println("fitness: " + fitness);
            DNA dna = rocket.getDNA();
            for (int i = 0; i < fitness; i++) {
                matingPool.add(dna);
            }
        });
        System.out.println("matingPool: " + matingPool.size());
    }

    private ArrayList<DNA> createNewPopulation() {
        ArrayList<DNA> newPopulation = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_ROCKETS; i++) {
            int indexA = new Random().nextInt(matingPool.size());
            int indexB = new Random().nextInt(matingPool.size());
            DNA childDNA = crossover(matingPool.get(indexA), matingPool.get(indexB));
            newPopulation.add(childDNA);
        }
        System.out.println("newPopulation: " + newPopulation.size());
        return newPopulation;
    }

    private DNA crossover(DNA dnaA, DNA dnaB) {
        DNA childDNA = new DNA(DNA_LENGTH);
        for (int index = 0; index < DNA_LENGTH; index++) {
            DNA.THRUSTER thruster = new Random().nextBoolean() ? dnaA.getThruster(index) : dnaB.getThruster(index);
            childDNA.setThruster(index, thruster);
            childDNA.setForce(index, (dnaA.getForce(index) + dnaB.getForce(index)) / 2);
            childDNA.setTime(index, (dnaA.getTime(index) + dnaB.getTime(index)) / 2);
        }
        return childDNA;
    }

    public static void main(String[] args) {
        SmartRockets simulation = new SmartRockets();
        simulation.run();
    }
}
