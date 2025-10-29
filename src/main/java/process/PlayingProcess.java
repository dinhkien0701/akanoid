package process;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import core.*;
import javafx.stage.Stage;
import object.Paddle;
import object.*;
import object.Ball;
import object.NormalBrick;
import object.Brick;
import map.*;
import powerup.DuplicateBallPowerUp;
import powerup.PowerUp;
import core.Process;

public class PlayingProcess extends Process {

    enum PlayingState{
        READY, RUNNING, FINISH_MAP, GAME_OVER, WINNER
    }

    private PlayingState playingState;

    public Paddle paddle;
    public boolean pressedLeft, pressedRight;
    public Ball ball;
    private final List<Brick> bricks;
    private final List<PowerUp> listOfPowerUp;
    public Rectangle map;
    private final ListOfMap LM;
    int currentMap;
    Image background;

    public PlayingProcess(int width, int height, Rectangle map) {
        super(width, height);
        String filePath = "file:src" + File.separator
                + "main" + File.separator
                + "resources" + File.separator
                + "image" + File.separator
                + "bk5.png";
        background = new Image(filePath);

        this.map = map;
        currentMap = 0;
        LM = new ListOfMap();

        bricks = new ArrayList<>();
        listOfPowerUp = new ArrayList<>();

        paddle = new Paddle(map.getX() + map.getWidth() / 2 - 50, map.getY() + map.getHeight() - 40);
        pressedLeft = false;
        pressedRight = false;
        ball = new Ball(map.getWidth() / 2 - 60 + map.getX() + 50 - 8, map.getHeight() - 40 + map.getY() - 16);

        initBall();
        initPaddle();
        initMap();
    }

    private void initBall() {
        ball.resetSpeed();
        ball.setX(map.getWidth() / 2 - 60 + map.getX() + 50 - 8);
        ball.setY(map.getHeight() - 40 + map.getY() - 16);
    }

    private void initPaddle(){
        paddle.setX(map.getX() + map.getWidth() / 2 - 50);
        paddle.setY(map.getY() + map.getHeight() - 40);
        pressedLeft = false;
        pressedRight = false;
    }

    private void initMap() {
        bricks.clear();
        double brickW = (map.getWidth() - 60) / 13;
        double brickH = 33;

        int[][] arr = LM.getMapByCode(18);

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 13; c++) {
                if(arr[r][c] == 0){
                    continue;
                }
                double bx = 30 + c * brickW + map.getX();
                double by = 50 + r * (brickH + 6) + map.getY();

                // có 8 loại gạch hehe
                // brickW - 6  : mỗi viên cách nhau 6 pixel
                if(arr[r][c] == 1) {
                    bricks.add(new NormalBrick(bx, by, brickW - 6, brickH));
                } else if(arr[r][c] == 2){
                    bricks.add(new ImmortalBrick(bx, by, brickW - 6, brickH));
                } else if (arr[r][c] == 3){
                    bricks.add(new LifeUpBrick(bx, by, brickW - 6, brickH));
                } else if (arr[r][c] == 4){
                    bricks.add(new GoldBrick(bx, by, brickW - 6, brickH));
                } else if (arr[r][c] == 5){
                    bricks.add(new FallBombBrick(bx, by, brickW - 6, brickH));
                } else if (arr[r][c] == 6){
                    bricks.add(new AreaBlastBrick(bx, by, brickW - 6, brickH));
                } else if (arr[r][c] == 7){
                    bricks.add(new LuckyWheelBrick(bx, by, brickW - 6, brickH));
                } else if (arr[r][c] == 8){
                    bricks.add(new BallUpSkillBrick(bx, by, brickW - 6, brickH));
                }
            }
        }
        playingState = PlayingState.READY;
    }


    public void reset() {
        currentMap = 0;
        initMap();
        initPaddle();
        initBall();
        paddle.reborn();
    }

    public void onBallLost() {
        paddle.takeHit();
        initBall();
        listOfPowerUp.clear();
        playingState = PlayingState.READY;
    }

    public void deadPaddle() {
        playingState = PlayingState.GAME_OVER;
    }

    public void startGame() {
        playingState = PlayingState.RUNNING;
    }

    public void nextLevel() {
        currentMap++;
        initMap();
        initPaddle();
        initBall();
        listOfPowerUp.clear();
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
            if (e.getCode() == KeyCode.LEFT) pressedLeft = false;
            if (e.getCode() == KeyCode.RIGHT) pressedRight = false;
        });

        if (pressedLeft) {
            paddle.moveLeft();
        } else if (pressedRight) {
            paddle.moveRight();
        } else {
            paddle.stop();
        }
    }

    public void checkBricksList(Ball ball) {
        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick b = it.next();
            Ball.BallCollision collision = ball.checkCollision(b);
            if (collision != Ball.BallCollision.NONE) {
                b.takeHit();
                if(b instanceof NormalBrick){
                    listOfPowerUp.add(new DuplicateBallPowerUp(b.getX(),b.getY()));
                }
                ball.bounceOff(b, collision);
                if (b.isDestroyed()) {
                    it.remove();
                }
                break;
            }
        }
        int countNormalBrick = 0;
        it = bricks.iterator();
        while (it.hasNext()) {
            Brick b = it.next();
            if (b instanceof NormalBrick) {
                countNormalBrick++;
            }
        }
        if (countNormalBrick <= 0) {
            playingState = PlayingState.FINISH_MAP;
            nextLevel();
        }
    }

    public void addPowerUp(PowerUp pu){
        listOfPowerUp.add(pu);
    }

    public void checkPowerUpList() {
        if(listOfPowerUp.isEmpty()){
            return;
        }
        Iterator<PowerUp> it = listOfPowerUp.iterator();
        while(it.hasNext()){
            PowerUp pu = it.next();
            pu.update(this);
            if(pu.isEnd()) {
                it.remove();
                break;
            }
        }
    }



    @Override
    public void update(Stage stage,GameManager gm) {
        initInput();
        switch (playingState) {
            case READY:
                paddle.update(this);
                ball.setY(paddle.getY() - ball.getHeight());
                ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getRadius());
                break;
            case RUNNING:
                paddle.update(this);
                ball.update(this);
                if (ball.checkCollision(paddle) != Ball.BallCollision.NONE) {
                    ball.bounceOff(paddle, ball.checkCollision(paddle));
                    ball.setY(paddle.getY() - ball.getHeight() - 1);
                }
                checkBricksList(this.ball);
                checkPowerUpList();
                break;
            case GAME_OVER:
                gm.finishPlay(stage);
        }
    }

    @Override
    public void render() {
        gc.drawImage(background, 0,0,width,height);
        gc.save();
        gc.setGlobalAlpha(0.5);
        gc.setFill(Color.BLACK);
        gc.fillRect(map.getX(), map.getY(), map.getWidth(), map.getHeight());
        gc.restore();

        for(PowerUp p : listOfPowerUp) {
            p.render(gc);
        }

        for (Brick b : bricks) {
            b.render(gc);
        }
        paddle.render(gc);
        ball.render(gc);
        gc.setFill(Color.WHITE);
        gc.fillText("State:    " + playingState.name() + "    Level:   " + (this.currentMap + 1), 10, 20);
        gc.fillText("Lives:    " + paddle.getLives(), 10, 40);
    }
}