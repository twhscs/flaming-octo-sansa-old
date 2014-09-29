package io.github.twhscs.game;

import io.github.twhscs.game.Location.Direction;

public class Player extends Entity {

  public enum Action {
    NONE, MOVING, TALKING, IN_MENU, IN_INVENTORY
  };

  private Action action = Action.NONE;

  public Player() {
    this(new Location());
  }

  public Player(final Location location) {
    setLocation(location);
    setSprite(new AnimatedSprite("player", this));
  }

  public final Action getAction() {
    return action;
  }

  public final void setAction(final Action action) {
    this.action = action;
  }

  @Override
  public final void update() {
    if (action == Action.MOVING && !((AnimatedSprite) getSprite()).isAnimating()) {
      action = Action.NONE;
    }
    super.update();
  }

  @Override
  public final void move(final Direction direction) {
    if (action == Action.NONE) {
      super.move(direction);
    }
  }
}
