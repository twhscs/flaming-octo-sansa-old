package io.github.twhscs.game;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

import io.github.twhscs.game.Location.Direction;

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
  
  protected final BaseSprite getSprite() {
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
  
  public boolean canMove(final Direction direction) {
    final Location relativeLocation = location.getRelativeLocation(direction);
    return parentMap.isValidLocation(relativeLocation);
  }

  public void move(final Direction direction) {
    location.setDirection(direction);
  }
  
  public void update() {
    sprite.update();
  }
  
  @Override
  public void draw(RenderTarget target, RenderStates states) {
    sprite.draw(target, states);
  }
}
