package dao;

import bean.TbDevice;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import uitl.CommonDbUtil;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TbDeviceDao {

    public Integer insert(TbDevice device){
        String colums = "name,typeNum,code,savePosition,image,features";
        String sql = "INSERT INTO TB_DEVICE("+ colums +") VALUES(?,?,?)";

        Object[] params = {device.getName(),device.getTypeNum(),device.getCode(),
                device.getSavePosition(),device.getFeatures()};
        return CommonDbUtil.insertOneRetureId(sql,params);
    }

    public TbDevice queryById(Integer id){
        String sql = "SELECT * FROM TB_DEVICE WHERE id = ?";
        Object[] params ={id};
        return CommonDbUtil.queryReturnBean(sql,TbDevice.class,params);
    }

    public void insertBatch(List<TbDevice> deviceList){
        String colums = "name,typeNum,code,savePosition,image,features";
        String sql = "INSERT INTO TB_DEVICE("+ colums +") VALUES(?,?,?,?,?,?)";

        if(CollectionUtils.isEmpty(deviceList)){
            return ;
        }
        List<Object[]> paramsList = new ArrayList<>();
        for (TbDevice device :deviceList){
            Object[] params = {device.getName(),device.getTypeNum(),device.getCode(),
                    device.getSavePosition(),device.getFeatures()};

            paramsList.add(params);
        }

        Object[] paramArr = new Object[paramsList.size()];
        paramsList.toArray(paramArr);
        CommonDbUtil.batchUpdate(sql,(Object[][]) paramArr);
    }

    public void update(TbDevice device){

        Object[] params = {device.getName(),device.getTypeNum(),device.getCode(),
                device.getSavePosition(),device.getImage(),device.getFeatures(),device.getId()};

        String sql = "update TB_DEVICE set name =?,typeNum =? ,code =? ,savePosition =? ,image =? ,features =? " +
                " where id = ?";
        CommonDbUtil.update(sql,params);
    }

    public void update(Integer id ,String colum,Object value){

        Object[] params = {value,id};
        String sql = "update TB_DEVICE set "+colum+" = ? where id = ?";
        CommonDbUtil.update(sql,params);
    }

}
