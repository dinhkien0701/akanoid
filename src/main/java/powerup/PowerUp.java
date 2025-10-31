package powerup;

import core.GameObject;
import core.MovableObject;
import object.Paddle;
import process.PlayingProcess;

import java.util.Objects;

public abstract class PowerUp extends MovableObject {
  public static final double FALLING_SPEED = 3.0;
  private POWERUPSTATE powerupstate;
  private long timer;

  public PowerUp(double x, double y, double width, double height) {
    super(x, y, width, height, 0, FALLING_SPEED);
    timer = System.currentTimeMillis();
    powerupstate = POWERUPSTATE.FALLING;
  }

  public long getTimer() {
      return this.timer;
  }

  public void setTimer(long timer) {
      this.timer = timer;
  }

  public boolean isFallOut() {
      return this.powerupstate == POWERUPSTATE.FALLOUT;
  }

  public boolean isEnd() {
      return this.powerupstate == POWERUPSTATE.END;
  }

  public boolean isApplying() {
    return this.powerupstate == POWERUPSTATE.APPLYING;
  }

  public void setIsEnd() {
      this.powerupstate = POWERUPSTATE.END;
  }

  public void setPowerupstate(POWERUPSTATE powerupstate) {
      this.powerupstate = powerupstate;
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
      if (isFalling()) {
          move();
          if (getY() > pp.getMap().getY() + pp.getMap().getHeight()) {
              powerupstate = POWERUPSTATE.FALLOUT;
          } else if (checkCollision(pp.getPaddle())) {
              applyEffect(pp);
              setTimer(System.currentTimeMillis());
              powerupstate = POWERUPSTATE.APPLYING;
          }
      }
  }


    public boolean isFalling() {
      return powerupstate == POWERUPSTATE.FALLING;
    }


    public abstract void applyEffect(PlayingProcess pp);

  public enum POWERUPSTATE {
      FALLING,
      FALLOUT,
      APPLYING,
      END
  };
}

