package net.sf.anathema.hero.framework.perspective;

import net.sf.anathema.framework.IApplicationModel;
import net.sf.anathema.framework.environment.Environment;
import net.sf.anathema.framework.view.perspective.Container;
import net.sf.anathema.framework.view.perspective.Perspective;
import net.sf.anathema.framework.view.perspective.PerspectiveAutoCollector;
import net.sf.anathema.framework.view.perspective.PerspectiveToggle;
import net.sf.anathema.fx.hero.perspective.CharacterStackFxBridge;
import net.sf.anathema.framework.environment.dependencies.Weight;
import net.sf.anathema.lib.file.RelativePath;
import net.sf.anathema.fx.hero.perspective.CharacterSystemView;
import net.sf.anathema.fx.hero.perspective.CharacterViewFactory;

@PerspectiveAutoCollector
@Weight(weight = 1)
public class CharacterSystemPerspective implements Perspective {

  @Override
  public void configureToggle(PerspectiveToggle toggle) {
    toggle.setIcon(new RelativePath("icons/King-icon.png"));
    toggle.setTooltip("CharacterSystem.Perspective.Name");
  }

  @Override
  public void initContent(Container container, IApplicationModel applicationModel, Environment environment) {
    CharacterSystemModel systemModel = new CharacterSystemModel(applicationModel);
    CharacterSystemView view = new CharacterSystemView();
    container.setContent(view.getNode());
    CharacterViewFactory viewFactory = new CharacterViewFactory(environment, applicationModel);
    CharacterStackBridge bridge = new CharacterStackFxBridge(viewFactory, view.getStackView());
    CharacterStackPresenter stackPresenter = new CharacterStackPresenter(bridge, systemModel);
    ShowOnSelect showOnSelect = new ShowOnSelect(stackPresenter);
    CharacterGridPresenter gridPresenter = new CharacterGridPresenter(systemModel, view.getGridView(), showOnSelect, environment);
    gridPresenter.initPresentation();
    new InteractionPresenter(systemModel, view.getInteractionView(), environment, view.getGridView(), showOnSelect).initPresentation();
  }
}
