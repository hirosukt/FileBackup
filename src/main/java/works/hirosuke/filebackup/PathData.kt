package works.hirosuke.filebackup

import java.io.File

object PathData {

    val backupFolder = File(FileBackup().config.getString("backupFolder") ?: "/backup/")
    val paths = mutableListOf<String>()
}