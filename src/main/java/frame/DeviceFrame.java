package frame;


import bean.TbDevice;
import dao.DaoFactory;
import dao.TbDeviceDao;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import table.TableBase;
import uitl.CommonUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DeviceFrame extends JFrame {
    private JButton btn1, btn2, btn3, prePage, nextPage, lastPage;
    private JPanel mainPanel, conditPanel, pagePanel;
    private JLabel jname1, jname2, jname3, pageInfo;
    private JTextField deviceNameField = null, deviceTypeNumField = null,
            deviceCodeField = null;
    private JScrollPane jsp;
    private JMenuBar menubar;
    private JMenu fileMenu;
    private JMenuItem imput, export,exit;

    TbDeviceDao dao = DaoFactory.getDeviceDao();

    //SQLiteDBImpl dao = new SQLiteDBImpl();
    //JExcelImpl excel=new JExcelImpl();
    TableBase table = null;

    JFrame jf = null;

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
        this.setPreferredSize(new Dimension(750, 380));
        // 设置主窗体显示在屏幕的位置
        this.setLocation(280, 100);
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


      /*  String proName = deviceNameField.getText();
        String vendorName = deviceTypeNumField.getText();
        String proType = deviceCodeField.getText();
        if ((proName != null && !proName.equals(""))
                || (vendorName != null && !vendorName.equals(""))
                || (proType != null && !proType.equals(""))) {
            //TableBase.bigList.clear();
            clearTable();

            //查询要展示的记录
            //List<TbDevice> deviceList = DaoFactory.getDeviceDao().findByPage(table.getCurrentPage(),table.getPageCount());


            //table.setRestCount(dao.countAll());

            //TableBase.bigList = dao.queryProInfo(proName, vendorName, proType);
            //table.initTable();

            //查询数据并且设置分页bar信息
            selectDataAndSetPageInfo();

            *//*if (0 == table.getTotalRowCount()) {
                clearTable();
                //table.initResultData(null);
                pageInfo.setText("总共 " + table.getTotalRowCount() + " 条记录|当前第 "
                        + table.getCurrentPage() + " 页|" + "总共 " + table.getTotalPage() + " 页");
                JOptionPane.showMessageDialog(null,
                        "<html> <font face= '宋体 ' size= '5'> <b>不存在所查询信息，请重新输入！</b> </font> </html> ");
                return;
            } else {
                //查询数据并且设置分页bar信息
                selectDataAndSetPageInfo();
            }*//*
        } else {
            JOptionPane.showMessageDialog(null, "请输入查询条件!", "提示消息",
                    JOptionPane.WARNING_MESSAGE);
        }*/
    }

    /**
     * 查询数据并且设置分页bar信息
     */
    private void selectDataAndSetPageInfo() {

        clearTable();
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


    // 清空表数据
    private void clearTable() {
        TableBase.model.fireTableDataChanged();
        table.clearTable();
        //table.initTable();
        //Product.products.clear();
        //showlist.clear();
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
                showCustomDialog(jf,jf);
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
        // 表格单元格内容气泡悬浮显示
        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row > -1 && col > -1) {
                    Object value = table.getValueAt(row, col);
                    if (null != value && !"".equals(value)) {
                        table.setToolTipText(value.toString());// 悬浮显示单元格内容
                    } else {
                        table.setToolTipText(null);// 关闭提示
                    }
                }
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
    private static void showCustomDialog(Frame owner, Component parentComponent) {
        // 创建一个模态对话框
        final JDialog dialog = new JDialog(owner, "新增", true);
        // 设置对话框的宽高
        dialog.setSize(400, 400);
        // 设置对话框大小不可改变
        dialog.setResizable(false);
        // 设置对话框相对显示的位置
        dialog.setLocationRelativeTo(parentComponent);

        JPanel namePane = new JPanel();
        JLabel jNameLabel = new JLabel("设备名称");
        JTextField jNameTextField=new JTextField(30);
        namePane.add(jNameLabel);
        namePane.add(jNameTextField);

        JPanel typePane = new JPanel();
        JLabel jTypeNumLabel = new JLabel("设备型号");
        JTextField jTypeNumTextField=new JTextField(30);
        typePane.add(jTypeNumLabel);
        typePane.add(jTypeNumTextField);

        JPanel codePane = new JPanel();
        JLabel jCodeLabel = new JLabel("设备编码");
        JTextField jCodeTextField=new JTextField(30);
        codePane.add(jCodeLabel);
        codePane.add(jCodeTextField);

        JPanel positionPane = new JPanel();
        JLabel jPositionLabel = new JLabel("存放位置");
        JTextField jPositionTextField=new JTextField(30);
        positionPane.add(jPositionLabel);
        positionPane.add(jPositionTextField);

        JPanel featuresPane = new JPanel();
        JLabel jFeaturesLabel = new JLabel("设备功能");
        JTextField jfeaturesTextField=new JTextField(30);
        featuresPane.add(jFeaturesLabel);
        featuresPane.add(jfeaturesTextField);

        JPanel buttonPane = new JPanel();
        JButton cancelBtn = new JButton("取消");
        JButton saveBtn = new JButton("保存");
        buttonPane.add(cancelBtn);
        buttonPane.add(saveBtn);

        // 保存按钮
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                TbDevice device = new TbDevice(jNameTextField.getText(),jTypeNumTextField.getText(),jCodeTextField.getText(),
                        jPositionTextField.getText(),null,jfeaturesTextField.getText());

                if(StringUtils.isBlank(device.getName())){
                    JOptionPane.showMessageDialog(new JPanel(),"请填写设备名称","提示",1);
                    return;
                }
                if(StringUtil.isBlank(device.getTypeNum())){
                    JOptionPane.showMessageDialog(new JPanel(),"请填写设备型号","提示",1);
                    return;
                }
                if(StringUtils.isBlank(device.getCode())){
                    JOptionPane.showMessageDialog(new JPanel(),"请填写设备编码","提示",1);
                    return;
                }

                DaoFactory.getDeviceDao().insert(device);
                dialog.dispose();
                JOptionPane.showMessageDialog(new JPanel(),"操作成功","提示",JOptionPane.PLAIN_MESSAGE);
            }
        });

        // 取消按钮
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });


        // 创建对话框的内容面板, 在面板内可以根据自己的需要添加任何组件并做任意是布局
        JPanel panel = new JPanel();

        // 添加组件到面板
        panel.add(namePane);
        panel.add(typePane);
        panel.add(codePane);
        panel.add(positionPane);
        panel.add(featuresPane);
        //panel.add(buttonPane);
        dialog.add(panel,BorderLayout.CENTER);
        dialog.add(buttonPane,BorderLayout.SOUTH);

        // 设置对话框的内容面板
        //dialog.setContentPane(panel);
        // 显示对话框
        dialog.setVisible(true);




    }

    public static void main(String[] args){
        //设置样式
        CommonUtil.setlookandfeel();
        DeviceFrame deviceFrame = new DeviceFrame();
    }
}
