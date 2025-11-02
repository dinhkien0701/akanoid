package core;

import java.util.Objects;
import javafx.scene.Scene;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import process.*;

public class GameManager {

    // Các khung hình cần cho game

    private final MenuProcess menu;
    private final PickLevel pickLevel;
    private final PlayingProcess playing;
    private final GameOverProcess gameOver;

    private GameState gameState; // GameState là một enum

    private Scene scene;
    private final int width;
    private final int height;

    public GameManager(int width, int height) {
        gameState = GameState.INIT;
        this.width = width;
        this.height = height;

        menu = new MenuProcess(this.width, this.height);

        Rectangle map = new Rectangle(150,0, 900,700);
        playing = new PlayingProcess(width, height, map);
        pickLevel = new PickLevel(this.width, this.height);

        gameOver = new GameOverProcess(width, height);

    }

    public void process(Stage stage){
        /**
         * StackPane là gì?
         * Nó xếp chồng (stack) tất cả các node con,
         *(component: Button, ImageView, Text, Canvas, v.v.),
         * lên trên nhau — giống như xếp nhiều lớp giấy chồng
         * lên cùng một điểm giữa */

        StackPane root = new StackPane();
        root.setPrefWidth(width);
        root.setPrefHeight(height);
        scene = new Scene(root);
        stage.setTitle("Akanoid - CaiWin Edition");
        stage.setScene(scene);
        stage.show();
        startMenu(stage);
        this.startLoop(stage);
    }

    public void startMenu(Stage stage){
        stage.setScene(menu.getScene());
        gameState = GameState.MENU;
    }

    public void finishMenu(Stage stage){
        // Sau khi ấn Start ở Menu -> chuyển sang màn chọn level
        stage.setScene(pickLevel.getScene());
        gameState = GameState.PICK_LEVEL;
    }

    // Từ màn chọn level -> vào chơi (tạm thời luôn vào gameplay mặc định)
    public void finishPickLevel(Stage stage , int level){
        // classic level 1 -> 13
        // ultimate level 14 - 15
        playing.setCurrentMap(level); // cài level cho màn
        stage.setScene(playing.getScene());
        gameState = GameState.PLAYING;
    }

    public void finishPlay(Stage stage) {
        stage.setScene(gameOver.getScene());
        gameState = GameState.GAME_OVER;
    }

    public void rePlay(Stage stage) {
        stage.setScene(playing.getScene());
        gameState = GameState.INIT;
        playing.reset();
        gameState = GameState.PLAYING;

    }

    public void update(Stage stage){
        scene.setOnKeyPressed(e -> {
            if (Objects.requireNonNull(e.getCode()) == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });

        switch (gameState){
            case MENU:
                //System.out.println("MENU");
                menu.update(stage,this);
                break;
            case PICK_LEVEL:
                pickLevel.update(stage, this);
                break;
            case PLAYING:
                //System.out.println("PLAYING");
                playing.update(stage,this);
                break;
            case GAME_OVER:
                //System.out.println("GAME OVER");
                gameOver.update(stage,this);
                break;
        }
    }

    public void render(){
        switch (gameState){
            case MENU:
                menu.render();
                break;
            case PICK_LEVEL:
                pickLevel.render();
                break;
            case PLAYING:
                playing.render();
                break;
            case GAME_OVER:
                gameOver.render();
                break;
        }
    }

    private static final double FPS = 70.0;
    private static final double FRAME_TIME = 1000.0 / FPS;
    private long lastFrameTime = 0;
    public void startLoop(Stage stage) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long nowMs = now / 1_000_000;
                if (lastFrameTime == 0) {
                    lastFrameTime = nowMs;
                    return;
                }
                long delta = nowMs - lastFrameTime;
                if (delta >= FRAME_TIME) {
                    update(stage);
                    render();
                    lastFrameTime = nowMs;
                }
            }
        };
        timer.start(); // chạy thay đổi khung hình này cho toàn bộ khung hình
    }

    // Đây là kiểu dữ liệu thay cho public static final
    public enum GameState {
        INIT,
        MENU,
        PICK_LEVEL,
        PLAYING,
        PAUSE,
        GAME_OVER,
        VICTORY,
        CREDITS
    }
}
