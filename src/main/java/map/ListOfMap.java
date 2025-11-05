package map;

import java.util.ArrayList;
import java.util.List;

public class ListOfMap {

  private final List<Map> listOfMaps;

  public ListOfMap() {
    listOfMaps = new ArrayList<>();
    this.RandomMap();
  }

  private void RandomMap() {
    // Đọc immortal có sẵn

    CreatSpecialBrick d = new CreatSpecialBrick(listOfMaps, Map.MAP_ROWS, Map.MAP_COLUMNS);
    d.readImmortal();

    for (int level = 1; level <= 20; level++) {

      if (level <= listOfMaps.size()) {
        // Nếu đã được tạo map sẵn
        int[][] map = listOfMaps.get(level - 1).getMap();
        CreateMap k = new CreateMap(Map.MAP_ROWS, Map.MAP_COLUMNS, level);
        k.creatMap(map);
      } else {
        CreateMap k = new CreateMap(Map.MAP_ROWS, Map.MAP_COLUMNS, level);
        int[][] map = k.creatMap(new int[Map.MAP_ROWS][Map.MAP_COLUMNS]);
        listOfMaps.add(new Map(map, level));
      }

    }
    d.creatSpecialBrick();

    // if(true ) {
    // int[][] map = listOfMaps.get(0).getMap();
    // for (int i = 0; i < 8; i++) {
    // for (int j = 0; j < 13; j++) {
    // System.out.print(map[i][j] + " ");
    // }
    // System.out.print("\n");
    // }
    // }
    // System.out.println("Các hàng được sinh ra :");
  }

  public Map getMapByCode(int Code) {
    if (Code > listOfMaps.size()) {
      return null;
    }
    return listOfMaps.get(Code);
  }

}
