import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class GameManager {
  int width, height;
  GraphicsContext gc;
  Main.GameState gameState = Main.GameState.READY;
  Paddle paddle;
  Ball ball;
  List<Brick> bricks = new ArrayList<>();
  boolean pressedLeft = false, pressedRight = false;

  GameManager(int width, int height, GraphicsContext gc) {
    this.width = width;
    this.height = height;
    this.gc = gc;
    initLevel();
  }

  void initLevel() {
    paddle = new Paddle(width / 2 - 60, height - 40, 120, 16);
    ball = new Ball(width / 2 - 8, height - 70, 8);
    bricks.clear();
    int rows = 4, cols = 8;

    double brickW = (width - 60) / cols;
    double brickH = 20;

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        double bx = 30 + c * brickW;
        double by = 50 + r * (brickH + 6);
        bricks.add(new NormalBrick(bx, by, brickW - 6, brickH));
      }
    }
    gameState = Main.GameState.READY;
  }

  void startGame() {
    gameState = Main.GameState.RUNNING;
  }

  void reset() {
    initLevel();
  }

  void onBallLost() {
    gameState = Main.GameState.GAMEOVER;
  }

  void update() {
    if (pressedLeft) {
      paddle.moveLeft();
    } else if (pressedRight) {
      paddle.moveRight();
    } else {
      paddle.stop();
    }
    if (gameState == Main.GameState.RUNNING) {
      paddle.update(this);
      ball.update(this);
      if (ball.checkCollision(paddle)) {
        ball.bounceOff(paddle);
        ball.y = paddle.y - ball.height - 1;
      }
      Iterator<Brick> it = bricks.iterator();
      while (it.hasNext()) {
        Brick b = it.next();
        if (ball.checkCollision(b)) {
          b.takeHit();
          ball.bounceOff(b);
          if (b.isDestroyed()) {
            it.remove();
          }
          break;
        }
      }
      if(bricks.isEmpty()){
        gameState = Main.GameState.GAMEOVER;
      }
    }
  }

  void render() {
    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, width, height);
    for (Brick b : bricks) {
      b.render(gc);
    }
    paddle.render(gc);
    ball.render(gc);
    gc.setFill(Color.WHITE);
    gc.fillText("State: " + gameState.name() + "    Press SPACE to start/reset", 10, 20);
  }
}