package process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import core.*;
import object.*;
import map.*;

public class PlayingProcess {
  enum PlayingState {
    READY, RUNNING, FINISH_MAP
  }

  private final int width, height;
  private final GraphicsContext gc;
  private final GameManager gameManager;
  private PlayingState playingState;
  private static final int INITIAL_LIVES = 3;
  private int lives;
  private Paddle paddle;
  private List<Ball> balls = new ArrayList<>();
  private List<Brick> bricks = new ArrayList<>();
  private List<PowerUp> powerUps = new ArrayList<>();
  private boolean pressedLeft = false, pressedRight = false;
  // ----------------------------------------

  public Rectangle map;
  ListOfMap LM = new ListOfMap();
  int currentMap = 0;

  public PlayingProcess(int width, int height, Rectangle map, GraphicsContext gc, GameManager gameManager) {
    this.width = width;
    this.height = height;
    this.gc = gc;
    this.map = map;
    this.gameManager = gameManager;
    this.lives = INITIAL_LIVES;
    initLevel();
  }

  private void initLevel() {
    paddle = new Paddle(map.getX() + map.getWidth() / 2 - 50, map.getY() + map.getHeight() - 40);
    pressedLeft = false;
    pressedRight = false;

    powerUps.clear();
    balls.clear();
    Ball initialBall = new Ball(0, 0);
    balls.add(initialBall);

    bricks.clear();
    double brickW = (map.getWidth() - 60) / 8;
    double brickH = 20;
    int[][] arr = LM.getMapByCode(currentMap);
    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        if (arr[r][c] == 0)
          continue;
        double bx = 30 + c * brickW + map.getX();
        double by = 50 + r * (brickH + 6) + map.getY();
        bricks.add(new NormalBrick(bx, by, brickW - 6, brickH));
      }
    }
    playingState = PlayingState.READY;
  }

  private void resetAfterLifeLost() {
    playingState = PlayingState.READY;
    paddle.setX(map.getX() + map.getWidth() / 2 - paddle.getWidth() / 2);
    paddle.setY(map.getY() + map.getHeight() - 40);
    balls.clear();
    Ball newBall = new Ball(0, 0);
    balls.add(newBall);
  }

  public void onBallLost(Ball lostBall) {
    balls.remove(lostBall);
    if (balls.isEmpty()) {
      this.lives--;
      if (this.lives > 0) {
        resetAfterLifeLost();
      } else {
        gameManager.setGameOver();
      }
    }
  }

  public void startGame() {
    if (playingState == PlayingState.READY) {
      playingState = PlayingState.RUNNING;
    }
  }

  public void duplicateBalls() {
    List<Ball> newGenerationOfBalls = new ArrayList<>();
    double rotationAngle = Math.toRadians(22.5);
    double cosAngle = Math.cos(rotationAngle);
    double sinAngle = Math.sin(rotationAngle);

    for (Ball originalBall : this.balls) {
      double originalDx = originalBall.getDx();
      double originalDy = originalBall.getDy();
      Ball ball1 = new Ball(originalBall.getX(), originalBall.getY());
      ball1.setDx(originalDx * cosAngle - originalDy * sinAngle);
      ball1.setDy(originalDx * sinAngle + originalDy * cosAngle);
      newGenerationOfBalls.add(ball1);
      Ball ball2 = new Ball(originalBall.getX(), originalBall.getY());
      ball2.setDx(originalDx * cosAngle + originalDy * sinAngle);
      ball2.setDy(-originalDx * sinAngle + originalDy * cosAngle);
      newGenerationOfBalls.add(ball2);
    }
    this.balls = newGenerationOfBalls;
  }

  private void initInput(Scene scene) {
    scene.setOnKeyPressed(e -> {
      KeyCode code = e.getCode();
      switch (code) {
        case SPACE:
        case UP:
          startGame();
          break;
        case LEFT:
          pressedLeft = true;
          break;
        case RIGHT:
          pressedRight = true;
          break;
      }
    });

    scene.setOnKeyReleased(e -> {
      if (e.getCode() == KeyCode.LEFT)
        pressedLeft = false;
      if (e.getCode() == KeyCode.RIGHT)
        pressedRight = false;
    });
  }

  public void update(Scene scene) {
    initInput(scene);

    if (pressedLeft)
      paddle.moveLeft();
    else if (pressedRight)
      paddle.moveRight();
    else
      paddle.stop();

    paddle.update(this);

    if (playingState == PlayingState.READY) {
      if (!balls.isEmpty()) {
        Ball firstBall = balls.get(0);
        firstBall.setY(paddle.getY() - firstBall.getHeight());
        firstBall.setX(paddle.getX() + paddle.getWidth() / 2 - firstBall.getRadius());
      }
    }

    if (playingState == PlayingState.RUNNING) {
      Iterator<Ball> ballIterator = balls.iterator();
      while (ballIterator.hasNext()) {
        Ball b = ballIterator.next();
        b.update(this);
        if (b.checkCollision(paddle) != Ball.BallCollision.NONE) {
          b.bounceOff(paddle, b.checkCollision(paddle));
          b.setY(paddle.getY() - b.getHeight() - 1);
        }
      }
      checkBricksList();
      updatePowerUps();
    }
  }

  private void checkBricksList() {
    for (Ball ball : balls) {
      Iterator<Brick> it = bricks.iterator();
      while (it.hasNext()) {
        Brick b = it.next();
        Ball.BallCollision collision = ball.checkCollision(b);
        if (collision != Ball.BallCollision.NONE) {
          b.takeHit();
          ball.bounceOff(b, collision);
          if (b.isDestroyed()) {
            if (Math.random() < 0.2) {
              powerUps.add(new DuplicateBallPowerUp(b.getX(), b.getY()));
            }
            it.remove();
          }
          break;
        }
      }
    }

    if (bricks.isEmpty()) {
      playingState = PlayingState.FINISH_MAP;
      currentMap++;
      initLevel();
    }
  }

  private void updatePowerUps() {
    Iterator<PowerUp> it = powerUps.iterator();
    while (it.hasNext()) {
      PowerUp pu = it.next();
      pu.update(this);

      if (paddle.getX() < pu.getX() + pu.getWidth() &&
          paddle.getX() + paddle.getWidth() > pu.getX() &&
          paddle.getY() < pu.getY() + pu.getHeight() &&
          paddle.getY() + paddle.getHeight() > pu.getY()) {
        pu.applyEffect(this);
        it.remove();
      } else if (pu.getY() > this.height) {
        it.remove();
      }
    }
  }

  public void render() {
    gc.setFill(Color.web("#2c3e50"));
    gc.fillRect(map.getX(), map.getY(), map.getWidth(), map.getHeight());

    for (Brick b : bricks)
      b.render(gc);
    for (PowerUp pu : powerUps)
      pu.render(gc);
    paddle.render(gc);
    for (Ball b : balls)
      b.render(gc);
    gc.setFill(Color.WHITE);
    String statusText = String.format("State: %s    Level: %d    Lives: %d",
        playingState.name(),
        this.currentMap + 1,
        this.lives);
    gc.fillText(statusText, 10, 20);
  }
}