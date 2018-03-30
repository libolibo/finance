package miao.finance.dao;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.alibaba.fastjson.JSONObject;

import miao.finance.model.Account;
import miao.finance.view.GuideFrame;

import javax.swing.*;

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
			e.printStackTrace();
		}
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(ExcelFileToRead);
		} catch (IOException e) {
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
//		Map<String, List<Account>> map = new ExcelDao().readExcel(GuideFrame.LEFT_PATH);
//		Map<String, List<Account>> newMap = new ExcelDao().readExcel(GuideFrame.RIGHT_PATH);
//
//		PrintWriter out = null;
//		File file = null;
//
//		List<Account> list = null;
//		Set<String> names = map.keySet();
//		for(String row : names){
//			file = new File("F:\\finance\\" + row + ".txt");
//			file.getParentFile().mkdirs();
//
//			out = new PrintWriter(file);
//			list = map.get(row);
//			for(Account temp : list){
//				out.println(JSONObject.toJSONString(temp));
//			}
//
//			out.flush();
//			out.close();
//		}
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

	public static void export(Object[] list, String currentPath) throws Exception{


		if(0 == list.length){
			return;
		}

		//声明一个工作簿
		HSSFWorkbook hwb = new HSSFWorkbook();
		//声明一个单子并命名
		HSSFSheet sheet = hwb.createSheet("银行对账单");
		//给单子名称一个长度
		sheet.setDefaultColumnWidth((short)15);
		//生成一个样式
		HSSFCellStyle style = hwb.createCellStyle();
		//创建第一行（也可以成为表头）
		HSSFRow row = sheet.createRow(0);

		//日期 对方单位编码	对方单位	摘要	结算方式编码	结算方式	票号	借方	贷方	余额
		//给表头第一行一次创建单元格
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("日期");
		cell = row.createCell((short)1);
		cell.setCellValue("对方单位编码");
		cell = row.createCell((short)2);
		cell.setCellValue("对方单位");
		cell = row.createCell((short)3);
		cell.setCellValue("摘要");
		cell = row.createCell((short)4);
		cell.setCellValue("结算方式编码");
		cell = row.createCell((short)5);
		cell.setCellValue("结算方式");
		cell = row.createCell((short)6);
		cell.setCellValue("票号");
		cell = row.createCell((short)7);
		cell.setCellValue("借方");
		cell = row.createCell((short)8);
		cell.setCellValue("贷方");
		cell = row.createCell((short)9);
		cell.setCellValue("余额");




		//向单元格里添加数据
		Account account = null;
		for(short i=0;i<list.length;i++){

			account = (Account)list[i];
			row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(account.getDt());
			row.createCell(3).setCellValue(account.getDigestBorrow() + "" + account.getDigestLoan());
			row.createCell(7).setCellValue(account.getBorrowAmount());
			row.createCell(8).setCellValue(account.getLoanAmount());
		}

		try {
			FileOutputStream out = new FileOutputStream(currentPath);
			hwb.write(out);
			out.close();
			JOptionPane.showMessageDialog(null, "导出成功!");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "导出失败！");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showConfirmDialog(null, "导出失败！");
			e.printStackTrace();
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
