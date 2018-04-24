package javafxsmartrockets;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static javafxsmartrockets.SmartRocketsConstants.*;

public class World extends Application {
    private Pane root;
    private List<Rocket> rockets = new ArrayList<>();
    private GameObject target;
    private GameObject obstacle;
    private int step;
    private int divider;

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
            Rocket rocket = new Rocket();
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
                rockets.forEach(rocket -> root.getChildren().removeAll(rocket.getView()));
                rockets.clear();
                initializeRockets();
                createMatingPool();
                mutate();
                createNewGeneration();
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

            if (!checkGameObjectIsInWorld(rocket)) {
                rocket.setDestroyed(true);
                rocket.setAlive(false);
                rocket.setVelocity(new Point2D(0, 0));
            }
        }
    }

    private void createNewGeneration() {

    }

    private void mutate() {

    }

    private void createMatingPool() {

    }


    private boolean checkGameObjectIsInWorld(GameObject object) {
        double objectX = object.getView().getTranslateX();
        double objectY = object.getView().getTranslateY();
        if (objectX > 0 && objectX < WORLD_WIDTH &&
            objectY > 0 && objectY < WORLD_HEIGHT) {
            return true;
        }
        return false;
    }
}
