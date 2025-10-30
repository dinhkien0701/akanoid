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

  @Override
  public void resetSpeed(){
    super.setDx(0);
    super.setDy(0);
  }

  public void takeHit(){
    lives--;
  }

  public int getLives(){
    return this.lives;
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
    this.setDx(-speed);
  }

  public void moveRight() {
    //System.out.println("Right");
    this.setDx(speed);
  }

  public void reborn(){
    this.lives = 3;
  }

  public void stop() {
    this.setDx(0);
  }

  @Override
  public void update(PlayingProcess gm) {
    move();
    if (getX() < gm.getMap().getX()) {
      this.setX(gm.getMap().getX());
    }
    if (getX() + getWidth() > gm.getMap().getWidth() + gm.getMap().getX()) {
      this.setX(gm.getMap().getWidth() + gm.getMap().getX() - this.getWidth());
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