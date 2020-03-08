package dpandev;

import javafx.animation.FadeTransition;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Represents an instance of the {@link SpeedyRocket} game.
 */
public class SpeedyRocket extends Application implements Commons {

    private boolean gamePlay = false;

    private Random random = new Random();
    private Image rocketImg;
    private Image alienImg;
    private List<Alien> aliens = new ArrayList<>();
    private Rocket player;
    private boolean collision = false;

    private AnimationTimer gameLoop;
    private AnimationTimer screenLoop;
    private KeyEvent event;
    private ImageView bgImage;
    private ImageView gameName;
    private ImageView pressStart;
    private Scene scene;
    private Pane mainPanel;
    private Stage primaryStage;
    private Pane scorePanel;//TODO

    public SpeedyRocket() {
        //default constructor
    }

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Sets the {@code scene} and initializes the game application window.
     * @param primaryStage the {@code Stage} to set
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            Group root = new Group();
            mainPanel = new Pane();
            root.getChildren().add(mainPanel);
            scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setFullScreen(false);
            primaryStage.setTitle("Speedy Rocket");
            primaryStage.show();

            //detects space bar being pressed to begin game
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    gamePlay = true;
                }
            });

            loadStartScreen();
            gameOver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the elements for the game start screen and starts the background image loop.
     */
    private void loadStartScreen() {
        bgImage = new ImageView("images/spaceBG.png");
        bgImage.relocate(0, -bgImage.getImage().getHeight() + SCREEN_HEIGHT);

        gameName = new ImageView("images/logoSR.png"); //0.77125
        gameName.setY(SCREEN_HEIGHT - (SCREEN_HEIGHT * 0.836));
        pressStart = new ImageView("images/pressStart.png"); //0.41625
        pressStart.setY(SCREEN_HEIGHT - (SCREEN_HEIGHT * 0.471));

        mainPanel.getChildren().addAll(bgImage, gameName, pressStart);

        screenLoop = new AnimationTimer() {
            double yReset = bgImage.getLayoutY();
            @Override
            public void handle(long now) {
                double y = bgImage.getLayoutY() + BG_SCROLL_SPEED;
                if (Double.compare(y, 0) >= 0) {
                    y = yReset;
                }
                bgImage.setLayoutY(y);
                if (gamePlay) {
                    fadeScreen(mainPanel);
                    loadGame();
                    createPlayer();
                    startGameLoop();
                }
            }
        };
        if (!gamePlay) {
            screenLoop.start();
        }
    }

    /**
     * Loads the elements of the game and adds them to the {@code mainPanel} scene.
     */
    private void loadGame() {
        rocketImg = new Image("images/rocketship.png", ROCKET_WIDTH, ROCKET_HEIGHT, true, true);
        alienImg = new Image("images/alien.png", ALIEN_WIDTH, ALIEN_HEIGHT, true, true);
        bgImage = new ImageView("images/spaceBG.png");
        //repositions the background so it can scroll from bottom to top
        bgImage.relocate(0, -bgImage.getImage().getHeight() + SCREEN_HEIGHT);

        mainPanel.getChildren().add(bgImage);
    }

    /**
     * Main game loop with a new background image loop
     */
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            double yLocReset = bgImage.getLayoutY();
            @Override
            public void handle(long now) {
                double yLoc = bgImage.getLayoutY() + BG_SCROLL_SPEED;
                if (Double.compare(yLoc, 0) >= 0) {
                    yLoc = yLocReset;
                }
                bgImage.setLayoutY(yLoc);

                player.processInput();
                spawnAliens(true);

                player.move();
                aliens.forEach(Sprite::move);
                checkCollisions();

                player.updateUI();
                aliens.forEach(Sprite::updateUI);
                aliens.forEach(Sprite::checkRemovability);
                removeSprites(aliens);
            }
        };
        gameLoop.start();
    }

    /**
     * Creates the main player ({@link Rocket}) object and assigns {@code Key Event} listeners
     * from {@link Input}.
     */
    private void createPlayer() {
        Input input = new Input(scene);
        input.addListeners();
        player = new Rocket(mainPanel, rocketImg, ROCKET_X_LOCATION, 0, 0, 0, 0, 0, ROCKET_SPEED, input);
    }

    /**
     * Controls the spawning of {@link Alien} objects. If {@code random.nextInt()} generates
     * an integer greater than 6, an {@link Alien} object is initialized with a random x position
     * within the screen bounds and a y position above the screen bounds. The objects are then
     * added to the {@code aliens} arraylist. Otherwise, if {@code random.nextInt()} generates a
     * number less than 6, the method returns empty and no {@link Alien} objects are initialized.
     * @param spawn boolean - Set to {@code True} to spawn {@link Alien} objects. Set to
     *              {@code False} otherwise.
     */
    private void spawnAliens(boolean spawn) {
        //limits the spawn rate by 0.5
        if (spawn && this.random.nextInt(ALIEN_SPAWN_RANDOMNESS) > 6) {
            return;
        }
        //randomly positions the Alien objects' x position
        double x = this.random.nextDouble() * (SCREEN_WIDTH - alienImg.getWidth());
        //sets the y position to be above the screen
        double y = (SCREEN_HEIGHT * 2) + alienImg.getHeight();
        Alien alien = new Alien(mainPanel, alienImg, x, y, 0, 0, ALIEN_MOV_SPEED, 0);
        aliens.add(alien); //adds the object to the arraylist
    }

    /**
     * Removes {@link Sprite} objects from the {@code List} and {@code Pane} if the objects'
     * {@code removable} value is {@code True}.
     * @param spriteList the list to check objects' values
     */
    private void removeSprites(List<? extends Sprite> spriteList) {
        Iterator<? extends Sprite> iter = spriteList.iterator();
        while (iter.hasNext()) {
            Sprite sprite = iter.next();
            if (sprite.isRemovable()) {
                sprite.removeFromLayer();
                iter.remove();
            }
        }
    }

    /**
     * Checks if the current {@code player} has collided with any {@link Alien} objects. If the
     * there is a collision, {@code gamePlay} is set to {@code False}.
     */
    private void checkCollisions() {
        collision = false;
        for (Alien aliens: aliens) {
            if (player.collidesWith(aliens)) {
                collision = true;
                this.gamePlay = false;
            }
        }
    }

    /**
     * Initializes the end of game elements.
     */
    private void gameOver() {
        //
    }

    /**
     * Applies a fade transition.
     * @param pane the Pane to apply the transition to
     */
    private void fadeScreen(Pane pane) {
        FadeTransition ft = new FadeTransition(Duration.millis(800), pane);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }
}
