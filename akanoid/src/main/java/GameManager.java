import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameManager {

  public enum GameState {
    READY, RUNNING, FINISHMAP, GAMEOVER
  }

  int width, height;
  public GraphicsContext gc;
  GameState gameState = GameState.READY;

  Paddle paddle;
  // Ball ball;
  List<Ball> balls = new ArrayList<>();
  List<Brick> bricks = new ArrayList<>();
  List<PowerUp> powerUps = new ArrayList<>();
  boolean pressedLeft = false, pressedRight = false;

  Rectangle map;
  ListOfMap LM = new ListOfMap();
  int currentMap = 0;

  public GameManager(int width, int height, Rectangle map, GraphicsContext gc) {
    this.width = width;
    this.height = height;
    this.gc = gc;
    this.map = map;
    ListOfMap LM = new ListOfMap();
    initLevel();
  }

  private void initLevel() {
    paddle = new Paddle(map.width / 2 - 60 + map.x, map.height - 40 + map.y);
    pressedLeft = false;
    pressedRight = false;
    balls.clear();
    Ball initialBall = new Ball(map.width / 2 - 60 + map.x + 50 - 8, map.height - 40 + map.y - 16);
    balls.add(initialBall);
    powerUps.clear();
    bricks.clear();

    double brickW = (map.width - 60) / 8;
    double brickH = 20;

    int[][] arr = LM.getMapByCode(currentMap);

    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        if (arr[r][c] == 0) {
          continue;
        }
        double bx = 30 + c * brickW + map.x;
        double by = 50 + r * (brickH + 6) + map.y;
        if (arr[r][c] == 1) {
          bricks.add(new NormalBrick(bx, by, brickW - 6, brickH));
        } else {
          bricks.add(new EternalBrick(bx, by, brickW, brickH));
        }
      }
    }
    gameState = GameState.READY;
  }

  public void startGame() {
    gameState = GameState.RUNNING;
  }

  public void reset() {
    initLevel();
    currentMap++;
  }

  public void onBallLost(Ball lostBall) {
    balls.remove(lostBall);
    if (balls.isEmpty()) {
      gameState = GameState.GAMEOVER;
      currentMap = 0;
    }
  }

  public void duplicateBalls() {
    List<Ball> newGenerationOfBalls = new ArrayList<>();
    double rotationAngle = Math.toRadians(22.5);
    double cosAngle = Math.cos(rotationAngle);
    double sinAngle = Math.sin(rotationAngle);

    for (Ball originalBall : this.balls) {
      double originalDx = originalBall.dx;
      double originalDy = originalBall.dy;
      double newDx1 = originalDx * cosAngle - originalDy * sinAngle;
      double newDy1 = originalDx * sinAngle + originalDy * cosAngle;

      Ball ball1 = new Ball(originalBall.getX(), originalBall.getY());
      ball1.dx = newDx1;
      ball1.dy = newDy1;
      newGenerationOfBalls.add(ball1);
      double newDx2 = originalDx * cosAngle + originalDy * sinAngle;
      double newDy2 = -originalDx * sinAngle + originalDy * cosAngle;

      Ball ball2 = new Ball(originalBall.getX(), originalBall.getY());
      ball2.dx = newDx2;
      ball2.dy = newDy2;
      newGenerationOfBalls.add(ball2);
    }
    this.balls = newGenerationOfBalls;
  }

  public void update() {
    if (pressedLeft) {
      paddle.moveLeft();
    } else if (pressedRight) {
      paddle.moveRight();
    } else {
      paddle.stop();
    }
    if (gameState == GameState.READY) {
      paddle.update(this);
      if (!balls.isEmpty()) {
        Ball firstBall = balls.get(0);
        firstBall.setY(paddle.getY() - firstBall.getHeight());
        firstBall.setX(paddle.getX() + paddle.getWidth() / 2 - firstBall.getRadius());
      }
    }
    if (gameState == GameState.RUNNING) {
      paddle.update(this);
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
        if (ball.checkCollision(b) != Ball.BallCollision.NONE) {
          b.takeHit();
          ball.bounceOff(b, ball.checkCollision(b));
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
      gameState = GameState.FINISHMAP;
      reset();
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

  void render() {
    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, width, height);
    gc.setFill(Color.GREEN);
    gc.fillRect(map.x, map.y, map.width, map.height);
    for (Brick b : bricks) {
      b.render(gc);
    }
    for (PowerUp pu : powerUps) {
      pu.render(gc);
    }
    paddle.render(gc);
    for (Ball b : balls) {
      b.render(gc);
    }
    gc.setFill(Color.WHITE);
    gc.fillText("State:    " + gameState.name() + "    Level:   " + (this.currentMap + 1), 10, 20);
  }
}