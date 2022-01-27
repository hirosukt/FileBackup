package works.hirosuke.filebackup;

import org.bukkit.plugin.java.JavaPlugin;
import works.hirosuke.filebackup.utils.runTaskTimer
import java.util.*
import kotlin.time.Duration.Companion.days

class FileBackup : JavaPlugin() {

    override fun onEnable() {
        saveResource("config.yml", false)
        config.getStringList("path").forEach { PathData.paths.add(it) }
        DateChecker.today = Date().time.days

        if (!PathData.backupFolder.isDirectory) PathData.backupFolder.mkdir()
        if (config.getBoolean(ConfigItems.BACKUP_ON_START.path)) SaveFiles.backupAllFiles()

        runTaskTimer(0, (config.getInt("checkDateInterval") * 60).toLong()) {
            SaveFiles.backupWhenDayChanged()
        }

    }

    override fun onDisable() {

    }
}
