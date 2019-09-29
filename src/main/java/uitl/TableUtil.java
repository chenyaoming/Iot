package uitl;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;


public class TableUtil {

    public static  JTable createTable(String[] columns, Object rows[][]) {
        JTable table;
        TableModel model = new DefaultTableModel(rows, columns);

        table = new JTable(model);
        RowSorter sorter = new TableRowSorter(model);
        table.setRowSorter(sorter);
        return table;
    }

    /**
     * 增加一行
     * @param table
     * @param data
     */
    public void tableAddRow(JTable table ,Object[] data) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.getColumnCount();
        // 添加数据到表格
        tableModel.addRow(data);

        // 更新表格
        table.invalidate();
    }

    /**
     * 填充表单
     * @param table
     * @param dataList
     */
    public void fillTable(JTable table ,List<Object[]> dataList) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);// 清除原有行

        // 填充数据
        for (Object[] objArr : dataList) {

            tableModel.addRow(objArr);

            /*Vector vector = new Vector<Object>();

            for (Object data:objArr){
                vector.add(data);
            }
            // 添加数据到表格
            tableModel.addRow(vector);*/
        }
        // 更新表格
        table.invalidate();
    }
}
