package dpandev;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Rocket extends Sprite implements Commons{

    public enum Position {
        left, mid, right;
    }

    private Position destination;
    private double rocketMinX;
    private double rocketMaxX;
    private double speed;
    private Input input;

    public Rocket(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr, double speed, Input input) {
        super(layer, image, x, y, r, dx, dy, dr);

        this.input = input;
        this.speed = speed;
        init();
    }
    public void init() {//doesnt allow rocket to be outside screen
        rocketMinX = 0 - image.getWidth();
        rocketMaxX = SCREEN_WIDTH - image.getWidth();
    }
    public void processInput() {//
        if (input.isMoveLeft()) {
            dx = -speed;
            setDestination(Position.left);
        } else if (input.isMoveRight()) {
            setDestination(Position.right);
            dx = speed;
        } else {
            setDestination(null);
            dx = 0.0;
        }
    }
    public void engageAutoThrusters(Position direction) {
        if (direction == Position.left) {
            if (x > SCREEN_WIDTH/3) {
                while (convertPos(x) != destination) {
                    thrustLeft();
                }
            } else {
                thrustBack(Position.right);
                setDestination(null);
            }
        } else if (direction == Position.right) {
            if (x > (SCREEN_WIDTH/3)*2) {
                while (convertPos(x) != destination) {
                    thrustRight();
                }
            } else {
                thrustBack(Position.left);
                setDestination(null);
            }
        }
    }
    @Override
    public void move() {
        if (!canMove(convertPos(x), destination)) {
            return;
        }
        x += dx;
        engageAutoThrusters(destination);
        checkBounds();
    }
    private void checkBounds() {
        if (Double.compare( x, rocketMinX) < 0) {
            x = rocketMinX;
        } else if (Double.compare(x, rocketMaxX) > 0) {
            x = rocketMaxX;
        }
    }
    public void checkRemovability() {
        //
    }
    public Position convertPos(double x) {
        if (x <= SCREEN_WIDTH/3) return Position.left;
        else if (x <= (SCREEN_WIDTH/3)*2) return Position.mid;
        else if (x <= (SCREEN_WIDTH)) return Position.right;
        return null;
    }
    public Position getDestination() {
        return this.destination;
    }
    public void setDestination(Position destination) {
        this.destination = destination;
    }
    public boolean canMove(Position rocketPos, Position destination) {
        return rocketPos != destination;
    }
    public void thrustLeft() {
        this.x--;
    }
    public void thrustRight() {
        this.x++;
    }
    public void thrustBack(Position pos) {
        if (pos == Position.left) {
            while (convertPos(x) != Position.left || convertPos(x) != Position.mid) {
                thrustLeft();
            }
        } else if (pos == Position.right) {
            while (convertPos(x) != Position.right || convertPos(x) != Position.mid) {
                thrustRight();
            }
        }
    }
}
