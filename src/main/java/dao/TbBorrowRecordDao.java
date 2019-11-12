package dao;

import bean.TbBorrowRecord;
import bean.TbDevice;
import org.apache.commons.lang3.StringUtils;
import uitl.CommonDbUtil;

import java.util.ArrayList;
import java.util.List;


public class TbBorrowRecordDao {

    public static final String[] columArr = {"deviceId","deviceName","deviceType","deviceCode","devicePosition","deviceImage","features","borrowNum",
     "borrowUserId","borrowUserName","borrowDate", "borrowClerkUserId","borrowClerkUserName"};


    public List<TbBorrowRecord> findByPage(int page, int size){
        Object[] params = {(page -1)*size ,size};
        String sql = "select * from TB_BORROW_RECORD  limit ?,?";

        return CommonDbUtil.queryReturnBeanList(sql,TbBorrowRecord.class,params);
    }

    public List<TbBorrowRecord> findByConditionPage(TbBorrowRecord record,int page,int size){

        StringBuilder sql = new StringBuilder("select * from TB_BORROW_RECORD where 1=1 ");

        List<Object> params = new ArrayList<>();
        getSearchCondition(record, sql, params);

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
        r.getBorrowUserName(),r.getBorrowDate(),r.getBorrowClerkUserId(),r.getBorrowClerkUserName()};

        //CommonDbUtil.getBeanValues(record,columArr);
        return CommonDbUtil.insertOneRetureId(sql,params);
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
