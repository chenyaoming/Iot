import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TableImage extends JFrame {
    public TableImage() {
        ImageIcon aboutIcon = new ImageIcon("C:\\Users\\mg\\Desktop\\kk.jpg");
        ImageIcon addIcon = new ImageIcon("C:\\Users\\mg\\Desktop\\ff.jpg");
        ImageIcon copyIcon = new ImageIcon("C:\\Users\\mg\\Desktop\\bbb.jpg");

        aboutIcon.setImage(aboutIcon.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));

        String[] columnNames = { "Picture", "Description" };
        Object[][] data = { { aboutIcon, "About" }, { addIcon, "Add" },
                { copyIcon, "Copy" }, };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable();
        table.setModel(model);



        /*JTable table = new JTable(model) {
            public Class getColumnClass(int column) {
                return (column == 0) ? Icon.class : Object.class;
            }
        };*/
        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);
    }

    public static void main(String[] args) {


        TableImage frame = new TableImage();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        /*new test();*/
    }

    public static class test extends JFrame {
        private ImageIcon img;
        private JLabel showImg;
        private final static int WIDTH = 147;
        private final static int HEIGHT = 136;

        public test() {
            img = new ImageIcon("C:\\Users\\mg\\Desktop\\kk.jpg");
            img.setImage(img.getImage().getScaledInstance(test.WIDTH, test.HEIGHT, Image.SCALE_DEFAULT));
            showImg = new JLabel();
            showImg.setIcon(img);
            this.add(showImg, BorderLayout.CENTER);
            this.setBounds(300, 200, 400, 300);
            this.pack();
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setVisible(true);
        }
    }

}
