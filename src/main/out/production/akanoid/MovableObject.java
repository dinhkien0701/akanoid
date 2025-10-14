package core;

public abstract class MovableObject extends GameObject {
  protected double dx, dy;

  protected MovableObject(double x, double y, double width, double height, double dx, double dy) {
    super(x, y, width, height);
    this.dx = dx;
    this.dy = dy;
  }

  public double getDx(){
    return this.dx;
  }

  public double getDy(){
    return this.dy;
  }

  protected void move() {
    x += dx;
    y += dy;
  }
}