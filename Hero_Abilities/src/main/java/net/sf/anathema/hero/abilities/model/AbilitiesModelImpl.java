package net.sf.anathema.hero.abilities.model;

import net.sf.anathema.character.main.library.trait.Trait;
import net.sf.anathema.character.main.library.trait.favorable.IncrementChecker;
import net.sf.anathema.character.main.template.HeroTemplate;
import net.sf.anathema.character.main.template.abilities.GroupedTraitType;
import net.sf.anathema.character.main.traits.AbilityTemplateFactory;
import net.sf.anathema.character.main.traits.TraitType;
import net.sf.anathema.character.main.traits.creation.FavorableTraitFactory;
import net.sf.anathema.character.main.traits.creation.TypedTraitTemplateFactory;
import net.sf.anathema.character.main.traits.groups.IIdentifiedCasteTraitTypeGroup;
import net.sf.anathema.hero.concept.CasteCollection;
import net.sf.anathema.hero.concept.HeroConcept;
import net.sf.anathema.hero.concept.HeroConceptFetcher;
import net.sf.anathema.hero.framework.HeroEnvironment;
import net.sf.anathema.hero.model.Hero;
import net.sf.anathema.hero.model.HeroModel;
import net.sf.anathema.hero.model.change.ChangeAnnouncer;
import net.sf.anathema.hero.traits.DefaultTraitMap;
import net.sf.anathema.hero.traits.TraitMap;
import net.sf.anathema.hero.traits.TraitModel;
import net.sf.anathema.hero.traits.TraitModelFetcher;
import net.sf.anathema.hero.traits.model.event.FavoredChangedListener;
import net.sf.anathema.hero.traits.model.event.TraitValueChangedListener;
import net.sf.anathema.lib.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AbilitiesModelImpl extends DefaultTraitMap implements AbilitiesModel, HeroModel {

  private IIdentifiedCasteTraitTypeGroup[] abilityTraitGroups;
  private Hero hero;

  @Override
  public Identifier getId() {
    return ID;
  }

  @Override
  public void initialize(HeroEnvironment environment, Hero hero) {
    this.hero = hero;
    HeroConcept concept = HeroConceptFetcher.fetch(hero);
    HeroTemplate template = hero.getTemplate();
    CasteCollection casteCollection = concept.getCasteCollection();
    this.abilityTraitGroups = new AbilityTypeGroupFactory().createTraitGroups(casteCollection, template.getAbilityGroups());
    IncrementChecker incrementChecker = createFavoredAbilityIncrementChecker(template, this);
    addFavorableTraits(incrementChecker, new AbilityTemplateFactory(template.getTraitTemplateCollection().getTraitTemplateFactory()));
    TraitModel traitModel = TraitModelFetcher.fetch(hero);
    traitModel.addTraits(getAll());
  }

  @Override
  public void initializeListening(ChangeAnnouncer changeAnnouncer) {
    for (Trait ability : getAll()) {
      ability.getFavorization().addFavorableStateChangedListener(new FavoredChangedListener(changeAnnouncer));
      ability.addCurrentValueListener(new TraitValueChangedListener(changeAnnouncer, ability));
    }
  }

  private IncrementChecker createFavoredAbilityIncrementChecker(HeroTemplate template, TraitMap traitMap) {
    int maxFavoredAbilityCount = template.getCreationPoints().getAbilityCreationPoints().getFavorableTraitCount();
    List<TraitType> abilityTypes = new ArrayList<>();
    for (GroupedTraitType traitType : template.getAbilityGroups()) {
      abilityTypes.add(traitType.getTraitType());
    }
    return new FavoredIncrementChecker(maxFavoredAbilityCount, abilityTypes.toArray(new TraitType[abilityTypes.size()]), traitMap);
  }

  public void addFavorableTraits(IncrementChecker incrementChecker, TypedTraitTemplateFactory factory) {
    FavorableTraitFactory favorableTraitFactory = createFactory();
    for (IIdentifiedCasteTraitTypeGroup traitGroup : abilityTraitGroups) {
      Trait[] traits = favorableTraitFactory.createTraits(traitGroup, incrementChecker, factory);
      addTraits(traits);
    }
  }

  private FavorableTraitFactory createFactory() {
    return new FavorableTraitFactory(hero);
  }

  @Override
  public IIdentifiedCasteTraitTypeGroup[] getAbilityTypeGroups() {
    return abilityTraitGroups;
  }
}
