package works.hirosuke.filebackup;

import org.bukkit.plugin.java.JavaPlugin;
import works.hirosuke.filebackup.utils.runTaskTimer
import java.io.File
import java.util.*
import kotlin.time.Duration.Companion.days

class FileBackup : JavaPlugin() {

    override fun onEnable() {
        saveResource("config.yml", false)

        config.getStringList("path").forEach {
            PathData.paths.add(it)
        }

        if (!PathData.backupFolder.isDirectory) {
            PathData.backupFolder.mkdir()
        }

        runTaskTimer(0, (config.getInt("checkDateInterval") * 60).toLong()) {
            SaveFiles.backupWhenDayChange()
        }

        DateChecker.today = Date().time.days
    }

    override fun onDisable() {

    }
}
