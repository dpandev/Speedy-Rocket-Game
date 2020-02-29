package dpandev;

import java.awt.*;

public class SplashScreen {
    private int screenWidth, screenHeight;
    private boolean isSplash = true;
    private int score;
    private Image background;

    private Rocket rocket;
    private Alien alien;

    public SplashScreen(int screenWidth, int screenHeight, boolean isSplash) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.isSplash = isSplash;
        background = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/space.png"));
        scaleBackground(screenWidth, screenHeight);
    }
    public void scaleBackground(int width, int height) {
        background = background.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
    public int getScore() {
        return score;
    }
    public void increaseScore() {
        score++;
    }
}
