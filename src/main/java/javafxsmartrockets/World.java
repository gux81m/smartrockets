package javafxsmartrockets;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static javafxsmartrockets.SmartRocketsConstants.*;

public class World extends Application {
    private Pane root;
    private List<Rocket> rockets = new ArrayList<>();
    private ArrayList<DNA> matingPool = new ArrayList<>();
    private GameObject target;
    private GameObject obstacle;
    private int step;
    private int divider;
    private int generation;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(WORLD_WIDTH, WORLD_HEIGHT);

        target = new Target();
        addGameObject(target, 490, 50);

        obstacle = new Obstacle();
        addGameObject(obstacle, 300, 475);

        initializeRockets();

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 28_000_000) {
                    onUpdate();
                    lastUpdate = now;
                }
            }
        };
        timer.start();

        return root;
    }

    private void initializeRockets() {
        for (int i = 0; i < NUMBER_OF_ROCKETS; i++) {
            Rocket rocket = new Rocket(target);
            rocket.setVelocity(START_VELOCITY);
            rockets.add(rocket);
        }
        rockets.forEach(object -> addGameObject(object, 500, 950));
    }

    private void addGameObject(GameObject object, double x, double y) {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        root.getChildren().add(object.getView());
    }

    private void onUpdate() {
        if (divider != 5) {
            divider++;
            rockets.forEach(Rocket::update);
        } else {
            rockets.stream().filter(rocket -> rocket.isAlive()).forEach(rocket -> rocket.makeNextMove(step));
            if ((step == DNA_LENGTH - 1) || rockets.stream().filter(Rocket::isAlive).count() == 0) {
                step = 0;
                System.out.println("hits/rockets: " + rockets.stream().filter(rocket -> rocket.isHit()).count() + "/" + NUMBER_OF_ROCKETS);
                createMatingPool();
                rockets.forEach(rocket -> root.getChildren().removeAll(rocket.getView()));
                rockets.clear();
                initializeRockets();
                createNewGeneration();
                mutate();
            } else {
                step++;
            }
            divider = 0;
        }

        for (Rocket rocket : rockets) {
            if (target.isColliding(rocket)) {
                rocket.setHit(true);
                rocket.setAlive(false);
                rocket.setVelocity(new Point2D(0, 0));
            }

            if (obstacle.isColliding(rocket)){
                rocket.setDestroyed(true);
                rocket.setAlive(false);
                rocket.setVelocity(new Point2D(0, 0));
            }

            if (!rocket.isInWorld()) {
                rocket.setDestroyed(true);
                rocket.setAlive(false);
                rocket.setVelocity(new Point2D(0, 0));
            }
        }
    }

    private void createNewGeneration() {
        for (Rocket rocket : rockets) {
            DNA parentA = matingPool.get(new Random().nextInt(matingPool.size()));
            DNA parentB = matingPool.get(new Random().nextInt(matingPool.size()));
            DNA newDna = crossover(parentA, parentB);
            rocket.setDna(newDna);
        }
        System.out.println("rocket number: " + rockets.size());
        System.out.println("generation: " + ++generation + "\n");
    }

    private void mutate() {
        for (int i = 0; i < MUTATION_NUMBER; i++) {
            int index = new Random().nextInt(rockets.size());
            DNA mutation = new DNA();
            rockets.get(index).setDna(mutation);
        }
    }

    private void createMatingPool() {
        matingPool.clear();
//        double maxFitness = 0;
//        for (Rocket rocket : rockets) {
//            double fitness = rocket.calculateFitness();
//            if (maxFitness < fitness) {
//                maxFitness = fitness;
//            }
//        }
        for (Rocket rocket : rockets) {
            int number = (int) (Math.pow(rocket.calculateFitness()*100, 2)/100);
            for (int i = 0; i < number; i++) {
                DNA dna = rocket.getDna();
                dna.setFitness(rocket.calculateFitness());
                matingPool.add(rocket.getDna());
            }
        }
        System.out.println("mating pool size: " + matingPool.size());
    }

    public DNA crossover(DNA dnaA, DNA dnaB) {
        DNA childDNA = new DNA();
        for (int index = 0; index < DNA_LENGTH; index++) {
            DNA.STEPS step = dnaA.getFitness() > dnaB.getFitness() ? dnaA.getSteps(index) : dnaB.getSteps(index);
            childDNA.setSteps(index, step);
        }
//        for (int index = 0; index < DNA_LENGTH; index++) {
//            DNA.STEPS step = new Random().nextBoolean() ? dnaA.getSteps(index) : dnaB.getSteps(index);
//            childDNA.setSteps(index, step);
//        }
//        int index = new Random().nextInt(DNA_LENGTH);
//        for (int i = 0; i < DNA_LENGTH; i++) {
//            if (i < index) {
//                childDNA.setSteps(i, dnaA.getSteps(i));
//            } else {
//                childDNA.setSteps(i, dnaB.getSteps(i));
//            }
//        }
        return childDNA;
    }
}
