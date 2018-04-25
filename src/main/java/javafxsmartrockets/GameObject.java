package javafxsmartrockets;

import javafx.geometry.Point2D;
import javafx.scene.Node;

import static javafxsmartrockets.SmartRocketsConstants.TURN_SIZE;
import static javafxsmartrockets.SmartRocketsConstants.WORLD_HEIGHT;
import static javafxsmartrockets.SmartRocketsConstants.WORLD_WIDTH;

public class GameObject {
    private Node view;
    private boolean alive = true;
    private Point2D velocity = new Point2D(0, 0);

    GameObject(Node view) {
        this.view = view;
    }

    public void update() {
        view.setTranslateX(view.getTranslateX() + velocity.getX());
        view.setTranslateY(view.getTranslateY() + velocity.getY());
    }

    public boolean isAlive() {
        return this.alive;
    }

    public boolean isDead() {
        return !this.alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public Node getView() {
        return view;
    }

    public double getRotate() {
        return view.getRotate() - 90;
    }

    public void rotateRight() {
        view.setRotate(view.getRotate() + TURN_SIZE);
        Point2D newVelocity = new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate())));
        setVelocity(newVelocity.multiply(getVelocity().magnitude()));
    }

    public void rotateLeft() {
        view.setRotate(view.getRotate() - TURN_SIZE);
        Point2D newVelocity = new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate())));
        setVelocity(newVelocity.multiply(getVelocity().magnitude()));
    }

    public boolean isColliding(GameObject other) {
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }

    public double getDistanceFromGameObject(GameObject object) {
        Point2D rocketPosition = new Point2D(this.getView().getTranslateX(), this.getView().getTranslateY());
        Point2D targetPosition = new Point2D(object.getView().getTranslateX(), object.getView().getTranslateY());
        return targetPosition.distance(rocketPosition);
    }

    public boolean isInWorld() {
        double objectX = this.getView().getTranslateX();
        double objectY = this.getView().getTranslateY();
        if (objectX > 0 && objectX < WORLD_WIDTH &&
            objectY > 0 && objectY < WORLD_HEIGHT) {
            return true;
        }
        return false;
    }
}
