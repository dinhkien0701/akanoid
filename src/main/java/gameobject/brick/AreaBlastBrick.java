package gameobject.brick;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import process.PlayingProcess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AreaBlastBrick extends Brick {
    private final List<Pair<Double,Double>> pairList = new  ArrayList<>();
    private int frameBlast = 1;

    public AreaBlastBrick(double x, double y, int locateX, int locateY) {
        super(x , y , locateX, locateY, 1);
    }


    private final Image brickImage = LoadImage.getImage("/image/areaBlast.png");
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
        hitPoints --;
    }

    @Override
    public void update(PlayingProcess pp) {
        if(isDestroyed()) {
            areaBlast(pp);
        }
    }

    private void areaBlast(PlayingProcess pp) {
        // Nếu không có gạch thì không thể chọn nổ xung quanh được
        if (pp.getBricks().isEmpty() || frameBlast < 8) {
            return;
        }

        frameBlast = 1;
        List<Pair<Double,Double>> np = new ArrayList<>();
        // Lấy minLocateY
        double dist = Brick.BRICK_HEIGHT * BRICK_HEIGHT + Brick.BRICK_WIDTH * BRICK_WIDTH ;
        Iterator<Pair<Double,Double>> it = pairList.iterator();
        while (it.hasNext()) {
            Pair<Double,Double> p = it.next();
            double x = p.getKey();
            double y = p.getValue();

            for (Brick b : pp.getBricks()) {

                if (b.isDestroyed()) {
                    continue;
                }

                double k_dist = (x - b.getX()) * (x - b.getX()) + (y - b.getY()) * ( y- b.getY());
                if (k_dist <= dist) {
                    b.takeHit();
                    if (b.isDestroyed()) {
                        np.add(new Pair<>(b.getX(), b.getY()));
                    }
                }
            }
            it.remove();
        }
        pairList.addAll(np);

    }
}
