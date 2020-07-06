import com.mysql.cj.jdbc.result.ResultSetImpl
import java.sql.Connection
import java.sql.DriverManager.getConnection

class Master {
    private fun cleanOldDataIfExists(connection: Connection) {
        val tables = connection.metaData
            .getTables(null, null, Defs.SCHEDULES_TABLE, arrayOf("TABLE")) as ResultSetImpl

        if (tables.next()) {
            connection.createStatement().execute("DROP TABLE " + Defs.SCHEDULES_TABLE)
        }

        connection.createStatement().execute(
            "CREATE TABLE " + Defs.SCHEDULES_TABLE + "(" +
                "task_id varchar(255)," +
                "cron varchar(255)," +
                "last_submission DATE," +
                "execute_in_parallel BIT" +
                ")"
        )
    }

    private fun populateSchedules(connection: Connection) {
        connection.autoCommit = false
        val sqlInsert = "INSERT INTO " + Defs.SCHEDULES_TABLE + " values(????)"
        val statement = connection.prepareStatement(sqlInsert)
        val schedulesToCommit = getSchedules()
        connection.commit()
        connection.autoCommit = true;
    }

    private fun getSchedules(): List<*> {
        TODO("Not yet implemented")
    }

    fun runBenchmark() {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val connection = MySQLConnection.getConnection()
            Master().cleanOldDataIfExists(connection)
        }
    }
}