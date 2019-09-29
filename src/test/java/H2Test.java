import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * @Description: 内嵌数据库H2的使用
 * @author： zxt
 * @time: 2019年6月4日 下午3:30:13
 */
public class H2Test {

    /**
     * 以嵌入式(本地)连接方式连接H2数据库
     */
    private static final String JDBC_URL = "jdbc:h2:D:/Java/H2Test/user";

    /**
     * 使用TCP/IP的服务器模式(远程连接)方式连接H2数据库(推荐)
     */
    // private static final String JDBC_URL = "jdbc:h2:tcp://10.35.14.122/C:/H2/user";

    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER_CLASS = "org.h2.Driver";

    public static void main(String[] args) throws Exception {

        System.out.println(new Date().toString());

        String[] arr = {"2","3"};


        String str4 = StringUtils.join(arr, ",");
        System.out.println(arr.toString());

        // TODO Auto-generated method stub
        Class.forName(DRIVER_CLASS);
        Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        Connection conn2 = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        Statement statement = conn.createStatement();
        Statement statement2 = conn.createStatement();
        /*statement.execute("DROP TABLE IF EXISTS USER_INF");
        statement.execute("CREATE TABLE USER_INF(id INTEGER PRIMARY KEY, name VARCHAR(100), sex VARCHAR(2))");

        statement.executeUpdate("INSERT INTO USER_INF VALUES(1, 'tom', '男') ");
        statement.executeUpdate("INSERT INTO USER_INF VALUES(2, 'jack', '女') ");
        statement.executeUpdate("INSERT INTO USER_INF VALUES(3, 'marry', '男') ");
        statement.executeUpdate("INSERT INTO USER_INF VALUES(4, 'lucy', '男') ");*/
        statement.execute("CREATE TABLE  IF not EXISTS STUDENT (id INTEGER IDENTITY PRIMARY KEY, name VARCHAR(100)) ");

        conn.setAutoCommit(false);
        try {
            statement.execute("INSERT INTO STUDENT(name) VALUES( 'chen88')");
            if(1==1){
                throw new RuntimeException("");
            }

            conn.commit();

        }catch (Exception e){
            conn.rollback();
        }finally {

        }

        ResultSet resultSet22 = statement.executeQuery("select * from STUDENT");
        while (resultSet22.next()) {
            System.out.println(
                    resultSet22.getInt("id") + ", " + resultSet22.getString("name") );
        }


        /*statement.executeUpdate("INSERT INTO USER_INF VALUES(6, 'tom', '男') ");
        statement.executeUpdate("INSERT INTO USER_INF VALUES(7, 'jack', '女') ");
        statement.executeUpdate("INSERT INTO USER_INF VALUES(8, 'marry', '男') ");
        statement.executeUpdate("INSERT INTO USER_INF VALUES(9, 'lucy', '男') ");*/


        /*statement.executeUpdate("INSERT INTO USER_INF VALUES(131, 'tom', '男') ");
        statement2.executeUpdate("INSERT INTO USER_INF VALUES(231, 'jack', '女') ");*/

        ResultSet resultSet = statement.executeQuery("select * from USER_INF");

        //insert(conn);

        while (resultSet.next()) {
            System.out.println(
                    resultSet.getInt("id") + ", " + resultSet.getString("name") + ", " + resultSet.getString("sex"));
        }


        statement.close();
        conn.close();
    }

    public static void insert(Connection conn){
        try {
            //获取一个用来执行SQL语句的对象   QueryRunner
            QueryRunner qr = new QueryRunner();

            String sql = "INSERT INTO USER_INF VALUES(151, 'tom', '男') ";
            Object[] params = {};
            // JDBCUtils这个工具是上篇所提到的工具类
            int line = qr.update(conn,sql,params);// 用来完成表数据的增加、删除、更新操作

            Object[] arr = new Object[]{1,'2'};

            //结果集处理
            System.out.println("line = " + line);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}