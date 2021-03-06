package net.sf.anathema.hero.charms.display.presenter;

import net.sf.anathema.hero.charms.model.ICharmGroup;

public interface CharmGroupInformer {
  boolean hasGroupSelected();

  ICharmGroup getCurrentGroup();
}