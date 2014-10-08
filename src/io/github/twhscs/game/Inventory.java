package io.github.twhscs.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.json.simple.parser.JSONParser;



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
   * Creates an array of shapes to draw the inventory from
   */
  private ArrayList<RectangleShape> itemSlots = new ArrayList<RectangleShape>();
  /**
   * Max items held in each inventory
   */
  private final int maxItems = 15;
  /**
   * Initializes the screen position of the inventory
   */
  private Vector2i centerScreenPosition;
  /**
   * The slot the inventory selector is hovering on
   */
  private int selectedSlot = 0;
  /**
   * The actual background of the inventory
   */
  private RectangleShape inventoryBackground;
  /**
   * Instantiates the selection box
   */
  private RectangleShape selectionBox;
  /**
   * Instantiates the action menu
   */
  private RectangleShape actionMenu;
  /**
   * Instantiates the action menu selection box
   */
  private RectangleShape actionMenuSelectionBox;
  /**
   * Boolean determining whether the window will be displayed or not
   */
  private boolean visible = false;
  /**
   * Number used to scale all inventory dimensions
   */
  private int rectSize = 64;
  /**
   * Used to determine whether or not to draw action menu
   */
  private boolean actionMenuDisplayed = true;
  /**
   * Text for inventory
   */
  private Font kenPixel = new Font();
  /**
   * Scanner for item list
   */
  private Scanner itemListScanner;
  /**
   * Default inventory object
   */
  public Inventory(Vector2i screenResolution) {

    try{
      kenPixel.loadFromFile(Paths.get("resources/kenpixel.ttf"));
    }catch(Exception e){
      e.printStackTrace();
    }
    
    initializeItems();

    inventoryBackground = new RectangleShape(new Vector2f(screenResolution)); // Creates background
    inventoryBackground.setFillColor(new Color(Color.BLACK, 230)); // Makes the background black

    /**
     * Find the center position on the screen on the screen
     */
    centerScreenPosition = new Vector2i((screenResolution.x / 2), (screenResolution.y / 2));

    createInventorySlots(centerScreenPosition);

    placeSelectionBox();

  }

  /**
   * Initializes all inventory slots in appropriate places
   * 
   * @param centerPosition is center of the screen
   */
  public void createInventorySlots(Vector2i centerPosition) {



    int rectSpacing = 6;

    int initialScreenOffsetX = centerPosition.x - centerPosition.x / 4; // Center the inventory on x
                                                                        // axis
    int initialScreenOffsetY = centerPosition.y + centerPosition.y / 2; // Center inventory on y
                                                                        // axis

    /**
     * Creates the selection box with appropriate colors and scale. Selection box is always larger
     * than the box it is placed on by two pixels on each side
     */
    selectionBox = new RectangleShape(new Vector2f(rectSize + 2, rectSize + 2));
    selectionBox.setOutlineColor(Color.CYAN);
    selectionBox.setOutlineThickness(2f);
    selectionBox.setFillColor(Color.TRANSPARENT);

    /**
     * Creates the stats box which will hold player and item information
     */
    RectangleShape statsBox = new RectangleShape();
    statsBox.setSize(new Vector2f(rectSize * 2 + 4, rectSize * 4)); // Sets size as two inventory
                                                                    // slots wide and four tall
    statsBox.setFillColor(new Color(Color.CYAN, 120));
    statsBox.setOutlineThickness(2f);
    statsBox.setOutlineColor(new Color(Color.CYAN, 200));
    statsBox.setPosition(new Vector2f(initialScreenOffsetX, initialScreenOffsetY
        - ((rectSize + 6) * 6))); // Puts center screen but six inventory slots above y center

    /**
     * Creates player box which will hold player image
     */
    RectangleShape playerBox = new RectangleShape();
    playerBox.setSize(new Vector2f(rectSize * 2 + 4, rectSize * 4));
    playerBox.setFillColor(new Color(Color.CYAN, 120));
    playerBox.setOutlineThickness(2f);
    playerBox.setOutlineColor(new Color(Color.CYAN, 200));
    playerBox.setPosition(new Vector2f(initialScreenOffsetX + (rectSize + rectSpacing) * 3,
        initialScreenOffsetY - (rectSize + rectSpacing) * 6));

    /**
     * Creates all actual inventory slots
     */
    for (int i = 0; i < maxItems; i++) {
      int column = i % 5; // Five columns
      int row = i / 5; // Three rows

      /**
       * All attributes of item slots are set
       */
      RectangleShape itemSlot = new RectangleShape(new Vector2f(rectSize, rectSize));
      itemSlot.setFillColor(new Color(Color.CYAN, 120));
      itemSlot.setOutlineColor(new Color(Color.CYAN, 200));
      itemSlot.setOutlineThickness(2f);

      // Adds item slot to inventory arraylist
      itemSlots.add(itemSlot);

      // Positions each item slot based on where the preceding one was placed by columns and rows
      itemSlots.get(i).setPosition(
          new Vector2f((initialScreenOffsetX + (rectSize + rectSpacing) * column),
              (initialScreenOffsetY - (rectSize + rectSpacing) * 2)
                  + ((rectSize + rectSpacing) * row + 1)));
    }

    /**
     * Creates four armor slots between player and stat boxes
     */
    for (int count = 0; count < 4; count++) {

      /**
       * Set armor slot attributes
       */
      RectangleShape armorSlot = new RectangleShape(new Vector2f(rectSize - 2, rectSize - 2));
      armorSlot.setFillColor(new Color(Color.CYAN, 120));
      armorSlot.setOutlineColor(new Color(Color.CYAN, 200));
      armorSlot.setOutlineThickness(2f);

      armorSlot.setPosition(initialScreenOffsetX + ((rectSize + 6) * 2), (initialScreenOffsetY
          - ((rectSize + rectSpacing) * 6) + ((rectSize * count))));

      itemSlots.add(armorSlot); // Adds the armor slots to the inventory arrayList
    }

    /**
     * Adds stat and player boxes to the end of the inventory arrayList
     */
    itemSlots.add(statsBox);
    itemSlots.add(playerBox);

    actionMenu = new RectangleShape();
    actionMenu.setFillColor(new Color(Color.BLUE, 200));
    actionMenu.setOutlineColor(new Color(Color.CYAN, 250));
    actionMenu.setOutlineThickness(2f);
    placeActionMenu();

  }

  public void addItem(Item i) {

    System.out.println(inventory.size());
    /**
     * If inventory is not full add item to the inventory
     */
    if (inventory.size() < maxItems) {
      inventory.add(i);
      i.setDisplayPosition(inventory.size() - 1, itemSlots, rectSize);
    }
  }

  /**
   * Removes item from inventory.
   *
   * @param place is spot in the inventory item is removed from
   */
  public void removeItem(int place) {
    if (inventory.size() > 0)
      inventory.remove(place);
  }

  /**
   * Returns inventory
   */
  public ArrayList<Item> getInventory() {
    return inventory;
  }

  /**
   * Method for the game to render inventory
   */
  public void draw(RenderTarget target, RenderStates states) {
    inventoryBackground.draw(target, states);
    selectionBox.draw(target, states);

    for (RectangleShape inventorySlots : itemSlots) {
      inventorySlots.draw(target, states);
    }
    for (Item item : inventory) {
      item.draw(target, states);
    }
    if (actionMenuDisplayed) {
      actionMenu.draw(target, states);
    }
  }

  /**
   * Swap between inventory being drawable or visible
   */
  public final void toggleInventoryDisplay() {
    visible = !visible;
  }

  /**
   * Return whether the inventory is visible or not.
   *
   * @return
   */
  public boolean isVisible() {
    return visible;
  }

  /**
   * Sets the selection box to the selected slot's position and scales it
   */
  public void placeSelectionBox() {

    selectionBox.setPosition(itemSlots.get(selectedSlot).getPosition().x - 1, // Place box over
                                                                              // selected box and
                                                                              // move
        itemSlots.get(selectedSlot).getPosition().y - 1); // left by one pixel to account for
                                                          // selection box being two pixels wider
                                                          // and taller

    selectionBox.setSize(new Vector2f(itemSlots.get(selectedSlot).getSize().x + 2, // Set selection
                                                                                   // box to same
        itemSlots.get(selectedSlot).getSize().y + 2)); // size as selected box but two pixels wider
                                                       // and taller
  }

  /**
   * Moves selection box on up button press.
   */
  public void moveSelectionBoxUp() {

    if (selectedSlot >= 5) { // If slot is not on the top row of inventory slots
      if (selectedSlot >= 16 && selectedSlot <= 18) // Check if selected slot is an armor slot
        selectedSlot--; // If it is an armor slot, move up one armor slot
      else if (selectedSlot <= 14) // If slot is not on the top row but not an armor slot
        selectedSlot -= 5; // Move down one row
    } else if (selectedSlot <= 4) // If the slot is on the top row of inventory slots
      selectedSlot = 18; // Move to bottom armor slot

    placeSelectionBox(); // Resize and re-position selection box
    placeActionMenu();
  }

  /**
   * Moves selection box on down button press
   */
  public void moveSelectionBoxDown() {

    if (selectedSlot >= 0 && selectedSlot <= 9) // Check if selected box is on top two inventory
                                                // rows
      selectedSlot += 5; // If it is, move down a row

    else if (selectedSlot >= 15 && selectedSlot <= 17) // Check if selected slot is the top three
                                                       // armor slots
      selectedSlot++; // If it is, move down armor slot

    else if (selectedSlot == 18) // Check if selected slot is bottom armor slot
      selectedSlot = 2; // If so, move to inventory slot directly below bottom armor slot

    placeSelectionBox(); // Readjusts and repositions selection box
    placeActionMenu();
  }

  /**
   * Move selection box on left key press
   */
  public void moveSelectionBoxLeft() {

    if (selectedSlot >= 1 && selectedSlot <= 14) // Check if selection box is on inventory slot
                                                 // except bottom left
      selectedSlot--; // If it is, move left one slot

    placeSelectionBox(); // Re-adjust and reposition selection box
    placeActionMenu();
  }

  /**
   * Move selection box on right button press
   */
  public void moveSelectionBoxRight() {

    if (selectedSlot <= 13) // Check if selection box is in inventory slot except top right
      selectedSlot++; // If it is, move right one slot

    placeSelectionBox(); // Re-adjust and reposition selection box
    placeActionMenu();
  }

  /**
   * Toggles action menu for displaying and interacting
   */
  public void toggleActionMenu() {
    actionMenuDisplayed = !actionMenuDisplayed;
  }

  public void placeActionMenu() {
    Vector2f newPosition = Vector2f.add(itemSlots.get(selectedSlot).getPosition(),
        new Vector2f(rectSize,rectSize* -2));

    actionMenu.setPosition(newPosition);
    actionMenu.setSize(new Vector2f(rectSize * 3, rectSize * 3));


  }
  
  private void initializeItems() {
    String itemList = "";
    
     try {
     itemListScanner = new Scanner(new File("resources/itemlist.dat"));
     } catch (FileNotFoundException e) {
     e.printStackTrace();
     } 
     while(itemListScanner.hasNext()){
       itemList += itemListScanner.next();
     }
     
     
     JSONParser jsonParser = new JSONParser();
     
     
     
   }


}
