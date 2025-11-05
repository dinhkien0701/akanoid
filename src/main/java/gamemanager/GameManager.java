package gamemanager;

import java.util.Objects;

import UI.GlobalSound;
import javafx.scene.Scene;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import process.*;

public class GameManager {
    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 750;

    private static GameManager instance;

    private final MenuProcess menu;
    private final PlayingProcess playing;
    private final GameOverProcess gameOver;
    private final PickLevelProcess pickLevel;
    private final OptionProcess option;

    private GameState gameState;

    private Scene scene;
    private final int width;
    private final int height;
    private final TutorialProcess tutorial;

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager(SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        return instance;
    }

    private GameManager(int width, int height) {
        gameState = GameState.INIT;
        this.width = width;
        this.height = height;
        menu = new MenuProcess(this.width, this.height);
        Rectangle map = new Rectangle(150, 0, 900, 700);
        playing = new PlayingProcess(width, height, map);
        gameOver = new GameOverProcess(width, height);
        pickLevel = new PickLevelProcess(width, height);
        option = new OptionProcess(width, height);
        tutorial = new TutorialProcess(width, height);
        try {
            AudioClip clip = new AudioClip(
                    Objects.requireNonNull(getClass().getResource("/sound/mapsound1.mp3")).toExternalForm()
            );
            clip.setCycleCount(AudioClip.INDEFINITE); // lặp vô hạn

            GlobalSound.register(clip);  // thêm clip vào GlobalSound
            GlobalSound.play(clip);      // phát nếu bật âm thanh

        } catch (Exception e) {
            System.out.println("Không tìm thấy mapsound1.mp3");
        }


    }

    public void process(Stage stage) {
        StackPane root = new StackPane();
        root.setPrefWidth(width);
        root.setPrefHeight(height);
        scene = new Scene(root);
        stage.setTitle("Akanoid - CaiWin Edition");
        stage.setScene(scene);
        stage.show();
        LeadToMenu(stage);
        this.startLoop(stage);
    }

    public void LeadToMenu(Stage stage) {
        menu.setScene(stage);
        menu.reset(stage, this);
        gameState = GameState.MENU;
    }

    public void LeadToTutorial(Stage stage) {
        tutorial.setScene(stage);
        gameState = GameState.TUTORIAL;
    }

    public void LeadToPickLevel(Stage stage) {
        pickLevel.setScene(stage);
        gameState = GameState.PICK_LEVEL;
    }

    public void LeadToPlaying(Stage stage, int level) {
        // classic level 1 -> 13
        // ultimate level 14 - 15
        playing.setCurrentMap(level); // cài level cho màn
        playing.reset();
        playing.addPause(this,stage);
        stage.setScene(playing.getScene());
        gameState = GameState.PLAYING;
    }

    public void LeadToOption(Stage stage) {
        gameState = GameState.INIT;
        option.setScene(stage);
        gameState = GameState.SETTING;
    }

    public void LeadToGameOver(Stage stage) {
        gameOver.setScene(stage);
        gameState = GameState.GAME_OVER;
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
                //System.out.println("PICK_LEVEL");
                pickLevel.update(stage, this);
                break;
            case PLAYING:
                //System.out.println("PLAYING");
                playing.update(stage,this);
                break;
            case TUTORIAL:
                tutorial.update(stage, this);
                break;
            case SETTING:
                option.update(stage,this);
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
            case SETTING:
                option.render();
                break;
            case TUTORIAL:
                tutorial.render();
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
        timer.start();
    }

    public enum GameState {
        INIT, MENU, PICK_LEVEL, PLAYING, SETTING, GAME_OVER, VICTORY,TUTORIAL
    }
}
