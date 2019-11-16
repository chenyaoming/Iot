package uitl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class JDBCTemplate<T> {

    private static Logger LOG = LoggerFactory.getLogger(JDBCTemplate.class);

    private Connection connection = null;

    public T doTask(){

        try {
            connection =JDBCUtils.getConnection();
            connection.setAutoCommit(false);

            T result = sqlTask(connection);
            connection.commit();

            return result;

        } catch (SQLException e) {
            LOG.error("执行事务sql错误：",e);
            try {
                connection.rollback();
            } catch (SQLException e1) {
                LOG.error("回滚事务出错："+e1);
            }
            throw new RuntimeException("执行sql错误");
        }finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    public abstract T sqlTask(Connection connection) throws SQLException;

}
