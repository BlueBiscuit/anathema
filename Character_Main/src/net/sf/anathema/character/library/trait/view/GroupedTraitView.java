package net.sf.anathema.character.library.trait.view;

import net.sf.anathema.character.library.trait.Trait;
import net.sf.anathema.character.presenter.ExtensibleTraitView;

public interface GroupedTraitView {
  void startNewGroup(String groupLabel);

  ExtensibleTraitView addExtensibleTraitView(String labelText, int value, int maxValue, Trait trait);
}