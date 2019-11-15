package frame.device;


import bean.TbDevice;
import constant.ImageConstant;
import dao.DaoFactory;
import frame.BigImageDialog;
import frame.FrameUtil;
import jodd.util.StringUtil;
import label.RequiredLabel;
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
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class DeviceAddDialog extends JFrame{


    private TbDevice oldDevice;

    private volatile JFrame thisDialog;

    public DeviceAddDialog(TbDevice oldDevice){
        this.setTitle("设备信息");
        thisDialog = this;

        this.setIconImage(new ImageIcon(ImageConstant.LOGO).getImage());

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


        JTextField idTextField =new JTextField(30);
        idTextField.setVisible(false);
        this.add(idTextField);

        //RequiredLabel jNameLabel = new RequiredLabel("设备名称");
        RequiredLabel jNameLabel = new RequiredLabel("设备名称");
        jNameLabel.setBounds(90, 0, 90, 50);
        JTextField jNameTextField=new JTextField(30);
        jNameTextField.setBounds(160, 10, 250, 30);
        this.add(jNameLabel);
        this.add(jNameTextField);

        RequiredLabel jTypeNumLabel = new RequiredLabel("设备型号");
        jTypeNumLabel.setBounds(90, 50, 90, 50);
        JTextField jTypeNumTextField=new JTextField(30);
        jTypeNumTextField.setBounds(160, 60, 250, 30);
        this.add(jTypeNumLabel);
        this.add(jTypeNumTextField);

        RequiredLabel jCodeLabel = new RequiredLabel("设备编码");
        jCodeLabel.setBounds(90, 100, 90, 50);
        JTextField jCodeTextField=new JTextField(30);
        jCodeTextField.setBounds(160, 110, 250, 30);
        this.add(jCodeLabel);
        this.add(jCodeTextField);

        RequiredLabel countLabel = new RequiredLabel("库存数量");
        countLabel.setBounds(90, 150, 90, 50);
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
        iPanel.setBounds(280, 310, 100, 90);
        //iPanel.setVisible(false);
        this.add(iPanel);

        JButton cancelBtn = new JButton("取消");
        cancelBtn.setBounds(160, 420, 80, 30);
        JButton saveBtn = new JButton("保存");
        saveBtn.setBounds(280, 420,  80, 30);
        this.add(cancelBtn);
        this.add(saveBtn);

        if(null != oldDevice){
            idTextField.setText(oldDevice.getId().toString());
            jNameTextField.setText(oldDevice.getName());
            jTypeNumTextField.setText(oldDevice.getTypeNum());

            jCodeTextField.setText(oldDevice.getCode());
            countTextField.setText(oldDevice.getCount()+"");

            jPositionTextField.setText(oldDevice.getSavePosition());
            if(StringUtils.isNotBlank(oldDevice.getImage())){
                iPanel.setImagePath(oldDevice.getImage());
                iPanel.repaint();
                //iPanel.setVisible(true);
            }
            jfeaturesTextField.setText(oldDevice.getFeatures());
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
        imageBtn.addActionListener(e -> {

            String[] picSufix = new String[]{".jpg",".jpeg",".png"};

            File selectedFile = JFileChooserUtil.getSelectedOpenFile(picSufix,FrameUtil.currentFrame);
            if(null != selectedFile){
                iPanel.setImagePath(selectedFile.getPath());
                iPanel.repaint();
                iPanel.setVisible(true);
            }
        });

        // 保存按钮
        saveBtn.addActionListener(e -> {

            TbDevice newDevice = new TbDevice(jNameTextField.getText(),jTypeNumTextField.getText(),jCodeTextField.getText(),
                    jPositionTextField.getText(),iPanel.getImagePath(),jfeaturesTextField.getText());

            if(null != oldDevice){
                newDevice.setId(oldDevice.getId());
            }

            if(StringUtils.isBlank(newDevice.getName())){
                JOptionPane.showMessageDialog(new JPanel(),"请填写设备名称","提示",1);
                return;
            }
            if(StringUtil.isBlank(newDevice.getTypeNum())){
                JOptionPane.showMessageDialog(new JPanel(),"请填写设备型号","提示",1);
                return;
            }
            if(StringUtils.isBlank(newDevice.getCode())){
                JOptionPane.showMessageDialog(new JPanel(),"请填写设备编码","提示",1);
                return;
            }
            if(StringUtils.isBlank(countTextField.getText())){
                JOptionPane.showMessageDialog(new JPanel(),"请填写库存数量","提示",1);
                return;
            }else {
                if(!NumberUtil.isNumeric(countTextField.getText())){
                    JOptionPane.showMessageDialog(new JPanel(),"库存数量请输入正整数","提示",1);
                    return;
                }else {
                    newDevice.setCount(Integer.valueOf(countTextField.getText().trim()));
                }
            }

            if(StringUtils.isNotBlank(newDevice.getImage())){
                newDevice.setImage(JFileChooserUtil.writeImgToUpload(new File(newDevice.getImage().trim())));
            }
            if(null != newDevice.getId()){
                //编辑更新
                DaoFactory.getDeviceDao().update(newDevice);
            }else{
                //增加
                DaoFactory.getDeviceDao().insert(newDevice);
            }

            thisDialog.dispose();
            JOptionPane.showMessageDialog(FrameUtil.currentFrame,"操作成功","提示",1);

            FrameUtil.doClickSearchBtn();
        });

        // 取消按钮
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisDialog.dispose();
            }
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
