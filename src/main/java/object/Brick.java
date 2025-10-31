package object;


import core.GameObject;

public abstract class Brick extends GameObject {

  protected int hitPoints;

  // thêm hai thuộc tính lưu vị trí trong mảng
  public int locateX;
  public int locateY;

  Brick(double x, double y, double w, double h, int locateX, int locateY, int hp) {
    super(x, y, w, h);
    this.hitPoints = hp;
    this.locateX = locateX;
    this.locateY = locateY;
  }

  public void ha_do_cao ( int k) {
      y += k;
  }

  public double getY() {
      return y;
  }

  public abstract void takeHit();

  public void upHitPoint() {
      // Sẽ được override trong ImmortalBrick
  }

  public boolean isDestroyed() {
    return hitPoints <= 0;
  }
}