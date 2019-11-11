package frame;

import frame.borrow.BorrowPanel;
import frame.device.DevicePanel;
import frame.user.UserPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFram extends JFrame{

    private JMenuBar menubar;
    private JMenu fileMenu;
    private JMenuItem imput, export,exit;

    // 创建选项卡面板
    final JTabbedPane tabbedPane = new JTabbedPane();

    public MainFram(){

        menubar = new JMenuBar();
        fileMenu = new JMenu("文件");
        imput = new JMenuItem("导入");
        export=new JMenuItem("导出");
        exit = new JMenuItem("退出");

        // 设置菜单
        setJMenuBar(menubar);
        menubar.add(fileMenu);
        fileMenu.add(imput);
        fileMenu.addSeparator();
        fileMenu.add(export);
        fileMenu.addSeparator();
        fileMenu.add(exit);


        this.setLayout(new BorderLayout());

        JPanel treePanel = createTreePanel();
        this.add(treePanel, BorderLayout.WEST);



        // 主界面标题
        this.setTitle("出入库系统");
        this.setIconImage(new ImageIcon("images/logo.png").getImage());



        // 创建第 1 个选项卡（选项卡只包含 标题）
        tabbedPane.addTab("设备管理", new DevicePanel(this));

        // 创建第 2 个选项卡（选项卡包含 标题 和 图标）
        tabbedPane.addTab("人员管理", new UserPanel(this));

        // 创建第 3 个选项卡（选项卡包含 标题、图标 和 tip提示）
        //tabbedPane.addTab("借出归还管理", new ImageIcon("bb.jpg"), createTextPanel("TAB 03"), "This is a tab.");

        tabbedPane.addTab("借出归还管理", new BorrowPanel(this));

        // 添加选项卡选中状态改变的监听器
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("当前选中的选项卡: " + tabbedPane.getSelectedIndex());
            }
        });

        // 设置默认选中的选项卡
        tabbedPane.setSelectedIndex(0);



        this.add(tabbedPane, BorderLayout.CENTER);


        this.setPreferredSize(new Dimension(900, 600));
        // 设置主窗体显示在屏幕的位置
        this.setLocation(280, 50);
        // 设置是否显示
        this.setVisible(true);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }


    public static void main(String[] args){
        MainFram mainFram = new MainFram();

        //设置样式
        /*CommonUtil.setlookandfeel();
        IndexFrame indexFrame = new IndexFrame();*/
    }

    public JPanel createTreePanel(){
        JPanel panel = new JPanel(new BorderLayout());

        // 创建根节点
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("出入库系统");

        // 创建二级节点
        DefaultMutableTreeNode deviceNode = new DefaultMutableTreeNode("设备管理");
        DefaultMutableTreeNode userNode = new DefaultMutableTreeNode("人员管理");
        DefaultMutableTreeNode borrowNode = new DefaultMutableTreeNode("借出归还管理");

        rootNode.add(deviceNode);
        rootNode.add(userNode);
        rootNode.add(borrowNode);

        // 使用根节点创建树组件
        JTree tree = new JTree(rootNode);

        // 设置树显示根节点句柄
        tree.setShowsRootHandles(true);
        // 设置树节点不可编辑
        tree.setEditable(false);

        // 设置节点选中监听器
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                JTree jTree = (JTree) e.getSource();
                // 设置默认选中的选项卡
                tabbedPane.setSelectedIndex(jTree.getSelectionModel().getLeadSelectionRow() - 1);

                //System.out.println("当前被选中的节点: " + e.getPath());
            }
        });

        // 创建滚动面板，包裹树（因为树节点展开后可能需要很大的空间来显示，所以需要用一个滚动面板来包裹）
        JScrollPane scrollPane = new JScrollPane(tree);

        panel.setPreferredSize(new Dimension(150, 600));

        // 添加滚动面板到那内容面板
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void initOperator() {
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitWindow();
            }
        });

        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //doExportExcelAction();
            }
        });

        imput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //doImportExcelAction(new String[]{".xls",".xlsx"});
            }
        });
    }

    // 退出窗口
    private void exitWindow() {
        //JOptionPane.showMessageDialog(null, "程序即将退出...");
        System.exit(0);
    }


    /**
     * 创建一个面板，面板中心显示一个标签，用于表示某个选项卡需要显示的内容
     */
    private static JComponent createTextPanel(String text) {
        // 创建面板, 使用一个 1 行 1 列的网格布局（为了让标签的宽高自动撑满面板）
        JPanel panel = new JPanel(new GridLayout(1, 1));

        // 创建标签
        JLabel label = new JLabel(text);
        label.setFont(new Font(null, Font.PLAIN, 50));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        // 添加标签到面板
        panel.add(label);

        return panel;
    }

}
