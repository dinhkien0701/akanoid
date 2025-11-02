package map;

import LoadResource.LoadImage;

import java.io.*;
import java.util.List;
import java.util.Random;

/**
 * CreatSpecialBrick: rải các loại gạch đặc biệt lên bản đồ đã có gạch thường.
 *
 * Quy ước ID (tham khảo hiện tại):
 * 1 = Normal, 2 = Immortal, 3 = LifeUp, 4 = Gold,
 * 5 = FallBomb, 6 = LuckyWheel, 7 = AreaBlast, 8 = SkillUp
 *
 * Cách làm: theo từng level, chọn ngẫu nhiên các vị trí đang là 1 (normal)
 * để thay bằng id đặc biệt tương ứng, số lượng phụ thuộc level và có trần.
 */
public class CreatSpecialBrick  {
    private List<Map> listOfMaps; // danh sách map theo level
    private int rows;             // số hàng
    private int cols;             // số cột

    // Khởi tạo
    public CreatSpecialBrick( List<Map> listOfMaps, int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.listOfMaps = listOfMaps;
    }

    /**
     * Đọc file immortalMap.txt để nạp thêm các bố cục "bất tử" (id=2)
     * vào danh sách map. Mỗi block trong file phân tách bằng dòng bắt đầu '#'.
     */
    public void readImmortal() {

        try (InputStream is = LoadImage.class.getResourceAsStream("/immortalMap.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            int row = 0;

            // tạo map tạm 8x13 rồi push vào list khi gặp '#'
            int[][] map = new int[8][13]; // listOfMaps.get(level).getMap();

            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                String[] path = line.split(" ");
                if (line.charAt(0) == '#') {
                    row = 0;
                    listOfMaps.add(new Map(map));
                    System.out.println(listOfMaps.size());
                    for (int i = 0; i < 8;i++){
                        for ( int j =0 ; j <13 ;j++){
                            System.out.print(map[i][j] + " ");
                        }
                        System.out.println();
                    }
                    map = new int[8][13];
                } else {
                    for (int i = 0; i < path.length; i++) {
                        map[row][i] = Integer.parseInt(path[i]);
                    }
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Đổi một số ô normal (id=1) sang id đặc biệt bất kỳ, chọn ngẫu nhiên vị trí.
     * @param id       loại gạch đặc biệt muốn gán
     * @param level    level mục tiêu trong listOfMaps (1-based)
     * @param so_luong số ô cần chuyển
     */
    private void creatWithId(int id , int level, int so_luong) {
        Random rand = new Random();
        int x,y;
        int[][] map = listOfMaps.get(level - 1).getMap();

        while (so_luong > 0) {
            x  = rand.nextInt(rows);
            y = rand.nextInt(cols);
            if (map[x][y] == 1) { // chỉ thay khi đang là normal
                map[x][y] = id;
                so_luong--;
            }
        }
    }

    /**
     * Rải toàn bộ loại gạch đặc biệt theo level với số lượng tỉ lệ level.
     * Mục tiêu: có tăng dần nhưng giữ trần để tránh quá tải màn chơi.
     */
    public void creatSpecialBrick() {
        for (int level = 1 ; level <= 20 ; level++) {
            // Mỗi màn tặng tối đa 3 mạng: level 1 -> +1, level 5 -> +2, level 10 -> +3
            int lifeUp = Math.min(3 , 1 + level / 5);

            // Tương tự với ô tặng xu (gold)
            int gold = lifeUp;

            // Bom rơi (cần cân bằng thêm): tăng theo nhiều bậc nhỏ, trần 10
            int fallBomb = Math.min(10 , level / 3 + level / 6 + level / 8 + level / 10 + level / 12 + level / 14 );

            // Nổ khu vực: không vượt quá số bom rơi, trần 5
            int areaBlast = Math.min(5 , fallBomb);

            // Phép đẩy: trần 3
            int push = Math.min(3 , level / 4);

            // Vòng quay may mắn: trần 3
            int wheel = Math.min(3 , level / 5);

            // Áp vào map level tương ứng
            creatWithId(3, level, lifeUp);
            creatWithId(4, level, gold);
            creatWithId(5, level, fallBomb);
            creatWithId(6, level, wheel);
            creatWithId(7, level, areaBlast);
            creatWithId(8, level, push);
        }

    }

}

