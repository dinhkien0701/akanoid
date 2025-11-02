package gameobject;

import java.util.Objects;

import javafx.scene.image.Image;
import process.PlayingProcess;

public abstract class GameObject {
  private double x, y, width, height;
  private Image image;
  private int frame;

  public GameObject (double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.frame = 0;
  }

  public GameObject (double x, double y, double width, double height, int frame) {
    this(x , y , width , height);
    this.frame = frame;
  }

  public void LoadImage(String path) {
      image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
  }

  public Image getImage() {
      return this.image;
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
