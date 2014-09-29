package io.github.twhscs.game;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;

import io.github.twhscs.game.AnimatedSprite.Animation;
import io.github.twhscs.game.Location.Direction;
import io.github.twhscs.game.Player.Action;

public abstract class Entity implements Drawable, Updateable {

  private Location location;

  private Map parentMap;

  private Sprite sprite;

  protected final void setSprite(final Sprite sprite) {
    this.sprite = sprite;
  }
  
  protected final Sprite getSprite() {
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

  public void move(final Direction direction) {
    final Location relativeLocation = location.getRelativeLocation(direction);
    location.setDirection(direction);
    if (parentMap.isValidLocation(relativeLocation)) {
      setLocation(relativeLocation);
      if (this instanceof Player) {
        Player player = (Player) this;
        player.setAction(Action.MOVING);
      }
      if (sprite instanceof AnimatedSprite) {
        AnimatedSprite animatedSprite = (AnimatedSprite) sprite;
        animatedSprite.startAnimation(Animation.WALK);
      }
    }
  }

  @Override
  public void update() {
    if (sprite instanceof AnimatedSprite) {
      ((AnimatedSprite) sprite).update();
    }
  }
  
  @Override
  public void draw(RenderTarget target, RenderStates states) {
    sprite.draw(target, states);
  }
}
