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
  
  private ArrayList<RectangleShape> itemSlots = new ArrayList<RectangleShape>();
  /**
   * Max items held in each inventory
   */
  private final int maxItems = 15;
  /**
   * Initializes the screen position of the inventory
   */
  private Vector2i centerScreenPosition;
  
  private RectangleShape inventoryBackground;
  
  private RectangleShape itemSlot = new RectangleShape(new Vector2f(32,32));
  /**
   * Instantiates the selection box
   */
  private RectangleShape selectionBox = new RectangleShape(new Vector2f(34, 34));
  /**
   * Boolean determining whether the window will be displayed or not
   */
  private boolean visible = false;
  
  /**
   * Default inventory object
   */
  public Inventory(Vector2i screenResolution){

    inventoryBackground = new RectangleShape(new Vector2f(screenResolution));
    inventoryBackground.setFillColor(new Color(Color.BLACK, 230));
    
    /**
     * Find the center position on the screen on the screen
     */
    centerScreenPosition = new Vector2i((screenResolution.x/2), (screenResolution.y/2));
    
    for(int i = 0; i<maxItems; i++){
      int column = i%5;
      int row = i/5;
      
      
      RectangleShape itemSlot = new RectangleShape(new Vector2f(64,64));
      itemSlot.setFillColor(new Color(Color.CYAN, 120));
      itemSlot.setOutlineColor(new Color(Color.CYAN, 200));
      itemSlot.setOutlineThickness(2f);
      
      itemSlots.add(itemSlot);
      
      itemSlots.get(i).setPosition(new Vector2f(30 + 70*column, screenResolution.y - 100 - (70 * row + 1)));
      
    }
    
    try{
    }catch(Exception ex){
      ex.printStackTrace();
    }


    /**
     * Set selectionBox in the center of the screen and
     * fill it with its color
     */
    selectionBox.setOutlineColor(Color.CYAN);
    selectionBox.setOutlineThickness(2f);
    selectionBox.setFillColor(Color.TRANSPARENT);
    selectionBox.setPosition(centerScreenPosition.x, centerScreenPosition.y);
  }

  public void addItem(Item i) {
    
    System.out.println(inventory.size());
    /**
     * If inventory is not full add item to the inventory
     */
    if(inventory.size() < maxItems){
      inventory.add(i);
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
    inventoryBackground.draw(target, states);
    selectionBox.draw(target,states);
    
    for(RectangleShape inventorySlots: itemSlots){
      inventorySlots.draw(target, states);
    }
    
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
    }
  }
  
  /**
   * Return whether the inventory is visible or not
   * @return
   */
  public boolean isVisible(){
    return visible;
  }


}
