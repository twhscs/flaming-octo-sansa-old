package io.github.twhscs.game.core;

import org.jsfml.system.Vector2i;

import io.github.twhscs.game.core.Location.Direction;

public class InanimateObject extends Entity {

  public InanimateObject(final Location location, final String fileName) {
    this(location, fileName, new Vector2i(32, 32));
  }

  public InanimateObject(final Location location, final String fileName, final Vector2i dimensions) {
    super(location);
    setSprite(new InanimateSprite("object/" + fileName, dimensions, this));
  }

  @Override
  public void move(Direction direction) {
    Location location = getLocation().getRelativeLocation(direction);
    if (getParentMap().isValidLocation(location)) {
      setLocation(location);
    }
  }

}
