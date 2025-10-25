package object;


import core.GameObject;

public abstract class Brick extends GameObject {
  private int hitPoints;

  Brick(double x, double y, double w, double h, int hp) {
    super(x, y, w, h);
    this.hitPoints = hp;
  }

  public void takeHit() {
    hitPoints--;
  }

  public boolean isDestroyed() {
    return hitPoints <= 0;
  }
}
