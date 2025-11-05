package process;

import gamemanager.GameManager;
import gameobject.ball.Ball;
import gameobject.brick.*;
import gameobject.paddle.Paddle;
import gameobject.powerup.FallBoomPowerUp;
import gameobject.powerup.LifeUpPowerUp;
import gameobject.powerup.PowerUp;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import map.ListOfMap;
import map.Map;
import javafx.scene.canvas.Canvas;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PlayingProcess extends Process {

    private PlayingState playingState;
    private final Paddle paddle;
    private boolean pressedLeft, pressedRight;
    int currentLevel;
    private Ball mainBall;


    private final List<PowerUp> listOfPowerUp;
    private final List<Ball> listOfBall;
    private final List<Ball> deletedBall;
    private final Rectangle map;
    private final ListOfMap LM;
    private int powerUpsSpawnedThisLevel;
    private Map mainMap;
    Image background;

    public PlayingProcess(int width, int height, Rectangle map) {
        super(width, height);
        Canvas canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        this.pane.getChildren().add(canvas);
        String filePath = "file:src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "image" + File.separator + "bk5.png";
        background = new Image(filePath);

        this.map = map;
        currentLevel = 1;
        LM = new ListOfMap();

        listOfPowerUp = new ArrayList<>();
        listOfBall = new ArrayList<>();

        paddle = new Paddle(map.getX() + map.getWidth() / 2 - 50,
                map.getY() + map.getHeight() - 40);
        pressedLeft = false;
        pressedRight = false;
        deletedBall = new ArrayList<>();
        mainBall = new Ball(map.getWidth() / 2 - 60 + map.getX() + 50 - 8,
                map.getHeight() - 40 + map.getY() - 16);
        powerUpsSpawnedThisLevel = 0;
        listOfBall.add(mainBall);
        initBall();
        initPaddle();
        playingState = PlayingState.READY;
    }

    public boolean isEnoughPowerUpsSpawnedThisLevel() {
        return this.powerUpsSpawnedThisLevel >= PowerUp.MAX_POWER_UP_PER_LEVEL;
    }

    public Rectangle getMap() {
        return this.map;
    }

    public ListOfMap getLM() {
        return this.LM;
    }

    public List<Brick> getBricks() {
        return mainMap.getBricks();
    }

    public List<Ball> getListOfBall() {
        return this.listOfBall;
    }

    public Paddle getPaddle() {
        return this.paddle;
    }

    public PlayingState getPlayingState() {
        return this.playingState;
    }


    private void initBall() {
        mainBall.reset();
        mainBall.setX(map.getWidth() / 2 - 60 + map.getX() + 50 - 8);
        mainBall.setY(map.getHeight() - 40 + map.getY() - 16);
    }

    private void initPaddle() {
        paddle.setX(map.getX() + map.getWidth() / 2 - 50);
        paddle.setY(map.getY() + map.getHeight() - 40);
        pressedLeft = false;
        pressedRight = false;
    }

    public void setCurrentLevel(int level) {
        currentLevel = level;
        mainMap = LM.getMapByCode(level - 1);

        if (mainMap == null) {
            System.err.println("Lỗi: Không tải được map cho level " + level);
            return;
        }
        listOfBall.clear();
        mainBall = new Ball(0, 0);
        listOfBall.add(mainBall);

        initPaddle();
        listOfPowerUp.clear();
        powerUpsSpawnedThisLevel = 0;

        playingState = PlayingState.READY;
    }

    public void deadPaddle() {
        playingState = PlayingState.GAME_OVER;
    }

    public void startGame() {
        playingState = PlayingState.RUNNING;
    }

    public void addPoints(int points) {
        mainMap.addPoints(points);
    }

    public void nextLevel() {
        currentLevel++;
        powerUpsSpawnedThisLevel = 0;
        mainMap = LM.getMapByCode(currentLevel - 1);
        initPaddle();
        initBall();
        listOfPowerUp.clear();
    }

    public void reset() {
        paddle.reborn();
        setCurrentLevel(this.currentLevel);
    }



    public void pauseGame() {
        for (PowerUp powerUp : listOfPowerUp) {
            powerUp.stop();
        }
        for (Ball ball : listOfBall) {
            ball.stop();
        }
        pressedRight = false;
        pressedLeft = false;
        playingState = PlayingState.PAUSE;
    }

    public void resumeGame() {
        for (PowerUp powerUp : listOfPowerUp) {
            powerUp.startFromStop();
        }
        for (Ball ball : listOfBall) {
            ball.startFromStop();
        }
        playingState = PlayingState.RUNNING;
    }

    private void initInput() {
        this.scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            switch (code) {
                case ESCAPE:
                    if (playingState == PlayingState.RUNNING
                            || playingState == PlayingState.READY) {
                        this.pauseGame();
                    } else if (playingState == PlayingState.PAUSE) {
                        this.resumeGame();
                    }
                    break;
                case SPACE:
                    if ((playingState).equals(PlayingState.READY)) {
                        this.startGame();
                    }
                    break;
                case LEFT:
                    pressedLeft = playingState != PlayingState.PAUSE;
                    break;
                case RIGHT:
                    pressedRight = playingState != PlayingState.PAUSE;
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

    public void addPowerUp(PowerUp pu) {
        listOfPowerUp.add(pu);
        if (!(pu instanceof LifeUpPowerUp) && !(pu instanceof FallBoomPowerUp)) {
            powerUpsSpawnedThisLevel++;
        }
    }

    public void onBallLost(Ball falledBall) {
        if (falledBall.equals(mainBall)) {
            if (listOfBall.size() - 1 <= 0) {
                paddle.takeHit();
                initBall();
                playingState = PlayingState.READY;
                listOfPowerUp.clear();
                return;
            }
            for (Ball ball : listOfBall) {
                if (ball.equals(mainBall)) {
                    continue;
                }
                if (!ball.isFallOut(map.getY() + map.getHeight())) {
                    mainBall = ball;
                    break;
                }
            }
        }
        deletedBall.add(falledBall);
    }

    public void checkPowerUpList() {
        if (listOfPowerUp.isEmpty()) {
            return;
        }
        Iterator<PowerUp> it = listOfPowerUp.iterator();
        while (it.hasNext()) {
            PowerUp pu = it.next();
            pu.update(this);
            if (pu.isFallOut() && pu.isEnd()) {
                it.remove();
                break;
            }
        }
    }

    public void checkBallList() {
        if (listOfBall.isEmpty()) {
            return;
        }
        for (Ball b : listOfBall) {
            b.update(this);
            if (b.checkCollision(paddle) != Ball.BallCollision.NONE) {
                b.bounceOff(paddle, b.checkCollision(paddle));
                b.setY(paddle.getY() - b.getHeight() - 1);
            }
            mainMap.checkBricksList(this, b);
        }
        if (!deletedBall.isEmpty()) {
            for (Ball b : deletedBall) {
                listOfBall.remove(b);
            }
            deletedBall.clear();
        }
    }

    @Override
    public void update(Stage stage, GameManager gm) {
        initInput();
        switch (playingState) {
            case READY:
                paddle.update(this);
                mainBall.setY(paddle.getY() - mainBall.getHeight());
                mainBall.setX(paddle.getX() + paddle.getWidth() / 2 - mainBall.getRadius());
                break;
            case RUNNING:
            case PAUSE:
                paddle.update(this);
                mainMap.update(this);
                checkBallList();
                checkPowerUpList();
                if (mainMap.isWin()) {
                    playingState = PlayingState.GAME_OVER;
                }
                break;
            case GAME_OVER:
                gm.LeadToGameOver(stage);
                break;
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

        mainMap.render(gc);
        for (PowerUp p : listOfPowerUp) {
            p.render(gc);
        }

        paddle.render(gc);
        // mainBall.render(gc);
        for (Ball ball : listOfBall) {
            ball.render(gc);
        }

        gc.setFill(Color.WHITE);
        gc.fillText("Level:   " + (this.currentLevel + 1), 10, 20);
        gc.fillText("Lives:   " + paddle.getLives(), 10, 40);
        gc.fillText("Score:   " + mainMap.getPoints(), 10, 60);
    }

    public enum PlayingState {
        READY, RUNNING, FINISH_MAP, GAME_OVER, WINNER, PAUSE
    }

}
