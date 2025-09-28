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
  private static final int HEIGHT = 800;

  private GameManager gameManager;

  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage primaryStage) {
    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    gameManager = new GameManager(WIDTH, HEIGHT, gc);

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
    });

    scene.setOnKeyReleased(e -> {
      if (e.getCode() == KeyCode.LEFT) {
        gameManager.pressedLeft = false;
      }
      if (e.getCode() == KeyCode.RIGHT) {
        gameManager.pressedRight = false;
      }
    });

    primaryStage.setTitle("Arkanoid - Skeleton");
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

  enum GameState { READY, RUNNING, GAMEOVER }
}
