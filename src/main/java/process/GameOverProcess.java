package process;

import UI.QuitButton;
import UI.ReplayButton;
import gamemanager.GameManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.File;

public class GameOverProcess extends Process {
    private boolean uiInitialized = false;
    private ReplayButton replayButton;
    private QuitButton quitButton;
    private int selectedButtonIndex = 0;

    public GameOverProcess(int width, int height) {
        super(width, height);
        this.pane.getChildren().clear();
    }

    private void initUI(Stage stage, GameManager gameManager) {
        String filePath = "file:src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "image" + File.separator + "GameOver.png";
        Image bgImage = new Image(filePath);
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(this.width);
        bgView.setFitHeight(this.height);
        this.pane.getChildren().add(0, bgView);

        int btnWidth = 250;
        int btnHeight = 60;

        replayButton = new ReplayButton("Replay", Pos.CENTER, new Insets(0, 0, 50, 0), btnWidth,
                btnHeight, gameManager, stage);

        quitButton = new QuitButton("Quit", Pos.CENTER, new Insets(50, 0, 0, 0), btnWidth, btnHeight);

        this.pane.getChildren().addAll(replayButton, quitButton);

        updateButtonFocus();
    }

    private void updateButtonFocus() {
        if (selectedButtonIndex == 0) {
            replayButton.requestFocus();
        } else {
            quitButton.requestFocus();
        }
    }

    @Override
    public void update(Stage stage, GameManager gm) {
        if (!uiInitialized) {
            initUI(stage, gm);
            uiInitialized = true;
        }
        this.scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.UP || code == KeyCode.DOWN) {
                selectedButtonIndex = 1 - selectedButtonIndex;
                updateButtonFocus();
            }
        });
    }

    @Override
    public void render() {}
}