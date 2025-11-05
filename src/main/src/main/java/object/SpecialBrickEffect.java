package object;

import javafx.scene.shape.Rectangle;
import map.ListOfMap;
import process.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class SpecialBrickEffect {

    public static int areaBlast(List<Brick> bricks,
                                List<Pair<Double, Double>> pairList,
                                double brickW,
                                double brickH,
                                int frameBlast) {
        if (bricks.isEmpty() || frameBlast < 8) {
            return frameBlast;
        }

        frameBlast = 1;
        List<Pair<Double, Double>> np = new ArrayList<>();
        double dist = brickH * brickH + brickW * brickW + 6;

        Iterator<Pair<Double, Double>> it = pairList.iterator();
        while (it.hasNext()) {
            Pair<Double, Double> p = it.next();
            double x = p.first;
            double y = p.second;

            for (Brick b : bricks) {
                if (b.isDestroyed()) {
                    continue;
                }
                double k_dist = (x - b.getX()) * (x - b.getX()) + (y - b.getY()) * (y - b.getY());
                if (k_dist <= dist) {
                    b.takeHit();
                    if (b.isDestroyed() && b instanceof PushBrick) {
                        np.add(new Pair<>(b.getX(), b.getY()));
                    }
                }
            }
            it.remove();
        }
        for (Pair<Double, Double> p : np) {
            pairList.add(p);
        }

        return frameBlast;
    }

    public static void doPushBrick(List<Brick> bricks, Rectangle map, double brickH) {
        int maxLocateY = 0;
        for (Brick b : bricks) {
            maxLocateY = Math.max(maxLocateY, (int) b.getY());
        }

        if (maxLocateY < map.getY() + 50 + brickH) {
            return; // không thể đẩy hết gạch ra khỏi màn chơi
        }

        for (Brick b : bricks) {
            b.setY(b.getY() - brickH);
        }
    }

    public static void selectMoveBrick(List<Brick> bricks,
                                       Rectangle map,
                                       double minLocateY,
                                       double brickW,
                                       double brickH) {
        Brick[][] arrBrick = new Brick[8][13];
        Random rand = new Random();

        if (bricks.isEmpty()) {
            return;
        }

        int[] hasMoveBrick = new int[8];
        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick b = it.next();
            double bx = b.getX() - (map.getX() + 30);
            double by = b.getY() - (minLocateY);

            arrBrick[(int) Math.round(by / brickH)][(int) Math.round(bx / brickW)] = b;

            if (b.movedist != 0) {
                hasMoveBrick[(int) Math.round(by / brickH)] = (int) Math.round(bx / brickW) + 1;
            }
        }

        for (int i = 0; i < 8; i++) {
            if (hasMoveBrick[i] > 0) {
                int k_l = 0;
                int k_r = 0;
                Brick b = arrBrick[i][hasMoveBrick[i] - 1];

                for (int k = hasMoveBrick[i] - 2; k >= 0; k--) {
                    if (arrBrick[i][k] == null) {
                        k_l++;
                    } else break;
                }

                for (int k = hasMoveBrick[i]; k < 13; k++) {
                    if (arrBrick[i][k] == null) {
                        k_r++;
                    } else break;
                }

                b.left = (int) (map.getX() + 30 + (hasMoveBrick[i] - 1 - k_l) * brickW);
                b.right = (int) (map.getX() + 30 + (hasMoveBrick[i] - 1 + k_r) * brickW);
                continue;
            }

            int pick = -1;
            int dist = 0;
            int l = 0;
            int r = 0;

            for (int j = 0; j < 13; j++) {
                if (arrBrick[i][j] == null) {
                    continue;
                }
                int k_dist = 1;
                int k_l = 0;
                int k_r = 0;

                for (int k = j - 1; k >= 0; k--) {
                    if (arrBrick[i][k] == null) {
                        k_dist++;
                        k_l++;
                    } else break;
                }

                for (int k = j + 1; k < 13; k++) {
                    if (arrBrick[i][k] == null) {
                        k_dist++;
                        k_r++;
                    } else break;
                }

                if (k_dist > dist) {
                    pick = j;
                    dist = k_dist;
                    l = k_l;
                    r = k_r;
                }
            }

            if (pick != -1 && dist > 1) {
                Brick b = arrBrick[i][pick];
                b.movedist = 3 * (rand.nextInt(3) - 1);
                b.left = (int) (map.getX() + 30 + (pick - l) * brickW);
                b.right = (int) (map.getX() + 30 + (pick + r) * brickW);
            }
        }
    }

    public static class RandomRowResult {
        public final int minLocateY;
        public final int randRowCount;
        public final int help;

        public RandomRowResult(int minLocateY, int randRowCount, int help) {
            this.minLocateY = minLocateY;
            this.randRowCount = randRowCount;
            this.help = help;
        }
    }

    public static RandomRowResult randomRow(List<Brick> bricks,
                                            Rectangle map,
                                            double brickW,
                                            double brickH,
                                            int minLocateY,
                                            int currentMap,
                                            int randRowCount,
                                            ListOfMap LM,
                                            boolean isRunning,
                                            boolean allowPushBrick,
                                            int help) {
        if (!isRunning) {
            return new RandomRowResult(minLocateY, randRowCount, help);
        }

        if (50 + brickH + map.getY() > minLocateY) {
            return new RandomRowResult(minLocateY, randRowCount, help);
        }

        if (currentMap >= 14) {
            randRowCount = 1000000;
        } else {
            randRowCount--;
        }

        Random rand = new Random();
        int[][] arr = LM.getMapByCode(rand.nextInt(5) + 7);
        int i = rand.nextInt(arr.length);

        double by;
        if (bricks.isEmpty()) {
            by = 50 + map.getY();
        } else {
            by = minLocateY - brickH;
            while (by - brickH >= 50 + map.getY()) {
                by -= brickH;
            }
        }

        if (help == 0) {
            arr[i][rand.nextInt(13)] = 3;
            help++;
        } else {
            arr[i][rand.nextInt(13)] = 8;
            help = 1;
        }

        for (int j = 0; j < arr[i].length; j++) {
            if (arr[i][j] == 0) {
                continue;
            }

            double bx = 30 + j * brickW + map.getX();

            if (arr[i][j] == 1) {
                bricks.add(new NormalBrick(bx, by, brickW - 6, brickH - 6, i, j));
            } else if (arr[i][j] == 2) {
                bricks.add(new ImmortalBrick(bx, by, brickW - 6, brickH - 6, i, j));
            } else if (arr[i][j] == 3) {
                bricks.add(new LifeUpBrick(bx, by, brickW - 6, brickH - 6, i, j));
            } else if (arr[i][j] == 4) {
                bricks.add(new GoldBrick(bx, by, brickW - 6, brickH - 6, i, j));
            } else if (arr[i][j] == 5) {
                bricks.add(new FallBombBrick(bx, by, brickW - 6, brickH - 6, i, j));
            } else if (arr[i][j] == 6) {
                bricks.add(new AreaBlastBrick(bx, by, brickW - 6, brickH - 6, i, j));
            } else if (arr[i][j] == 7) {
                bricks.add(new LuckyWheelBrick(bx, by, brickW - 6, brickH - 6, i, j));
            } else if (arr[i][j] == 8 && allowPushBrick) {
                bricks.add(new PushBrick(bx, by, brickW - 6, brickH - 6, i, j));
            }
        }

        minLocateY = (int) by;
        return new RandomRowResult(minLocateY, randRowCount, help);
    }
}

