package dpandev;

public interface Commons {
    public static final double SCREEN_WIDTH = 400d;
    public static final double SCREEN_HEIGHT = 800d;
    public static final double ALIEN_WIDTH = 80d;
    public static final double ALIEN_HEIGHT = 80d;
    public static final double ALIEN_MOV_SPEED = 2.0;
    public static final int ALIEN_SPAWN_RANDOMNESS = 10;
    public static final double ROCKET_WIDTH = 75d;
    public static final double ROCKET_HEIGHT = 148d;
    public static final double ROCKET_X_LOCATION = SCREEN_WIDTH/2;
    public static final double ROCKET_Y_LOCATION = SCREEN_HEIGHT - (SCREEN_HEIGHT/4);
    public static final double ROCKET_THRUST_DIFF = 10.0;
    public static final double ROCKET_RETURN_THRUST_DIFF = ROCKET_THRUST_DIFF/2;
    public static final double ROCKET_SPEED = 2.0;
    public static final double BG_SCROLL_SPEED = 5.0;
}
