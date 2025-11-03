package process;


import gamemanager.GameManager;
import gameobject.ball.Ball;
import gameobject.brick.*;
import gameobject.paddle.Paddle;
import gameobject.powerup.PowerUp;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import map.ListOfMap;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PlayingProcess extends Process {

    private static final int MAX_POWERUPS_PER_LEVEL = 10;

    private PlayingState playingState;
    private final Paddle paddle;
    private boolean pressedLeft, pressedRight;

    private Ball mainBall;

    private final List<Brick> bricks;
    private final List<PowerUp> listOfPowerUp;
    private final List<Ball> listOfBall;
    private final List<Ball> deletedBall;
    private final Rectangle map;
    private final ListOfMap LM;
    private int powerUpsSpawnedThisLevel;
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
        currentMap = 18;
        LM = new ListOfMap();

        bricks = new ArrayList<>();
        listOfPowerUp = new ArrayList<>();
        listOfBall = new ArrayList<>();
        paddle = new Paddle(map.getX() + map.getWidth() / 2 - 50, map.getY() + map.getHeight() - 40);
        pressedLeft = false;
        pressedRight = false;
        deletedBall = new ArrayList<>();
        mainBall = new Ball(map.getWidth() / 2 - 60 + map.getX() + 50 - 8, map.getHeight() - 40 + map.getY() - 16);
        powerUpsSpawnedThisLevel = 0;
        listOfBall.add(mainBall);
        initBall();
        initPaddle();
        initMap();
    }

    public int getPowerUpsSpawnedThisLevel() {
        return this.powerUpsSpawnedThisLevel;
    }

    public boolean isEnoughPowerUpsSpawnedThisLevel() {
        return this.powerUpsSpawnedThisLevel >= MAX_POWERUPS_PER_LEVEL;
    }

    public List<Brick> getBricks() {
        return this.bricks;
    }

    public Rectangle getMap() {
        return this.map;
    }

    public ListOfMap getLM() {
        return this.LM;
    }

    public List<PowerUp> getListOfPowerUp() {
        return this.listOfPowerUp;
    }

    public List<Ball> getListOfBall() {
        return this.listOfBall;
    }

    public Paddle getPaddle() {
        return this.paddle;
    }

    public Ball getMainBall() {
        return this.mainBall;
    }

    private void initBall() {
        mainBall.reset();
        mainBall.setX(map.getWidth() / 2 - 60 + map.getX() + 50 - 8);
        mainBall.setY(map.getHeight() - 40 + map.getY() - 16);
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

        int[][] arr = LM.getMapByCode(currentMap);

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 13; c++) {
                if(arr[r][c] == 0){
                    continue;
                }
                double bx = 30 + c * brickW + map.getX();
                double by = 50 + r * (brickH + 6) + map.getY();

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
//                if(arr[r][c] == 2){
//                    bricks.add(new ImmortalBrick(bx, by, brickW - 6, brickH));
//                } else if(arr[r][c] == 1) {
//                    bricks.add(new NormalBrick(bx, by, brickW - 6, brickH));
//                } else {
//                    bricks.add(new LuckyWheelBrick(bx, by, brickW - 6, brickH));
//                }
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

    public void deadPaddle() {
        playingState = PlayingState.GAME_OVER;
    }

    public void startGame() {
        playingState = PlayingState.RUNNING;
    }

    public void nextLevel() {
        currentMap++;
        powerUpsSpawnedThisLevel = 0;
        initMap();
        initPaddle();
        initBall();
        listOfPowerUp.clear();
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
                if(playingState == PlayingState.RUNNING ||  playingState == PlayingState.READY) {
                    this.pauseGame();
                } else if(playingState == PlayingState.PAUSE) {
                    this.resumeGame();
                }
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
                if(playingState == PlayingState.PAUSE) {
                    pressedLeft = false;
                }
                break;
            case RIGHT:
                pressedRight = true;
                if(playingState == PlayingState.PAUSE) {
                    pressedRight = false;
                }
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
                ball.bounceOff(b, collision);
                if (b.isDestroyed()) {
                    b.update(this);
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
        powerUpsSpawnedThisLevel++;
    }

    public void removePowerUp(PowerUp pu){
        listOfPowerUp.remove(pu);
    }

    public void onBallLost(Ball falledBall) {
        if(falledBall.equals(mainBall)) {
            if(listOfBall.size() - 1 <= 0) {
                paddle.takeHit();
                initBall();
                playingState = PlayingState.READY;
                listOfPowerUp.clear();
                return;
            }
            for(Ball ball : listOfBall) {
                if(ball.equals(mainBall)) {
                    continue;
                }
                if(!ball.isFallOut(map.getY() + map.getHeight())) {
                    mainBall = ball;
                    break;
                }
            }
        }
        deletedBall.add(falledBall);
    }

    public void checkPowerUpList() {
        if(listOfPowerUp.isEmpty()){
            return;
        }
        Iterator<PowerUp> it = listOfPowerUp.iterator();
        while(it.hasNext()){
            PowerUp pu = it.next();
            pu.update(this);
            if(pu.isFallOut() && pu.isEnd()) {
                it.remove();
                break;
            }
        }
    }

    public void checkBallList() {
        if(listOfBall.isEmpty()){
            return;
        }
        for (Ball b : listOfBall) {
            b.update(this);
            if (b.checkCollision(paddle) != Ball.BallCollision.NONE) {
                b.bounceOff(paddle, b.checkCollision(paddle));
                b.setY(paddle.getY() - b.getHeight() - 1);
            }
            checkBricksList(b);
        }
        if(!deletedBall.isEmpty()) {
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
        case RUNNING: case PAUSE:
            paddle.update(this);
            checkBallList();
            checkPowerUpList();
            break;
        case GAME_OVER:
            gm.LeadToGameOver(stage);
            break;
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
        //mainBall.render(gc);
        for (Ball ball : listOfBall) {
            ball.render(gc);
        }
        gc.setFill(Color.WHITE);
        gc.fillText("Level:   " + (this.currentMap + 1), 10, 20);
        gc.fillText("Lives:   " + paddle.getLives(), 10, 40);
    }

    enum PlayingState{
        READY, RUNNING, FINISH_MAP, GAME_OVER, WINNER, PAUSE
    }

}