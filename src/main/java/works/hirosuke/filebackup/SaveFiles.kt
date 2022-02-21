package works.hirosuke.filebackup

import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.compress.utils.IOUtils
import works.hirosuke.filebackup.FileBackup.Companion.plugin
import works.hirosuke.filebackup.utils.runTaskAsync
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import kotlin.io.path.name
import kotlin.system.measureTimeMillis


object SaveFiles {

    fun backupAllFiles() {
        PathData.paths.forEach {
            val time = measureTimeMillis {
                val file = Paths.get(it)
                val dest = Paths.get(
                    "./${PathData.backupFolder.path}/${file.name}_${
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
                            if (p.toFile().isFile) Files.newInputStream(p).use { i -> IOUtils.copy(i, tar) }
                            tar.closeArchiveEntry()
                        } catch (e: IOException) {
                            throw RuntimeException(e)
                        }
                    }
                    if (plugin.config.getBoolean(ConfigItems.VERBOSE.path)) {
                        plugin.logger.info("§8$p archived in §9${time / 1000.0}s")
                    }
                }
                tar.finish()
            }
            plugin.logger.info("§6$it§a was saved in §9${time / 1000.0}s.")
        }
    }

    fun backupWithMessage() {
        val time = measureTimeMillis {
            backupAllFiles()
        }
        plugin.logger.info("§aAll files was saved in §9${time / 1000.0}s.")
    }
}