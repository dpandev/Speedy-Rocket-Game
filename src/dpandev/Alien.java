package dpandev;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Alien extends Sprite implements Commons{
    public Alien(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr) {
        super(layer, image, x, y, r, dx, dy, dr);
    }
    @Override
    public void move() {
        y += dy;
    }
    @Override
    public void checkRemovability() {
        if(Double.compare(getY(), SCREEN_HEIGHT) > 0) {
            setRemovable(true);
        }
    }
}
