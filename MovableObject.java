class MovableObject extends GameObject {
  double dx = 0, dy = 0;

  MovableObject(double x, double y, double width, double height) {
    super(x, y, width, height);
  }

  void move() {
    x += dx;
    y += dy;
  }
}