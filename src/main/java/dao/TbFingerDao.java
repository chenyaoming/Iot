package dao;

import bean.TbFinger;
import bean.TbUser;
import uitl.CommonDbUtil;

import java.util.List;

public class TbFingerDao {

    public List<TbFinger> findAll(){
        String sql = "SELECT * FROM TB_FINGER";
        return CommonDbUtil.queryReturnBeanList(sql,TbFinger.class);
    }

    public Integer insert(TbFinger finger){
        String sql = "INSERT INTO TB_FINGER(template,userId) VALUES(?,?)";
        Object[] params = {finger.getTemplate(),finger.getUserId()};
        return CommonDbUtil.insertOneRetureId(sql,params);
    }

    public TbFinger queryById(Integer id){
        String sql = "SELECT * FROM TB_FINGER WHERE id = ?";
        Object[] params ={id};
        return CommonDbUtil.queryReturnBean(sql,TbFinger.class,params);
    }


}
