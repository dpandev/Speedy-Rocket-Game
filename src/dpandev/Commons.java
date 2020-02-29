package dpandev;

import java.awt.*;

public interface Commons {
    public static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public static final int ALIEN_X = 0;
    public static final int ALIEN_Y = 0;
    public static final int ALIEN_MOV_DIFF = 120;
    public static final int ALIEN_GAP = 120;
    public static final int ROCKET_WIDTH = 75;
    public static final int ROCKET_HEIGHT = 120;
    public static final int ROCKET_X_LOCATION = SCREEN_WIDTH/2;
    public static final int ROCKET_Y_LOCATION = SCREEN_HEIGHT/4;
    public static final int ROCKET_THRUST_DIFF = 10;
    public static final int ROCKET_RETURN_THRUST_DIFF = ROCKET_THRUST_DIFF/2;
    public static final int SCREEN_DELAY = 300;
    public static final int UPDATE_DIFF = 50;
}
