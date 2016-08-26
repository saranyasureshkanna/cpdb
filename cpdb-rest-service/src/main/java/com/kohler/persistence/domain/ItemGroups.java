package com.kohler.persistence.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemGroups {
	/*
	 * `Parent_ID` int(11) NOT NULL,
  `Item_ID` int(11) DEFAULT NULL,
  `Item_Kind` varchar(45) DEFAULT NULL,
  `Created_By` varchar(45) DEFAULT NULL,
  `Created_Date` datetime DEFAULT NULL,
  `Updated_By` varchar(45) DEFAULT NULL,
  `Updated_Date` datetime DEFAULT NULL,
  `Language_Code` varchar(45) DEFAULT NULL
	 */
	
	Long parentId;
	Long itemId;
	String itemKind;
	String createdBy;
	Date createdDate = new Date();
	String updatedBy="null";
	Date updatedDate = new Date();
	String languageCode;
	List<ItemInfo> itemInfoList= new ArrayList();
	
	public List<ItemInfo> getItemInfoList() {
		return itemInfoList;
	}
	public void setItemInfoList(List<ItemInfo> itemInfoList) {
		this.itemInfoList = itemInfoList;
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
	
	public String getItemKind() {
		return itemKind;
	}
	public void setItemKind(String itemKind) {
		this.itemKind = itemKind;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	
}
