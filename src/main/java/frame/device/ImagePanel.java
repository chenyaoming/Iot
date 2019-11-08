package frame.device;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

@Data
@NoArgsConstructor
public class ImagePanel extends JLabel {

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
                    g.drawImage(image, 0, 0, 100, 100, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
