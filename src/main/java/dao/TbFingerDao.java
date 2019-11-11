package dao;

import bean.TbFinger;
import uitl.CommonDbUtil;

import java.util.List;

public class TbFingerDao {

    public List<TbFinger> findAll(){
        String sql = "SELECT * FROM TB_FINGER";
        return CommonDbUtil.queryReturnBeanList(sql,TbFinger.class);
    }

    public Integer insert(TbFinger finger){
        String sql = "INSERT INTO TB_FINGER(template) VALUES(?)";
        Object[] params = {finger.getTemplate()};
        return CommonDbUtil.insertOneRetureId(sql,params);
    }

}
