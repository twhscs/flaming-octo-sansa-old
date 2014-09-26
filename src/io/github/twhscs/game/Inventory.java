package io.github.twhscs.game;

import java.nio.file.Paths;
import java.util.ArrayList;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

/**
 * 
 * @author chris
 *
 */
public class Inventory implements Drawable {
  
  /**
   * Each inventory has an array of items it can hold
   */
  private ArrayList<Item> inventory = new ArrayList<Item>();
  
  /**
   * Max items held in each inventory
   */
  private final int maxItems = 20;
  /**
   * Initializes the screen position of the inventory
   */
  private Vector2i centerScreenPosition;
  /**
   * Instantiates the selection box
   */
  private RectangleShape selectionBox = new RectangleShape(new Vector2f(200, 30));
  /**
   * The amount of offset the selectionBox is set to by default
   */
  private int selectionBoxOffsetX = 100;
  private int selectionBoxOffsetY = 90;
  /**   
   * Initializes texture and sprite for inventory
   */
  private Texture invTexture = new Texture();
  private Sprite invSprite;
  /**
   * Creates a sound buffer for cannotAddItem to be loaded into memory
   */
  SoundBuffer cannotAddItemBuffer = new SoundBuffer();
 /**
  * Initialize sound object
  */
  Sound cannotAddItem = new Sound();
  /**
   * Boolean determining whether the window will be displayed or not
   */
  private boolean visible = false;
  /**
   * Default inventory object
   */
  
  public Inventory(Vector2i screenResolution){
    /**
     * Load the sprite and sound files
     */
    try{
     cannotAddItemBuffer.loadFromFile(Paths.get("resources/beep-02.wav"));
     invTexture.loadFromFile(Paths.get("resources/inventory_texture.png"));
    }catch(Exception ex){
      ex.printStackTrace();
    }
    /**
     * Find the center position on the screen on the screen
     */
    centerScreenPosition = new Vector2i((screenResolution.x/2) - (invTexture.getSize().x/2), 
        (screenResolution.y/2) - (invTexture.getSize().y/2));
    /**
     * Set the sprite to the inventory texture
     */
    invSprite = new Sprite(invTexture);
    /**
     * Set the sound object cannotAddItem to load the sound from the sound buffer
     */
    cannotAddItem.setBuffer(cannotAddItemBuffer);
    /**
     * Place the sprite in the center of the screen
     */
    invSprite.setPosition(centerScreenPosition.x, centerScreenPosition.y);
    /**
     * Set selectionBox in the center of the screen and
     * fill it with its color
     */
    selectionBox.setOutlineColor(Color.CYAN);
    selectionBox.setOutlineThickness(2f);
    selectionBox.setFillColor(Color.TRANSPARENT);
    selectionBox.setPosition(centerScreenPosition.x + selectionBoxOffsetX, 
        centerScreenPosition.y + selectionBoxOffsetY);
  }

  public void addItem(Item i) {
    
    System.out.println(inventory.size());
    /**
     * If inventory is not full add item to the inventory
     */
    if(inventory.size() < maxItems){
      i.setDisplayPosition(centerScreenPosition, inventory.size(), maxItems);
      inventory.add(i);
    }
    /**
     * Play full bag sound
     */
    else{
      cannotAddItem.play();
    }
  }
  
  /**
   * Removes item from inventory
   * @param place is spot in the inventory item is removed from
   */
  public void removeItem(int place){
    inventory.remove(place);
  }
  
  /**
   * Returns inventory
   */
  public ArrayList<Item> getInventory(){
    return inventory;
  }

  /**
   * Method for the game to render inventory
   */
  public void draw(RenderTarget target, RenderStates states) {
    invSprite.draw(target, states);
    selectionBox.draw(target,states);
    for(Item item: inventory){
      item.draw(target, states);
    }
   
  }
  
  /**
   * Swap between inventory being drawable or visible
   */
  public void toggleInventoryDisplay(){
    if(visible == false)
      visible = true;
    else if(visible == true){
      visible = false;
      /**
       * Return the selection box back to top left slot
       */
      selectionBoxOffsetX = 90;
      selectionBoxOffsetY = 90;
      resetSelectionBox();
    }
  }
  
  /**
   * Return whether the inventory is visible or not
   * @return
   */
  public boolean isVisible(){
    return visible;
  }
  
  /**
   * Moves the selection box if in a valid location on the inventory
   */
  public void moveSelectionBoxDown(){
    /**
     * Move the box down thirty pixels if in valid location
     */
    if(selectionBoxOffsetY <= 330)
      selectionBoxOffsetY += 30;
    /**
     * Update the position
     */
    resetSelectionBox();
  }
  
  public void moveSelectionBoxUp(){
    /**
     * Move the box up thirty pixels if in valid location
     */
    if(selectionBoxOffsetY >= 120)
        selectionBoxOffsetY -= 30;
    /**
     * Update the position
     */
    resetSelectionBox();
  }
  
  public void moveSelectionBoxRight(){
    /**
     * Move the box right two hundred pixels if in valid location
     */
    if(selectionBoxOffsetX<=355)
      selectionBoxOffsetX += 230;
    /**
     * Update the position
     */
    resetSelectionBox();
  }
  
  public void moveSelectionBoxLeft(){
    /**
     * Move the box right two hundred pixels if in valid location
     */
    if(selectionBoxOffsetX>200)
      selectionBoxOffsetX -= 230;
    /**
     * Update the position
     */
    resetSelectionBox();
  }
  
  /**
   * Resets the selection box area to the top left slot
   */
  public void resetSelectionBox(){
    selectionBox.setPosition(centerScreenPosition.x + selectionBoxOffsetX, 
        centerScreenPosition.y + selectionBoxOffsetY);
  }
}
