package miao.finance.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

import javax.swing.*;
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

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private List<Account> leftAccount = null;
	private List<Account> rightAccount = null;
	private String sheetName = "";

	public MainFrame(){
		menu();
		init();
	}
	
	private void menu(){
		
		JMenuBar menuBar = new JMenuBar();
		
		final JMenu menu = new JMenu("展开");

		menu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {

				if ("展开".equals(menu.getText())) {
					MainFrame.this.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight() - 100);
					rightPane.setVisible(true);

					//menu.setText("折叠");
					menu.setVisible(false);
				} else {
					rightPane.setVisible(false);
					MainFrame.this.setSize(200, 500);


					menu.setText("展开");
				}
				menu.setSelected(false);
				menu.setFocusable(false);
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});
		menuBar.add(menu);

		JMenu exportMenu = new JMenu("导出");

		final JMenuItem exportPathItem = new JMenuItem("导出目录");
		exportMenu.add(exportPathItem);

		int leftSplit = GuideFrame.LEFT_PATH.lastIndexOf(File.separator);
		final JMenuItem exportLeftMenu = new JMenuItem(GuideFrame.LEFT_PATH.substring(leftSplit + 1));
		exportMenu.add(exportLeftMenu);

		int rightSplit = GuideFrame.RIGHT_PATH.lastIndexOf(File.separator);
		final JMenuItem exportRightMenu = new JMenuItem(GuideFrame.RIGHT_PATH.substring(rightSplit + 1));
		exportMenu.add(exportRightMenu);

		exportPathItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				fileChooser.showDialog(new JLabel(), "选择");

				File file = fileChooser.getSelectedFile();

				if(null != file){
					GuideFrame.EXPORT_PATH = fileChooser.getSelectedFile().getPath();

					Properties properties = new Properties();
					try{
						properties.put(GuideFrame.PROPERTIES_KEY, GuideFrame.EXPORT_PATH);
						properties.store(new FileOutputStream(new File(System.getProperty("user.home") + File.separator + GuideFrame.PROPERTIES_NAME)), "");
						properties.clear();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});

		exportLeftMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					String folderPath = GuideFrame.EXPORT_PATH;
					if (StringUtils.isEmpty(folderPath)) {
						folderPath = GuideFrame.LEFT_PATH.substring(0, leftSplit);
					}

					ExcelDao.export(leftAccount.stream().filter(account -> account.isDifference()).toArray(),
							folderPath + File.separator + sheetName + "(左侧).xls");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		exportRightMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try{
					String folderPath = GuideFrame.EXPORT_PATH;
					if(StringUtils.isEmpty(folderPath)){
						folderPath = GuideFrame.RIGHT_PATH.substring(0, rightSplit);
					}

					ExcelDao.export(rightAccount.stream().filter(account -> account.isDifference()).toArray(),
							folderPath + File.separator + sheetName + "(右侧).xls");
				}catch(Exception e){


					e.printStackTrace();
				}
			}
		});

		menuBar.add(exportMenu);
		this.setJMenuBar(menuBar);
	}
	
	private void init(){
		this.setTitle("");

		this.setSize(200, 500);
		//this.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());

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
		rightPane.setVisible(false);


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
		leftAccount = leftData.get(name);
		rightAccount = rightData.get(name);
		sheetName = name;

		jList.setFont(new Font("宋体", Font.PLAIN, 13));
		jList.setListData(leftNameArray);
		jList.setSelectedIndex(0);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {

				if (!e.getValueIsAdjusting()) {
					Set<Integer> leftRows = new HashSet<Integer>();
					Set<Integer> rightRows = new HashSet<Integer>();

					int index = ((JList<Object>) e.getSource()).getSelectedIndex();
					leftTable.setModel(new DefaultTableModel(getArray(leftData.get(leftNameArray[index]), titleArray, leftRows), titleArray));
					rightTable.setModel(new DefaultTableModel(getArray(rightData.get(leftNameArray[index]), titleArray, rightRows), titleArray));

					leftAccount = leftData.get(leftNameArray[index]);
					rightAccount = rightData.get(leftNameArray[index]);
					sheetName = leftNameArray[index].toString();

//					for (Object row : titleArray) {
//						leftTable.getColumn(row).setCellRenderer(new MyTableCellRenderrer(leftRows));
//						rightTable.getColumn(row).setCellRenderer(new MyTableCellRenderrer(rightRows));
//					}
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
			
//			for(Object row : titleArray){
//				table.getColumn(row).setCellRenderer(new MyTableCellRenderrer(rows));
//			}
		}
	}
	
	public Object[][] getArray(List<Account> src, Object[] titleArray, Set<Integer> rows){
		
		//筛选天数
		Map<String, List<Account>> dateToAccount = new LinkedHashMap<String, List<Account>>();

		if(null == src){
			src = new ArrayList<Account>();
		}
		
		Account accountTemp = null;
		Object[] array = src.stream().filter(account -> account.isDifference()).toArray();
		Object[][] tableArray = new Object[array.length][titleArray.length];

		for(int i = 0; i < array.length; i++){
			accountTemp = (Account)array[i];
			tableArray[i] = new Object[]{accountTemp.getRow(), accountTemp.getDt(), accountTemp.getDigestBorrow(), accountTemp.getDigestLoan(),
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
		}else {
			comp.setBackground(Color.WHITE);
		}


		return comp;
	}
}

