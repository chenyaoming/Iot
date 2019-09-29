package bean;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class TbUser {

    private Integer id;

    private String name;

    private Date createDate;

    /**
     * 指纹id,这个唯一
     */
    private int fingerId;
}
