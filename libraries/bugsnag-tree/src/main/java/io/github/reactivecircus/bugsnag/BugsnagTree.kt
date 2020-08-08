package io.github.reactivecircus.bugsnag

import android.util.Log.ASSERT
import android.util.Log.ERROR
import android.util.Log.INFO
import android.util.Log.WARN
import com.bugsnag.android.Bugsnag
import com.bugsnag.android.Event
import com.bugsnag.android.Severity
import timber.log.Timber
import java.util.Locale

/**
 * A logging implementation which buffers the last 200 messages and notifies on error exceptions.
 */
class BugsnagTree : Timber.Tree() {

    // Adding one to the initial size accounts for the add before remove.
    private val buffer = ArrayDeque<String>(BUFFER_SIZE + 1)

    private val capacityOffset = 16

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        // only log WARN, ERROR, and WTF
        return priority == WARN || priority == ERROR || priority == ASSERT
    }

    @Suppress("ComplexMethod")
    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {

        val log = buildString(message.length + capacityOffset) {
            append(System.currentTimeMillis())
            append(' ')
            append(
                when (priority) {
                    ERROR -> "E"
                    WARN -> "W"
                    INFO -> "I"
                    else -> priority.toString()
                }
            )
            append(' ')
            append(message)
        }

        synchronized(buffer) {
            buffer.addLast(log)
            if (buffer.size > BUFFER_SIZE) {
                buffer.removeFirst()
            }
        }

        if (throwable != null) {
            when (priority) {
                ERROR -> Bugsnag.notify(throwable) {
                    it.severity = Severity.ERROR
                    true
                }
                WARN -> Bugsnag.notify(throwable) {
                    it.severity = Severity.WARNING
                    true
                }
                INFO -> Bugsnag.notify(throwable) {
                    it.severity = Severity.INFO
                    true
                }
            }
        }
    }

    fun update(event: Event) {
        synchronized(buffer) {
            buffer.forEachIndexed { index, message ->
                event.addMetadata("Log", String.format(Locale.getDefault(), "%03d", index), message)
            }
        }
    }

    companion object {
        private const val BUFFER_SIZE = 200
    }
}
