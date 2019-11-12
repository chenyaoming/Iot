package frame.borrow;

import bean.TbBorrowRecord;
import frame.FrameUtil;
import frame.device.ImagePanel;
import org.apache.commons.lang3.StringUtils;
import print.BorrowPrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BorrowFinishDialog extends JDialog{

    public BorrowFinishDialog(TbBorrowRecord record) {

        super(FrameUtil.currentFrame,"借出信息",true);

        // 创建一个模态对话框
        // 设置对话框的宽高
        this.setSize(500, 500);
        // 设置对话框大小不可改变
        this.setResizable(false);
        // 设置对话框相对显示的位置
        this.setLocationRelativeTo(FrameUtil.currentFrame);
        this.setLayout(null);

        Font f1 = new Font("楷体", Font.BOLD, 19);
        Font f2 = new Font("楷体", Font.BOLD, 16);


        JLabel borrowStatusLabel = new JLabel("出借状态");
        borrowStatusLabel.setBounds(100, 0, 90, 50);

        JLabel borrowStatus = new JLabel("出借成功");
        borrowStatus.setBounds(100, 0, 90, 50);

        JLabel borrowLabel = new JLabel(" 借用人");
        borrowLabel.setBounds(100, 0, 90, 50);

        JTextField borrowUserName = new JTextField(30);
        borrowUserName.setBounds(160, 10, 250, 30);
        this.add(borrowLabel);
        this.add(borrowUserName);

        JLabel borrowClickLabel = new JLabel("借出保管员");
        borrowClickLabel.setBounds(100, 0, 90, 50);

        JTextField borrowClickName = new JTextField(30);
        borrowClickName.setBounds(160, 10, 250, 30);

        this.add(borrowClickLabel);
        this.add(borrowClickName);

        JLabel jNameLabel = new JLabel("设备名称");
        jNameLabel.setBounds(100, 0, 90, 50);
        JTextField jNameTextField = new JTextField(30);
        jNameTextField.setBounds(160, 10, 250, 30);
        this.add(jNameLabel);
        this.add(jNameTextField);

        JLabel jTypeNumLabel = new JLabel("设备型号");
        jTypeNumLabel.setBounds(100, 50, 90, 50);
        JTextField jTypeNumTextField = new JTextField(30);
        jTypeNumTextField.setBounds(160, 60, 250, 30);
        this.add(jTypeNumLabel);
        this.add(jTypeNumTextField);

        JLabel jCodeLabel = new JLabel("设备编码");
        jCodeLabel.setBounds(100, 100, 90, 50);
        JTextField jCodeTextField = new JTextField(30);
        jCodeTextField.setBounds(160, 110, 250, 30);
        this.add(jCodeLabel);
        this.add(jCodeTextField);

        JLabel countLabel = new JLabel("借出数量");
        countLabel.setBounds(100, 150, 90, 50);

        JTextField borrowCountTextField = new JTextField(30);
        borrowCountTextField.setBounds(160, 160, 250, 30);
        borrowCountTextField.setText(record.getBorrowNum()+"");
        this.add(countLabel);
        this.add(borrowCountTextField);

        JLabel jPositionLabel = new JLabel("存放位置");
        jPositionLabel.setBounds(100, 200, 90, 50);
        JTextField jPositionTextField = new JTextField(30);
        jPositionTextField.setBounds(160, 210, 250, 30);
        this.add(jPositionLabel);
        this.add(jPositionTextField);

        JLabel jFeaturesLabel = new JLabel("设备功能");
        jFeaturesLabel.setBounds(100, 250, 90, 50);
        JTextField jfeaturesTextField = new JTextField(30);
        jfeaturesTextField.setBounds(160, 260, 250, 30);

        jfeaturesTextField.setBackground(Color.LIGHT_GRAY); //文本框背景设置为 亮灰色
        //tf.setBackground(new Color(244, 244, 244));//文本框背景设置为指定的颜色
        jfeaturesTextField.setEditable(false);//文本框设置为不可编辑

        this.add(jFeaturesLabel);
        this.add(jfeaturesTextField);

        JLabel imageLabel = new JLabel("设备图片");
        imageLabel.setBounds(100, 300, 90, 50);
        this.add(imageLabel);

        ImagePanel iPanel = new ImagePanel();
        iPanel.setBounds(160, 310, 100, 90);
        //iPanel.setVisible(false);
        this.add(iPanel);

        JButton printBtn = new JButton("打印");
        printBtn.setBounds(280, 420, 80, 30);
        this.add(printBtn);

        if (null != record) {
            jNameTextField.setText(record.getDeviceName());
            jTypeNumTextField.setText(record.getDeviceType());

            jCodeTextField.setText(record.getDeviceCode());
            jPositionTextField.setText(record.getDevicePosition());
            if (StringUtils.isNotBlank(record.getDeviceImage())) {
                iPanel.setImagePath(record.getDeviceImage());
                iPanel.repaint();
                //iPanel.setVisible(true);
            }
            jfeaturesTextField.setText(record.getFeatures());
        }

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
