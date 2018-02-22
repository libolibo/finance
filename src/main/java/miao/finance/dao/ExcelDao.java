package miao.finance.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public Map<String, List<Account>> readExcel() throws Exception{
		Map<String, List<Account>> result = new HashMap<String, List<Account>>();
		
		if(StringUtils.isEmpty(GuideFrame.CURRENT_PATH)){
			return result;
		}
		
		InputStream ExcelFileToRead = new FileInputStream(GuideFrame.CURRENT_PATH);
		HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

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
	
	public String getCellValue(HSSFCell cell){
		String result = null;
		
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
		Map<String, List<Account>> map = new ExcelDao().readExcel();
		
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
}
