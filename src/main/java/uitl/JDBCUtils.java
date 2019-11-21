package uitl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


public final class JDBCUtils {
    private static Logger LOG = LoggerFactory.getLogger(JDBCUtils.class);

    private static final String DRIVER;
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    private JDBCUtils(){}

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("jdbc");

        DRIVER = bundle.getString("driver");
        URL = bundle.getString("url");
        USER = bundle.getString("user");
        PASSWORD = bundle.getString("password");

        /**
         * 驱动注册
         */
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {

            LOG.error("加载驱动错误：",e);
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 获取 Connetion
     * @return
     * @throws SQLException
     */
    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            try {
                return DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e1) {
                LOG.error("数据库获取连接错误：",e1);
                throw new RuntimeException("数据库获取连接错误");
            }
        }
    }

    /**
     * 释放资源
     * @param conn
     * @param st
     * @param rs
     */
    public static void colseResource(Connection conn,Statement st,ResultSet rs) {
        closeResultSet(rs);
        closeStatement(st);
        closeConnection(conn);
    }

    /**
     * 释放连接 Connection
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if(conn !=null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOG.error("关闭数据库连接错误：",e);
            }
        }
        //等待垃圾回收
        conn = null;
    }

    /**
     * 释放语句执行者 Statement
     * @param st
     */
    public static void closeStatement(Statement st) {
        if(st !=null) {
            try {
                st.close();
            } catch (SQLException e) {
                LOG.error("关闭数据库Statement错误：",e);
            }
        }
        //等待垃圾回收
        st = null;
    }

    /**
     * 释放结果集 ResultSet
     * @param rs
     */
    public static void closeResultSet(ResultSet rs) {
        if(rs !=null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOG.error("释放结果集错误：",e);
            }
        }
        //等待垃圾回收
        rs = null;
    }


    public static void initDatabaseTabel() {
        Connection conn = getConnection();
        Statement statement = null;
        try {
            statement = conn.createStatement();
            conn.setAutoCommit(false);

            statement.execute("create table IF not EXISTS TB_BORROW_RECORD\n" +
                    "(\n" +
                    "   id                   int IDENTITY not null,\n" +
                    "   status               varchar(128),\n" +
                    "   deviceId             int,\n" +
                    "   deviceName           varchar(255),\n" +
                    "   deviceType           varchar(255),\n" +
                    "   deviceCode           varchar(255),\n" +
                    "   devicePosition       varchar(255),\n" +
                    "   deviceImage          varchar(255),\n" +
                    "   features             varchar(255),\n" +
                    "   borrowNum            int not null DEFAULT 0,\n" +
                    "   borrowUserId         int,\n" +
                    "   borrowUserName       varchar(128),\n" +
                    "   borrowDate           datetime,\n" +
                    "   borrowClerkUserId    int,\n" +
                    "   borrowClerkUserName  varchar(128),\n" +
                    "   returnUserId         int,\n" +
                    "   returnUserName       varchar(128),\n" +
                    "   returnDate           datetime,\n" +
                    "   returnClerkUserId    int,\n" +
                    "   returnClerkUserName  varchar(128),\n" +
                    "   remark               varchar(512),\n" +
                    "   primary key (id)\n" +
                    ")");

            statement.execute("create table IF not EXISTS TB_FINGER\n" +
                    "(\n" +
                    "   id                   int IDENTITY not null,\n" +
                    "   template             BINARY(2048),\n" +
                    "   userId               int not null,\n" +
                    "   primary key (id)\n" +
                    ")");

            statement.execute("create table IF not EXISTS TB_USER\n" +
                    "(\n" +
                    "   id                   int IDENTITY not null,\n" +
                    "   type                 int not null,\n" +
                    "   name                 varchar(255),\n" +
                    "   gender               varchar(8),\n" +
                    "   age                  int,\n" +
                    "   phone                varchar(255),\n" +
                    "   createDate           datetime,\n" +
                    "   primary key (id)\n" +
                    ")");

            statement.execute("create table IF not EXISTS TB_DEVICE\n" +
                    "(\n" +
                    "   id                   int IDENTITY not null,\n" +
                    "   name                 varchar(512),\n" +
                    "   typeNum              varchar(255),\n" +
                    "   code                 varchar(255),\n" +
                    "   count                int DEFAULT 0,\n" +
                    "   savePosition         varchar(512),\n" +
                    "   image                varchar(255),\n" +
                    "   features             varchar(512),\n" +
                    "   primary key (id)\n" +
                    ")");

            conn.commit();

        } catch (SQLException e) {
            LOG.error("初始化表结构错误：",e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LOG.error("初始化表结构事务回滚错误：",e);
            }
            throw new RuntimeException("初始化表结构错误");
        } finally {
            closeStatement(statement);
            closeConnection(conn);
        }
    }
}