package dao;

import bean.TbUser;
import frame.MainFram;
import frame.user.UserPanel;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uitl.CommonDbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TbUserDao {

    private static Logger LOG = LoggerFactory.getLogger(TbUserDao.class);


    public static final String[] COLUM_ARR = {"name","gender","age","phone","createDate","type"};

    /*public long countAll(){
        String sql = "select count(*) FROM TB_USER";
        return CommonDbUtil.queryReturnSimpleVal(sql,1);
    }*/

    /*public List<TbUser> findByPage(int page, int size){
        Object[] params = {(page -1)*size ,size};
        String sql = "select * from TB_USER  limit ?,?";

        return CommonDbUtil.queryReturnBeanList(sql,TbUser.class,params);
    }*/

    public long countAllByCondition(TbUser user){
        StringBuilder sql = new StringBuilder("select count(*) FROM TB_USER where 1=1 ");

        List<Object> params = new ArrayList<>();
        //增加用户类型的条件
        addTypeCondition(sql, params);

        getSearchCondition(user, sql, params);
        Object[] paramArr = new Object[params.size()];
        params.toArray(paramArr);
        return CommonDbUtil.queryReturnSimpleVal(sql.toString(),1,paramArr);
    }

    private void addTypeCondition(StringBuilder sql, List<Object> params) {
        if(MainFram.getCurrentPanel() instanceof UserPanel) {
            Integer userType =((UserPanel) MainFram.getCurrentPanel()).getUserType();
            sql.append(" and type = ? ");
            params.add(userType);
        }
    }

    public List<TbUser> findByConditionPage(TbUser user,int page,int size){

        StringBuilder sql = new StringBuilder("select * from TB_USER where 1=1 ");

        List<Object> params = new ArrayList<>();
        //增加用户类型的条件
        addTypeCondition(sql, params);

        getSearchCondition(user, sql, params);

        sql.append(" limit ?,? ");
        params.add((page -1)*size);
        params.add(size);

        Object[] paramArr = new Object[params.size()];

        params.toArray(paramArr);

        return CommonDbUtil.queryReturnBeanList(sql.toString(),TbUser.class,paramArr);
    }



    /*public Integer insert(TbUser user){

        String colums = StringUtils.join(COLUM_ARR, ",");
        String questionMarks = CommonDbUtil.spliceSqlQuestionMark(COLUM_ARR.length);

        String sql = "INSERT INTO TB_USER("+ colums +") VALUES("+questionMarks+")";

        Object[] params = {user.getName(),user.getGender(),user.getAge(),
                user.getPhone(),user.getCreateDate()};

        LOG.info("执行sql：{}, 参数：{}",sql,user);
        return CommonDbUtil.insertOneRetureId(sql,params);
    }*/


    public Integer insertUser(Connection connection,TbUser user) throws SQLException {

        QueryRunner runner = new QueryRunner();

        String colums = StringUtils.join(TbUserDao.COLUM_ARR, ",");
        String questionMarks = CommonDbUtil.spliceSqlQuestionMark(TbUserDao.COLUM_ARR.length);

        String sql = "INSERT INTO TB_USER("+ colums +") VALUES("+questionMarks+")";

        Object[] params = {user.getName(),user.getGender(),user.getAge(),
                user.getPhone(),user.getCreateDate(),user.getType()};

        LOG.info("执行sql：{}, 参数：{}",sql,user);

        return (Integer) runner.insert(connection,sql,new ScalarHandler(1),params);
    }


    public void update(TbUser user){

        Object[] params = {user.getName(),user.getGender(),user.getAge(),user.getPhone(),user.getId()};

        String sql = "update TB_USER set name =?,gender =? ,age =?,phone =? where id = ?";

        LOG.info("执行sql：{}, 参数：{}",sql,user);

        CommonDbUtil.update(sql,params);
    }

    public TbUser queryById(Integer id){
        String sql = "SELECT * FROM TB_USER WHERE id = ?";
        Object[] params ={id};
        return CommonDbUtil.queryReturnBean(sql,TbUser.class,params);
    }

    /*public TbUser queryByFingerId(int fingerId){
        String sql = "SELECT * FROM TB_USER WHERE fingerId = ?";
        Object[] params ={fingerId};
        return CommonDbUtil.queryReturnBean(sql,TbUser.class,params);
    }*/

    private void getSearchCondition(TbUser user, StringBuilder sql, List<Object> params) {
        if (null != user) {
            if (StringUtils.isNotBlank(user.getName())) {
                sql.append(" and name like ? ");
                params.add("%" + user.getName().trim() + "%");
            }
            if (StringUtils.isNotBlank(user.getPhone())) {
                sql.append(" and phone like ? ");
                params.add("%" + user.getPhone().trim() + "%");
            }
        }
    }

}
