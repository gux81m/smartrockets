package javafxsmartrockets;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static javafxsmartrockets.SmartRocketsConstants.TARGET_RADIUS;

public class Target extends GameObject {
    Target() {
        super(new Circle(20, 20, TARGET_RADIUS, Color.RED));
    }
}
