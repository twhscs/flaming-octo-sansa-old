package io.github.twhscs.game.util;

import org.jsfml.graphics.Image;
import org.jsfml.graphics.Texture;

import java.io.IOException;
import java.io.InputStream;

/**
 * Base resource loader and container.
 * @author Robert
 *
 */
public abstract class Resource {
  
  private static final InputStream getStream(final String path) {
    return Resource.class.getClassLoader().getResourceAsStream(path);
  }
  
  public static final Texture loadTexture(final String name) {
    final Texture texture = new Texture();
    try {
      texture.loadFromStream(getStream("images/" + name + ".png"));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return texture;
  }
  
  public static final Image loadImage(final String name) {
    final Image image = new Image();
    try {
      image.loadFromStream(getStream("images/" + name + ".png"));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return image;
  }
  
}
