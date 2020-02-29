package dpandev;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SpeedyRocket extends Application implements ActionListener, KeyListener, dpandev.Commons {

    private boolean screenLoop = true;
    private boolean gamePlay = false;

    private boolean thrust = false;
    private KeyEvent movement, released;
    private int rocketXTracker = SCREEN_WIDTH/2 - ROCKET_WIDTH;

    private double SCENE_WIDTH = 400;
    private double SCENE_HEIGHT = 800;
    private AnimationTimer gameLoop;
    private ImageView bgImageView1;
    private double bgScrollSpeed = 0.5;
    private Pane backgroundLayer;

    private static SpeedyRocket sr = new SpeedyRocket();
    private static SplashScreen ss;

    public SpeedyRocket() {
        //default constructor
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Group root = new Group();
            backgroundLayer = new Pane();
            root.getChildren().add(backgroundLayer);
            Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setFullScreen(false);
            primaryStage.setTitle("Speedy Rocket");
            primaryStage.show();

            loadGame();
            while (gamePlay) {
                startGameLoop();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadGame() {
        bgImageView1 = new ImageView("images/spaceBG.png");
        //repositions the background so it can scroll from bottom to top
        bgImageView1.relocate(0, -bgImageView1.getImage().getHeight() + SCENE_HEIGHT);
        backgroundLayer.getChildren().add(bgImageView1);
    }
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            double yReset = bgImageView1.getLayoutY();
            @Override
            public void handle(long now) {
                double y = bgImageView1.getLayoutY() + bgScrollSpeed;

                if (Double.compare(y, 0) >= 0) {
                    y = yReset;
                }
                bgImageView1.setLayoutY(y);
            }
        };
        gameLoop.start();
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
