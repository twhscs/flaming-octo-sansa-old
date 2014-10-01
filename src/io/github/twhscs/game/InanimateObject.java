package io.github.twhscs.game;

import org.jsfml.system.Vector2i;

public class InanimateObject extends Entity {
  
  public InanimateObject(final Location location, final String fileName) {
    super(location);
    setSprite(new InanimateSprite("object/" + fileName, new Vector2i(32, 32), this));
  }

}
