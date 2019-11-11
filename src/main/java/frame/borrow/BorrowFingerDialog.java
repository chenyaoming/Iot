package frame.borrow;


import bean.TbDevice;
import bean.TbUser;
import frame.device.FingerImage;
import frame.user.UserAddDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BorrowFingerDialog extends JDialog  {

    private JFrame parentFrame;
    /**
     * 提示还要按多少次指纹的标签
     */
    private JLabel tipLabel;

    /**
     * 查询按钮
     */
    private JButton searchBtn;

    private volatile JDialog thisDialog;

    /**
     * 指纹标语
     */
    public static final String TIPTEMP = "请%s按下指纹";

    public BorrowFingerDialog(JFrame parentFrame, JButton searchBtn, TbDevice device){

        super(parentFrame,"指纹信息",true);

        thisDialog = this;

        //thisDialog = this;
        this.parentFrame = parentFrame;
        this.searchBtn = searchBtn;

        // 处理鼠标点击
        this.setLayout(new BorderLayout());

        // 设置对话框的宽高
        this.setSize(550, 450);
        this.setLocationRelativeTo(parentFrame);
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
                //new UserAddDialog( parentFrame,device,searchBtn).showDialog();
            }
        });

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public void showDialog(){
        //会阻塞
        //ModalFrameUtil.showAsModal(this,parentFrame);
        this.setVisible(true);
    }

    public JFrame getParentFrame() {
        return parentFrame;
    }

    public void setParentFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }


    public JLabel getTipLabel() {
        return tipLabel;
    }

    public void setTipLabel(JLabel tipLabel) {
        this.tipLabel = tipLabel;
    }

    public JButton getSearchBtn() {
        return searchBtn;
    }

    public void setSearchBtn(JButton searchBtn) {
        this.searchBtn = searchBtn;
    }
}
