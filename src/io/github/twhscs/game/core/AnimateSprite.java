package io.github.twhscs.game.core;

import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

public class AnimateSprite extends BaseSprite {

  public enum Animation {
    NONE, WALK, WALK_IN_PLACE
  }

  private final float speed = 5;

  private final int maxFrames = 4;

  private int currentFrame = 0;

  private int updates = 0;

  private Animation currentAnimation = Animation.NONE;

  private Vector2f position = new Vector2f(0, 0);

  public AnimateSprite(final String fileName, final Entity parent) {
    super(fileName, new Vector2i(32, 48), parent);
  }

  public final void startAnimation(final Animation animation) {
    this.currentAnimation = animation;
  }

  public final boolean isAnimating() {
    return currentAnimation != Animation.NONE;
  }

  private final IntRect getDefaultTextureRect() {
    final int positionX = currentFrame * getDimensions().x;
    int positionY = 0;
    switch (getParent().getLocation().getDirection()) {
      case NORTH:
        positionY = 144;
        break;
      case SOUTH:
        positionY = 0;
        break;
      case EAST:
        positionY = 96;
        break;
      case WEST:
        positionY = 48;
        break;
      default:
        positionY = 0;
        break;
    }
    return new IntRect(positionX, positionY, getDimensions().x, getDimensions().y);
  }

  public final void updatePosition(float amount) {
    amount = Math.abs(1 - amount);
    position = getParent().getLocation().getPosition();
    switch (getParent().getLocation().getDirection()) {
      case NORTH:
        position = Vector2f.add(position, new Vector2f(0, amount));
        break;
      case SOUTH:
        position = Vector2f.sub(position, new Vector2f(0, amount));
        break;
      case EAST:
        position = Vector2f.sub(position, new Vector2f(amount, 0));
        break;
      case WEST:
        position = Vector2f.add(position, new Vector2f(amount, 0));
        break;
      default:
        break;
    }
  }

  @Override
  public final void update() {
    getSprite().setTextureRect(getDefaultTextureRect());
    if (isAnimating()) {
      updatePosition((updates + (speed * currentFrame)) / (speed * maxFrames));
      updates++;
      if (updates >= speed) {
        updates = 0;
        currentFrame++;
        if (currentFrame >= maxFrames) {
          currentFrame = 0;
          currentAnimation = Animation.NONE;
        }
      }
    } else {
      position = getParent().getLocation().getPosition();
    }
    Vector2f newPosition = Vector2f.mul(position, getDimensions().x);
    newPosition = Vector2f.sub(newPosition, new Vector2f(0, getDimensions().y - getDimensions().x));
    getSprite().setPosition(newPosition);
  }

}
