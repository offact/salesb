package com.offact.salesb.vo.business;

import com.offact.salesb.vo.AbstractVO;
/**
 * @author 4530
 *
 */
public class ProductMasterVO extends AbstractVO {
	
	private String idx;
	private String groupId;
	private String productCode;
	private String productCategory;
	private String productName;
	private String modelName;
	private String makeCompany;
	private String suplycompany;
	private String productPrice;
	private String salesPrice;
	private String setteleRate;
	private String stockCnt;
	private String deliveryOption;
	private String optionKey;
	
	private String createUserId;
	private String createUserName;
	private String createDateTime;
	private String updateUserId;
	private String updateUserName;
	private String updateDateTime;
	private String deletedYn;
	
    private String searchGubun;
    private String searchValue;

    // /** for paging */
    private String totalCount       = "0";
    private String curPage          = "1";
    private String rowCount         = "10";
    private String page_limit_val1;
    private String page_limit_val2;

    private String errMsg;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getMakeCompany() {
		return makeCompany;
	}

	public void setMakeCompany(String makeCompany) {
		this.makeCompany = makeCompany;
	}

	public String getSuplycompany() {
		return suplycompany;
	}

	public void setSuplycompany(String suplycompany) {
		this.suplycompany = suplycompany;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(String salesPrice) {
		this.salesPrice = salesPrice;
	}

	public String getSetteleRate() {
		return setteleRate;
	}

	public void setSetteleRate(String setteleRate) {
		this.setteleRate = setteleRate;
	}

	public String getStockCnt() {
		return stockCnt;
	}

	public void setStockCnt(String stockCnt) {
		this.stockCnt = stockCnt;
	}

	public String getDeliveryOption() {
		return deliveryOption;
	}

	public void setDeliveryOption(String deliveryOption) {
		this.deliveryOption = deliveryOption;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public String getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(String updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

	public String getDeletedYn() {
		return deletedYn;
	}

	public void setDeletedYn(String deletedYn) {
		this.deletedYn = deletedYn;
	}

	public String getSearchGubun() {
		return searchGubun;
	}

	public void setSearchGubun(String searchGubun) {
		this.searchGubun = searchGubun;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getCurPage() {
		return curPage;
	}

	public void setCurPage(String curPage) {
		this.curPage = curPage;
	}

	public String getRowCount() {
		return rowCount;
	}

	public void setRowCount(String rowCount) {
		this.rowCount = rowCount;
	}

	public String getPage_limit_val1() {
		return page_limit_val1;
	}

	public void setPage_limit_val1(String page_limit_val1) {
		this.page_limit_val1 = page_limit_val1;
	}

	public String getPage_limit_val2() {
		return page_limit_val2;
	}

	public void setPage_limit_val2(String page_limit_val2) {
		this.page_limit_val2 = page_limit_val2;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getOptionKey() {
		return optionKey;
	}

	public void setOptionKey(String optionKey) {
		this.optionKey = optionKey;
	}

}
