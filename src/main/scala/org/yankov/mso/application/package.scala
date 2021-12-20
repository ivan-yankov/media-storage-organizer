package org.yankov.mso

import org.yankov.mso.application.model.DataModel._

package object application {
  type Id = String
  type MergeTrack = (FolkloreTrack, FolkloreTrack) => FolkloreTrack
  type MergeTrackMap = Map[Int, MergeTrack]
}
