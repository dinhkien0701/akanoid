package map;

import gameobject.GameObject;
import gameobject.ball.Ball;
import gameobject.brick.*;
import gameobject.brick.LuckyWheelBrick;
import gameobject.brick.NormalBrick;
import javafx.scene.canvas.GraphicsContext;
import process.PlayingProcess;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Map extends GameObject {

    public static final int MAP_ROWS = 8;
    public static final int MAP_COLUMNS = 13;
    public static Rectangle MAP_ZONE = new Rectangle(150, 0, 900, 700);

    private final int[][] map;
    private boolean mapBeaten;
    private final List<Brick> bricks;
    private LevelType levelType;
    private int randRowCount;
    private int frameCount;
    private int points;
    private int minLocateY;
    private final int level;

    public Map(int[][] map, int level) {
        super(MAP_ZONE.getX() , MAP_ZONE.getY(), MAP_ZONE.getWidth(), MAP_ZONE.getHeight());
        this.map = map;
        this.level = level;
        mapBeaten = false;
        points = 0;
        bricks = new ArrayList<>();
        this.setCurrentMap(level);
        this.LoadImage("/image/Map_1.png");
        initMap();
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public int[][] getMap() {
        return this.map;
    }

    public void initMap() {
        bricks.clear();
        for (int r = 0; r < MAP_ROWS; r++) {
            for (int c = 0; c < MAP_COLUMNS; c++) {
                if(map[r][c] == 0){
                    continue;
                }
                // map.getX và map.getY để lấy vị trí cục bộ của màn hình chơi

                double bx = 30 + c * Brick.BRICK_WIDTH + MAP_ZONE.getX();
                double by = 50 + r * Brick.BRICK_HEIGHT + MAP_ZONE.getY();

                // có 8 loại gạch hehe
                // brickW - 6  : mỗi viên cách nhau 6 pixel ( ngang)
                // brickH - 6  : mỗi viên gạch cách nhau 6 pixel ( dọc )
                //(bx,by) : vị trí góc trái cùng
                //(w,h) : kích thước
                //(r,c) vị trí trong int map[][]
                if(map[r][c] == 1) {
                    bricks.add(new NormalBrick(bx, by, r, c));
                } else if(map[r][c] == 2){
                    bricks.add(new ImmortalBrick(bx, by, r, c));
                } else if (map[r][c] == 3){
                    bricks.add(new LifeUpBrick(bx, by,  r, c));
                } else if (map[r][c] == 4){
                    bricks.add(new GoldBrick(bx, by, r, c));
                } else if (map[r][c] == 5){
                    bricks.add(new FallBombBrick(bx, by,  r, c));
                } else if (map[r][c] == 6){
                    bricks.add(new AreaBlastBrick(bx, by,  r, c));
                } else if (map[r][c] == 7){
                    bricks.add(new LuckyWheelBrick(bx, by, r, c));
                } else if (map[r][c] == 8 && (this.levelType != LevelType.ULTIMATE_TWO)){
                    bricks.add(new PushBrick(bx, by,  r, c));
                }
            }
        }
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return points;
    }

    public void checkBricksList(PlayingProcess pp,Ball ball) {
        //boolean pushBrick = false; // kiểm tra xem có đẩy brick lên không
        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick b = it.next();
            Ball.BallCollision collision = ball.checkCollision(b);
            if (collision != Ball.BallCollision.NONE) {
                b.takeHit();
                ball.bounceOff(b, collision);
                if (b.isDestroyed()) {
                    b.update(pp);
                    it.remove();
                    points += 5; // cộng 5 điểm mỗi lần phá hủy brick bất kỳ
                }
                break;
            }
        }
//        // Neu pushBrick bi pha huy
//        if (pushBrick && (currentMap >= 7 && levelType != LevelType.ULTIMATE_TWO) ) {
//            // chỉ áp dụng từ level 7 -> level 14 ( ultimate 1 )
//            // level 15 không khả dụng
//            doPushBrick();
//        }
//
//        if (playingState == PlayingProcess.PlayingState.RUNNING){
//            areaBlast();
//        }

        // Nếu đã phá hủy toàn bộ brick , sang level tiếp theo.
        int countBrick = bricks.size();
        if (countBrick == 0) {
            setWin();
        }
    }

//    int frameBlast = 1;
//    private void areaBlast() {
//        // Nếu không có gạch thì không thể chọn nổ xung quanh được
//        if (bricks.isEmpty() || frameBlast < 8) {
//            return;
//        }
//
//        frameBlast = 1;
//        List<Pair<Double,Double>> np = new ArrayList<>();
//        // Lấy minLocateY
//        double dist = brickH * brickH + brickW * brickW ;
//        Iterator<Pair<Double,Double>> it = pairList.iterator();
//        while (it.hasNext()) {
//            Pair<Double,Double> p = it.next();
//            double x = p.first;
//            double y = p.second;
//
//            for (Brick b : bricks) {
//
//                if (b.isDestroyed()) {
//                    continue;
//                }
//
//                double k_dist = (x - b.getX()) * (x - b.getX()) + (y - b.getY()) * ( y- b.getY());
//                if (k_dist <= dist) {
//                    b.takeHit();
//                    if (b.isDestroyed()) {
//                        np.add(new Pair<Double,Double>(b.getX(),b.getY()));
//                    }
//                }
//            }
//            it.remove();
//        }
//        pairList.addAll(np);
//
//    }

    public boolean isWin() {
        return mapBeaten;
    }

    public void setWin() {
        this.mapBeaten = true;
    }

    @Override
    public void update(PlayingProcess pp) {
        if(pp.getPlayingState() == PlayingProcess.PlayingState.RUNNING) {
            frameCount ++; // tăng frame
            if (frameCount == 1401) {
                // reset framecount sau mỗi chu kỳ 20 giây
                frameCount = 1;
            }
        }
        if (frameCount % 140 == 0 && pp.getPlayingState() == PlayingProcess.PlayingState.RUNNING) {
            minLocateY = 2000; // đặt măcj định là một số tương đối lớn
            for (Brick b : bricks) {

                // tính năng hạ map từ level 7
                if (this.level > 6)b.ha_do_cao(5) ;  // giảm độ cao , mỗi 140 fps hay 2 giây , giảm  k pixel
                // đồng thời lấy vị trí gần mép trên nhất
                minLocateY = (int) Math.min(minLocateY , b.getY());
            }

            if (this.level > 9 && randRowCount > 0) {
                // khi level từ 10 trở đi render thêm hàng
                randomRow(pp); // đồng thời gọi random map
            }
            // lấy lại độ cao
            for (Brick b : bricks) {
                minLocateY = (int) Math.min(minLocateY , b.getY());
            }
            if (this.level >= 4) {
                // các viên gạch có thể tự do di chuyển từ level 4
                selectMoveBrick();
            }

        } else  if (pp.getPlayingState() == PlayingProcess.PlayingState.RUNNING) {
            // còn nếu chưa đủ 2 giây chưa hạ độ cao
            for (Brick b : bricks) {
                // Dù chưa hạ độ cao nhưng vẫn xử lý dịch trái hay phải mỗi 10 fps
                if (frameCount % 10 == 0) b.dich_trai_phai();
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.save();
        gc.drawImage(this.getImage(), MAP_ZONE.getX(), MAP_ZONE.y, MAP_ZONE.getWidth(), MAP_ZONE.getHeight());
        for (Brick b : bricks) {
            if (b.getY() >= Map.MAP_ZONE.getY() + 50 ){
                b.render(gc);
            }
        }
        gc.restore();
    }

    public void setCurrentMap( int level) {
        points = 0; // tính điểm lại cho level mới
        mapBeaten = false;
        randRowCount = 0;
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

    private void selectMoveBrick() {
        Brick[][] arrBrick = new Brick[8][13];
        Random rand = new Random();
        // Nếu không có gạch thì không thể chọn được gạch di chuyển tự do
        if (bricks.isEmpty()) {
            return;
        }
        int[] hasMoveBrick = new  int[8];
        Iterator<Brick> it = bricks.iterator();
        while(it.hasNext()){
            Brick b = it.next();
            double bx = b.getX() -  ( Map.MAP_ZONE.getX() + 30); // do lui them 30 so bien
            double by = b.getY() - ( minLocateY );
            // đưa vào mảng
            arrBrick[(int)Math.round(by/Brick.BRICK_HEIGHT)][(int)Math.round(bx/Brick.BRICK_WIDTH)] = b;

            if (b.movedist != 0) {
                // nếu đã có ô được chọn tự do di chuyển ở hàng này
                // thì đánh số nó
                hasMoveBrick[(int)Math.round(by/Brick.BRICK_HEIGHT)] = (int)Math.round(bx/Brick.BRICK_WIDTH) + 1;

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
                b.left = (int)(MAP_ZONE.getX() + 30 + (hasMoveBrick[i] -1 - k_l) * Brick.BRICK_WIDTH); // biên trái trên màn hình
                b.right = (int)(MAP_ZONE.getX() + 30 + (hasMoveBrick[i] -1 + k_r) * Brick.BRICK_WIDTH); // biên phải trên màn hình
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
                b.left = (int)(Map.MAP_ZONE.getX() + 30 + (pick - l) * (Brick.BRICK_HEIGHT)); // biên trái trên màn hình
                b.right = (int)(Map.MAP_ZONE.getX() + 30 + (pick + r) * (Brick.BRICK_WIDTH)); // biên phải trên màn hình
            }
        }
    }

    int help = 0;
    private void randomRow (PlayingProcess pp) {
        // Sử dụng cho chế độ vô hạn
        if(pp.getPlayingState() != PlayingProcess.PlayingState.RUNNING){
            // tạm thời xem xét với running
            return;
        }

        if (50 + Brick.BRICK_HEIGHT + Map.MAP_ZONE.getY() > minLocateY) {
            // khi này nếu không thể chèn gạch thì cũng dừng lại
            return;
        }

        if (this.level >= 14 ) {
            randRowCount = 1000000;
            // làm cho random vô tận với ultimate 1 & 2
        } else {
            randRowCount --; // các level từ 9 - > 14 đều có giới hạn random
        }

        // Hiện tại có thể vẽ
        Random rand = new Random();
        // Nó sẽ lấy một map được tạo từ map đã tạo : 13 -> 18;
        int[][] arr = pp.getLM().getMapByCode(rand.nextInt(5) + 7).getMap();
        int i = rand.nextInt(arr.length); // lấy một hàng bất kỳ từ map đã nhận

        double by;

        if (bricks.isEmpty()) {
            // Nếu khay đã rỗng thì in lên trên cùng
            by = 50 + Map.MAP_ZONE.getY();

        } else {
            by = minLocateY - Brick.BRICK_HEIGHT; // đảm bảo khoảng cách đẹp nhất
            while (by - Brick.BRICK_HEIGHT >= 50 + MAP_ZONE.getY()) {
                // Giải làm sao để in ra gạch mới trên cùng nhất
                by -= Brick.BRICK_HEIGHT;
            }
        }
        // Để giảm độ khó mỗi một hàng đẩy xuống sẽ đảm bảo có 1 trong hai trợ giúp lifeUp và pushBrick
        if (help == 0){
            // thêm máu
            arr[i][rand.nextInt(13)] = 3;
            help++;
        } else {
            // thêm đẩy lên pushBrick
            arr[i][rand.nextInt(13)] = 8;
            help = 1; // reset lại biến
        }
        // dùng lại code của initMap  ( hàm này cũng được viết trong class này )
        for (int j = 0; j < arr[i].length; j++) {
            System.out.print(arr[i][j] + " ");
            if(arr[i][j] == 0){
                continue;
            }
            // map.getX và map.getY để lấy vị trí cục bộ của màn hình chơi

            double bx = 30 + j * Brick.BRICK_WIDTH + Map.MAP_ZONE.getX();


            // có 8 loại gạch hehe
            // brickW - 6  : mỗi viên cách nhau 6 pixel ( ngang)
            // brickH - 6  : mỗi viên gạch cách nhau 6 pixel ( dọc )
            //(bx,by) : vị trí góc trái cùng
            //(w,h) : kích thước
            //(r,c) vị trí trong int map[][]
            if(arr[i][j] == 1) {
                bricks.add(new NormalBrick(bx, by,  i, j));
            } else if(arr[i][j] == 2){
                bricks.add(new ImmortalBrick(bx, by,  i, j));
            } else if (arr[i][j] == 3){
                bricks.add(new LifeUpBrick(bx, by,  i, j));
            } else if (arr[i][j] == 4){
                bricks.add(new GoldBrick(bx, by,  i, j));
            } else if (arr[i][j] == 5){
                bricks.add(new FallBombBrick(bx, by,  i, j));
            } else if (arr[i][j] == 6){
                bricks.add(new AreaBlastBrick(bx, by,  i, j));
            } else if (arr[i][j] == 7){
                bricks.add(new LuckyWheelBrick(bx, by,  i, j));
            } else if (arr[i][j] == 8 && levelType != LevelType.ULTIMATE_TWO){
                bricks.add(new PushBrick(bx, by, i, j));
            }
        }
        // gán độ cao mới cho minLoctateY
        minLocateY = (int) by;
        System.out.println();
    }

    public enum LevelType{
        CLASSICAL_ONE, CLASSICAL_TWO, CLASSICAL_THREE, CLASSICAL_FOUR,
        ULTIMATE_ONE, ULTIMATE_TWO

        // classical_one : các màn thuộc level 1 - > 3 : dễ
        // classical_two : các màn thuộc level 4  -> 6 : bình thường
        // classical_three : các màn thuộc level 7 ->9 : hơi khó
        // classical_four : các màn thuộc level 10 -> 13 : khó
        // ultimate_one : màn này , các viên gạch sẽ hạ xuống được và random thêm
        // ultimate_one : màn này , các viên gạch có thể tự do bay so với ultimate_one và có thể đẩy gạch
    }
}