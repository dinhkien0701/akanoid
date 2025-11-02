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
    
    enum LevelType{
        CLASSICAL, ULTIMATE_ONE, ULTIMATE_TWO

        // classical : các màn thuộc chế độ classic
        // ultimate_one : màn này , các viên gạch sẽ hạ xuống được và random thêm
        // ultimate_one : màn này , các viên gạch có thể tự do bay so với ultimate_one
    }

    public int points;
    public int frameCount ; // bộ đếm dựa trên fps
    public int minLocateY; // phục vụ xác định vị trí có đủ để sinh map thời gian thực không
    private PlayingState playingState;
    private LevelType levelType = LevelType.CLASSICAL; // tạm thời gắn classical

    public Paddle paddle;
    public boolean pressedLeft, pressedRight;
    public Ball ball;
    private final List<Brick> bricks;
    private final List<PowerUp> listOfPowerUp;

    // Thuộc tính khung hình chứa logic chơi game và kích thước gạch
    public Rectangle map;
    double brickW ; // độ rộng theo phương ngang của gạch
    double brickH ; // Độ rộng theo phương dọc của gạch

    private final ListOfMap LM;
    int currentMap;
    Image background;

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

        // Đặt kích thước bricks

        brickW = (map.getWidth() - 60) / 13;
        brickH = 40;
        int[][] arr = LM.getMapByCode(10);

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
                } else if (arr[r][c] == 8){
                    bricks.add(new BallUpSkillBrick(bx, by, brickW - 6, brickH - 6, r, c));
                }
            }
        }
        playingState = PlayingState.READY;
    }


    public void reset() {
        currentMap = 0; // reset map
        points = 0; // reset điểm
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
        points = 0; // reset điểm sang màn mới
        frameCount = 1; // reset lại bộ đếm frame
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
                    points += 5; // cộng 5 điểm mỗi lần phá hủy brick bất kỳ
                }
                break;
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

    private void selectMoveBrick() {
        Brick[][] arrBrick = new Brick[8][13];
        Random rand = new Random();
        // Nếu không có gạch thì không thể chọn được gạch di chuyển tự do
        if (bricks.isEmpty()) {
            return;
        }
        int[] hasMoveBrick = new  int[8];
        Iterator <Brick> it = bricks.iterator();
        while(it.hasNext()){
            Brick b = it.next();
            double bx = b.getX() -  ( map.getX() + 30); // do lui them 30 so bien
            double by = b.getY() - ( minLocateY + 50 ); // do lui them 50 so bien
            // đưa vào mảng
            arrBrick[(int)Math.round(by/brickH)][(int)Math.round(bx/brickW)] = b;

            if (b.movedist != 0) {
                // nếu đã có ô được chọn tự do di chuyển ở hàng này
                // thì đánh số nó
                hasMoveBrick[(int)Math.round(by/brickH)] = (int)Math.round(bx/brickW) + 1;

                // cộng thêm 1 : để tránh chỉ mục 0 ( do mặc định mảng int[] chứa phần tử 0 )
            }

        }

        for (int i = 0; i < 8; i++) {
            if(hasMoveBrick[i] > 0) {
                // cập nhật lại
                int k_l = 0 ;
                int k_r = 0; // biên trái và phải đối với ô đang xét
                Brick b =  arrBrick[i][hasMoveBrick[i]-1];
                // đếm bên trái
                for( int k = hasMoveBrick[i] - 2; k >= 0; k--) {
                    if (arrBrick[i][k] == null) {
                        k_l++;
                    } else break;

                }
                // đếm bên phải
                for( int k = hasMoveBrick[i] ; k < 13; k++) {
                    if (arrBrick[i][k] == null) {
                        k_r++;
                    } else break;
                }
                b.left = (int)(b.getX() + 30 - k_l * brickW); // biên trái trên màn hình
                b.right = (int)(b.getX() + 30 + k_r * brickW); // biên phải trên màn hình
                // chuyển sang hàng tiếp
                continue;
            }

            int pick = -1; // ô được chọn trên hàng
            int dist =  0; // phạm vi di chuyển

            int l = 0; // khoảng biên trái nếu di chuyển được
            int r = 0; // khoảng cách biên phải nếu di chuyển được

            for (int j = 0; j < 13; j++) {
                if(arrBrick[i][j] == null) {
                    // vị trí rỗng , sang khối tiếp
                    continue;
                }
                // nếu không rỗng đếm số ô mà nó có thể di chuyển
                int k_dist = 1; // bao gồm cả chính nó
                int k_l = 0 ;
                int k_r = 0; // biên trái và phải đối với ô đang xét

                // đếm bên trái
                for( int k = j - 1; k >= 0; k--) {
                    if (arrBrick[i][k] == null) {
                        k_dist ++;
                        k_l++;
                    } else break;

                }
                // đếm bên phải
                for( int k = j + 1; k < 13; k++) {
                    if (arrBrick[i][k] == null) {
                        k_dist ++;
                        k_r++;
                    } else break;
                }

                if (k_dist > dist) {
                    // lấy ô có thể tự do di chuyển tốt nhất đến hiện tại
                    pick = j;
                    dist = k_dist;
                    l = k_l;
                    r = k_r;
                }

            }

            if ( pick != -1 && dist > 1) {
                // thực thi , lấy brick sẽ di chuyển tự do
                Brick b =  arrBrick[i][pick];
                b.movedist = 3*(rand.nextInt(3) - 1); // ta mặc định vận tốc là -3/0/3 khi dịch chuyển trái / phải
                b.left = (int)(b.getX() + 30 - l * brickW); // biên trái trên màn hình
                b.right = (int)(b.getX() + 30 + r * brickW); // biên phải trên màn hình
            }
        }

    }

    private void randomRow () {
        // Sử dụng cho chế độ vô hạn
        if(playingState != PlayingState.RUNNING){
            // tạm thời xem xét với running
            return;
        }
        if (50 + brickH + map.getY() > minLocateY) {
            // khi này nếu không thể chèn gạch thì cũng dừng lại
            return;
        }

        // Hiện tại có thể vẽ
        Random rand = new Random();
        // Nó sẽ lấy một map được tạo từ map đã tạo : 13 -> 18;
        int[][] arr = LM.getMapByCode(rand.nextInt(5) + 7);
        int i = rand.nextInt(arr.length); // lấy một hàng bất kỳ từ map đã nhận

        double by;

        if (bricks.isEmpty()) {
            // Nếu khay đã rỗng thì in lên trên cùng
            by = 50 + map.getY();

        } else {
            by = minLocateY - brickH; // đảm bảo khoảng cách đẹp nhất
            while (by - brickH >= 50 + map.getY()) {
                // Giải làm sao để in ra gạch mới trên cùng nhất
                by -= brickH;
            }
        }
        // dùng lại code của initMap  ( hàm này cũng được viết trong class này )
        for (int j = 0; j < arr[i].length; j++) {
            System.out.print(arr[i][j] + " ");
            if(arr[i][j] == 0){
                continue;
            }
            // map.getX và map.getY để lấy vị trí cục bộ của màn hình chơi

            double bx = 30 + j * brickW + map.getX();


            // có 8 loại gạch hehe
            // brickW - 6  : mỗi viên cách nhau 6 pixel ( ngang)
            // brickH - 6  : mỗi viên gạch cách nhau 6 pixel ( dọc )
            //(bx,by) : vị trí góc trái cùng
            //(w,h) : kích thước
            //(r,c) vị trí trong int map[][]
            if(arr[i][j] == 1) {
                bricks.add(new NormalBrick(bx, by, brickW - 6, brickH - 6, i, j));
            } else if(arr[i][j] == 2){
                bricks.add(new ImmortalBrick(bx, by, brickW - 6, brickH - 6, i, j));
            } else if (arr[i][j] == 3){
                bricks.add(new LifeUpBrick(bx, by, brickW - 6, brickH - 6, i, j));
            } else if (arr[i][j] == 4){
                bricks.add(new GoldBrick(bx, by, brickW - 6, brickH -6, i, j));
            } else if (arr[i][j] == 5){
                bricks.add(new FallBombBrick(bx, by, brickW - 6, brickH - 6, i, j));
            } else if (arr[i][j] == 6){
                bricks.add(new AreaBlastBrick(bx, by, brickW - 6, brickH - 6, i, j));
            } else if (arr[i][j] == 7){
                bricks.add(new LuckyWheelBrick(bx, by, brickW - 6, brickH - 6, i, j));
            } else if (arr[i][j] == 8){
                bricks.add(new BallUpSkillBrick(bx, by, brickW - 6, brickH - 6, i, j));
            }
        }
        System.out.println();
    }

    @Override

    // update và kiểm tra cả va chạm với paddle

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

        if(playingState == PlayingState.RUNNING) {
            frameCount ++; // tăng frame
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

                b.ha_do_cao(5) ;  // giảm độ cao , mỗi 140 fps hay 2 giây , giảm  k pixel
                // đồng thời lấy vị trí gần mép trên nhất
                minLocateY = (int) Math.min(minLocateY , b.getY());

                // sau đó render như bình thường
                b.render(gc);
            }
            randomRow(); // đồng thời gọi random map
            selectMoveBrick();


        } else  if (playingState == PlayingState.RUNNING) {
            // còn nếu chưa đủ 2 giây chưa hạ độ cao
            for (Brick b : bricks) {
                // Dù chưa hạ độ cao nhưng vẫn xử lý dịch trái hay phải mỗi 10 fps
                if ( frameCount % 10 == 0) b.dich_trai_phai();
                b.render(gc);
            }
            
        } else  {
            // đôi khi game tạm dừng do không còn thuộc RUNNING
            // khi đó chỉ in thôi
            for (Brick b : bricks) {
                b.render(gc);
            }
        }
        paddle.render(gc);
        ball.render(gc);
        gc.setFill(Color.WHITE);
        gc.fillText("Points:    " + points + "    Level:   " + (this.currentMap + 1), 10, 20);
        gc.fillText("Lives:    " + paddle.getLives(), 10, 40);

    }
}