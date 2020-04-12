package dpandev;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * Represents a {@link Rocket} object. Inherits from {@link Sprite}.
 */
public class Rocket extends Sprite implements Commons{

    private double rocketMinX = 0 - getCenterX()/7;
    private double rocketMaxX = SCREEN_WIDTH - getCenterX()/4.5;
    private double rocketMinR = -30.0;
    private double rocketMaxR = 30.0;
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
    }

    /**
     * Sets the {@link #dx} and {@link #dr} of the {@link Rocket} based on
     * the received input. Calls {@link #engageAutoThrusters()} to reposition the rocket
     * automatically.
     */
    public void processInput() {
        if (input.isMoveLeft()) {
            if (r <= 0) {
                dr = 0 - r;
                dx = -speed;
            }
        } else if (input.isMoveRight()) {
            if (r >= 0) {
                dr = 0 - r;
                dx = speed;
            }
        } else {
            dx = 0d;
            dr = 0d;
        }
        engageAutoThrusters();
    }

    /**
     * Automatically resets the rocket rotational angle to zero when no input is detected and
     * adjusts it when input is detected.
     */
    public void engageAutoThrusters() {
        if (dx < 0) {
            dr = -speed/6;
        } else if (dx > 0) {
            dr = speed/6;
        }
        if (dx == 0 && r < 0) { //if not moving and rocket is tilted left
            if (r > -5) { //fixes rocket shaking bug
                dr = 0;
                r = 3;
            } else {
                dr = speed/4;
            }
        } else if (dx == 0 && r > 0) { //if not moving and rocket is tilted right
            if (r < 5) { //fixes rocket shaking bug
                dr = 0;
                r = 0;
            } else {
                dr = -speed/4;
            }
        }
    }

    /**
     * Checks rocket rotational positioning and prevents the rotational angle from
     * being greater than {@link Rocket#rocketMaxR} and not lower than {@link Rocket#rocketMinR}.
     */
    public void checkRotation() {
        if ((r += dr) > rocketMaxR) {
            dr = 0;
        }
        if ((r += dr) < rocketMinR) {
            dr = 0;
        }
        if (r > rocketMaxR) r = rocketMaxR;
        if (r < rocketMinR) r = rocketMinR;
    }

    /**
     * Moves the {@link Rocket} object and updates {@link dpandev.Sprite#r}. Movement is
     * restricted to the {@code x} axis only. Calls {@link #engageAutoThrusters()} to
     * complete the journey and {@link #checkBounds()} to check that the rocket is within the
     * given screen bounds.
     */
    @Override
    public void move() {
        x += dx;
        r += dr;
        checkRotation();
        checkBounds();
    }

    /**
     * Checks that the {@link #x} is not less than {@link #rocketMinX} nor greater than
     * {@link #rocketMaxX}. Otherwise, sets the {@link #x} to the respective min/max value.
     */
    private void checkBounds() {
        if (x > rocketMaxX) {
            x = rocketMaxX;
        }
        if (x < rocketMinX) {
            x = rocketMinX;
        }
    }

    /**
     * {@inheritDoc}
     * @param otherSprite another {@link Sprite} object to compare values with
     * @return
     */
    @Override
    public boolean collidesWith(Sprite otherSprite) {
        return (otherSprite.x + otherSprite.width >= x + (getWidth() * 0.10)
                && otherSprite.y + otherSprite.height >= y + (getHeight() * 0.10)
                && otherSprite.x + (otherSprite.getWidth() * 0.20) <= (x + width)
                && otherSprite.y + (otherSprite.getHeight() * 0.20) <= (y + height)
        );
    }

    /**
     * {@inheritDoc}
     */
    public void checkRemovability() {}
}
