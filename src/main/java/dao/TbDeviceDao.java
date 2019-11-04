package dao;

import bean.TbDevice;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import uitl.CommonDbUtil;

import java.util.ArrayList;
import java.util.List;

public class TbDeviceDao {

    public static final String COLUMS = "name,typeNum,code,savePosition,image,features";

    public List<TbDevice> findByPage(int page,int size){
        Object[] params = {(page -1)*size ,size};
        String sql = "select * from TB_DEVICE  limit ?,?";

       return CommonDbUtil.queryReturnBeanList(sql,TbDevice.class,params);
    }

    public List<TbDevice> findByConditionPage(TbDevice device,int page,int size){

        StringBuilder sql = new StringBuilder("select * from TB_DEVICE where 1=1 ");

        List<Object> params = new ArrayList<>();
        getSearchCondition(device, sql, params);

        sql.append(" limit ?,? ");
        params.add((page -1)*size);
        params.add(size);

        Object[] paramArr = new Object[params.size()];

        params.toArray(paramArr);

        return CommonDbUtil.queryReturnBeanList(sql.toString(),TbDevice.class,paramArr);
    }

    public long countAll(){
        String sql = "select count(*) FROM TB_DEVICE";
        return CommonDbUtil.queryReturnSimpleVal(sql,1);
    }

    public long countAllByCondition(TbDevice device){
        StringBuilder sql = new StringBuilder("select count(*) FROM TB_DEVICE where 1=1 ");

        List<Object> params = new ArrayList<>();
        getSearchCondition(device, sql, params);
        Object[] paramArr = new Object[params.size()];
        params.toArray(paramArr);
        return CommonDbUtil.queryReturnSimpleVal(sql.toString(),1,paramArr);
    }

    public Integer insert(TbDevice device){

        String sql = "INSERT INTO TB_DEVICE("+ COLUMS +") VALUES(?,?,?,?,?,?)";

        Object[] params = {device.getName(),device.getTypeNum(),device.getCode(),
                device.getSavePosition(),device.getImage(),device.getFeatures()};
        return CommonDbUtil.insertOneRetureId(sql,params);
    }

    public TbDevice queryById(Integer id){
        String sql = "SELECT * FROM TB_DEVICE WHERE id = ?";
        Object[] params ={id};
        return CommonDbUtil.queryReturnBean(sql,TbDevice.class,params);
    }

    public void insertBatch(List<TbDevice> deviceList){
        String sql = "INSERT INTO TB_DEVICE("+ COLUMS +") VALUES(?,?,?,?,?,?)";

        if(CollectionUtils.isEmpty(deviceList)){
            return ;
        }
        Object[][] dataParam = new Object[deviceList.size()][6];

        //List<Object[]> paramsList = new ArrayList<>();

        for (int i = 0; i< deviceList.size(); i++){
            TbDevice device = deviceList.get(i);
            dataParam[i][0]=device.getName();
            dataParam[i][1]=device.getTypeNum();
            dataParam[i][2]=device.getCode();
            dataParam[i][3]=device.getSavePosition();
            dataParam[i][4]=device.getImage();
            dataParam[i][5]=device.getFeatures();
        }

       /* for (TbDevice device :deviceList){



            *//*Object[] params = {device.getName(),device.getTypeNum(),device.getCode(),
                    device.getSavePosition(),device.getImage(),device.getFeatures()};

            paramsList.add(params);*//*
        }*/

        /*Object[] paramArr = new Object[paramsList.size()];
        paramsList.toArray(paramArr);*/
        CommonDbUtil.batchUpdate(sql,dataParam);
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

    private void getSearchCondition(TbDevice device, StringBuilder sql, List<Object> params) {
        if (null != device) {
            if (StringUtils.isNotBlank(device.getName())) {
                sql.append(" and name like ? ");
                params.add("%" + device.getName().trim() + "%");
            }
            if (StringUtils.isNotBlank(device.getTypeNum())) {
                sql.append(" and typeNum like ? ");
                params.add("%" + device.getTypeNum().trim() + "%");
            }
            if (StringUtils.isNotBlank(device.getCode())) {
                sql.append(" and code like ? ");
                params.add("%" + device.getCode().trim() + "%");
            }
        }
    }

}
