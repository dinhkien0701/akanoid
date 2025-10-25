package powerup;

import core.GameObject;
import core.MovableObject;
import core.MovableObject;
import object.Paddle;
import process.PlayingProcess;

public abstract class PowerUp extends MovableObject {
  private static final double FALLING_SPEED = 3.0;

  protected PowerUp(double x, double y, double width, double height) {
    super(x, y, width, height, 0, FALLING_SPEED);
  }

  public boolean checkCollision(GameObject other) {
    if (!(other instanceof Paddle)) {
      return false;
    }
    return getX() < other.getX() + other.getWidth() && getX() + getWidth() > other.getX()
        && getY() < other.getY() + other.getHeight() && getY() + getHeight() > other.getY();
  }

  @Override
  public void resetSpeed() {
    super.setDx(0);
    super.setDy(FALLING_SPEED);
  }

  @Override
  public void update(PlayingProcess pp) {
    move();
  }

  public abstract void applyEffect(PlayingProcess pp);
}

