package net.sf.anathema.hero.magic.costs;

import com.google.common.collect.ImmutableList;
import net.sf.anathema.character.main.costs.AbstractBonusPointTestCase;
import net.sf.anathema.character.main.library.trait.favorable.FavorableState;
import net.sf.anathema.character.main.magic.advance.MagicCostCalculator;
import net.sf.anathema.character.main.magic.model.spells.ISpell;
import net.sf.anathema.character.main.testing.BasicCharacterTestCase;
import net.sf.anathema.character.main.testing.dummy.DummyBonusPointCosts;
import net.sf.anathema.character.main.testing.dummy.DummyHero;
import net.sf.anathema.character.main.testing.dummy.magic.DummyCharmsModel;
import net.sf.anathema.character.main.testing.dummy.magic.DummySpell;
import net.sf.anathema.character.main.testing.dummy.magic.DummySpellModel;
import net.sf.anathema.character.main.traits.context.CreationTraitValueStrategy;
import net.sf.anathema.character.main.traits.types.AbilityType;
import net.sf.anathema.hero.spells.SpellModel;
import net.sf.anathema.hero.traits.TraitModel;
import net.sf.anathema.hero.traits.TraitModelFetcher;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CharmCostCalculatorTest extends AbstractBonusPointTestCase {

  private MagicCostCalculator calculator;
  private SpellModel spells;
  private TraitModel traitModel;

  @Before
  public void setUp() throws Exception {
    DummyCharmsModel charms = new DummyCharmsModel();
    spells = new DummySpellModel();
    DummyHero hero = new BasicCharacterTestCase().createModelContextWithEssence2(new CreationTraitValueStrategy());
    hero.addModel(charms);
    hero.addModel(spells);
    traitModel = TraitModelFetcher.fetch(hero);
    addAbilityAndEssence(traitModel, hero);
    hero.template.creationPoints.favoredCreationCharmCount = 2;
    hero.template.creationPoints.defaultCreationCharmCount = 3;
    calculator = new MagicCostCalculator(hero, new DummyBonusPointCosts());
  }

  @Test
  public void testNoSpellsLearned() {
    assertEquals(0, calculator.getGeneralCharmPicksSpent());
    assertEquals(0, calculator.getFavoredCharmPicksSpent());
    assertEquals(0, calculator.getBonusPointsSpent());
  }

  @Test
  public void testOneSpellLearnedOccultUnfavored() {
    spells.addSpells(Collections.<ISpell>singletonList(new DummySpell()));
    calculator.calculateMagicCosts();
    assertEquals(1, calculator.getGeneralCharmPicksSpent());
    assertEquals(0, calculator.getFavoredCharmPicksSpent());
    assertEquals(0, calculator.getBonusPointsSpent());
  }

  @Test
  public void testOneSpellLearnedOccultFavored() {
    setOccultFavored();
    spells.addSpells(Collections.<ISpell>singletonList(new DummySpell()));
    calculator.calculateMagicCosts();
    assertEquals(0, calculator.getGeneralCharmPicksSpent());
    assertEquals(1, calculator.getFavoredCharmPicksSpent());
    assertEquals(0, calculator.getBonusPointsSpent());
  }

  private void setOccultFavored() {
    traitModel.getTrait(AbilityType.Occult).getFavorization().setFavorableState(FavorableState.Favored);
  }

  @Test
  public void testUnfavoredSpellsOverflowToBonus() {
    DummySpell dummySpell = new DummySpell();
    spells.addSpells(ImmutableList.<ISpell>of(dummySpell, dummySpell, dummySpell, dummySpell));
    calculator.calculateMagicCosts();
    assertEquals(3, calculator.getGeneralCharmPicksSpent());
    assertEquals(0, calculator.getFavoredCharmPicksSpent());
    assertEquals(5, calculator.getBonusPointsSpent());
  }

  @Test
  public void testUnfavoredSpellsOverflowToBonusAndAreReset() {
    DummySpell dummySpell = new DummySpell();
    DummySpell dummySpellToRemove = new DummySpell();
    spells.addSpells(ImmutableList.<ISpell>of(dummySpell, dummySpell, dummySpell, dummySpellToRemove));
    calculator.calculateMagicCosts();
    assertEquals(3, calculator.getGeneralCharmPicksSpent());
    assertEquals(0, calculator.getFavoredCharmPicksSpent());
    assertEquals(5, calculator.getBonusPointsSpent());
    spells.removeSpells(Collections.<ISpell>singletonList(dummySpellToRemove), false);
    calculator.calculateMagicCosts();
    assertEquals(3, calculator.getGeneralCharmPicksSpent());
  }

  @Test
  public void removalRemovesBonusPointCost() {
    DummySpell dummySpell = new DummySpell();
    DummySpell dummySpellToRemove = new DummySpell();
    spells.addSpells(ImmutableList.<ISpell>of(dummySpell, dummySpell, dummySpell, dummySpellToRemove), false);
    spells.removeSpells(Collections.<ISpell>singletonList(dummySpellToRemove), false);
    calculator.calculateMagicCosts();
    assertThat(calculator.getBonusPointsSpent(), is(0));
  }

  @Test
  public void testFavoredSpellsOverflowToGeneralAndBonus() {
    setOccultFavored();
    DummySpell dummySpell = new DummySpell();
    spells.addSpells(ImmutableList.<ISpell>of(dummySpell, dummySpell, dummySpell, dummySpell, dummySpell, dummySpell));
    calculator.calculateMagicCosts();
    assertEquals(3, calculator.getGeneralCharmPicksSpent());
    assertEquals(2, calculator.getFavoredCharmPicksSpent());
    assertEquals(4, calculator.getBonusPointsSpent());
  }
}