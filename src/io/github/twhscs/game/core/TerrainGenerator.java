/**
 * 
 */
package io.github.twhscs.game.core;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import io.github.twhscs.game.core.Location.Direction;
import io.github.twhscs.game.core.Map.Tile;
import io.github.twhscs.game.util.Random;


/**
 * @author Robert
 *
 */
public class TerrainGenerator {

  private Map map;

  private int n;
  private int wmult;
  private int hmult;
  private float smoothness;

  private final float waterThreshold = 0.38f;
  private final float sandThreshold = 0.50f;

  // private final float grassThreshold = 1.25f;

  public TerrainGenerator(int n, int wmult, int hmult, float smoothness) {
    this.n = n;
    this.wmult = wmult;
    this.hmult = hmult;
    this.smoothness = smoothness;
  }

  public Map generate() {
    int power = (int) Math.pow(2, n);
    int width = wmult * power + 1;
    int height = hmult * power + 1;

    map = new Map(new Vector2i(width, height), Tile.GRASS);
    float[][] tempMap = new float[width][height];

    int step = power / 2;
    float sum = 0;
    int count = 0;

    float h = .1f;

    for (int i = 0; i < width; i += 2 * step) {
      for (int j = 0; j < height; j += 2 * step) {
        tempMap[i][j] = Random.floatRange(0, 2 * h); // might have to fix
        System.out.println("X: " + tempMap[i][j]);
      }
    }

    // ======
    /*
     * for (int i = 0; i < width; i++) { tempMap[i][0] = Random.floatRange(-5, -2);
     * tempMap[i][height - 1] = Random.floatRange(-5, -2); }
     * 
     * for (int i = 0; i < height; i++) { tempMap[0][i] = Random.floatRange(-5, -2); tempMap[width -
     * 1][0] = Random.floatRange(-5, -2); }
     * 
     * tempMap[(width - 1) / 2][(height - 1) / 2] = (2 * h) + Random.floatRange(4, 5);
     */

    tempMap[0][0] = -5f;
    tempMap[0][height - 1] = -5f;
    tempMap[width - 1][0] = -5f;
    tempMap[width - 1][height - 1] = -5f;

    tempMap[(width - 1) / 2][(height - 1) / 2] = 20f;

    // ======

    while (step > 0) {
      for (int x = step; x < width; x += 2 * step) {
        for (int y = step; y < height; y += 2 * step) {
          sum =
              tempMap[x - step][y - step] + tempMap[x - step][y + step]
                  + tempMap[x + step][y - step] + tempMap[x + step][y + step];
          tempMap[x][y] = sum / 4 + Random.floatRange(-h, h);
        }
      }

      for (int x = 0; x < width; x += step) {
        for (int y = step * (1 - (x / step) % 2); y < height; y += 2 * step) {
          sum = 0;
          count = 0;
          if (x - step >= 0) {
            sum += tempMap[x - step][y];
            count++;
          }
          if (x + step < width) {
            sum += tempMap[x + step][y];
            count++;
          }
          if (y - step >= 0) {
            sum += tempMap[x][y - step];
            count++;
          }
          if (y + step < height) {
            sum += tempMap[x][y + step];
            count++;
          }
          if (count > 0) {
            tempMap[x][y] = sum / count + Random.floatRange(-h, h);
          } else {
            tempMap[x][y] = 0;
          }
        }
      }
      h /= smoothness;
      step /= 2;
    }

    float max = Float.MIN_VALUE;
    float min = Float.MAX_VALUE;
    for (float[] row : tempMap) {
      for (float d : row) {
        if (d > max) {
          max = d;
        }
        if (d < min) {
          min = d;
        }
      }
    }

    for (int row = 0; row < width; row++) {
      for (int col = 0; col < height; col++) {
        float val = (tempMap[row][col] - min) / (max - min);
        // System.out.println(val);
        Tile t = null;
        if (val < waterThreshold) {
          t = Tile.WATER;
        } else if (val < sandThreshold) {
          t = Tile.SAND;
        } else {
          t = Tile.GRASS;
        }
        map.setTile(new Location(new Vector2f(row, col), Direction.SOUTH), t);
      }
    }
    for (int abc = 0; abc < 10; abc++) {
      for (int row = 1; row < width - 1; row++) {
        for (int col = 1; col < height - 1; col++) {
          final Location location = new Location(new Vector2f(row, col), Direction.SOUTH);
          System.out.println(location);
          Tile t = map.getTile(location);
          System.out.println("A");
          if (t != Tile.SAND
              && map.getTile(location.getRelativeLocation(Direction.NORTH)) == Tile.SAND
              && map.getTile(location.getRelativeLocation(Direction.SOUTH)) == Tile.SAND
              && map.getTile(location.getRelativeLocation(Direction.EAST)) == Tile.SAND
              && map.getTile(location.getRelativeLocation(Direction.WEST)) == Tile.SAND) {
            map.setTile(location, Tile.SAND);
          } else if (t != Tile.GRASS
              && map.getTile(location.getRelativeLocation(Direction.NORTH)) == Tile.GRASS
              && map.getTile(location.getRelativeLocation(Direction.SOUTH)) == Tile.GRASS
              && map.getTile(location.getRelativeLocation(Direction.EAST)) == Tile.GRASS
              && map.getTile(location.getRelativeLocation(Direction.WEST)) == Tile.GRASS) {
            map.setTile(location, Tile.GRASS);
          } else if (t != Tile.WATER
              && map.getTile(location.getRelativeLocation(Direction.NORTH)) == Tile.WATER
              && map.getTile(location.getRelativeLocation(Direction.SOUTH)) == Tile.WATER
              && map.getTile(location.getRelativeLocation(Direction.EAST)) == Tile.WATER
              && map.getTile(location.getRelativeLocation(Direction.WEST)) == Tile.WATER) {
            map.setTile(location, Tile.WATER);
          }

          if (t == Tile.GRASS) {
            int counter = 0;
            if (map.getTile(location.getRelativeLocation(Direction.NORTH)) != Tile.GRASS) {
              counter++;
            }
            if (map.getTile(location.getRelativeLocation(Direction.SOUTH)) != Tile.GRASS) {
              counter++;
            }
            if (map.getTile(location.getRelativeLocation(Direction.EAST)) != Tile.GRASS) {
              counter++;
            }
            if (map.getTile(location.getRelativeLocation(Direction.WEST)) != Tile.GRASS) {
              counter++;
            }
            if (counter >= 3) {
              map.setTile(location, Tile.SAND);
            }
          } else if (t == Tile.SAND) {
            int counter = 0;
            if (map.getTile(location.getRelativeLocation(Direction.NORTH)) == Tile.WATER) {
              counter++;
            }
            if (map.getTile(location.getRelativeLocation(Direction.SOUTH)) == Tile.WATER) {
              counter++;
            }
            if (map.getTile(location.getRelativeLocation(Direction.EAST)) == Tile.WATER) {
              counter++;
            }
            if (map.getTile(location.getRelativeLocation(Direction.WEST)) == Tile.WATER) {
              counter++;
            }
            if (counter >= 3) {
              map.setTile(location, Tile.WATER);
            }
          }
        }
      }
    }
    return this.map;
  }
}
