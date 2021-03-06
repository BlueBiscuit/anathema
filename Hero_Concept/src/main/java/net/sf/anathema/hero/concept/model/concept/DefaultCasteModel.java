package net.sf.anathema.hero.concept.model.concept;

import net.sf.anathema.hero.concept.CasteCollection;
import net.sf.anathema.hero.concept.CasteSelection;

public class DefaultCasteModel {

  private final CasteSelection selection;
  private final CasteCollection collection;

  public DefaultCasteModel(CasteSelection selection, CasteCollection collection) {
    this.selection = selection;
    this.collection = collection;
  }

  public CasteSelection getSelection() {
    return selection;
  }

  public CasteCollection getCollection() {
    return collection;
  }
}