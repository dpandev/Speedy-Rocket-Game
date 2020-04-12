package dpandev;

import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
    private int meteorsOnScreen;
    private Image rocketImg;
    private Image meteorImg;
    private ImageView explosionImage;
    private List<Meteor>meteors = new ArrayList<>();
    private List<Alien> aliens = new ArrayList<>();
    private Rocket player;
    private boolean collision = false;

    private double gameSpeed;
    private double meteorMOVspeed;
    private double rocketSpeed = 4.5;
    private int score;

    private AnimationTimer gameLoop;
    private AnimationTimer screenLoop;
    private KeyEvent event;
    private EventHandler<KeyEvent> spaceBar;
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
        spaceBar = event -> {
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
        mainPanel.getChildren().clear();
        rocketImg = new Image("images/rocketship03.png", ROCKET_WIDTH, ROCKET_HEIGHT, true, true);
//        alienImg = new Image("images/alien.png", ALIEN_WIDTH, ALIEN_HEIGHT, true, true);
        meteorImg = new Image("images/meteor.png", METEOR_WIDTH, METEOR_HEIGHT, true, true);
        bgImage = new ImageView("images/spaceBG.png");
        Image explosionImg = new Image("images/explosion2d.png", 200, 200, true, true);
        explosionImage = new ImageView(explosionImg);
        //repositions the background so it can scroll from bottom to top
        bgImage.relocate(0, -bgImage.getImage().getHeight() + SCREEN_HEIGHT);

        mainPanel.getChildren().add(bgImage);
    }

    /**
     * Main game loop with a new background image loop
     */
    private void startGameLoop() {
        gameSpeed = 7.0;
        meteorMOVspeed = 4.0;
        rocketSpeed = 4.5;
        score = 0;
        gamePlay = true;
        gameLoop = new AnimationTimer() {
            double yLocReset = bgImage.getLayoutY();
            @Override
            public void handle(long now) {
                if (score % 3000 == 0) {
                    gameSpeed++;
                    meteorMOVspeed++;
                    rocketSpeed++;
                    if (score > 30000) {
                        rocketSpeed += 5.0;
                    }
                }
                double yLoc = bgImage.getLayoutY() + gameSpeed;
                if (Double.compare(yLoc, 0) >= 0) {
                    yLoc = yLocReset;
                }
                bgImage.setLayoutY(yLoc);

                player.processInput();
                if (meteorsOnScreen == 0) {
                    spawnMeteors(true);
                } else {
                    //gets y position of last spawned meteor
                    if (meteors.get(meteors.size() - 1).getY() > SCREEN_HEIGHT/3) {
                        spawnMeteors(true);
                    }
                }

                player.move();
                meteors.forEach(Sprite::move);
                checkCollisions();

                player.updateUI();
                meteors.forEach(Sprite::updateUI);
                meteors.forEach(Sprite::checkRemovability);
                removeSprites(meteors);

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
                ROCKET_Y_LOCATION, 0, 0, 0, 0, rocketSpeed, input);
    }

    /**
     * Controls the spawning of {@link Meteor} objects. If {@code random.nextInt()} generates
     * an integer greater than 6, an {@link Meteor} object is initialized with a random x position
     * within the screen bounds and a y position above the screen bounds. The objects are then
     * added to the {@code meteors} arraylist. Otherwise, if {@code random.nextInt()} generates a
     * number less than 6, the method returns empty and no {@link Meteor} objects are initialized.
     * @param spawn boolean - Set to {@code True} to spawn {@link Meteor} objects. Set to
     *              {@code False} otherwise.
     */
    private void spawnMeteors(boolean spawn) {
        //limits the total active meteor count to 3
        if (spawn && gameSpeed < 12) {
            if (meteorsOnScreen > 3) {
                return;
            }
        } else if (spawn && gameSpeed < 20) {
            if (meteorsOnScreen > 1) {
                return;
            }
        }
        //randomly positions the Meteor objects' x position
        double x = this.random.nextDouble() * (SCREEN_WIDTH - meteorImg.getWidth());
        //sets the y position to be above the screen
        double y = 0 - meteorImg.getHeight();
        Meteor meteor = new Meteor(mainPanel, meteorImg, x, y, 0, 0, meteorMOVspeed, 0);
        meteors.add(meteor); //adds the object to the arraylist
        meteorsOnScreen++;
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
                meteorsOnScreen--;
            }
        }
    }

    /**
     * Checks if the current {@code player} has collided with any {@link Meteor} objects. If the
     * there is a collision, {@code gamePlay} is set to {@code False}.
     */
    private void checkCollisions() {
        collision = false;
        for (Meteor meteors: meteors) {
            if (player.collidesWith(meteors)) {
                collision = true;
                gamePlay = false;

                 explosionImage.relocate(player.getCenterX() - explosionImage.getImage().getWidth()/2,
                        player.getCenterY() - explosionImage.getImage().getHeight()/2);
                mainPanel.getChildren().add(explosionImage);
            }
        }
    }

    /**
     * Initializes the end of game components. Displays a 'game over' message and waits for player
     * input to restart the game.
     */
    private void gameOver() {
        player.stopMovement();
        meteors.forEach(Sprite::stopMovement);

        Text goMessage = new Text("Crashed!");
        goMessage.setFont(Font.font("Impact", FontWeight.BOLD, 40));
        goMessage.setFill(Color.WHITE);
        goMessage.setStroke(Color.BLACK);
        mainPanel.getChildren().add(goMessage);
        double x = SCREEN_WIDTH/2 - goMessage.getBoundsInLocal().getWidth()/2;
        double y = SCREEN_HEIGHT/2 - goMessage.getBoundsInLocal().getHeight()/2;
        goMessage.relocate(x, y);

        //detects space bar being pressed
        spaceBar = event -> {
            if (event.getCode() == KeyCode.SPACE) {
                meteors.forEach(Sprite::remove);
                removeSprites(meteors);
                player.removeFromLayer();
                scene.removeEventFilter(KeyEvent.KEY_PRESSED, spaceBar);
                fadeScreen(mainPanel);
                scoreText.setText("");
                loadStartScreen();
            }
        };
        scene.addEventFilter(KeyEvent.KEY_PRESSED, spaceBar);
    }

    /**
     * Creates the score text.
     */
    private void createScoreLayer() {
        scorePanel.getChildren().clear();
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
     * Updates the score on screen while player is in game.
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
     * Applies a quick fade transition.
     * @param pane the Pane to apply the transition to
     */
    private void fadeScreen(Pane pane) {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), pane);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }
}
