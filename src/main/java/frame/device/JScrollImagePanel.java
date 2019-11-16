package frame.device;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uitl.CommonDbUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Data
@NoArgsConstructor
public class JScrollImagePanel extends JPanel{

    private static Logger LOG = LoggerFactory.getLogger(JScrollImagePanel.class);

    BufferedImage image = null;
    private String imagePath = "";

    private int width ;
    private int height;

    public JScrollImagePanel(String imagePath){
        this.imagePath = imagePath;
        File file = new File(imagePath.trim());
        if(file.exists()) {
            try {
                image = ImageIO.read(file);
                width = image.getWidth();
                height = image.getHeight();
                this.setPreferredSize(new Dimension(image.getWidth(),image.getHeight()));
            } catch (IOException e) {
                LOG.error("加载图片报错:",e);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        try {
            if(StringUtils.isNotEmpty(imagePath)){
                File file = new File(imagePath.trim());
                if(file.exists()){
                    image = ImageIO.read(file);
                    width = image.getWidth();
                    height = image.getHeight();
                    g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                }
            }
        } catch (Exception e) {
            LOG.error("加载图片paint报错:",e);
        }
    }

}
