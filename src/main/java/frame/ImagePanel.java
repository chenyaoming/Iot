package frame;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

@Data
@NoArgsConstructor
public class ImagePanel extends JPanel {

    Image image = null;

    private String imagePath = "";

    public ImagePanel(String imagePath){
        this.imagePath = imagePath;
    }

    @Override
    public void paint(Graphics g) {
        try {
            if(StringUtils.isNotEmpty(imagePath)){
                image = ImageIO.read(new File(imagePath));
                g.drawImage(image, 0, 0, 500, 400, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
