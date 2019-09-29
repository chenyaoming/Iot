package dao;

import bean.TbBorrowRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import uitl.CommonDbUtil;


@Slf4j
public class TbBorrowRecordDao {

    public static final String[] columArr = {"deviceId","status",
     "borrowUserId","borrowUserName","borrowDate", "borrowClerkUserId","borrowClerkUserName",
    "returnUserId","returnUserName","returnDate","returnClerkUserId","returnClerkUserName","remark"};

    public Integer insert(TbBorrowRecord record){
        String colums = StringUtils.join(columArr, ",");
        String questionMarks = CommonDbUtil.spliceSqlQuestionMark(columArr.length);

        String sql = "INSERT INTO TB_BORROW_RECORD("+ colums +") VALUES("+ questionMarks +")";
        Object[] params = CommonDbUtil.getBeanValues(record,columArr);
        return CommonDbUtil.insertOneRetureId(sql,params);
    }

}
