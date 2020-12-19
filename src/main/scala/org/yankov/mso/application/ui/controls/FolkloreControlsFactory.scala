package org.yankov.mso.application.ui.controls

import org.yankov.mso.application.converters.StringConverters._
import org.yankov.mso.application.model.DataModel._
import org.yankov.mso.application.model.SearchModel
import org.yankov.mso.application.model.SearchModel.{Operator, Variable}
import org.yankov.mso.application.{Main, Resources}

object FolkloreControlsFactory {
  private val dataManager = Main.dataManager

  def createSourceTypeInput(): LabeledComboBox[SourceType] = {
    val sourceTypes = dataManager.getSourceTypes
    LabeledComboBox[SourceType](
      labelText = Resources.Controls.sourceType,
      cbItems = sourceTypes,
      value = sourceTypes.head,
      itemToString = sourceTypeToString,
      emptyValue = Option(SourceType())
    )
  }

  def createPerformer(value: Artist): LabeledComboBox[Artist] = {
    LabeledComboBox[Artist](
      labelText = Resources.TableColumns.performer,
      cbItems = filterArtists(List(Singer, Orchestra, InstrumentPlayer, Ensemble, Choir, ChamberGroup)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Artist())
    )
  }

  def createAccompanimentPerformer(value: Artist): LabeledComboBox[Artist] = {
    LabeledComboBox[Artist](
      labelText = Resources.TableColumns.accompanimentPerformer,
      cbItems = filterArtists(List(Orchestra, InstrumentPlayer, Ensemble, ChamberGroup)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Artist())
    )
  }

  def createArrangementAuthor(value: Artist): LabeledComboBox[Artist] = {
    LabeledComboBox[Artist](
      labelText = Resources.TableColumns.arrangementAuthor,
      cbItems = filterArtists(List(Composer)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Artist())
    )
  }

  def createConductor(value: Artist): LabeledComboBox[Artist] = {
    LabeledComboBox[Artist](
      labelText = Resources.TableColumns.conductor,
      cbItems = filterArtists(List(Conductor)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Artist())
    )
  }

  def createAuthor(value: Artist): LabeledComboBox[Artist] = {
    LabeledComboBox[Artist](
      labelText = Resources.TableColumns.author,
      cbItems = filterArtists(List(Composer)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Artist())
    )
  }

  def createSoloist(value: Artist): LabeledComboBox[Artist] = {
    LabeledComboBox[Artist](
      labelText = Resources.TableColumns.soloist,
      cbItems = filterArtists(List(Singer, InstrumentPlayer)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Artist())
    )
  }

  def createEthnographicRegion(value: EthnographicRegion): LabeledComboBox[EthnographicRegion] = {
    LabeledComboBox[EthnographicRegion](
      labelText = Resources.TableColumns.ethnographicRegion,
      cbItems = dataManager.getEthnographicRegions,
      value = value,
      itemToString = ethnographicRegionToString,
      emptyValue = Option(EthnographicRegion())
    )
  }

  def createSource(value: Source): LabeledComboBox[Source] = {
    LabeledComboBox[Source](
      labelText = Resources.TableColumns.source,
      cbItems = dataManager.getSources,
      value = value,
      itemToString = sourceToString,
      emptyValue = Option(Source())
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
      emptyValue = Option.empty
    )
  }

  def createInstrument(): LabeledComboBox[Instrument] = {
    LabeledComboBox[Instrument](
      labelText = Resources.Controls.instrument,
      cbItems = dataManager.getInstruments,
      value = Instrument(),
      itemToString = instrumentToString,
      emptyValue = Option(Instrument())
    )
  }

  private def filterArtists(missions: List[ArtistMission]): List[Artist] = {
    dataManager
      .getArtists
      .filter(x => x.missions.intersect(missions).nonEmpty)
  }
}
