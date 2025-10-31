package object;


import core.GameObject;

public abstract class Brick extends GameObject {

  protected int hitPoints;
  Brick(double x, double y, double w, double h, int hp) {
    super(x, y, w, h);
    this.hitPoints = hp;
  }

  public void ha_do_cao ( int k) {
      y += k;
  }

  public abstract void takeHit();

  public void upHitPoint() {
      // Sẽ được override trong ImmortalBrick
  }

  public boolean isDestroyed() {
    return hitPoints <= 0;
  }
}