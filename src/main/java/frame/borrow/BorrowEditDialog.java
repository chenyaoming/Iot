package frame.borrow;


import bean.TbBorrowRecord;
import bean.TbDevice;
import constant.ImageConstant;
import dao.DaoFactory;
import frame.BigImageDialog;
import frame.FrameUtil;
import frame.device.ImagePanel;
import frame.device.JScrollImagePanel;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import uitl.JFileChooserUtil;
import uitl.ModalFrameUtil;
import uitl.NumberUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class BorrowEditDialog extends JFrame{


    private JFrame thisDialog;

    public BorrowEditDialog(TbBorrowRecord record){
        this.setTitle("借出详情");
        thisDialog = this;

        this.setIconImage(new ImageIcon(ImageConstant.LOGO).getImage());

        // 创建一个模态对话框
       // final JFrame dialog = new JFrame("");
        // 设置对话框的宽高
        //dialog.setSize(400, 400);
        this.setSize(500, 650);
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

        JLabel countLabel = new JLabel("借出数量");
        countLabel.setBounds(100, 150, 90, 50);
        JTextField countTextField=new JTextField(30);
        countTextField.setBounds(160, 160, 250, 30);
        this.add(countLabel);
        this.add(countTextField);

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
        this.add(jFeaturesLabel);
        this.add(jfeaturesTextField);

        JLabel imageLabel = new JLabel("设备图片");
        imageLabel.setBounds(100, 300, 90, 50);
        JButton imageBtn  = new JButton("请选择图片...");
        imageBtn.setBounds(160, 310, 100, 30);
        this.add(imageLabel);
        this.add(imageBtn);

        ImagePanel iPanel = new ImagePanel();
        iPanel.setBounds(280, 300, 100, 90);
        //iPanel.setVisible(false);
        this.add(iPanel);


        JLabel remarkLabel = new JLabel("    备注");
        remarkLabel.setBounds(100, 390, 90, 50);
        JTextField remarkField=new JTextField(30);
        remarkField.setBounds(160, 400, 250, 30);
        this.add(remarkLabel);
        this.add(remarkField);

        JLabel borrowUserNameLabel = new JLabel("借用人:");
        borrowUserNameLabel.setBounds(100, 430, 100, 50);
        JLabel borrowUserNameValueLabel = new JLabel(record.getBorrowUserName());
        borrowUserNameValueLabel.setBounds(160, 430, 100, 50);
        this.add(borrowUserNameLabel);
        this.add(borrowUserNameValueLabel);

        JLabel borrowDateLabel = new JLabel("借用日期：");
        borrowDateLabel.setBounds(270, 430, 100, 50);
        JLabel borrowValueDateLabel = new JLabel();
        if(null != record.getBorrowDate()){
            borrowValueDateLabel.setText(new SimpleDateFormat("yyyy-MM-dd").format(record.getBorrowDate()));
        }
        borrowValueDateLabel.setBounds(340, 430, 100, 50);
        this.add(borrowDateLabel);
        this.add(borrowValueDateLabel);


        JLabel borrowClickUserNameLabel = new JLabel("借出保管人：");
        borrowClickUserNameLabel.setBounds(100, 460, 100, 50);
        JLabel borrowClickUserNameValueLabel = new JLabel(record.getBorrowClerkUserName());
        borrowClickUserNameValueLabel.setBounds(180, 460, 100, 50);
        this.add(borrowClickUserNameLabel);
        this.add(borrowClickUserNameValueLabel);

        JLabel statusLabel = new JLabel("状态：");
        statusLabel.setBounds(270, 460, 100, 50);
        JLabel statusValueLabel = new JLabel(record.getStatus() == "RETURNED" ? "已归还":"已出借");
        statusValueLabel.setBounds(320, 460, 100, 50);
        this.add(statusLabel);
        this.add(statusValueLabel);

        JLabel returnUserNameLabel = new JLabel("归还人:");
        returnUserNameLabel.setBounds(100, 490, 100, 50);
        JLabel returnUserNameValueLabel = new JLabel(record.getReturnClerkUserName());
        returnUserNameValueLabel.setBounds(160, 490, 100, 50);
        this.add(returnUserNameLabel);
        this.add(returnUserNameValueLabel);

        JLabel returnDateLabel = new JLabel("归还日期：");
        returnDateLabel.setBounds(270, 490, 100, 50);
        JLabel returnDateValueLabel = new JLabel();
        if(null != record.getReturnDate()){
            returnDateValueLabel.setText(new SimpleDateFormat("yyyy-MM-dd").format(record.getReturnDate()));
        }
        returnDateValueLabel.setBounds(340, 490, 100, 50);
        this.add(returnDateLabel);
        this.add(returnDateValueLabel);



        JLabel returnClickUserNameLabel = new JLabel("归还保管人：");
        returnClickUserNameLabel.setBounds(100, 520, 100, 50);
        JLabel returnClickUserNameValueLabel = new JLabel(record.getReturnClerkUserName());
        returnClickUserNameValueLabel.setBounds(180, 520, 100, 50);
        this.add(returnClickUserNameLabel);
        this.add(returnClickUserNameValueLabel);


        JButton cancelBtn = new JButton("取消");
        cancelBtn.setBounds(160, 570, 80, 30);
        JButton saveBtn = new JButton("保存");
        saveBtn.setBounds(280, 570,  80, 30);
        this.add(cancelBtn);
        this.add(saveBtn);

        if(null != record){
            jNameTextField.setText(record.getDeviceName());
            jTypeNumTextField.setText(record.getDeviceType());

            jCodeTextField.setText(record.getDeviceCode());
            countTextField.setText(record.getBorrowNum()+"");

            jPositionTextField.setText(record.getDevicePosition());
            if(StringUtils.isNotBlank(record.getDeviceImage())){
                iPanel.setImagePath(record.getDeviceImage());
                iPanel.repaint();
                //iPanel.setVisible(true);
            }
            jfeaturesTextField.setText(record.getFeatures());
            remarkField.setText(record.getRemark());
        }

        iPanel.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                // 处理鼠标点击
                if(StringUtils.isNotBlank(iPanel.getImagePath()) && new File(iPanel.getImagePath().trim()).exists()){
                   new BigImageDialog(thisDialog,iPanel.getImagePath()).showDialog();
                }

            }

        });

        // 选择图片
        imageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String[] picSufix = new String[]{".jpg",".jpeg",".png"};

                File selectedFile = JFileChooserUtil.getSelectedOpenFile(picSufix,FrameUtil.currentFrame);
                if(null != selectedFile){
                    iPanel.setImagePath(selectedFile.getPath());
                    iPanel.repaint();
                    iPanel.setVisible(true);
                }
            }
        });




        // 保存按钮
        saveBtn.addActionListener(e -> {

            TbBorrowRecord newRecord = new TbBorrowRecord();
            newRecord.setId(record.getId());

            newRecord.setDeviceName(jNameTextField.getText());
            newRecord.setDeviceType(jTypeNumTextField.getText());
            newRecord.setDeviceCode(jCodeTextField.getText());
            newRecord.setDevicePosition( jPositionTextField.getText());
            newRecord.setDeviceImage(iPanel.getImagePath());
            newRecord.setFeatures(jfeaturesTextField.getText());
            newRecord.setRemark(remarkField.getText());


            if(StringUtils.isBlank(countTextField.getText())){
                JOptionPane.showMessageDialog(new JPanel(),"请填写借出数量","提示",1);
                return;
            }else {
                if(!NumberUtil.isNumeric(countTextField.getText())){
                    JOptionPane.showMessageDialog(new JPanel(),"借出数量请输入正整数","提示",1);
                    return;
                }else {
                    newRecord.setBorrowNum(Integer.valueOf(countTextField.getText().trim()));
                }
            }

            if(StringUtils.isBlank(newRecord.getDeviceName())){
                JOptionPane.showMessageDialog(new JPanel(),"请填写设备名称","提示",1);
                return;
            }
            if(StringUtil.isBlank(newRecord.getDeviceType())){
                JOptionPane.showMessageDialog(new JPanel(),"请填写设备型号","提示",1);
                return;
            }
            if(StringUtils.isBlank(newRecord.getDeviceCode())){
                JOptionPane.showMessageDialog(new JPanel(),"请填写设备编码","提示",1);
                return;
            }
            if(null == newRecord.getBorrowNum()){
                JOptionPane.showMessageDialog(new JPanel(),"请填写借出数量数量","提示",1);
                return;
            }

            if(StringUtils.isNotBlank(newRecord.getDeviceImage())){
                newRecord.setDeviceImage(JFileChooserUtil.writeImgToUpload(new File(newRecord.getDeviceImage().trim())));
            }
            DaoFactory.getBorrowRecordDao().updateRecord(newRecord);

            thisDialog.dispose();
            JOptionPane.showMessageDialog(FrameUtil.currentFrame,"操作成功","提示",1);

            FrameUtil.doClickSearchBtn();
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
        ModalFrameUtil.showAsModal(this, FrameUtil.currentFrame);
        //this.setVisible(true);
    }

}
