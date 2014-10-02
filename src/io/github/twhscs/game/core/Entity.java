package io.github.twhscs.game.core;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

import io.github.twhscs.game.core.Location.Direction;

public abstract class Entity implements Drawable {

  private Location location;

  private Map parentMap;

  private BaseSprite sprite;

  protected Entity(final Location location) {
    this.location = location;
  }

  protected final void setSprite(final BaseSprite sprite) {
    this.sprite = sprite;
  }

  protected BaseSprite getSprite() {
    return sprite;
  }

  public final Location getLocation() {
    return location;
  }

  public final void setLocation(final Location location) {
    this.location = location;
  }

  public final Map getParentMap() {
    return parentMap;
  }

  public final void setParentMap(final Map parentMap) {
    this.parentMap = parentMap;
  }

  /**public void move(final Direction direction) {
    Location location = this.location.getRelativeLocation(direction);
    if (parentMap.isValidLocation(location)) {
      this.location = location;
    }
  }*/
  
  public abstract void move(final Direction direction);

  public void update() {
    sprite.update();
  }

  @Override
  public void draw(RenderTarget target, RenderStates states) {
    sprite.draw(target, states);
  }
}
