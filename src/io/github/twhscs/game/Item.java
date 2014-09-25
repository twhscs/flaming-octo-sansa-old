package io.github.twhscs.game;

import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

/**
 *
 * @author chris
 *
 */
public class Item extends Entity {
  
  private String itemName;

  private Sprite itemSprite;
  
  private Texture itemTexture = new Texture();
  
  private Location location;
  
  private int healthRegen;

  /**
   * Creates a new item
   * @param i is the type of item created
   */
  public Item(ItemType i){
    /**
     * Check the type of item and assign attributes
     * depending on the item type
     */
    switch(i){
      case HEALTH_POTION:
        itemName = "Health Potion";
        healthRegen = 20;
        break;
      default:
        break;
    }
  }
  
  /**
   * The location for each item texture
   */
  private Vector2f getTextureCoords(ItemType itemType){
    /**
     * Position of the texture in the image
     */
    Vector2f textureCoords = new Vector2f(0,0);
    /**
     * Check the type and return where the texture is
     */
    switch(itemType){
      case HEALTH_POTION:
        return textureCoords = new Vector2f(10,10);
      default:
        return textureCoords;
    }
  }
  
  public void useItem(){
    
  }

  public void dropItem(){
    
  }
}
