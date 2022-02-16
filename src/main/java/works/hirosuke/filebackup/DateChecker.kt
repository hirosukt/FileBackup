package works.hirosuke.filebackup

import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

object DateChecker {

    var today: Duration = Duration.ZERO

    fun isDayChanged(): Boolean {
        return Date().time.days != today
    }

    fun getFormattedDate(): String {
        return SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())
    }
}