package dyn4smartrockets;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;

import java.util.ArrayList;

public class HitWallListener extends CollisionAdapter {
    private Rocket rocket;
    private Body rocketBody;
    private ArrayList<SimulationBody> walls;
    private boolean isDestroyed;

    public HitWallListener(Rocket rocket, ArrayList<SimulationBody> walls) {
        this.rocket = rocket;
        this.rocketBody = rocket.getRocket();
        this.walls = walls;
        isDestroyed = false;
    }

    @Override
    public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2) {
        walls.forEach(wall -> {
            if ((body1 == rocketBody && body2 == wall) ||
                (body1 == wall && body2 == rocketBody)) {
                isDestroyed = true;
                rocket.destroyed();
            }
        });
        return isDestroyed;
    }
}
