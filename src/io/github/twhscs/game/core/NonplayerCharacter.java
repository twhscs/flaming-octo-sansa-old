package io.github.twhscs.game.core;

import io.github.twhscs.game.core.AnimateSprite.Animation;
import io.github.twhscs.game.core.Location.Direction;

public class NonplayerCharacter extends Entity {
  
  private String name;
  
  public NonplayerCharacter(final Location location, final String fileName, final String name) {
    super(location);
    setSprite(new AnimateSprite("npc/spriteset/" + fileName, this));
    this.name = name;
  }
  
  @Override
  protected AnimateSprite getSprite() {
    return (AnimateSprite) super.getSprite();
  }
  
  @Override
  public void move(Direction direction) {
    Location location = getLocation().getRelativeLocation(direction);
    if (getParentMap().isValidLocation(location) && !getSprite().isAnimating()) {
      getSprite().startAnimation(Animation.WALK);
      setLocation(location);
    }
  }
  
}
