package frame.borrow;

import bean.TbBorrowRecord;
import frame.FrameUtil;
import print.BorrowPrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;

public class BorrowFinishDialog extends JDialog{

    public BorrowFinishDialog(TbBorrowRecord record) {

        super(FrameUtil.currentFrame,"借出信息",true);

        // 创建一个模态对话框
        // 设置对话框的宽高
        this.setSize(480, 400);
        // 设置对话框大小不可改变
        //this.setResizable(false);
        // 设置对话框相对显示的位置
        this.setLocationRelativeTo(FrameUtil.currentFrame);
        this.setLayout(null);

        Font f1 = new Font("宋体", Font.BOLD, 15);
        Font f2 = new Font("宋体", Font.PLAIN, 14);


        JLabel successLabel = new JLabel("借出成功");
        successLabel.setBounds(170, 0, 90, 50);
        successLabel.setFont(f1);
        successLabel.setForeground(Color.GREEN);


        JLabel nameLabel = new JLabel("设备名称：");
        nameLabel.setBounds(120, 40, 90, 50);
        nameLabel.setFont(f2);

        JLabel nameValueLabel = new JLabel(record.getDeviceName());
        nameValueLabel.setBounds(200, 40, 90, 50);
        nameValueLabel.setFont(f2);

        JLabel typeLabel = new JLabel("设备型号：");
        typeLabel.setBounds(120, 80, 90, 50);
        typeLabel.setFont(f2);

        JLabel typeValueLabel = new JLabel(record.getDeviceType());
        typeValueLabel.setBounds(200, 80, 90, 50);
        typeValueLabel.setFont(f2);


        JLabel countLabel = new JLabel("借出数量：");
        countLabel.setBounds(120, 120, 90, 50);
        countLabel.setFont(f2);

        JLabel countValueLabel = new JLabel(record.getBorrowNum()+"");
        countValueLabel.setBounds(200, 120, 90, 50);
        countValueLabel.setFont(f2);

        JLabel borrowUsernameLabel = new JLabel("  借用人：");
        borrowUsernameLabel.setBounds(120, 160, 90, 50);
        borrowUsernameLabel.setFont(f2);

        JLabel borrowUsernameValueLabel = new JLabel(record.getBorrowUserName()+"");
        borrowUsernameValueLabel.setBounds(200, 160, 90, 50);
        borrowUsernameValueLabel.setFont(f2);

        JLabel borrowDateLabel = new JLabel("借用日期：");
        borrowDateLabel.setBounds(120, 200, 90, 50);
        borrowDateLabel.setFont(f2);

        String borrowDate = new SimpleDateFormat("yyyy-MM-dd").format(record.getBorrowDate());
        JLabel borrowDateValueLabel = new JLabel(borrowDate);
        borrowDateValueLabel.setBounds(200, 200, 90, 50);
        borrowDateValueLabel.setFont(f2);

        JLabel borrowClerkLabel = new JLabel("借出保管员：");
        borrowClerkLabel.setBounds(120, 240, 90, 50);
        borrowClerkLabel.setFont(f2);

        JLabel borrowClerkValueLabel = new JLabel(record.getBorrowClerkUserName());
        borrowClerkValueLabel.setBounds(210, 240, 90, 50);
        borrowClerkValueLabel.setFont(f2);

        this.add(successLabel);

        this.add(nameLabel);
        this.add(nameValueLabel);

        this.add(typeLabel);
        this.add(typeValueLabel);

        this.add(countLabel);
        this.add(countValueLabel);

        this.add(borrowUsernameLabel);
        this.add(borrowUsernameValueLabel);

        this.add(borrowDateLabel);
        this.add(borrowDateValueLabel);

        this.add(borrowClerkLabel);
        this.add(borrowClerkValueLabel);

        JButton printBtn = new JButton("打印");
        printBtn.setBounds(180, 290, 80, 30);
        this.add(printBtn);

        printBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BorrowPrinter(record).printBorrow();
            }
        });

    }

    public void showDialog(){
        //会阻塞
        this.setVisible(true);
    }

}
