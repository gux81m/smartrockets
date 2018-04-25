package javafxsmartrockets;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static javafxsmartrockets.SmartRocketsConstants.*;

public class Rocket extends GameObject {
    private DNA dna = new DNA();
    private GameObject target;
    private boolean destroyedByObstacle;
    private boolean destroyedByWall;
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

    public void setDna(DNA dna) {
        this.dna = dna;
    }

    public DNA getDna() {
        return dna;
    }

    public void setDestroyedByObstacle(boolean destroyedByObstacle) {
        this.destroyedByObstacle = destroyedByObstacle;
    }

    public void setDestroyedByWall(boolean destroyedByWall) {
        this.destroyedByWall = destroyedByWall;
    }

    public double calculateFitness() {
        double distance = getDistanceFromGameObject(target);
        double remappedValue = (WORLD_HEIGHT - distance) / WORLD_HEIGHT;
        if (isHit()) {
            return 1;
        } else {
            if (destroyedByWall) {
                return remappedValue * PENALTY_FOR_HITTING_WALL;
            } else if (destroyedByObstacle) {
                return remappedValue * PENALTY_FOR_HITTING_OBSTACLE;
            } else {
                return remappedValue;
            }
        }
    }
}
