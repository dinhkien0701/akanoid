package process;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import process.PauseMenu;
import gamemanager.GameManager;
import gameobject.ball.Ball;
import gameobject.brick.*;
import gameobject.paddle.Paddle;
import gameobject.powerup.FallBoomPowerUp;
import gameobject.powerup.LifeUpPowerUp;
import gameobject.powerup.PowerUp;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import javafx.stage.Stage;

import map.*;


public class PlayingProcess extends Process {

    public enum PlayingState{
        READY, RUNNING, FINISH_MAP, GAME_OVER, WINNER, PAUSE
    }

    enum LevelType{
        CLASSICAL_ONE, CLASSICAL_TWO, CLASSICAL_THREE, CLASSICAL_FOUR,
        ULTIMATE_ONE, ULTIMATE_TWO

        // classical_one : các màn thuộc level 1 - > 3 : dễ
        // classical_two : các màn thuộc level 4  -> 6 : bình thường
        // classical_three : các màn thuộc level 7 ->9 : hơi khó
        // classical_four : các màn thuộc level 10 -> 13 : khó
        // ultimate_one : màn này , các viên gạch sẽ hạ xuống được và random thêm
        // ultimate_one : màn này , các viên gạch có thể tự do bay so với ultimate_one và có thể đẩy gạch
    }


    public int points;
    public int frameCount ; // bộ đếm dựa trên fps
    public int minLocateY; // phục vụ xác định vị trí có đủ để sinh map thời gian thực không
    private PlayingState playingState;
    private LevelType levelType = LevelType.CLASSICAL_ONE; // tạm thời gắn classical

    private Paddle paddle;
    private boolean pressedLeft, pressedRight;
    private Ball mainball;
    private final List<Brick> bricks;
    private final List<PowerUp> listOfPowerUp;
    private final List<Ball> listOfBall;
    private final List<Ball> deletedBall;
    private Ball mainBall;
    private PauseMenu pauseMenu;

    // Thuộc tính khung hình chứa logic chơi game và kích thước gạch
    private Rectangle map;
    double brickW ; // độ rộng theo phương ngang của gạch
    double brickH ; // Độ rộng theo phương dọc của gạch

    private final ListOfMap LM;
    protected int currentMap;
    protected int randRowCount;
    Image background;
    private int numPowerUpSpawnThisLevel = 0;

    public List<Pair<Double,Double>> pairList = new  ArrayList<>();

    public PlayingProcess(int width, int height, Rectangle map) {

        super(width, height);

        points = 0; // khởi tạo mới

        String filePath = "file:src" + File.separator
                + "main" + File.separator
                + "resources" + File.separator
                + "image" + File.separator
                + "bk5.png";
        background = new Image(filePath);

        this.map = map;
        LM = new ListOfMap();

        bricks = new ArrayList<>();
        listOfPowerUp = new ArrayList<>();
        listOfBall = new ArrayList<>();

        paddle = new Paddle(map.getX() + map.getWidth() / 2 - 50, map.getY() + map.getHeight() - 40);
        pressedLeft = false;
        pressedRight = false;
        pressedLeft = false;
        pressedRight = false;
        deletedBall = new ArrayList<>();
        mainBall = new Ball(map.getWidth() / 2 - 60 + map.getX() + 50 - 8,
                map.getHeight() - 40 + map.getY() - 16);
        listOfBall.add(mainBall);
        this.numPowerUpSpawnThisLevel = 0;
        initBall();
        initPaddle();
    }

    public void addPause(GameManager gm, Stage stage) {
        pauseMenu = new PauseMenu(
                () -> this.resumeGame(),           // Resume
                () -> gm.LeadToPickLevel(stage),   // Replay
                () -> System.exit(0)               // Quit
        );
        pane.getChildren().add(pauseMenu);
    }

    public void setCurrentMap( int level) {
        points = 0; // tính điểm lại cho level mới
        randRowCount = 0; // số lượng hàng sẽ vẽ thêm
        currentMap = level; // lưu lại level
        // cài độ khó ứng với level
        if (level <= 3){
            levelType = LevelType.CLASSICAL_ONE;
        }  else if (level <= 6){
            levelType = LevelType.CLASSICAL_TWO;
        }   else if (level <= 9){
            levelType =  LevelType.CLASSICAL_THREE;
        } else if (level <= 13){
            levelType = LevelType.CLASSICAL_FOUR;
            randRowCount = level - 9;
            if (level == 13) randRowCount += 5;
        } else if (level == 14){
            levelType = LevelType.ULTIMATE_ONE;
            randRowCount = 1000000;
        } else {
            levelType = LevelType.ULTIMATE_TWO;
            randRowCount = 1000000;
        }
        initMap();
    }

    public void addPoints(int points){
        this.points += points;
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

        // Đặt kích thước bricks

        brickW = (map.getWidth() - 60) / 13;
        brickH = 40;
        // lấy map ứng với level ( currentMap)
        System.out.println(this.currentMap);
        int[][] arr = LM.getMapByCode(this.currentMap);
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 13; c++) {
                if(arr[r][c] == 0){
                    continue;
                }
                // map.getX và map.getY để lấy vị trí cục bộ của màn hình chơi

                double bx = 30 + c * brickW + map.getX();
                double by = 50 + r * brickH + map.getY();

                // có 8 loại gạch hehe
                // brickW - 6  : mỗi viên cách nhau 6 pixel ( ngang)
                // brickH - 6  : mỗi viên gạch cách nhau 6 pixel ( dọc )
                //(bx,by) : vị trí góc trái cùng
                //(w,h) : kích thước
                //(r,c) vị trí trong int map[][]
                if(arr[r][c] == 1) {
                    bricks.add(new NormalBrick(bx, by, brickW - 6, brickH - 6, r, c));
                } else if(arr[r][c] == 2){
                    bricks.add(new ImmortalBrick(bx, by, brickW - 6, brickH - 6, r, c));
                } else if (arr[r][c] == 3){
                    bricks.add(new LifeUpBrick(bx, by, brickW - 6, brickH - 6, r, c));
                } else if (arr[r][c] == 4){
                    bricks.add(new GoldBrick(bx, by, brickW - 6, brickH - 6, r, c));
                } else if (arr[r][c] == 5){
                    bricks.add(new FallBombBrick(bx, by, brickW - 6, brickH - 6, r, c));
                } else if (arr[r][c] == 6){
                    bricks.add(new AreaBlastBrick(bx, by, brickW - 6, brickH - 6, r, c));
                } else if (arr[r][c] == 7){
                    bricks.add(new LuckyWheelBrick(bx, by, brickW - 6, brickH- 6, r, c));
                } else if (arr[r][c] == 8 && levelType != LevelType.ULTIMATE_TWO){
                    bricks.add(new PushBrick(bx, by, brickW - 6, brickH - 6, r, c));
                }
            }
        }
        playingState = PlayingState.READY;
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

    public boolean isEnoughPowerUpsSpawnedThisLevel(){
        return this.numPowerUpSpawnThisLevel >= PowerUp.MAX_POWER_UP_PER_LEVEL;
    }

    public void reset() {
        points = 0;
        setCurrentMap(currentMap);
        pairList = new  ArrayList<>();
        initPaddle();
        initBall();
        paddle.reborn();
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

    public void deadPaddle() {
        playingState = PlayingState.GAME_OVER;
    }

    public void startGame() {
        playingState = PlayingState.RUNNING;
    }

    public void nextLevel() {
        currentMap++;
        points = 0; // reset điểm sang màn mới
        frameCount = 1; // reset lại bộ đếm frame
        frameBlast = 1;
        setCurrentMap(currentMap);
        pairList  = new  ArrayList<>();
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
                        pauseMenu.show();  // hiển thị PauseMenu
                    } else if(playingState == PlayingState.PAUSE) {
                        this.resumeGame();
                        pauseMenu.hide();  // ẩn PauseMenu
                    }
                    break;
                case SPACE:
                    if (playingState == PlayingState.READY) {
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


    int frameBlast = 1;

    public void checkBallList() {
        if (listOfBall.isEmpty()) {
            return;
        }
        for (Ball ball : listOfBall) {
            ball.update(this);
            if (ball.checkCollision(paddle) != Ball.BallCollision.NONE) {
                // nếu bóng chạm paddle
                points = Math.max(points - 1, 0); // mỗi lần chạm paddle trừ một điểm , min >= 0
                Iterator <Brick> it = bricks.iterator();
                while (it.hasNext()) {
                    Brick b = it.next();

                    // tăng hitpoint của gạch bất tử
                    if (b instanceof ImmortalBrick) {
                        b.upHitPoint();
                    }
                }
                ball.bounceOff(paddle, ball.checkCollision(paddle));
                ball.setY(paddle.getY() - ball.getHeight() - 1);
            }
            checkBricksList(ball);
        }
        if (!deletedBall.isEmpty()) {
            for (Ball b : deletedBall) {
                listOfBall.remove(b);
            }
            deletedBall.clear();
        }
    }


    public void checkBricksList(Ball ball) {
        boolean pushBrick = false; // kiểm tra xem có đẩy brick lên không
        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick b = it.next();
            Ball.BallCollision collision = ball.checkCollision(b);
            if (b.isDestroyed()) {
                if (b instanceof PushBrick) {
                    pushBrick = true;
                }
                if (b instanceof AreaBlastBrick) {
                    pairList.add(new Pair<Double,Double>(b.getX(),b.getY()));
                }
                it.remove();
                points += 5;
            }
            else if (b.getY() < map.getY() + 50) {
                continue; // bóng không thể chạm brick không trong màn chơi
            }
            else if (collision != Ball.BallCollision.NONE) {
                b.takeHit();
                ball.bounceOff(b, collision);
                if (b.isDestroyed()) {
                    if (b instanceof PushBrick) {
                        pushBrick = true;
                    } else if (b instanceof AreaBlastBrick) {
                        pairList.add(new Pair<Double,Double>(b.getX(),b.getY()));
                    } else {
                        b.update(this);
                    }

                    it.remove();
                    points += 5; // cộng 5 điểm mỗi lần phá hủy brick bất kỳ
                }
                break;
            }
        }
        // Neu pushBrick bi pha huy
        if (pushBrick && (currentMap >= 7 && levelType != LevelType.ULTIMATE_TWO) ) {
            // chỉ áp dụng từ level 7 -> level 14 ( ultimate 1 )
            // level 15 không khả dụng
            SpecialBrickEffect.doPushBrick(bricks, map, brickH);
        }

        if (playingState == PlayingState.RUNNING){
            frameBlast = SpecialBrickEffect.areaBlast(bricks, pairList, brickW, brickH, frameBlast);
        }

        for(Brick b : bricks){
            if (b.getY() + b.getHeight() >= map.getY() + map.getHeight()) {
                deadPaddle();
            }
        }

        // Nếu đã phá hủy toàn bộ brick , sang level tiếp theo.
        int countBrick = bricks.size();
        if (countBrick <= 0) {
            playingState = PlayingState.FINISH_MAP;
            nextLevel();
        }
    }

    public void addPowerUp(PowerUp pu){
        listOfPowerUp.add(pu);
        if(!(pu instanceof LifeUpPowerUp) && !(pu instanceof FallBoomPowerUp)){
            this.numPowerUpSpawnThisLevel++;
        }
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

    int help = 0; // biến trợ giúp

    @Override

    // update và kiểm tra cả va chạm với paddle

    public void update(Stage stage, GameManager gm) {
        initInput();
        switch (playingState) {
            case READY:
                paddle.update(this);
                mainBall.setY(paddle.getY() - mainBall.getHeight());
                mainBall.setX(paddle.getX() + paddle.getWidth() / 2 - mainBall.getRadius());
                break;
            case RUNNING:
                paddle.update(this);
                checkBallList();
                checkPowerUpList();
                break;
            case GAME_OVER:
                gm.LeadToGameOver(stage);
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

        if(playingState == PlayingState.RUNNING) {
            frameCount ++; // tăng frame
            frameBlast ++;
            if (frameCount == 1401) {
                // reset framecount sau mỗi chu kỳ 20 giây
                frameCount = 1;

            }
        }
        for(PowerUp p : listOfPowerUp) {
            p.render(gc);
        }

        if (frameCount % 140 == 0 && playingState == PlayingState.RUNNING) {
            minLocateY = 2000; // đặt măcj định là một số tương đối lớn
            for (Brick b : bricks) {

                // tính năng hạ map từ level 7
                if (currentMap > 6)b.ha_do_cao(5) ;  // giảm độ cao , mỗi 140 fps hay 2 giây , giảm  k pixel
                // đồng thời lấy vị trí gần mép trên nhất
                minLocateY = (int) Math.min(minLocateY , b.getY());

                // sau đó render như bình thường
                if (b.getY() >= map.getY() + 50 )b.render(gc);
            }

            if ( currentMap > 9 && randRowCount > 0) {
                // khi level từ 10 trở đi render thêm hàng
                SpecialBrickEffect.RandomRowResult rr = SpecialBrickEffect.randomRow(
                        bricks,
                        map,
                        brickW,
                        brickH,
                        minLocateY,
                        currentMap,
                        randRowCount,
                        LM,
                        playingState == PlayingState.RUNNING,
                        levelType != LevelType.ULTIMATE_TWO,
                        help
                );
                minLocateY = rr.minLocateY;
                randRowCount = rr.randRowCount;
                help = rr.help;
            }
            // lấy lại độ cao
            for (Brick b : bricks) {
                minLocateY = (int) Math.min(minLocateY , b.getY());
            }
            if (currentMap >= 4) {
                // các viên gạch có thể tự do di chuyển từ level 4
                SpecialBrickEffect.selectMoveBrick(bricks, map, minLocateY, brickW, brickH);
            }

        } else  if (playingState == PlayingState.RUNNING) {
            // còn nếu chưa đủ 2 giây chưa hạ độ cao
            for (Brick b : bricks) {
                // Dù chưa hạ độ cao nhưng vẫn xử lý dịch trái hay phải mỗi 10 fps
                if ( frameCount % 10 == 0) b.dich_trai_phai();
                if (b.getY() >= map.getY() + 50 )b.render(gc);
            }

        } else  {
            // đôi khi game tạm dừng do không còn thuộc RUNNING
            // khi đó chỉ in thôi
            for (Brick b : bricks) {
                if (b.getY() >= map.getY() + 50 )b.render(gc);
            }
        }
        paddle.render(gc);
        for(Ball ball : listOfBall) {
            ball.render(gc);
        }
        gc.setFill(Color.WHITE);
        gc.fillText("Points:    " + points + "    Level:   " + (currentMap ), 10, 20);
        gc.fillText("Lives:    " + paddle.getLives(), 10, 40);

    }
}
