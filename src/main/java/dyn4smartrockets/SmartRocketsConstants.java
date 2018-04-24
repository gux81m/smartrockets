package dyn4smartrockets;

import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.awt.*;

public class SmartRocketsConstants {
    // RocketWorld constants
    public static final double SCALE = 20.0;
    public static final Dimension CANVAS_SIZE = new Dimension(1000, 1000);
    public static final double TARGET_RADIUS = 1;
    public static final Vector2 GRAVITY = new Vector2(0, -3);
    public static final int NUMBER_OF_ROCKETS = 30;

    // Rocket constants
    public static final double ROCKET_WIDTH = 10;
    public static final double ROCKET_HEIGHT = 30;
    public static final MassType MASS_TYPE = MassType.NORMAL;

    // DNA constants
    public static final Integer DNA_LENGTH = 300;
}
