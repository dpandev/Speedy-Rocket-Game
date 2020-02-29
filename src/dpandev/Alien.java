package dpandev;

import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Alien {
    private Image alien;
    private int x = 0, y = 0;

    public Alien(int width, int height) {
        alien = Toolkit.getDefaultToolkit().getImage("images/alien.png");
        scaleAlien(width, height);
    }
    public void scaleAlien(int width, int height) {
        alien = alien.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
    public Image getAlien() {
        return alien;
    }
    public int getWidth() {
        try {
            return alien.getWidth(null);
        } catch (Exception e) {
            return -1;
        }
    }
    public int getHeight() {
        try {
            return alien.getHeight(null);
        } catch (Exception e) {
            return -1;
        }
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public Rectangle getHitbox() {
        return (new Rectangle(x, y, alien.getWidth(null), alien.getHeight(null)));
    }
    public BufferedImage getBI() {
        BufferedImage bi = new BufferedImage(alien.getWidth(null), alien.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.drawImage(alien, 0, 0, null);
        g.dispose();
        return bi;
    }
}
