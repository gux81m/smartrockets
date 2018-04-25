package javafxsmartrockets;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static javafxsmartrockets.SmartRocketsConstants.OPACITY;
import static javafxsmartrockets.SmartRocketsConstants.ROCKET_HEIGHT;
import static javafxsmartrockets.SmartRocketsConstants.ROCKET_WIDTH;
import static javafxsmartrockets.SmartRocketsConstants.WORLD_HEIGHT;

public class Rocket extends GameObject {
    private DNA dna = new DNA();
    private GameObject target;
    private boolean destroyed;
    private boolean hit;

    Rocket(GameObject target) {
        super(new Rectangle(ROCKET_WIDTH, ROCKET_HEIGHT, Color.BLUE));
        this.getView().setOpacity(OPACITY);
        this.target = target;
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

    public void setDna(DNA dna) {
        this.dna = dna;
    }

    public DNA getDna() {
        return dna;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public double calculateFitness() {
        double distance = getDistanceFromGameObject(target);
        double remappedValue = (WORLD_HEIGHT - distance) / WORLD_HEIGHT;
        if (isHit()) {
            return 1;
        } else {
            return remappedValue;
        }
    }
}
