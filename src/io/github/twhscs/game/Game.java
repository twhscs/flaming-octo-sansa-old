package io.github.twhscs.game;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2i;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The main class of the game. Contains the main loop and pieces everything together.
 * @author Robert
 *
 */
public class Game { 
  private RenderWindow renderWindow = new RenderWindow();
  private final String renderWindowTitle = "Game";
  private final Vector2i renderWindowDimensions = new Vector2i(640, 480);
  private Player player;
  private ArrayList<Map> maps = new ArrayList<Map>();
  
  public static void main(String[] args) {
    Game g = new Game();
    g.run();
  }
  
  public Map getRandomMap() {
    Collections.shuffle(maps);
    return maps.get(0);
  }
  
  public void handleInitialization() {
    renderWindow.create(new VideoMode(renderWindowDimensions.x, renderWindowDimensions.y), 
                                                                       renderWindowTitle);
    player = new Player();

    maps.add(new Map(10, 10, Tile.SAND));
    maps.add(new Map (5, 4, Tile.WATER));
    maps.add(new Map(15, 20, Tile.GRASS));
    
    player.changeMap(getRandomMap());
  }
  
  public void run() {
    handleInitialization();
    while (renderWindow.isOpen()) {
      handleInput();
      handleLogic();
      handleDrawing();
    }
  }
  
  public void handleInput() {
    for (Event event : renderWindow.pollEvents()) {
      switch(event.type) {
        case CLOSED:
          renderWindow.close();
          break;
        case KEY_PRESSED:
          switch(event.asKeyEvent().key) {
            case W:
            case UP:
              player.move(Direction.NORTH);
              break;
            case S:
            case DOWN:
              player.move(Direction.SOUTH);
              break;
            case A:
            case LEFT:
              player.move(Direction.WEST);
              break;
            case D:
            case RIGHT:
              player.move(Direction.EAST);
              break;
            case N:
              player.resetLocation();
              player.changeMap(getRandomMap());
              break;
            default:
              break;
          }
          break;
        default:
          break;
      }
    }
  }
  
  public void handleLogic() {
    
  }
  
  public void handleDrawing() {
    renderWindow.clear();
    renderWindow.draw(player.getMap());
    renderWindow.draw(player);
    renderWindow.display();
  }
}
