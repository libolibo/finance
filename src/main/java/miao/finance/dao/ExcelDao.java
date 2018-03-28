package miao.finance.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.alibaba.fastjson.JSONObject;

import miao.finance.model.Account;
import miao.finance.view.GuideFrame;

public class ExcelDao {

	public static Map<String, List<Account>> readExcel(String currentPath){
		LinkedHashMap<String, List<Account>> result = new LinkedHashMap<String, List<Account>>();
		
		if(StringUtils.isEmpty(currentPath)){
			return result;
		}
		
		InputStream ExcelFileToRead = null;
		try {
			ExcelFileToRead = new FileInputStream(currentPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(ExcelFileToRead);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HSSFSheet sheet = null;
		HSSFRow row = null; 
		HSSFCell cell = null;
		Iterator<Row> rows = null;
		Iterator<Cell> cells = null;
		List<Account> accountList = null;
		Account account = null;
		Iterator<Sheet> sheetIterator = wb.sheetIterator();
		while(sheetIterator.hasNext()){
			sheet = (HSSFSheet) sheetIterator.next();
			rows = sheet.rowIterator();
			accountList = new ArrayList<Account>();
			result.put(sheet.getSheetName(), accountList);
			
			sheet.getFirstRowNum();
			sheet.getLastRowNum();
			for(int i = 0; i < sheet.getLastRowNum(); i++){
				row = sheet.getRow(i);
				account = new Account();
				
				if((null == row) || (2 > i) || StringUtils.isEmpty(getCellValue(row.getCell(0)))){
					continue;
				}
				account.setRow(i + 1);
				account.setDt(getCellValue(row.getCell(0)));
				account.setDigestBorrow(getCellValue(row.getCell(1)));
				account.setDigestLoan(getCellValue(row.getCell(2)));
				account.setBorrowAmount(getCellValue(row.getCell(3)));
				account.setLoanAmount(getCellValue(row.getCell(4)));
				account.setTotalAmount(getCellValue(row.getCell(5)));
				account.setComment(getCellValue(row.getCell(6)));
				
				accountList.add(account);
			}
//			while (rows.hasNext()){
//				row = (XSSFRow) rows.next();
//				account = new Account();
//				account.setDt(getCellValue(row.getCell(0)));
//				account.setDigestBorrow(getCellValue(row.getCell(1)));
//				account.setDigestLoan(getCellValue(row.getCell(2)));
//				account.setBorrwoAmount(getCellValue(row.getCell(2)));
//				account.setLoanAmount(getCellValue(row.getCell(3)));
//				account.setTotalAmount(getCellValue(row.getCell(4)));
//				account.setComment(getCellValue(row.getCell(5)));
//				
//				accountList.add(account);
//				cells  = row.cellIterator();
//				while (cells.hasNext()){
//					cell = (XSSFCell) cells.next();
//			
//					if(cell.getCellTypeEnum() == CellType.NUMERIC){
//						if(DateUtil.isCellDateFormatted(cell)){
//							temp = sdf.format(cell.getDateCellValue());
//						}else{
//							temp = cell.getNumericCellValue() + " ";
//						}
//					}else if (cell.getCellTypeEnum() == CellType.STRING){
//						temp = cell.getStringCellValue() + "";
//					}else{
//						temp = "";
//					}
//					
//					if(StringUtils.isNotEmpty(temp)){
//						System.out.print(temp + "\t");
//					}
//				}
//			}
		}
		
		return result;
	}
	
	public static String getCellValue(HSSFCell cell){
		String result = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(null == cell){
			return "";
		}
		if(cell.getCellTypeEnum() == CellType.NUMERIC){
			if(DateUtil.isCellDateFormatted(cell)){
				result = sdf.format(cell.getDateCellValue());
			}else{
				result = cell.getNumericCellValue() + " ";
			}
		}else if (cell.getCellTypeEnum() == CellType.STRING){
			result = cell.getStringCellValue() + "";
		}else{
			result = "";
		}
		return result;
	}
	
	
	public static void main(String[] args) throws Exception {
		Map<String, List<Account>> map = new ExcelDao().readExcel(GuideFrame.LEFT_PATH);
		Map<String, List<Account>> newMap = new ExcelDao().readExcel(GuideFrame.RIGHT_PATH);
		
		PrintWriter out = null;
		File file = null;
		
		List<Account> list = null;
		Set<String> names = map.keySet();
		for(String row : names){
			file = new File("F:\\finance\\" + row + ".txt");
			file.getParentFile().mkdirs();
			
			out = new PrintWriter(file);
			list = map.get(row);
			for(Account temp : list){
				out.println(JSONObject.toJSONString(temp));
			}
			
			out.flush();
			out.close();
		}
	}
	
	
	public static void removeAll(Map<String, List<Account>> leftData,
				Map<String, List<Account>> rightData){
		
		List<String> sameAccounts = new ArrayList<String>();
		
		Set<String> leftKeys = leftData.keySet();
		List<Account> leftValue = null;
		List<Account> rightValue = null;
		for(String row : leftKeys){
			leftValue = leftData.get(row);
			rightValue = rightData.get(row);
			
			if((null != rightValue) 
					&& sameDetails(leftValue, rightValue)){
				sameAccounts.add(row);
			}
		}
		
		for(String row : sameAccounts){
			leftData.remove(row);
			rightData.remove(row);
		}
	}
	
	private static boolean sameDetails(List<Account> leftValue, List<Account> rightValue){
			boolean flag = true;

			for(Account row : leftValue){

				for(Account value : rightValue){
					if(value.isDifference() && row.equals(value)){
						row.setDifference(false);
						value.setDifference(false);
						break;
					}
				}
			}

			for(Account row : leftValue) {
				if(row.isDifference()) {
					flag = false;
					break;
				}
			}

			if(flag) {
				for(Account row : rightValue) {
					if(row.isDifference()) {
						flag = false;
						break;
					}
				}
			}
		return flag;
	} 
}
