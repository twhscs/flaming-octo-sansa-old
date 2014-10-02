package io.github.twhscs.game.core;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;

public class Camera {
  
  private RenderWindow window;
  
  private View defaultView;
  
  public Camera(final RenderWindow window) {
    this.window = window;
    defaultView = (View) window.getDefaultView();
  }
  
  public void targetEntity(Entity entity) {
    Vector2f position = entity.getSprite().getPosition();
    View newView = new View(defaultView.getCenter(), defaultView.getSize());
    position = new Vector2f((int) position.x, (int) position.y);
    newView.setCenter(position);
    window.setView(newView);
  }
  
  public void targetDefault() {
    
  }
}
