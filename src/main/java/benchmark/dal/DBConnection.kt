package benchmark.dal

import java.sql.Connection

interface DBConnection {
    fun getConnection(): Connection
}