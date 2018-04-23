package launcher;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Vector2;

import java.awt.*;
import java.awt.geom.Line2D;

import static launcher.SmartRocketsConstants.*;

public class Rocket {
    private SimulationBody rocket;
    private DNA dna;
    private int step;

    public Rocket() {
        rocket = new SimulationBody();
        rocket.addFixture(Geometry.createRectangle(ROCKET_WIDTH/SCALE, ROCKET_HEIGHT/SCALE), 1, 0.2, 0.2);
        BodyFixture rocketHead = rocket.addFixture(Geometry.createEquilateralTriangle(ROCKET_HEIGHT/SCALE/3), 1, 0.2, 0.2);
        rocketHead.getShape().translate(0, ROCKET_HEIGHT/SCALE*0.6);
        rocket.translate(0.0, 2.0);
        rocket.setMass(MASS_TYPE);
        dna = new DNA(DNA_LENGTH);
    }

    public SimulationBody getRocket() {
        return rocket;
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
            System.out.println("thruster: " + thruster);
            double force = dna.getForce(step) * elapsedTime * 1000;
            System.out.println("force: " + force);
            double timeSpentInAction = 0;
            double time = dna.getTime(step);
            System.out.println("time: " + time);

            while (timeSpentInAction < time) {
                if (DNA.THRUSTER.FORWARD.equals(thruster)) {
                    System.out.println("fel");
                    this.forwardThrust(force, g);
                }
                if (DNA.THRUSTER.LEFT.equals(thruster)) {
                    System.out.println("balra");
                    this.leftThrustOn(0.1 * force, g);
                }
                if (DNA.THRUSTER.RIGHT.equals(thruster)) {
                    System.out.println("jobbra");
                    this.rightThrustOn(0.1 * force, g);
                }
                timeSpentInAction += elapsedTime;
                System.out.println(timeSpentInAction);
            }
            step++;
            System.out.println("step: " + step);
        }
    }

    private Vector2 getRotation() {
        return new Vector2(this.rocket.getTransform().getRotation() + Math.PI * 0.5);
    }
    private Vector2 getCenter() {
        return this.rocket.getWorldCenter();
    }
}
