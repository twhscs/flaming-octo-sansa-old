package io.github.twhscs.game;

import java.nio.file.Paths;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

/**
 *
 * @author chris
 *
 */
public class Item extends Entity implements Drawable{
  
  Font kenpixel = new Font();
  
  private Text displayedItemText;
  
  private String itemName;

  private Sprite itemSprite;
  
  private Texture itemTexture = new Texture();
  
  private Vector2i inventoryLocation;
  
  private int healthRegen;

  /**
   * Creates a new item
   * @param i is the type of item created
   */
  public Item(ItemType itemType){
    
    try{
      kenpixel.loadFromFile(Paths.get("resources/kenpixel.ttf"));
    }catch(Exception ex){
      ex.printStackTrace();
    }

    /**
     * Check the type of item and assign attributes
     * depending on the item type
     */
    setItemAttributes(itemType);
    displayedItemText.setStyle(Text.BOLD | Text.UNDERLINED);
    displayedItemText.getLocalBounds();
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
  
  public void setItemAttributes(ItemType itemType){
    switch(itemType){
      case HEALTH_POTION:
        itemName = "Health Potion";
        healthRegen = 20;
        displayedItemText = new Text(itemName, kenpixel, 16);
        break;
      default:
        break;
    }
  }
  
  public void useItem(){
    
  }

  public void dropItem(){
    
  }
  
  public void setDisplayPosition(Vector2i centerPosition, int inventorySize, int maxItems){
    
    int centerX = centerPosition.x;
    int centerY = centerPosition.y;
    int spaceBetweenLines = 30;
    int initialBufferX = 120;
    int initialBufferY = 100;
    int spaceBetweenColumns = 350;
    
    centerY = centerY + initialBufferY + (inventorySize*spaceBetweenLines);

    if(inventorySize < (maxItems / 2))
      centerX = centerX + initialBufferX;
    else{
      centerY = centerPosition.y + initialBufferY + ((inventorySize-10)*spaceBetweenLines);
      centerX = centerX + spaceBetweenColumns;
    }
    
    Vector2f displayPosition = new Vector2f(centerX, centerY);
    
    displayedItemText.setPosition(displayPosition);
  }

  @Override
  public void draw(RenderTarget target, RenderStates states) {
    displayedItemText.draw(target, states);
  }
}
