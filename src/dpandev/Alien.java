package dpandev;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * Represents an {@code Alien} object. Inherits from {@link Sprite}.
 */
public class Alien extends Sprite implements Commons{
    /**
     * Constructs a new {@link Alien} object.
     * @param layer {@inheritDoc}
     * @param image {@inheritDoc}
     * @param x {@inheritDoc}
     * @param y {@inheritDoc}
     * @param r {@inheritDoc}
     * @param dx {@inheritDoc}
     * @param dy {@inheritDoc}
     * @param dr {@inheritDoc}
     */
    public Alien(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr) {
        super(layer, image, x, y, r, dx, dy, dr);
    }

    /**
     * Moves the {@link Alien} object. The movement is restricted to the {@code y} axis and is
     * rendered off-screen at first.
     */
    @Override
    public void move() {
        y += dy;
    }

    /**
     * {@inheritDoc}
     * If the {@link Alien} object's y position is below the bottom of the screen,
     * {@link dpandev.Sprite#removable} is set to {@code True} for the object.
     */
    @Override
    public void checkRemovability() {
        if(Double.compare(getY(), SCREEN_HEIGHT) > 0) {
            setRemovable(true);
        }
    }
}
