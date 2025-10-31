package object;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import process.PlayingProcess;

public class ImmortalBrick extends Brick {

  public ImmortalBrick(double x, double y, double width, double height, int locateX, int locateY) {

      super(x , y , width, height , locateX, locateY, 8);
      // Ta xây cơ chế nếu giữa hai lần tâng bóng trúng không cách nhau quá 3 lần tâng thì nó sẽ phá hủy
      // Giải thích  lần t  và t + k với  0<= k<= 4 đều chạm trúng đều phá hủy

      // Hp tối đa là 8 , mỗi lần chạm trúng trừ 6  - > về âm hay = 0 thì bị phá hủy
      // 8 - 6 = 2 ; 2 + 4 - 6  = 0

  }


    private Image brickImage = LoadResource.LoadImage.getImage("/image/immortal.png");
    @Override
    public void render(GraphicsContext gc) {
        if (brickImage != null) {
            gc.drawImage(brickImage, getX(), getY(), getWidth(), getHeight());
        } else {
            System.out.println("brickImage is null");
            gc.setFill(Color.RED);
            gc.fillRect(getX(), getY(), getWidth(), getHeight());
        }
    }

    @Override
    public void takeHit() {
        hitPoints -= 6;
    }

    @Override
    public void upHitPoint() {
        // quay trở về tối đa 8 điểm sau mỗi phát nảy
        hitPoints = Math.min(8,  hitPoints + 1);
    }

  @Override
  public void update(PlayingProcess gameManager) {}
}