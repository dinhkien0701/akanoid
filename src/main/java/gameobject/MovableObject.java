package gameobject;

public abstract class MovableObject extends GameObject {
  protected double dx, dy;

  protected MovableObject(double x, double y, double width, double height, double dx, double dy) {
    super(x, y, width, height);
    this.dx = dx;
    this.dy = dy;
  }

  public void stop(){
    dx = 0;
    dy = 0;
  }

  public double getDx(){
    return this.dx;
  }

  public double getDy(){
    return this.dy;
  }

  public void setDx(double dx) {
    this.dx = dx;
  }

  public void setDy(double dy) {
    this.dy = dy;
  }

  public abstract void resetSpeed();

  protected void move() {
    this.setX(this.getX() + dx);
    this.setY(this.getY() + dy);
  }
}