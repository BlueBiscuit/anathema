package net.sf.anathema.character.main.traits;

import net.sf.anathema.character.main.traits.types.AbilityType;
import net.sf.anathema.character.main.traits.types.AttributeType;
import net.sf.anathema.character.main.traits.types.OtherTraitType;
import net.sf.anathema.character.main.traits.types.VirtueType;

import java.util.ArrayList;

import static java.util.Collections.addAll;

public class TraitTypeUtils {

  private final ArrayList<TraitType> allPrerequisiteTypeList = new ArrayList<>();

  public TraitTypeUtils() {
    addAll(allPrerequisiteTypeList, AbilityType.values());
    addAll(allPrerequisiteTypeList, AttributeType.values());
    addAll(allPrerequisiteTypeList, VirtueType.values());
    addAll(allPrerequisiteTypeList, OtherTraitType.values());
  }

  public TraitType getTraitTypeById(String id) {
    for (TraitType type : allPrerequisiteTypeList) {
      if (id.equals(type.getId())) {
        return type;
      }
    }
    throw new IllegalArgumentException("No trait type with id: " + id);
  }
}