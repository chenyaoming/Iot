package frame.device;


import bean.TbDevice;
import controller.ExcelUtil;
import dao.DaoFactory;
import frame.FrameUtil;
import frame.PanelOperation;
import helper.DeviceExportHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import progress.BaseProgress;
import table.device.DeviceTable;
import uitl.JFileChooserUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;

public class DevicePanel extends JPanel implements PanelOperation {
    private JButton searchBtn, resetBtn, addBtn,btnImport,btnExport, btnEdit,prePage, nextPage, lastPage;
    private JPanel conditPanel, pagePanel;
    private JLabel devNameLabel, devTypeNumLabel, devCodeLabel, pageInfo;
    private JTextField deviceNameField = null, deviceTypeNumField = null,
            deviceCodeField = null;
    private JScrollPane jsp;

    DeviceTable table = null;

    public DevicePanel() {
        table = new DeviceTable();
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
        new BaseProgress(FrameUtil.currentFrame,"正在查询..."){
            @Override
            public void invokeBusiness() {
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
        }.doAsynWork();
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
        addBtn.addActionListener(e -> {
            showCustomDialog(null);
            //add1();
        });

        //导入
        btnImport.addActionListener(e -> doImportExcelAction(new String[]{".xls",".xlsx"}));

        //导出
        btnExport.addActionListener(e -> doExportExcelAction());

        //编辑
        btnEdit.addActionListener(e -> doEditAction());



        // 上一页
        prePage.addActionListener(e -> {
            table.getPreviousPage();
            //查询数据并且设置分页bar信息
            selectDataAndSetPageInfo();
        });
        // 下一页
        nextPage.addActionListener(e -> {
            int i = table.getNextPage();
            if (i == 1) {
                return;
            } else {
                //查询数据并且设置分页bar信息
                selectDataAndSetPageInfo();
            }
        });
        // 末 页
        lastPage.addActionListener(e -> {
            table.getLastPage();
            //查询数据并且设置分页bar信息
            selectDataAndSetPageInfo();
        });


    }

    private void doEditAction() {
        if(table.getSelectedRow() < 0){
            JOptionPane.showMessageDialog(FrameUtil.currentFrame,"请选择一条记录编辑","提示",1);
            return;
        }
        String id = table.getValueAt(table.getSelectedRow(), 0).toString();
        if(StringUtils.isBlank(id)){
            JOptionPane.showMessageDialog(FrameUtil.currentFrame,"此记录不存在","警告",2);
            return;
        }
        TbDevice device = DaoFactory.getDeviceDao().queryById(Integer.valueOf(id));
        if(null == device){
            JOptionPane.showMessageDialog(FrameUtil.currentFrame,"此记录不存在","警告",2);
            return;
        }
        showCustomDialog(device);

    }

    private void setPageInfo() {
        pageInfo.setText("总共 " + table.getTotalRowCount() + " 条记录|当前第 "
                + table.getCurrentPage() + " 页|" + "总共 " + table.getTotalPage() + " 页");
    }

    /**
     * 显示一个自定义的对话框
     *
     */
    private void showCustomDialog(TbDevice oldDevice) {
       new DeviceAddDialog(oldDevice).showDialog();
    }


    private void doImportExcelAction(String[] sufixArr) {
        File selectedFile = JFileChooserUtil.getSelectedOpenFile(sufixArr,FrameUtil.currentFrame);
        if (selectedFile != null) {
            new BaseProgress(FrameUtil.currentFrame,"正在导入..."){
                @Override
                public void invokeBusiness() {
                    // String name=selectedFile.getName();
                    List<TbDevice> deviceList = DeviceExportHelper.getDeviceData(selectedFile.getPath());
                    DaoFactory.getDeviceDao().insertBatch(deviceList);
                }
            }.doAsynWork();
        }
    }
    private void doExportExcelAction() {
        String fileName = "设备表格-"+new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File selectedFile = JFileChooserUtil.getSelectedFile(fileName,".xlsx",FrameUtil.currentFrame);
        if (selectedFile != null) {

            new BaseProgress(FrameUtil.currentFrame,"正在导出..."){
                @Override
                public void invokeBusiness() {
                    String path = selectedFile.getPath();
                    ToExcel(path);
                }
            }.doAsynWork();
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

    @Override
    public JButton getSearchButton() {
        return this.searchBtn;
    }
}
