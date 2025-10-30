package map;

import LoadResource.LoadImage;

import java.io.*;
import java.util.List;
import java.util.Random;

public class CreatSpecialBrick  {
    private List<Map> listOfMaps;
    private int rows;
    private int cols;

    // khơir tạo
    public CreatSpecialBrick( List<Map> listOfMaps, int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.listOfMaps = listOfMaps;
    }

    public void readImmortal() {

        try (InputStream is = LoadImage.class.getResourceAsStream("/staticMap/immortalMap.txt")) {
            assert is != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                int row = 0;

                //lấy bản đồ
                int[][] map = new int[8][13]; //listOfMaps.get(level).getMap();

                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    String[] path = line.split(" ");
                    if (line.charAt(0) == '#') {
                        row = 0;
                        listOfMaps.add(new Map(map));
                        map = new int[8][13];
                    } else {
                        for (int i = 0; i < path.length; i++) {
                            map[row][i] = Integer.parseInt(path[i]);

                        }
                        row++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Hàm kiểm tra viên gạch bốn hướng xung quanh nó bị kẹt không ( gặp tường hoặc gạch bất tử )
    // Trapped (adj) : bị mắc kẹt
    private boolean isBrickTrapped(int[][] map, int r, int c, int rows, int cols) {

        int[] dr = {-1, 1, 0, 0}; // delta row (Lên, Xuống)
        int[] dc = {0, 0, -1, 1}; // delta col (Trái, Phải)

        int trappedSides = 0; // Đếm số mặt bị chặn

        for (int i = 0; i < 4; i++) {
            int nr = r + dr[i]; // Hàng xóm
            int nc = c + dc[i]; // Hàng xóm

            // 1. Kiểm tra "Tường" (biên bản đồ)
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                trappedSides++;
            }
            // 2. Kiểm tra gạch bất tử (2)
            else if (map[nr][nc] == 2) {
                trappedSides++;
            }
        }

        // Nếu cả 4 mặt đều bị chặn, gạch này đã bị "kẹt"
        return trappedSides == 4;
    }


    private void creatWithId(int id , int level, int so_luong) {
        Random rand = new Random();
        int x,y;
        int[][] map = listOfMaps.get(level - 1).getMap();

        while (so_luong > 0) {
            x  = rand.nextInt(rows);
            y = rand.nextInt(cols);
            if (map[x][y] == 1) {
                map[x][y] = id;
                so_luong--;
            }
        }
    }


    public void creatSpecialBrick() {
        for (int level = 1 ; level <= 20 ; level++) {
            //  Mỗi màn tặng tối đa 3 mạng :  từ level 1 +1 mạng , từ level 5  + 2 mạng , từ level 10 + 3 mạng
            int lifeUp = Math.min(3 , 1 + level / 5);

            // Tương tụ với ô tặng xu

            int gold = lifeUp;

            // Bom rơi
            // cần chỉnh lại
            int fallBomb = Math.min(10 , level / 3 + level / 6 + level / 8 + level / 10 + level / 12 + level / 14 );

            // Nổ khu vực
            int areaBlast = Math.min(5 , fallBomb);

            // Phép bổ trợ skill
            int skillUp = Math.min(3 ,level / 4);

            // Vòng quay may mắn

            int wheel = Math.min(3 , level / 5);

            // Khởi tạo
            creatWithId(3, level, lifeUp);
            creatWithId(4, level, gold);
            creatWithId(5, level, fallBomb);
            creatWithId(6, level, wheel);
            creatWithId(7, level, areaBlast);
            creatWithId(8, level, skillUp);
        }

    }

}

