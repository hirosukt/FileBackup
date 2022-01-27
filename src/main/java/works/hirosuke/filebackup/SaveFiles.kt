package works.hirosuke.filebackup

import org.kamranzafar.jtar.TarEntry
import org.kamranzafar.jtar.TarOutputStream
import java.io.*
import kotlin.system.measureTimeMillis


object SaveFiles {

    fun backupAllFiles() {
        PathData.paths.forEach {
            val dest = FileOutputStream("${PathData.backupFolder.path}${it.substringAfterLast("/")}.tar")
            val out = TarOutputStream(BufferedOutputStream(dest))

            val filesToTar = mutableListOf<File>()
            File(it).listFiles().forEach { file ->
                filesToTar.add(file)
            }

            filesToTar.forEach { file ->
                out.putNextEntry(TarEntry(file, file.name))
                val origin = BufferedInputStream(FileInputStream(file))
                var count: Int
                val data = ByteArray(2048)
                while (origin.read(data).also { read -> count = read } != -1) {
                    out.write(data, 0, count)
                }
                out.flush()
                origin.close()
            }

            out.close()
        }
    }

    fun backupWhenDayChanged() {
        if (DateChecker.isDayChanged()) {
            val time = measureTimeMillis {
                backupAllFiles()
            }
            FileBackup().logger.info("Files was backuped in ${time / 1000.0}s.")
        }
    }
}