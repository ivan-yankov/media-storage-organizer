package yankov.mso.application.ui.controls

import yankov.mso.application.converters.StringConverters._
import yankov.mso.application.model.DataModel._
import yankov.mso.application.search.SearchModel
import yankov.mso.application.search.SearchModel.{Filter, Variable}
import yankov.mso.application.{Main, Resources}

object FolkloreControlsFactory {
  private val dataManager = Main.dataManager

  def createSourceTypeInput(): LabeledComboBox[SourceType] = {
    val sourceTypes = dataManager.getSourceTypes
    LabeledComboBox[SourceType](
      labelText = Resources.Controls.sourceType,
      cbItems = () => sourceTypes,
      value = sourceTypes.head,
      itemToString = sourceTypeToString,
      emptyValue = Option.empty
    )
  }

  def createPerformer(value: Artist): LabeledComboBox[Artist] = {
    LabeledComboBox[Artist](
      labelText = Resources.TableColumns.performer,
      cbItems = () => filterArtists(List(Singer, Orchestra, InstrumentPlayer, Ensemble, Choir, ChamberGroup)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Artist())
    )
  }

  def createAccompanimentPerformer(value: Artist): LabeledComboBox[Artist] = {
    LabeledComboBox[Artist](
      labelText = Resources.TableColumns.accompanimentPerformer,
      cbItems = () => filterArtists(List(Orchestra, InstrumentPlayer, Ensemble, ChamberGroup)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Artist())
    )
  }

  def createArrangementAuthor(value: Artist): LabeledComboBox[Artist] = {
    LabeledComboBox[Artist](
      labelText = Resources.TableColumns.arrangementAuthor,
      cbItems = () => filterArtists(List(Composer)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Artist())
    )
  }

  def createConductor(value: Artist): LabeledComboBox[Artist] = {
    LabeledComboBox[Artist](
      labelText = Resources.TableColumns.conductor,
      cbItems = () => filterArtists(List(Conductor)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Artist())
    )
  }

  def createAuthor(value: Artist): LabeledComboBox[Artist] = {
    LabeledComboBox[Artist](
      labelText = Resources.TableColumns.author,
      cbItems = () => filterArtists(List(Composer)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Artist())
    )
  }

  def createSoloist(value: Artist): LabeledComboBox[Artist] = {
    LabeledComboBox[Artist](
      labelText = Resources.TableColumns.soloist,
      cbItems = () => filterArtists(List(Singer, InstrumentPlayer)),
      value = value,
      itemToString = artistToString,
      emptyValue = Option(Artist())
    )
  }

  def createEthnographicRegion(value: EthnographicRegion): LabeledComboBox[EthnographicRegion] = {
    LabeledComboBox[EthnographicRegion](
      labelText = Resources.TableColumns.ethnographicRegion,
      cbItems = () => dataManager.getEthnographicRegions,
      value = value,
      itemToString = ethnographicRegionToString,
      emptyValue = Option(EthnographicRegion())
    )
  }

  def createSource(value: Source): LabeledComboBox[Source] = {
    LabeledComboBox[Source](
      labelText = Resources.TableColumns.source,
      cbItems = () => dataManager.getSources,
      value = value,
      itemToString = sourceToString,
      emptyValue = Option(Source())
    )
  }

  def createSearchVariable(): LabeledComboBox[Variable] = {
    val variables = SearchModel.Variables.asList
    LabeledComboBox[Variable](
      labelText = Resources.Search.variable,
      cbItems = () => variables,
      value = variables.head,
      itemToString = variableToString,
      sortItems = false,
      emptyValue = Option.empty
    )
  }

  def createSearchFilter(): LabeledComboBox[Filter[FolkloreTrack]] = {
    val operators = SearchModel.Filters.asList
    LabeledComboBox[Filter[FolkloreTrack]](
      labelText = Resources.Search.filter,
      cbItems = () => operators,
      value = operators.head,
      itemToString = filterToString,
      sortItems = false,
      emptyValue = Option.empty
    )
  }

  private def filterArtists(missions: List[ArtistMission]): List[Artist] = {
    dataManager
      .getArtists
      .filter(x => x.missions.intersect(missions).nonEmpty)
  }
}