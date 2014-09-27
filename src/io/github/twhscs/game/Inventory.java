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
  
  private int selectedSlot = 10;
  
  private RectangleShape inventoryBackground;
  /**
   * Instantiates the selection box
   */
  private RectangleShape selectionBox;
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
    
    createInventorySlots(centerScreenPosition);
    selectionBox.setPosition(itemSlots.get(selectedSlot).getPosition().x-1,
        itemSlots.get(selectedSlot).getPosition().y-1);

    try{
    }catch(Exception ex){
      ex.printStackTrace();
    }



  }
  

  public void createInventorySlots(Vector2i centerPosition){
    
    
    int rectSize = 64;
    
    int initialScreenOffsetX = centerPosition.x - centerPosition.x/4;
    int initialScreenOffsetY = centerPosition.y + centerPosition.y/2;
    
    selectionBox = new RectangleShape(new Vector2f(rectSize + 2, rectSize + 2));
    selectionBox.setOutlineColor(Color.CYAN);
    selectionBox.setOutlineThickness(2f);
    selectionBox.setFillColor(Color.TRANSPARENT);
    
    RectangleShape statsBox = new RectangleShape();
    statsBox.setSize(new Vector2f(rectSize * 2 + 4, rectSize * 4));
    statsBox.setFillColor(new Color(Color.CYAN, 120));
    statsBox.setOutlineThickness(2f);
    statsBox.setOutlineColor(new Color(Color.CYAN, 200));
    statsBox.setPosition(new Vector2f(initialScreenOffsetX, initialScreenOffsetY - ((rectSize + 6) * 6)));
    
    RectangleShape playerBox = new RectangleShape();
    playerBox.setSize(new Vector2f(rectSize * 2 + 4, rectSize * 4));
    playerBox.setFillColor(new Color(Color.CYAN, 120));
    playerBox.setOutlineThickness(2f);
    playerBox.setOutlineColor(new Color(Color.CYAN, 200));
    playerBox.setPosition(new Vector2f(initialScreenOffsetX + (rectSize + 6)*3,
        initialScreenOffsetY - (rectSize + 6) * 6));
    
    
    for(int i = 0; i<maxItems; i++){
      int column = i%5;
      int row = i/5;
      
      RectangleShape itemSlot = new RectangleShape(new Vector2f(rectSize,rectSize));
      itemSlot.setFillColor(new Color(Color.CYAN, 120));
      itemSlot.setOutlineColor(new Color(Color.CYAN, 200));
      itemSlot.setOutlineThickness(2f);
      
      itemSlots.add(itemSlot);
      
      itemSlots.get(i).setPosition(new Vector2f(initialScreenOffsetX + (rectSize+ 6)*column,
          initialScreenOffsetY - ((rectSize + 6) * row + 1)));
      }
    
    for(int i = 0; i<4; i++){
      
      RectangleShape armorSlot = new RectangleShape(new Vector2f(rectSize - 2, rectSize - 2));
      armorSlot.setFillColor(new Color(Color.CYAN, 120));
      armorSlot.setOutlineColor(new Color(Color.CYAN, 200));
      armorSlot.setOutlineThickness(2f);
      
      armorSlot.setPosition(initialScreenOffsetX + ((rectSize + 6)*2),
          (initialScreenOffsetY - ((rectSize + 6) * 6) + ((rectSize * i)) + 2));
      
      itemSlots.add(armorSlot);
    }
      
    itemSlots.add(statsBox);
    itemSlots.add(playerBox);
    
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
  
  public void moveSelectionBoxUp(){

    if(selectedSlot<=9)
      selectedSlot += 5;
    
    else if(selectedSlot<=14 && selectedSlot>=10)
      selectedSlot = 18;
    
    else if(selectedSlot<=18){
      if(selectedSlot != 15)
        selectedSlot--;
    }
    
    selectionBox.setPosition(itemSlots.get(selectedSlot).getPosition().x-1,
        itemSlots.get(selectedSlot).getPosition().y-1);
    
    selectionBox.setSize(new Vector2f(itemSlots.get(selectedSlot).getSize().x + 2,
        itemSlots.get(selectedSlot).getSize().y + 2));
  }
  
  public void moveSelectionBoxDown(){

    if(selectedSlot>=5 && selectedSlot<=14)
      selectedSlot -= 5;
    
    else if(selectedSlot>14 && selectedSlot<=17)
      selectedSlot++;
    
    else if(selectedSlot == 18)
      selectedSlot = 12;
    
    selectionBox.setPosition(itemSlots.get(selectedSlot).getPosition().x-1,
        itemSlots.get(selectedSlot).getPosition().y-1);
    
    selectionBox.setSize(new Vector2f(itemSlots.get(selectedSlot).getSize().x + 2,
        itemSlots.get(selectedSlot).getSize().y + 2));
    }
  
  public void moveSelectionBoxLeft(){
    
    if(selectedSlot>=1 && selectedSlot <= 14)
      selectedSlot--;
    
    selectionBox.setPosition(itemSlots.get(selectedSlot).getPosition().x-1,
        itemSlots.get(selectedSlot).getPosition().y-1);
    
    selectionBox.setSize(new Vector2f(itemSlots.get(selectedSlot).getSize().x + 2,
        itemSlots.get(selectedSlot).getSize().y + 2));
  }
  
  public void moveSelectionBoxRight(){
    
    if(selectedSlot<=13)
      selectedSlot++;
    
    selectionBox.setPosition(itemSlots.get(selectedSlot).getPosition().x-1,
        itemSlots.get(selectedSlot).getPosition().y-1);
    
    selectionBox.setSize(new Vector2f(itemSlots.get(selectedSlot).getSize().x + 2,
        itemSlots.get(selectedSlot).getSize().y + 2));
  }


}
