package works.hirosuke.filebackup

import java.io.File

object PathData {

    val backupFolder = File(FileBackup().config.getString(ConfigItems.BACKUP_FOLDER.path) ?: "/backup/")
    val paths = mutableListOf<String>()
}