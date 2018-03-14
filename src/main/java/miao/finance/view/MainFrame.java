package miao.finance.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;

import miao.finance.dao.ExcelDao;
import miao.finance.model.Account;

/**
 * MainFrame
 * @Description: 主界面
 * @author libo
 * @date 2018年2月2日 下午4:11:53
 */
public class MainFrame extends JFrame{

	private JSplitPane mainPane;
	private JPanel leftPane;
	private JSplitPane rightPane;

	private JList<Object> jList;
	private JTable leftTable;
	private JTable rightTable;
	
	public MainFrame(){
//		menu();
		init();
	}
	
	private void menu(){
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("导入");
		menu.addMenuListener(new MenuListener() {
			
			@Override
			public void menuSelected(MenuEvent e) {
				new GuideFrame();
			}
			
			@Override
			public void menuDeselected(MenuEvent e) {}
			@Override
			public void menuCanceled(MenuEvent e) {}
		});
		
		
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	
	private void init(){
		this.setTitle("");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
		
//		this.setLocation((int)(screenSize.getWidth() / 2 - FRAME_WIDTH / 2), 
//				(int)(screenSize.getHeight() / 2 - FRAME_HEIGHT / 2));
		jList = new JList();
		leftTable = new JTable();
		rightTable = new JTable();

		JScrollPane leftScrollPane = new JScrollPane(leftTable);
		JScrollPane rightScrollPane = new JScrollPane(rightTable);
		
		if(StringUtils.isNotEmpty(GuideFrame.LEFT_PATH)){
			leftScrollPane.setBorder(new TitledBorder(null, 
					GuideFrame.LEFT_PATH.substring(GuideFrame.LEFT_PATH.lastIndexOf(File.separator) + 1, GuideFrame.LEFT_PATH.length()), TitledBorder.DEFAULT_JUSTIFICATION, 
					TitledBorder.DEFAULT_POSITION, new Font(null, Font.BOLD, 20), Color.BLUE));
		}
		if(StringUtils.isNotEmpty(GuideFrame.RIGHT_PATH)){
			rightScrollPane.setBorder(new TitledBorder(null, 
					GuideFrame.RIGHT_PATH.substring(GuideFrame.RIGHT_PATH.lastIndexOf(File.separator) + 1, GuideFrame.RIGHT_PATH.length()), TitledBorder.DEFAULT_JUSTIFICATION, 
					TitledBorder.DEFAULT_POSITION, new Font(null, Font.BOLD, 20), Color.BLUE));
		}
		
		rightPane = new JSplitPane();
		rightPane.setLeftComponent(leftScrollPane);
		rightPane.setRightComponent(rightScrollPane);
		rightPane.setDividerSize(5);
		rightPane.setDividerLocation(600);
		
		mainPane = new JSplitPane();
		mainPane.setLeftComponent(jList);
		mainPane.setRightComponent(rightPane);
		mainPane.setDividerSize(5);
		mainPane.setDividerLocation(150);
		
		this.add(mainPane);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//数据
		final Map<String, List<Account>> leftData = ExcelDao.readExcel(GuideFrame.LEFT_PATH);
		final Map<String, List<Account>> rightData = ExcelDao.readExcel(GuideFrame.RIGHT_PATH);
		
		ExcelDao.removeAll(leftData, rightData);
		
		List<String> leftKeys = new ArrayList<String>(leftData.keySet());
		leftKeys.addAll(rightData.keySet());
		
		Set<String> leftKeySet = new HashSet<String>(leftKeys);
		final Object[] leftNameArray = leftKeySet.toArray();
		final Object[] titleArray = new Object[]{"行号", "日期", "摘要(借)", "摘要(贷)", "借方", "贷方", "余额", "备注"};
		
		String name = null;
		if((null != leftNameArray) && (0 < leftNameArray.length)){
			name = leftNameArray[0].toString();
		}
		
		setTable(leftTable, name, titleArray, leftData, new HashSet<Integer>());
		setTable(rightTable, name, titleArray, rightData, new HashSet<Integer>());
		
		jList.setFont(new Font("宋体", Font.PLAIN, 13));
		jList.setListData(leftNameArray);
		jList.setSelectedIndex(0);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				
				if(!e.getValueIsAdjusting()){
					Set<Integer> leftRows = new HashSet<Integer>();
					Set<Integer> rightRows = new HashSet<Integer>();
					
					int index = ((JList<Object>) e.getSource()).getSelectedIndex();
					leftTable.setModel(new DefaultTableModel(getArray(leftData.get(leftNameArray[index]), titleArray, leftRows), titleArray));
					rightTable.setModel(new DefaultTableModel(getArray(rightData.get(leftNameArray[index]), titleArray, rightRows), titleArray));
					for(Object row : titleArray){
						leftTable.getColumn(row).setCellRenderer(new MyTableCellRenderrer(leftRows));
						rightTable.getColumn(row).setCellRenderer(new MyTableCellRenderrer(rightRows));
					}
				}
			}
		});
	}
	
	public void setTable(JTable table, String name, Object[] titleArray, Map<String, List<Account>> data, Set<Integer> rows){
		
		if(StringUtils.isNotEmpty(name)){
			List<Account> account = data.get(name);
			
			DefaultTableModel tableModel = new DefaultTableModel(getArray(account, titleArray, rows), titleArray);
			table.setModel(tableModel);
			table.setFont(new Font("宋体", Font.PLAIN, 13));
			table.setRowHeight(30);
			
			for(Object row : titleArray){
				table.getColumn(row).setCellRenderer(new MyTableCellRenderrer(rows));
			}
		}
	}
	
	public Object[][] getArray(List<Account> src, Object[] titleArray, Set<Integer> rows){
		
		//筛选天数
		Map<String, List<Account>> dateToAccount = new LinkedHashMap<String, List<Account>>();

		if(null == src){
			src = new ArrayList<Account>();
		}
		
		Account accountTemp = null;
		Object[][] tableArray = new Object[src.size()][titleArray.length];
		
		for(int i = 0; i < tableArray.length; i++){
			accountTemp = src.get(i);
			tableArray[i] = new Object[]{(i + 1), accountTemp.getDt(), accountTemp.getDigestBorrow(), accountTemp.getDigestLoan(),
					accountTemp.getBorrowAmount(), accountTemp.getLoanAmount(), accountTemp.getTotalAmount(), 
					accountTemp.getComment()};
			
			if(accountTemp.isDifference()){
				rows.add(i);
			}
		}
		
		return tableArray;
	}
	
	public static void main(String[] args) {
		new MainFrame();
	}
	
}

class MyTableCellRenderrer extends DefaultTableCellRenderer{
	
	private Set<Integer> rows = null;
	private static final long serialVersionUID = 1L;
	
	public MyTableCellRenderrer(Set<Integer> rows){
		super();
		this.rows = rows;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column){
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if(rows.contains(row)){
			comp.setBackground(new Color(0XC1FFC1));
		}else{
			comp.setBackground(Color.WHITE);
		}
		
		if(0 == column) {
			comp.setForeground(Color.red);
		}
		return comp;
	}
}

