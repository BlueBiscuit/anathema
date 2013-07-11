package net.sf.anathema.character.main.testing.dummy;

import net.sf.anathema.character.main.magic.advance.creation.MagicCosts;
import net.sf.anathema.character.main.magic.model.charm.Charm;
import net.sf.anathema.character.main.magic.model.charm.MartialArtsLevel;
import net.sf.anathema.character.main.magic.model.magic.Magic;
import net.sf.anathema.character.main.template.creation.BonusPointCosts;
import net.sf.anathema.character.main.template.experience.CurrentRatingCosts;
import net.sf.anathema.character.main.template.experience.ICostAnalyzer;
import net.sf.anathema.character.main.template.points.FixedValueRatingCosts;
import net.sf.anathema.character.main.traits.ValuedTraitType;

public class DummyBonusPointCosts implements BonusPointCosts, MagicCosts {

  private int getSpellCosts(ICostAnalyzer costMapping) {
    boolean isSorceryFavored = costMapping.isOccultFavored();
    return getCharmCosts(isSorceryFavored, null);
  }

  private int getCharmCosts(Charm charm, ICostAnalyzer costMapping) {
    return getCharmCosts(costMapping.isMagicFavored(charm), costMapping.getMartialArtsLevel(charm));
  }

  protected int getCharmCosts(boolean favored, MartialArtsLevel martialArtsLevel) {
    if (martialArtsLevel == null) {
      return favored ? 4 : 5;
    }
    if (martialArtsLevel.compareTo(MartialArtsLevel.Sidereal) < 0) {
      return favored ? 4 : 5;
    }
    throw new IllegalArgumentException("Sidereal Martial Arts shan't be learned at Hero Creation!");
  }

  @Override
  public CurrentRatingCosts getAbilityCosts(boolean favored) {
    if (favored) {
      return new FixedValueRatingCosts(1);
    }
    return new FixedValueRatingCosts(2);
  }

  @Override
  public MagicCosts getMagicCosts() {
    return this;
  }

  @Override
  public int getAttributeCosts(ValuedTraitType trait) {
    return new FixedValueRatingCosts(4).getRatingCosts(trait.getCurrentValue());
  }

  @Override
  public CurrentRatingCosts getVirtueCosts() {
    return new FixedValueRatingCosts(3);
  }

  @Override
  public int getMaximumFreeVirtueRank() {
    return 3;
  }

  @Override
  public int getMaximumFreeAbilityRank() {
    return 3;
  }

  @Override
  public int getWillpowerCosts() {
    return 2;
  }

  @Override
  public int getFavoredSpecialtyDotsPerPoint() {
    return 2;
  }

  @Override
  public int getDefaultSpecialtyDotsPerPoint() {
    return 1;
  }

  @Override
  public CurrentRatingCosts getEssenceCost() {
    return new FixedValueRatingCosts(7);
  }

  @Override
  public int getMagicCosts(Magic magic, final ICostAnalyzer analyzer) {
    if (magic instanceof Charm) {
      return getCharmCosts((Charm) magic, analyzer);
    }
    return getSpellCosts(analyzer);
  }
}