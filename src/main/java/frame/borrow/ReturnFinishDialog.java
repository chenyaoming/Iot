package frame.borrow;

import bean.TbBorrowRecord;
import dao.DaoFactory;
import frame.FrameUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ReturnFinishDialog extends JDialog{

    private JDialog thisDialog = null;

    public ReturnFinishDialog(TbBorrowRecord record) {

        super(FrameUtil.currentFrame,"归还信息",true);

        this.thisDialog = this;

        // 创建一个模态对话框
        // 设置对话框的宽高
        this.setSize(350, 200);
        // 设置对话框大小不可改变
        //this.setResizable(false);
        // 设置对话框相对显示的位置
        this.setLocationRelativeTo(FrameUtil.currentFrame);
        this.setLayout(null);

        Font f1 = new Font("宋体", Font.BOLD, 15);
        Font f2 = new Font("宋体", Font.PLAIN, 14);


        JLabel successLabel = new JLabel("归还成功");
        successLabel.setBounds(120, 0, 90, 50);
        successLabel.setFont(f1);
        successLabel.setForeground(Color.GREEN);


        JLabel remarkLabel = new JLabel(" 备注：");
        remarkLabel.setBounds(40, 40, 90, 50);

        JTextField remarkField = new JTextField(30);
        remarkField.setBounds(90, 50, 200, 30);

        this.add(successLabel);
        this.add(remarkLabel);
        this.add(remarkField);

        JButton comfireBtn = new JButton("提交");
        comfireBtn.setBounds(100, 95, 70, 30);
        this.add(comfireBtn);

        comfireBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(StringUtils.isNotBlank(remarkField.getText())){
                    DaoFactory.getBorrowRecordDao().updateRemark(remarkField.getText().trim(),record.getId());
                }else {
                    JOptionPane.showMessageDialog(new JPanel(),"请输入备注","提示",1);
                    return;
                }
                FrameUtil.getCurrentSearchBtn().doClick();
                thisDialog.dispose();
            }
        });

        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                thisDialog.dispose();
                FrameUtil.getCurrentSearchBtn().doClick();
            }
        });
    }

    public void showDialog(){
        //会阻塞
        this.setVisible(true);
    }

}
