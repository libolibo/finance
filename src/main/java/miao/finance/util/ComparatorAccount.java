package miao.finance.util;

import java.util.Comparator;

import miao.finance.model.Account;

/**
 * 账单 按日期排序
 */
public class ComparatorAccount implements Comparator<Account>{
	
	@Override
	public int compare(Account map1, Account map2) {
		String data1, data2;
		
		try {
        	data1 = map1.getDt();
        	data1 = (data1 != null)? data1 : "";
        	
        	data2 = map2.getDt();
        	data2 = (data1 != null)? data2 : "";
        	
        	return data2.compareTo(data1);
        } catch (Exception e) {
            return 0;
        }
	}
}
