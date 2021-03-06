package net.sf.anathema.fx.hero.perspective;

import net.sf.anathema.framework.repository.Item;
import net.sf.anathema.hero.framework.perspective.CharacterStackBridge;
import net.sf.anathema.hero.framework.perspective.model.CharacterIdentifier;
import net.sf.anathema.platform.fx.NodeHolder;

public class CharacterStackFxBridge implements CharacterStackBridge {

  private final StackView stackView;
  private final CharacterViewFactory viewFactory;

  public CharacterStackFxBridge(CharacterViewFactory viewFactory, StackView stackView) {
    this.viewFactory = viewFactory;
    this.stackView = stackView;
  }

  @Override
  public void addViewForCharacter(CharacterIdentifier identifier, Item item) {
    NodeHolder itemView = viewFactory.createView(item);
    stackView.addView(identifier, itemView);
  }

  @Override
  public void showCharacterView(CharacterIdentifier identifier) {
    stackView.showView(identifier);
  }
}
