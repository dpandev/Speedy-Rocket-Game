package dpandev;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SpeedyRocket implements ActionListener, KeyListener, dpandev.Commons {

    private boolean screenLoop = true;
    private boolean gamePlay = false;

    private boolean thrust = false;
    private KeyEvent movement, released;
    private int rocketXTracker = SCREEN_WIDTH/2 - ROCKET_WIDTH;

    private static SpeedyRocket sr = new SpeedyRocket();
    private static SplashScreen ss;

    public SpeedyRocket() {
        //default constructor
    }

    public static void main(String[] args) {
        //
    }
    private void gameScreen(boolean isSplash) {
        //splash screen motion elements and graphics
        Rocket player = new Rocket(Commons.ROCKET_WIDTH, Commons.ROCKET_HEIGHT);

        int rocketX = rocketXTracker;

        long startTime = System.currentTimeMillis();

        while (screenLoop) {
            //game loop
            if ((System.currentTimeMillis() - startTime) > UPDATE_DIFF) {
                //will check here is alien object has left the screen and add new aliens

                //moves the rocket based on input
                if (!isSplash) {
                    if (thrust) {
                        player.keyPressed(movement);
                    } else {
                        if (released != null) {
                            player.keyReleased(released);
                        }
                    }
                }
            }
        }
    }
    public void actionPerformed(ActionEvent e) {
        //
    }
    public void keyPressed(KeyEvent e) {
        this.movement = e;
        if (e.getKeyCode() == KeyEvent.VK_A && gamePlay) {
            thrust = true;
        } else if (e.getKeyCode() == KeyEvent.VK_D && gamePlay) {
            thrust = true;
        } else if(e.getKeyCode() == KeyEvent.VK_ESCAPE && !gamePlay) {
            System.exit(0);
        }
    }
    public void keyReleased(KeyEvent e) {
        this.released = e;
        if (e.getKeyCode() == KeyEvent.VK_A && gamePlay) {
            thrust = false;
        } else if (e.getKeyCode() == KeyEvent.VK_D && gamePlay) {
            thrust = false;
        }
    }
    public void keyTyped(KeyEvent e) {
        //
    }
    private void fadeScreen() {
        //
    }
}
