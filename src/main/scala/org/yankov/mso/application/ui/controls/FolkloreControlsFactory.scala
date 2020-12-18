package org.yankov.mso.application.ui.controls

import org.yankov.mso.application.converters.StringConverters._
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.EmptyValues._
import org.yankov.mso.application.model.SearchModel
import org.yankov.mso.application.model.SearchModel.{Operator, Variable}
import org.yankov.mso.application.{Main, Resources}

object FolkloreControlsFactory {
  private val dataManager = Main.dataManager

  def createSourceTypeInput(): LabeledComboBox[Option[SourceType]] = {
    val sourceTypes = dataManager.getSourceTypes.map(x => Option(x))
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
      cbItems = dataManager.getEthnographicRegions.map(x => Option(x)),
      value = value,
      itemToString = ethnographicRegionToString,
      emptyValue = Option(Option(emptyEthnographicRegion))
    )
  }

  def createSource(value: Option[Source]): LabeledComboBox[Option[Source]] = {
    LabeledComboBox[Option[Source]](
      labelText = Resources.TableColumns.source,
      cbItems = dataManager.getSources.map(x => Option(x)),
      value = value,
      itemToString = sourceToString,
      emptyValue = Option(Option(emptySource))
    )
  }

  def createSearchVariable(): LabeledComboBox[Variable[FolkloreTrack]] = {
    val variables = SearchModel.Variables.asList
    LabeledComboBox[Variable[FolkloreTrack]](
      labelText = Resources.Search.variable,
      cbItems = variables,
      value = variables.head,
      itemToString = variableToString,
      sortItems = false,
      nullable = false,
      emptyValue = Option.empty
    )
  }

  def createSearchOperator(): LabeledComboBox[Operator] = {
    val operators = SearchModel.Operators.asList
    LabeledComboBox[Operator](
      labelText = Resources.Search.operator,
      cbItems = operators,
      value = operators.head,
      itemToString = operatorToString,
      sortItems = false,
      nullable = false,
      emptyValue = Option.empty
    )
  }

  def createInstrument(): LabeledComboBox[Option[Instrument]] = {
    LabeledComboBox[Option[Instrument]](
      labelText = Resources.Controls.instrument,
      cbItems = dataManager.getInstruments.map(x => Option(x)),
      value = Option(emptyInstrument),
      itemToString = instrumentToString,
      emptyValue = Option(Option(emptyInstrument))
    )
  }

  private def filterArtists(missions: List[ArtistMission]): List[Option[Artist]] = {
    dataManager
      .getArtists
      .filter(x => x.missions.intersect(missions).nonEmpty)
      .map(x => Option(x))
  }
}
