package process;


import UI.OptionButton;
import UI.QuitButton;
import UI.StartButton;
import gamemanager.GameManager;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import javafx.stage.Stage;

public class GameOverProcess extends Process {
    private int score = 0;

  public GameOverProcess(int width, int height) {
      super(width,height);
  }


  @Override
  public void update(Stage stage, GameManager gm) {
    this.scene.setOnKeyPressed(e -> {
      switch (e.getCode()) {
        case ESCAPE:
          System.exit(0);
          break;
      }
    });
    if(!isAddButton()) {
        addOptionButton();
        addStartButton(stage, gm);
        addQuitButton();
        addButtonDone();
    }
  }

    private void addOptionButton() {
        pane.getChildren().add(
            new OptionButton("Option", new Insets(0, 360, 180, 0),
            250,60,
            "/image/OptionButton.png"));
    }

    private void addStartButton(Stage stage, GameManager gameManager) {
        pane.getChildren().add(
            new StartButton("Start", new Insets(0, 360, 260, 0),
            250,60,
            "/image/Sprite-0001.png", gameManager,stage));
    }

    private void addQuitButton() {
        pane.getChildren().add(
            new QuitButton("Quit", new Insets(0, 360, 100, 0),
            250,60,
            "/image/QuitButton.png"));
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