package com.kohler.persistence.provider;

import java.util.List;

import com.kohler.persistence.domain.ItemGroups;
import com.kohler.persistence.domain.ItemInfo;
import com.kohler.persistence.domain.ItemLinkTypes;
import com.kohler.persistence.domain.ItemLinks;

public interface PimDaoMapper {
	
	public void insertOrUpdateItemObjects(Object itemObjs);
	
	public int deleteItemHierarchies(Object itemObjs);
	
	public List<ItemGroups> getParentItemGroupList(ItemGroups itemGroups);
	
	public ItemInfo getItemInfoByName(ItemInfo itemInfo);
	
	public int deleteHierarchyLink(Object itemObjs);
	
	public List<ItemLinks> getParentItemLinkList(ItemLinks itemLinks);
	
	public List<ItemLinks> getItemLinkListByParentId(ItemLinks itemLinks);
	
	public ItemLinkTypes getItemLinkIdByLinkTypUc(ItemLinkTypes itemLinkTypes);
	
	public List<ItemLinks> getItemLinksByCSellRelation(ItemLinks itemLinks);
	
	public int deleteItemLinksByParent(Object itemLinkObjs);
	
}
