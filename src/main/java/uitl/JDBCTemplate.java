package uitl;


import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class JDBCTemplate<T> {

    private Connection connection = null;

    public T doTask(){

        try {
            connection =JDBCUtils.getConnection();
            connection.setAutoCommit(false);

            T result = sqlTask(connection);
            connection.commit();

            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException("执行sql错误");
        }finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    public abstract T sqlTask(Connection connection) throws SQLException;

}
