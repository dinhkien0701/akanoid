package process;


import java.io.InputStream;
import java.util.Objects;

import UI.OptionButton;
import UI.QuitButton;
import UI.StartButton;
import gamemanager.GameManager;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class MenuProcess extends Process {
    private Image startScreen;
    private boolean checkEndCRTEffect;
    private boolean check = true;
    private int buttonStage = 0;


    public MenuProcess(int width, int height) {
        super(width, height);
        try {
            InputStream imageStream = getClass().getResourceAsStream("/image/BackgroundMenu.png");

            if (imageStream == null) {
                throw new IllegalArgumentException("Lỗi: Không tìm thấy tài nguyên ảnh tại '/image/BackgroundMenu.png'");
            }

            startScreen = new Image(imageStream);

        } catch (Exception e) {
            System.err.println("Không thể tải ảnh menu. Game có thể sẽ không hiển thị đúng.");
            e.printStackTrace();
            startScreen = null;
        }
        checkEndCRTEffect = false;
        buttonStage = 0;
    }

    @Override
    public void update(Stage stage, GameManager gameManager) {
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            switch (code) {
                case UP:
                    buttonStage = (buttonStage + 1) % 2;
                    break;
                case DOWN:
                    buttonStage = (buttonStage - 1) % 2;
                    if(buttonStage < 0) {
                        buttonStage = 1;
                    }
                    break;
                case ENTER:
                    System.exit(0);
                    break;
            }
        });
        if (checkEndCRTEffect && !check) {
            addMenuBackground(stage, gameManager);
            addStartButton(stage, gameManager);
            addOptionButton();
            addQuitButton();
            buttonStage = 0;
            check = true;
        }

    }


    private void CRTTvShow(){
        Rectangle flash = new Rectangle(width, 5, Color.WHITE);
        flash.setArcWidth(20);
        flash.setArcHeight(20);
        this.pane.getChildren().add(flash);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.2), flash);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition expandY = new ScaleTransition(Duration.seconds(1.0), flash);
        expandY.setFromY(1);
        expandY.setToY(150);

        SequentialTransition tvOn = new SequentialTransition(fadeIn, expandY);
        tvOn.setOnFinished(e -> {
            this.pane.getChildren().remove(flash);
            check = false;
        });
        tvOn.play();
    }

    private void addMenuBackground(Stage stage, GameManager gameManager) {
        ImageView imageView = new ImageView();
        imageView.setImage(startScreen);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        this.pane.getChildren().add(imageView);
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
        if(!checkEndCRTEffect) {
            CRTTvShow();
            checkEndCRTEffect = true;
        }
    }

    public enum ButtonStage {
        STARTBUTTON,
        SETTINGBUTTON,
        EXITBUTTON
    }
}
