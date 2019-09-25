package uitl;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableUtil {

    public static  JTable createTable(String[] columns, Object rows[][]) {
        JTable table;
        TableModel model = new DefaultTableModel(rows, columns);

        table = new JTable(model);
        RowSorter sorter = new TableRowSorter(model);
        table.setRowSorter(sorter);
        return table;
    }
}
