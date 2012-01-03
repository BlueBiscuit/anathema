package net.sf.anathema.character.reporting.second.rendering.health;

import net.sf.anathema.character.reporting.pdf.content.BasicContent;
import net.sf.anathema.character.reporting.pdf.rendering.EncoderIds;
import net.sf.anathema.character.reporting.pdf.rendering.boxes.AbstractBoxContentEncoderFactory;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.IBoxContentEncoder;
import net.sf.anathema.lib.resources.IResources;

public class HealthAndMovementBoxEncoderFactory extends AbstractBoxContentEncoderFactory {

  public HealthAndMovementBoxEncoderFactory() {
    super(EncoderIds.HEALTH_AND_MOVEMENT);
  }

  @Override
  public IBoxContentEncoder create(IResources resources, BasicContent content) {
    return new HealthAndMovementEncoder(resources);
  }

  @Override
  public boolean supports(BasicContent content) {
    return content.isSecondEdition();
  }
}