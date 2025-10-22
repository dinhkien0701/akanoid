package powerup;

import core.MovableObject;
import object.Paddle;
import process.PlayingProcess;

public abstract class PowerUp extends MovableObject {
  private static final double FALLING_SPEED = 5.0;
  protected boolean isApply;

  protected PowerUp(double x, double y, double width, double height) {
    super(x, y, width, height, 0, FALLING_SPEED);
    isApply = false;
  }

  protected boolean checkCollisionPaddle(Paddle p) {
    return (this.x + this.width >= p.getX() && this.x <= p.getX() + p.getWidth()
    && this.y + this.width >= p.getY());
  }

  @Override
  public void resetSpeed(){
    super.setDx(0);
    super.setDy(FALLING_SPEED);
  }

  public abstract boolean isEnd();
  public abstract void applyEffect(PlayingProcess pp);
}