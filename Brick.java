class Brick extends GameObject {
  int hitPoints;

  Brick(double x, double y, double w, double h, int hp) {
    super(x, y, w, h);
    this.hitPoints = hp;
  }

  void takeHit() {
    hitPoints--;
  }

  boolean isDestroyed() {
    return hitPoints <= 0;
  }
}