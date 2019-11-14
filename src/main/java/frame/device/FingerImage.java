package frame.device;

import constant.ImageConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

@Data
public class FingerImage extends JPanel{

    private Image image;

    public FingerImage() {
        image = Toolkit.getDefaultToolkit().createImage(ImageConstant.FINGERING);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 100, 0, this);
        }
    }


}
