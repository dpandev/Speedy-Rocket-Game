package dpandev;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;

/**
 * Represents a {@link Sprite} object.
 */
public abstract class Sprite {

    /**
     * Represents the {@code Image} which will be assigned to the {@code ImageView} object.
     */
    Image image;

    /**
     * Represents the {@code ImageView} object.
     */
    ImageView imageView;

    /**
     * Represents a {@code Rotate} object that will be used for applying transformations to the
     * {@code ImageView} object.
     */
    Rotate rotate;

    /**
     * Represents the {@code Pane} for the {@link Sprite} object to modify/update.
     */
    Pane layer;

    /**
     * Represents the horizontal positioning of the {@link Sprite} object.
     */
    double x;

    /**
     * Represents the vertical positioning of the {@link Sprite} object.
     */
    double y;

    /**
     * Represents the rotational angle of the {@link Sprite} object.
     */
    double r;

    /**
     * Represents the horizontal change in {@code x}.
     */
    double dx;

    /**
     * Represents the vertical change in {@code y}.
     */
    double dy;

    /**
     * Represents the rotational angle change in {@code r}.
     */
    double dr;

    /**
     * Represents the removability characteristic of the {@link Sprite} object.
     * A {@link Sprite} object can be removed from the {@code layer} if {@code removable}
     * is set to {@code True}.
     */
    boolean removable = false;

    /**
     * Represents the horizontal length of the {@code Image} object.
     */
    double width;

    /**
     * Represents the vertical length of the {@code Image} object.
     */
    double height;

    /**
     * Represents the mobility characteristic of the {@link Sprite} object.
     * A {@link Sprite} object can move (can update {@link #x}, {@link #y}, {@link #r} positions)
     * if {@code canMove} is set to {@code True}.
     */
    boolean canMove = true;

    /**
     * Constructs a new {@code Sprite} object.
     * @param layer  the {@code Pane} layer to add the {@code Image} object to
     * @param image  the {@code Image} to add to the {@code Sprite} object
     * @param x  the x position of the {@code Image} object to set relative to the screen
     * @param y  the y position of the {@code Image} object to set relative to the screen
     * @param r  the rotational angle of the {@code Image} object to set
     * @param dx  the change to add for the x position of the {@code Image} object
     * @param dy  the change to add for the y position of the {@code Image} object
     * @param dr  the change to add for the rotational angle of the {@code Image} object
     */
    public Sprite(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr) {
        this.layer = layer;
        this.image = image;
        this.x = x;
        this.y = y;
        this.r = r;
        this.dx = dx;
        this.dy = dy;
        this.dr = dr;

        this.imageView = new ImageView(image);
        this.imageView.relocate(x, y); //positions the image to the given coordinates (x, y)
        this.rotate = new Rotate();
        rotate.setAngle(r); //sets the rotational angle for the image
        rotate.setPivotX(x);
        rotate.setPivotY(y);
        //transforms the image based on the parameters (x, y, r)
        imageView.getTransforms().add(rotate);

        this.width = image.getWidth();
        this.height = image.getHeight();

        addToLayer(); //updates the current Pane object
    }

    /**
     * Adds the current {@code ImageView} object to the {@link #layer}.
     */
    public void addToLayer() {
        this.layer.getChildren().add(this.imageView);
    }

    /**
     * Removes the current {@code ImageView} object from the {@link #layer}.
     */
    public void removeFromLayer() {
        this.layer.getChildren().remove(this.imageView);
    }

    /**
     * Gets the current {@code Pane} object ({@code layer}).
     * @return {@link #layer}
     */
    public Pane getLayer() {
        return layer;
    }

    /**
     * Sets the {@code Pane} object ({@code layer}).
     * @param layer the {@code Pane} object to set
     */
    public void setLayer(Pane layer) {
        this.layer = layer;
    }

    /**
     * Gets the {@code x} position of the {@link Sprite} object.
     * @return {@link #x}
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the {@code x} position of the {@link Sprite} object.
     * @param x the position to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the {@code y} position of the {@link Sprite} object.
     * @return {@link #y}
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the {@code y} position of the {@link Sprite} object.
     * @param y the position to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Gets the {@code r} angle of the {@link Sprite} object.
     * @return {@link #r}
     */
    public double getR() {
        return r;
    }

    /**
     * Sets the rotational angle {@code r} for the {@link Sprite} object.
     * @param r the rotational angle to set
     */
    public void setR(double r) {
        this.r = r;
    }

    /**
     * Gets the {@code dx} for the {@link Sprite} object.
     * @return {@link #dx}
     */
    public double getDx() {
        return dx;
    }

    /**
     * Sets the {@link #dx} for the {@link Sprite} object.
     * @param dx the horizontal change in {@code x} to set
     */
    public void setDx(double dx) {
        this.dx = dx;
    }

    /**
     * Gets the change in {@code y} position for the {@link Sprite} object.
     * @return {@link #dy}
     */
    public double getDy() {
        return dy;
    }

    /**
     * Sets the {@link #dy} for the {@link Sprite} object.
     * @param dy the vertical change in {@code y} to set
     */
    public void setDy(double dy) {
        this.dy = dx;
    }

    /**
     * Gets the change in the rotational angle for the {@link Sprite} object.
     * @return {@link #dr}
     */
    public double getDr() {
        return dr;
    }

    /**
     * Sets the {@link #dr} for the {@link Sprite} object.
     * @param dr the change in the rotational angle
     */
    public void setDr(double dr) {
        this.dr = dr;
    }

    /**
     * Gets the status of {@link #removable}.
     * @return {@link #removable}
     */
    public boolean isRemovable() {
        return removable;
    }

    /**
     * Sets the removability of the {@link Sprite} object.
     * @param removable {@code True} or {@code False}
     */
    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    /**
     * Moves the {@link Sprite} object.
     * More specifically, this updates the positioning ({@link #x}, {@link #y}, {@link #r})
     * of the {@link Sprite} object IF the {@code this} object's {@link #canMove} is
     * set to {@code True}, else it returns empty.
     */
    public void move() {
        if (!canMove) {
            return;
        }
        x += dx;
        y += dy;
        r += dr;
    }

    /**
     * Gets the {@code ImageView} object.
     * @return {@link #imageView}
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Repositions the {@code ImageView} object. Sets the {@link #x} and {@link #y} positions
     * and rotates the {@code ImageView} object based on the value of {@link #r}.
     */
    public void updateUI() {
        imageView.relocate(x, y);
        imageView.setRotate(r);
    }

    /**
     * Gets the {@code width} of the {@code Image} object.
     * @return {@link #width}
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the {@code height} of the {@code Image} object.
     * @return {@link #height}
     */
    public double getHeight() {
        return height;
    }

    /**
     * Gets the horizontal center position of the {@code Image} object.
     * @return the horizontal center position
     */
    public double getCenterX() {
        return x + width * 0.5;
    }

    /**
     * Gets the vertical center position of the {@code Image} object.
     * @return the vertical center position
     */
    public double getCenterY() {
        return y + height * 0.5;
    }

    /**
     * Detects any collisions with another {@link Sprite} object.
     * This method returns {@code True} if {@code this} object has overlapping
     * ({@link #x}, {@link #y}) values with another {@link Sprite} object.
     * @param otherSprite another {@link Sprite} object to compare values with
     * @return {@code True} if sprites overlap, {@code False} otherwise.
     */
    public boolean collidesWith(Sprite otherSprite) {
        return (otherSprite.x + otherSprite.width >= x && otherSprite.y + otherSprite.height >= y
                && otherSprite.x <= x + width && otherSprite.y <= y + height);
    }

    /**
     * This method automatically sets the removability of the {@link Sprite} object to {@code True}.
     */
    public void remove() {
        setRemovable(true);
    }

    /**
     * This method automatically sets the mobility ({@link #canMove}) of the {@link Sprite}
     * object to {@code False}.
     */
    public void stopMovement() {
        this.canMove = false;
    }

    /**
     * Checks the removability of the {@link Sprite} object.
     */
    public abstract void checkRemovability();
}
