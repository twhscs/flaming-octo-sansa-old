package io.github.twhscs.game;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import io.github.twhscs.game.util.Resource;

import java.util.ArrayList;
import java.util.Arrays;

public class Map implements Drawable, Updateable {

  public enum Tile {
    WATER, SAND, GRASS;
  }

  private Vector2i dimensions = new Vector2i(0, 0);

  private Tile[][] tiles;

  private final int tileSize = 32;

  private final Texture tileSheet = Resource.loadTexture("terrain");

  private final ArrayList<Entity> entities = new ArrayList<Entity>();

  public Map() {
    this(new Vector2i(10, 10), Tile.GRASS);
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

  private final Tile getTile(final Location location) {
    final Vector2f position = location.getPosition();
    return tiles[(int) position.x][(int) position.y];
  }

  private final Vector2f getTileTexture(final Tile tile) {
    switch (tile) {
      case WATER:
        return new Vector2f(480, 544);
      case SAND:
        return new Vector2f(576, 352);
      case GRASS:
        return new Vector2f(448, 352);
      default:
        return new Vector2f(0, 0);
    }
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

  public final boolean isValidLocation(final Location location) {
    final Vector2f position = location.getPosition();
    final boolean aboveMin = (position.x >= 0 && position.y >= 0);
    final boolean belowMax = (position.x < dimensions.x && position.y < dimensions.y);
    return (aboveMin && belowMax && getTileCollision(getTile(location)));
  }

  public final void addEntity(final Entity entity) {
    if (isValidLocation(entity.getLocation())) {
      entity.setParentMap(this);
      entities.add(entity);
    }
  }

  public final Entity getEntity(final Location location) {
    for (Entity entity : entities) {
      if (entity.getLocation().equals(location)) {
        return entity;
      }
    }
    return null;
  }

  @Override
  public final void update() {
    for (Entity entity : entities) {
      entity.update();
    }
  }

  @Override
  public final void draw(final RenderTarget target, final RenderStates states) {
    VertexArray vertexArray = new VertexArray(PrimitiveType.QUADS);
    for (int ti = 0; ti < dimensions.x; ti++) {
      for (int tj = 0; tj < dimensions.y; tj++) {
        final Tile tile = tiles[ti][tj];

        final Vector2f baseTexture = getTileTexture(tile);
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
