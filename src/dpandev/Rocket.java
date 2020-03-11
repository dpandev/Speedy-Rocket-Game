package dpandev;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * Represents a {@link Rocket} object. Inherits from {@link Sprite}.
 */
public class Rocket extends Sprite implements Commons{

    /**
     * Represents the three main position states a {@link Rocket} object can have with respect
     * to the {@code x} axis.
     * {@link #LEFT}
     * {@link #MID}
     * {@link #RIGHT}
     */
    public enum Position {
        LEFT, MID, RIGHT;
    }

    private Position destination;
    private double rocketMinX;
    private double rocketMaxX;
    private double rocketMinR;
    private double rocketMaxR;
    private double speed;
    private Input input;

    /**
     * Constructs a new {@link Rocket} object.
     * @param layer {@inheritDoc}
     * @param image {@inheritDoc}
     * @param x {@inheritDoc}
     * @param y {@inheritDoc}
     * @param r {@inheritDoc}
     * @param dx {@inheritDoc}
     * @param dy {@inheritDoc}
     * @param dr {@inheritDoc}
     * @param speed {@inheritDoc}
     * @param input {@inheritDoc}
     */
    public Rocket(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr, double speed, Input input) {
        super(layer, image, x, y, r, dx, dy, dr);

        this.input = input;
        this.speed = speed;
        init();
    }

    /**
     * Initializes the {@link #rocketMinX} and {@link #rocketMaxX} for the {@link Rocket} object.
     * These values prevent the {@link Rocket} from going outside of the screen width.
     */
    public void init() {//doesnt allow rocket to be outside screen by more than half rocket width
        rocketMinX = 0 - this.getCenterX();
        rocketMaxX = SCREEN_WIDTH + this.getCenterX();
        rocketMinR = -30.0;
        rocketMaxR = 30.0;
    }

    /**
     * Sets the {@link #dx}, {@link #dr} and {@link #destination} of the {@link Rocket} based on
     * the received {@link Input}.
     */
    public void processInput() {//
        if (input.isMoveLeft()) {
            setDestination(Position.LEFT);
            dx = -speed;
            dr = -speed/4;
        } else if (input.isMoveRight()) {
            setDestination(Position.RIGHT);
            dx = speed;
            dr = speed/4;
        } else {
            setDestination(null);
            dx = 0d;
            dr = 0d;
        }
        thrusters();
    }

    /**
     * Testing method to replace engageAutoThrusters()
     */
    public void thrusters() {
        if (x < SCREEN_WIDTH/3 - getCenterX()) {
            if (dx > 0) { //if on left side, moving right
                dr = speed/4;
            } else {
                dr = -speed/4;
            }
        }
        if (x > (SCREEN_WIDTH/3) * 2 + getCenterX()) {
            if (dx < 0) { //if on right side, moving left
                dr = -speed/4;
            } else {
                dr = speed/4;
            }
        }
        if (dx == 0 && r < 0) { //if not moving and r is not reset
            dr = speed/4;
        } else if (dx == 0 && r > 0) {
            dr = -speed/4;
        }
    }

    public void checkR() {
        if ((r += dr) > rocketMaxR) {
            System.out.println("Reached MAX R value");
            dr = 0;
        }
        if ((r += dr) < rocketMinR) {
            System.out.println("Reached MIN R value");
            dr = 0;
        }
        if (r > rocketMaxR) r = rocketMaxR;
        if (r < rocketMinR) r = rocketMinR;
    }

    /**
     * Automatically completes the journey to the {@link #destination}.
     * @param direction the direction in which to move the rocket
     */
    public void engageAutoThrusters(Position direction) {
        if (direction == Position.LEFT) {
            if (x > SCREEN_WIDTH/3 - this.getCenterX()) {
                while (convertPos(x) != destination) {
                    thrustLeft();
                    if (r != -45) {
                        this.r = -45;
                    }
                }
                r += 45; //sets rotation back to 0 once thrust action completes
            } else {
                thrustBack(Position.RIGHT);
            }
            setDestination(null);
        } else if (direction == Position.RIGHT) {
            if (x > (SCREEN_WIDTH/3)*2 - this.getCenterX()) {
                while (convertPos(x) != destination) {
                    thrustRight();
                    if (r != 45) {
                        this.r = 45;
                    }
                }
                r -= 45; //set rotation back to 0 once thrust action completes
            } else {
                thrustBack(Position.LEFT);
            }
            setDestination(null);
        }
    }

    /**
     * Moves the {@link Rocket} object and updates {@link dpandev.Sprite#r}. Movement is
     * restricted to the {@code x} axis only. Calls {@link #engageAutoThrusters(Position)} to
     * complete the journey and {@link #checkBounds()} to check that the rocket is within the
     * given screen bounds.
     */
    @Override
    public void move() {
//        if (!canMove(convertPos(x), destination)) {
//            return;
//        }
        if (x >= rocketMaxX && destination == Position.RIGHT) {
            return;
        } else if (x <= rocketMinX && destination == Position.LEFT) {
            return;
        }
        checkR();
        x += dx;
        r += dr;
        checkR();
//        engageAutoThrusters(destination);
        checkBounds();
    }

    /**
     * Checks that the {@link #x} is not less than {@link #rocketMinX} nor greater than
     * {@link #rocketMaxX}. Otherwise, sets the {@link #x} to the respective min/max value.
     */
    private void checkBounds() {
        if (Double.compare(x, rocketMinX) < 0) {
            x = rocketMinX;
        } else if (Double.compare(x, rocketMaxX) > 0) {
            x = rocketMaxX;
        }
    }

    /**
     * {@inheritDoc} Sets the removability of the rocket to {@code True} if {@code dx} is 0.
     */
    public void checkRemovability() {
        if (Double.compare(dx, 0) < 0) {
            setRemovable(true);
        }
    }

    /**
     * Converts a given {@code Double} value to a {@link Position} value.
     * @param x the value to convert
     * @return the resulting {@link Position} value
     */
    public Position convertPos(double x) {
        if (x <= (SCREEN_WIDTH/3 - this.getCenterX())) return Position.LEFT;
        else if (x <= ((SCREEN_WIDTH/3)*2) - this.getCenterX()) return Position.MID;
        else if (x <= (SCREEN_WIDTH) - this.getCenterX()) return Position.RIGHT;
        return null;
    }

    /**
     * Gets the destination.
     * @return {@link #destination}
     */
    public Position getDestination() {
        return this.destination;
    }

    /**
     * Sets the {@link #destination}.
     * @param destination the {@link Position} value to set
     */
    public void setDestination(Position destination) {
        this.destination = destination;
    }

    /**
     * Determines whether the {@link Rocket} can move towards a given {@link #destination}, given
     * the current rocket {@link Position}.
     * @param rocketPos the current rocket {@link Position}
     * @param destination the current destination for the rocket
     * @return {@code True} if the rocketPos does not equal destination, {@code False} otherwise.
     */
    public boolean canMove(Position rocketPos, Position destination) {
        return rocketPos != destination;
    }

    /**
     * Moves the rocket to the left.
     */
    public void thrustLeft() {
        this.x -= speed;
    }

    /**
     * Moves the rocket to the right.
     */
    public void thrustRight() {
        this.x += speed;
    }

    /**
     * Automatically moves the rocket back to a specific {@link Position}.
     * @param pos the {@link Position} to move the rocket to
     */
    public void thrustBack(Position pos) {
        if (pos == Position.LEFT) {
            while (convertPos(x) != Position.LEFT || convertPos(x) != Position.MID) {
                thrustLeft();
                if (r != -45) {
                    this.r = -45;
                }
            }
            this.r += 45; //sets rocket angle back to zero
        } else if (pos == Position.RIGHT) {
            while (convertPos(x) != Position.RIGHT || convertPos(x) != Position.MID) {
                thrustRight();
                if (r != 45) {
                    this.r = 45;
                }
            }
            this.r -= 45; //sets rocket angle back to zero
        }
    }
}
