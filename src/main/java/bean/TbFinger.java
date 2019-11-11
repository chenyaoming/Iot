package bean;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TbFinger {


    private Integer id;

    /**
     * 指纹模板字节数组
     */
    private byte[] template;

}
