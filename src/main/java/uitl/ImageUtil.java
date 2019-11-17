package uitl;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ImageUtil {

    public static final List<String> PIC_SUFIX_LIST = Arrays.asList(new String[]{"jpg","jpeg","png"});

    public static final String[] PIC_DOT_SUFIX = new String[]{".jpg",".jpeg",".png"};

    private static Logger LOG = LoggerFactory.getLogger(ImageUtil.class);

    public static void setImage(JFrame frame,String imageUri){
        try {
            frame.setIconImage(new ImageIcon(Image.class.getResource(imageUri)).getImage());
        }catch (Exception e){
            LOG.error("加载图片错误:{}",imageUri);
        }
    }

    public static Image getImage(String imageUri){
        try {
            return new ImageIcon(Image.class.getResource(imageUri)).getImage();
        }catch (Exception e){
            LOG.error("加载图片错误:{}",imageUri);
        }
        return null;
    }

}
