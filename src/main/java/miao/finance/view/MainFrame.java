package miao.finance.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import miao.finance.dao.ExcelDao;
import miao.finance.model.Account;

/**
 * MainFrame
 * @Description: 主界面
 * @author libo
 * @date 2018年2月2日 下午4:11:53
 */
public class MainFrame extends JFrame{

	private static final int FRAME_WIDTH = 1200;
	private static final int FRAME_HEIGHT = 800;
	
	private JSplitPane mainPane;
	private JPanel leftPane;
	private JPanel rightPane;

	private JList jList;
	private JTable jTable;
	
	private Map<String, List<Account>> data = null;
	
	public MainFrame(){
		init();
	}
	
	private void init(){
		
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int)(screenSize.getWidth() / 2 - FRAME_WIDTH / 2), 
				(int)(screenSize.getHeight() / 2 - FRAME_HEIGHT / 2));
		jList = new JList();
		jTable = new JTable();
		
		
		mainPane = new JSplitPane();
		mainPane.setLeftComponent(jList);
		mainPane.setRightComponent(new JScrollPane(jTable));
		mainPane.setDividerSize(5);
		mainPane.setDividerLocation(150);
		
		this.add(mainPane);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//数据
		try {
			data = new ExcelDao().readExcel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final Object[] nameArray = data.keySet().toArray();
		final Object[] titleArray = new Object[]{"日期", "摘要(借)", "摘要(贷)", "借方", "贷方", "余额", "备注"};
		
		List<Account> account = data.get(nameArray[0]);
		
		DefaultTableModel tableModel = new DefaultTableModel(getArray(account, titleArray), titleArray);
		jTable.setModel(tableModel);
		jTable.setFont(new Font(null, Font.CENTER_BASELINE, 13));
		jTable.setRowHeight(30);
		
		jList.setFont(new Font(null, Font.CENTER_BASELINE, 13));
		jList.setListData(nameArray);
		jList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()){
					jTable.setModel(new DefaultTableModel(getArray(data.get(nameArray[e.getFirstIndex()]), titleArray), titleArray));
				}
			}
		});
		
		
	}
	
	public Object[][] getArray(List<Account> src, Object[] titleArray){
		
		//筛选天数
		Map<String, List<Account>> dateToAccount = new LinkedHashMap<String, List<Account>>();

		Collections.sort(src, new Comparator<Account>() {
			@Override
			public int compare(Account map1, Account map2) {
				String data1, data2;
				
				try {
		        	data1 = map1.getDt();
		        	data1 = (data1 != null)? data1 : "";
		        	
		        	data2 = map2.getDt();
		        	data2 = (data1 != null)? data2 : "";
		        	
		        	return data1.compareTo(data2);
		        } catch (Exception e) {
		            return 0;
		        }
			}
		});
		
		
		for(Account row : src){
			if(!dateToAccount.containsKey(row.getDt())){
				dateToAccount.put(row.getDt(), new ArrayList<Account>());
			}
			dateToAccount.get(row.getDt()).add(row);
		}
		
		//每日总计
		Set<String> dates = dateToAccount.keySet();
		List<Account> array = null;
		BigDecimal borrowTotal = new BigDecimal(0);
		BigDecimal loanTotal = new BigDecimal(0);
		BigDecimal amountTotal = new BigDecimal(0);
		Account accountTotal = null;
		List<Account> totals = new ArrayList<Account>();
		
		for(String row : dates){
			array = dateToAccount.get(row);
			borrowTotal = new BigDecimal(0);
			loanTotal = new BigDecimal(0);
			
			for(Account account : array){
				borrowTotal = borrowTotal.add(new BigDecimal(account.getBorrwoAmount()));
				loanTotal = loanTotal.add(new BigDecimal(account.getLoanAmount()));
				amountTotal = amountTotal.add(new BigDecimal(account.getTotalAmount()));
			}
			
			accountTotal = new Account();
			accountTotal.setDt("总计");
			accountTotal.setBorrowAmount("" + borrowTotal.doubleValue());
			accountTotal.setLoanAmount("" + loanTotal.doubleValue());
			accountTotal.setTotalAmount("" + amountTotal.doubleValue());
			array.add(accountTotal);
			totals.add(accountTotal);
		}
		
		src = new ArrayList<Account>();
		for(String row : dates){
			src.addAll(dateToAccount.get(row));
		}
		
		//最晚一天的账单总计数据
		int length = totals.size();
		Account lastTotal = new Account();
		if(length > 0){
			lastTotal = totals.get(length - 1);
			length--;
		}
		
		Account temp = null;
		for(int i = 0; i < length; i++){
			temp = totals.get(i);
			
			temp.setBorrowAmount(temp.getBorrwoAmount() + "  (" + 
					(new BigDecimal(temp.getBorrwoAmount()).subtract(new BigDecimal(lastTotal.getBorrwoAmount())) + ")"));
			temp.setLoanAmount(temp.getLoanAmount() + "  (" + 
					(new BigDecimal(temp.getLoanAmount()).subtract(new BigDecimal(lastTotal.getLoanAmount())) + ")"));
			temp.setTotalAmount(temp.getTotalAmount() + "  (" + 
					(new BigDecimal(temp.getTotalAmount()).subtract(new BigDecimal(lastTotal.getTotalAmount())) + ")"));
		}
		
		Account accountTemp = null;
		Object[][] tableArray = new Object[src.size()][titleArray.length];
		for(int i = 0; i < tableArray.length; i++){
			accountTemp = src.get(i);
			tableArray[i] = new Object[]{accountTemp.getDt(), accountTemp.getDigestBorrow(), accountTemp.getDigestLoan(),
					accountTemp.getBorrwoAmount(), accountTemp.getLoanAmount(), accountTemp.getTotalAmount(), 
					accountTemp.getComment()};
		}
		
		return tableArray;
	}
	
	public static void main(String[] args) {
		
		new MainFrame();
	}
}
