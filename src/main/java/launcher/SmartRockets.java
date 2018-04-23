package launcher;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static launcher.SmartRocketsConstants.*;

public class SmartRockets extends SimulationFrame {
    private RocketWorld rocketWorld;
    private ArrayList<Rocket> rockets;

        // Some booleans to indicate that a key is pressed
        private AtomicBoolean forwardThrustOn = new AtomicBoolean(false);
        private AtomicBoolean reverseThrustOn = new AtomicBoolean(false);
        private AtomicBoolean leftThrustOn = new AtomicBoolean(false);
        private AtomicBoolean rightThrustOn = new AtomicBoolean(false);

        public SmartRockets() {
            super("SmartRockets", SCALE, CANVAS_SIZE);
        }

        protected void initializeWorld() {
            this.world.setGravity(GRAVITY);

            // the bounds so we can keep playing
            rocketWorld = new RocketWorld();
            rocketWorld.getWalls().forEach(wall -> this.world.addBody(wall));

            rockets = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_ROCKETS; i++) {
                rockets.add(new Rocket());
            }
            rockets.stream().forEach(rocket -> this.world.addBody(rocket.getRocket()));
        }

        @Override
        protected void update(Graphics2D g, double elapsedTime) {
            super.update(g, elapsedTime);

            for (int i = 0; i < NUMBER_OF_ROCKETS; i++) {
                rockets.get(i).makeNextMove(elapsedTime, g);
            }

        }

        public static void main(String[] args) {
            SmartRockets simulation = new SmartRockets();
            simulation.run();
        }
    }
