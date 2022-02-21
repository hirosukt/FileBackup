package works.hirosuke.filebackup

enum class ConfigItems(val path: String) {
    PATHS("path"),
    BACKUP_FOLDER("backupFolder"),
    BACKUP_ON_START("backupOnStart"),
    BACKUP_ON_END("backupOnEnd"),
    CHECK_DATE_INTERVAL("saveInterval"),
    VERBOSE("verbose")
    ;
}
