package miao.finance.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

/**
 * Account
 * @Description: 流水账
 * @author libo
 * @date 2018年2月2日 上午10:13:23
 */
public class Account implements Serializable{
	
	private static final long serialVersionUID = -3449751171244434029L;

	/** 主键ID*/
	private int id;
	
	/** 日期*/
	private String dt;
	
	/** 摘要（借）*/
	private String digestBorrow;
	
	/** 摘要（还）*/
	private String digestLoan;
	
	/** 借方金额*/
	private String borrowAmount;
	
	/** 贷方金额*/
	private String loanAmount;
	
	/** 总计金额*/
	private String totalAmount;
	
	/** 备注*/
	private String comment;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public String getDigestBorrow() {
		return digestBorrow;
	}

	public void setDigestBorrow(String digestBorrow) {
		this.digestBorrow = digestBorrow;
	}

	public String getDigestLoan() {
		return digestLoan;
	}

	public void setDigestLoan(String digestLoan) {
		this.digestLoan = digestLoan;
	}

	public String getBorrwoAmount() {
		return StringUtils.isNotEmpty(borrowAmount) ? borrowAmount.trim(): "0";
	}

	public void setBorrowAmount(String borrowAmount) {
		this.borrowAmount = borrowAmount;
	}

	public String getLoanAmount() {
		return StringUtils.isNotEmpty(loanAmount) ? loanAmount.trim(): "0";
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getTotalAmount() {
		return StringUtils.isNotEmpty(totalAmount) ? totalAmount.trim(): "0";
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
