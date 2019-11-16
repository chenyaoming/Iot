package bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 借用记录表
 */

@ToString
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

    private String deviceName;

    private String deviceType;

    private String deviceCode;

    private String devicePosition;

    private String deviceImage;

    /**
     * 功能
     */
    private String features;

    /**
     * 借出数量
     */
    private Integer borrowNum;

    /**
     * 借用人id
     */
    private Integer borrowUserId;

    /**
     * 借用人名称
     */
    private String borrowUserName;

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
    private String borrowClerkUserName;

    /**
     * 归还人id
     */
    private Integer returnUserId;

    /**
     * 归还人名称
     */
    private String returnUserName;

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
    private String returnClerkUserName;

    /**
     * 备注
     */
    private String remark;


}
