package net.sf.anathema.hero.magic.advance.creation;

import net.sf.anathema.character.main.magic.model.magic.Magic;
import net.sf.anathema.hero.advance.CostAnalyzer;

public interface MagicCosts {

  int getMagicCosts(Magic magic, CostAnalyzer analyzer);
}