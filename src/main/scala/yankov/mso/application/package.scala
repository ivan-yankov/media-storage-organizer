package yankov.mso

import yankov.mso.application.model.DataModel._
import yankov.mso.application.model.DataModel.FolkloreTrack

package object application {
  type Id = String
  type MergeTrack = (FolkloreTrack, FolkloreTrack) => FolkloreTrack
  type MergeTrackMap = Map[String, MergeTrack]
}
