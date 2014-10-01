package io.github.twhscs.game;


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
}
