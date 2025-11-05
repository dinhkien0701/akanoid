package map;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

/**
 * CreatSpecialBrick: rải các loại gạch đặc biệt lên bản đồ đã có gạch thường.
 *
 * Quy ước ID (tham khảo hiện tại): 1 = Normal, 2 = Immortal, 3 = LifeUp, 4 = Gold, 5 = FallBomb, 6
 * = LuckyWheel, 7 = AreaBlast, 8 = SkillUp
 *
 * Cách làm: theo từng level, chọn ngẫu nhiên các vị trí đang là 1 (normal) để thay bằng id đặc biệt
 * tương ứng, số lượng phụ thuộc level và có trần.
 */
public class CreatSpecialBrick {
    private final List<Map> listOfMaps; // danh sách map theo level
    private int rows; // số hàng
    private int cols; // số cột

    // Khởi tạo
    public CreatSpecialBrick(List<Map> listOfMaps, int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.listOfMaps = listOfMaps;
    }

    /**
     * Đọc file immortalMap.txt để nạp thêm các bố cục "bất tử" (id=2) vào danh sách map. Mỗi block
     * trong file phân tách bằng dòng bắt đầu '#'.
     */
    // public void readImmortal() {
    // int level = 0;
    // try (InputStream is = LoadImage.class.getResourceAsStream("/immortalMap.txt");
    // BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
    // String line;
    // int row = 0;

    // // tạo map tạm 8x13 rồi push vào list khi gặp '#'
    // int[][] map = new int[Map.MAP_ROWS][Map.MAP_COLUMNS]; // listOfMaps.get(level).getMap();

    // while ((line = br.readLine()) != null) {
    // // System.out.println(line);
    // String[] path = line.split(" ");
    // if (line.charAt(0) == '#') {
    // level++;
    // row = 0;
    // listOfMaps.add(new Map(map, level));
    // System.out.println(listOfMaps.size());
    // for (int i = 0; i < 8; i++) {
    // for (int j = 0; j < 13; j++) {
    // System.out.print(map[i][j] + " ");
    // }
    // System.out.println();
    // }
    // map = new int[8][13];
    // } else {
    // for (int i = 0; i < path.length; i++) {
    // map[row][i] = Integer.parseInt(path[i]);
    // }
    // row++;
    // }
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    public void readImmortal() {
        final String RESOURCE = "/staticMap/immortalMap.txt"; // đúng với
                                                              // src/main/resources/staticMap/immortalMap.txt
        int level = 0;
        int row = 0;

        int[][] map = new int[Map.MAP_ROWS][Map.MAP_COLUMNS];

        try (InputStream in = getClass().getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new FileNotFoundException("Không thấy resource trên classpath: " + RESOURCE);
            }
            try (BufferedReader br =
                    new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty())
                        continue; // bỏ qua dòng trống

                    if (line.charAt(0) == '#') {
                        // gặp block mới: nếu block trước đã có dữ liệu, push vào list trước
                        if (row > 0) {
                            level++;
                            listOfMaps.add(new Map(deepCopy(map), level));
                            map = new int[Map.MAP_ROWS][Map.MAP_COLUMNS];
                            row = 0;
                        }
                        continue;
                    }

                    // điền 1 dòng dữ liệu
                    String[] tokens = line.split("\\s+");
                    int colsToRead = Math.min(tokens.length, Map.MAP_COLUMNS);
                    for (int c = 0; c < colsToRead; c++) {
                        map[row][c] = Integer.parseInt(tokens[c]);
                    }
                    row++;

                    // bảo vệ tràn hàng (nếu file có nhiều hơn MAP_ROWS dòng trong 1 block)
                    if (row >= Map.MAP_ROWS) {
                        // khi đầy hàng, coi như kết thúc block hiện tại
                        level++;
                        listOfMaps.add(new Map(deepCopy(map), level));
                        map = new int[Map.MAP_ROWS][Map.MAP_COLUMNS];
                        row = 0;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc " + RESOURCE, e);
        }

        // thêm block cuối nếu có dữ liệu mà chưa push
        if (row > 0) {
            level++;
            listOfMaps.add(new Map(map, level)); // map mới nên không cần copy nữa
        }
    }

    /** Sao chép sâu mảng 2D để tránh bị ghi đè dữ liệu giữa các level. */
    private static int[][] deepCopy(int[][] src) {
        int[][] dst = new int[src.length][];
        for (int i = 0; i < src.length; i++) {
            dst[i] = src[i].clone();
        }
        return dst;
    }

    /**
     * Đổi một số ô normal (id=1) sang id đặc biệt bất kỳ, chọn ngẫu nhiên vị trí.
     *
     * @param id loại gạch đặc biệt muốn gán
     * @param level level mục tiêu trong listOfMaps (1-based)
     * @param so_luong số ô cần chuyển
     */
    private void creatWithId(int id, int level, int so_luong) {
        Random rand = new Random();
        int x, y;
        int[][] map = listOfMaps.get(level - 1).getMap();
        int num = 5000;
        while (so_luong > 0 && num > 0) {
            x = rand.nextInt(rows);
            y = rand.nextInt(cols);
            if (map[x][y] == 1) { // chỉ thay khi đang là normal
                map[x][y] = id;
                so_luong--;
            }
            num--; // random tối đa 5000 vòng thôi
        }
    }

    /**
     * Rải toàn bộ loại gạch đặc biệt theo level với số lượng tỉ lệ level. Mục tiêu: có tăng dần
     * nhưng giữ trần để tránh quá tải màn chơi.
     */
    public void creatSpecialBrick() {
        for (int level = 1; level <= 20; level++) {
            // Random thêm gạch bất tử từ level 7
            if (level >= 7) {
                int immortal = level;
                if (level >= 10)
                    immortal += 3;
                if (level >= 13)
                    immortal += 2;
                creatWithId(2, level, immortal);
            }

            // Mỗi màn tặng tối đa 3 mạng: level 1 -> +1, level 5 -> +2, level 10 -> +3
            int lifeUp = Math.min(3, 1 + level / 5);

            // Tương tự với ô tặng xu (gold)
            int gold = lifeUp;

            // Bom rơi (cần cân bằng thêm): tăng theo nhiều bậc nhỏ, trần 10
            int fallBomb = Math.min(10,
                    level / 3 + level / 6 + level / 8 + level / 10 + level / 12 + level / 14);

            // Nổ khu vực: không vượt quá số bom rơi, trần 5
            int areaBlast = Math.min(5, fallBomb);

            // Phép đẩy: trần 3
            if (level >= 7) {
                int push = Math.min(6, level / 2);
                if (level >= 10)
                    push += 1;
                if (level >= 13)
                    push += 1;
                creatWithId(8, level, push);
            }

            // Vòng quay may mắn: trần 3
            int wheel = Math.min(3, level / 5);

            // Áp vào map level tương ứng
            creatWithId(3, level, lifeUp);
            creatWithId(4, level, gold);
            creatWithId(5, level, fallBomb);
            creatWithId(6, level, wheel);
            creatWithId(7, level, areaBlast);
        }

    }

}


