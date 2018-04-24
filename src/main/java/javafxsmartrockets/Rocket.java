package javafxsmartrockets;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static javafxsmartrockets.SmartRocketsConstants.ROCKET_HEIGHT;
import static javafxsmartrockets.SmartRocketsConstants.ROCKET_WIDTH;

public class Rocket extends GameObject {
    private DNA dna = new DNA();
    private boolean destroyed;
    private boolean hit;

    Rocket() {
        super(new Rectangle(ROCKET_WIDTH, ROCKET_HEIGHT, Color.BLUE));
    }

    public void makeNextMove(int step) {
        DNA.STEPS steps = dna.getSteps(step);
        if (DNA.STEPS.LEFT.equals(steps)) {
            super.rotateLeft();
        } else if (DNA.STEPS.RIGHT.equals(steps)) {
            super.rotateRight();
        }
        super.update();
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
