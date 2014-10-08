package io.github.twhscs.game.core;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import io.github.twhscs.game.core.Location.Direction;
import io.github.twhscs.game.util.Random;
import io.github.twhscs.game.util.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

class Map implements Drawable {

  public enum Tile {
    WATER, SAND, GRASS;
  }

  private Vector2i dimensions = new Vector2i(0, 0);

  private Tile[][] tiles;

  private final int tileSize = 32;

  private final Texture tileSheet = Resource.loadTexture("tileset");

  private final ArrayList<Entity> entities = new ArrayList<Entity>();

  public Map() {
    this(new Vector2i(10, 10), Tile.SAND);
  }

  public Map(final Vector2i dimensions, final Tile defaultTile) {
    this.dimensions = dimensions;
    tiles = new Tile[dimensions.x][dimensions.y];
    fillMap(defaultTile);
  }

  private final void fillMap(final Tile tile) {
    for (Tile[] row : tiles) {
      Arrays.fill(row, tile);
    }
  }

  public final Tile getTile(final Location location) {
    final Vector2f position = location.getPosition();
    return tiles[(int) position.x][(int) position.y];
  }

  public final void setTile(final Location location, final Tile tile) {
    final Vector2f position = location.getPosition();
    tiles[(int) position.x][(int) position.y] = tile;
  }

  private final Vector2f getTileTexture(final Tile tile, final Location tileLocation) {
    switch (tile) {
      case WATER:
        /*
         * int random = Random.intRange(0, 2); switch (random) { case 0: return new Vector2f(672,
         * 352); case 1: return new Vector2f(704, 352); case 2: return new Vector2f(736, 352); }
         * break;
         */
        return new Vector2f((tileLocation.getPosition().x * tileLocation.getPosition().y) % 3 * 32 + 864, 160);
      case SAND:
        return new Vector2f((tileLocation.getPosition().x * tileLocation.getPosition().y) % 3 * 32 + 576, 352);
      case GRASS:
        return new Vector2f((tileLocation.getPosition().x * tileLocation.getPosition().y) % 2 * 32 + 32, 352);
    }
    return null;
  }

  private final boolean getTileCollision(final Tile tile) {
    switch (tile) {
      case WATER:
        return false;
      case SAND:
        return true;
      case GRASS:
        return true;
      default:
        return false;
    }
  }

  final boolean isValidLocation(final Location location) {
    if (location != null) {
      final Vector2f position = location.getPosition();
      final boolean aboveMin = (position.x >= 0 && position.y >= 0);
      final boolean belowMax = (position.x < dimensions.x && position.y < dimensions.y);
      final boolean open = getEntity(location) == null;
      return (aboveMin && belowMax && open && getTileCollision(getTile(location)));
    } else {
      return false;
    }
  }

  final void addEntity(final Entity entity) {
    if (isValidLocation(entity.getLocation())) {
      entity.setParentMap(this);
      entities.add(entity);
    }
  }

  private final Entity getEntity(final Location location) {
    for (Entity entity : entities) {
      if (entity.getLocation().equals(location)) {
        System.out.println("T");
        return entity;
      }
    }
    return null;
  }

  public Location getRandomValidLocation() {
    /*
     * Location location = null; while (!isValidLocation(location)) { location =
     * getRandomLocation(); } return location;
     */
    int x = dimensions.x / 2;
    int y = dimensions.y / 2;
    return new Location(new Vector2f(x, y), Direction.SOUTH);
  }

  private Location getRandomLocation() {
    int randomX = Random.intRange(0, dimensions.x);
    int randomY = Random.intRange(0, dimensions.y);
    return new Location(new Vector2f(randomX, randomY), Direction.SOUTH);
  }

  final void update() {
    for (Entity entity : entities) {
      entity.update();
    }
    Collections.sort(entities, new Comparator<Entity>() {
      @Override
      public int compare(final Entity entity1, final Entity entity2) {
        return entity1.getLocation().compareTo(entity2.getLocation());
      }
    });
  }

  @Override
  public final void draw(final RenderTarget target, final RenderStates states) {
    VertexArray vertexArray = new VertexArray(PrimitiveType.QUADS);
    for (int ti = 0; ti < dimensions.x; ti++) {
      for (int tj = 0; tj < dimensions.y; tj++) {
        final Tile tile = tiles[ti][tj];
        final Location tileLocation = new Location(new Vector2f(ti, tj), Direction.SOUTH);

        Vector2f baseTexture = getTileTexture(tile, tileLocation);
        //

        if (tile == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.WATER
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.WATER
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.WATER
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.WATER) {
          baseTexture = new Vector2f(672, 224);
        } else if (tile == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.WATER
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.WATER
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.SAND) {
          baseTexture = new Vector2f(736, 256);
        } else if (tile == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.WATER
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.WATER
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.SAND) {
          baseTexture = new Vector2f(672, 256);
        } else if (tile == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.WATER
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.WATER
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.SAND) {
          baseTexture = new Vector2f(736, 320);
        } else if (tile == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.WATER
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.WATER
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.SAND) {
          baseTexture = new Vector2f(672, 320);
        } else if (tile == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.WATER
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.SAND) {
          baseTexture = new Vector2f(704, 256);
        } else if (tile == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.WATER
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.SAND) {
          baseTexture = new Vector2f(704, 320);
        } else if (tile == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.WATER) {
          baseTexture = new Vector2f(736, 288);
        } else if (tile == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.WATER
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.SAND) {
          baseTexture = new Vector2f(672, 288);
        } else if (tile == Tile.SAND
            && getTile(new Location(Vector2f.add(tileLocation.getPosition(), new Vector2f(1, 1)),
                Direction.SOUTH)) == Tile.WATER) {
          baseTexture = new Vector2f(704, 192);
        } else if (tile == Tile.SAND
            && getTile(new Location(Vector2f.add(tileLocation.getPosition(), new Vector2f(-1, 1)),
                Direction.SOUTH)) == Tile.WATER) {
          baseTexture = new Vector2f(736, 192);
        } else if (tile == Tile.SAND
            && getTile(new Location(Vector2f.add(tileLocation.getPosition(), new Vector2f(1, -1)),
                Direction.SOUTH)) == Tile.WATER) {
          baseTexture = new Vector2f(704, 224);
        } else if (tile == Tile.SAND
            && getTile(new Location(Vector2f.add(tileLocation.getPosition(), new Vector2f(-1, -1)),
                Direction.SOUTH)) == Tile.WATER) {
          baseTexture = new Vector2f(736, 224);
        } else if (tile == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.GRASS) {
          baseTexture = new Vector2f(128, 800);
        } else if (tile == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.GRASS) {
          baseTexture = new Vector2f(128, 832);
        } else if (tile == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.GRASS) {
          baseTexture = new Vector2f(128, 864);
        } else if (tile == Tile.GRASS
          && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.SAND
          && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.GRASS
          && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.GRASS
          && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.GRASS) {
        baseTexture = new Vector2f(150, 800);
        } else if (tile == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.SAND) {
          baseTexture = new Vector2f(182, 800);
        } else if (tile == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.GRASS) {
          baseTexture = new Vector2f(150, 864);
        } else if (tile == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.GRASS) {
          baseTexture = new Vector2f(128, 864);
        } else if (tile == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.NORTH)) == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.SOUTH)) == Tile.SAND
            && getTile(tileLocation.getRelativeLocation(Direction.WEST)) == Tile.GRASS
            && getTile(tileLocation.getRelativeLocation(Direction.EAST)) == Tile.SAND) {
          baseTexture = new Vector2f(182, 864);
        } else if (tile == Tile.GRASS
            && getTile(new Location(Vector2f.add(tileLocation.getPosition(), new Vector2f(1, 1)),
                Direction.SOUTH)) == Tile.SAND) {
          baseTexture = new Vector2f(192, 736);
        } else if (tile == Tile.GRASS
            && getTile(new Location(Vector2f.add(tileLocation.getPosition(), new Vector2f(-1, 1)),
                Direction.SOUTH)) == Tile.SAND) {
          baseTexture = new Vector2f(224, 736);
        } else if (tile == Tile.GRASS
            && getTile(new Location(Vector2f.add(tileLocation.getPosition(), new Vector2f(1, -1)),
                Direction.SOUTH)) == Tile.SAND) {
          baseTexture = new Vector2f(192, 768);
        } else if (tile == Tile.GRASS
            && getTile(new Location(Vector2f.add(tileLocation.getPosition(), new Vector2f(-1, -1)),
                Direction.SOUTH)) == Tile.SAND) {
          baseTexture = new Vector2f(224, 768);
        }


        final Vector2f basePosition = new Vector2f(ti * tileSize, tj * tileSize);
        final Vector2f tileSizeVector = new Vector2f(tileSize, tileSize);

        final Vector2f topLeftPosition = basePosition;
        final Vector2f topLeftTexture = baseTexture;
        vertexArray.add(new Vertex(topLeftPosition, topLeftTexture));

        final Vector2f botLeftPosition = Vector2f.add(basePosition, new Vector2f(0, tileSize));
        final Vector2f botLeftTexture = Vector2f.add(baseTexture, new Vector2f(0, tileSize));
        vertexArray.add(new Vertex(botLeftPosition, botLeftTexture));

        final Vector2f botRightPosition = Vector2f.add(basePosition, tileSizeVector);
        final Vector2f botRightTexture = Vector2f.add(baseTexture, tileSizeVector);
        vertexArray.add(new Vertex(botRightPosition, botRightTexture));

        final Vector2f topRightPosition = Vector2f.add(basePosition, new Vector2f(tileSize, 0));
        final Vector2f topRightTexture = Vector2f.add(baseTexture, new Vector2f(tileSize, 0));
        vertexArray.add(new Vertex(topRightPosition, topRightTexture));
      }
    }
    RenderStates newStates = new RenderStates(tileSheet);
    vertexArray.draw(target, newStates);
    for (Entity entity : entities) {
      entity.draw(target, states);
    }
  }
}
