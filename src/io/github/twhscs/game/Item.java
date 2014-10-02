package io.github.twhscs.game;

import java.nio.file.Paths;
import java.util.ArrayList;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RectangleShape;
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
public class Item extends Entity implements Drawable {

  public enum ItemType {
    HEALTH_POTION, FISH, GOLD
  }

  Font kenpixel = new Font();

  private Text displayedItemText;

  private String itemName;

  private Sprite itemSprite = new Sprite();

  private Texture itemTexture = new Texture();

  private Vector2i inventoryLocation;

  private int healthRegen;

  /**
   * Creates a new item
   * 
   * @param i is the type of item created
   */
  public Item(ItemType itemType) {

    try {
      kenpixel.loadFromFile(Paths.get("resources/kenpixel.ttf"));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    setItemAttributes(itemType);
  }


  /**
   * Assigns all the different attributes to the items
   * 
   * @param itemType is the type of item
   */
  public void setItemAttributes(ItemType itemType) {
    switch (itemType) {
      case HEALTH_POTION:
        itemName = "Health Potion";
        healthRegen = 20;
        displayedItemText = new Text(itemName, kenpixel, 16);
        setSprite("potion_health");
        break;
      case FISH:
        itemName = "Fish";
        healthRegen = 10;
        displayedItemText = new Text(itemName, kenpixel, 16);
        setSprite("fish");
      default:
        break;
    }
  }

  public void useItem() {

  }

  public void setDisplayPosition(int inventorySize, ArrayList<RectangleShape> inventorySlots,
      int rectSize) {
    /**
     * Center the item texture on the item slot
     */
    float offset = rectSize / 2 - (itemSprite.getScale().x * itemTexture.getSize().x) / 2;
    /**
     * Add offset to the itemslot coordinates to get correct position
     */
    Vector2f spritePosition =
        new Vector2f(inventorySlots.get(inventorySize).getPosition().x + offset, inventorySlots
            .get(inventorySize).getPosition().y + offset);
    /**
     * Set the position
     */
    itemSprite.setPosition(spritePosition);
  }

  public void draw(RenderTarget target, RenderStates states) {
    itemSprite.draw(target, states);
  }

  public void setSprite(String filename) {
    try {
      itemTexture.loadFromFile(Paths.get("resources/" + filename + ".png"));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    itemSprite.setTexture(itemTexture);
    itemSprite.setScale(new Vector2f(3, 3));
  }

  public void setDisplayedItemTextPosition(Vector2f newPosition) {
    displayedItemText.setPosition(newPosition);
  }

  public Text getDisplayedItemText() {
    return displayedItemText;
  }

}
