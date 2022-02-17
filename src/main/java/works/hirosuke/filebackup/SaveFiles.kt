package works.hirosuke.filebackup

import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.compress.utils.IOUtils
import works.hirosuke.filebackup.FileBackup.Companion.plugin
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import kotlin.io.path.name
import kotlin.system.measureTimeMillis


object SaveFiles {

    fun backupAllFiles() {
        PathData.paths.forEach {
            val file = Paths.get(it)
            val dest = Paths.get("./${PathData.backupFolder.path}/${file.name}_${SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())}.tar.gz")

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

    fun backupWithMessage() {
        val time = measureTimeMillis {
            backupAllFiles()
        }
        plugin.logger.info("Files was saved in ${time / 1000.0}s.")
    }
}