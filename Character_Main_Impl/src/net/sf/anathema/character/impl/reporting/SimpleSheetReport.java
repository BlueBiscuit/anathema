package net.sf.anathema.character.impl.reporting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.anathema.character.generic.character.IGenericCharacter;
import net.sf.anathema.character.generic.character.IGenericDescription;
import net.sf.anathema.character.generic.framework.ICharacterGenerics;
import net.sf.anathema.character.generic.framework.module.object.ICharacterModuleObjectMap;
import net.sf.anathema.character.generic.rules.IExaltedEdition;
import net.sf.anathema.character.generic.template.ICharacterTemplate;
import net.sf.anathema.character.generic.traits.types.OtherTraitType;
import net.sf.anathema.character.generic.type.ICharacterType;
import net.sf.anathema.character.impl.generic.GenericDescription;
import net.sf.anathema.character.impl.util.GenericCharacterUtilities;
import net.sf.anathema.character.model.ICharacter;
import net.sf.anathema.character.reporting.CharacterReportingModule;
import net.sf.anathema.character.reporting.CharacterReportingModuleObject;
import net.sf.anathema.character.reporting.pdf.content.ReportContent;
import net.sf.anathema.character.reporting.pdf.content.ReportContentRegistry;
import net.sf.anathema.character.reporting.pdf.rendering.graphics.SheetGraphics;
import net.sf.anathema.character.reporting.pdf.rendering.page.IPdfPageEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.page.PdfPageConfiguration;
import net.sf.anathema.character.reporting.pdf.layout.simple.ISimplePartEncoder;
import net.sf.anathema.character.reporting.pdf.layout.simple.PdfFirstPageEncoder;
import net.sf.anathema.character.reporting.pdf.layout.simple.PdfSecondPageEncoder;
import net.sf.anathema.character.reporting.pdf.layout.simple.SimpleEncodingRegistry;
import net.sf.anathema.framework.itemdata.model.IItemData;
import net.sf.anathema.framework.reporting.IITextReport;
import net.sf.anathema.framework.reporting.ReportException;
import net.sf.anathema.framework.repository.IItem;
import net.sf.anathema.lib.resources.IResources;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class SimpleSheetReport implements IITextReport {

  private final IResources resources;
  private final ICharacterGenerics characterGenerics;
  private final PageSize pageSize;

  public SimpleSheetReport(IResources resources, ICharacterGenerics characterGenerics, PageSize pageSize) {
    this.resources = resources;
    this.characterGenerics = characterGenerics;
    this.pageSize = pageSize;
  }

  @Override
  public String toString() {
    return resources.getString("CharacterModule.Reporting.SecondEdition.Name") + " (" + resources.getString("PageSize." + pageSize.name()) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
  }

  public void performPrint(IItem item, Document document, PdfWriter writer) throws ReportException {
    ICharacter stattedCharacter = (ICharacter) item.getItemData();
    document.setPageSize(pageSize.getRectangle());
    document.open();
    PdfContentByte directContent = writer.getDirectContent();
    PdfPageConfiguration configuration = PdfPageConfiguration.create(pageSize.getRectangle());
    SimpleEncodingRegistry encodingRegistry = getEncodingRegistry();
    try {
      int traitMax = Math.max(5, getEssenceMax(stattedCharacter));
      ISimplePartEncoder partEncoder = getPartEncoder(stattedCharacter);
      IGenericCharacter character = GenericCharacterUtilities.createGenericCharacter(stattedCharacter.getStatistics());
      IGenericDescription description = new GenericDescription(stattedCharacter.getDescription());
      List<IPdfPageEncoder> encoderList = new ArrayList<IPdfPageEncoder>();
      encoderList.add(new PdfFirstPageEncoder(partEncoder, encodingRegistry, resources, traitMax, configuration));
      Collections.addAll(encoderList, partEncoder.getAdditionalPages(configuration));
      if (partEncoder.hasSecondPage()) {
        encoderList.add(new PdfSecondPageEncoder(resources, encodingRegistry, configuration));
      }
      boolean isFirstPrinted = false;
      for (IPdfPageEncoder encoder : encoderList) {
        if (isFirstPrinted) {
          document.newPage();
        }
        else {
          isFirstPrinted = true;
        }
        SheetGraphics graphics = new SheetGraphics(directContent, encodingRegistry.getBaseFont(), encodingRegistry.getSymbolBaseFont());
        ReportContent content = new ReportContent(getContentRegistry(), character, description);
        encoder.encode(document, graphics, content);
      }
    }
    catch (Exception e) {
      throw new ReportException(e);
    }
  }

  private int getEssenceMax(ICharacter character) {
    return character.getStatistics().getTraitConfiguration().getTrait(OtherTraitType.Essence).getMaximalValue();
  }

  private ISimplePartEncoder getPartEncoder(ICharacter character) {
    SimpleEncodingRegistry encodingRegistry = getEncodingRegistry();
    ICharacterTemplate characterTemplate = character.getStatistics().getCharacterTemplate();
    ICharacterType characterType = characterTemplate.getTemplateType().getCharacterType();
    IExaltedEdition edition = characterTemplate.getEdition();
    return encodingRegistry.getPartEncoder(characterType, edition);
  }

  private SimpleEncodingRegistry getEncodingRegistry() {
    ICharacterModuleObjectMap moduleObjectMap = characterGenerics.getModuleObjectMap();
    CharacterReportingModuleObject moduleObject = moduleObjectMap.getModuleObject(CharacterReportingModule.class);
    return moduleObject.getSimpleEncodingRegistry();
  }

  private ReportContentRegistry getContentRegistry() {
    ICharacterModuleObjectMap moduleObjectMap = characterGenerics.getModuleObjectMap();
    CharacterReportingModuleObject moduleObject = moduleObjectMap.getModuleObject(CharacterReportingModule.class);
    return moduleObject.getReportContentRegistry();
  }

  public boolean supports(IItem item) {
    if (item == null) {
      return false;
    }
    IItemData itemData = item.getItemData();
    if (!(itemData instanceof ICharacter)) {
      return false;
    }
    ICharacter character = (ICharacter) itemData;
    if (!character.hasStatistics()) {
      return false;
    }
    return getPartEncoder(character) != null;
  }
}
