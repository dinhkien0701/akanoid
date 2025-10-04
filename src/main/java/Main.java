import java.awt.Rectangle;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

  private static final int WIDTH = 1200;
  private static final int HEIGHT = 700;
  private static final int MAP_WIDTH = 600;
  private static final int MAP_HEIGHT = 700;
  private static final int MAP_X = 300;
  private static final int MAP_Y = 0;

  Rectangle map = new Rectangle(MAP_X, MAP_Y, MAP_WIDTH, MAP_HEIGHT);
  private GameManager gameManager;

  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage primaryStage) {
    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    gameManager = new GameManager(WIDTH, HEIGHT, map, gc);

    StackPane root = new StackPane(canvas);
    Scene scene = new Scene(root);

    scene.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ESCAPE) {
        System.exit(0);
      }
      if (gameManager.gameState == GameState.READY) {
        if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.UP) {
            gameManager.startGame();
        }

      } else if (gameManager.gameState == GameState.GAMEOVER) {
        if (e.getCode() == KeyCode.SPACE) {
          gameManager.reset();
        }
      }
      if (e.getCode() == KeyCode.LEFT) {
        gameManager.pressedLeft = true;
      }
      if (e.getCode() == KeyCode.RIGHT) {
        gameManager.pressedRight = true;
      }
//      if (e.getCode() == KeyCode.B) {
//        gameManager.bricks.clear();
//      }
    });

    scene.setOnKeyReleased(e -> {
      if (e.getCode() == KeyCode.LEFT) {
        gameManager.pressedLeft = false;
      }
      if (e.getCode() == KeyCode.RIGHT) {
        gameManager.pressedRight = false;
      }
    });

    primaryStage.setTitle("Dionakra");
    primaryStage.setScene(scene);
    primaryStage.show();

    AnimationTimer timer = new AnimationTimer() {
      public void handle(long now) {
        gameManager.update();
        gameManager.render();
      }
    };
    timer.start();
  }

  public enum GameState {
    READY, RUNNING, FINISHMAP, GAMEOVER
  }
}
