package io.github.twhscs.game;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2i;

import io.github.twhscs.game.util.Resource;

public abstract class BaseSprite implements Drawable {
  
  private Entity parent = null;
  
  private Vector2i dimensions = new Vector2i(0, 0);
  
  private Sprite sprite = new Sprite();
  
  protected BaseSprite(final String fileName, final Vector2i dimensions, final Entity parent) {
    Texture texture = Resource.loadTexture(fileName);
    sprite.setTexture(texture);
    this.dimensions = dimensions;
    this.parent = parent;
  }
  
  protected final Entity getParent() {
    return parent;
  }
  
  protected final Sprite getSprite() {
    return sprite;
  }
  
  protected final Vector2i getDimensions() {
    return dimensions;
  }
  
  public abstract void update();
  
  @Override
  public final void draw(final RenderTarget target, final RenderStates states) {
    sprite.draw(target, states);
  }
  
}
