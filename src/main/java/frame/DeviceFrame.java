package frame;


import bean.TbDevice;
import controller.ExcelUtil;
import dao.DaoFactory;
import helper.DeviceExportHelper;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import table.TableBase;
import uitl.CommonUtil;
import uitl.JFileChooserUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.*;

public class DeviceFrame extends JFrame {
    private JButton btn1, btn2, btn3,btnImport,btnExport, btnEdit,prePage, nextPage, lastPage;
    private JPanel mainPanel, conditPanel, pagePanel;
    private JLabel jname1, jname2, jname3, pageInfo;
    private JTextField deviceNameField = null, deviceTypeNumField = null,
            deviceCodeField = null;
    private JScrollPane jsp;
    private JMenuBar menubar;
    private JMenu fileMenu;
    private JMenuItem imput, export,exit;

    //JExcelImpl excel=new JExcelImpl();
    TableBase table = null;

    public  static JFrame jf = null ;

    public DeviceFrame() {
        jf = this;
        table = new TableBase();
        // 初始化所有控件
        initComponent();
        // 构造函数中调用initUI来向窗口中添加控件
        initUI();
        // 初始化操作函数
        initOperator();
        //初始化数据
        //showAllData();
        // 设置主窗体大小
        this.setPreferredSize(new Dimension(750, 600));
        // 设置主窗体显示在屏幕的位置
        this.setLocation(280, 50);
        // 设置是否显示
        this.setVisible(true);
        this.pack();
    }

    private void initComponent() {
        mainPanel = new JPanel();
        conditPanel = new JPanel();
        pagePanel = new JPanel();
        menubar = new JMenuBar();
        fileMenu = new JMenu("文件");
        imput = new JMenuItem("导入");
        export=new JMenuItem("导出");
        exit = new JMenuItem("退出");
        btn1 = new JButton("查询");
        btn2 = new JButton("重置");
        btn3 = new JButton("新增");
        btnImport = new JButton("导入");
        btnExport = new JButton("导出");
        btnEdit = new JButton("编辑");


        prePage = new JButton("上一页");
        nextPage = new JButton("下一页");
        lastPage = new JButton("末  页");
        jname1 = new JLabel("设备名称");
        deviceNameField = new JTextField(13);
        jname2 = new JLabel("设备型号");
        deviceTypeNumField = new JTextField(13);
        jname3 = new JLabel("设备编码");
        deviceCodeField = new JTextField(13);

        jsp = new JScrollPane(table);
        // 设置菜单
        setJMenuBar(menubar);
        menubar.add(fileMenu);
        fileMenu.add(imput);
        fileMenu.addSeparator();
        fileMenu.add(export);
        fileMenu.addSeparator();
        fileMenu.add(exit);
    }

    private void initUI() {
        // 主界面标题
        this.setTitle("竞品查询");
        this.setIconImage(new ImageIcon("images/logo.png").getImage());
        conditPanel.setBorder(BorderFactory.createTitledBorder("查询条件"));
        // 条件窗口控制条件
        conditPanel.setLayout(new GridBagLayout());
        GridBagConstraints s = new GridBagConstraints();
        // 设置设备名称label
        s.gridx = 1;
        s.gridy = 0;
        s.weightx = 0;
        s.weighty = 0;
        s.anchor = GridBagConstraints.EAST;
        conditPanel.add(jname1, s);
        // 设置设备名称输入框
        s.gridx = 2;
        s.gridy = 0;
        s.weightx = 1;
        s.anchor = GridBagConstraints.WEST;
        conditPanel.add(deviceNameField, s);


        // 设置设备类型label
        s.gridx = 3;
        s.gridy = 0;
        s.weightx = 1;
        s.anchor = GridBagConstraints.EAST;
        conditPanel.add(jname2, s);
        // 设置设备类型输入框
        s.gridx = 4;
        s.gridy = 0;
        s.weightx = 1;
        s.anchor = GridBagConstraints.WEST;
        conditPanel.add(deviceTypeNumField, s);

        // 设置设备编码label
        s.gridx = 5;
        s.gridy = 0;
        s.weightx = 1;
        s.anchor = GridBagConstraints.EAST;
        conditPanel.add(jname3, s);

        s.gridx = 6;
        s.gridy = 0;
        s.weightx = 1;
        s.anchor = GridBagConstraints.WEST;
        conditPanel.add(deviceCodeField, s);

        // 设置查询、重置按钮
        s.gridx = 1;
        s.gridy = 3;
        s.anchor = GridBagConstraints.EAST;
        conditPanel.add(btn1, s);

        s.gridx = 2;
        s.gridy = 3;
        s.anchor = GridBagConstraints.WEST;
        s.insets = new Insets(0, 40, 0, 0);
        conditPanel.add(btn2, s);

        s.gridx = 3;
        s.gridy = 3;
        //s.anchor = GridBagConstraints.WEST;
        //s.insets = new Insets(0, 40, 0, 0);
        conditPanel.add(btn3, s);

        s.gridx = 4;
        s.gridy = 3;
        //s.anchor = GridBagConstraints.WEST;
        //s.insets = new Insets(0, 40, 0, 0);
        conditPanel.add(btnImport, s);

        s.gridx = 5;
        s.gridy = 3;
        //s.anchor = GridBagConstraints.WEST;
        //s.insets = new Insets(0, 40, 0, 0);
        conditPanel.add(btnExport, s);

        s.gridx = 6;
        s.gridy = 3;
        //s.anchor = GridBagConstraints.WEST;
        //s.insets = new Insets(0, 40, 0, 0);
        conditPanel.add(btnEdit, s);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(conditPanel, BorderLayout.NORTH);

        // 设置查询结果标题
        jsp.setBorder(BorderFactory.createTitledBorder("查询结果"));
        // 添加结果表格
        mainPanel.add(jsp, BorderLayout.CENTER);
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
        mainPanel.add(pagePanel, BorderLayout.SOUTH);

        this.getContentPane().add(mainPanel);
    }

    // 重置文本输入框
    private void clear() {
        deviceNameField.setText("");
        deviceTypeNumField.setText("");
        deviceCodeField.setText("");
    }

    // 退出窗口
    private void exitWindow() {
        JOptionPane.showMessageDialog(null, "程序即将退出...");
        System.exit(0);
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

        TbDevice device = new TbDevice(deviceNameField.getText(),deviceTypeNumField.getText(),deviceCodeField.getText());

        //设置记录总数
        table.setTotalRowCount((int) DaoFactory.getDeviceDao().countAllByCondition(device));
        //结果集的总页数
        table.setTotalPage(table.getTotalRowCount() % table.getPageCount() == 0
                ? table.getTotalRowCount()/  table.getPageCount() : table.getTotalRowCount() / table.getPageCount() + 1);


        List<TbDevice> deviceList  = DaoFactory.getDeviceDao().findByConditionPage(device,table.getCurrentPage(),table.getPageCount());
        setPageInfo();
        table.showTable(deviceList);
    }


    private void initOperator() {
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitWindow();
            }
        });
        // 查询事件
        // 增加回车事件
        this.getRootPane().setDefaultButton(btn1);// 获取焦点
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchData();
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me)) {
                    final int row = table.rowAtPoint(me.getPoint());
                    System.out.println("row:" + row);
                    if (row != -1) {
                        final int column = table.columnAtPoint(me.getPoint());

                        final JPopupMenu popup = new JPopupMenu();
                        JMenuItem select = new JMenuItem("选择");

                        select.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                System.out.println("选择");
                                table.setRowSelectionInterval(row, row); // 高亮选择指定的行
                            }
                        });
                        popup.add(select);
                        popup.add(new JSeparator());
                        JMenuItem edit = new JMenuItem("编辑");
                        popup.add(edit);
                        edit.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                System.out.println("编辑");
                                table.clearSelection(); // 清除高亮选择状态
                                //table.editCellAt(row, column); // 设置某列为可编辑
                                table.isCellEditable(row, column);
                            }
                        });
                        JMenuItem calcel = new JMenuItem("取消");
                        calcel.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                System.out.println("取消");
                                popup.setVisible(false);
                            }
                        });
                        popup.add(new JSeparator());
                        popup.add(calcel);
                        popup.show(me.getComponent(), me.getX(), me.getY());
                    }
                }
            }
        });

        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        //新增
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCustomDialog(jf,jf,null);
                //add1();
            }
        });

        //导入
        btnImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doImportExcelAction(new String[]{".xls",".xlsx"});
            }
        });

        //导出
        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doExportExcelAction();
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

        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*if(excel.openFile("export")){
                    try {
                        DefaultTableModel excelmodel = new DefaultTableModel(TableBase.resultData, TableBase.columnNames);
                        JTable exceltable = new JTable(excelmodel);
                        exceltable.setModel(excelmodel);
                        excel.exportTable(exceltable, new File(excel.getRealPath()));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }*/
            }
        });

        imput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*if(excel.openFile("imput")){
                    List<?> list=excel.jxlExlToList(new File(excel.getRealPath()));
                    if(dao.save(list, "products")){
                        JOptionPane.showMessageDialog(null, "数据导入成功！");
                    }
                }*/
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
        TbDevice device = DaoFactory.getDeviceDao().queryById(Integer.valueOf(id));
        if(null == device){
            JOptionPane.showMessageDialog(jf,"此记录不存在","警告",2);
            return;
        }
        showCustomDialog(jf,jf,device);

    }

    private void setPageInfo() {
        pageInfo.setText("总共 " + table.getTotalRowCount() + " 条记录|当前第 "
                + table.getCurrentPage() + " 页|" + "总共 " + table.getTotalPage() + " 页");
    }

    public void add1() {
        Font f1 = new Font("楷体", Font.BOLD, 19);
        Font f2 = new Font("楷体", Font.BOLD, 16);

        JFrame frame = new JFrame("请输入要添加的教师信息");
        JLabel t0 = new JLabel("编号");
        JLabel t1 = new JLabel("姓名");
        JLabel t2 = new JLabel("身份证号");
        JLabel t3 = new JLabel("地址");
        JLabel t4 = new JLabel("电话");
        JLabel t5 = new JLabel("部门");
        JLabel t6 = new JLabel("工资");
        JLabel t7 = new JLabel("参加工作时间");
        JLabel t71 = new JLabel("年");
        JLabel t8 = new JLabel("专业");
        JLabel t9 = new JLabel("职务");
        JLabel t10 = new JLabel("备注");
        JTextField tid = new JTextField(30);
        JTextField name = new JTextField(30);
        JTextField id = new JTextField(30);
        JTextField address = new JTextField(30);
        JTextField phone = new JTextField(30);
        JTextField department = new JTextField(50);
        JTextField salary = new JTextField(50);
        JTextField worktime = new JTextField(50);
        JTextField profess = new JTextField(50);
        JTextField duty = new JTextField(50);
        JTextField remark = new JTextField(50);

        JButton e1 = new JButton("确定");
        JButton e2 = new JButton("取消");
        frame.setLayout(null);
        t0.setBounds(100, 0, 90, 50);
        t0.setFont(f2);
        t1.setBounds(100, 50, 90, 50);
        t1.setFont(f2);
        t2.setBounds(70, 100, 90, 50);
        t2.setFont(f2);
        t3.setBounds(100, 150, 90, 50);
        t3.setFont(f2);
        t4.setBounds(100, 200, 90, 50);
        t4.setFont(f2);
        t5.setBounds(100, 250, 90, 50);
        t5.setFont(f2);
        t6.setBounds(100, 300, 90, 50);
        t6.setFont(f2);
        t7.setBounds(30, 350, 130, 50);
        t7.setFont(f2);
        t71.setBounds(400, 350, 130, 50);
        t7.setFont(f2);
        t8.setBounds(100, 400, 90, 50);
        t8.setFont(f2);
        t9.setBounds(100, 450, 90, 50);
        t9.setFont(f2);
        t10.setBounds(100, 500, 90, 50);
        t10.setFont(f2);
        tid.setBounds(150, 10, 250, 30);
        name.setBounds(150, 60, 250, 30);
        id.setBounds(150, 110, 250, 30);
        address.setBounds(150, 160, 250, 30);
        phone.setBounds(150, 210, 250, 30);
        department.setBounds(150, 260, 250, 30);
        salary.setBounds(150, 310, 250, 30);
        worktime.setBounds(150, 360, 250, 30);
        profess.setBounds(150, 410, 250, 30);
        duty.setBounds(150, 460, 250, 30);
        remark.setBounds(150, 510, 250, 30);
        e1.setBounds(60, 560, 80, 30);
        e1.setFont(f2);
        e2.setBounds(300, 560, 80, 30);
        e2.setFont(f2);
        frame.add(t0);
        frame.add(tid);
        frame.add(t1);
        frame.add(name);
        frame.add(t2);
        frame.add(id);
        frame.add(t3);
        frame.add(address);
        frame.add(t4);
        frame.add(phone);
        frame.add(t5);
        frame.add(department);
        frame.add(t6);
        frame.add(salary);
        frame.add(t7);
        frame.add(t71);
        frame.add(worktime);

        frame.add(t8);
        frame.add(profess);
        frame.add(t9);
        frame.add(duty);
        frame.add(t10);
        frame.add(remark);
        frame.add(e1);
        frame.add(e2);
        frame.setLocation(500, 110);
        frame.setSize(500, 650);
        frame.setVisible(true);
    }

    /**
     * 显示一个自定义的对话框
     *
     * @param owner 对话框的拥有者
     * @param parentComponent 对话框的父级组件
     */
    private void showCustomDialog(Frame owner, Component parentComponent,TbDevice oldDevice) {
        // 创建一个模态对话框
        final JDialog dialog = new JDialog(owner, "设备信息", true);
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


        JLabel jNameLabel = new JLabel("设备名称");
        jNameLabel.setBounds(100, 0, 90, 50);
        JTextField jNameTextField=new JTextField(30);
        jNameTextField.setBounds(150, 10, 250, 30);
        dialog.add(jNameLabel);
        dialog.add(jNameTextField);

        JLabel jTypeNumLabel = new JLabel("设备型号");
        jTypeNumLabel.setBounds(100, 50, 90, 50);
        JTextField jTypeNumTextField=new JTextField(30);
        jTypeNumTextField.setBounds(150, 60, 250, 30);
        dialog.add(jTypeNumLabel);
        dialog.add(jTypeNumTextField);

        JLabel jCodeLabel = new JLabel("设备编码");
        jCodeLabel.setBounds(100, 100, 90, 50);
        JTextField jCodeTextField=new JTextField(30);
        jCodeTextField.setBounds(150, 110, 250, 30);
        dialog.add(jCodeLabel);
        dialog.add(jCodeTextField);

        JLabel jPositionLabel = new JLabel("存放位置");
        jPositionLabel.setBounds(100, 150, 90, 50);
        JTextField jPositionTextField=new JTextField(30);
        jPositionTextField.setBounds(150, 160, 250, 30);
        dialog.add(jPositionLabel);
        dialog.add(jPositionTextField);

        JLabel jFeaturesLabel = new JLabel("设备功能");
        jFeaturesLabel.setBounds(100, 200, 90, 50);
        JTextField jfeaturesTextField=new JTextField(30);
        jfeaturesTextField.setBounds(150, 210, 250, 30);
        dialog.add(jFeaturesLabel);
        dialog.add(jfeaturesTextField);

        JLabel imageLabel = new JLabel("设备图片");
        imageLabel.setBounds(100, 250, 90, 50);
        JButton imageBtn  = new JButton("请选择图片...");
        imageBtn.setBounds(150, 260, 100, 30);
        dialog.add(imageLabel);
        dialog.add(imageBtn);

        ImagePanel iPanel = new ImagePanel();
        iPanel.setBounds(290, 260, 120, 90);
        iPanel.setVisible(false);
        dialog.add(iPanel);

        JButton cancelBtn = new JButton("取消");
        cancelBtn.setBounds(130, 370, 80, 30);
        JButton saveBtn = new JButton("保存");
        saveBtn.setBounds(300, 370,  80, 30);
        dialog.add(cancelBtn);
        dialog.add(saveBtn);

        if(null != oldDevice){
            idTextField.setText(oldDevice.getImage());
            jNameTextField.setText(oldDevice.getName());
            jTypeNumTextField.setText(oldDevice.getTypeNum());

            jCodeTextField.setText(oldDevice.getCode());
            jPositionTextField.setText(oldDevice.getSavePosition());
            if(StringUtils.isNotBlank(oldDevice.getImage())){
                iPanel.setImagePath(oldDevice.getImage());
                iPanel.repaint();
                iPanel.setVisible(true);
            }
            jfeaturesTextField.setText(oldDevice.getFeatures());
        }

        iPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 创建一个模态对话框
               // final JDialog dialog = new JDialog(owner, "图片", true);

                final JDialog dialog = new JDialog(owner, "图片", true);

                // 设置对话框的宽高
                //dialog.setSize(400, 400);
                dialog.setSize(500, 400);
                // 设置对话框大小不可改变
               // dialog.setResizable(false);
                // 设置对话框相对显示的位置
                dialog.setLocationRelativeTo(parentComponent);
                //dialog.setLayout(null);

                JScrollPane scrollPane=new JScrollPane();
                scrollPane.setViewportView(new JScrollImagePanel(iPanel.getImagePath()));

                dialog.add(scrollPane,BorderLayout.CENTER);
                dialog.setVisible(true);

            }
        });


        // 选择图片
        imageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String[] picSufix = new String[]{".bmp",".jpg",".png",".tif",".gif",".pcx",".tga",".exif",".fpx",".svg",
                        ".psd",".cdr",".pcd",".dxf",".ufo",".eps",".ai",".raw",".WMF",".w!ebp"};

                File selectedFile = JFileChooserUtil.getSelectedOpenFile(picSufix,jf);
                if(null != selectedFile){
                    iPanel.setImagePath(selectedFile.getPath());
                    iPanel.repaint();
                    iPanel.setVisible(true);
                }
            }
        });


        // 保存按钮
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
                if(StringUtils.isNotEmpty(newDevice.getImage())){
                    newDevice.setImage(JFileChooserUtil.writeImgToUpload(new File(newDevice.getImage())));
                }

                if(null != newDevice.getId()){
                    //编辑更新
                    DaoFactory.getDeviceDao().update(newDevice);
                }else{
                    //增加
                    DaoFactory.getDeviceDao().insert(newDevice);
                }


                dialog.dispose();
                JOptionPane.showMessageDialog(new JPanel(),"操作成功","提示",JOptionPane.PLAIN_MESSAGE);

                btn1.doClick();
            }
        });

        // 取消按钮
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        // 添加组件到面板
        /*panel.add(namePane);
        panel.add(typePane);
        panel.add(codePane);
        panel.add(positionPane);
        panel.add(featuresPane);
        panel.add(imagePane);*/
        //panel.add(buttonPane);
        /*dialog.add(panel,BorderLayout.CENTER);
        dialog.add(buttonPane,BorderLayout.SOUTH);*/

        // 设置对话框的内容面板
        //dialog.setContentPane(panel);

        // 显示对话框
        dialog.setVisible(true);
    }

    private void doImportExcelAction(String[] sufixArr) {
        File selectedFile = JFileChooserUtil.getSelectedOpenFile(sufixArr,this);
        if (selectedFile != null) {
            // String name=selectedFile.getName();
            String path = selectedFile.getPath();

            List<TbDevice> deviceList = DeviceExportHelper.getDeviceData(selectedFile.getPath());
            DaoFactory.getDeviceDao().insertBatch(deviceList);
            //System.out.println(path);
            //FromExcel(path);
            //fillTable(userdao.findAll());

        }
    }

    private void doExportExcelAction() {
        File selectedFile = JFileChooserUtil.getSelectedFile(".xls",this);
        if (selectedFile != null) {
            String path = selectedFile.getPath();

            // System.out.println(path);
            ToExcel(path);
        }
    }

    public void ToExcel(String path) {

        List<TbDevice> list = DaoFactory.getDeviceDao().findByPage(10,5);

       /* HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Users");

        String[] n = { "序号", "名称", "型号", "管理编码",
                "存放位置", "图片", "功用"};

        Object[][] value = new Object[list.size() + 1][7];

        for (int i = 0; i < list.size(); i++) {
            TbDevice device = list.get(i);

            value[i + 1][0] = device.getId();
            value[i + 1][1] = device.getName();
            value[i + 1][2] = device.getTypeNum();
            value[i + 1][3] = device.getCode();
            value[i + 1][4] = device.getSavePosition();
            value[i + 1][5] = device.getImage();
            value[i + 1][6] = device.getFeatures();

        }
        ExcelUtil.writeArrayToExcel(wb, sheet, list.size() + 1, 7, value);*/

        XSSFWorkbook wb = ExcelUtil.getWorkBook(list,path);

        ExcelUtil.writeWorkbook(wb, path);

    }

    public static void main(String[] args){
        //设置样式
        CommonUtil.setlookandfeel();
        DeviceFrame deviceFrame = new DeviceFrame();
    }
}
