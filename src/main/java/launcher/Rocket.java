package launcher;

import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Vector2;

import java.awt.*;
import java.awt.geom.Line2D;

import static launcher.SmartRocketsConstants.*;

public class Rocket {
    private SimulationBody rocket;
    private Vector2 targetVector;
    private DNA dna;
    private int step;
    private double elapsedTimeInStep;
    private boolean directHit;
    private boolean destroyed;
    private double startDistance;

    public Rocket(Vector2 targetVector) {
        rocket = new SimulationBody();
        this.targetVector = targetVector;
        BodyFixture body = rocket.addFixture(Geometry.createRectangle(ROCKET_WIDTH/SCALE, ROCKET_HEIGHT/SCALE), 1, 0.2, 0.2);
        body.setFilter(new CategoryFilter(1L, 2L));
        BodyFixture rocketHead = rocket.addFixture(Geometry.createEquilateralTriangle(ROCKET_HEIGHT/SCALE/3), 1, 0.2, 0.2);
        rocketHead.setFilter(new CategoryFilter(1L, 2L));
        rocketHead.getShape().translate(0, ROCKET_HEIGHT/SCALE*0.6);
        rocket.translate(0.0, -CANVAS_SIZE.getHeight()/SCALE/2 + 2);
        rocket.setMass(MASS_TYPE);
        dna = new DNA(DNA_LENGTH);
        directHit = false;
        destroyed = false;
        startDistance = getCenter().distance(targetVector);
    }

    public SimulationBody getRocket() {
        return rocket;
    }

    public void directHit() {
        this.directHit = true;
        rocket.setActive(false);
    }

    public DNA getDNA() {
        return this.dna;
    }

    public void setDNA(DNA dna) {
        this.dna = dna;
    }

    public void forwardThrust(double force, Graphics2D g) {
        Vector2 rotation = getRotation();
        Vector2 f = rotation.product(force);
        Vector2 p = getCenter().sum(rotation.product(-0.9));

        this.rocket.applyForce(f);

        g.setColor(Color.ORANGE);
        g.draw(new Line2D.Double(p.x * SCALE, p.y * SCALE, (p.x - f.x) * SCALE, (p.y - f.y) * SCALE));
    }

    public void reverseThrustOn(double force, Graphics2D g) {
        Vector2 rotation = getRotation();
        Vector2 f = rotation.product(-force);
        Vector2 p = getCenter().sum(rotation.product(0.9));

        this.rocket.applyForce(f);

        g.setColor(Color.ORANGE);
        g.draw(new Line2D.Double(p.x * SCALE, p.y * SCALE, (p.x - f.x) * SCALE, (p.y - f.y) * SCALE));
    }

    public void leftThrustOn(double force, Graphics2D g) {
        Vector2 rotation = getRotation();
        Vector2 center = getCenter();
        Vector2 f1 = rotation.product(force).right();
        Vector2 f2 = rotation.product(force).left();
        Vector2 p1 = center.sum(rotation.product(0.9));
        Vector2 p2 = center.sum(rotation.product(-0.9));

        // apply a force to the top going left
        this.rocket.applyForce(f1, p1);
        // apply a force to the bottom going right
        this.rocket.applyForce(f2, p2);

        g.setColor(Color.RED);
        g.draw(new Line2D.Double(p1.x * SCALE, p1.y * SCALE, (p1.x - f1.x) * SCALE, (p1.y - f1.y) * SCALE));
        g.draw(new Line2D.Double(p2.x * SCALE, p2.y * SCALE, (p2.x - f2.x) * SCALE, (p2.y - f2.y) * SCALE));
    }

    public void rightThrustOn(double force, Graphics2D g) {
        Vector2 rotation = getRotation();
        Vector2 center = getCenter();
        Vector2 f1 = rotation.product(force).left();
        Vector2 f2 = rotation.product(force).right();
        Vector2 p1 = center.sum(rotation.product(0.9));
        Vector2 p2 = center.sum(rotation.product(-0.9));

        // apply a force to the top going left
        this.rocket.applyForce(f1, p1);
        // apply a force to the bottom going right
        this.rocket.applyForce(f2, p2);

        g.setColor(Color.RED);
        g.draw(new Line2D.Double(p1.x * SCALE, p1.y * SCALE, (p1.x - f1.x) * SCALE, (p1.y - f1.y) * SCALE));
        g.draw(new Line2D.Double(p2.x * SCALE, p2.y * SCALE, (p2.x - f2.x) * SCALE, (p2.y - f2.y) * SCALE));
    }

    public void makeNextMove(double elapsedTime, Graphics2D g) {
        if (step != DNA_LENGTH) {
            DNA.THRUSTER thruster = dna.getThruster(step);
            double force = dna.getForce(step) * elapsedTime * 1000;
            if (DNA.THRUSTER.FORWARD.equals(thruster)) {
                this.forwardThrust(force, g);
            }
            if (DNA.THRUSTER.LEFT.equals(thruster)) {
                this.leftThrustOn(0.1 * force, g);
            }
            if (DNA.THRUSTER.RIGHT.equals(thruster)) {
                this.rightThrustOn(0.1 * force, g);
            }

            double timeInStep = dna.getTime(step);
            if (elapsedTimeInStep < timeInStep) {
                elapsedTimeInStep += elapsedTime;
            } else {
                step++;
            }
        } else {
            rocket.setActive(false);
        }
    }

    public int calculateFitness() {
        if (destroyed) {
            return 1;
        } else {
            return (int) (2 - getDistance() / startDistance) * 100;
        }
    }

    public double getDistance() {
        if (directHit) {
            return 0;
        } else {
            return getCenter().distance(targetVector);
        }
    }

    private Vector2 getRotation() {
        return new Vector2(this.rocket.getTransform().getRotation() + Math.PI * 0.5);
    }

    private Vector2 getCenter() {
        return this.rocket.getWorldCenter();
    }

    public void destroyed() {
        destroyed = true;
        rocket.setActive(false);
    }
}
