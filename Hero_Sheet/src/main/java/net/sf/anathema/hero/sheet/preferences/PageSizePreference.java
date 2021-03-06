package net.sf.anathema.hero.sheet.preferences;

import net.sf.anathema.framework.environment.ApplicationEnvironment;
import net.sf.anathema.framework.environment.Preferences;
import net.sf.anathema.framework.preferences.persistence.PreferenceValue;
import net.sf.anathema.framework.reporting.pdf.PageSize;

public class PageSizePreference {

  private Preferences preferences;

  public PageSizePreference(Preferences preferences) {
    this.preferences = preferences;
  }

  public PageSize getPageSize() {
    String value = preferences.getPreference(SheetPreferenceModel.KEY.key);
    if (value == ApplicationEnvironment.PREFERENCE_NOT_SET) {
      return PageSize.A4;
    }
    PreferenceValue preferenceValue = new PreferenceValue(value);
    return SheetPreferenceModel.calculatePageSize(preferenceValue);
  }
}