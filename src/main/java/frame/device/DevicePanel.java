package frame.device;


import bean.TbDevice;
import controller.ExcelUtil;
import dao.DaoFactory;
import helper.DeviceExportHelper;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import table.device.DeviceTable;
import uitl.JFileChooserUtil;
import uitl.ModalFrameUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.*;

import static java.util.regex.Pattern.*;

public class DevicePanel extends JPanel {
    private JButton searchBtn, resetBtn, addBtn,btnImport,btnExport, btnEdit,prePage, nextPage, lastPage;
    private JPanel conditPanel, pagePanel;
    private JLabel devNameLabel, devTypeNumLabel, devCodeLabel, pageInfo;
    private JTextField deviceNameField = null, deviceTypeNumField = null,
            deviceCodeField = null;
    private JScrollPane jsp;

    DeviceTable table = null;

    public JFrame jf = null ;

    public DevicePanel(JFrame jFrame) {
        jf = jFrame;
        table = new DeviceTable(jf);
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

        btnImport = new JButton("导入");
        btnExport = new JButton("导出");
        btnEdit = new JButton("编辑");


        prePage = new JButton("上一页");
        nextPage = new JButton("下一页");
        lastPage = new JButton("末  页");
        devNameLabel = new JLabel("设备名称");
        deviceNameField = new JTextField(15);
        devTypeNumLabel = new JLabel("设备型号");
        deviceTypeNumField = new JTextField(15);
        devCodeLabel = new JLabel("设备编码");
        deviceCodeField = new JTextField(15);

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
        gridBagLayout.setConstraints(devNameLabel, gridBagConstraints);
        //组件2
        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridwidth=3;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(deviceNameField, gridBagConstraints);

        gridBagConstraints.gridx=4;
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(devCodeLabel, gridBagConstraints);

        gridBagConstraints.gridx=5;
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridwidth=3;
        gridBagConstraints.gridheight=1;
        gridBagConstraints.insets = new Insets(0, 0, 0, 10);
        gridBagLayout.setConstraints(deviceCodeField, gridBagConstraints);

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

        gridBagConstraints.gridx=4;
        gridBagConstraints.gridy=1;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(btnImport, gridBagConstraints);

        gridBagConstraints.gridx=5;
        gridBagConstraints.gridy=1;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(btnExport, gridBagConstraints);


        conditPanel.add(devNameLabel);
        conditPanel.add(deviceNameField);
        conditPanel.add(devCodeLabel);
        conditPanel.add(deviceCodeField);

        conditPanel.add(searchBtn);
        conditPanel.add(resetBtn);

        conditPanel.add(addBtn);
        conditPanel.add(btnEdit);
        conditPanel.add(btnImport);
        conditPanel.add(btnExport);


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
        deviceNameField.setText("");
        deviceTypeNumField.setText("");
        deviceCodeField.setText("");
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

        // 查询事件
        // 增加回车事件
        //this.getRootPane().setDefaultButton(searchBtn);// 获取焦点
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchData();
            }
        });

        /*table.addMouseListener(new MouseAdapter() {
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
        });*/

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

    /**
     * 显示一个自定义的对话框
     *
     * @param owner 对话框的拥有者
     * @param parentComponent 对话框的父级组件
     */
    private void showCustomDialog(Frame owner, Component parentComponent,TbDevice oldDevice) {
        // 创建一个模态对话框
        final JFrame dialog = new JFrame("设备信息");
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
        jNameTextField.setBounds(160, 10, 250, 30);
        dialog.add(jNameLabel);
        dialog.add(jNameTextField);

        JLabel jTypeNumLabel = new JLabel("设备型号");
        jTypeNumLabel.setBounds(100, 50, 90, 50);
        JTextField jTypeNumTextField=new JTextField(30);
        jTypeNumTextField.setBounds(160, 60, 250, 30);
        dialog.add(jTypeNumLabel);
        dialog.add(jTypeNumTextField);

        JLabel jCodeLabel = new JLabel("设备编码");
        jCodeLabel.setBounds(100, 100, 90, 50);
        JTextField jCodeTextField=new JTextField(30);
        jCodeTextField.setBounds(160, 110, 250, 30);
        dialog.add(jCodeLabel);
        dialog.add(jCodeTextField);

        JLabel countLabel = new JLabel("库存数量");
        countLabel.setBounds(100, 150, 90, 50);
        JTextField countTextField=new JTextField(30);
        countTextField.setBounds(160, 160, 250, 30);
        dialog.add(countLabel);
        dialog.add(countTextField);

        JLabel jPositionLabel = new JLabel("存放位置");
        jPositionLabel.setBounds(100, 200, 90, 50);
        JTextField jPositionTextField=new JTextField(30);
        jPositionTextField.setBounds(160, 210, 250, 30);
        dialog.add(jPositionLabel);
        dialog.add(jPositionTextField);

        JLabel jFeaturesLabel = new JLabel("设备功能");
        jFeaturesLabel.setBounds(100, 250, 90, 50);
        JTextField jfeaturesTextField=new JTextField(30);
        jfeaturesTextField.setBounds(160, 260, 250, 30);
        dialog.add(jFeaturesLabel);
        dialog.add(jfeaturesTextField);

        JLabel imageLabel = new JLabel("设备图片");
        imageLabel.setBounds(100, 300, 90, 50);
        JButton imageBtn  = new JButton("请选择图片...");
        imageBtn.setBounds(160, 310, 100, 30);
        dialog.add(imageLabel);
        dialog.add(imageBtn);

        ImagePanel iPanel = new ImagePanel();
        iPanel.setBounds(280, 310, 100, 90);
        //iPanel.setVisible(false);
        dialog.add(iPanel);

        JButton cancelBtn = new JButton("取消");
        cancelBtn.setBounds(160, 420, 80, 30);
        JButton saveBtn = new JButton("保存");
        saveBtn.setBounds(280, 420,  80, 30);
        dialog.add(cancelBtn);
        dialog.add(saveBtn);

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
                    JFrame imageFrame = new JFrame();

                    // 设置对话框的宽高
                    imageFrame.setSize(550, 450);
                    imageFrame.setLocationRelativeTo(jf);
                    imageFrame.toFront();

                    JScrollImagePanel jScrollImagePanel = new JScrollImagePanel(iPanel.getImagePath());
                    JScrollPane scrollPane=new JScrollPane();
                    scrollPane.setViewportView(jScrollImagePanel);

                    //dialog.setSize(jScrollImagePanel.getWidth(), jScrollImagePanel.getHeight());
                    imageFrame.add(scrollPane,BorderLayout.CENTER);
                    //imageFrame.setVisible(true);
                    ModalFrameUtil.showAsModal(imageFrame,dialog);

                    imageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }

            }

        });


        // 选择图片
        imageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String[] picSufix = new String[]{".jpg",".jpeg",".png"};

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


                if(!isNumeric(countTextField.getText())){
                    JOptionPane.showMessageDialog(new JPanel(),"库存数量请输入正整数","提示",1);
                    return;
                }
                if(StringUtils.isNotBlank(countTextField.getText())){
                    newDevice.setCount(Integer.valueOf(countTextField.getText()));
                }

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
                if(null == newDevice.getCount()){
                    JOptionPane.showMessageDialog(new JPanel(),"请填写设备库存数量","提示",1);
                    return;
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

    public boolean isNumeric(String string){
        Pattern pattern = compile("[0-9]*");
        return pattern.matcher(string).matches();
    }

    private void doImportExcelAction(String[] sufixArr) {
        File selectedFile = JFileChooserUtil.getSelectedOpenFile(sufixArr,jf);
        if (selectedFile != null) {
            // String name=selectedFile.getName();
            List<TbDevice> deviceList = DeviceExportHelper.getDeviceData(selectedFile.getPath());
            DaoFactory.getDeviceDao().insertBatch(deviceList);
        }
    }
    private void doExportExcelAction() {
        String fileName = "设备表格-"+new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File selectedFile = JFileChooserUtil.getSelectedFile(fileName,".xlsx",jf);
        if (selectedFile != null) {
            String path = selectedFile.getPath();
            ToExcel(path);
        }
    }

    public void ToExcel(String path) {

        List<TbDevice> list = DaoFactory.getDeviceDao().findAll();

       /* ExcelUtil.writeArrayToExcel(wb, sheet, list.size() + 1, 7, value);*/
        XSSFWorkbook wb = ExcelUtil.getWorkBook(list,path);

        ExcelUtil.writeWorkbook(wb, path);

    }




    public static void main(String[] args){
        //设置样式
        //CommonUtil.setlookandfeel();
       // DevicePanel devicePanel = new DevicePanel();
    }
}
