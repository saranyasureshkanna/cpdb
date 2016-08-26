package com.kohler.persistence.domain;

import java.util.Date;

public class ItemLinks {
	//Item_Id in Item_Link
	private Long parentId;
	//Linked_Item_Id/cs_item_id in Item_Link
	private Long itemId;
	private String itemLinkTypeId;
	private String crSellItemId;
	private String createdBy;
	private String displayOrder;
	private String itemLinkText;
	private String itemStyleId;	
	private Date createdDate;	
	private String updatedBy;
	private Date updatedDate;
	private String languageCode;
	
	private String priority;
	private String crSellingTxt;
	
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getCrSellingTxt() {
		return crSellingTxt;
	}
	public void setCrSellingTxt(String crSellingTxt) {
		this.crSellingTxt = crSellingTxt;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
		public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getItemLinkTypeId() {
		return itemLinkTypeId;
	}
	public void setItemLinkTypeId(String itemLinkTypeId) {
		this.itemLinkTypeId = itemLinkTypeId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getItemLinkText() {
		return itemLinkText;
	}
	public void setItemLinkText(String itemLinkText) {
		this.itemLinkText = itemLinkText;
	}
	public String getItemStyleId() {
		return itemStyleId;
	}
	public void setItemStyleId(String itemStyleId) {
		this.itemStyleId = itemStyleId;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getCrSellItemId() {
		return crSellItemId;
	}
	public void setCrSellItemId(String crSellItemId) {
		this.crSellItemId = crSellItemId;
	}

}
