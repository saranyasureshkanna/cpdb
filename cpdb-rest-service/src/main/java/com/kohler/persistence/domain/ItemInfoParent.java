package com.kohler.persistence.domain;

import java.util.List;

public class ItemInfoParent {
	private String parentName;
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	private List<ItemLinks> itemLinks;

	public List<ItemLinks> getItemLinks() {
		return itemLinks;
	}

	public void setItemLinks(List<ItemLinks> itemLinks) {
		this.itemLinks = itemLinks;
	}
}
