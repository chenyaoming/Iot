package table;

import bean.TbDevice;
import dao.DaoFactory;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

@Data
public class TableBase extends JTable {
	// JTable表分页信息相关变量
	private int currentPage = 1;
	private int pageCount = 5; //每页显示的条数
    private int totalPage = 0;//总页数
    private int totalRowCount = 0;//总行数
    //private int currentPageRowCount=0;//当前页数据条数
    //private int column = 0; //表的列数
    //private long restCount;
    //private static Object[][] resultData;

	//public static List<TbDevice> bigList=new ArrayList<>(); //大集合，从外界获取
	//public static List<TbDevice> smallList=new ArrayList<>(); //小集合，返回给调用它的类


	// JTable表信息相关变量
	//private List<TbDevice> products = Product.products;
	public static String[] columnNames = { "序号", "名称", "型号", "管理编码",
			"存放位置", "图片", "功用"};
	public static DefaultTableModel model = new DefaultTableModel(columnNames,
			5);

	public TableBase() {
		initTable();
	}

	public void initTable() {

        this.setTotalRowCount((int) DaoFactory.getDeviceDao().countAll());
		List<TbDevice> deviceList = DaoFactory.getDeviceDao().findByPage(this.getCurrentPage(),this.getPageCount());
		//结果集的总页数
		this.setTotalPage(totalRowCount % pageCount == 0 ? totalRowCount/ pageCount : totalRowCount / pageCount + 1);

		//Object[][] data = getData(deviceList);
        this.setModel(model);
        this.showTable(deviceList);
		/*if (data != null) {
			*//*initResultData(data);
			model = new DefaultTableModel(getPageData(), columnNames);*//*
		} else {
			// 如果结果集中没有数据，那么就用空来代替数据集中的每一行
			Object[][] nothing = { {}, {}, {}, {}, {} };
			model = new DefaultTableModel(nothing, columnNames);
		}*/
		// 表头不可拖动
		this.getTableHeader().setReorderingAllowed(false);
		//this.setModel(model);
		// 设置表格内容不能被编辑
		isCellEditable(this.getRowCount(), this.getColumnCount());
		this.setRowHeight(20);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		setDefaultRenderer(Object.class, r);
	}


	public void clearTable() {
		//用空来代替数据集中的每一行
		Object[][] nothing = { {}, {}, {}, {}, {} };
		model = new DefaultTableModel(nothing, columnNames);
		totalRowCount = 0;

		// 表头不可拖动
		this.getTableHeader().setReorderingAllowed(false);
		this.setModel(model);
		// 设置表格内容不能被编辑
		isCellEditable(this.getRowCount(), this.getColumnCount());
		this.setRowHeight(20);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		setDefaultRenderer(Object.class, r);
	}
	// 表格允许被编辑
	@Override
	public boolean isCellEditable(int row, int column) {
		return true;
	}

	/**
	 * 显示表格数据 { "序号", "名称", "型号", "管理编码", "存放位置", "图片", "功用"};
	 * @param list
	 */
	public void showTable(List<TbDevice> list){
	    if(CollectionUtils.isNotEmpty(list)){
            for (int i = 0; i < list.size(); i++) {
                this.setValueAt((i + 1)+(currentPage-1)*pageCount, i, 0);
                this.setValueAt((list.get(i)).getName(), i, 1);
                this.setValueAt((list.get(i)).getTypeNum(), i, 2);
                this.setValueAt((list.get(i)).getCode(), i, 3);
                this.setValueAt((list.get(i)).getSavePosition(), i, 4);
                this.setValueAt((list.get(i)).getImage(), i, 5);
                this.setValueAt((list.get(i)).getFeatures(), i, 6);
            }
        }

	}
	// 初始化结果集
	private Object[][] getData(List<TbDevice> devices) {
		if (CollectionUtils.isNotEmpty(devices)) {
			Object[][] data = new Object[devices.size()][7];
			for (int i = 0; i < devices.size(); i++) {
				TbDevice d = devices.get(i);
				d.setId(i+1);//按遍历顺序排列数据集
				Object[] a = { d.getId(), d.getName(),d.getTypeNum(),d.getCode(),
						d.getSavePosition(),d.getImage(),d.getFeatures()};// 把List集合的数据赋给Object数组
				data[i] = a;// 把数组的值赋给二维数组的一行
			}
			return data;
		}
		return null;
	}

	/*public void initResultData(Object[][] data) {
		if (data != null) {
			resultData = data;// 总的结果集
			column = data[0].length;// 表的列数
			totalRowCount = data.length;// 表的长度
			totalPage = totalRowCount % pageCount == 0 ? totalRowCount/ pageCount : totalRowCount / pageCount + 1;// 结果集的总页数
			//restCount = totalRowCount % pageCount == 0 ? 5 : totalRowCount% pageCount;// 最后一页的数据数
		}else{
			currentPageRowCount=0;
			currentPage=1;
			totalPage=1;
		}

	}*/

	/**
	 * 根据当前页，筛选记录
	 */
	public List<TbDevice> select() {
		/*restCount = bigList.size();
		for (int i = (currentPage - 1) * pageCount; i < currentPage	* pageCount && i < restCount; i++) {
			smallList.add(bigList.get(i));
		}
		currentPageRowCount=smallList.size();
		return smallList;*/
		List<TbDevice> deviceList = DaoFactory.getDeviceDao().findByPage(this.getCurrentPage(),this.getPageCount());
		if(null == deviceList ){
			deviceList = new ArrayList<>();
		}
		//currentPageRowCount = deviceList.size();
		return deviceList;
	}
	/**
	 * 获取分页数据
	 *
	 * @return
	 */
	/*public Object[][] getPageData() {
		Object[][] currentPageData = new Object[pageCount][column];// 构造每页数据集
		if (this.getCurrentPage() < this.totalPage) {// 如果当前页数小于总页数，那么每页数目应该是规定的数pageCount
			for (int i = pageCount * (this.getCurrentPage() - 1); i < pageCount
					* (this.getCurrentPage() - 1) + pageCount; i++) {
				for (int j = 0; j < column; j++) {
					// 把结果集中对应每页的每一行数据全部赋值给当前页的每一行的每一列
					currentPageData[i % pageCount][j] = resultData[i][j];
				}
			}
		} else {
			// 在动态改变数据结果集的时候，如果当前页没有数据了，则回到前一页（一般针对最后一页而言）
			if (1!=currentPage&&pageCount * (this.getCurrentPage() - 1) >= totalRowCount) {
				this.currentPage--;
			}
			for (int i = pageCount * (this.getCurrentPage() - 1); i < pageCount
					* (this.getCurrentPage() - 1) + restCount; i++) {
				for (int j = 0; j < column; j++) {
					currentPageData[i % pageCount][j] = resultData[i][j];
				}
			}
		}
		return currentPageData;
	}*/

	/**
	 * 获取上一页
	 */
	public int getPreviousPage() {
		if (this.currentPage != 1) {
			return --currentPage;
		}
		return -1;
	}

	/**
	 * 获取下一页
	 *
	 * @return
	 */
	public int getNextPage() {
		if (this.currentPage != this.totalPage) {
			return ++currentPage;
		}
		return -1;
	}

	/**
	 * 获取最后一页
	 */
	public int getLastPage() {
        this.currentPage=totalPage;
		return this.currentPage;
	}

	/**
	 * 获取第一页
	 */
	public int getFirstPage() {
		return 1;
	}



}
