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

	/** 是否有差异*/
	private boolean isDifference = true;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDt() {
		return (null != dt)? dt.trim() : "";
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public String getDigestBorrow() {
		return (null != digestBorrow)? digestBorrow.trim() : "";
	}

	public void setDigestBorrow(String digestBorrow) {
		this.digestBorrow = digestBorrow;
	}

	public String getDigestLoan() {
		return (null != digestLoan)? digestLoan.trim() : "";
	}

	public void setDigestLoan(String digestLoan) {
		this.digestLoan = digestLoan;
	}

	public String getBorrowAmount() {
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
		return (null != comment)? comment.trim() : "";
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isDifference() {
		return isDifference;
	}

	public void setDifference(boolean isDifference) {
		this.isDifference = isDifference;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean flag = false;
		
		if(null != obj){
			Account account = (Account)obj;
			if(this.getDt().equals(account.getDt())
				&& this.getDigestBorrow().equals(account.getDigestBorrow())
				&& this.getDigestLoan().equals(account.getDigestLoan())
				&& this.getBorrowAmount().equals(account.getBorrowAmount())
				&& this.getLoanAmount().equals(account.getLoanAmount())
				&& this.getTotalAmount().equals(account.getTotalAmount())
				&& this.getComment().equals(account.getComment())){
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", dt=" + dt + ", digestBorrow=" + digestBorrow + ", digestLoan=" + digestLoan
				+ ", borrowAmount=" + borrowAmount + ", loanAmount=" + loanAmount + ", totalAmount=" + totalAmount
				+ ", comment=" + comment + ", isDifference=" + isDifference + "]";
	}
}
