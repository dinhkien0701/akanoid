package object;


import core.GameObject;

public abstract class Brick extends GameObject {

  protected int hitPoints;

  // thêm hai thuộc tính lưu vị trí trong mảng
  public int locateX;
  public int locateY;

  // thêm hai thuộc tính để xác định khoảng có thể di chuyển trong khoảng [l,r]
    public int left;
    public int right;
    public int movedist; // khoảng cách mỗi lần dịch trái phải

  Brick(double x, double y, double w, double h, int locateX, int locateY, int hp) {
    super(x, y, w, h);
    this.hitPoints = hp;
    this.locateX = locateX;
    this.locateY = locateY;
    this.movedist = 0;
    right = (int) x;
    left  = (int) x;

  }

  public void ha_do_cao ( int k) {
      y += k;
  }

  public void dich_trai_phai() {
      if (movedist == 0 || left == right) {
          return;
      }

      if( x + movedist > right || x + movedist < left) {
          movedist = - movedist;
      }
      x += movedist;
  }

  public double getY() {
      return y;
  }

  public double  getX() {
      return x;
  }

  public abstract void takeHit();

  public void upHitPoint() {
      // Sẽ được override trong ImmortalBrick
  }

  public boolean isDestroyed() {
    return hitPoints <= 0;
  }
}