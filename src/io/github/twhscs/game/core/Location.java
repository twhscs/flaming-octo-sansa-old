package io.github.twhscs.game.core;

import org.jsfml.system.Vector2f;

/**
 * A 2D location on the map represented by a position (x, y) and a direction.
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
   * The position (x, y).
   */
  private Vector2f position = new Vector2f(0, 0);

  /**
   * The direction.
   */
  private Direction direction = Direction.SOUTH;

  /**
   * Create a new location defaulting to position (0, 0) and facing south.
   */
  public Location() {
    this(new Vector2f(0, 0), Direction.SOUTH);
  }

  /**
   * Create a new location with the specified position and direction.
   * 
   * @param position The new position.
   * @param direction The new direction.
   */
  public Location(final Vector2f position, final Direction direction) {
    this.position = position;
    this.direction = direction;
  }

  /**
   * Get the position of the current location.
   * 
   * @return The position as a vector.
   */
  public final Vector2f getPosition() {
    return position;
  }

  /**
   * Update the location's position.
   * 
   * @param position The new position.
   */
  public final void setPosition(final Vector2f position) {
    this.position = position;
  }

  /**
   * Get the direction of the current location.
   * 
   * @return The direction.
   */
  public final Direction getDirection() {
    return direction;
  }

  /**
   * Update the location's direction.
   * 
   * @param direction The new direction.
   */
  public final void setDirection(final Direction direction) {
    this.direction = direction;
  }

  /**
   * Get the location adjacent to the current location in the specified direction.
   * 
   * @param direction The direction of the new location relative to the current location.
   * @return The relative location.
   */
  public final Location getRelativeLocation(final Direction direction) {
    Vector2f position = this.position;
    switch (direction) {
      case NORTH:
        position = Vector2f.sub(position, new Vector2f(0, 1));
        break;
      case SOUTH:
        position = Vector2f.add(position, new Vector2f(0, 1));
        break;
      case EAST:
        position = Vector2f.add(position, new Vector2f(1, 0));
        break;
      case WEST:
        position = Vector2f.sub(position, new Vector2f(1, 0));
        break;
      default:
        break;
    }
    return new Location(position, direction);
  }

  /**
   * Get the current location as a string.
   */
  @Override
  public String toString() {
    return "Location [position=(" + position.x + ", " + position.y + "), direction=" + direction
        + "]";
  }

  /**
   * Compare two locations based on their position's y values.
   */
  @Override
  public final int compareTo(final Location location) {
    return (int) (position.y - location.getPosition().y);
  }

  /**
   * This must be overridden when changing equals. This was generated by eclipse.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((position == null) ? 0 : position.hashCode());
    return result;
  }

  /**
   * Compare two locations based on position only, not direction. This was generated by eclipse.
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
    //System.out.println(this);
    //System.out.println(other);
    if (position == null) {
      if (other.position != null) {
        return false;
      }
    } else if (!position.equals(other.position)) {
      return false;
    }
    return true;
  }
}
