package bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 借用记录表
 */

@Data
@NoArgsConstructor
public class TbBorrowRecord {

    private Integer id;
    /**
     * 设备id
     */
    private Integer deviceId;

    /**
     * 借用人id
     */
    private Integer borrowUserId;

    /**
     * 借用日期
     */
    private Date borrowDate;

    /**
     * 借出保管员id
     */
    private Integer borrowClerkUserId;

    /**
     * 归还人id
     */
    private Integer returnUserId;

    /**
     * 归还日期
     */
    private Date returnDate;

    /**
     * 归还保管员id
     */
    private Integer returnClerkUserId;

    /**
     * 备注
     */
    private String remark;
}
