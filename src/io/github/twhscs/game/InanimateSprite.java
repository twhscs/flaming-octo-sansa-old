package io.github.twhscs.game;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

public class InanimateSprite extends BaseSprite {
  
  public InanimateSprite(final String fileName, final Vector2i dimensions, final Entity parent) {
    super(fileName, dimensions, parent);
  }
  
  @Override
  public final void update() {
    Vector2f position = getParent().getLocation().getPosition();
    position = Vector2f.mul(position, getDimensions().x);
    getSprite().setPosition(position);
  }
  
}
