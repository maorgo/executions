package benchmark.objects

import java.time.LocalDateTime

data class Schedule(
    val taskId: String,
    val cron: String,
    val lastSubmission: LocalDateTime? = null,
    val executeInParallel: Int
)
