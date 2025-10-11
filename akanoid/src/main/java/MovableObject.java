abstract class MovableObject extends GameObject {
  double dx, dy;

  protected MovableObject(double x, double y, double width, double height, double dx, double dy) {
    super(x, y, width, height);
    this.dx = dx;
    this.dy = dy;
  }

  protected void move() {
    x += dx;
    y += dy;
  }
}