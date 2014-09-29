package io.github.twhscs.game;

import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import io.github.twhscs.game.util.Resource;

public class AnimatedSprite extends Sprite implements Updateable {

  public enum Animation {
    NONE, WALK, WALK_IN_PLACE
  }

  private final float speed = 5;

  private int frame = 0;

  private int updates = 0;

  private Vector2i dimensions;

  private Entity parent;

  private Animation animation = Animation.NONE;

  private final int maxFrames = 4;
  
  private Vector2f position;

  public AnimatedSprite(final String filename, final Entity parent) {
    this(filename, parent, new Vector2i(32, 48));
  }

  public AnimatedSprite(final String filename, final Entity parent, final Vector2i dimensions) {
    setTexture(Resource.loadTexture(filename));
    this.dimensions = dimensions;
    this.parent = parent;
  }

  public final void startAnimation(final Animation animation) {
    this.animation = animation;
  }

  public final boolean isAnimating() {
    return animation != Animation.NONE;
  }

  private final IntRect getDefaultTextureRect() {
    final int positionX = frame * dimensions.x;
    int positionY = 0;
    switch (parent.getLocation().getDirection()) {
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
    return new IntRect(positionX, positionY, dimensions.x, dimensions.y);
  }

  public final void updatePosition(final float amount) {
    switch (parent.getLocation().getDirection()) {
      case NORTH:
        position = Vector2f.sub(position, new Vector2f(0, amount));
        break;
      case SOUTH:
        position = Vector2f.add(position, new Vector2f(0, amount));
        break;
      case EAST:
        position = Vector2f.add(position, new Vector2f(amount, 0));
        break;
      case WEST:
        position = Vector2f.sub(position, new Vector2f(amount, 0));
        break;
      default:
        break;
    }
  }

  @Override
  public final void update() {
    setTextureRect(getDefaultTextureRect());
    if (isAnimating()) {
      if (updates >= speed) {
        updates = 0;
        frame++;
        if (frame >= maxFrames) {
          frame = 0;
          animation = Animation.NONE;
        }
      }
      if (animation == Animation.WALK) {
        final float step = 1 / (maxFrames * speed);
        updatePosition(step);
      }
      updates++;
    } else {
      position = parent.getLocation().getPosition();
    }
    Vector2f newPosition = Vector2f.mul(position, dimensions.x);
    newPosition = Vector2f.sub(newPosition, new Vector2f(0, dimensions.y - dimensions.x));
    setPosition(newPosition);
  }

}
