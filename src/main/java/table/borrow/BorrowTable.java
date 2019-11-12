package table.borrow;

import bean.TbBorrowRecord;
import bean.TbUser;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
public class BorrowTable extends JTable {
	// JTable表分页信息相关变量
	private int currentPage = 1;
	private int pageCount = 10; //每页显示的条数 ,跟下面mode 的10对应
	private int totalPage = 0;//总页数
    private int totalRowCount = 0;//总行数

	//从0开始，第0个列是id隐藏列
	public static final int ID_HIDDEN_COLUM = 0;
	//从0开始，第6个列是图片列
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
	public static String[] COLUMN_NAMES = { "ID","序号","名称","型号", "管理编码", "存放位置", "图片","功用",
			"借用人","借用日期","借出保管员","归还人","归还日期","归还保管员","备注"};

	public BorrowTable() {
		initTable();
	}


	public void initTable() {

        this.setTotalRowCount((int) DaoFactory.getUserDao().countAll());
		List<TbBorrowRecord> borrowList = DaoFactory.getBorrowRecordDao().findByPage(this.getCurrentPage(),this.getPageCount());
		//结果集的总页数
		this.setTotalPage(totalRowCount % pageCount == 0 ? totalRowCount/ pageCount : totalRowCount / pageCount + 1);
        this.showTable(borrowList);
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
	 * 显示表格数据 COLUMN_NAMES
	 * @param list
	 */
	public void showTable(List<TbBorrowRecord> list){

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Object[][] data = new Object[list.size()][COLUMN_NAMES.length];
		if(CollectionUtils.isNotEmpty(list)){
			for (int i = 0; i < list.size(); i++) {

				TbBorrowRecord record = list.get(i);
				//隐藏列
				data[i][0] = record.getId();
				//序号
				data[i][1] = (i + 1)+(currentPage-1)*pageCount;

				data[i][2] = record.getDeviceName();
				data[i][3] = record.getDeviceType();
				data[i][4] = record.getDeviceCode();
				data[i][5] = record.getDevicePosition();


				if(StringUtils.isNotBlank(record.getDeviceImage())){

					ImageIcon imageIcon = new ImageIcon(record.getDeviceImage().trim());
					imageIcon.setImage(imageIcon.getImage().getScaledInstance(IMAGE_SHOW_WIDTH, IMAGE_SHOW_HEIGHT, Image.SCALE_DEFAULT));

					data[i][6] = imageIcon;
				}
				data[i][7] = record.getFeatures();

				data[i][8] = record.getBorrowUserName();
				if(null != record.getBorrowDate()){
					data[i][9] = simpleDateFormat.format(record.getBorrowDate());
				}
				data[i][10] = record.getBorrowClerkUserName();
				data[i][11] = record.getReturnUserName();
				if(null != record.getReturnDate()){
					data[i][12] = simpleDateFormat.format(record.getReturnDate());
				}
				data[i][13] = record.getReturnClerkUserName();
				data[i][14] = record.getRemark();
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

	@Override
	public Class getColumnClass(int column) {
		return (column == IMAGE_COLUM) ? Icon.class : Object.class;
	}


}
