package net.sf.anathema.hero.experience.display;

import net.sf.anathema.character.main.advance.IExperiencePointEntry;

public interface ExperienceConfigurationViewListener {

  void addRequested();

  void removeRequested(IExperiencePointEntry entry);

  void selectionChanged(IExperiencePointEntry entry);
}