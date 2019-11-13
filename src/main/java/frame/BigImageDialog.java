package frame;

import frame.device.JScrollImagePanel;
import uitl.ModalFrameUtil;

import javax.swing.*;
import java.awt.*;

public class BigImageDialog extends JFrame {

    private JFrame ownFrame =null;

    public BigImageDialog(JFrame ownFrame,String imgUrl){
        this.ownFrame = ownFrame;

        // 设置对话框的宽高
        this.setSize(550, 450);
        this.setLocationRelativeTo(ownFrame);

        JScrollImagePanel jScrollImagePanel = new JScrollImagePanel(imgUrl);
        JScrollPane scrollPane=new JScrollPane();
        scrollPane.setViewportView(jScrollImagePanel);

        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void showDialog(){
        ModalFrameUtil.showAsModal(this,ownFrame);
    }

}
