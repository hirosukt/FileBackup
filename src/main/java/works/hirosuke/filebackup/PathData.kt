package works.hirosuke.filebackup

import java.io.File

object PathData {

    var backupFolder = File("./backup/")
    val paths = mutableListOf<String>()
}
