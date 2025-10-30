package core.mode;

import core.GameManager;
import core.GameMode;
import javafx.stage.Stage;

// UltimatePvp: chế độ Ultimate 2 người (PvP)
// Dự kiến đồng bộ tối thiểu (bóng + paddle), map quyết định bằng seed.
public class UltimatePvp implements GameMode {
    @Override
    public String id() {
        return "ultimatepvp";
    }

    @Override
    public void onEnter(Stage stage, GameManager gameManager) {
        // Khởi tạo riêng cho Ultimate PvP (nếu cần)
    }
}
