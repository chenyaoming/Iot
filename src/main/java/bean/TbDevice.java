package bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备表
 */
@Data
@NoArgsConstructor
public class TbDevice {

    private Integer id;

    private String name;

    /**
     * 型号
     */
    private String typeNum;

    /**
     * 编码
     */
    private String code;

    /**
     * 存放位置
     */
    private String savePosition;

    /**
     * 图片
     */
    private String image;

    /**
     * 功能
     */
    private String features;
}
