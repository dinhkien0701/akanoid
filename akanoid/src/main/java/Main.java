import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

  public static final int SCREEN_WIDTH = 1200;
  public static final int SCREEN_HEIGHT = 700;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    GameStart gameApp = new GameStart(SCREEN_WIDTH, SCREEN_HEIGHT);

    StackPane root = new StackPane(gameApp.getCanvas());
    Scene scene = new Scene(root);

    primaryStage.setTitle("Akanoid");
    primaryStage.setScene(scene);
    primaryStage.show();

    gameApp.initInput(scene); // Gắn điều khiển
    gameApp.startLoop(); // Khởi chạy game loop
  }
}
