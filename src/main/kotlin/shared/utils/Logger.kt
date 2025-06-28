package com.shared.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Logs structured messages with timestamps and categorized actionLog levels.
 *
 * @param data The data to actionLog.
 * @param endpoint The API endpoint or action related to the actionLog.
 * @param details The details name where the actionLog was triggered.
 * @param level The actionLog level (INFO, DEBUG, ERROR, WARN).
 */
fun logger(data: Any, endpoint: String = "", details: String = "", level: String = "INFO") {
    val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    val threadName = Thread.currentThread().name

    println(
        """
        |-----------------------------------------------------
        | 📝 [${level.uppercase()}] | $timestamp | Thread: $threadName
        | 🌍 Endpoint: $endpoint
        | 🔧 Details: $details
        | 🔍 Data: $data
        |-----------------------------------------------------
        """.trimMargin()
    )
}
