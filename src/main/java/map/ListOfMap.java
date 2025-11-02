package map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListOfMap {

  private final List<Map> listOfMaps ;

  public ListOfMap() {
    //this.ReadInFile();
      listOfMaps = new ArrayList<>();
    this.RandomMap();
  }

  private void RandomMap() {
      // Đọc immortal có sẵn

      CreatSpecialBrick d = new CreatSpecialBrick(listOfMaps , 8 , 13);
      d.readImmortal();

      for(int level  = 1; level <= 20; level++) {

          if ( level <= listOfMaps.size()) {
              // Nếu đã được tạo map sẵn
              int[][] map = listOfMaps.get(level - 1).getMap();
              CreateMap k = new CreateMap(8,13, level);
              k.creatMap(map);


          } else {
              CreateMap k = new CreateMap(8,13, level);
              int[][] map = k.creatMap(new int[8][13]);
              listOfMaps.add(new Map(map));
          }

      }
      d.creatSpecialBrick();

      if(true ) {
          int[][]  map = listOfMaps.get(0).getMap();
          for (int i = 0; i < 8; i++) {
              for (int j = 0; j < 13; j++) {
                  System.out.print(map[i][j] + " ");
              }
              System.out.print("\n");
          }
      }
      System.out.println("Các hàng được sinh ra :");
  }

  public int[][] getMapByCode(int Code) {
    if(Code > listOfMaps.size()) {
      return null;
    }
    Code = Math.max(Code - 1, 0);
    return listOfMaps.get(Code).getMap();
  }

}