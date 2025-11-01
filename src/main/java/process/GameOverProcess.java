package process;


import gamemanager.GameManager;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import javafx.stage.Stage;

public class GameOverProcess extends Process {


  public GameOverProcess(int width, int height) {
      super(width,height);
  }


  @Override
  public void update(Stage stage, GameManager gm) {
    this.scene.setOnKeyPressed(e -> {
      switch (e.getCode()) {
        case SPACE:
          gm.rePlay(stage);
          break;
        case ESCAPE:
          System.exit(0);
          break;
      }
    });
  }

  @Override
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