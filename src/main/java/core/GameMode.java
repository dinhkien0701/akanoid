package core;

import javafx.stage.Stage;

// GameMode: giao diện để tách chế độ chơi (mode)
// Ý tưởng: các mode sẽ cắm vào 4 vòng đời: vào, cập nhật, vẽ, thoát.
// Mặc định tất cả đều no-op (không làm gì) -> không phá code hiện tại.
public interface GameMode {
    // id ổn định cho từng mode (ví dụ: "classic", "ultimate")
    String id();

    // Gọi khi mode được kích hoạt (bắt đầu dùng)
    default void onEnter(Stage stage, GameManager gameManager) {}

    // Cập nhật mỗi khung hình khi đang ở mode này
    default void onUpdate(Stage stage, GameManager gameManager) {}

    // Vẽ mỗi khung hình (nếu mode cần can thiệp render)
    default void onRender(GameManager gameManager) {}

    // Gọi khi rời khỏi mode hiện tại
    default void onExit(Stage stage, GameManager gameManager) {}
}
