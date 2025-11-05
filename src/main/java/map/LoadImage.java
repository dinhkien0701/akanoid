package map;

import javafx.scene.image.Image;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class LoadImage {

    /*
     * sử dụng static chính lớp để không cần tạo đối tượng mà một phương thức tĩnh sẽ chiếu đến
     * chính một bản khác của nó chứ hình ảnh
     */

    // Tạo instance để có thể lấy từ mọi nơi qua phuong thức loadImage
    // private static LoadImage instance; // Instance duy nhất của LoadImage
    private static final Map<String, Image> imageMap = new HashMap<>(); // Lưu trữ các ảnh đã tải

    // Private constructor để ngăn việc tạo đối tượng từ bên ngoài
    /*
     * private LoadImage() { imageMap = new HashMap<>(); }
     */

    // Phương thức để lấy instance duy nhất
    /*
     * public static LoadImage getInstance() { if (instance == null) { instance = new LoadImage(); }
     * return instance; }
     */

    /**
     * Tải và lưu trữ một hình ảnh vào cache nếu nó chưa được tải. Sử dụng ClassPath để tải (tốt
     * nhất khi đóng gói game). Ví dụ: "/image/gach_do.png"
     *
     * @param resourcePath Đường dẫn tài nguyên bắt đầu bằng "/"
     * @return Đối tượng Image đã tải, hoặc null nếu có lỗi.
     */
    public static Image getImage(String resourcePath) {
        // Nếu ảnh đã có trong cache, trả về ngay lập tức
        // constainkey : kiểm tra đối phần tử với khóa đã tồn tại hay chưa
        if (imageMap.containsKey(resourcePath)) {
            return imageMap.get(resourcePath);
        }

        // Nếu chưa có, tiến hành tải ảnh
        Image image = null;
        try {
            // tạo luồng để nhận hình ảnh
            // Dặt là loadImage.class hay bất kì another.class nào đêù tương đương
            // nó sẽ lấy từ đường dẫn tương đối bắt đầu từ resorource path của project
            InputStream is = LoadImage.class.getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("Lỗi: Không tìm thấy tài nguyên: " + resourcePath);
                return null;
            }
            image = new Image(is);
            if (image.isError()) {
                System.err.println("Lỗi tải ảnh (resource): " + resourcePath + " - "
                        + image.exceptionProperty().get().getMessage());
                image = null;
            } else {
                imageMap.put(resourcePath, image); // Lưu vào cache nếu tải thành công
            }
            is.close();
        } catch (Exception e) {
            System.err.println("Lỗi tải ảnh (Exception từ resource): " + resourcePath + " - "
                    + e.getMessage());
        }
        return image;
    }

    /**
     * Phương thức này có thể được dùng để tải trước tất cả các ảnh khi khởi tạo game. Ví dụ:
     * ImageCache.getInstance().preloadImages();
     */
    public static void preloadImages() {
        // Ví dụ tải trước các ảnh. Bạn có thể mở rộng danh sách này.
        getImage("/image/gach_do.png");
        getImage("/image/gach_bat_tu.png");
        getImage("/image/gach_la_cay.png");
        // ... thêm các ảnh khác
        System.out.println("Đã tải trước " + imageMap.size() + " hình ảnh.");
    }

    // Bạn có thể thêm phương thức để xóa cache nếu cần thiết (ví dụ khi chuyển màn hình chính)
    public static void clearCache() {
        imageMap.clear();
        System.out.println("Image cache đã được xóa.");
    }
}
