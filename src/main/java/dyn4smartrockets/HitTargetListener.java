package dyn4smartrockets;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;

public class HitTargetListener extends CollisionAdapter {
    private Rocket rocket;
    private Body rocketBody, target;

    public HitTargetListener(Rocket rocket, Body target) {
        this.rocket = rocket;
        this.rocketBody = rocket.getRocket();
        this.target = target;
    }

    @Override
    public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2) {
        if ((body1 == rocketBody && body2 == target) ||
            (body1 == target && body2 == rocketBody)) {
            rocket.directHit();
            return false;
        }
        return true;
    }
}
