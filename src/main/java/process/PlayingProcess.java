package process;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import core.*;
import javafx.stage.Stage;
import object.Paddle;
import object.EternalBrick;
import object.Ball;
import object.NormalBrick;
import object.Brick;
import map.*;
import core.Process;
import powerup.*;
import object.Bullet;



public class PlayingProcess extends Process {

    enum PlayingState {
        READY, RUNNING, FINISH_MAP, GAME_OVER
    }

    public enum EffectType {
        BIGGER_BALL, LONGER_PADDLE, SHOT
    }

    private PlayingState playingState;
    private final GameManager gameManager;

    public Paddle paddle;
    private final List<Ball> balls;
    private final List<Brick> bricks;
    private final List<PowerUp> listOfPowerUp;
    private final List<TimedEffect> timedEffects;
    private final List<Bullet> bullets;

    private static final double POWERUP_SPAWN_CHANCE = 1;
    private static final int MAX_POWERUPS_PER_LEVEL = 100;
    private int powerUpsSpawnedThisLevel;
    private final Random random = new Random();
    private long lastShotTime = 0;
    private static final long SHOT_COOLDOWN = 300;

    public boolean pressedLeft, pressedRight;
    public Rectangle map;
    private final ListOfMap LM;
    int currentMap;
    Image background;

    public PlayingProcess(int width, int height, Rectangle map, GameManager gameManager) {
        super(width, height);
        this.map = map;
        this.LM = new ListOfMap();
        this.gameManager = gameManager;
        this.bricks = new ArrayList<>();
        this.balls = new ArrayList<>();
        this.listOfPowerUp = new ArrayList<>();
        this.paddle = new Paddle(0, 0);
        this.timedEffects = new ArrayList<>();
        this.bullets = new ArrayList<>();

        String filePath = "file:src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "image" + File.separator + "purple.png";
        background = new Image(filePath);

        reset();
    }

    public List<Ball> getBalls() {
        return this.balls;
    }

    public void addBall(Ball newBall) {
        this.balls.add(newBall);
    }

    public void reset() {
        this.currentMap = 0;
        this.paddle.reborn();
        initLevel();
    }

    private void initLevel() {
        this.powerUpsSpawnedThisLevel = 0;
        this.listOfPowerUp.clear();
        this.bullets.clear();
        this.timedEffects.clear();
        paddle.resetSize();
        paddle.disableShooting();

        initPaddle();
        initBall();
        initMap();

        this.playingState = PlayingState.READY;
    }

    private void initBall() {
        balls.clear();
        Ball initialBall = new Ball(0, 0);
        balls.add(initialBall);
    }

    private void initPaddle() {
        paddle.setX(map.getX() + map.getWidth() / 2 - paddle.getWidth() / 2);
        paddle.setY(map.getY() + map.getHeight() - 40);
        paddle.resetSpeed();
    }

    private void initMap() {
        bricks.clear();
        double brickW = (map.getWidth() - 60) / 8;
        double brickH = 20;

        int[][] arr = LM.getMapByCode(currentMap);

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (arr[r][c] == 0) {
                    continue;
                }
                double bx = 30 + c * brickW + map.getX();
                double by = 50 + r * (brickH + 6) + map.getY();
                if (arr[r][c] % 3 == 1) {
                    bricks.add(new NormalBrick(bx, by, brickW - 6, brickH));
                } else if (arr[r][c] % 3 == 2) {
                    bricks.add(new EternalBrick(bx, by, brickW - 6, brickH));
                }
            }
        }
    }

    public void resetAfterLifeLost() {
        this.listOfPowerUp.clear();
        initPaddle();
        initBall();
        this.playingState = PlayingState.READY;
    }

    public void onBallLost(Ball lostBall) {
        balls.remove(lostBall);
    }

    public void deadPaddle() {
        playingState = PlayingState.GAME_OVER;
    }

    public void startGame() {
        if (playingState == PlayingState.READY) {
            playingState = PlayingState.RUNNING;
        }
    }

    public void nextLevel() {
        currentMap++;
        initLevel();
    }

    private void spawnRandomPowerUp(double x, double y) {
        int powerUpType = random.nextInt(7);
        // int powerUpType = 6;
        switch (powerUpType) {
            case 0:
                listOfPowerUp.add(new DuplicateBallPowerUp(x, y));
                break;
            case 1:
                listOfPowerUp.add(new BiggerBallPowerUp(x, y));
                break;
            case 2:
                listOfPowerUp.add(new LongerPaddlePowerUp(x, y));
                break;
            case 3:
                listOfPowerUp.add(new ShortenPaddlePowerUp(x, y));
                break;
            case 4:
                listOfPowerUp.add(new FallBoomPowerUp(x, y));
                break;
            case 5:
                listOfPowerUp.add(new LifeUpPowerUp(x, y));
                break;
            case 6:
                listOfPowerUp.add(new ShotPowerUp(x, y));
        }
        powerUpsSpawnedThisLevel++;
    }

    private void updateTimedEffects() {
        Iterator<TimedEffect> it = timedEffects.iterator();
        while (it.hasNext()) {
            TimedEffect effect = it.next();
            if (effect.isFinished()) {
                effect.execute();
                it.remove();
            }
        }
    }

    private void checkCollisions() {
        for (Ball b : new ArrayList<>(balls)) {
            if (b.checkCollision(paddle) != Ball.BallCollision.NONE) {
                b.bounceOff(paddle, b.checkCollision(paddle));
                b.setY(paddle.getY() - b.getHeight() - 1);
            }
            Iterator<Brick> it = bricks.iterator();
            while (it.hasNext()) {
                Brick brick = it.next();
                if (b.checkCollision(brick) != Ball.BallCollision.NONE) {
                    b.bounceOff(brick, b.checkCollision(brick));
                    brick.takeHit();
                    if (brick.isDestroyed()) {
                        if (powerUpsSpawnedThisLevel < MAX_POWERUPS_PER_LEVEL
                                && random.nextDouble() < POWERUP_SPAWN_CHANCE) {
                            spawnRandomPowerUp(brick.getX(), brick.getY());
                        }
                        it.remove();
                    }
                    break;
                }
            }
        }

        Iterator<PowerUp> pu_it = listOfPowerUp.iterator();
        while (pu_it.hasNext()) {
            PowerUp pu = pu_it.next();
            if (pu.checkCollision(paddle)) {
                pu.applyEffect(this);
                pu_it.remove();
            }
        }

        Iterator<Bullet> bullet_it = bullets.iterator();
        while (bullet_it.hasNext()) {
            Bullet bullet = bullet_it.next();
            boolean bulletHit = false;

            Iterator<Brick> brick_it = bricks.iterator();
            while (brick_it.hasNext()) {
                Brick brick = brick_it.next();
                if (bullet.getX() < brick.getX() + brick.getWidth()
                        && bullet.getX() + bullet.getWidth() > brick.getX()
                        && bullet.getY() < brick.getY() + brick.getHeight()
                        && bullet.getY() + bullet.getHeight() > brick.getY()) {
                    brick.takeHit();
                    if (brick.isDestroyed()) {
                        brick_it.remove();
                    }

                    bulletHit = true;
                    break;
                }
            }
            if (bulletHit || bullet.getY() < 0) {
                bullet_it.remove();
            }
        }
    }

    private static class TimedEffect {
        EffectType type;
        long endTime;
        Runnable onEndAction;

        TimedEffect(EffectType type, int durationSeconds, Runnable onEndAction) {
            this.type = type;
            this.endTime = System.currentTimeMillis() + durationSeconds * 1000L;
            this.onEndAction = onEndAction;
        }

        boolean isFinished() {
            return System.currentTimeMillis() >= endTime;
        }

        void execute() {
            onEndAction.run();
        }
    }

    public void addTimedEffect(EffectType type, int durationSeconds, Runnable onEndAction) {
        timedEffects.removeIf(effect -> effect.type == type);
        timedEffects.add(new TimedEffect(type, durationSeconds, onEndAction));
    }

    private void autoShooting() {
        if (paddle.canShoot()) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastShotTime) > SHOT_COOLDOWN) {
                bullets.add(new Bullet(paddle.getX(), paddle.getY()));
                bullets.add(new Bullet(paddle.getX() + paddle.getWidth() - Bullet.BULLET_WIDTH,
                        paddle.getY()));
                lastShotTime = currentTime;
            }
        }
    }


    private void initInput() {
        this.scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            switch (code) {
                case ESCAPE:
                    System.exit(0);
                    break;
                case SPACE:
                    if ((playingState).equals(PlayingState.READY)) {
                        this.startGame();
                    } else if ((playingState).equals(PlayingState.GAME_OVER)) {
                        this.reset();
                    }
                    break;
                case LEFT:
                    pressedLeft = true;
                    break;
                case RIGHT:
                    pressedRight = true;
                    break;
            }
        });

        this.scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT)
                pressedLeft = false;
            if (e.getCode() == KeyCode.RIGHT)
                pressedRight = false;
        });

        if (pressedLeft) {
            paddle.moveLeft();
        } else if (pressedRight) {
            paddle.moveRight();
        } else {
            paddle.stop();
        }
    }

    @Override
    public void setScene(Stage stage) {
        stage.setScene(this.scene);
    }

    @Override
    public void update(Stage stage, GameManager gm) {
        initInput();
        if (pressedLeft) {
            paddle.moveLeft();
        } else if (pressedRight) {
            paddle.moveRight();
        } else {
            paddle.stop();
        }
        paddle.update(this);

        if (playingState == PlayingState.READY) {
            if (!balls.isEmpty()) {
                balls.get(0).setY(paddle.getY() - balls.get(0).getHeight());
                balls.get(0).setX(paddle.getX() + paddle.getWidth() / 2 - balls.get(0).getRadius());
            }
        }

        if (playingState == PlayingState.RUNNING) {
            for (Ball b : new ArrayList<>(balls)) {
                b.update(this);
            }
            for (PowerUp pu : listOfPowerUp) {
                pu.update(this);
            }
            autoShooting();
            for (Bullet b : bullets) {
                b.update(this);
            }
            updateTimedEffects();
            checkCollisions();
            if (balls.isEmpty()) {
                paddle.takeHit();
                resetAfterLifeLost();
            }
        }

        if (bricks.isEmpty()) {
            playingState = PlayingState.FINISH_MAP;
            nextLevel();
        }
        if (playingState == PlayingState.GAME_OVER) {
            gm.finishPlay(stage);
        }
    }

    @Override
    public void render() {
        gc.drawImage(background, 0, 0, width, height);
        gc.save();
        gc.setGlobalAlpha(0.5);
        gc.setFill(Color.BLACK);
        gc.fillRect(map.getX(), map.getY(), map.getWidth(), map.getHeight());
        gc.restore();

        for (Brick b : bricks) {
            b.render(gc);
        }
        for (PowerUp p : listOfPowerUp) {
            p.render(gc);
        }
        paddle.render(gc);
        for (Ball b : balls) {
            b.render(gc);
        }
        for (Bullet b : bullets) {
            b.render(gc);
        }
        gc.setFill(Color.WHITE);
        gc.fillText("State:    " + playingState.name() + "    Level:   " + (this.currentMap + 1),
                10, 20);
        gc.fillText("Lives:    " + paddle.getLives(), 10, 40);
    }
}
