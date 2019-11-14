package frame.user;


import bean.TbUser;
import frame.FrameUtil;
import frame.device.FingerImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FingerDialog extends JDialog  {


    private TbUser newUser;

    /**
     * 提示还要按多少次指纹的标签
     */
    private JLabel tipLabel;


    private volatile JDialog thisDialog;

    /**
     * 指纹标语
     */
    public static final String TIPTEMP = "还需用同一手指按 %s 次指纹";

    public FingerDialog(TbUser newUser){

        super(FrameUtil.currentFrame,"指纹信息",true);

        thisDialog = this;

        //thisDialog = this;
        this.newUser = newUser;

        // 处理鼠标点击
        this.setLayout(new BorderLayout());

        // 设置对话框的宽高
        this.setSize(500, 400);
        this.setLocationRelativeTo(FrameUtil.currentFrame);
        this.toFront();

        FingerImage fingerImageLabel = new FingerImage();

        this.add(fingerImageLabel, BorderLayout.CENTER);

        JPanel labelPanel = new JPanel();
        JLabel tipLabel = new JLabel( String.format(TIPTEMP, 3));

        Font font = new Font("宋体", Font.PLAIN, 25);
        tipLabel.setFont(font);
        tipLabel.setForeground(Color.RED);

        this.tipLabel = tipLabel;

        labelPanel.add(tipLabel);

        this.add(labelPanel,BorderLayout.SOUTH);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               thisDialog.dispose();
                new UserAddDialog(newUser).showDialog();
            }
        });

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public void showDialog(){
        //会阻塞
        //ModalFrameUtil.showAsModal(this,parentFrame);
        this.setVisible(true);
    }


    public TbUser getNewUser() {
        return newUser;
    }

    public void setNewUser(TbUser newUser) {
        this.newUser = newUser;
    }

    public JLabel getTipLabel() {
        return tipLabel;
    }

    public void setTipLabel(JLabel tipLabel) {
        this.tipLabel = tipLabel;
    }

}
