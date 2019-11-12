package table.device;

import bean.TbDevice;
import dao.DaoFactory;
import frame.device.JScrollImagePanel;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import uitl.ModalFrameUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

@Data
public class DeviceTable extends JTable {
	// JTable表分页信息相关变量
	private int currentPage = 1;
	private int pageCount = 10; //每页显示的条数 ,跟下面mode 的10对应
	private int totalPage = 0;//总页数
    private int totalRowCount = 0;//总行数

	//从0开始，第0个列是id隐藏列
	public static final int ID_HIDDEN_COLUM = 0;
	//从0开始，第7个列是图片列
	public static final int IMAGE_COLUM = 7;
	//图片列宽度
	public static final int IMAGE_COLUM_WIDTH = 100;
    //表格行高大小
	public static final int ROW_HEIGHT = 40;

	/**
	 * 表格展示图片的宽度
	 */
	public static final int IMAGE_SHOW_WIDTH = 80;
	/**
	 * 表格展示图片的高度
	 */
	public static final int IMAGE_SHOW_HEIGHT = 30;

	//ID为隐藏列
	public static String[] COLUMN_NAMES = { "ID","序号", "名称", "型号", "管理编码","库存数量",
			"存放位置", "图片", "功用"};

	private Component ownFrame = null;

	public DeviceTable() {
		initTable();
	}


	public void initTable() {

        this.setTotalRowCount((int) DaoFactory.getDeviceDao().countAll());
		List<TbDevice> deviceList = DaoFactory.getDeviceDao().findByPage(this.getCurrentPage(),this.getPageCount());
		//结果集的总页数
		this.setTotalPage(totalRowCount % pageCount == 0 ? totalRowCount/ pageCount : totalRowCount / pageCount + 1);
        this.showTable(deviceList);
        //表格单元格内容气泡悬浮显示
		addMouseMotionListener(this);
		addMouseListener(this);

	}

	// 表格允许被编辑
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	/**
	 * 显示表格数据 {"ID", "序号", "名称", "型号", "管理编码", "库存数量","存放位置", "图片", "功用"};
	 * @param list
	 */
	public void showTable(List<TbDevice> list){

		Object[][] data = new Object[list.size()][COLUMN_NAMES.length];
		if(CollectionUtils.isNotEmpty(list)){
			for (int i = 0; i < list.size(); i++) {

				TbDevice device = list.get(i);
				//隐藏列
				data[i][0] = device.getId();
				data[i][1] = (i + 1)+(currentPage-1)*pageCount;
				data[i][2] = device.getName();
				data[i][3] = device.getTypeNum();
				data[i][4] = device.getCode();
				data[i][5] = device.getCount();
				data[i][6] = device.getSavePosition();


				if(StringUtils.isNotBlank(device.getImage())){

					ImageIcon imageIcon = new ImageIcon(device.getImage().trim());
					imageIcon.setImage(imageIcon.getImage().getScaledInstance(IMAGE_SHOW_WIDTH, IMAGE_SHOW_HEIGHT, Image.SCALE_DEFAULT));

					data[i][7] = imageIcon;
				}
				data[i][8] = device.getFeatures();
			}
		}
		DefaultTableModel model = new DefaultTableModel(data, COLUMN_NAMES);
	    this.setModel(model);

	    //设置隐藏列
		setHideColumn(ID_HIDDEN_COLUM);

		// 表头不可拖动
		this.getTableHeader().setReorderingAllowed(false);
		// 设置表格内容不能被编辑
		isCellEditable(this.getRowCount(), this.getColumnCount());
		//设置行高
		this.setRowHeight(ROW_HEIGHT);
		//设置图片的列宽
		setColumnWidth(IMAGE_COLUM,IMAGE_COLUM_WIDTH);

		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		setDefaultRenderer(Object.class, r);
	}

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


	@Override
	public Class getColumnClass(int column) {
		return (column == IMAGE_COLUM) ? Icon.class : Object.class;
	}

	/**
	 * 设置指定列的宽度
	 * @param colname  列名
	 * @param width
	 */
	public void setColumnWidth(Object colname,int width){
		//此方法是通过equals方法查找的，需要注意列名重复问题
		this.getColumn(colname).setPreferredWidth(width);
	}

	/**
	 * 设置指定列的宽度
	 * @param column
	 * @param width
	 */
	public void setColumnWidth(int column,int width){
		this.getTableHeader().getColumnModel().getColumn(column).setPreferredWidth(width);
		this.getColumnModel().getColumn(column).setPreferredWidth(width);
	}

	/**
	 * 设置隐藏列
	 * @param i 所隐藏的列
	 */
	public void setHideColumn(int i){
		this.getTableHeader().getColumnModel().getColumn(i).setMaxWidth(0);
		this.getTableHeader().getColumnModel().getColumn(i).setMinWidth(0);
		this.getColumnModel().getColumn(i).setMaxWidth(0);
		this.getColumnModel().getColumn(i).setMinWidth(0);
	}

	/**
	 *  表格单元格内容气泡悬浮显示
	 */
	public void addMouseMotionListener(JTable table){
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());
				//图片一列不展示
				if (row > -1 && col > -1 && col != IMAGE_COLUM) {
					Object value = table.getValueAt(row, col);
					if (null != value && !"".equals(value)) {
						table.setToolTipText(value.toString());// 悬浮显示单元格内容
					} else {
						table.setToolTipText(null);// 关闭提示
					}
				}
			}
		});
	}

	public void addMouseListener(final  JTable table) {
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				// 记录进入编辑状态前单元格得数据
				try {
					if(table.getSelectedColumn() == IMAGE_COLUM){
						//图片那一列

						Object obj = table.getValueAt(table.getSelectedRow(),
								table.getSelectedColumn());
						if(null != obj){
							String imgUrl = obj.toString();
							if(StringUtils.isNotBlank(imgUrl) && new File(imgUrl.trim()).exists()){
								JFrame imageFrame = new JFrame();

								// 设置对话框的宽高
								imageFrame.setSize(550, 450);
								imageFrame.setLocationRelativeTo(table);

								JScrollImagePanel jScrollImagePanel = new JScrollImagePanel(imgUrl);
								JScrollPane scrollPane=new JScrollPane();
								scrollPane.setViewportView(jScrollImagePanel);

								//dialog.setSize(jScrollImagePanel.getWidth(), jScrollImagePanel.getHeight());
								imageFrame.add(scrollPane,BorderLayout.CENTER);
								//imageFrame.setVisible(true);
								ModalFrameUtil.showAsModal(imageFrame,null);

								imageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							}
						}
					}
				} catch (Exception ex) {
				}
			}

		});
	}

}
