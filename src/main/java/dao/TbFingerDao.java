package dao;

import bean.TbFinger;
import bean.TbUser;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uitl.CommonDbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TbFingerDao {

    private static Logger LOG = LoggerFactory.getLogger(TbFingerDao.class);


    public List<TbFinger> findAll(){
        String sql = "SELECT * FROM TB_FINGER";
        return CommonDbUtil.queryReturnBeanList(sql,TbFinger.class);
    }

    public Integer insert(TbFinger finger){
        String sql = "INSERT INTO TB_FINGER(template,userId) VALUES(?,?)";
        Object[] params = {finger.getTemplate(),finger.getUserId()};

        LOG.info("执行sql：{}, 参数：{}",sql,finger);
        return CommonDbUtil.insertOneRetureId(sql,params);
    }

    public Integer insertFinger(Connection connection, TbFinger finger) throws SQLException {
        QueryRunner runner = new QueryRunner();

        String sql = "INSERT INTO TB_FINGER(template,userId) VALUES(?,?)";
        Object[] fingerParams = {finger.getTemplate(),finger.getUserId()};
        LOG.info("执行sql：{}, 参数：{}",sql,finger);
        return (Integer) runner.insert(connection,sql,new ScalarHandler(1),fingerParams);
    }

    public TbFinger queryById(Integer id){
        String sql = "SELECT * FROM TB_FINGER WHERE id = ?";
        Object[] params ={id};
        return CommonDbUtil.queryReturnBean(sql,TbFinger.class,params);
    }


}
