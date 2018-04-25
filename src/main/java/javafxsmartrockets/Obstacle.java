package javafxsmartrockets;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Obstacle extends GameObject {
    Obstacle() {
        super(new Rectangle(300, 20, Color.BLACK));
    }
}
