import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import java.awt.Rectangle;

public class GameStart {

  private static final double FPS = 70.0;
  private static final double FRAME_TIME = 1000.0 / FPS;

  private final Canvas canvas;
  private final GameManager gameManager;
  private long lastFrameTime = 0;

  public GameStart(int width, int height) {
    canvas = new Canvas(width, height);

    int MAP_X = 300;
    int MAP_Y = 0;
    int MAP_WIDTH = 600;
    int MAP_HEIGHT = 700;
    Rectangle map = new Rectangle(MAP_X, MAP_Y, MAP_WIDTH, MAP_HEIGHT);
    gameManager = new GameManager(width, height, map, getCanvas().getGraphicsContext2D());
    gameManager.gc = canvas.getGraphicsContext2D();
  }

  public Canvas getCanvas() {
    return canvas;
  }

  public void initInput(Scene scene) {
    scene.setOnKeyPressed(e -> {
      KeyCode code = e.getCode();
      switch (code) {
        case ESCAPE -> System.exit(0);
        case SPACE, UP -> {
          if ((gameManager.gameState).equals(GameManager.GameState.READY)) {
            gameManager.startGame();
          } else if ((gameManager.gameState).equals(GameManager.GameState.GAMEOVER)) {
            gameManager.reset();
          }
        }
        case LEFT -> gameManager.pressedLeft = true;
        case RIGHT -> gameManager.pressedRight = true;
      }
    });

    scene.setOnKeyReleased(e -> {
      if (e.getCode() == KeyCode.LEFT)
        gameManager.pressedLeft = false;
      if (e.getCode() == KeyCode.RIGHT)
        gameManager.pressedRight = false;
    });
  }

  public void startLoop() {
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
          gameManager.update();
          gameManager.render();
          lastFrameTime = nowMs;
        }
      }
    };
    timer.start();
  }
}
