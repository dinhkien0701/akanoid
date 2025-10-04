import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameManager {
  int width, height;
  GraphicsContext gc;
  Main.GameState gameState = Main.GameState.READY;

  Paddle paddle;
  Ball ball;
  List<Brick> bricks = new ArrayList<>();
  boolean pressedLeft = false, pressedRight = false;
  Rectangle map;
  ListOfMap LM = new ListOfMap();
  int currentMap = 0;

  public GameManager(int width, int height, Rectangle map, GraphicsContext gc) {
    this.width = width;
    this.height = height;
    this.gc = gc;
    this.map = map;
    ListOfMap LM = new ListOfMap("F:\\akanoid\\src\\map.txt");
    initLevel();
  }

  private void initLevel() {
    ListOfMap LM = new ListOfMap("F:\\akanoid\\src\\map.txt");
    paddle = new Paddle(map.width / 2 - 60 + map.x, map.height - 40 + map.y);
    ball = new Ball(map.width / 2 - 60 + map.x + 50 - 8, map.height - 40 + map.y - 16);
    bricks.clear();

    double brickW = (map.width - 60) / 8;
    double brickH = 20;

    int[][] arr = LM.getMapByCode(currentMap);

    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        if(arr[r][c] == 0){
          continue;
        }
        double bx = 30 + c * brickW + map.x;
        double by = 50 + r * (brickH + 6) + map.y;
        if(arr[r][c] == 1) {
          bricks.add(new NormalBrick(bx, by, brickW - 6, brickH));
        } else {
          bricks.add(new EternalBrick(bx, by, brickW, brickH));
        }
      }
    }
    gameState = Main.GameState.READY;
  }

  public void startGame() {
    gameState = Main.GameState.RUNNING;
  }

  public void reset() {
    initLevel();
    currentMap++;
  }

  public void onBallLost() {
    gameState = Main.GameState.GAMEOVER;
    currentMap = 0;
  }

  public void update() {
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
      if (ball.checkCollision(paddle) != Ball.BallCollision.NONE) {
        ball.bounceOff(paddle, ball.checkCollision(paddle));
        ball.y = paddle.y - ball.height - 1;
      }

      Iterator<Brick> it = bricks.iterator();
      while (it.hasNext()) {
        Brick b = it.next();
        if (ball.checkCollision(b) != Ball.BallCollision.NONE) {
          b.takeHit();
          ball.bounceOff(b , ball.checkCollision(b));
          if (b.isDestroyed()) {
            it.remove();
          }
          break;
        }
      }
      if(bricks.isEmpty()){
        gameState = Main.GameState.FINISHMAP;
        reset();
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
    paddle.render(gc);
    ball.render(gc);
    gc.setFill(Color.WHITE);
    gc.fillText("State:    " + gameState.name() + "    Level:   " + this.currentMap, 10, 20);
  }
}