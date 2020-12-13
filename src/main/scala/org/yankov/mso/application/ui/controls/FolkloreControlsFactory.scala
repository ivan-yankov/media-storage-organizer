package org.yankov.mso.application.ui.controls

import org.yankov.mso.application.Resources
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.{DataModelOperations, FolkloreSearchFactory}
import org.yankov.mso.application.converters.StringConverters._
import org.yankov.mso.application.model.EmptyValues._

object FolkloreControlsFactory {
  def createSourceTypeInput(): LabeledComboBox[Option[SourceType]] = {
    val sourceTypes = DataModelOperations.getSourceTypes.map(x => Option(x))
    LabeledComboBox[Option[SourceType]](
      labelText = Resources.Controls.sourceType,
      cbItems = sourceTypes,
      value = sourceTypes.head,
      itemToString = sourceTypeToString,
      emptyValue = Option(Option(emptySourceType))
    )
  }

  def createPerformer(value: Option[Artist]): LabeledComboBox[Option[Artist]] = {
    LabeledComboBox[Option[Artist]](
      labelText = Resources.TableColumns.performer,
      cbItems = filterArtists(List(Singer, Orchestra, InstrumentPlayer, Ensemble, Choir, ChamberGroup)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Option(emptyArtist))
    )
  }

  def createAccompanimentPerformer(value: Option[Artist]): LabeledComboBox[Option[Artist]] = {
    LabeledComboBox[Option[Artist]](
      labelText = Resources.TableColumns.accompanimentPerformer,
      cbItems = filterArtists(List(Orchestra, InstrumentPlayer, Ensemble, ChamberGroup)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Option(emptyArtist))
    )
  }

  def createArrangementAuthor(value: Option[Artist]): LabeledComboBox[Option[Artist]] = {
    LabeledComboBox[Option[Artist]](
      labelText = Resources.TableColumns.arrangementAuthor,
      cbItems = filterArtists(List(Composer)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Option(emptyArtist))
    )
  }

  def createConductor(value: Option[Artist]): LabeledComboBox[Option[Artist]] = {
    LabeledComboBox[Option[Artist]](
      labelText = Resources.TableColumns.conductor,
      cbItems = filterArtists(List(Conductor)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Option(emptyArtist))
    )
  }

  def createAuthor(value: Option[Artist]): LabeledComboBox[Option[Artist]] = {
    LabeledComboBox[Option[Artist]](
      labelText = Resources.TableColumns.author,
      cbItems = filterArtists(List(Composer)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Option(emptyArtist))
    )
  }

  def createSoloist(value: Option[Artist]): LabeledComboBox[Option[Artist]] = {
    LabeledComboBox[Option[Artist]](
      labelText = Resources.TableColumns.soloist,
      cbItems = filterArtists(List(Singer, InstrumentPlayer)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Option(emptyArtist))
    )
  }

  def createEthnographicRegion(value: Option[EthnographicRegion]): LabeledComboBox[Option[EthnographicRegion]] = {
    LabeledComboBox[Option[EthnographicRegion]](
      labelText = Resources.TableColumns.ethnographicRegion,
      cbItems = DataModelOperations.getEthnographicRegions.map(x => Option(x)),
      value = value,
      itemToString = ethnographicRegionToString,
      emptyValue = Option(Option(emptyEthnographicRegion))
    )
  }

  def createSource(value: Option[Source]): LabeledComboBox[Option[Source]] = {
    LabeledComboBox[Option[Source]](
      labelText = Resources.TableColumns.source,
      cbItems = DataModelOperations.getSources.map(x => Option(x)),
      value = value,
      itemToString = sourceToString,
      emptyValue = Option(Option(emptySource))
    )
  }

  def createSearchVariable(value: Variable[FolkloreTrack]): LabeledComboBox[Variable[FolkloreTrack]] = {
    LabeledComboBox[Variable[FolkloreTrack]](
      labelText = Resources.Search.variable,
      cbItems = FolkloreSearchFactory.createVariables,
      value = value,
      itemToString = variableToString,
      sortItems = false,
      nullable = false,
      emptyValue = Option.empty
    )
  }

  def createSearchOperators(value: Operator): LabeledComboBox[Operator] = {
    LabeledComboBox[Operator](
      labelText = Resources.Search.operator,
      cbItems = FolkloreSearchFactory.createOperators,
      value = value,
      itemToString = operatorToString,
      sortItems = false,
      nullable = false,
      emptyValue = Option.empty
    )
  }

  def createInstrument(): LabeledComboBox[Option[Instrument]] = {
    LabeledComboBox[Option[Instrument]](
      labelText = Resources.Controls.instrument,
      cbItems = DataModelOperations.getInstruments.map(x => Option(x)),
      value = Option(emptyInstrument),
      itemToString = instrumentToString,
      emptyValue = Option(Option(emptyInstrument))
    )
  }

  private def filterArtists(missions: List[ArtistMission]): List[Option[Artist]] = {
    DataModelOperations
      .getArtists
      .filter(x => x.missions.intersect(missions).nonEmpty)
      .map(x => Option(x))
  }
}
