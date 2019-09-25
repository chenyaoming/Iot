package uitl;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CommonDbUtil {

    /**
     * ArrayHandler：将查询的结果的第一行放到一个数组中
     * @return
     * @throws SQLException
     */
    public Object[] queryReturnArray(String sql,Object... params)  {

        Connection connection = null;
        try {

            connection =JDBCUtils.getConnection();
            QueryRunner runner=new QueryRunner();
            return runner.query(connection,sql,new ArrayHandler(),params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("执行sql错误："+sql+"参数"+params);
        }finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    /**
     * ArrayListHandler：将查询的结果的每一行放到一个数组中，然后再将数组放到集合中；
     * @return
     * @throws SQLException
     */
    public List<Object[]> queryReturnArrayList(String sql,Object... params)  {

        Connection connection = null;
        try {
            connection =JDBCUtils.getConnection();
            QueryRunner runner=new QueryRunner();
            List<Object[]> list=runner.query(connection,sql,new ArrayListHandler());
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("执行sql错误："+sql+"参数"+params);
        }finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    /**
     * BeanHandler：将查询的结果的第一行封装到一份javabean对象中；
     * @return
     * @throws SQLException
     */
    public <T> T queryReturnBean(String sql, Class<T> beanClass, Object... params)  {

        Connection connection = null;
        try {
            connection =JDBCUtils.getConnection();
            QueryRunner runner=new QueryRunner();
            return runner.query(connection,sql, new BeanHandler<>(beanClass));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("执行sql错误："+sql+"参数"+params);
        }finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    /**
     * BeanListHandler：将查询的结果的每一行封装到一个javabean对象中，然后再将这些对象存入list中；
     * @return
     * @throws SQLException
     */
    public <T> List<T> queryReturnBeanList(String sql, Class<T> beanClass, Object... params)  {

        Connection connection = null;
        try {
            connection =JDBCUtils.getConnection();
            QueryRunner runner=new QueryRunner();
            return runner.query(connection,sql, new BeanListHandler<>(beanClass));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("执行sql错误："+sql+"参数"+params);
        }finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    /**
     * MapHandler：将查询的结果的第一行存入到一个map中，键为列名，值为各列值；
     * @return
     * @throws SQLException
     */
    public Map<String,Object> queryReturnMap(String sql, Object... params)  {

        Connection connection = null;
        try {

            connection =JDBCUtils.getConnection();
            QueryRunner runner=new QueryRunner();
            return runner.query(connection,sql,new MapHandler(),params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("执行sql错误："+sql+"参数"+params);
        }finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    /**
     * MapListHandler：将查询的结果的每一行存入到一个map中，键为列名，值为各列值；然后再将map存入list中；
     * @return
     * @throws SQLException
     */
    public List<Map<String,Object>> queryReturnMapList(String sql, Object... params)  {

        Connection connection = null;
        try {
            connection =JDBCUtils.getConnection();
            QueryRunner runner=new QueryRunner();
            return runner.query(connection,sql,new MapListHandler(),params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("执行sql错误："+sql+"参数"+params);
        }finally {
            JDBCUtils.closeConnection(connection);
        }
    }


    /**
     * ScalarHandler：将查询的结果的第一行的某一列放到一个对象中；精确定位到某个值；
     * @return
     * @throws SQLException
     */
    public <T> T queryReturnSimpleVal(String sql,int col,Object... params)  {

        Connection connection = null;
        try {

            connection =JDBCUtils.getConnection();
            QueryRunner runner=new QueryRunner();
            return (T)runner.query(connection,sql,new ScalarHandler(col),params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("执行sql错误："+sql+"参数"+params);
        }finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    public void insertOne(String sql,Object... params)  {
        Object key = insertOneRetureId(sql,params);
    }

    /**
     * 插入一条数据返回Id
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T> T insertOneRetureId(String sql,Object... params)  {

        Connection connection = null;
        try {

            connection =JDBCUtils.getConnection();
            QueryRunner runner=new QueryRunner();

            return (T)runner.insert(connection,sql,new ScalarHandler(1),params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("执行sql错误："+sql+"参数"+params);
        }finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    public void batchUpdate(String sql,Object[]... params)  {
        Connection connection = null;
        try {
            connection =JDBCUtils.getConnection();
            connection.setAutoCommit(false);
            QueryRunner runner=new QueryRunner();
            runner.batch(connection,sql,params);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException("执行sql错误："+sql+"参数"+params);
        }finally {
            JDBCUtils.closeConnection(connection);
        }
    }


}
