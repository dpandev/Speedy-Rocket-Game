package dpandev;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;

public class Rocket implements Commons{

    public enum Position {
        left, mid, right;
    }

    private Image rocketship;
    private int x = 0, y = 0;
    private boolean thrusterOn = false;
    private Position destination;

    public Rocket(int width, int height) {
        rocketship = Toolkit.getDefaultToolkit().getImage("images/rocketship.png");
        scaleRocket(width, height);
    }
    public void scaleRocket(int width, int height) {
        rocketship = rocketship.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
    public Image getRocketship() {
        return rocketship;
    }
    public int getWidth() {
        try {
            return rocketship.getWidth(null);
        } catch (Exception e) {
            return -1;
        }
    }
    public int getHeight() {
        try {
            return rocketship.getHeight(null);
        } catch (Exception e) {
            return -1;
        }
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public Position convertPos(int x) {
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
    public boolean isThrusterOn() {
        return thrusterOn;
    }
    public void setThrusterOn(boolean thrusterOn) {
        this.thrusterOn = thrusterOn;
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
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            if (x > SCREEN_WIDTH/3) {
                while (convertPos(x) != destination) {
                    thrustLeft();
                }
                setThrusterOn(false);
            } else {
                setThrusterOn(false);
                thrustBack(Position.right);
                setDestination(null);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            if (x > (SCREEN_WIDTH/3)*2) {
                while (convertPos(x) != destination) {
                    thrustRight();
                }
                setThrusterOn(false);
            } else {
                setThrusterOn(false);
                thrustBack(Position.left);
                setDestination(null);
            }
        }
    }
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            setDestination(Position.left);
            while (canMove(convertPos(getX()), getDestination())) {
                thrustLeft();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            setDestination(Position.right);
            while (canMove(convertPos(getX()), getDestination())) {
                thrustRight();
            }
        }
    }
    public Rectangle getHitbox() {
        return (new Rectangle(x, y, rocketship.getWidth(null), rocketship.getHeight(null)));
    }
    public BufferedImage getBI() {
        BufferedImage bi = new BufferedImage(rocketship.getWidth(null), rocketship.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.drawImage(rocketship, 0, 0, null);//(null) not using interface ImageObserver
        g.dispose();
        return bi;
    }
}
