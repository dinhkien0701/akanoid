package process;

import core.GameManager;
import java.io.InputStream; // Import thêm InputStream
import java.util.Objects;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import core.Process;

public class MenuProcess extends Process {
    private Image startScreen;
    private boolean checkEndCRTEffect;
    public MenuProcess(int width, int height) {
        super(width, height);
        try {
            InputStream imageStream = getClass().getResourceAsStream("/image/startGame.png");

            if (imageStream == null) {
                throw new IllegalArgumentException("Lỗi: Không tìm thấy tài nguyên ảnh tại '/image/startGame.png'");
            }

            startScreen = new Image(imageStream);

        } catch (Exception e) {
            System.err.println("Không thể tải ảnh menu. Game có thể sẽ không hiển thị đúng.");
            e.printStackTrace();
            startScreen = null;
        }
        checkEndCRTEffect = false;
    }

    private boolean check = true;

    @Override
    public void update(Stage stage, GameManager  gameManager) {
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.ENTER) {
                gameManager.finishMenu(stage);
            } else if(code == KeyCode.ESCAPE) {
              System.exit(0);
            }
        });
        if (checkEndCRTEffect && !check) {
            addSecond(stage, gameManager);
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

    private void addSecond(Stage stage, GameManager gameManager) {

        ImageView imageView = new ImageView();
        imageView.setImage(startScreen);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        this.pane.getChildren().add(imageView);


        Button btn = new Button("Start");

        btn.setPrefWidth(250);
        btn.setPrefHeight(60);
        btn.setStyle(
            "-fx-background-color: #0227; " +
            "-fx-background-radius: 100;");
        StackPane.setAlignment(btn, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(btn, new Insets(0,360,260,0));
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/StartMenuButton.png")));
        ImageView imageView2 = new ImageView(image);
        this.pane.getChildren().add(btn);
        imageView2.setViewport(new Rectangle2D(0,0,250,60));
        btn.setGraphic(imageView2);
        ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
        btn.setOnMouseEntered(e -> {
            st.setToX(1.1);
            st.setToY(1.1);
            st.playFromStart();
            btn.setFocusTraversable(true);
            imageView2.setViewport(new Rectangle2D(0,540,250,60));
        });
        btn.setOnMouseClicked(e-> {
            gameManager.finishMenu(stage);
        });
        btn.setOnMouseExited(e -> {
            st.setToX(1.0);
            st.setToY(1.0);
            st.playFromStart();
            btn.setFocusTraversable(false);
            imageView2.setViewport(new Rectangle2D(0,0,250,60));
        });

//        Button SelectModeButton = new Button("SelectMode");
//        SelectModeButton.setPrefWidth(250);
//        SelectModeButton.setPrefHeight(60);
//        ScaleTransition SMB = new ScaleTransition(Duration.millis(200), SelectModeButton);
    }

    @Override
    public void render() {
        if(!checkEndCRTEffect) {
            CRTTvShow();
            checkEndCRTEffect = true;
        }
    }
}
