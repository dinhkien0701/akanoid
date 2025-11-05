package process;

import UI.BackButton;
import UI.SoundButton;
import UI.TutorialButton;
import gamemanager.GameManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;


public class OptionProcess extends Process {

    private SoundButton soundButton;
    private TutorialButton tutorialButton;
    private BackButton backButton;

    public OptionProcess(int width, int height) {
        super(width, height);
    }

    public void addSoundButton() {
        soundButton = new SoundButton("Sound", Pos.CENTER, new Insets(0, 0, 180, 0), 300, 72);
        pane.getChildren().add(soundButton);
    }

    public void addTutorialButton(Stage stage, GameManager gameManager) {
        tutorialButton = new TutorialButton("Tutorial", Pos.CENTER, new Insets(0, 0, 0, 0), 300, 72,
                gameManager, stage);
        pane.getChildren().add(tutorialButton);
    }

    public void addBackButton(Stage stage, GameManager gameManager) {
        backButton = new BackButton("Back", Pos.CENTER, new Insets(180, 0, 0, 0), 300, 72,
                gameManager, stage);
        pane.getChildren().add(backButton);
    }

    private void addBackground() {
        String filePath = "file:src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "image" + File.separator + "OptionMenu.png";
        Image backgroundImage = new Image(filePath);
        ImageView backgroundImageView = new ImageView(backgroundImage);

        backgroundImageView.setFitWidth(this.width);
        backgroundImageView.setFitHeight(this.height);
        this.pane.getChildren().add(0, backgroundImageView);
    }


    @Override
    public void update(Stage stage, GameManager gameManager) {
        if (!isAddButton()) {
            addBackground();
            addBackButton(stage, gameManager);
            addSoundButton();
            addTutorialButton(stage, gameManager);
            addButtonDone();
        }
    }

    @Override
    public void render() {}
}
