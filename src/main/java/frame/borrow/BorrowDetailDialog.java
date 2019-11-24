package frame.borrow;


import bean.TbBorrowRecord;
import bean.TbDevice;
import constant.ImageConstant;
import dao.DaoFactory;
import frame.BigImageDialog;
import frame.FrameUtil;
import frame.InfiniteProgressPanel;
import frame.device.ImagePanel;
import helper.DeviceExportHelper;
import label.RequiredLabel;
import org.apache.commons.lang3.StringUtils;
import progress.BaseProgress;
import progress.MySwingWorker;
import uitl.FingerHelper;
import uitl.ImageUtil;
import uitl.ModalFrameUtil;
import uitl.NumberUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class BorrowDetailDialog extends JFrame{

    private JFrame thisDialog;

    public BorrowDetailDialog( TbBorrowRecord record){
        this.setTitle("设备信息");
        thisDialog = this;

        ImageUtil.setImage(this,ImageConstant.LOGO);

        // 创建一个模态对话框
       // final JFrame dialog = new JFrame("");
        // 设置对话框的宽高
        //dialog.setSize(400, 400);
        this.setSize(500, 500);
        // 设置对话框大小不可改变
        this.setResizable(false);
        // 设置对话框相对显示的位置
        this.setLocationRelativeTo(FrameUtil.currentFrame);
        this.setLayout(null);

        Font f1 = new Font("楷体", Font.BOLD, 19);
        Font f2 = new Font("楷体", Font.BOLD, 16);

        JLabel jNameLabel = new JLabel("设备名称");
        jNameLabel.setBounds(100, 0, 90, 50);
        JTextField jNameTextField=new JTextField(30);
        jNameTextField.setBounds(160, 10, 250, 30);
        this.add(jNameLabel);
        this.add(jNameTextField);

        JLabel jTypeNumLabel = new JLabel("设备型号");
        jTypeNumLabel.setBounds(100, 50, 90, 50);
        JTextField jTypeNumTextField=new JTextField(30);
        jTypeNumTextField.setBounds(160, 60, 250, 30);
        this.add(jTypeNumLabel);
        this.add(jTypeNumTextField);

        JLabel jCodeLabel = new JLabel("设备编码");
        jCodeLabel.setBounds(100, 100, 90, 50);
        JTextField jCodeTextField=new JTextField(30);
        jCodeTextField.setBounds(160, 110, 250, 30);
        this.add(jCodeLabel);
        this.add(jCodeTextField);

        RequiredLabel countLabel = new RequiredLabel("借出数量");
        countLabel.setBounds(100, 150, 90, 50);

        JTextField borrowCountTextField=new JTextField(30);
        borrowCountTextField.setBounds(160, 160, 250, 30);
        borrowCountTextField.setText("1"); //默认借出一个可以更改
        this.add(countLabel);
        this.add(borrowCountTextField);

        JLabel jPositionLabel = new JLabel("存放位置");
        jPositionLabel.setBounds(100, 200, 90, 50);
        JTextField jPositionTextField=new JTextField(30);
        jPositionTextField.setBounds(160, 210, 250, 30);
        this.add(jPositionLabel);
        this.add(jPositionTextField);

        JLabel jFeaturesLabel = new JLabel("设备功能");
        jFeaturesLabel.setBounds(100, 250, 90, 50);
        JTextField jfeaturesTextField=new JTextField(30);
        jfeaturesTextField.setBounds(160, 260, 250, 30);


        jNameTextField.setBackground(Color.LIGHT_GRAY);
        jNameTextField.setEditable(false);

        jTypeNumTextField.setBackground(Color.LIGHT_GRAY);
        jTypeNumTextField.setEditable(false);

        jCodeTextField.setBackground(Color.LIGHT_GRAY);
        jCodeTextField.setEditable(false);

        jPositionTextField.setBackground(Color.LIGHT_GRAY);
        jPositionTextField.setEditable(false);

        jfeaturesTextField.setBackground(Color.LIGHT_GRAY);
        jfeaturesTextField.setEditable(false);


        this.add(jFeaturesLabel);
        this.add(jfeaturesTextField);

        JLabel imageLabel = new JLabel("设备图片");
        imageLabel.setBounds(100, 300, 90, 50);
        this.add(imageLabel);

        ImagePanel iPanel = new ImagePanel();
        iPanel.setBounds(160, 310, 100, 90);
        //iPanel.setVisible(false);
        this.add(iPanel);

        JButton cancelBtn = new JButton("取消");
        cancelBtn.setBounds(160, 420, 80, 30);
        JButton nextBtn = new JButton("下一步");
        nextBtn.setBounds(280, 420,  80, 30);
        this.add(cancelBtn);
        this.add(nextBtn);

        if(null != record){
            jNameTextField.setText(record.getDeviceName());
            jTypeNumTextField.setText(record.getDeviceType());

            jCodeTextField.setText(record.getDeviceCode());
            jPositionTextField.setText(record.getDevicePosition());
            if(StringUtils.isNotBlank(record.getDeviceImage())){
                iPanel.setImagePath(record.getDeviceImage());
                iPanel.repaint();
                //iPanel.setVisible(true);
            }
            jfeaturesTextField.setText(record.getFeatures());
        }

        iPanel.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                // 处理鼠标点击
                if(StringUtils.isNotBlank(iPanel.getImagePath()) && new File(iPanel.getImagePath().trim()).exists()){
                    new BigImageDialog(FrameUtil.currentFrame,iPanel.getImagePath()).showDialog();
                }

            }

        });

        // 下一步按钮
        nextBtn.addActionListener(e -> {

            if(StringUtils.isBlank(borrowCountTextField.getText())){
                JOptionPane.showMessageDialog(thisDialog,"请输入借出数量","提示",1);
                return;
            }else {
                if(!NumberUtil.isNumeric(borrowCountTextField.getText())){
                    JOptionPane.showMessageDialog(thisDialog,"借出数量请输入正整数","提示",1);
                    return;
                }
            }

            TbDevice device = DaoFactory.getDeviceDao().queryById(record.getDeviceId());

            int borNum = Integer.parseInt(borrowCountTextField.getText().trim());
            if(borNum == 0 ){
                JOptionPane.showMessageDialog(thisDialog,"借出数量请输入正整数","提示",1);
                return;
            }

            if(borNum > device.getCount()){
                JOptionPane.showMessageDialog(thisDialog,"此设备库存数量不足, 只剩"+device.getCount(),"提示",1);
                return;
            }
            record.setBorrowNum(borNum);

            new MySwingWorker(thisDialog){
                @Override
                public void invokeBusiness() {
                    try {
                        BorrowFingerDialog borrowFingerDialog = new BorrowFingerDialog(record);
                        FingerHelper fingerThread = new FingerHelper(borrowFingerDialog);

                        Thread dialogThread = new Thread(() -> {
                            /**
                             * 指纹弹窗
                             */
                            borrowFingerDialog.showDialog();
                            fingerThread.interrupt();
                        });
                        dialogThread.start();
                        fingerThread.start();

                        thisDialog.dispose();

                    }catch (Exception ex){

                    }
                }
            }.execute();
        });

        // 取消按钮
        cancelBtn.addActionListener(e -> {
            thisDialog.dispose();
        });
        // 显示对话框
        //这个只能调用一次，不然会删两次才能删掉
        //dialog.setVisible(true);
    }

    public void showDialog(){
        //会阻塞
        ModalFrameUtil.showAsModal(this,FrameUtil.currentFrame);
        //this.setVisible(true);
    }

}
