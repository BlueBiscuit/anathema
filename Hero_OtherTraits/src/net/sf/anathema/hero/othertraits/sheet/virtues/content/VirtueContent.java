package net.sf.anathema.hero.othertraits.sheet.virtues.content;

import net.sf.anathema.character.generic.traits.GenericTrait;
import net.sf.anathema.character.generic.traits.types.VirtueType;
import net.sf.anathema.character.main.model.othertraits.OtherTraitModelFetcher;
import net.sf.anathema.character.main.model.traits.TraitMap;
import net.sf.anathema.character.reporting.pdf.content.AbstractSubBoxContent;
import net.sf.anathema.character.reporting.pdf.content.general.NamedValue;
import net.sf.anathema.character.reporting.pdf.content.general.PrintTrait;
import net.sf.anathema.hero.model.Hero;
import net.sf.anathema.lib.resources.Resources;

public class VirtueContent extends AbstractSubBoxContent {

  private Hero hero;

  public VirtueContent(Resources resources, Hero hero) {
    super(resources);
    this.hero = hero;
  }

  public NamedValue getUpperLeftVirtue() {
    return getVirtue(VirtueType.Compassion);
  }

  public NamedValue getUpperRightVirtue() {
    return getVirtue(VirtueType.Temperance);
  }

  public NamedValue getLowerLeftVirtue() {
    return getVirtue(VirtueType.Conviction);
  }

  public NamedValue getLowerRightVirtue() {
    return getVirtue(VirtueType.Valor);
  }

  private NamedValue getVirtue(VirtueType type) {
    TraitMap virtueCollection = getVirtueCollection();
    GenericTrait virtue = virtueCollection.getTrait(type);
    Resources resources = getResources();
    return new PrintTrait(resources, virtue);
  }

  private TraitMap getVirtueCollection() {
    return OtherTraitModelFetcher.fetch(hero);
  }

  @Override
  public String getHeaderKey() {
    return "Virtues";
  }
}