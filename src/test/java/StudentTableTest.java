
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class StudentTableTest extends JFrame implements ActionListener {
    private JScrollPane panel;
    private JButton next, previous, add, delete;
    private JLabel label1;
    private StudentTable table;

    public StudentTableTest() {
        super("表分页及操作");
        initTableData();
        initComponent();
    }

    private void initTableData() {
        // TODO Auto-generated method stub
        Student s = new Student(1, "yangfei", "男", 21);
        Student.students.add(s);
        s = new Student(2, "yangf", "女", 22);
        Student.students.add(s);
        s = new Student(3, "yangfei", "男", 23);
        Student.students.add(s);
        s = new Student(4, "yangf", "女", 24);
        Student.students.add(s);
        s = new Student(5, "yangfei", "男", 25);
        Student.students.add(s);
        s = new Student(6, "yangf", "女", 26);
        Student.students.add(s);
        s = new Student(7, "yangfei", "男", 27);
        Student.students.add(s);
        s = new Student(8, "yangf", "女", 28);
        Student.students.add(s);
    }

    private void initComponent() {
        // TODO Auto-generated method stub
        this.setSize(500, 230);
        table = new StudentTable();
        panel = new JScrollPane(table);
        panel.setBounds(10, 10, 470, 119);
        previous = new JButton("上一页");
        previous.setBounds(150, 150, 75, 20);
        next = new JButton("下一页");
        next.setBounds(255, 150, 75, 20);
        add = new JButton("添加");
        add.setBounds(350, 150, 65, 20);
        delete = new JButton("删除");
        delete.setBounds(420, 150, 65, 20);
        previous.addActionListener(this);
        next.addActionListener(this);
        add.addActionListener(this);
        delete.addActionListener(this);
        label1 = new JLabel("总共" + table.totalRowCount + "记录|当前第"
                + table.currentPage + "页");
        label1.setBounds(10, 150, 130, 20);
        this.getContentPane().setLayout(null);
        this.getContentPane().add(panel);
        this.getContentPane().add(previous);
        this.getContentPane().add(next);
        this.getContentPane().add(add);
        this.getContentPane().add(delete);
        this.getContentPane().add(label1);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * 按钮事件
     */
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        JButton button = (JButton) e.getSource();
        if (button.equals(previous)) {
            int i = table.getPreviousPage();
            if (i == -1)
                return;
        }
        if (button.equals(next)) {
            int i = table.getNextPage();
            if (i == -1)
                return;
        }
        if (button.equals(delete)) {
            int i = table.getSelectedRow();
            if (i == -1)
                return;
            Integer id = (Integer) table.getValueAt(i, 0);
            if (id == null)
                return;
            Student s = null;
            for (Student stu : Student.students) {
                if (stu.getId().equals(id))
                    s = stu;
            }
            int index = Student.students.indexOf(s);
            Student.students.remove(index);
            table.initTable();
            label1.setText("总共" + table.totalRowCount + "记录|当前第"
                    + table.currentPage + "页");
            return;
        }
        if (button.equals(add)) {
            Integer id = 0;
            for (Student stu : Student.students) {
                if (stu.getId() > id)
                    id = stu.getId();
            }
            Student student = new Student(id + 1, "wuming" + (id + 1), "男", 22);
            Student.students.add(student);
            table.initTable();
            label1.setText("总共" + table.totalRowCount + "记录|当前第"
                    + table.currentPage + "页");
            return;
        }
        DefaultTableModel model = new DefaultTableModel(table.getPageData(),
                table.columnNames);
        table.setModel(model);
        label1.setText("总共" + table.totalRowCount + "记录|当前第"
                + table.currentPage + "页");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new StudentTableTest();
    }
}