package object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import core.MovableObject;
import process.PlayingProcess;

public class Paddle extends MovableObject {
  private static final double PADDLE_SPEED = 10.0;
  private int lives;

  private double speed = 10.0;
  static double paddleWidth = 100;
  static double paddleHeight = 16;


  public Paddle(double x, double y) {
    super(x, y, paddleWidth, paddleHeight, 0, 0);
    lives = 3;
  }

  public void takeHit(){
    lives--;
  }

  public void setPaddleWidth(double w) {
    this.setWidth(w);
  }
  public void setPaddleHeight(double h) {
    this.setHeight(h);
  }

  public void setSpeed(double s) {
    this.speed = s;
  }

  public void moveLeft() {
    //System.out.println("Left");
    dx = -speed;
  }

  public void moveRight() {
    //System.out.println("Right");
    dx = speed;
  }

  public void reborn(){
    this.lives = 3;
  }

  public void stop() {
    dx = 0;
  }

  @Override
  public void update(PlayingProcess gm) {
    move();
    if (getX() < gm.map.getX()) {
      this.setX(gm.map.getX());
    }
    if (getX() + getWidth() > gm.map.getWidth() + gm.map.getX()) {
      this.setX(gm.map.getWidth() + gm.map.getX() - this.getWidth());
    }
    if(lives <= 0){
      gm.deadPaddle();
    }
  }

  @Override
  public void render(GraphicsContext gc) {
    gc.setFill(Color.YELLOW);
    gc.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
  }
}