package javafxsmartrockets;

import javafx.geometry.Point2D;

public class SmartRocketsConstants {
    // RocketWorld constants
    public static final double WORLD_HEIGHT = 1000;
    public static final double WORLD_WIDTH = 1000;
    public static final double TARGET_RADIUS = 20;
    public static final int NUMBER_OF_ROCKETS = 100;
    public static final double MUTATION_RATE = 0.05;

    // Rocket constants
    public static final double ROCKET_WIDTH = 5;
    public static final double ROCKET_HEIGHT = 20;
    public static final double OPACITY = 0.3;
    public static final Point2D START_VELOCITY = new Point2D(0, -5);
    public static final double TURN_SIZE = 20;

    // DNA constants
    public static final Integer DNA_LENGTH = 200;
    public static final double PENALTY_FOR_HITTING_OBSTACLE = 0.8;
    public static final double PENALTY_FOR_HITTING_WALL = 0.9;
}
