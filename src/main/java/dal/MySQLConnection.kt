package dal

import com.mysql.cj.jdbc.MysqlDataSource
import java.sql.Connection

class MySQLConnection {
    companion object {
        fun getConnection(): Connection {
            val dataSource = MysqlDataSource()
            dataSource.serverName = "localhost"
            dataSource.port = 3306
            dataSource.user = "root"
            dataSource.password = "password"
            dataSource.databaseName = "sys"
            return dataSource.connection
        }
    }
}