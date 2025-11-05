package process;

import UI.BackButton;
import gamemanager.GameManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TutorialProcess extends Process {

    private boolean uiInitialized = false;
    private final List<Image> pages = new ArrayList<>();
    private int currentPageIndex = 0;
    private ImageView pageImageView;
    private BackButton backButton;

    public TutorialProcess(int width, int height) {
        super(width, height);
        this.pane = new StackPane();
        this.scene = new Scene(this.pane, width, height);
        this.pane.setStyle("-fx-background-color: black;");
        loadPages();
    }

    private void loadPages() {
        for (int i = 1; i <= 4; i++) {
            String filePath = "file:src/main/resources/image/HowToPlay" + i + ".jpg";
            try {
                Image pageImage = new Image(filePath);
                if (!pageImage.isError()) {
                    pages.add(pageImage);
                } else {
                    System.err.println("Lỗi khi tải ảnh: " + filePath);
                }
            } catch (Exception e) {
                System.err.println("Ngoại lệ khi tải ảnh: " + filePath);
                e.printStackTrace();
            }
        }
    }

    private void initUI(Stage stage, GameManager gameManager) {
        pageImageView = new ImageView();
        pageImageView.setPreserveRatio(true);
        pageImageView.setFitHeight(this.height);
        this.pane.getChildren().add(0, pageImageView);
        backButton = new BackButton("Back", Pos.BOTTOM_LEFT, new Insets(0, 0, 50, 50), 200, 50,
                () -> gameManager.LeadToOption(stage));
        this.pane.getChildren().add(backButton);
        updatePageContent();
    }

    private void updatePageContent() {
        if (pages.isEmpty())
            return;

        if (currentPageIndex >= 0 && currentPageIndex < pages.size()) {
            pageImageView.setImage(pages.get(currentPageIndex));
        }
    }

    public void nextPage() {
        if (currentPageIndex < pages.size() - 1) {
            currentPageIndex++;
            updatePageContent();
        }
    }

    public void previousPage() {
        if (currentPageIndex > 0) {
            currentPageIndex--;
            updatePageContent();
        }
    }

    @Override
    public void update(Stage stage, GameManager gameManager) {
        if (!uiInitialized) {
            initUI(stage, gameManager);
            uiInitialized = true;
        }

        this.scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.RIGHT || code == KeyCode.SPACE)
                nextPage();
            else if (code == KeyCode.LEFT)
                previousPage();
        });
    }

    @Override
    public void setScene(Stage stage) {
        this.currentPageIndex = 0;
        if (uiInitialized) {
            updatePageContent();
        }
        stage.setScene(this.scene);
        this.pane.requestFocus(); // đảm bảo pane nhận sự kiện
        this.scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.RIGHT || code == KeyCode.SPACE)
                nextPage();
            else if (code == KeyCode.LEFT)
                previousPage();
        });
    }

    public void onExit() {}

    @Override
    public void render() {}
}