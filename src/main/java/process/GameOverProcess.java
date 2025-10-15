package process;

import core.GameManager;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class GameOverProcess {
  private final int width, height;
  private final GraphicsContext gc;

  public GameOverProcess(int width, int height, GraphicsContext gc) {
    this.width = width;
    this.height = height;
    this.gc = gc;
  }

  public void update(Scene scene, GameManager gm) {
    scene.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.ENTER) {
        gm.rePlay();
      }
    });
  }

  public void render() {
    gc.save();
    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, width, height);
    gc.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
    gc.setFill(Color.RED);
    gc.setTextAlign(TextAlignment.CENTER);
    gc.fillText("GAME OVER", width / 2.0, height / 2.0);
    gc.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
    gc.setFill(Color.WHITE);
    gc.fillText("Press SPACE to Play Again", width / 2.0, height / 2.0 + 40);
    gc.restore();
  }
}