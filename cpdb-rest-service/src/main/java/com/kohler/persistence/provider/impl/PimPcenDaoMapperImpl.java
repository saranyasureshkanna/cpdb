package com.kohler.persistence.provider.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.kohler.persistence.domain.ItemGroups;
import com.kohler.persistence.domain.ItemInfo;
import com.kohler.persistence.domain.ItemLinkTypes;
import com.kohler.persistence.domain.ItemLinks;
import com.kohler.persistence.provider.PimDaoMapper;
import com.kohler.puni.persistence.dao.ItemGroupsPcenDao;
import com.kohler.puni.persistence.dao.ItemInfoDao;


public class PimPcenDaoMapperImpl implements PimDaoMapper {
	private static final Logger log = Logger.getLogger(PimPcenDaoMapperImpl.class);
	@Autowired
	private ItemGroupsPcenDao itemGroupsPcenDao;
	@Autowired
	private SqlSession sqlSessionTemplateSql;
	
	@Autowired
	private ItemInfoDao itemInfoDao;

	// Use DAOs from pcenschema mapped to pcenschema
	@SuppressWarnings("unused")
	@Transactional("pcen")
	public void insertOrUpdateItemObjects(Object itemObjs) {
		System.out.println("PimPcenDaoMapperImpl insertOrUpdateItemObjects ");
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (itemObjs instanceof ItemGroups) {
			//List<ItemGroups> itemgrpList = sqlSessionTemplateSql
			//		.selectList(
			//				"com.kohler.puni.persistence.dao.ItemGroupsPcenDao.getItemGroupByParentId",
			//				itemObjs);
			sqlSessionTemplateSql
					.insert("com.kohler.puni.persistence.dao.ItemGroupsPcenDao.persistItemGroups",
							itemObjs);

		} 	else if(itemObjs instanceof ItemLinks) {			
			sqlSessionTemplateSql
			.insert("com.kohler.puni.persistence.dao.ItemLinksPcenDao.persistItemLinks",
					itemObjs);

			
		}	else if (itemObjs instanceof ItemInfo) {
			List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
			System.out.println("Executing getItemInfoByName"+((ItemInfo) itemObjs).getItemName());
			itemInfoList = sqlSessionTemplateSql
					.selectList(
							"com.kohler.puni.persistence.dao.ItemGroupsPcenDao.getItemInfoByName",
							itemObjs);
			System.out.println("List size"+itemInfoList.size());
			if (itemInfoList != null && itemInfoList.size() > 0) {
				System.out.println("Updating itemInfoList pcen"+itemInfoList.size());
				sqlSessionTemplateSql
						.update("com.kohler.puni.persistence.dao.ItemGroupsPcenDao.updateItemInfo",
								itemObjs);
			} else {
				System.out.println("Inserting itemInfoList pcen"+itemInfoList.size());
				sqlSessionTemplateSql
						.insert("com.kohler.puni.persistence.dao.ItemGroupsPcenDao.persistItemInfo",
								itemObjs);
			}
		}
	}

	public int deleteItemHierarchies(Object itemObjs) {
		System.out.println("PimPcenDaoMapperImpl DeleteItemObjects ");
		return sqlSessionTemplateSql
				.delete("com.kohler.puni.persistence.dao.ItemGroupsPcenDao.deletehierarchy",
						itemObjs);
	}
	
	public List<ItemGroups> getParentItemGroupList(ItemGroups itemGrpObj) {
		List itemGroupsList = sqlSessionTemplateSql
		.selectList(
				"com.kohler.puni.persistence.dao.ItemGroupsPcenDao.getItemGroupByParentId",
				itemGrpObj);
		return itemGroupsList;
	}

	public ItemInfo getItemInfoByName(ItemInfo itemInfo) {
		// TODO Auto-generated method stub
		System.out.println("getItemInfoByName"+itemInfo.getItemName());
		List<ItemInfo> itemInfoList = sqlSessionTemplateSql
		.selectList(
				"com.kohler.puni.persistence.dao.ItemGroupsPcenDao.getItemInfoByName",
				itemInfo);
		return itemInfoList.get(0);
	}
	
	public List<ItemLinks> getParentItemLinkList(ItemLinks itemLnkObj) {
		
		List<ItemLinks> itemLnksList = sqlSessionTemplateSql
				.selectList("com.kohler.puni.persistence.dao.ItemLinksPcenDao.getItemLinksByParentnChildId", itemLnkObj);
		return itemLnksList;
	}
	
	public List<ItemLinks> getItemLinkListByParentId(ItemLinks itemLnkObj) {
		
		List<ItemLinks> itemLnksList = sqlSessionTemplateSql
				.selectList("com.kohler.puni.persistence.dao.ItemLinksPcenDao.getItemLinkListByParentId", itemLnkObj);
		return itemLnksList;
	}
	
	public int deleteHierarchyLink(Object itemObjs) {
		
		return sqlSessionTemplateSql
				.delete("com.kohler.puni.persistence.dao.ItemLinksPcenDao.deleteHierarchyLink",
						itemObjs);
	}
	
	public ItemLinkTypes getItemLinkIdByLinkTypUc(ItemLinkTypes itemLinkType) {
		
		List<ItemLinkTypes> itemLinkTypeList= sqlSessionTemplateSql
				.selectList(
						"com.kohler.puni.persistence.dao.ItemLinksPuniDao.getItemLinkIdByLinkTypUc",
						itemLinkType);
		return itemLinkTypeList.get(0);
	}
	
	
	public List<ItemLinks> getItemLinksByCSellRelation(ItemLinks itemLnk) {
		return sqlSessionTemplateSql
				.selectList(
						"com.kohler.puni.persistence.dao.ItemLinksPuniDao.getItemLinksByCSellRelation",
						itemLnk);
	}

	public int deleteItemLinksByParent(Object itemLinkObjs) {
		return sqlSessionTemplateSql
				.delete("com.kohler.puni.persistence.dao.ItemLinksPuniDao.deleteItemLinksByParent",
						itemLinkObjs);
	}
}
