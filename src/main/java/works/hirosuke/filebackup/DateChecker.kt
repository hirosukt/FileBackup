package works.hirosuke.filebackup

import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

object DateChecker {

    var today: Duration = Duration.ZERO

    fun isDayChanged(): Boolean {
        return today != Date().time.days
    }
}