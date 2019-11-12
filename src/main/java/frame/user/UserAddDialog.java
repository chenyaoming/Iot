package frame.user;


import bean.TbUser;
import dao.DaoFactory;
import frame.FrameUtil;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import uitl.FingerHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class UserAddDialog extends JDialog {


    private JDialog thisDialog;

    public UserAddDialog(TbUser oldUser){

        super(FrameUtil.currentFrame,"用户信息",true);


        thisDialog = this;

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


        JLabel nameLabel = new JLabel("用户姓名");
        nameLabel.setBounds(100, 0, 90, 50);
        JTextField nameField=new JTextField(30);
        nameField.setBounds(160, 10, 250, 30);
        this.add(nameLabel);
        this.add(nameField);

        // 创建两个单选按钮
        JRadioButton manBtn = new JRadioButton("男");
        JRadioButton remanBtn = new JRadioButton("女");

        // 创建按钮组，把两个单选按钮添加到该组
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(manBtn);
        buttonGroup.add(remanBtn);
        // 设置第一个单选按钮选中
        manBtn.setSelected(true);


        JLabel genderLabel = new JLabel("用户性别");
        genderLabel.setBounds(100, 50, 90, 50);

        manBtn.setBounds(170, 50, 50, 50);
        remanBtn.setBounds(230, 50, 50, 50);

        this.add(genderLabel);
        this.add(manBtn);
        this.add(remanBtn);


        JLabel ageLabel = new JLabel("用户年龄");
        ageLabel.setBounds(100, 100, 90, 50);
        JTextField ageField=new JTextField(30);
        ageField.setBounds(160, 110, 250, 30);
        this.add(ageLabel);
        this.add(ageField);

        JLabel phoneLabel = new JLabel("联系电话");
        phoneLabel.setBounds(100, 150, 90, 50);
        JTextField phoneField=new JTextField(30);
        phoneField.setBounds(160, 160, 250, 30);
        this.add(phoneLabel);
        this.add(phoneField);

        JLabel createDateLabel = new JLabel("创建时间");
        createDateLabel.setBounds(100, 200, 90, 50);

        JLabel createDateValueLabel = new JLabel();
        createDateValueLabel.setBounds(160, 210, 250, 30);

        this.add(createDateLabel);
        this.add(createDateValueLabel);


        JButton cancelBtn = new JButton("取消");
        cancelBtn.setBounds(160, 370, 80, 30);

        JButton saveBtn = new JButton("保存");
        saveBtn.setBounds(310, 370,  80, 30);

        this.add(cancelBtn);
        this.add(saveBtn);



        if(null == oldUser || null == oldUser.getId()){
            saveBtn.setText("下一步");
        }

        //当是录入指纹页的页面返回时，id也是空的
        if(null != oldUser){
            if(null != oldUser.getId()){
                idTextField.setText(oldUser.getId().toString());
            }
            nameField.setText(oldUser.getName());

            if("女".equals(oldUser.getGender())){
                remanBtn.setSelected(true);
            }

            //genderField.setText(oldUser.getGender());
            if(null != oldUser.getAge()){
                ageField.setText(oldUser.getAge().toString());
            }
            phoneField.setText(oldUser.getPhone());
            if(null != oldUser.getCreateDate()){
                createDateValueLabel.setText(new SimpleDateFormat("yyyy-MM-dd").format(oldUser.getCreateDate()));
            }
        }
        if(StringUtils.isBlank(createDateValueLabel.getText())){
            createDateValueLabel.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }

        // 保存按钮
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                TbUser newUser = new TbUser();
                if(null != oldUser){
                    newUser.setId(oldUser.getId());
                }
                newUser.setName(nameField.getText());
                newUser.setCreateDate(new Date());
                newUser.setPhone(phoneField.getText());

                if(!isNumeric(ageField.getText())){
                    JOptionPane.showMessageDialog(new JPanel(),"年龄请输入正整数","提示",1);
                    return;
                }
                if(StringUtils.isNotBlank(ageField.getText())){
                    newUser.setAge(Integer.valueOf(ageField.getText()));
                }

                newUser.setGender("男");
                if(remanBtn.isSelected()){
                    newUser.setGender("女");
                }
                if(null != oldUser){
                    newUser.setId(oldUser.getId());
                }

                if(StringUtils.isBlank(newUser.getName())){
                    JOptionPane.showMessageDialog(new JPanel(),"请填写姓名","提示",1);
                    return;
                }
                if(StringUtil.isBlank(newUser.getGender())){
                    JOptionPane.showMessageDialog(new JPanel(),"请选择性别","提示",1);
                    return;
                }
                if(null == newUser.getAge()){
                    JOptionPane.showMessageDialog(new JPanel(),"请填写年龄","提示",1);
                    return;
                }
                if(StringUtils.isBlank(newUser.getPhone())){
                    JOptionPane.showMessageDialog(new JPanel(),"请填写联系电话","提示",1);
                    return;
                }

                if(null != newUser.getId()){
                    //编辑更新
                    DaoFactory.getUserDao().update(newUser);


                    JOptionPane.showMessageDialog(new JPanel(),"操作成功","提示",JOptionPane.PLAIN_MESSAGE);

                    thisDialog.dispose();

                    FrameUtil.doClickSearchBtn();
                }else{
                    //增加
                    //DaoFactory.getUserDao().insert(newUser);

                    FingerDialog fingerDialog = new FingerDialog(newUser);
                    FingerHelper fingerThread = new FingerHelper(fingerDialog);

                    /**
                     * 让添加的弹框隐藏
                     */
                    //thisDialog.setVisible(false);
                    thisDialog.dispose();

                    Thread dialogThread = new Thread(() -> {
                        /**
                         * 指纹弹窗
                         */
                        fingerDialog.showDialog();
                        fingerThread.interrupt();
                    });
                    dialogThread.start();
                    fingerThread.start();

                }
            }
        });

        //取消按钮加监听事件
        cancelBtnAddActionListener(cancelBtn);

        // 显示对话框
        //这个只能调用一次，不然会删两次才能删掉
        //dialog.setVisible(true);
        //  this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void showDialog(){
        //ModalFrameUtil.showAsModal(this,parentFrame);
        this.setVisible(true);
    }

    private void cancelBtnAddActionListener(JButton cancelBtn) {
        // 取消按钮
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisDialog.dispose();
            }
        });
    }


    public boolean isNumeric(String string){
        Pattern pattern = compile("[0-9]*");
        return pattern.matcher(string).matches();
    }


}
