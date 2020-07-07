package benchmark.dal

import com.mysql.cj.jdbc.MysqlDataSource
import benchmark.constants.DataSource
import java.sql.Connection

class MySQLConnection: DBConnection {
    override fun getConnection(): Connection {
        val dataSource = MysqlDataSource()
        dataSource.serverName = DataSource.SERVER_NAME
        dataSource.port = DataSource.PORT
        dataSource.user = DataSource.USER
        dataSource.password = DataSource.PASSWORD
        dataSource.databaseName = DataSource.DB_NAME
        return dataSource.connection
    }
}