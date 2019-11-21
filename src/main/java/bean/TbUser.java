package bean;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ToString
@Data
@NoArgsConstructor
public class TbUser {

    private Integer id;

    /**
     *   1：借用人和归还人管理 ,2:保管员管理
     */
    private Integer type;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 创建时间
     */
    private Date createDate;


    public TbUser(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
