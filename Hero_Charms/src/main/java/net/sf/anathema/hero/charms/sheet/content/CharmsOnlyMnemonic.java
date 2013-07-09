package net.sf.anathema.hero.charms.sheet.content;

import net.sf.anathema.character.main.magic.model.magic.IMagicStats;
import net.sf.anathema.hero.magic.sheet.content.mnemonic.AbstractMagicMnemonic;

import java.util.List;

public class CharmsOnlyMnemonic extends AbstractMagicMnemonic {

  public CharmsOnlyMnemonic(List<IMagicStats> printMagic) {
    super(printMagic);
  }
}
