package frame.borrow;


import bean.TbBorrowRecord;
import frame.FrameUtil;
import frame.device.FingerImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BorrowFingerDialog extends JDialog  {

    /**
     * 提示还要按多少次指纹的标签
     */
    private JLabel tipLabel;

    private JDialog thisDialog;

    private TbBorrowRecord record;

    /**
     * 指纹标语
     */
    public static final String TIPTEMP = "请%s按下指纹";

    public BorrowFingerDialog(TbBorrowRecord record){

        super(FrameUtil.currentFrame,"指纹信息",true);
        thisDialog = this;

        this.record = record;

        // 处理鼠标点击
        this.setLayout(new BorderLayout());

        // 设置对话框的宽高
        this.setSize(500, 400);
        this.setLocationRelativeTo(FrameUtil.currentFrame);
        this.toFront();

        FingerImage fingerImageLabel = new FingerImage();

        this.add(fingerImageLabel, BorderLayout.CENTER);

        JPanel labelPanel = new JPanel();
        JLabel tipLabel = null;
        if(null == record.getId()){
            tipLabel = new JLabel( String.format(TIPTEMP, "借用人"));
        }else{
            tipLabel = new JLabel( String.format(TIPTEMP, "归还人"));
        }


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
               //借出时才弹出
               if(null == record.getId()){
                   new BorrowDetailDialog(record).showDialog();
               }
            }
        });

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public void showDialog(){
        //会阻塞
        //ModalFrameUtil.showAsModal(this,parentFrame);
        this.setVisible(true);
    }

    public JLabel getTipLabel() {
        return tipLabel;
    }

    public void setTipLabel(JLabel tipLabel) {
        this.tipLabel = tipLabel;
    }

    public TbBorrowRecord getRecord() {
        return record;
    }

    public void setRecord(TbBorrowRecord record) {
        this.record = record;
    }
}
