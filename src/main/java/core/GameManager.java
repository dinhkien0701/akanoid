package core;

import java.util.Objects;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import process.*;

public class GameManager {

  private MenuProcess menu;
  private PlayingProcess playing;
  private GameOverProcess gameOver;

  private GameState gameState;
  private final Canvas canvas;
  private final GraphicsContext gc;

  public enum GameState {
    INIT,
    MENU,
    PLAYING,
    PAUSE,
    GAME_OVER,
    VICTORY,
    CREDITS
  }

  private int width, height;

  public GameManager(int width, int height) {
    gameState = GameState.INIT;
    this.width = width;
    this.height = height;

    canvas = new Canvas(this.width, this.height);
    gc = canvas.getGraphicsContext2D();

    menu = new MenuProcess(this.width, this.height, this.gc);
    Rectangle map = new Rectangle(150,0, 900,700);
    playing = new PlayingProcess(width, height, map , this.gc);
    gameOver = new GameOverProcess(width, height, this.gc);

    gameState = GameState.MENU;
  }

  public void process(Stage stage){
    StackPane root = new StackPane(canvas);
    Scene scene = new Scene(root);

    stage.setTitle("Akanoid - CaiWin Edition");
    stage.setScene(scene);
    stage.show();

    this.startLoop(scene);
  }

  public void finishMenu(){
    gameState = GameState.PLAYING;
  }

  public void finishPlay() {
    gameState = GameState.GAME_OVER;
  }

  public void rePlay() {
    gameState = GameState.INIT;
    playing.reset();
    gameState = GameState.PLAYING;
  }

  public void update(Scene scene){
    scene.setOnKeyPressed(e -> {
      if (Objects.requireNonNull(e.getCode()) == KeyCode.ESCAPE) {
        System.exit(0);
      }
    });

    switch (gameState){
      case MENU:
        menu.update(scene,this);
        break;
      case PLAYING:
        playing.update(scene,this);
        break;
      case GAME_OVER:
        gameOver.update(scene,this);
        break;
    }
  }

  public void render(){
    switch (gameState){
      case MENU:
        menu.render();
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
  public void startLoop(Scene scene) {
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
          update(scene);
          render();
          lastFrameTime = nowMs;
        }
      }
    };
    timer.start();
  }
}