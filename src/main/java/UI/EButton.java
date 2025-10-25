//package UI;
//
//import javafx.animation.ScaleTransition;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.control.Button;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.StackPane;
//import javafx.util.Duration;
//
//public class EButton extends Button {
//    private ImageView imageView;
//    private Image image;
//
//    public EButton(String s,int x, int y, int width, int height) {
//        super(s);
//        super.setPrefWidth(x);
//        super.setPrefHeight(y);
//        super.setStyle(
//                "-fx-background-color: #0227; " +
//                        "-fx-background-radius: 100;");
//        StackPane.setAlignment(btn, Pos.BOTTOM_RIGHT);
//        StackPane.setMargin(btn, new Insets(0,360,260,0));
//        ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
//        btn.setOnMouseEntered(e -> {
//            st.setToX(1.1);
//            st.setToY(1.1);
//            st.playFromStart();
//            btn.setFocusTraversable(true);
//        });
//        btn.setOnMouseClicked(e-> {
//            gameManager.finishMenu(stage);
//        });
//        btn.setOnMouseExited(e -> {
//            st.setToX(1.0);
//            st.setToY(1.0);
//            st.playFromStart();
//            btn.setFocusTraversable(false);
//        });
//
//        Button SelectModeButton = new Button("SelectMode");
//        SelectModeButton.setPrefWidth(250);
//        SelectModeButton.setPrefHeight(60);
//        ScaleTransition SMB = new ScaleTransition(Duration.millis(200), SelectModeButton);
//    }
//}
