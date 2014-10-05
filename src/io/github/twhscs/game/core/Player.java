package io.github.twhscs.game.core;

import io.github.twhscs.game.core.AnimateSprite.Animation;
import io.github.twhscs.game.core.Location.Direction;


public class Player extends Entity {

  public enum Action {
    NONE, MOVING, TALKING, IN_MENU, IN_INVENTORY
  };

  private Action action = Action.NONE;

  public Player() {
    this(new Location());
  }

  public Player(final Location location) {
    super(location);
    setSprite(new AnimateSprite("spriteset", this));
  }

  public final Action getAction() {
    return action;
  }

  public final void setAction(final Action action) {
    this.action = action;
  }

  @Override
  protected AnimateSprite getSprite() {
    return (AnimateSprite) super.getSprite();
  }

  @Override
  public void update() {
    if (!getSprite().isAnimating() && action == Action.MOVING) {
      action = Action.NONE;
    }
    super.update();
  }

  @Override
  public void move(Direction direction) {
    if (!getSprite().isAnimating()) {
      Location location = getLocation();
      location.setDirection(direction);
      Location newLocation = location.getRelativeLocation(direction);
      System.out.println(getLocation());
      if (getParentMap().isValidLocation(newLocation)) {
        System.out.println("move");
        setLocation(newLocation);
        System.out.println(getLocation());
        getSprite().startAnimation(Animation.WALK);
        action = Action.MOVING;
      }
    }
  }
}
