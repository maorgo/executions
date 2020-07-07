package benchmark.services

import benchmark.constants.Constants
import benchmark.constants.DataSource
import benchmark.dal.MySQLConnection
import benchmark.objects.Schedule
import com.mysql.cj.jdbc.result.ResultSetImpl
import org.apache.pulsar.shade.org.apache.commons.lang.RandomStringUtils
import org.slf4j.LoggerFactory

class MasterService {

    companion object {
        private val logger = LoggerFactory.getLogger(MasterService::class.java)
        private val dbConnection = MySQLConnection().getConnection()
    }

    private fun cleanOldDataIfExists() {
        val tables = dbConnection.metaData
            .getTables(null, null, DataSource.SCHEDULES_TABLE, arrayOf("TABLE")) as ResultSetImpl

        if (tables.next()) {
            dbConnection.createStatement().execute("DROP TABLE " + DataSource.SCHEDULES_TABLE)
        }

        dbConnection.createStatement().execute(
            "CREATE TABLE " + DataSource.SCHEDULES_TABLE + "(" +
                "task_id varchar(255)," +
                "cron varchar(255)," +
                "last_submission DATE NULL," +
                "execute_in_parallel INT" +
                ")"
        )
    }

    private fun populateSchedules() {
        dbConnection.autoCommit = false
        val sqlInsert = "INSERT INTO " + DataSource.SCHEDULES_TABLE + " values(?, ?, null, ?)"
        val statement = dbConnection.prepareStatement(sqlInsert)
        val schedules = getSchedules()
        for (schedule in schedules) {
            statement.setString(1, schedule.taskId)
            statement.setString(2, schedule.cron)
            statement.setString(3, schedule.executeInParallel.toString())
            statement.addBatch()
        }
        statement.executeBatch()
        dbConnection.autoCommit = true
    }

    private fun getSchedules(): List<Schedule> {
        val schedules = mutableListOf<Schedule>()

        val parallelSchedules = createSchedules(1)
        val nonParallelSchedules = createSchedules(0)
        schedules.addAll(parallelSchedules)
        schedules.addAll(nonParallelSchedules)

        return schedules
    }

    private fun createSchedules(executeInParallel: Int): List<Schedule> {
        val schedules = mutableListOf<Schedule>()
        val schedulesCount = Constants.SCHEDULES_COUNT / 2
        val cronExpression = "* * * * * * *"

        repeat(schedulesCount) {
            val taskId = RandomStringUtils.randomAlphanumeric(10)
            schedules.add(Schedule(taskId = taskId, cron = cronExpression, executeInParallel = executeInParallel))
        }

        return schedules
    }

    fun runBenchmark() {
        logger.info("Cleaning old data if exists")
        cleanOldDataIfExists()

        logger.info("Populating new schedules")
        populateSchedules()

        logger.info("Finished")
    }
}