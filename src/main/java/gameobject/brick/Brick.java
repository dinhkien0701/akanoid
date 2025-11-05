package gameobject.brick;


import gameobject.GameObject;


public abstract class Brick extends GameObject {
    public static final double BRICK_WIDTH = 64;
    public static final double BRICK_HEIGHT = 40;
  protected int hitPoints;

  // thêm hai thuộc tính lưu vị trí trong mảng
  public int locateX;
  public int locateY;

  // thêm hai thuộc tính để xác định khoảng có thể di chuyển trong khoảng [l,r]
    public int left;
    public int right;
    public int movedist; // khoảng cách mỗi lần dịch trái phải

  Brick(double x, double y, int locateX, int locateY, int hp) {
    super(x, y, BRICK_WIDTH - 6, BRICK_HEIGHT - 6);
    this.hitPoints = hp;
    this.locateX = locateX;
    this.locateY = locateY;
    this.movedist = 0;
    right = (int) x;
    left  = (int) x;

  }

  public void ha_do_cao ( int k) {
      setY(getY() + k);
  }

  public void dich_trai_phai() {
      if (movedist == 0 || left == right) {
          return;
      }

      if( getX() + movedist > right || getX() + movedist < left) {
          movedist = - movedist;
      }
      setX(getX() + movedist);
  }

  public abstract void takeHit();

  public void upHitPoint() {
      // Sẽ được override trong ImmortalBrick
  }

  public boolean isDestroyed() {
    return hitPoints <= 0;
  }
}