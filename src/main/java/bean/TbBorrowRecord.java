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
     * 状态
     */
    private String status;

    /**
     * 设备id
     */
    private Integer deviceId;

    /**
     * 借用人id
     */
    private Integer borrowUserId;

    /**
     * 借用人名称
     */
    private Integer borrowUserName;

    /**
     * 借用日期
     */
    private Date borrowDate;

    /**
     * 借出保管员id
     */
    private Integer borrowClerkUserId;

    /**
     * 借出保管员名称
     */
    private Integer borrowClerkUserName;

    /**
     * 归还人id
     */
    private Integer returnUserId;

    /**
     * 归还人名称
     */
    private Integer returnUserName;

    /**
     * 归还日期
     */
    private Date returnDate;

    /**
     * 归还保管员id
     */
    private Integer returnClerkUserId;

    /**
     * 归还保管员名称
     */
    private Integer returnClerkUserName;

    /**
     * 备注
     */
    private String remark;
}
