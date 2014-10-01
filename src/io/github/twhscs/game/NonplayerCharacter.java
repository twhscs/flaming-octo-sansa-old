package io.github.twhscs.game;

public class NonplayerCharacter extends Entity {
  
  private String name;
  
  public NonplayerCharacter(final Location location, final String fileName, final String name) {
    super(location);
    setSprite(new AnimateSprite("npc/spriteset/" + fileName, this));
    this.name = name;
  }
  
}
