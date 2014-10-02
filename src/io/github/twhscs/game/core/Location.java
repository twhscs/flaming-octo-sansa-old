package io.github.twhscs.game.core;

import org.jsfml.system.Vector2f;

/**
 * Represents a position and direction.
 * 
 * @author Robert
 *
 */
public class Location implements Comparable<Location> {
  /**
   * The four cardinal directions.
   * 
   * @author Robert
   *
   */
  public enum Direction {
    NORTH, SOUTH, EAST, WEST;
  }

  /**
   * The 2D position.
   */
  private Vector2f position = new Vector2f(0, 0);

  /**
   * The direction.
   */
  private Direction direction;

  /**
   * Create a location at 0, 0.
   */
  public Location() {
    this(0, 0);
  }

  /**
   * Create a location at x, y.
   * 
   * @param posX
   * @param posY
   */
  public Location(final int posX, final int posY) {
    this(new Vector2f(posX, posY), Direction.SOUTH);
  }

  /**
   * 
   * @param position
   * @param direction
   */
  public Location(final Vector2f position, final Direction direction) {
    this.setPosition(position);
    this.setDirection(direction);
  }

  /**
   * 
   * @return
   */
  public final Vector2f getPosition() {
    return position;
  }

  /**
   * 
   * @param position
   */
  public final void setPosition(final Vector2f position) {
    this.position = position;
  }

  /**
   * 
   * @return
   */
  public final Direction getDirection() {
    return direction;
  }

  /**
   * 
   * @param direction
   */
  public final void setDirection(final Direction direction) {
    this.direction = direction;
  }

  /**
   * 
   * @param direction
   * @return
   */
  public final Location getRelativeLocation(final Direction direction) {
    Vector2f newPosition;
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

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((position == null) ? 0 : position.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Location other = (Location) obj;
    if (position == null) {
      if (other.position != null) {
        return false;
      }
    } else if (!position.equals(other.position)) {
      return false;
    }
    return true;
  }

  /**
   * 
   */
  @Override
  public final int compareTo(final Location location) {
    return (int) (position.y - location.getPosition().y);
  }

}
