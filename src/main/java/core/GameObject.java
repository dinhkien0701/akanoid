package core;

import java.awt.Image;
import process.PlayingProcess;

public abstract class GameObject {
  protected double x, y, width, height;
  private Image image;
  private int frame;

  protected GameObject(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.frame = 0;
  }

  protected GameObject(double x, double y, double width, double height, int frame) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.frame = frame;
  }

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  public double getWidth() {
    return this.width;
  }

  public double getHeight() {
    return this.height;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void setY(double y) {
    this.y = y;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public void setHeight(double height) {
    this.height = height;
  }

  public abstract void update(PlayingProcess gm);

  public abstract void render(javafx.scene.canvas.GraphicsContext gc);
}
