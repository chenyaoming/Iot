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
     * 库存数量
     */
    private Integer count;

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

    public TbDevice(String name,String typeNum,String code){
        this.name = name;
        this.typeNum=typeNum;
        this.code = code;
    }

    public TbDevice(String name, String typeNum, String code, String savePosition, String image, String features) {
        this.name = name;
        this.typeNum = typeNum;
        this.code = code;
        this.savePosition = savePosition;
        this.image = image;
        this.features = features;
    }
}
