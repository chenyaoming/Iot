package frame.device;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

@Data
@NoArgsConstructor
public class ImagePanel extends JLabel {

    private static Logger LOG = LoggerFactory.getLogger(ImagePanel.class);

    Image image = null;

    private String imagePath = "";

    public ImagePanel(String imagePath){
        this.imagePath = imagePath;
    }

    @Override
    public void paint(Graphics g) {
        try {
            if(StringUtils.isNotEmpty(imagePath)){
                File file = new File(imagePath.trim());
                if(file.exists()){
                    image = ImageIO.read(file);
                    g.drawImage(image, 0, 0, 90, 90, null);
                }
            }
        } catch (Exception e) {
            LOG.error("加载图片paint报错:",e);
        }
    }

}
