import java.io.File;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;

public class MenuProcess {
  private final Image startScreen;
  private final int height, width;
  private int frame;
  private GraphicsContext gc;

  public MenuProcess(int width, int height, GraphicsContext gc){
    String filePath = "file:src" + File.separator
        + "main" + File.separator
        + "resources" + File.separator
        + "image" + File.separator
        + "startGame.png";
    startScreen = new Image(filePath);
    frame = height;
    this.width = width;
    this.height = height;
    this.gc = gc;
  }

  public void update(Scene scene, GameManager mn){
    frame -= 10;
    frame = Math.max(frame, 0);
    scene.setOnKeyPressed(e -> {
      KeyCode code = e.getCode();
      switch (code) {
        case ENTER:
          mn.finishMenu();
          break;
      }
    });
  }

  public void render(){
    gc.drawImage(startScreen, 0, frame, this.width, this.height);
  }
}
