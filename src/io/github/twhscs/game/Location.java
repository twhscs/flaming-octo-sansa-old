package io.github.twhscs.game;

import org.jsfml.system.Vector2f;

public class Location implements Comparable<Location> {

  public enum Direction {
    NORTH, SOUTH, EAST, WEST;
  }

  private Vector2f position = new Vector2f(0, 0);

  private Direction direction;

  public Location() {
    this(0, 0);
  }
  
  public Location(final int posX, final int posY) {
    this(new Vector2f(posX, posY), Direction.SOUTH);
  }

  public Location(final Vector2f position, final Direction direction) {
    this.setPosition(position);
    this.setDirection(direction);
  }

  public final Vector2f getPosition() {
    return position;
  }

  public final void setPosition(final Vector2f position) {
    this.position = position;
  }

  public final Direction getDirection() {
    return direction;
  }

  public final void setDirection(final Direction direction) {
    this.direction = direction;
  }

  public final Location getRelativeLocation(final Direction direction) {
    Vector2f newPosition = null;
    switch (direction) {
      case NORTH:
        newPosition = Vector2f.sub(position, new Vector2f(0, 1));
        break;
      case SOUTH:
        newPosition = Vector2f.add(position, new Vector2f(0, 1));
        break;
      case EAST:
        newPosition = Vector2f.add(position, new Vector2f(1, 0));
        break;
      case WEST:
        newPosition = Vector2f.sub(position, new Vector2f(1, 0));
        break;
      default:
        newPosition = new Vector2f(0, 0);
        break;
    }
    return new Location(newPosition, direction);
  }

  public final boolean equals(final Location location) {
    return position.equals(location.getPosition());
  }

  @Override
  public final int compareTo(final Location location) {
    return (int) (position.y - location.getPosition().y);
  }
  
}
