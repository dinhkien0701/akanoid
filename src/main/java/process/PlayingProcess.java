package process;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import core.*;
import object.Paddle;
import object.EternalBrick;
import object.Ball;
import object.NormalBrick;
import object.Brick;
import map.*;

public class PlayingProcess {

  enum PlayingState{
    READY, RUNNING, FINISH_MAP, GAME_OVER
  }

  int width, height;
  Paddle paddle;
  Ball ball;
  List<Brick> bricks = new ArrayList<>();
  GraphicsContext gc;
  boolean pressedLeft = false, pressedRight = false;
  private PlayingState playingState;

  public Rectangle map;
  ListOfMap LM = new ListOfMap();
  int currentMap = 0;

  public PlayingProcess(int width, int height, Rectangle map, GraphicsContext gc) {
    this.width = width;
    this.height = height;
    this.gc = gc;
    this.map = map;
    initLevel();
  }

  private void initLevel() {
    paddle = new Paddle(map.getX() + map.getWidth() / 2 - 50,
        map.getY() + map.getHeight() - 40);
    pressedLeft = false;
    pressedRight = false;
    ball = new Ball(map.getWidth() / 2 - 60 + map.getX() + 50 - 8,
        map.getHeight() - 40 + map.getY() - 16);
    bricks.clear();

    double brickW = (map.getWidth() - 60) / 8;
    double brickH = 20;

    int[][] arr = LM.getMapByCode(currentMap);

    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        if(arr[r][c] == 0){
          continue;
        }
        double bx = 30 + c * brickW + map.getX();
        double by = 50 + r * (brickH + 6) + map.getY();
        if(arr[r][c] == 1) {
          bricks.add(new NormalBrick(bx, by, brickW - 6, brickH));
        } else if(arr[r][c] == 2){
          bricks.add(new EternalBrick(bx, by, brickW - 6, brickH));
        }
      }
    }
    playingState = PlayingState.READY;
  }

  public void reset() {
    initLevel();
  }

  public void onBallLost() {
    playingState = PlayingState.GAME_OVER;
    currentMap = 0;
  }
  public void startGame() {
    playingState = PlayingState.RUNNING;
  }

  private void initInput(Scene scene) {
    scene.setOnKeyPressed(e -> {
      KeyCode code = e.getCode();
      switch (code) {
        case ESCAPE:
          System.exit(0);
          break;
        case SPACE:
          if ((playingState).equals(PlayingState.READY)) {
            this.startGame();
          } else if ((playingState).equals(PlayingState.GAME_OVER)) {
            this.reset();
          }
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
      if (e.getCode() == KeyCode.LEFT) pressedLeft = false;
      if (e.getCode() == KeyCode.RIGHT) pressedRight = false;
    });

    if (pressedLeft) {
      paddle.moveLeft();
    } else if (pressedRight) {
      paddle.moveRight();
    } else {
      paddle.stop();
    }
  }

  public void update(Scene scene) {
    initInput(scene);
    switch (playingState) {
      case READY:
        paddle.update(this);
        ball.setY(paddle.getY() - ball.getHeight());
        ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getRadius());
        break;
      case RUNNING:
        paddle.update(this);
        ball.update(this);
        if (ball.checkCollision(paddle) != Ball.BallCollision.NONE) {
          ball.bounceOff(paddle, ball.checkCollision(paddle));
          ball.setY(paddle.getY() - ball.getHeight() - 1);
        }
        checkBricksList();
        break;
    }
  }

  private void checkBricksList() {
    Iterator<Brick> it = bricks.iterator();
    while (it.hasNext()) {
      Brick b = it.next();
      Ball.BallCollision collision = ball.checkCollision(b);
      if (collision != Ball.BallCollision.NONE) {
        b.takeHit();
        ball.bounceOff(b, collision);
        if (b.isDestroyed()) {
          it.remove();
        }
        break;
      }
    }

    int countNormalBrick = 0;
    it = bricks.iterator();
    while (it.hasNext()) {
      Brick b = it.next();
      if (b instanceof NormalBrick) {
        countNormalBrick++;
      }
    }
    if (countNormalBrick <= 0) {
      playingState = PlayingState.FINISH_MAP;
      currentMap++;
      reset();
    }
  }

  public void render() {
    String filePath = "file:src" + File.separator
        + "main" + File.separator
        + "resources" + File.separator
        + "image" + File.separator
        + "purple.png";
    Image background = new Image(filePath);
    gc.drawImage(background, 0,0,width,height);
    gc.setFill(Color.BLACK);
    gc.fillRect(map.getX(), map.getY(), map.getWidth(), map.getHeight());
    for (Brick b : bricks) {
      b.render(gc);
    }
    paddle.render(gc);
    ball.render(gc);
    gc.setFill(Color.WHITE);
    gc.fillText("State:    " + playingState.name() + "    Level:   " + (this.currentMap + 1), 10, 20);
  }
}