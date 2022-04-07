package works.hirosuke.filebackup

import net.md_5.bungee.api.ChatColor
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.compress.utils.IOUtils
import works.hirosuke.filebackup.FileBackup.Companion.plugin
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import kotlin.io.path.deleteIfExists
import kotlin.io.path.name
import kotlin.system.measureTimeMillis


object SaveFiles {

    fun backupAllFiles() {
        if (plugin.config.getBoolean(ConfigItems.DELETE_OLD_FILES.path)) deleteAllOldFiles()
        PathData.paths.forEach {
            val time = measureTimeMillis {
                val file = Paths.get(it)
                val dest = Paths.get(
                    "${PathData.backupFolder.path}/${file.name}_${
                        SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())
                    }.tar.gz"
                )

                val tar = TarArchiveOutputStream(GzipCompressorOutputStream(Files.newOutputStream(dest)))
                Files.walk(file).forEach { p ->
                    val time = measureTimeMillis {
                        try {
                            val entry =
                                tar.createArchiveEntry(
                                    p.toFile(),
                                    p.subpath(file.nameCount - 1, p.nameCount).toString()
                                )
                            tar.putArchiveEntry(entry)
                            if (p.toFile().isFile) IOUtils.copy(Files.newInputStream(p), tar)
                            tar.closeArchiveEntry()
                        } catch (e: IOException) {
                            throw RuntimeException(e)
                        }
                    }
                    if (plugin.config.getBoolean(ConfigItems.VERBOSE.path)) {
                        plugin.logger.info("$p archived in ${time / 1000.0}s")
                    }
                }
                tar.finish()
            }
            plugin.logger.info("$it was saved in ${time / 1000.0}s.")
        }
    }

    fun backupWithMessage() {
        val time = measureTimeMillis {
            backupAllFiles()
        }
        plugin.logger.info("All files was saved in ${time / 1000.0}s.")
    }

    private fun deleteOldFiles(target: String) {
        val dest = Paths.get(PathData.backupFolder.path)
        Files.walk(dest).skip(1).filter { it.name.contains(target.substringBeforeLast('/').substringAfterLast('/')) }.forEach {
            it.deleteIfExists()
        }
    }

    private fun deleteAllOldFiles() {
        PathData.paths.forEach {
            deleteOldFiles(it)
        }
    }
}