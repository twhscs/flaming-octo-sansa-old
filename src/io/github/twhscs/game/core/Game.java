package io.github.twhscs.game.core;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;
import org.jsfml.window.ContextSettings;
import org.jsfml.window.Joystick;
import org.jsfml.window.Joystick.Axis;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.JoystickMoveEvent;

import io.github.twhscs.game.core.Location.Direction;
import io.github.twhscs.game.util.Resource;

public class Game {

  private enum InputAction {
    MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT, SELECT_UP, SELECT_DOWN, SELECT_LEFT, SELECT_RIGHT, OPEN_MENU, OPEN_INVENTORY, INTERACT, DEV_MODE
  }

  private final RenderWindow window = new RenderWindow();

  private final String windowTitle = "Game";

  private final Vector2f windowDimensions = new Vector2f(640, 480);

  private final int windowAntialiasing = 0;

  private final boolean windowVsync = false;

  private boolean windowFocus = true;

  private boolean usingController = false;

  private Camera camera;

  private Map currentMap;

  private final Player player = new Player();

  private boolean devMode = false;

  private InanimateObject rock;
  private NonplayerCharacter deadpool;

  public static final void main(final String[] args) {
    final Game game = new Game();
    game.run();
  }

  public final void run() {
    initialize();
    int framesDrawn = 0;
    final float updateRate = 20;
    final Clock updateClock = new Clock();
    final Clock frameClock = new Clock();
    updateClock.restart();
    long nextUpdateTime = updateClock.getElapsedTime().asMilliseconds();
    while (window.isOpen()) {
      getInput();
      final long currentUpdateTime = updateClock.getElapsedTime().asMilliseconds();
      while ((currentUpdateTime - nextUpdateTime) >= updateRate) {
        updateLogic();
        nextUpdateTime += updateRate;
      }
      handleDrawing();
      framesDrawn++;
      final float elapsedTime = frameClock.getElapsedTime().asSeconds();
      if (elapsedTime >= 1) {
        final int FPS = (int) (framesDrawn / elapsedTime);
        framesDrawn = 0;
        frameClock.restart();
      }
    }
  }

  private final void initialize() {
    /*
     * Configure and create the window.
     */
    final int windowWidth = (int) windowDimensions.x;
    final int windowHeight = (int) windowDimensions.y;
    final VideoMode windowVideoMove = new VideoMode(windowWidth, windowHeight);
    final int windowStyle = WindowStyle.CLOSE | WindowStyle.TITLEBAR;
    final ContextSettings windowContextSettings = new ContextSettings(windowAntialiasing);
    window.create(windowVideoMove, windowTitle, windowStyle, windowContextSettings);
    window.setKeyRepeatEnabled(false);
    window.setVerticalSyncEnabled(windowVsync);
    window.setIcon(Resource.loadImage("icon"));
    /*
     * Configure and create a map.
     */
    TerrainGenerator generator = new TerrainGenerator(5, 2, 2, 5f);
    currentMap = generator.generate();
    player.setLocation(currentMap.getRandomValidLocation());
    currentMap.addEntity(player);
    /*rock = new InanimateObject(new Location(new Vector2f(1, 1), Direction.SOUTH), "rock");
    currentMap.addEntity(rock);
    deadpool = new NonplayerCharacter(new Location(new Vector2f(2, 2), Direction.SOUTH), "npc2_spriteset", "Joe");
    currentMap.addEntity(deadpool);
    InanimateObject tree = new InanimateObject(new Location(new Vector2f(3, 3), Direction.SOUTH), "tree", new Vector2i(32, 48));
    currentMap.addEntity(tree);*/
    /*
     * Configure other options.
     */
    usingController = Joystick.isConnected(0);

    camera = new Camera(window);
  }

  private final void getInput() {
    // final PlayerAction playerAction = player.getCurrentAction();
    for (Event event : window.pollEvents()) {
      switch (event.type) {
        case GAINED_FOCUS:
          windowFocus = true;
          break;
        case LOST_FOCUS:
          windowFocus = false;
          break;
        case JOYSTICK_CONNECETED:
        case JOYSTICK_DISCONNECTED:
          usingController = Joystick.isConnected(0);
          break;
        case CLOSED:
          window.close();
          break;
        case KEY_PRESSED:
          if (!usingController) {
            switch (event.asKeyEvent().key) {
              case W:
              case NUMPAD8:
                handleInput(InputAction.SELECT_UP);
                break;
              case S:
              case NUMPAD5:
                handleInput(InputAction.SELECT_DOWN);
                break;
              case A:
              case NUMPAD4:
                handleInput(InputAction.SELECT_LEFT);
                break;
              case D:
              case NUMPAD6:
                handleInput(InputAction.SELECT_RIGHT);
                break;
              case E:
              case NUMPAD9:
                handleInput(InputAction.INTERACT);
                break;
              case Q:
              case NUMPAD7:
                handleInput(InputAction.OPEN_INVENTORY);
                break;
              case ESCAPE:
                handleInput(InputAction.OPEN_MENU);
                break;
              case F1:
                handleInput(InputAction.DEV_MODE);
                break;
              default:
                break;
            }
          }
          break;
        case JOYSTICK_BUTTON_PRESSED:
          if (usingController) {
            switch (event.asJoystickButtonEvent().button) {
              case 0:
                handleInput(InputAction.INTERACT);
                break;
              case 2:
                handleInput(InputAction.OPEN_INVENTORY);
                break;
              case 7:
                handleInput(InputAction.OPEN_MENU);
                break;
              case 6:
                handleInput(InputAction.DEV_MODE);
                break;
              default:
                break;
            }
          }
          break;
        case JOYSTICK_MOVED:
          if (usingController) {
            final JoystickMoveEvent joystickEvent = event.asJoystickMoveEvent();
            switch (joystickEvent.joyAxis) {
              case POV_X:
                if (joystickEvent.position == 100) {
                  handleInput(InputAction.SELECT_UP);
                } else if (joystickEvent.position == -100) {
                  handleInput(InputAction.SELECT_DOWN);
                }
                break;
              case POV_Y:
                if (joystickEvent.position == 100) {
                  handleInput(InputAction.SELECT_RIGHT);
                } else if (joystickEvent.position == -100) {
                  handleInput(InputAction.SELECT_LEFT);
                }
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

    if (windowFocus) {
      if (!usingController) {
        if (Keyboard.isKeyPressed(Key.W) || Keyboard.isKeyPressed(Key.NUMPAD8)) {
          handleInput(InputAction.MOVE_UP);
        } else if (Keyboard.isKeyPressed(Key.S) || Keyboard.isKeyPressed(Key.NUMPAD5)) {
          handleInput(InputAction.MOVE_DOWN);
        } else if (Keyboard.isKeyPressed(Key.A) || Keyboard.isKeyPressed(Key.NUMPAD4)) {
          handleInput(InputAction.MOVE_LEFT);
        } else if (Keyboard.isKeyPressed(Key.D) || Keyboard.isKeyPressed(Key.NUMPAD6)) {
          handleInput(InputAction.MOVE_RIGHT);
        }
      } else {
        if (Joystick.getAxisPosition(0, Axis.POV_X) == 100) {
          handleInput(InputAction.MOVE_UP);
        } else if (Joystick.getAxisPosition(0, Axis.POV_X) == -100) {
          handleInput(InputAction.MOVE_DOWN);
        } else if (Joystick.getAxisPosition(0, Axis.POV_Y) == -100) {
          handleInput(InputAction.MOVE_LEFT);
        } else if (Joystick.getAxisPosition(0, Axis.POV_Y) == 100) {
          handleInput(InputAction.MOVE_RIGHT);
        }
      }
    }
  }

  private final void handleInput(final InputAction action) {
    switch (action) {
      case DEV_MODE:
        devMode = !devMode;
        break;
      case INTERACT:
        deadpool.move(Direction.SOUTH);
        break;
      case MOVE_DOWN:
        player.move(Direction.SOUTH);
        break;
      case MOVE_LEFT:
        player.move(Direction.WEST);
        break;
      case MOVE_RIGHT:
        player.move(Direction.EAST);
        break;
      case MOVE_UP:
        player.move(Direction.NORTH);
        break;
      case OPEN_INVENTORY:
        rock.move(Direction.SOUTH);
        break;
      case OPEN_MENU:
        break;
      case SELECT_DOWN:
        break;
      case SELECT_LEFT:
        break;
      case SELECT_RIGHT:
        break;
      case SELECT_UP:
        break;
      default:
        break;
    }
  }

  private final void updateLogic() {
    currentMap.update();
  }

  private final void handleDrawing() {
    window.clear();
    camera.targetEntity(player);
    window.draw(currentMap);
    window.display();
  }
}
