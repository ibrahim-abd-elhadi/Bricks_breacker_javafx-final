package org.example.bricks_breacker_final_project;


import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class GamePaneController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Slider sliderangel;
    @FXML
    private Text ballnum;
    @FXML
    private Text bestScore;
    private List<Text> healthTexts = new ArrayList<>();
    private AnimationTimer gameTimer;
    private double ballRadius = 10;
    private Brick[] bricks;
    private int ballsFallen = 0;
    private ArrayList<Ball> balls = new ArrayList<>();
    private int maxHealth = 30; // Maximum health of a brick
    private int numBricks = 88; // Total number of bricks
    private double cannonX = 350; // X position of the cannon
    private double cannonY = 720; // Y position of the cannon
    private Cannon cannon; // Cannon shape
    private int numBallsToLaunch = 44; // Number of balls to launch
    private int ballsLaunched = 0; // Counter for launched balls
    private boolean gameStarted = false;
    private Line alignment;
    private Line reflectedLine;
    private Circle pivot;
    private int score = 0;
    @FXML
    private Text scoreText; // For displaying the score on the screen
    private int BestScore = 0;
    private GameController gameController;
    private boolean warningSoundPlayed = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameController = new GameController(this);

        createBricks();
        createCannon();
        createAlignment();
        createReflectedLine();
        creatPivot();
        loadBestScore();
        bestScore.setText("Best Score: " + BestScore);

        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBalls();
                updateBestScore(score);
                ballnum.setText("x" + (numBallsToLaunch - ballsLaunched) + "");
                for (Brick brick : bricks) {
                    if (brick != null && brick.getY() >= 570 && brick.getY() <= 591) {
                        if (!warningSoundPlayed) {
                            SoundManager.playSound("Sound_Effects/Warning.mp3");
                            warningSoundPlayed = true;
                        }
                    }
                    if (brick != null && brick.getY() >= 591) {
                        this.stop(); // Stop the game timer
                        Platform.runLater(this::showEndGameDialog); // Show dialog on JavaFX Application Thread
                        warningSoundPlayed = false;
                        break;
                    } else if (areAllBricksInvisible() && (ballsFallen == numBallsToLaunch)) {
                        this.stop(); // Stop the game timer
                        maxHealth += 20;
                        Platform.runLater(this::showLevelUpDialog); // Show dialog on JavaFX Application Thread
                        warningSoundPlayed = false;
                        break;
                    }
                }
                if (ballsFallen == numBallsToLaunch && gameStarted) {
                    reloadBalls(); // Reset game to start new launch
                }
            }

            private void showEndGameDialog() {
                VBox vBox = new VBox(10);
                ImageView loseIcon = new ImageView(String.valueOf(this.getClass().getResource("/org/example/bricks_breacker_final_project/photos/loseicon.jpg")));
                Label loseTextHeader = new Label("A brick has reached critical position: ");
                loseTextHeader.setTextFill(Color.WHITE);
                vBox.setAlignment(Pos.CENTER); // Center the elements in the VBox
                vBox.getChildren().addAll(loseIcon, loseTextHeader);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                SoundManager.playSound("Sound_Effects/Lose.mp3");
                alert.getDialogPane().setContent(vBox);
                alert.setGraphic(null);
                alert.setHeaderText(null);
                alert.getDialogPane().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/css/dialogStyle.css")).toExternalForm());
                alert.setTitle("Game Over");
                ButtonType buttonTypeRetry = new ButtonType("Retry");
                ButtonType buttonTypeExit = new ButtonType("Exit");
                alert.getButtonTypes().setAll(buttonTypeRetry, buttonTypeExit);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == buttonTypeRetry) {
                        SoundManager.playSound("Sound_Effects/Start.mp3");
                        maxHealth = 30;
                        score = 0;
                        numBallsToLaunch = 74;
                        updateScore(0);
                        reset();  // Reset game state and start over
                        gameTimer.start();  // Restart the game timer
                    } else if (result.get() == buttonTypeExit) {
                        System.exit(0);  // Exit the game
                    }
                } else {
                    // Log when no selection is made and the dialog is closed
                    System.out.println("No selection made, dialog closed.");
                }
            }

            private void showLevelUpDialog() {
                SoundManager.playSound("Sound_Effects/Victory.mp3");
                VBox vBox = new VBox(10);
                ImageView winIcon = new ImageView(String.valueOf(this.getClass().getResource("/org/example/bricks_breacker_final_project/photos/winicon.jpg")));
                Label winTextHeader = new Label("Level Cleared You Won: ");
                winTextHeader.setTextFill(Color.WHITE);
                vBox.setAlignment(Pos.CENTER); // Center the elements in the VBox
                vBox.getChildren().addAll(winIcon, winTextHeader);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.getDialogPane().setContent(vBox);
                alert.setGraphic(null);
                alert.setHeaderText(null);
                alert.getDialogPane().getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/css/dialogStyle.css")).toExternalForm());
                alert.setTitle("Congratulations");
                ButtonType buttonTypeNextLevel = new ButtonType("Next Level");
                ButtonType buttonTypeExit = new ButtonType("Exit");
                alert.getButtonTypes().setAll(buttonTypeNextLevel, buttonTypeExit);


                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == buttonTypeNextLevel) {
                        reset();  // Reset game state and start over
                        SoundManager.playSound("Sound_Effects/Start.mp3");
                        gameTimer.start();  // Restart the game timer
                    } else if (result.get() == buttonTypeExit) {
                        System.exit(0);  // Exit the game
                    }
                } else {
                    // Log when no selection is made and the dialog is closed
                    System.out.println("No selection made, dialog closed.");
                }
            }
        };
        gameTimer.start();

        sliderangel.valueChangingProperty().addListener((obs, wasChanging, isNowChanging) -> {
            if (wasChanging && !isNowChanging) {
                launchBalls(sliderangel.getValue());
                gameStarted = true;
                sliderangel.setValue(90);
            }
        });
    }

    private boolean areAllBricksInvisible() {
        for (Brick brick : bricks) {
            if (brick != null && brick.isVisible()) {
                return false;  // Return false immediately if any brick is visible
            }
        }
        return true;  // All bricks are invisible
    }

    private void reset() {
        ballsFallen = 0;
        balls.clear();
        gameStarted = false;
        healthTexts.clear();
        alignment.setVisible(false);
        boolean warningSoundPlayed = false;


        root.getChildren().removeIf(node ->
                (node instanceof Brick ||
                        (node instanceof Text && node != scoreText && node != ballnum && node != bestScore)));

        createBricks();
        createCannon();
        createAlignment();
        createReflectedLine();
        creatPivot();
        numBallsToLaunch += 10;
        gameTimer.start();
    }

    private void loadBestScore() {
        gameController.loadBestScore();
    }

    private void saveBestScore() {
        gameController.saveBestScore();
    }

    public void updateBestScore(int newScore) {
        if (score > BestScore) {
            BestScore = score;
            saveBestScore();
        }
    }

    private void updateScore(int points) {
        score += points;
        scoreText.setText("Score: " + score); // Update the displayed text
    }

    private void updateBalls() {
        ArrayList<Ball> ballsToRemove = new ArrayList<>();

        for (Ball ball : balls) {
            ball.updatePosition();

            if (ball.getCenterX() <= ballRadius || ball.getCenterX() >= root.getWidth() - ballRadius) {
                ball.reverseX();
            }

            if (ball.getCenterY() <= ballRadius) {
                ball.reverseY();
            }

            if (ball.getCenterY() >= root.getHeight() - ballRadius) {
                ballsToRemove.add(ball);
                ballsFallen++;
            }

            if (ballsFallen == numBallsToLaunch) {
                repositionBricks();
                sliderangel.setVisible(true);
            }

            checkBrickCollision(ball);
        }

        balls.removeAll(ballsToRemove);
        root.getChildren().removeAll(ballsToRemove);
    }


    private void createBricks() {
        int brickSize = 50;
        int spacing = 4;
        int numRows = 8;
        int numCols = 11;
        int startX = 50;
        int startY = 80;

        bricks = new Brick[numBricks];

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (isBrick(row, col)) {
                    int index = row * numCols + col;
                    int x = startX + col * (brickSize + spacing);
                    int y = startY + row * (brickSize + spacing);
                    Color color;

                    switch (row % 4) {
                        case 0:
                            color = Color.ORANGE;
                            break;
                        case 1:
                            color = Color.VIOLET;
                            break;
                        case 2:
                            color = Color.RED;
                            break;
                        case 3:
                            color = Color.BLUEVIOLET;
                            break;
                        default:
                            color = Color.WHITE;
                    }

                    Brick brick = new Brick(x, y, brickSize, maxHealth, color);
                    root.getChildren().add(brick);
                    root.getChildren().add(brick.getHealthText());
                    bricks[index] = brick;
                    healthTexts.add(brick.getHealthText());
                } else {
                    bricks[row * numCols + col] = null;
                }
            }
        }
    }

    private boolean isBrick(int row, int col) {
        String design = "###  ## ###" +
                "# ## #   ###" +
                "##    #   #" +
                "##     ####" +
                "###  ## ###" +
                "# ## #   ###" +
                "##    #   #" +
                "##     ####";
        int index = row * 11 + col;
        return design.charAt(index) == '#';
    }

    private void createCannon() {
        cannon = new Cannon(cannonX - ballRadius / 2, cannonY, ballRadius, ballRadius * 2);
        root.getChildren().add(cannon);
    }

    private void launchBalls(double angle) {
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            private final long interval = 49500000;
            private final double fixedAngle = Math.toRadians(-angle + 180);

            @Override
            public void handle(long now) {
                sliderangel.setVisible(false);
                if (ballsLaunched < numBallsToLaunch && (lastUpdate == 0 || now - lastUpdate >= interval)) {
                    double speed = 14;
                    double vx = Math.cos(fixedAngle) * speed;
                    double vy = -Math.sin(fixedAngle) * speed;

                    Ball newBall = new Ball(cannonX, cannonY - ballRadius / 2, ballRadius, vx, vy);
                    root.getChildren().add(newBall);
                    balls.add(newBall);

                    ballsLaunched++;
                    lastUpdate = now;
                }

                if (ballsLaunched >= numBallsToLaunch) {
                    this.stop();
                    sliderangel.setValue(90);
                }
            }
        };
        timer.start();
    }

    private void reloadBalls() {
        balls.clear();
        ballsLaunched = 0;
        ballsFallen = 0;
        gameStarted = false;
    }

    private void repositionBricks() {
        int deltaY = 30;
        for (Brick brick : bricks) {
            if (brick != null && brick.isVisible()) {
                brick.setY(brick.getY() + deltaY);
                brick.getHealthText().setY(brick.getHealthText().getY() + deltaY);
            }
        }
    }

    private void updateHealthText(int index) {
        if (bricks[index] != null && bricks[index].getHealth() == 0) {
            bricks[index].getHealthText().setVisible(false);
        } else if (bricks[index] != null) {
            bricks[index].getHealthText().setText(Integer.toString(bricks[index].getHealth()));
        }
    }


    private void checkBrickCollision(Ball ball) {
        double vx = ball.getVx();
        double vy = ball.getVy();

        for (int i = 0; i < numBricks; i++) {
            if (bricks[i] != null && bricks[i].getHealth() > 0 && ball.getBoundsInParent().intersects(bricks[i].getBoundsInParent())) {
                bricks[i].decreaseHealth();
                updateScore(1);
                updateHealthText(i);
                //  SoundManager.playSound("Sound_Effects/Hit.mp3");
                if (bricks[i].getHealth() == 0) {
                    bricks[i].setVisible(false);
                    SoundManager.playSound("Sound_Effects/Explosion.mp3");
                    updateScore(20);
                }

                double ballCenterX = ball.getCenterX();
                double ballCenterY = ball.getCenterY();
                double brickTop = bricks[i].getY();
                double brickBottom = brickTop + bricks[i].getHeight();
                double brickLeft = bricks[i].getX();
                double brickRight = brickLeft + bricks[i].getWidth();

                double overlapLeft = ballCenterX + ball.getRadius() - brickLeft;
                double overlapRight = brickRight - (ballCenterX - ball.getRadius());
                double overlapTop = ballCenterY + ball.getRadius() - brickTop;
                double overlapBottom = brickBottom - (ballCenterY - ball.getRadius());

                double minHorizontalOverlap = Math.min(overlapLeft, overlapRight);
                double minVerticalOverlap = Math.min(overlapTop, overlapBottom);

                if (minHorizontalOverlap < minVerticalOverlap) {
                    vx *= -1;
                    ball.setCenterX(ballCenterX + (overlapLeft < overlapRight ? -1 : 1) * ball.getRadius());
                } else {
                    vy *= -1;
                    ball.setCenterY(ballCenterY + (overlapTop < overlapBottom ? -1 : 1) * ball.getRadius());
                }

                ball.setVx(vx);
                ball.setVy(vy);
                break;
            }
        }
    }


    public void createAlignment() {
        alignment = new Line();
        alignment.setStartX(cannonX);
        alignment.setStartY(cannonY);
        alignment.setStroke(Color.VIOLET);
        alignment.setStrokeWidth(7);
        alignment.getStrokeDashArray().addAll(11d, 19d);
        root.getChildren().add(alignment);
        alignment.setVisible(false);

        sliderangel.setOnMouseDragged(e -> {
            alignment.setVisible(true);
            updateAlignmentLine(sliderangel.getValue());
            reflectedLine.setVisible(true);
            pivot.setVisible(true);
            reflectedLine.setStartX(alignment.getEndX());
            reflectedLine.setStartY(alignment.getEndY());
            updateReflectedLine(sliderangel.getValue());

        });

        sliderangel.setOnMouseReleased(e -> {
            alignment.setVisible(false);
            reflectedLine.setVisible(false);
            pivot.setVisible(false);

        });
    }

    private void updateAlignmentLine(double angleDegrees) {
        double angleRadians = Math.toRadians(-angleDegrees + 180);
        double lineLength = 1000;
        double endX = cannonX + lineLength * Math.cos(angleRadians);
        double endY = cannonY - lineLength * Math.sin(angleRadians);
        alignment.setEndX(endX);
        alignment.setEndY(endY);

    }


    public void createReflectedLine() {
        reflectedLine = new Line();
        reflectedLine.setStroke(Color.VIOLET);
        reflectedLine.setStrokeWidth(7);
        reflectedLine.getStrokeDashArray().addAll(11d, 15d);
        root.getChildren().add(reflectedLine);
        reflectedLine.setVisible(false);
    }

    private void updateReflectedLine(double angleDegrees) {
        double angleRadians = Math.toRadians(180 - angleDegrees);
        double lineLength = 500;

        if (alignment.getEndX() >= 700) {
            reflectedLine.setStartX(700);
            reflectedLine.setStartY(cannonY - ((700 - cannonX) * Math.tan(angleRadians)));
            reflectedLine.endXProperty().bind(reflectedLine.startXProperty().subtract(lineLength * Math.cos(angleRadians)));
            reflectedLine.endYProperty().bind(reflectedLine.startYProperty().subtract(lineLength * Math.sin(angleRadians)));
        } else if (alignment.getEndX() <= 0) {
            reflectedLine.setStartX(0);
            reflectedLine.setStartY(cannonY + ((cannonX) * Math.tan(angleRadians)));
            reflectedLine.endXProperty().bind(reflectedLine.startXProperty().subtract(lineLength * Math.cos(angleRadians)));
            reflectedLine.endYProperty().bind(reflectedLine.startYProperty().subtract(lineLength * Math.sin(angleRadians)));
        } else if (alignment.getEndY() <= 5) {
            reflectedLine.setStartY(5);
            if (sliderangel.getValue() >= 90) {
                reflectedLine.setStartX(cannonX + ((cannonY) * (1 / Math.tan(angleRadians))));
            }
            if (sliderangel.getValue() < 90) {
                reflectedLine.setStartX(cannonX - (cannonY) * (Math.tan(angleRadians - Math.toRadians(90))));
            }
            reflectedLine.endXProperty().bind(reflectedLine.startXProperty().add(lineLength * Math.cos(angleRadians)));
            reflectedLine.endYProperty().bind(reflectedLine.startYProperty().add(lineLength * Math.sin(angleRadians)));
        } else {
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i] != null && bricks[i].getHealth() > 0 && alignment.getBoundsInParent().intersects(bricks[i].getBoundsInParent())) {
                    double startX = reflectedLine.getStartX();
                    double startY = reflectedLine.getStartY();
                    double brickTop = bricks[i].getY();
                    double brickBottom = brickTop + bricks[i].getHeight();
                    double brickLeft = bricks[i].getX();
                    double brickRight = brickLeft + bricks[i].getWidth();

                    if (startY < brickBottom) {
                        if (startX > brickLeft || startX < brickRight) {
                            reflectedLine.endXProperty().bind(reflectedLine.startXProperty().subtract(lineLength * Math.cos(angleRadians)));
                            reflectedLine.endYProperty().bind(reflectedLine.startYProperty().subtract(lineLength * Math.sin(angleRadians)));
                        }
                    } else {
                        reflectedLine.endXProperty().bind(reflectedLine.startXProperty().add(lineLength * Math.cos(angleRadians)));
                        reflectedLine.endYProperty().bind(reflectedLine.startYProperty().add(lineLength * Math.sin(angleRadians)));
                    }
                }
            }
        }
    }

    public void setBestScore(int bestScore) {
        this.BestScore = bestScore;
    }

    public int getBestScore() {
        return BestScore;
    }

    public void creatPivot() {
        pivot = new Circle();
        pivot.centerXProperty().bind(reflectedLine.startXProperty());
        pivot.centerYProperty().bind(reflectedLine.startYProperty());
        pivot.setRadius(10);
        pivot.setFill(Color.RED);
        pivot.setVisible(false);
        root.getChildren().add(pivot);
    }
}
