package io.github.twhscs.game;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;
import org.jsfml.window.ContextSettings;
import org.jsfml.window.Joystick;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.JoystickMoveEvent;

import io.github.twhscs.game.util.ImageResource;

/**
 * The main class containing the main method and game loop. This file should ideally be as short as
 * possible.
 * 
 * @author Robert
 *
 */
public class Game {

  /**
   * Main window where everything is drawn to.
   */
  private final RenderWindow window = new RenderWindow();

  /**
   * Title that displays in the title-bar of the window.
   */
  private final String windowTitle = "Game";

  /**
   * Dimensions (resolution) of the window in pixels.
   */
  private final Vector2f windowDimensions = new Vector2f(640, 480);

  /**
   * Whether or not the user is focused on the window. Used for determining whether or not to
   * capture input.
   */
  private boolean windowFocus = true;

  /**
   * Level of anti-aliasing the window uses. From what I gather this does not appear to do anything.
   * Leaving it for now just in case.
   */
  // TODO: Verify is this is necessary.
  private final int windowAntialiasing = 0;

  /**
   * Determines whether or not the window uses v-sync. V-sync limits the FPS to the same rate as the
   * monitor to prevent tearing.
   */
  private final boolean windowVsync = false;

  /**
   * Whether or not the user has a controller connected.
   */
  private boolean usingController = Joystick.isConnected(0);

  /**
   * Main camera. Allows the window to center on objects.
   */
  private final Camera camera = new Camera(window);

  /**
   * Current map loaded in memory. This is also the map the player is currently on.
   */
  private Map currentMap;

  /**
   * Player object representing the player.
   */
  private final Player player = new Player();

  /**
   * Actions corresponding to key and joystick inputs.
   * 
   * @author Robert
   *
   */
  private enum inputAction {
    /**
     * UP, DOWN, LEFT, and RIGHT are for menu movement. OPEN_MENU opens the main menu.
     * OPEN_INVENTORY opens the inventory. INTERACT either makes a selection or causes the player to
     * interact with a nearby NPC.
     */
    MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT, SELECT_UP, SELECT_DOWN, SELECT_LEFT, SELECT_RIGHT, OPEN_MENU, OPEN_INVENTORY, INTERACT
  };

  /**
   * Creates a game object and runs it, starting the game.
   * 
   * @param args Command line arguments passed in at execution.
   */
  public static void main(final String[] args) {
    Game g = new Game();
    g.run();
  }

  /**
   * Initializes variables and creates the window.
   */
  public final void handleInitialization() {
    // Cast the window width to an integer.
    final int wWidth = (int) windowDimensions.x;
    // Cast the window height to an integer.
    final int wHeight = (int) windowDimensions.y;
    // Create the video mode using the width and height.
    final VideoMode wVideoMode = new VideoMode(wWidth, wHeight);
    // Create the style to prevent window re-sizing.
    final int wStyle = WindowStyle.CLOSE | WindowStyle.TITLEBAR;
    // Create the context settings with the specified anti-aliasing level.
    final ContextSettings wALevel = new ContextSettings(windowAntialiasing);
    // Create the window with the specified settings.
    window.create(wVideoMode, windowTitle, wStyle, wALevel);
    // Prevent repeated key input.
    window.setKeyRepeatEnabled(false);
    // Enable or disable v-sync.
    window.setVerticalSyncEnabled(windowVsync);
    // Load the icon image.
    final ImageResource icon = new ImageResource("icon");
    // Set the window icon.
    window.setIcon(icon.getImage());
    // Set the map width.
    final int mWidth = 10;
    // Set the map height.
    final int mHeight = 10;
    // Create a new map of the specified width and height.
    currentMap = new Map(mWidth, mHeight, Tile.GRASS);
    // Add the player to the map.
    currentMap.addEntity(player);

    System.out.println(usingController);
  }

  /**
   * Starts the game and main loop.
   */
  public final void run() {
    // Initialize and create the window.
    handleInitialization();
    // Keeps track of frames drawn for calculating the FPS.
    int framesDrawn = 0;
    /*
     * The rate the logic is limited to, in Hz. A rate of 20 would run the logic function 20 times
     * per second.
     */
    final float updateRate = 20;
    // Clock used to restrict update loop.
    final Clock updateClock = new Clock();
    // Clock used to calculate FPS.
    final Clock frameClock = new Clock();
    // Reset the update clock.
    updateClock.restart();
    // Calculate the next time to update the logic in milliseconds.
    long nextUpdate = updateClock.getElapsedTime().asMilliseconds();
    // Run the main loop as long as the window is open.
    while (window.isOpen()) {
      // Process any input.
      handleInput();
      // Calculate the elapsed time in milliseconds.
      final long upTime = updateClock.getElapsedTime().asMilliseconds();
      // Update the logic based on the update rate.
      while ((upTime - nextUpdate) >= updateRate) {
        // Process fixed time logic.
        handleLogic();
        // Calculate next appropriate update time.
        nextUpdate += updateRate;
      }
      // Draw everything to the window.
      handleDrawing();
      // Update the frame counter.
      framesDrawn++;
      // Get the elapsed time in seconds.
      final float elapsedTime = frameClock.getElapsedTime().asSeconds();
      // Check if one second has passed.
      if (elapsedTime >= 1) {
        // Calculate the frames drawn in one second. Also known as: FPS.
        final int framesPerSecond = (int) (framesDrawn / elapsedTime);
        // Reset the frames drawn counter.
        framesDrawn = 0;
        // Reset the FPS clock.
        frameClock.restart();
      }
    }
  }

  /**
   * Collects and reacts to user input.
   */
  public final void handleInput() {

    for (Event event : window.pollEvents()) {
      switch (event.type) {
        case GAINED_FOCUS:
          windowFocus = true;
          break;
        case LOST_FOCUS:
          windowFocus = false;
          break;
        case JOYSTICK_CONNECETED:
          usingController = true;
          break;
        case JOYSTICK_DISCONNECTED:
          usingController = false;
          break;
        case CLOSED:
          window.close();
          break;
        case KEY_PRESSED:
          if (!usingController) {
            switch (event.asKeyEvent().key) {
              case W:
              case NUMPAD8:
                handleInputAction(inputAction.SELECT_UP);
                break;
              case S:
              case NUMPAD5:
                handleInputAction(inputAction.SELECT_DOWN);
                break;
              case A:
              case NUMPAD4:
                handleInputAction(inputAction.SELECT_LEFT);
                break;
              case D:
              case NUMPAD6:
                handleInputAction(inputAction.SELECT_RIGHT);
                break;
              case E:
              case NUMPAD9:
                handleInputAction(inputAction.INTERACT);
                break;
              case Q:
              case NUMPAD7:
                handleInputAction(inputAction.OPEN_INVENTORY);
                break;
              case ESCAPE:
                handleInputAction(inputAction.OPEN_MENU);
                break;
              default:
                break;
            }
          }
          break;
        case JOYSTICK_MOVED:
          final JoystickMoveEvent joyEvent = event.asJoystickMoveEvent();
          switch (joyEvent.joyAxis) {
            case POV_X:
              if (joyEvent.position > 0) {
                handleInputAction(inputAction.SELECT_UP);
              } else if (joyEvent.position < 0) {
                handleInputAction(inputAction.SELECT_DOWN);
              }
              break;
            case POV_Y:
              if (joyEvent.position > 0) {
                handleInputAction(inputAction.SELECT_RIGHT);
              } else if (joyEvent.position < 0) {
                handleInputAction(inputAction.SELECT_LEFT);
              }
            default:
              break;
          }
        case JOYSTICK_BUTTON_PRESSED:
          if (usingController) {
            switch (event.asJoystickButtonEvent().button) {
              case 0:
                handleInputAction(inputAction.INTERACT);
                break;
              case 2:
                handleInputAction(inputAction.OPEN_INVENTORY);
                break;
              case 7:
                handleInputAction(inputAction.OPEN_MENU);
                break;
              default:
                break;
            }
          }
          break;
        default:
          break;
      }
    }

    /*
     * Window based event queue (slight ms lag) Good for single-press actions, bad for repeated
     * actions
     */

    /*
     * for (Event event : window.pollEvents()) { switch (event.type) { case CLOSED: window.close();
     * // Close the window if the user clicks the X // button break; case GAINED_FOCUS: windowFocus
     * = true; // Update windowFocus if the user focuses // the window break; case LOST_FOCUS:
     * windowFocus = false; // Update windowFocus if the user // un-focuses the window break; case
     * KEY_PRESSED: if (!usingController) { switch (event.asKeyEvent().key) { // If a non-movement
     * key // is pressed case E: case NUMPAD9: if (player.getCurrentAction() == PlayerAction.NONE ||
     * player.getCurrentAction() == PlayerAction.TALKING) { player.interact(); // Interact with
     * entities by // pressing E } else if (player.getCurrentAction() == PlayerAction.IN_MENU) { }
     * break; case ESCAPE: if (player.getCurrentAction() == PlayerAction.NONE) { // opposite to the
     * current // visibility player.setCurrentAction(PlayerAction.IN_MENU); } else if
     * (player.getCurrentAction() == PlayerAction.IN_MENU) {
     * player.setCurrentAction(PlayerAction.NONE); } break; case W: if (player.getCurrentAction() ==
     * PlayerAction.IN_MENU) { } break; case S: if (player.getCurrentAction() ==
     * PlayerAction.IN_MENU) { } break; default: break; } } break; case JOYSTICK_BUTTON_PRESSED:
     * switch (event.asJoystickButtonEvent().button) { case 0: player.interact(); break; } break;
     * case JOYSTICK_CONNECETED: usingController = true; break; case JOYSTICK_DISCONNECTED:
     * usingController = false; break; } }
     */

    /*
     * Real-time input (no lag) Good for repeated actions, bad for single-press actions Chances are
     * you should be adding your key above, NOT below
     */

    // isKeyPressed will work whether the window is focused or not,
    // therefore we must check manually
    /*
     * if (windowFocus && player.getCurrentAction() != PlayerAction.IN_MENU) { if (!usingController)
     * { if (Keyboard.isKeyPressed(Key.W) || Keyboard.isKeyPressed(Key.NUMPAD8)) {
     * player.move(Direction.NORTH); // W moves the player up // (north) } else if
     * (Keyboard.isKeyPressed(Key.S) || Keyboard.isKeyPressed(Key.NUMPAD5)) {
     * player.move(Direction.SOUTH); // S moves the player down // (south) } else if
     * (Keyboard.isKeyPressed(Key.A) || Keyboard.isKeyPressed(Key.NUMPAD4)) {
     * player.move(Direction.WEST); // A moves the player left // (west) } else if
     * (Keyboard.isKeyPressed(Key.D) || Keyboard.isKeyPressed(Key.NUMPAD6)) {
     * player.move(Direction.EAST); // D moves the player right // (east) } } else { if
     * (Joystick.getAxisPosition(0, Axis.POV_X) == 100) { player.move(Direction.NORTH); // W moves
     * the player up // (north) } else if (Joystick.getAxisPosition(0, Axis.POV_X) == -100) {
     * player.move(Direction.SOUTH); // S moves the player down // (south) } else if
     * (Joystick.getAxisPosition(0, Axis.POV_Y) == 100) { player.move(Direction.EAST); // D moves
     * the player right // (east) } else if (Joystick.getAxisPosition(0, Axis.POV_Y) == -100) {
     * player.move(Direction.WEST); // A moves the player left // (west) } } }
     */

  }

  public void handleInputAction(final inputAction a) {

  }

  /**
   * Updates at a fixed rate (20Hz).
   */
  public void handleLogic() {
    currentMap.updateAllEntities(); // Call each the update method for each
    // entity
  }

  /**
   * Updates as fast as possible. Draws all objects onto the screen.
   */
  public final void handleDrawing() {
    // The window has automatic double buffering
    window.clear(); // Wipe everything from the window
    // Draw each object like layers, background to foreground
    // camera.centerOn(player.getAnimatedSprite()); // Draw everything
    // relative
    // to player, centering it
    window.draw(currentMap); // Draw the map
    currentMap.drawAllEntities(window); // Draw each entity, sorted by
    // y-value
    // camera.centerOnDefault(); // Stop drawing relative to the player
    window.display(); // Show the window to the user
  }
}
