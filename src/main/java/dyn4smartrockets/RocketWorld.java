package dyn4smartrockets;

import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

import java.util.ArrayList;

import static dyn4smartrockets.SmartRocketsConstants.CANVAS_SIZE;
import static dyn4smartrockets.SmartRocketsConstants.SCALE;
import static dyn4smartrockets.SmartRocketsConstants.TARGET_RADIUS;

public class RocketWorld {
    private ArrayList<SimulationBody> walls;
    private SimulationBody target;

    public RocketWorld() {
        walls = new ArrayList<>();
        double canvasRelativeHeight = CANVAS_SIZE.getHeight()/SCALE;
        double canvasRelativeWidth = CANVAS_SIZE.getWidth()/SCALE;

        SimulationBody left = new SimulationBody();
        left.addFixture(Geometry.createRectangle(1, canvasRelativeHeight));
        left.translate(-(canvasRelativeWidth + 1)/2, 0);
        left.setMass(MassType.INFINITE);
        walls.add(left);

        SimulationBody right = new SimulationBody();
        right.addFixture(Geometry.createRectangle(1, canvasRelativeHeight));
        right.translate((canvasRelativeWidth + 1)/2, 0);
        right.setMass(MassType.INFINITE);
        walls.add(right);

        SimulationBody top = new SimulationBody();
        top.addFixture(Geometry.createRectangle(canvasRelativeWidth, 1));
        top.translate(0, (canvasRelativeHeight + 1)/2);
        top.setMass(MassType.INFINITE);
        walls.add(top);

        SimulationBody bottom = new SimulationBody();
        bottom.addFixture(Geometry.createRectangle(canvasRelativeWidth, 1));
        bottom.translate(0, -(canvasRelativeHeight + 1)/2);
        bottom.setMass(MassType.INFINITE);
        walls.add(bottom);

        SimulationBody obstacle = new SimulationBody();
        obstacle.addFixture(Geometry.createRectangle(canvasRelativeWidth/3, 2));
        obstacle.translate(0, 0);
        obstacle.setMass(MassType.INFINITE);
        walls.add(obstacle);

        target = new SimulationBody();
        target.addFixture(Geometry.createCircle(TARGET_RADIUS));
        target.translate(TARGET_RADIUS / 2, (canvasRelativeHeight + 1)/2 - TARGET_RADIUS * 2);
        target.setMass(MassType.INFINITE);
    }

    public ArrayList<SimulationBody> getWalls() {
        return walls;
    }

    public SimulationBody getTarget() {
        return target;
    }
}
