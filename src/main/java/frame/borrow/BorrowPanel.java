package frame.borrow;

import bean.TbBorrowRecord;
import dao.DaoFactory;
import enums.Status;
import frame.FrameUtil;
import interfaces.BorrowUserNameFieldOperation;
import interfaces.PanelOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.jam.JField;
import progress.BaseProgress;
import progress.MySwingWorker;
import table.borrow.BorrowTable;
import uitl.FingerHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Vector;

public class BorrowPanel extends JPanel implements PanelOperation, BorrowUserNameFieldOperation {
    private JButton searchBtn, resetBtn, borrowBtn, btnEdit,returnBtn,printBtn, fingerSearchBtn, prePage, nextPage, lastPage;
    private JPanel conditPanel, pagePanel;
    private JLabel borrowUserNameLabel, deviceNameLabel,deviceCodeLabel,pageInfo,returnUserNameLabel,statusLabel;
    private JTextField borrowUserNameField = null, deviceNameField = null,deviceCodeField = null, returnUserNameField = null;
    private JComboBox<String> statusComboBox = null;

    private JScrollPane jsp;

    BorrowTable table = null;

    public BorrowPanel() {

        table = new BorrowTable();
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

        borrowBtn = new JButton("出借");
        btnEdit = new JButton("编辑");

        returnBtn = new JButton("归还");
        printBtn = new JButton("打印");

        fingerSearchBtn = new JButton("借用人指纹查询");

        prePage = new JButton("上一页");
        nextPage = new JButton("下一页");
        lastPage = new JButton("末  页");
        borrowUserNameLabel = new JLabel("借用人");
        borrowUserNameField = new JTextField(15);
        deviceNameLabel = new JLabel("设备名称");
        deviceNameField = new JTextField(15);

        deviceCodeLabel = new JLabel("设备编码");
        deviceCodeField = new JTextField(15);

        returnUserNameLabel = new JLabel("归还人");
        returnUserNameField = new JTextField(15);

        statusLabel = new JLabel("出借状态");

        String[] listData = new String[]{"全部", "待归还", "已归还"};
        statusComboBox = new JComboBox<>(listData);

        statusComboBox.setFont(new Font("alias", Font.PLAIN, 12));
        statusComboBox.setPreferredSize(new Dimension(100, 20));

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
        gridBagLayout.setConstraints(deviceCodeLabel, gridBagConstraints);
        //组件2
        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridwidth=3;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(deviceCodeField, gridBagConstraints);


        gridBagConstraints.insets = new Insets(0, 20, 0, 5);
        gridBagConstraints.gridx=4;
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(deviceNameLabel, gridBagConstraints);

        gridBagConstraints.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints.gridx=5;
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridwidth=3;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(deviceNameField, gridBagConstraints);


        gridBagConstraints.insets = new Insets(0, 20, 0, 0);
        gridBagConstraints.gridx=8;
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(borrowUserNameLabel, gridBagConstraints);


        gridBagConstraints.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints.gridx=9;
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridwidth=3;
        gridBagConstraints.gridheight=1;
        //gridBagConstraints.insets = new Insets(0, 0, 0, 10);
        gridBagLayout.setConstraints(borrowUserNameField, gridBagConstraints);


        gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=1;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(returnUserNameLabel, gridBagConstraints);

        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=1;
        gridBagConstraints.gridwidth=3;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(returnUserNameField, gridBagConstraints);


        gridBagConstraints.insets = new Insets(5, 20, 0, 5);
        gridBagConstraints.gridx=4;
        gridBagConstraints.gridy=1;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(statusLabel, gridBagConstraints);

        gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        gridBagConstraints.gridx=5;
        gridBagConstraints.gridy=1;
        gridBagConstraints.gridwidth=2;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(statusComboBox, gridBagConstraints);


        gridBagConstraints.gridx=8;
        gridBagConstraints.gridy=1;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(searchBtn, gridBagConstraints);

        gridBagConstraints.insets = new Insets(5, 10, 0, 0);
        gridBagConstraints.gridx=9;
        gridBagConstraints.gridy=1;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(resetBtn, gridBagConstraints);

        gridBagConstraints.gridx=11;
        gridBagConstraints.gridy=1;
        gridBagConstraints.gridwidth=3;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(fingerSearchBtn, gridBagConstraints);



        gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=2;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(printBtn, gridBagConstraints);


        gridBagConstraints.insets = new Insets(5, 10, 0, 0);
        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=2;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(btnEdit, gridBagConstraints);


        gridBagConstraints.gridx=4;
        gridBagConstraints.gridy=2;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(borrowBtn, gridBagConstraints);

        gridBagConstraints.gridx=5;
        gridBagConstraints.gridy=2;
        gridBagConstraints.gridwidth=1;
        gridBagConstraints.gridheight=1;
        gridBagLayout.setConstraints(returnBtn, gridBagConstraints);


        conditPanel.add(borrowUserNameLabel);
        conditPanel.add(borrowUserNameField);
        conditPanel.add(deviceNameLabel);
        conditPanel.add(deviceNameField);
        conditPanel.add(deviceCodeLabel);
        conditPanel.add(deviceCodeField);

        conditPanel.add(returnUserNameLabel);
        conditPanel.add(returnUserNameField);

        conditPanel.add(statusLabel);
        conditPanel.add(statusComboBox);


        conditPanel.add(searchBtn);
        conditPanel.add(resetBtn);

        conditPanel.add(borrowBtn);
        conditPanel.add(btnEdit);

        conditPanel.add(returnBtn);
        conditPanel.add(printBtn);

        conditPanel.add(fingerSearchBtn);

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
        borrowUserNameField.setText("");
        deviceNameField.setText("");
        deviceCodeField.setText("");
        returnUserNameField.setText("");
        statusComboBox.setSelectedIndex(0);
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

        new MySwingWorker(FrameUtil.currentFrame){
            @Override
            public void invokeBusiness() {
                TbBorrowRecord record = new TbBorrowRecord();
                record.setBorrowUserName(borrowUserNameField.getText());
                record.setDeviceName(deviceNameField.getText());
                record.setDeviceCode(deviceCodeField.getText());
                record.setReturnUserName(returnUserNameField.getText());
                //String selectedItem = (String) statusComboBox.getSelectedItem();

                if(statusComboBox.getSelectedIndex() == 1){
                    record.setStatus(Status.BORROWING.name());
                }else if(statusComboBox.getSelectedIndex() == 2){
                    record.setStatus(Status.RETURNED.name());
                }


                //设置记录总数
                table.setTotalRowCount((int) DaoFactory.getBorrowRecordDao().countAllByCondition(record));
                //结果集的总页数
                table.setTotalPage(table.getTotalRowCount() % table.getPageCount() == 0
                        ? table.getTotalRowCount()/  table.getPageCount() : table.getTotalRowCount() / table.getPageCount() + 1);


                List<TbBorrowRecord> recordList  = DaoFactory.getBorrowRecordDao().findByConditionPage(record,table.getCurrentPage(),table.getPageCount());
                setPageInfo();
                table.showTable(recordList);
            }
        }.execute();
    }


    private void initOperator() {

        deviceCodeField.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyChar()==KeyEvent.VK_ENTER )   //按回车键执行相应操作;
                {
                    String code = deviceCodeField.getText();
                    clear();
                    deviceCodeField.setText(code);
                    searchData();
                }
            }
        });

        // 查询事件
        // 增加回车事件
        //this.getRootPane().setDefaultButton(searchBtn);// 获取焦点
        searchBtn.addActionListener(e -> {
            searchData();
        });

        resetBtn.addActionListener(e -> {
            clear();
            searchData();
        });



        //编辑
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = table.getSelectedRows();
                if(selectedRows.length == 0){
                    JOptionPane.showMessageDialog(FrameUtil.currentFrame,"请选择一条借出记录修改","提示",1);
                    return;
                }

                if(selectedRows.length > 1){
                    JOptionPane.showMessageDialog(FrameUtil.currentFrame,"最多选择一条借出记录修改","提示",1);
                    return;
                }
                String id = table.getValueAt(selectedRows[0], 0).toString();
                if(StringUtils.isBlank(id)){
                    JOptionPane.showMessageDialog(FrameUtil.currentFrame,"借出记录不存在, 请刷新列表","提示",2);
                    return;
                }
                TbBorrowRecord record = DaoFactory.getBorrowRecordDao().queryById(Integer.valueOf(id));
                if(null == record){
                    JOptionPane.showMessageDialog(FrameUtil.currentFrame,"借出记录不存在, 请刷新列表","提示",1);
                    return;
                }
                new BorrowEditDialog(record).showDialog();
            }
        });

        //借出
        borrowBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCustomDialog();
            }
        });
        //归还
        returnBtn.addActionListener(e -> {

            int[] selectedRows = table.getSelectedRows();

            if(selectedRows.length == 0){
                JOptionPane.showMessageDialog(FrameUtil.currentFrame,"请选择一条借出记录归还","提示",1);
                return;
            }

            if(selectedRows.length > 1){
                JOptionPane.showMessageDialog(FrameUtil.currentFrame,"最多选择一条借出记录归还","提示",1);
                return;
            }
            String id = table.getValueAt(selectedRows[0], 0).toString();
            if(StringUtils.isBlank(id)){
                JOptionPane.showMessageDialog(FrameUtil.currentFrame,"借出记录不存在, 请刷新列表","提示",2);
                return;
            }
            TbBorrowRecord record = DaoFactory.getBorrowRecordDao().queryById(Integer.valueOf(id));
            if(null == record){
                JOptionPane.showMessageDialog(FrameUtil.currentFrame,"借出记录不存在, 请刷新列表","提示",1);
                return;
            }
            if(Status.RETURNED.name().equals(record.getStatus())){
                JOptionPane.showMessageDialog(FrameUtil.currentFrame,"此借出记录已经归还, 请选择其它借出记录","提示",1);
                return;
            }

            record.setOldReturnNum(record.getReturnNum());
            new ReturnDetailDialog(record).showDialog();

           /* new MySwingWorker(FrameUtil.currentFrame){
                @Override
                public void invokeBusiness() {
                    BorrowFingerDialog borrowFingerDialog = new BorrowFingerDialog(record);
                    FingerHelper fingerThread = new FingerHelper(borrowFingerDialog);

                    Thread dialogThread = new Thread(() -> {
                        *//**
                         * 指纹弹窗
                         *//*
                        borrowFingerDialog.showDialog();
                        fingerThread.interrupt();
                    });
                    dialogThread.start();
                    fingerThread.start();

                }
            }.execute();*/


        });

        //打印
        printBtn.addActionListener(e -> {
            int[] selectedRows = table.getSelectedRows();

            if(selectedRows.length == 0){
                JOptionPane.showMessageDialog(FrameUtil.currentFrame,"请选择一条借出记录打印","提示",1);
                return;
            }

            if(selectedRows.length > 1){
                JOptionPane.showMessageDialog(FrameUtil.currentFrame,"最多选择一条借出记录打印","提示",1);
                return;
            }
            String id = table.getValueAt(selectedRows[0], 0).toString();
            if(StringUtils.isBlank(id)){
                JOptionPane.showMessageDialog(FrameUtil.currentFrame,"借出记录不存在, 请刷新列表","警告",2);
                return;
            }
            TbBorrowRecord record = DaoFactory.getBorrowRecordDao().queryById(Integer.valueOf(id));
            if(null == record){
                JOptionPane.showMessageDialog(FrameUtil.currentFrame,"借出记录不存在, 请刷新列表","警告",2);
                return;
            }
            new BorrowFinishDialog(record).showDialog();
        });

        fingerSearchBtn.addActionListener(e -> {
            new MySwingWorker(FrameUtil.currentFrame){
                @Override
                public void invokeBusiness() {
                    BorrowSearchDialog borrowSearchDialog = new BorrowSearchDialog();
                    FingerHelper fingerThread = new FingerHelper(borrowSearchDialog);

                    Thread dialogThread = new Thread(() -> {
                        /**
                         * 指纹弹窗
                         */
                        borrowSearchDialog.showDialog();
                        fingerThread.interrupt();
                    });
                    dialogThread.start();
                    fingerThread.start();
                }
            }.execute();
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


    private void setPageInfo() {
        pageInfo.setText("总共 " + table.getTotalRowCount() + " 条记录|当前第 "
                + table.getCurrentPage() + " 页|" + "总共 " + table.getTotalPage() + " 页");
    }

    /**
     * 显示一个自定义的对话框
     **/
    private void showCustomDialog() {
        //添加用户框的弹出框
         new BorrowSelectDialog().showDialog();
    }


    @Override
    public JButton getSearchButton() {
        return this.resetBtn;
    }

    @Override
    public JTextField getBorrowUserNameField() {
        return borrowUserNameField;
    }

    @Override
    public JTextField getFirstSearchField() {
        return this.deviceCodeField;
    }
}
