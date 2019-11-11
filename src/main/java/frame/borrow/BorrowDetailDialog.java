package frame.borrow;


import bean.TbDevice;
import dao.DaoFactory;
import frame.device.ImagePanel;
import frame.device.JScrollImagePanel;
import frame.user.FingerDialog;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import uitl.FingerHelper;
import uitl.JFileChooserUtil;
import uitl.ModalFrameUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class BorrowDetailDialog extends JFrame{

    private JFrame parentFrame;

    private TbDevice oldDevice;

    /**
     * 查询按钮
     */
    private JButton searchBtn;

    private volatile JFrame thisDialog;

    public BorrowDetailDialog(JFrame parentFrame, JButton searchBtn, TbDevice oldDevice){
        this.setTitle("设备信息");
        this.parentFrame = parentFrame;
        thisDialog = this;
        this.searchBtn = searchBtn;

        // 创建一个模态对话框
       // final JFrame dialog = new JFrame("");
        // 设置对话框的宽高
        //dialog.setSize(400, 400);
        this.setSize(500, 500);
        // 设置对话框大小不可改变
        this.setResizable(false);
        // 设置对话框相对显示的位置
        this.setLocationRelativeTo(parentFrame);
        this.setLayout(null);

        Font f1 = new Font("楷体", Font.BOLD, 19);
        Font f2 = new Font("楷体", Font.BOLD, 16);


        JTextField idTextField =new JTextField(30);
        idTextField.setVisible(false);
        this.add(idTextField);


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

        JButton cancelBtn = new JButton("取消");
        cancelBtn.setBounds(160, 420, 80, 30);
        JButton nextBtn = new JButton("保存");
        nextBtn.setBounds(280, 420,  80, 30);
        this.add(cancelBtn);
        this.add(nextBtn);

        if(null != oldDevice){
            idTextField.setText(oldDevice.getId().toString());
            jNameTextField.setText(oldDevice.getName());
            jTypeNumTextField.setText(oldDevice.getTypeNum());

            jCodeTextField.setText(oldDevice.getCode());
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
                    JFrame imageFrame = new JFrame();

                    // 设置对话框的宽高
                    imageFrame.setSize(550, 450);
                    imageFrame.setLocationRelativeTo(parentFrame);
                    imageFrame.toFront();

                    JScrollImagePanel jScrollImagePanel = new JScrollImagePanel(iPanel.getImagePath());
                    JScrollPane scrollPane=new JScrollPane();
                    scrollPane.setViewportView(jScrollImagePanel);

                    //dialog.setSize(jScrollImagePanel.getWidth(), jScrollImagePanel.getHeight());
                    imageFrame.add(scrollPane,BorderLayout.CENTER);
                    //imageFrame.setVisible(true);
                    ModalFrameUtil.showAsModal(imageFrame,thisDialog);

                    imageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }

            }

        });



        // 下一步按钮
        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                FingerHelper fingerThread = new FingerHelper();
                BorrowFingerDialog fingerDialog = new BorrowFingerDialog(parentFrame,searchBtn,oldDevice);

                //fingerThread.setFingerDialog(fingerDialog);

                Thread dialogThread = new Thread(() -> {
                    /**
                     * 指纹弹窗
                     */
                    fingerDialog.showDialog();
                    fingerThread.interrupt();
                });
                dialogThread.start();
                fingerThread.start();

               /* thisDialog.dispose();
                JOptionPane.showMessageDialog(new JPanel(),"操作成功","提示",JOptionPane.PLAIN_MESSAGE);

                searchBtn.doClick();*/
            }
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
        //this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void showDialog(){
        //会阻塞
        ModalFrameUtil.showAsModal(this,parentFrame);
        //this.setVisible(true);
    }

    public boolean isNumeric(String string){
        Pattern pattern = compile("[0-9]*");
        return pattern.matcher(string).matches();
    }

}
