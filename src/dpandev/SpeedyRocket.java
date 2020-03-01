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
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private Pane scorePanel;//TODO

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
            mainPanel = new Pane();
            root.getChildren().add(mainPanel);
            scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setFullScreen(false);
            primaryStage.setTitle("Speedy Rocket");
            primaryStage.show();

            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    System.out.println("Pressed: " + event.getCode());
                    gamePlay = true;
                }
            });

            loadStartScreen();
            gameOver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
    private void loadGame() {
        rocketImg = new Image("images/rocketship.png", ROCKET_WIDTH, ROCKET_HEIGHT, true, true);
        alienImg = new Image("images/alien.png", ALIEN_WIDTH, ALIEN_HEIGHT, true, true);
        bgImage = new ImageView("images/spaceBG.png");
        //repositions the background so it can scroll from bottom to top
        bgImage.relocate(0, -bgImage.getImage().getHeight() + SCREEN_HEIGHT);

        mainPanel.getChildren().add(bgImage);
    }
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            double yReset = bgImage.getLayoutY();
            @Override
            public void handle(long now) {
                double y = bgImage.getLayoutY() + BG_SCROLL_SPEED;
                if (Double.compare(y, 0) >= 0) {
                    y = yReset;
                }
                bgImage.setLayoutY(y);

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
    private void createPlayer() {
        Input input = new Input(scene);
        input.addListeners();
        player = new Rocket(mainPanel, rocketImg, ROCKET_X_LOCATION, ROCKET_Y_LOCATION, 0, 0, 0, 0, ROCKET_SPEED, input);
    }
    private void spawnAliens(boolean random) {
        if (random && this.random.nextInt(ALIEN_SPAWN_RANDOMNESS) != 0) {
            return;
        }
        double x = this.random.nextDouble() * (SCREEN_WIDTH - alienImg.getWidth());
        double y = -alienImg.getHeight();
        Alien alien = new Alien(mainPanel, alienImg, x, y, 0, 0, ALIEN_MOV_SPEED, 0);
        aliens.add(alien);
    }
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
    private void checkCollisions() {
        collision = false;
        for (Alien aliens: aliens) {
            if (player.collidesWith(aliens)) {
                collision = true;
                this.gamePlay = false;
            }
        }
    }
    private void gameOver() {
        //
    }
    private void fadeScreen(Pane pane) {
        FadeTransition ft = new FadeTransition(Duration.millis(3000), pane);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }
}
