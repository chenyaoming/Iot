package dao;

import bean.TbUser;
import lombok.extern.slf4j.Slf4j;
import uitl.CommonDbUtil;

@Slf4j
public class TbUserDao {


    public Integer insert(TbUser user){
        String colums = "name,createDate,fingerId";
        String sql = "INSERT INTO TB_USER("+ colums +") VALUES(?,?,?)";

        Object[] params = {user.getName(),user.getCreateDate(),user.getFingerId()};
        return CommonDbUtil.insertOneRetureId(sql,params);
    }

    public TbUser queryById(Integer id){
        String sql = "SELECT * FROM TB_USER WHERE id = ?";
        Object[] params ={id};
        return CommonDbUtil.queryReturnBean(sql,TbUser.class,params);
    }

    public TbUser queryByFingerId(int fingerId){
        String sql = "SELECT * FROM TB_USER WHERE fingerId = ?";
        Object[] params ={fingerId};
        return CommonDbUtil.queryReturnBean(sql,TbUser.class,params);
    }

}
