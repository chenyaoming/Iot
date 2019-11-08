package frame.user;

import bean.TbUser;
import dao.DaoFactory;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import table.user.UserTable;
import uitl.ModalFrameUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class UserPanel extends JPanel {
    private JButton searchBtn, resetBtn, addBtn, btnEdit,prePage, nextPage, lastPage;
    private JPanel conditPanel, pagePanel;
    private JLabel userNameLabel, phoneLabel, pageInfo;
    private JTextField userNameField = null, phoneField = null;

    private JScrollPane jsp;

    UserTable table = null;

    public JFrame jf = null ;

    public UserPanel(JFrame jFrame) {
        jf = jFrame;
        table = new UserTable(jf);
        // 初始化所有控件
        initComponent();
        // 构造函数中调用initUI来向窗口中添加控件
        initUI();
        // 初始化操作函数
        initOperator();
        //初始化数据
        //showAllData();
        // 设置主窗体大小

    }

    private void initComponent() {
        conditPanel = new JPanel();
        pagePanel = new JPanel();

        searchBtn = new JButton("查询");

        /*Dimension preferredSize = new Dimension(60,30);
        searchBtn.setPreferredSize(preferredSize );*/

        resetBtn = new JButton("重置");
        addBtn = new JButton("新增");

        btnEdit = new JButton("编辑");


        prePage = new JButton("上一页");
        nextPage = new JButton("下一页");
        lastPage = new JButton("末  页");
        userNameLabel = new JLabel("姓名");
        userNameField = new JTextField(15);
        phoneLabel = new JLabel("联系电话");
        phoneField = new JTextField(15);

        jsp = new JScrollPane(table);

    }

    private void initUI() {

        conditPanel.setBorder(BorderFactory.createTitledBorder(" "));
        // 条件窗口控制条件
        //conditPanel.setLayout(new GridBagLayout());

        GridBagLayout gridBagLayout=new GridBagLayout(); //实例化布局对象
        conditPanel.setLayout(gridBagLayout);                     //jf窗体对象设置为GridBagLayout布局


        GridBagConstraints gridBagConstraints=new GridBagConstraints();//实例化这个对象用来对组件进行管理
       // gridBagConstraints.fill=GridBagConstraints.BOTH;//该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况
        //NONE：不调整组件大小。
        //HORIZONTAL：加宽组件，使它在水平方向上填满其显示区域，但是不改变高度。
        //VERTICAL：加高组件，使它在垂直方向上填满其显示区域，但是不改变宽度。
        //BOTH：使组件完全填满其显示区域。
        /*
         * 分别对组件进行设置
         */
        //组件1(gridx,gridy)组件的左上角坐标，gridwidth，gridheight：组件占用的网格行数和列数


        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(userNameLabel, gridBagConstraints);
        //组件2
        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridwidth=3;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(userNameField, gridBagConstraints);

        gridBagConstraints.gridx=4;
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(phoneLabel, gridBagConstraints);

        gridBagConstraints.gridx=5;
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridwidth=3;
        gridBagConstraints.gridheight=1;
        gridBagConstraints.insets = new Insets(0, 0, 0, 10);
        gridBagLayout.setConstraints(phoneField, gridBagConstraints);

        gridBagConstraints.gridx=8;
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(searchBtn, gridBagConstraints);

        gridBagConstraints.gridx=9;
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;

        gridBagLayout.setConstraints(resetBtn, gridBagConstraints);


        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=1;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(addBtn, gridBagConstraints);

        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=1;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(btnEdit, gridBagConstraints);



        conditPanel.add(userNameLabel);
        conditPanel.add(userNameField);
        conditPanel.add(phoneLabel);
        conditPanel.add(phoneField);

        conditPanel.add(searchBtn);
        conditPanel.add(resetBtn);

        conditPanel.add(addBtn);
        conditPanel.add(btnEdit);


        this.setLayout(new BorderLayout());
        this.add(conditPanel, BorderLayout.NORTH);

        // 设置查询结果标题
        jsp.setBorder(BorderFactory.createTitledBorder(" "));
        // 添加结果表格
        this.add(jsp, BorderLayout.CENTER);
        // 添加翻页按钮
        GridBagConstraints page = new GridBagConstraints();
        pagePanel.setLayout(new GridBagLayout());

        //设置分页bar信息
        pageInfo = new JLabel();
        setPageInfo();

        page.insets = new Insets(0, 0, 0, 200);
        pagePanel.add(pageInfo, page);
        page.insets = new Insets(0, 10, 0, 10);
        pagePanel.add(prePage, page);
        page.insets = new Insets(0, 10, 0, 10);
        pagePanel.add(nextPage, page);
        page.insets = new Insets(0, 10, 0, 10);
        pagePanel.add(lastPage, page);
        this.add(pagePanel, BorderLayout.SOUTH);

    }

    // 重置文本输入框
    private void clear() {
        userNameField.setText("");
        phoneField.setText("");
    }


    // 查询
    private void searchData() {
        table.setCurrentPage(1);

        //查询数据并且设置分页bar信息
        selectDataAndSetPageInfo();
    }

    /**
     * 查询数据并且设置分页bar信息
     */
    private void selectDataAndSetPageInfo() {

        TbUser user = new TbUser(userNameField.getText(),phoneField.getText());

        //设置记录总数
        table.setTotalRowCount((int) DaoFactory.getUserDao().countAllByCondition(user));
        //结果集的总页数
        table.setTotalPage(table.getTotalRowCount() % table.getPageCount() == 0
                ? table.getTotalRowCount()/  table.getPageCount() : table.getTotalRowCount() / table.getPageCount() + 1);


        List<TbUser> userList  = DaoFactory.getUserDao().findByConditionPage(user,table.getCurrentPage(),table.getPageCount());
        setPageInfo();
        table.showTable(userList);
    }


    private void initOperator() {

        // 查询事件
        // 增加回车事件
        //this.getRootPane().setDefaultButton(searchBtn);// 获取焦点
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchData();
            }
        });

        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        //新增
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCustomDialog(jf,jf,null);
                //add1();
            }
        });


        //编辑
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doEditAction();
            }
        });

        // 上一页
        prePage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.getPreviousPage();
                //查询数据并且设置分页bar信息
                selectDataAndSetPageInfo();
            }
        });
        // 下一页
        nextPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = table.getNextPage();
                if (i == 1) {
                    return;
                } else {
                    //查询数据并且设置分页bar信息
                    selectDataAndSetPageInfo();
                }
            }
        });
        // 末 页
        lastPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.getLastPage();
                //查询数据并且设置分页bar信息
                selectDataAndSetPageInfo();
            }

        });

    }

    private void doEditAction() {
        if(table.getSelectedRow() < 0){
            JOptionPane.showMessageDialog(jf,"请选择一条记录编辑","提示",1);
            return;
        }
        String id = table.getValueAt(table.getSelectedRow(), 0).toString();
        if(StringUtils.isBlank(id)){
            JOptionPane.showMessageDialog(jf,"此记录不存在","警告",2);
            return;
        }
        TbUser user = DaoFactory.getUserDao().queryById(Integer.valueOf(id));
        if(null == user){
            JOptionPane.showMessageDialog(jf,"此记录不存在","警告",2);
            return;
        }
        showCustomDialog(jf,jf,user);

    }

    private void setPageInfo() {
        pageInfo.setText("总共 " + table.getTotalRowCount() + " 条记录|当前第 "
                + table.getCurrentPage() + " 页|" + "总共 " + table.getTotalPage() + " 页");
    }

    /**
     * 显示一个自定义的对话框
     *
     * @param owner 对话框的拥有者
     * @param parentComponent 对话框的父级组件
     */
    private void showCustomDialog(Frame owner, Component parentComponent,TbUser oldUser) {
        // 创建一个模态对话框
        final JFrame dialog = new JFrame("用户信息");
        // 设置对话框的宽高
        //dialog.setSize(400, 400);
        dialog.setSize(500, 500);
        // 设置对话框大小不可改变
        dialog.setResizable(false);
        // 设置对话框相对显示的位置
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setLayout(null);

        Font f1 = new Font("楷体", Font.BOLD, 19);
        Font f2 = new Font("楷体", Font.BOLD, 16);


        JTextField idTextField =new JTextField(30);
        idTextField.setVisible(false);
        dialog.add(idTextField);


        JLabel nameLabel = new JLabel("姓名");
        nameLabel.setBounds(100, 0, 90, 50);
        JTextField nameField=new JTextField(30);
        nameField.setBounds(160, 10, 250, 30);
        dialog.add(nameLabel);
        dialog.add(nameField);

        JLabel genderLabel = new JLabel("性别");
        genderLabel.setBounds(100, 50, 90, 50);
        JTextField genderField=new JTextField(30);
        genderField.setBounds(160, 60, 250, 30);
        dialog.add(genderLabel);
        dialog.add(genderField);

        JLabel ageLabel = new JLabel("年龄");
        ageLabel.setBounds(100, 100, 90, 50);
        JTextField ageField=new JTextField(30);
        ageField.setBounds(160, 110, 250, 30);
        dialog.add(ageLabel);
        dialog.add(ageField);

        JLabel phoneLabel = new JLabel("联系电话");
        phoneLabel.setBounds(100, 150, 90, 50);
        JTextField phoneField=new JTextField(30);
        phoneField.setBounds(160, 160, 250, 30);
        dialog.add(phoneLabel);
        dialog.add(phoneField);

        JLabel createDateLabel = new JLabel("创建时间");
        createDateLabel.setBounds(100, 200, 90, 50);
        JTextField createDateField=new JTextField(30);
        createDateField.setBounds(160, 210, 250, 30);
        dialog.add(createDateLabel);
        dialog.add(createDateField);


        JButton cancelBtn = new JButton("取消");
        cancelBtn.setBounds(160, 370, 80, 30);
        JButton saveBtn = new JButton("保存");
        saveBtn.setBounds(280, 370,  80, 30);
        dialog.add(cancelBtn);
        dialog.add(saveBtn);

        if(null != oldUser){
            idTextField.setText(oldUser.getId().toString());
            nameField.setText(oldUser.getName());

            genderField.setText(oldUser.getGender());
            if(null != oldUser.getAge()){
                ageField.setText(oldUser.getAge().toString());
            }
            phoneField.setText(oldUser.getPhone());
            createDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(oldUser.getCreateDate()));

        }


        // 保存按钮
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                TbUser newUser = new TbUser();
                //user....

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
                if(null != newUser.getAge()){
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
                }else{
                    //增加
                    DaoFactory.getUserDao().insert(newUser);
                }

                dialog.dispose();
                JOptionPane.showMessageDialog(new JPanel(),"操作成功","提示",JOptionPane.PLAIN_MESSAGE);

                searchBtn.doClick();
            }
        });

        // 取消按钮
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        ModalFrameUtil.showAsModal(dialog,jf);
        // 显示对话框
        //这个只能调用一次，不然会删两次才能删掉
        //dialog.setVisible(true);
        dialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


}
