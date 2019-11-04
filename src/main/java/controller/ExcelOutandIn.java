package controller;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import bean.AddUsers;
import bean.Users;
import dao.UsersDAO;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import uitl.CommonUtil;
import uitl.JFileChooserUtil;

public class ExcelOutandIn extends JFrame implements ActionListener {

    JButton button1 = new JButton("ToExcel");
    JButton button2 = new JButton("FromExcel");

    //Container ct = null;


    JButton add = new JButton("添加");
    JButton delete = new JButton("删除");
    JButton save = new JButton("保存");
    JButton reset = new JButton("刷新");

    //jp1 用来装 导入导出两个按钮，即 button1 ,button2
    JPanel jp1 = new JPanel(), jp;
    JPanel jp2 = new JPanel();
    JScrollPane jsp = null;
    UsersDAO userdao = new UsersDAO();
    Users users = null;
    //表格
    protected JTable table = null;
    protected String oldvalue = "";
    protected String newvalue = "";

    /**
     * @param args
     */
    public static void main(String[] args) {
        ExcelOutandIn tm = new ExcelOutandIn();
    }

    public ExcelOutandIn() {

        //new BorderLayout(); 这个没啥用，去掉

        Font font = new Font("宋体", 4, 14);

        //excel相关按钮
        jp1.add(button1);
        jp1.add(button2);

        //增加 保存 删除 刷新按钮相关
        add.setFont(font);
        save.setFont(font);
        delete.setFont(font);
        reset.setFont(font);

        jp2.add(add);
        jp2.add(delete);
        jp2.add(save);
        jp2.add(reset);

        this.getContentPane().add(jp1, BorderLayout.NORTH);
        this.getContentPane().add(jp2, BorderLayout.SOUTH);



        //初始化表格数据和添加监听器
        initTableAndAddListener();
        this.setTitle("ToOrFromExcel");
        this.setVisible(true);
        this.setSize(600, 400);
        this.setLocation(400, 250);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    void initTableAndAddListener() {
        //构建表格
        buildTable();

        button1.setActionCommand("ToExcel");
        button1.addActionListener(this);

        button2.setActionCommand("FromExcel");
        button2.addActionListener(this);

        delete.setActionCommand("delete");
        delete.addActionListener(this);

        reset.setActionCommand("reset");
        reset.addActionListener(this);

        save.setActionCommand("save");
        save.addActionListener(this);

        add.setActionCommand("add");
        add.addActionListener(this);
    }

    public void ToExcel(String path) {

        List list = userdao.findAll();

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Users");

        String[] n = { "编号", "姓名", "密码", "邮箱" };

        Object[][] value = new Object[list.size() + 1][4];
        for (int m = 0; m < n.length; m++) {
            value[0][m] = n[m];
        }
        for (int i = 0; i < list.size(); i++) {
            users = (Users) list.get(i);

            value[i + 1][0] = users.getId();
            value[i + 1][1] = users.getUsername();
            value[i + 1][2] = users.getPassword();
            value[i + 1][3] = users.getUEmail();

        }
        ExcelUtil.writeArrayToExcel(wb, sheet, list.size() + 1, 4, value);

        ExcelUtil.writeWorkbook(wb, path);

    }

    /**
     * 从Excel导入数据到数据库
     * @param filename
     */
    public void FromExcel(String filename) {

        String result = "success";
        /** Excel文件的存放位置。注意是正斜线 */
        // String fileToBeRead = "F:\\" + fileFileName;
        try {
            // 创建对Excel工作簿文件的引用
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
                    filename));
            // 创建对工作表的引用。
            // HSSFSheet sheet = workbook.getSheet("Sheet1");
            HSSFSheet sheet = workbook.getSheetAt(0);

            int j = 1;//从第2行开始堵数据
            // 第在excel中读取一条数据就将其插入到数据库中
            while (j < sheet.getPhysicalNumberOfRows()) {
                HSSFRow row = sheet.getRow(j);
                Users user = new Users();

                for (int i = 0; i <= 3; i++) {
                    HSSFCell cell = row.getCell((short) i);

                    if (i == 0) {
                        user.setId((int) cell.getNumericCellValue());
                    } else if (i == 1){
                        user.setUsername(cell.getStringCellValue());
                    }

                    else if (i == 2){
                        user.setPassword(cell.getStringCellValue());
                    }


                    else if (i == 3){
                        user.setUEmail(cell.getStringCellValue());
                    }

                }

                System.out.println(user.getId() + " " + user.getUsername()
                        + " " + user.getPassword() + " " + user.getUEmail());

                j++;

                userdao.save(user);

            }

        } catch (FileNotFoundException e2) {
            // TODO Auto-generated catch block
            System.out.println("notfound");
            e2.printStackTrace();
        } catch (IOException e3) {
            // TODO Auto-generated catch block
            System.out.println(e3.toString());

            e3.printStackTrace();
        } catch (Exception e4) {
            System.out.println(e4.toString());

        }

    }



    @SuppressWarnings("unchecked")
    public void fillTable(List<Users> users) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);// 清除原有行

        // 填充数据
        for (Users Users : users) {
            Vector vector = new Vector<Users>();

            vector.add(Users.getId());
            vector.add(Users.getUsername());
            vector.add(Users.getPassword());
            vector.add(Users.getUEmail());

            // 添加数据到表格
            tableModel.addRow(vector);
        }

        // 更新表格
        table.invalidate();
    }

    @SuppressWarnings("unchecked")
    public void tableAddRow(int id, String name, String pwd, String email) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.getColumnCount();

        // 填充数据

        Vector vector = new Vector<Users>();

        vector.add(id);
        vector.add(name);
        vector.add(pwd);
        vector.add(email);

        // 添加数据到表格
        tableModel.addRow(vector);

        // 更新表格
        table.invalidate();
    }

    public void buildTable() {
        //构建表格模型数据
        DefaultTableModel defaultModel = buildTableModelAndData();
        //根据模型新建表单
        table = new JTable(defaultModel);
        //设置排序器
        setRowSorter(defaultModel);
        //添加表格数据变更监听器
        addTableDataChangeListener(defaultModel);
        //添加鼠标监听事件
        addMouseListener();

        jsp = new JScrollPane(table);

        this.getContentPane().add(jsp);
    }

    private DefaultTableModel buildTableModelAndData() {
        String[] n = { "编号", "姓名", "密码", "邮箱" };
        List list = userdao.findAll();
        Object[][] value = new Object[list.size()][4];

        for (int i = 0; i < list.size(); i++) {
            users = (Users) list.get(i);

            value[i][0] = users.getId();
            value[i][1] = users.getUsername();
            value[i][2] = users.getPassword();
            value[i][3] = users.getUEmail();

        }

        DefaultTableModel defaultModel = new DefaultTableModel(value, n) {
            boolean[] editables = { false, true, true, true };

            @Override
            public boolean isCellEditable(int row, int col) {
                return editables[col];
            }
        };
        defaultModel.isCellEditable(1, 1);
        return defaultModel;
    }

    private void addMouseListener() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                // 记录进入编辑状态前单元格得数据

                try {
                    oldvalue = table.getValueAt(table.getSelectedRow(),
                            table.getSelectedColumn()).toString();
                    System.out.println(oldvalue);
                } catch (Exception ex) {
                    // TODO: handle exception
                }

            }

        });
    }

    private void addTableDataChangeListener(DefaultTableModel defaultModel) {
        defaultModel.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    newvalue = table.getValueAt(e.getLastRow(), e.getColumn())
                            .toString();
                    System.out.println(newvalue);
                    int rowss = table.getEditingRow();
                    if (newvalue.equals(oldvalue)) {
                        System.out.println(rowss);
                        System.out.println(
                                  table.getValueAt(rowss, 0) + ""
                                + table.getValueAt(rowss, 1) + ""
                                + table.getValueAt(rowss, 2) + ""
                                + table.getValueAt(rowss, 3));
                        JOptionPane.showMessageDialog(null, "数据没有修改");

                    } else {

                        int dialog = JOptionPane.showConfirmDialog(null,
                                "是否确认修改", "温馨提示", JOptionPane.YES_NO_OPTION);
                        if (dialog == JOptionPane.YES_OPTION) {

                            System.out.println(" 修改了");
                            String s1 = (String) table.getValueAt(rowss, 0)
                                    .toString();
                            int id = Integer.parseInt(s1);
                            users = new Users();
                            users.setId(id);
                            users.setUEmail(table.getValueAt(rowss, 3)
                                    .toString());
                            users.setUsername(table.getValueAt(rowss, 1)
                                    .toString());
                            users.setPassword(table.getValueAt(rowss, 2)
                                    .toString());

                            try {
                                userdao.saveOrUpdate2(users);

                            } catch (Exception eee) {
                                new UsersDAO().saveOrUpdate2(users);
                            }

                        } else if (dialog == JOptionPane.NO_OPTION) {
                            table.setValueAt(oldvalue, rowss, table
                                    .getSelectedColumn());
                            // System.out.println("没有确认修改");
                        }

                    }

                }

            }

        });
    }

    private void setRowSorter(DefaultTableModel defaultModel) {
        RowSorter sorter = new TableRowSorter(defaultModel);
        table.setRowSorter(sorter);

        // 设置排序
        ((DefaultRowSorter) sorter).setComparator(0, new Comparator<Object>() {
            @Override
            public int compare(Object arg0, Object arg1) {
                try {
                    int a = Integer.parseInt(arg0.toString());
                    int b = Integer.parseInt(arg1.toString());
                    return a - b;
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        });
    }



    public void dialog() {
        JTextArea tx1 = new JTextArea();
        JTextArea tx2 = new JTextArea();
        JTextArea tx3 = new JTextArea();

        JPanel pan = new JPanel();
        JPanel pan1 = new JPanel();
        JPanel pan2 = new JPanel();
        JPanel pan3 = new JPanel();

        pan1.add(tx1);
        pan2.add(tx2);
        pan3.add(tx3);
        pan.setLayout(new BorderLayout());
        pan.add(pan1, BorderLayout.NORTH);
        pan.add(pan2, BorderLayout.CENTER);
        pan.add(pan3, BorderLayout.SOUTH);

        JDialog jd = new JDialog();
        jd.add(pan);
        jd.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        if (e.getActionCommand().equals("add")) {
            //添加的监控事件
            doAddAction();
        }
        // defaultModel.addRow(v);
        if (e.getActionCommand().equals("delete")) {
            //删除的监控事件
            doDeleteAction();
        }

        if (e.getActionCommand().equals("save")) {
            //保存的监控事件
            doSaveAction();
        }

        if (e.getActionCommand().equals("reset")) {
            //刷新的监控事件
            doResetAction();
        }

        if (e.getActionCommand().equalsIgnoreCase("ToExcel")) {
            //导出excel的监控事件
            doExportExcelAction();

        } else if (e.getActionCommand().equalsIgnoreCase("FromExcel")) {
            //导入excel的监控事件
            doImportExcelAction();
        }

    }

    private void doImportExcelAction() {
        File selectedFile = JFileChooserUtil.getSelectedOpenFile(new String[]{".xls",".xlsx"},this);
        if (selectedFile != null) {
            // String name=selectedFile.getName();
            String path = selectedFile.getPath();
            FromExcel(path);
            fillTable(userdao.findAll());

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

    private void doResetAction() {
        fillTable(userdao.findAll());
    }

    private void doSaveAction() {
        System.out.println("save");
        this.getContentPane().remove(jp);
    }

    private void doDeleteAction() {
        int srow = 0;

        try {
            srow = table.getSelectedRow();
        } catch (Exception ee) {

        }
        int rowcount = table.getModel().getRowCount() - 1;// getRowCount返回行数，rowcount<0代表已经没有任何行了。

        if (srow > 0) {
            DefaultTableModel defaultModel = (DefaultTableModel) table.getModel();

            Object id = defaultModel.getValueAt(srow, 0);
            String ID = id.toString();
            users = userdao.findById(Integer.parseInt(ID));
            defaultModel.getRowCount();
            defaultModel.removeRow(srow);
            // userdao.delete(users);
            defaultModel.setRowCount(rowcount);
        }
    }

    private void doAddAction() {
        dialog();

        AddUsers adduser = new AddUsers();
        jp = new JPanel();
        jp.add(adduser);
        this.getContentPane().add(jp, BorderLayout.WEST);

        /*
         * users= adduser.getU(); if(users==null){
         * JOptionPane.showMessageDialog(null, "Null");
         *
         * }else{
         *
         * }
         */
        // tableAddRow(id, name, pwd, email);
    }

}