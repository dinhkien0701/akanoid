package map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListOfMap {

  private final List<Map> listOfMaps = new ArrayList<>();
  private int Size = 0;

  public ListOfMap() {
    //this.ReadInFile();
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
              Size++;


          } else {
              CreateMap k = new CreateMap(8,13, level);
              int[][] map = k.creatMap(new int[8][13]);
              listOfMaps.add(new Map(map));
              Size++;
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
  }

  private void ReadInFile() {
    String filePath = "src" + File.separator
        + "main" + File.separator
        + "resources" + File.separator
        + "staticMap" + File.separator
        + "map.txt";

    try (BufferedReader br  = new BufferedReader(new FileReader(filePath))) {
      String line;
      int[][] map = new int[8][8];
      int row = 0;
      while ((line = br.readLine()) != null) {
        String[] path = line.split(" ");
        if (line.charAt(0) == '#') {
          if (row > 0) {
            Size++;
            Map e = new Map(map);
            listOfMaps.add(e);
            row = 0;
            map = new int[8][8];
          }
        } else {
          for (int i = 0; i < path.length; i++) {
            map[row][i] = Integer.parseInt(path[i]);
          }
          row++;
        }
      }
    } catch ( IOException e){
      e.printStackTrace();
    }
  }

  public int[][] getMapByCode(int Code) {
    if(Code > Size) {
      return null;
    }
    Code = Math.max(Code - 1, 0);
    return listOfMaps.get(Code).getMap();
  }

}