package map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListOfMap {

  private final List<Map> listOfMaps = new ArrayList<>();
  private int Size = 0;

  public ListOfMap() {
    //this.ReadInFile();
    this.RandomMap();
  }

  private void RandomMap() {

      for(int n = 0; n < 20; n++) {
          CreateMap k = new CreateMap(8,8,20);
          int[][] map = k.creatMap();
          listOfMaps.add(new Map(map));
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