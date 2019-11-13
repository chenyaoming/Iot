package dao;

import bean.TbBorrowRecord;
import org.apache.commons.lang3.StringUtils;
import uitl.CommonDbUtil;

import java.util.ArrayList;
import java.util.List;


public class TbBorrowRecordDao {

    public static final String[] columArr = {"deviceId","deviceName","deviceType","deviceCode","devicePosition","deviceImage","features","borrowNum",
     "borrowUserId","borrowUserName","borrowDate", "borrowClerkUserId","borrowClerkUserName","status"};


    public static final String[] returnArr = {"returnUserId","returnUserName","returnDate","returnClerkUserId","returnClerkUserName"};

    /*public List<TbBorrowRecord> findByPage(int page, int size){
        Object[] params = {(page -1)*size ,size};
        String sql = "select * from TB_BORROW_RECORD order by id desc limit ?,? ";

        return CommonDbUtil.queryReturnBeanList(sql,TbBorrowRecord.class,params);
    }*/

    public void updateRecord(TbBorrowRecord r){
        String sql = "UPDATE TB_BORROW_RECORD set deviceName = ?,deviceType = ?,deviceCode = ?,devicePosition = ?," +
                " deviceImage =? ,features = ?,borrowNum = ?,remark = ? where id = ?";
        Object[] params = {r.getDeviceName(),r.getDeviceType(),r.getDeviceCode(),r.getDevicePosition(),
                r.getDeviceImage(),r.getFeatures(),r.getBorrowNum(),r.getRemark(),r.getId()};

        CommonDbUtil.update(sql,params);
    }

    public void updateReturnData(TbBorrowRecord r){

        String sql = "UPDATE TB_BORROW_RECORD set returnUserId = ? ,returnUserName = ? " +
                " ,returnDate = ? ,returnClerkUserId =?  ,returnClerkUserName = ? ,status = ? where id = ?";
        Object[] params = {r.getReturnUserId(),r.getReturnUserName(),r.getReturnDate(),
                r.getReturnClerkUserId(),r.getReturnClerkUserName(),r.getStatus(),r.getId()};

        CommonDbUtil.update(sql,params);
    }

    public void updateRemark(String remark,Integer id){
        String sql = "UPDATE TB_BORROW_RECORD set remark = ? where id = ?";
        Object[] params = {remark,id};
        CommonDbUtil.update(sql,params);
    }


    public List<TbBorrowRecord> findByConditionPage(TbBorrowRecord record,int page,int size){

        StringBuilder sql = new StringBuilder("select * from TB_BORROW_RECORD  where 1=1 ");

        List<Object> params = new ArrayList<>();
        getSearchCondition(record, sql, params);

        sql.append(" order by status asc, id desc ");
        sql.append(" limit ?,? ");
        params.add((page -1)*size);
        params.add(size);

        Object[] paramArr = new Object[params.size()];

        params.toArray(paramArr);

        return CommonDbUtil.queryReturnBeanList(sql.toString(), TbBorrowRecord.class,paramArr);
    }

    public long countAll(){
        String sql = "select count(*) FROM TB_BORROW_RECORD";
        return CommonDbUtil.queryReturnSimpleVal(sql,1);
    }

    public long countAllByCondition(TbBorrowRecord record){
        StringBuilder sql = new StringBuilder("select count(*) FROM TB_BORROW_RECORD where 1=1 ");

        List<Object> params = new ArrayList<>();
        getSearchCondition(record, sql, params);
        Object[] paramArr = new Object[params.size()];
        params.toArray(paramArr);
        return CommonDbUtil.queryReturnSimpleVal(sql.toString(),1,paramArr);
    }

    public Integer insert(TbBorrowRecord r){
        String colums = StringUtils.join(columArr, ",");
        String questionMarks = CommonDbUtil.spliceSqlQuestionMark(columArr.length);

        String sql = "INSERT INTO TB_BORROW_RECORD("+ colums +") VALUES("+ questionMarks +")";
        Object[] params = {r.getDeviceId(),r.getDeviceName(),r.getDeviceType(),r.getDeviceCode(),
        r.getDevicePosition(),r.getDeviceImage(),r.getFeatures(),r.getBorrowNum(),r.getBorrowUserId(),
        r.getBorrowUserName(),r.getBorrowDate(),r.getBorrowClerkUserId(),r.getBorrowClerkUserName(),r.getStatus()};

        //CommonDbUtil.getBeanValues(record,columArr);
        return CommonDbUtil.insertOneRetureId(sql,params);
    }

    public TbBorrowRecord queryById(Integer id){
        String sql = "SELECT * FROM TB_BORROW_RECORD WHERE id = ?";
        Object[] params ={id};
        return CommonDbUtil.queryReturnBean(sql,TbBorrowRecord.class,params);
    }

    private void getSearchCondition(TbBorrowRecord record, StringBuilder sql, List<Object> params) {
        if (null != record) {
            if (StringUtils.isNotBlank(record.getDeviceName())) {
                sql.append(" and deviceName like ? ");
                params.add("%" + record.getDeviceName().trim() + "%");
            }
            if (StringUtils.isNotBlank(record.getDeviceCode())) {
                sql.append(" and deviceCode like ? ");
                params.add("%" + record.getDeviceCode().trim() + "%");
            }
            if (StringUtils.isNotBlank(record.getBorrowUserName())) {
                sql.append(" and borrowUserName like ? ");
                params.add("%" + record.getBorrowUserName().trim() + "%");
            }
        }
    }

}
