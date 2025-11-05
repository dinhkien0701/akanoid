package gameobject.ball;

import java.util.Deque;
import java.util.LinkedList;

import gameobject.GameObject;
import gameobject.MovableObject;
import gameobject.paddle.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


import process.PlayingProcess;

public class Ball extends MovableObject {

    private static final double BALL_RADIUS = 10.0;
    private static final double BALL_SPEED = 5.0;

    private double speed;
    private double r;

    private final Deque<Position> previousPosition = new LinkedList<>();

    // private boolean justBounced = false;

    public Ball(double x, double y) {
        super(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2, BALL_SPEED / 2, -BALL_SPEED);
        previousPosition.clear();
        this.setRadius(BALL_RADIUS);
    }


    public void reset() {
        resetSpeed();
        resetSize();
        previousPosition.clear();
    }

    public void resetSize() {
        setRadius(BALL_RADIUS);
    }

    @Override
    public void resetSpeed() {
        super.setDx(BALL_SPEED / 2);
        super.setDy(-BALL_SPEED);
    }

    public double getRadius() {
        return this.r;
    }

    public void setRadius(double r) {
        this.r = r;
        this.setWidth(r * 2);
        this.setHeight(r * 2);
    }

    public void bounceOff(GameObject other, BallCollision collision) {
        if (collision == BallCollision.CORNER) {
            bounceOffCorner();
        } else if (collision == BallCollision.VERTICAL) {
            bounceOffVertical();
        } else if (collision == BallCollision.HORIZONTAL) {

            if (other instanceof Paddle) {
                Paddle paddle = (Paddle) other;

                double ballCenterX = this.getX() + this.r;
                double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
                double normalizedHitX = (ballCenterX - paddleCenterX) / (paddle.getWidth() / 2);
                normalizedHitX = Math.max(-1.0, Math.min(1.0, normalizedHitX));
                double maxBounceAngle = Math.toRadians(75);
                double bounceAngle = normalizedHitX * maxBounceAngle;
                double currentSpeed = Math.sqrt(dx * dx + dy * dy);
                double newSpeed = currentSpeed * 1.05;
                dx = newSpeed * Math.sin(bounceAngle);
                dy = -newSpeed * Math.cos(bounceAngle);
                dx += paddle.getDx() * 0.3;
            } else {
                bounceOffHorizontal();
            }

        }
        double maxSpeed = 7.0;
        double speed = Math.sqrt(dx * dx + dy * dy);
        if (speed > maxSpeed) {
            dx = dx / speed * maxSpeed;
            dy = dy / speed * maxSpeed;
        }
        double randomFactor = 1.0 + (Math.random() - 0.5) * 0.05;
        dx *= randomFactor;
        dy *= randomFactor;

        if (dx == 0) {
            dx += dy / 2;
            dy -= dy / 2;
        } else if (dy == 0) {
            dy += dx / 3;
            dx -= dx / 3;
        }
    }

    private void bounceOffCorner() {
        dx = -dx * 1.05;
        dy = -dy * 1.05;
    }

    private void bounceOffVertical() {
        dx = -dx * 1.05;
    }

    private void bounceOffHorizontal() {
        dy = -dy * 1.05;
    }

    public BallCollision checkCollision(GameObject other) {

        double cX = this.getX() + this.r;
        double cY = this.getY() + this.r;

        double left = other.getX();
        double right = other.getX() + other.getWidth();
        double top = other.getY();
        double bottom = other.getY() + other.getHeight();

        double nearestX = Math.max(left, Math.min(cX, right));
        double nearestY = Math.max(top, Math.min(cY, bottom));

        double distX = cX - nearestX;
        double distY = cY - nearestY;

        if (distX * distX + distY * distY <= this.r * this.r) {

            boolean hitLeftOrRight = (nearestX == left || nearestX == right);
            boolean hitTopOrBottom = (nearestY == top || nearestY == bottom);

            if (hitLeftOrRight && hitTopOrBottom)
                return BallCollision.CORNER;
            else if (hitLeftOrRight)
                return BallCollision.VERTICAL;
            else if (hitTopOrBottom)
                return BallCollision.HORIZONTAL;
        }
        return BallCollision.NONE;
    }

    private void savePosition() {
        previousPosition.addFirst(new Position(this.getX(), this.getY()));
        if (previousPosition.size() >= 60) {
            previousPosition.removeLast();
        }
    }

    @Override
    public void update(PlayingProcess gm) {
        move();
        if (this.getX() < gm.getMap().getX()) {
            this.setX(gm.getMap().getX());
            bounceOffVertical();
        }
        if (this.getX() + this.getWidth() > gm.getMap().getWidth() + gm.getMap().getX()) {
            this.setX(gm.getMap().getWidth() + gm.getMap().getX() - this.getWidth());
            bounceOffVertical();
        }
        if (this.getY() < 0) {
            this.setY(0);
            bounceOffHorizontal();
        }
        double bottom = gm.getMap().getY() + gm.getMap().getHeight();
        if (isFallOut(bottom)) {
            gm.onBallLost(this);
        }
        this.savePosition();
    }

    public boolean isFallOut(double bottom) {
        return this.getY() > bottom;
    }

    private void drawEffect(GraphicsContext gc) {
        double i = 0;
        gc.setFill(Color.WHITE);
        for (Position ball : previousPosition) {
            gc.setFill(Color.PINK);
            gc.fillOval(ball.x(), ball.y(), this.getWidth() - i, this.getHeight() - i);
            i = i + 0.4;
            if (this.getWidth() < 0) {
                this.setWidth(0);
            }
            if (this.getHeight() < 0) {
                this.setHeight(0);
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        // this.drawEffect(gc);
        gc.setFill(Color.PINK);
        gc.fillOval(this.getX(), this.getY(), this.getHeight(), this.getWidth());
    }

    public enum BallCollision {
        NONE, HORIZONTAL, VERTICAL, CORNER
    };
}