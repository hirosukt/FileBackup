package works.hirosuke.filebackup

import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.compress.utils.IOUtils
import works.hirosuke.filebackup.FileBackup.Companion.plugin
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.name
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.days


object SaveFiles {

    fun backupAllFiles() {
        PathData.paths.forEach {
            val file = Paths.get(it)
            val dest = Paths.get("./${PathData.backupFolder.path}/${file.name}_${DateChecker.getFormattedDate()}.tar.gz")

            Files.newOutputStream(dest).use { fo ->
                GzipCompressorOutputStream(fo).use { gzo ->
                    TarArchiveOutputStream(gzo).use { out ->
                        Files.walk(file).use { stream ->
                            stream.forEach { p: Path ->
                                try {
                                    val entry = out.createArchiveEntry(p.toFile(), p.subpath(file.nameCount - 1, p.nameCount).toString())
                                    out.putArchiveEntry(entry)
                                    if (p.toFile().isFile) Files.newInputStream(p).use { i -> IOUtils.copy(i, out) }
                                    out.closeArchiveEntry()
                                } catch (e: IOException) {
                                    throw RuntimeException(e)
                                }
                            }
                            out.finish()
                        }
                    }
                }
            }
        }
    }

    fun backupWhenDayChanged() {
        if (DateChecker.isDayChanged()) {
            val time = measureTimeMillis {
                backupAllFiles()
            }
            DateChecker.today = Date().time.days
            plugin.logger.info("Files was backuped in ${time / 1000.0}s.")
        }
    }
}