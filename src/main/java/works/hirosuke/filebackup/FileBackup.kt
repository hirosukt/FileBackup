package works.hirosuke.filebackup

import org.bukkit.plugin.java.JavaPlugin
import works.hirosuke.filebackup.utils.runTaskTimer
import java.io.File
import java.util.*
import kotlin.time.Duration.Companion.days

class FileBackup: JavaPlugin() {

    companion object {
        lateinit var plugin: JavaPlugin
    }

    init {
        plugin = this
    }

    override fun onEnable() {
        saveResource("config.yml", false)
        DateChecker.today = Date().time.days
        PathData.backupFolder = File(config.getString(ConfigItems.BACKUP_FOLDER.path) ?: "./backup/")
        config.getStringList("path").forEach { PathData.paths.add(it) }

        if (!PathData.backupFolder.isDirectory) PathData.backupFolder.mkdir()
        if (config.getBoolean(ConfigItems.BACKUP_ON_START.path)) SaveFiles.backupAllFiles()

        runTaskTimer(0, (config.getInt(ConfigItems.CHECK_DATE_INTERVAL.path) * 1200).toLong()) {
            SaveFiles.backupWhenDayChanged()
        }
    }

    override fun onDisable() {

    }
}
