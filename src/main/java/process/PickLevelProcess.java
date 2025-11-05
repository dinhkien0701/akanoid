package process;


import gamemanager.GameManager;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// PickLevel: màn hình chọn map vẽ bằng Canvas.
// Có 3 ô màu (đen, trắng, vàng). Mỗi lần render: duyệt list, nếu chuột nằm trong ô
// thì vẽ kích thước lớn, ngược lại vẽ kích thước mặc định.
public class PickLevelProcess extends Process {

    private boolean initialized = false; // chỉ dựng UI một lần

    // Kích thước cơ bản cũ (giữ lại nếu cần)
    private double w0 = 240, h0 = 90;      // kích thước mặc định

    private GameManager gmRef; // tham chiếu để xử lý click
    private Stage stageRef;

    // Chế độ
    private enum Mode { CLASSIC, ULTIMATE }
    private Mode currentMode = Mode.CLASSIC;

    // Kích thước nút chế độ (MODE)
    private final double MODE_W = 240, MODE_H = 90;          // thường
    private final double MODE_W_SEL = 300, MODE_H_SEL = 110; // khi được chọn

    // Kích thước nút level (LEVEL) và Back
    private final double LVL_W = 160, LVL_H = 60;            // các nút level cùng kích thước
    private final double BACK_W = 140, BACK_H = 54;

    private enum BtnType { MODE_CLASSIC, MODE_ULTIMATE, LEVEL, BACK }

    private static class Btn {
        double cx, cy, w, h; // tâm và kích thước
        Color color;        // màu
        String label;       // nhãn
        BtnType type;       // loại nút
        int levelIndex;     // index level
        Image image;        // ảnh hiển thị nếu có
        Btn(double cx, double cy, double w, double h, Color color, String label, BtnType type){
            this.cx=cx; this.cy=cy; this.w=w; this.h=h; this.color=color; this.label=label; this.type=type; this.levelIndex=0; this.image=null;
        }
        Btn withLevel(int idx){ this.levelIndex = idx; return this; }
        Btn withImage(Image img){ this.image = img; return this; }
    }
    private final List<Btn> modeButtons = new ArrayList<>();
    private final List<Btn> levelButtons = new ArrayList<>();
    private Btn backButton;

    public PickLevelProcess(int width, int height) {
        super(width, height);
    }

    @Override
    public void update(Stage stage, GameManager gameManager) {
        if (!initialized) {
            this.stageRef = stage;
            this.gmRef = gameManager;
            addBackground();
            setupStaticButtons();
            rebuildLevelButtons();
            initMouseInput();
            initialized = true;
        }
    }

    private void addBackground() {
        // Dùng nền chơi game làm nền chọn level (bk5.png)
        String filePath = "file:src" + File.separator
                + "main" + File.separator
                + "resources" + File.separator
                + "image" + File.separator
                + "bk12.jpg";
        Image bg = new Image(filePath);
        ImageView iv = new ImageView(bg);
        iv.setFitWidth(width);
        iv.setFitHeight(height);
        // Thêm vào vị trí 0 để nằm dưới Canvas
        this.pane.getChildren().add(0, iv);
        Pos.CENTER.getClass(); // giữ import Pos
    }

    private void setupStaticButtons() {
        double centerX = width / 2.0;
        double topY = 150;

        double cW = currentMode == Mode.CLASSIC ? MODE_W_SEL : MODE_W;
        double cH = currentMode == Mode.CLASSIC ? MODE_H_SEL : MODE_H;
        Btn classic = new Btn(centerX - 180, topY, cW, cH,
                Color.color(0, 0, 0, 0.7), "CLASSIC", BtnType.MODE_CLASSIC);

        double uW = currentMode == Mode.ULTIMATE ? MODE_W_SEL : MODE_W;
        double uH = currentMode == Mode.ULTIMATE ? MODE_H_SEL : MODE_H;
        Btn ultimate = new Btn(centerX + 180, topY, uW, uH,
                Color.color(1, 0.85, 0, 0.85), "ULTIMATE", BtnType.MODE_ULTIMATE);

        modeButtons.clear();
        modeButtons.add(classic);
        modeButtons.add(ultimate);

        backButton = new Btn(80, height - 60, BACK_W, BACK_H,
                Color.color(0.8, 0.2, 0.2, 0.85), "BACK", BtnType.BACK);
    }

    private void initMouseInput() {
        // Chỉ xử lý click
        this.scene.setOnMouseClicked(e -> {
            double x = e.getX(), y = e.getY();

            // Chế độ
            for (Btn b : modeButtons) {
                if (containsPoint(b, x, y)) {
                    if (b.type == BtnType.MODE_CLASSIC && currentMode != Mode.CLASSIC) {
                        currentMode = Mode.CLASSIC;
                        refreshModeButtonSizes();
                        rebuildLevelButtons();
                    } else if (b.type == BtnType.MODE_ULTIMATE && currentMode != Mode.ULTIMATE) {
                        currentMode = Mode.ULTIMATE;
                        refreshModeButtonSizes();
                        rebuildLevelButtons();
                    }
                    return;
                }
            }

            // Back
            if (containsPoint(backButton, x, y)) {
                if (gmRef != null && stageRef != null) gmRef.LeadToMenu(stageRef);
                return;
            }

            // Level -> vào game
            for (Btn b : levelButtons) {
                if (containsPoint(b, x, y)) {
                    if (gmRef != null && stageRef != null) {
                      gmRef.LeadToPlaying(stageRef, b.levelIndex);
                    }
                    return;
                }
            }
        });
    }

    private boolean containsPoint(Btn b, double x, double y) {
        double left = b.cx - b.w/2, top = b.cy - b.h/2;
        return x >= left && x <= left + b.w && y >= top && y <= top + b.h;
    }

    @Override
    public void render() {
        // Xóa canvas
        gc.clearRect(0, 0, width, height);

        // Vẽ nút chế độ
        gc.setLineWidth(2);
        for (Btn b : modeButtons) {
            drawButton(b);
        }

        // Vẽ danh sách level
        for (Btn b : levelButtons) {
            drawButton(b);
        }

        // Vẽ Back
        drawButton(backButton);
    }

    private void refreshModeButtonSizes() {
        for (Btn b : modeButtons) {
            boolean selected = (b.type == BtnType.MODE_CLASSIC && currentMode == Mode.CLASSIC)
                    || (b.type == BtnType.MODE_ULTIMATE && currentMode == Mode.ULTIMATE);
            b.w = selected ? MODE_W_SEL : MODE_W;
            b.h = selected ? MODE_H_SEL : MODE_H;
        }
    }

    private void rebuildLevelButtons() {
        levelButtons.clear();

        double startY = 300;
        double gapX = 24, gapY = 22;

        if (currentMode == Mode.CLASSIC) {
            int total = 13;
            int cols = 4;
            int rows = (int) Math.ceil(total / (double) cols);
            double totalWidth = cols * LVL_W + (cols - 1) * gapX;
            double startX = (width - totalWidth) / 2.0 + LVL_W / 2.0;
            int idx = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (idx >= total) break;
                    double cx = startX + c * (LVL_W + gapX);
                    double cy = startY + r * (LVL_H + gapY);
                    Btn b = new Btn(cx, cy, LVL_W, LVL_H,
                            Color.color(1, 1, 1, 0.85), "LEVEL " + (idx + 1), BtnType.LEVEL).withLevel(idx + 1);
                    levelButtons.add(b);
                    idx++;
                }
            }
        } else {
            double totalWidth = 2 * LVL_W + gapX;
            double startX = (width - totalWidth) / 2.0 + LVL_W / 2.0;
            double cy = startY;
            for (int i = 0; i < 2; i++) {
                double cx = startX + i * (LVL_W + gapX);
                Btn b = new Btn(cx, cy, LVL_W, LVL_H,
                        Color.color(1, 1, 1, 0.85), "ULTIMATE " + (i + 1), BtnType.LEVEL).withLevel(i + 14);
                levelButtons.add(b);
            }
        }
    }

    private void drawButton(Btn b) {
        double x = b.cx - b.w / 2.0;
        double y = b.cy - b.h / 2.0;

        if (b.image != null) {
            gc.drawImage(b.image, x, y, b.w, b.h);
        } else {
            gc.setFill(b.color);
            gc.fillRoundRect(x, y, b.w, b.h, 20, 20);
        }
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(x, y, b.w, b.h, 20, 20);

        Color textColor = (b.image != null)
                ? Color.WHITE
                : ((b.color.getRed() + b.color.getGreen() + b.color.getBlue()) / 3 > 0.6 ? Color.BLACK : Color.WHITE);
        gc.setFill(textColor);
        int fontSize = (b.type == BtnType.MODE_CLASSIC || b.type == BtnType.MODE_ULTIMATE) ? 28 : 22;
        gc.setFont(javafx.scene.text.Font.font(fontSize));
        String text = b.label == null ? "" : b.label;
        double tw = gc.getFont().getSize() * 0.6 * text.length();
        gc.fillText(text, b.cx - tw / 2.0, b.cy + 9);
    }
}
