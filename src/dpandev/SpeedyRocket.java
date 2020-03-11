package dpandev;

import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Represents an instance of the {@link SpeedyRocket} game.
 */
public class SpeedyRocket extends Application implements Commons {

    private boolean gamePlay = false;
    private Random random = new Random();
    private int aliensOnScreen = 0;
    private Image rocketImg;
    private Image alienImg;
    private List<Alien> aliens = new ArrayList<>();
    private Rocket player;
    private boolean collision = false;
    private int score = 0;

    private AnimationTimer gameLoop;
    private AnimationTimer screenLoop;
    private KeyEvent event;
    private Text scoreText = new Text();
    private ImageView bgImage;
    private ImageView gameName;
    private ImageView pressStart;
    private Scene scene;
    private Pane mainPanel;
    private Stage primaryStage;
    private Pane scorePanel;

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
     * Sets the {@code scene} and initializes the game application window. The scene height is
     * larger than the visible application window height to allow for the rendering of off screen
     * entities.
     * @param primaryStage the {@code Stage} to set
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            Group root = new Group();
            mainPanel = new Pane();
            scorePanel = new Pane();
            mainPanel.setMaxHeight(SCREEN_HEIGHT*2); //larger for off-screen rendering
            root.getChildren().add(mainPanel);
            root.getChildren().add(scorePanel);
            scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setFullScreen(false);
            primaryStage.setTitle("Speedy Rocket");
            primaryStage.show();

            loadStartScreen();
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

        //detects space bar being pressed to begin game
        EventHandler<KeyEvent> spaceBar = event -> {
            if (event.getCode() == KeyCode.SPACE) {
                gamePlay = true;
            }
        };
        scene.addEventFilter(KeyEvent.KEY_PRESSED, spaceBar);

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
                    screenLoop.stop();
                    scene.removeEventFilter(KeyEvent.KEY_PRESSED, spaceBar);
                    fadeScreen(mainPanel);
                    loadGame();
                    createPlayer();
                    createScoreLayer();
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
                if (aliensOnScreen == 0) {
                    spawnAliens(true);
                } else {
                    //gets y position of last spawned alien
                    if (aliens.get(aliens.size() - 1).getY() > SCREEN_HEIGHT/3) {
                        spawnAliens(true);
                    }
                }

                player.move();
                aliens.forEach(Sprite::move);
                checkCollisions();

                player.updateUI();
                aliens.forEach(Sprite::updateUI);
                aliens.forEach(Sprite::checkRemovability);
                removeSprites(aliens);

                updateScoreLayer();

                if (!gamePlay) {
                    gameLoop.stop();
                    gameOver();
                }
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
        player = new Rocket(mainPanel, rocketImg, ROCKET_X_LOCATION - rocketImg.getWidth()/2,
                ROCKET_Y_LOCATION, 0, 0, 0, 0, ROCKET_SPEED, input);
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
        //limits the total active alien count to 3
        if (spawn && aliensOnScreen > 3) {
            return;
        }
        //randomly positions the Alien objects' x position
        double x = this.random.nextDouble() * (SCREEN_WIDTH - alienImg.getWidth());
        //sets the y position to be above the screen
        double y = 0 - alienImg.getHeight();
        Alien alien = new Alien(mainPanel, alienImg, x, y, 0, 0, ALIEN_MOV_SPEED, 0);
        aliens.add(alien); //adds the object to the arraylist
        aliensOnScreen++;
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
                aliensOnScreen--;
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
                System.out.println("Collision detected!");
                collision = true;
                gamePlay = false;
            }
        }
    }

    /**
     * Initializes the end of game elements.
     */
    private void gameOver() {
        player.setDx(0); //sets player movement to halt
        for (Alien alien : aliens) { //sets alien movement to halt
            alien.setDy(0);
        }

        Text goMessage = new Text();
        goMessage.setFont(Font.font("Impact", FontWeight.BOLD, 64));
        goMessage.setFill(Color.WHITE);
        goMessage.setStroke(Color.BLACK);
        scorePanel.getChildren().add(goMessage);
        double x = SCREEN_WIDTH/2 - goMessage.getBoundsInLocal().getWidth()/2;
        double y = SCREEN_HEIGHT/2 - goMessage.getBoundsInLocal().getHeight()/2;
        goMessage.relocate(x, y);
    }

    /**
     * Creates the score text.
     */
    private void createScoreLayer() {
        scoreText.setFont(Font.font("Impact", FontWeight.BOLD, 36));
        scoreText.setFill(Color.WHITE);
        scoreText.setStroke(Color.BLACK);
        scorePanel.getChildren().add(scoreText);
        scoreText.setText(Integer.toString(score));
        double x = SCREEN_WIDTH/2 - scoreText.getBoundsInLocal().getWidth()/2;
        double y = SCREEN_HEIGHT - (SCREEN_HEIGHT - (scoreText.getBoundsInLocal().getHeight())/2);
        scoreText.relocate(x, y);
    }

    /**
     * Updates the score on screen.
     */
    private void updateScoreLayer() {
        scorePanel.getChildren().remove(scoreText);
        score += BG_SCROLL_SPEED;
        double x = SCREEN_WIDTH/2 - scoreText.getBoundsInLocal().getWidth()/2;
        double y = SCREEN_HEIGHT - (SCREEN_HEIGHT - (scoreText.getBoundsInLocal().getHeight())/2);
        scoreText.relocate(x,y);
        scoreText.setText(Integer.toString(score));
        scorePanel.getChildren().add(scoreText);
    }

    /**
     * Applies a fade transition.
     * @param pane the Pane to apply the transition to
     */
    private void fadeScreen(Pane pane) {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), pane);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }
}
