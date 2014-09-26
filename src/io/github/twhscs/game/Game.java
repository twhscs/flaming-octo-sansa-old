package io.github.twhscs.game;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.TextStyle;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

/**
 * The main class of the game. Contains the main loop and pieces everything together.
 * This file should ideally be as short as possible.
 * @author Robert
 *
 */
public class Game { 
  /**
   * Main window where everything is drawn to. Handles all input.
   */
  private final RenderWindow window = new RenderWindow();
  /**
   * Sets the title that displays in the title-bar of the window.
   */
  private final String windowTitle = "Game";
  /**
   * Sets the dimensions (resolution) the window is created with.
   */
  private final Vector2i windowDimensions = new Vector2i(640, 480);
  /**
   * The main object representing the player.
   */
  private final Player player = new Player();
  /**
   * The UI element responsible for displaying the FPS on screen.
   */
  private final TextUIElement fpsUI = 
      new TextUIElement(InterfacePosition.TOP_LEFT, Color.YELLOW, 24, TextStyle.BOLD);
  /**
   * Represents whether or not the user has the window opened and focused.
   */
  private boolean windowFocus = true;
  /**
   * Allows the window to shift focus.
   */
  private Camera camera;
  /**
   * The player's personal inventory
   */
  private Inventory playerInventory;
  
  /**
   * Creates an instance of the game and runs it.
   * @param args Command line arguments passed in at run-time.
   */
  public static void main(String[] args) {
    Game g = new Game();
    g.run();
  }
  
  /**
   * Configures one time settings at start-up.
   */
  public void handleInitialization() {
    window.create(new VideoMode(windowDimensions.x, windowDimensions.y), windowTitle);
    player.changeMap(new Map(10, 10, Tile.SAND));
    camera = new Camera(window);
    playerInventory = new Inventory(windowDimensions);
  }
  
  /**
   * Initializes the game and holds the main loop.
   */
  public void run() {
    handleInitialization(); // Initial configuration of various things
    int framesDrawn = 0; // Count each frame that is drawn
    float updateRate = 20; // Limit the logic loop to update at 20Hz (times per second)
    Clock updateClock = new Clock(); // Clock used to restrict update loop to a fixed rate
    Clock frameClock = new Clock(); // Clock used to calculate average FPS
    updateClock.restart(); // Reset update clock
    long nextUpdate = 
        updateClock.getElapsedTime().asMilliseconds(); // Calculate next update time in milliseconds
    while (window.isOpen()) { // Run main loop as long as window is open
      handleInput(); // Process input
      long updateTime = 
          updateClock.getElapsedTime().asMilliseconds(); // Make note of the current update time
      while ((updateTime - nextUpdate) >= updateRate) { // Update loop
        handleLogic(); // Process fixed-time logic
        nextUpdate += updateRate;  // Computer next appropriate update time
      }
      handleDrawing(); // Draw everything to the window
      framesDrawn++; // Increment; a frame has been drawn
      // Calculate how long it has been since last calculating FPS
      float elapsedTime = 
          frameClock.getElapsedTime().asSeconds(); 
      if (elapsedTime >= 1.0f) { // If it has been one second
        // Divide the frames drawn by one second, aka FPS
        fpsUI.updateString("FPS: " + (int) (framesDrawn / elapsedTime));
        framesDrawn = 0; // Reset frame count
        frameClock.restart(); // Reset frame clock
      }
    }
  }
  
  /**
   * Responds to any user input (keyboard or mouse).
   */
  public void handleInput() {
    
    /* Window based event queue (slight ms lag)
     * Good for single-press actions, bad for repeated actions
     */
    
    for (Event event : window.pollEvents()) {
      switch(event.type) {
        case CLOSED:
          window.close(); // Close the window if the user clicks the X button
          break;
        case GAINED_FOCUS:
          windowFocus = true; // Update windowFocus if the user focuses the window
          break;
        case LOST_FOCUS:
          windowFocus = false; // Update windowFocus if the user unfocuses the window
          break;
        case KEY_PRESSED:
          /**
           * Check if the inventory is visible and if it is take input to interact with menu
           */
          if(playerInventory.isVisible()){
            switch(event.asKeyEvent().key){
              case W:
                //playerInventory.moveSelectionBoxUp();
                System.out.println("UP");
                break;
              case S:
                //playerInventory.moveSelectionBoxDown();
                System.out.println("DOWN");
                break;
              case A:
                //playerInventory.moveSelectionBoxLeft();
                System.out.println("LEFT");
                break;
              case D:
                //playerInventory.moveSelectionBoxRight();
                System.out.println("RIGHT");
                break;
              case R:
                playerInventory.addItem(new Item(ItemType.HEALTH_POTION));
            }
          }
          /**
           * Toggle inventory view on pressing Q key
           */
          switch(event.asKeyEvent().key){
            case Q:
              playerInventory.toggleInventoryDisplay();
          }
        default:
          break;
      }
    }
    
    /* Real-time input (no lag)
     * Good for repeated actions, bad for single-press actions
     */
    
    // isKeyPressed will work whether the window is focused or not, therefore we must check manually
    if (windowFocus && !playerInventory.isVisible()) { 
      if (Keyboard.isKeyPressed(Key.W)) {
        player.move(Direction.NORTH); // W moves the player up (north)
      } else if (Keyboard.isKeyPressed(Key.S)) {
        player.move(Direction.SOUTH); // S moves the player down (south)
      } else if (Keyboard.isKeyPressed(Key.A)) {
        player.move(Direction.WEST); // A moves the player left (west)
      } else if (Keyboard.isKeyPressed(Key.D)) {
        player.move(Direction.EAST); // D moves the player right (east)
      }
    }
    
  }
  
  /**
   * Updates at a fixed rate (20Hz).
   */
  public void handleLogic() {
    player.update();
  }
  
  /**
   * Updates as fast as possible. Draws all objects onto the screen.
   */
  public void handleDrawing() {
    // The window has automatic double buffering
    window.clear(); // Wipe everything from the window
    // Draw each object like layers, background to foreground
    Vector2f playerPos = player.getSprite().getPosition();
    camera.centerOn(playerPos, 0);
    window.draw(player.getMap()); 
    window.draw(player);
    camera.centerOnDefault();
    window.draw(fpsUI);
    if(playerInventory.isVisible())
      window.draw(playerInventory);
    window.display(); // Show the window to the user
  }
}
