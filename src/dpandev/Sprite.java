package dpandev;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class Sprite {
    Image image;
    ImageView imageView;

    Pane layer;

    double x;
    double y;
    double r;

    double dx;
    double dy;
    double dr;

    boolean removable = false;

    double width;
    double height;

    boolean canMove = true;

    public Sprite(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr) {
        this.layer = layer;
        this.image = image;
        this.x = x;
        this.r = r;
        this.dx = dx;
        this.dy = dy;
        this.dr = dr;

        this.imageView = new ImageView(image);
        this.imageView.relocate(x, y);
        this.imageView.setRotate(r);

        this.width = image.getWidth();
        this.height = image.getHeight();

        addToLayer();
    }
    public void addToLayer() {
        this.layer.getChildren().add(this.imageView);
    }

    public void removeFromLayer() {
        this.layer.getChildren().remove(this.imageView);
    }
    public Pane getLayer() {
        return layer;
    }
    public void setLayer(Pane layer) {
        this.layer = layer;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getR() {
        return r;
    }
    public void setR(double r) {
        this.r = r;
    }
    public double getDx() {
        return dx;
    }
    public void setDx(double dx) {
        this.dx = dx;
    }
    public double getDy() {
        return dy;
    }
    public void setDy(double dy) {
        this.dy = dx;
    }
    public double getDr() {
        return dr;
    }
    public void setDr(double dr) {
        this.dr = dr;
    }
    public boolean isRemovable() {
        return removable;
    }
    public void setRemovable(boolean removable) {
        this.removable = removable;
    }
    public void move() {
        if (!canMove) {
            return;
        }
        x += dx;
        y += dy;
        r += dr;
    }
    public ImageView getImageView() {
        return imageView;
    }
    public void updateUI() {
        imageView.relocate(x, y);
        imageView.setRotate(r);
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }
    public double getCenterX() {
        return x + width * 0.5;
    }
    public double getCenterY() {
        return y + height * 0.5;
    }
    public boolean collidesWith(Sprite otherSprite) {
        return (otherSprite.x + otherSprite.width >= x && otherSprite.y + otherSprite.height >= y
                && otherSprite.x <= x + width && otherSprite.y <= y + height);
    }
    public void remove() {
        setRemovable(true);
    }
    public void stopMovement() {
        this.canMove = false;
    }
    public abstract void checkRemovability();
}
