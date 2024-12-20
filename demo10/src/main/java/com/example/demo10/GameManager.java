package com.example.demo10;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.*;

public class GameManager {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private static final int BASKET_WIDTH = 50;
    private static final int BASKET_HEIGHT = 70;
    private static final int FRUIT_SIZE = 40;
    private static final int BOMB_SIZE = 40;
    private static final int TARGET_SCORE = 10;
    private static final int GAME_DURATION = 30_000;

    private double basketX;
    private double basketY;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private int score = 0;
    private boolean gameOver = false;
    private boolean gameWon = false;

    private Random random = new Random();
    private List<GameObject> fruits = new ArrayList<>();
    private List<GameObject> bombs = new ArrayList<>();

    private Image appleImage;
    private Image bananaImage;
    private Image orangeImage;
    private Image basketImage;
    private Image tntImage;
    private Image backgroundImage;

    private long startTime;
    private Canvas canvas;
    private GraphicsContext gc;

    private int level = 1; // Default level
    private SceneManager sceneManager; // Reference to SceneManager

    public GameManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;

        basketX = WIDTH / 2 - BASKET_WIDTH / 2;
        basketY = HEIGHT - BASKET_HEIGHT - 10;

        appleImage = new Image(getClass().getResourceAsStream("/apple.png"));
        bananaImage = new Image(getClass().getResourceAsStream("/banana.png"));
        orangeImage = new Image(getClass().getResourceAsStream("/orange.png"));
        basketImage = new Image(getClass().getResourceAsStream("/basket.png"));
        tntImage = new Image(getClass().getResourceAsStream("/tnt.png"));
        backgroundImage = new Image(getClass().getResourceAsStream("/background.jpg"));

        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
    }

    public void setLevel(int level) {
        this.level = level;
        resetGame();
    }

    public Scene createGameScene() {
        Pane gameRoot = new Pane();
        gameRoot.getChildren().add(canvas);

        Scene gameScene = new Scene(gameRoot);
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                leftPressed = true;
            } else if (event.getCode() == KeyCode.RIGHT) {
                rightPressed = true;
            }
        });

        gameScene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                leftPressed = false;
            } else if (event.getCode() == KeyCode.RIGHT) {
                rightPressed = false;
            }
        });

        return gameScene;
    }

    public void startGame() {
        resetGame();
        startTime = System.currentTimeMillis();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver && !gameWon) {
                    updateGame();
                    renderGame();
                }
            }
        };
        timer.start();
    }

    private void resetGame() {
        basketX = WIDTH / 2 - BASKET_WIDTH / 2;
        fruits.clear();
        bombs.clear();
        score = 0;
        gameOver = false;
        gameWon = false;

    }


    private void increaseFruitSpeed(int speed) {
        fruits.forEach(fruit -> fruit.y += speed);
    }

    private void increaseBombSpawnRate(int rate) {
        for (int i = 0; i < rate; i++) {
            bombs.add(new GameObject(random.nextInt(WIDTH - BOMB_SIZE), 0, BOMB_SIZE, BOMB_SIZE, "tnt"));
        }
    }

    private void updateGame() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (elapsedTime > GAME_DURATION) {
            gameOver = true;
            endGame();
        }

        if (score >= TARGET_SCORE) {
            gameWon = true;
            endGame();
        }

        if (leftPressed) {
            basketX -= 5;
            if (basketX < 0) {
                basketX = 0;
            }
        }

        if (rightPressed) {
            basketX += 5;
            if (basketX > WIDTH - BASKET_WIDTH) {
                basketX = WIDTH - BASKET_WIDTH;
            }
        }

        if (random.nextInt(100) < (3 + level)) {
            String fruitType = getRandomFruitType();
            fruits.add(new GameObject(random.nextInt(WIDTH - FRUIT_SIZE), 0, FRUIT_SIZE, FRUIT_SIZE, fruitType));
        }

        if (random.nextInt(100) < (2 + level)) {
            bombs.add(new GameObject(random.nextInt(WIDTH - BOMB_SIZE), 0, BOMB_SIZE, BOMB_SIZE, "tnt"));
        }

        Iterator<GameObject> fruitIterator = fruits.iterator();
        while (fruitIterator.hasNext()) {
            GameObject fruit = fruitIterator.next();
            fruit.y += 5;

            if (fruit.y > HEIGHT) {
                fruitIterator.remove();
            } else if (fruit.intersects(basketX, basketY, BASKET_WIDTH, BASKET_HEIGHT)) {
                score++;
                fruitIterator.remove();
            }
        }

        Iterator<GameObject> bombIterator = bombs.iterator();
        while (bombIterator.hasNext()) {
            GameObject bomb = bombIterator.next();
            bomb.y += 5;

            if (bomb.y > HEIGHT) {
                bombIterator.remove();
            } else if (bomb.intersects(basketX, basketY, BASKET_WIDTH, BASKET_HEIGHT)) {
                gameOver = true;
                bombIterator.remove();
                endGame();
            }
        }
    }

    private void renderGame() {
        gc.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT);
        gc.drawImage(basketImage, basketX, basketY, BASKET_WIDTH, BASKET_HEIGHT);

        for (GameObject fruit : fruits) {
            switch (fruit.type) {
                case "apple" -> gc.drawImage(appleImage, fruit.x, fruit.y, fruit.width, fruit.height);
                case "banana" -> gc.drawImage(bananaImage, fruit.x, fruit.y, fruit.width, fruit.height);
                case "orange" -> gc.drawImage(orangeImage, fruit.x, fruit.y, fruit.width, fruit.height);
            }
        }

        for (GameObject bomb : bombs) {
            gc.drawImage(tntImage, bomb.x, bomb.y, bomb.width, bomb.height);
        }

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gc.fillText("Score: " + score, 10, 20);
        gc.fillText("Time left: " + Math.max(0, (GAME_DURATION - (System.currentTimeMillis() - startTime)) / 1000) + "s", 10, 40);

        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            gc.fillText("Game Over!", WIDTH / 2 - 60, HEIGHT / 2);
        }

        if (gameWon) {
            gc.setFill(Color.GREEN);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            gc.fillText("You Win!", WIDTH / 2 - 60, HEIGHT / 2);
        }
    }

    private void endGame() {
        renderGame();

        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(
                        javafx.util.Duration.seconds(2),
                        event -> sceneManager.redirectToLevelSelection()
                )
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    private String getRandomFruitType() {
        int randomIndex = random.nextInt(3);
        return switch (randomIndex) {
            case 0 -> "apple";
            case 1 -> "banana";
            case 2 -> "orange";
            default -> "apple";
        };
    }

    private static class GameObject {
        double x, y, width, height;
        String type;

        GameObject(double x, double y, double width, double height, String type) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.type = type;
        }

        boolean intersects(double otherX, double otherY, double otherWidth, double otherHeight) {
            return x < otherX + otherWidth && x + width > otherX && y < otherY + otherHeight && y + height > otherY;
        }
    }
}
