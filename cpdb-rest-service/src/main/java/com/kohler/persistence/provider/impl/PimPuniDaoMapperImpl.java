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
import com.kohler.puni.persistence.dao.ItemGroupsDao;

public  class PimPuniDaoMapperImpl implements PimDaoMapper {
	private static final Logger log = Logger.getLogger(PimPuniDaoMapperImpl.class);
	@Autowired
	private ItemGroupsDao itemGroupsDao;
	@Autowired
	private SqlSession sqlSessionTemplate;
	
	
	@Transactional("puni")
	public void insertOrUpdateItemObjects(Object itemObjs) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		System.out.println("PimPuniDaoMapperImpl insertOrUpdateItemObjects ");

		if (itemObjs instanceof ItemGroups) {
			//List<ItemGroups> itemgrpList = sqlSessionTemplate
			//		.selectList(
			//				"com.kohler.puni.persistence.dao.ItemGroupsDao.getItemGroupByParentId",
			//				itemObjs);
			sqlSessionTemplate
					.insert("com.kohler.puni.persistence.dao.ItemGroupsDao.persistItemGroups",
							itemObjs);
		} else if(itemObjs instanceof ItemLinks) {
			System.out.println("PimPuniDaoMapperImpl insertOrUpdateItemObjects ");
			sqlSessionTemplate
			.insert("com.kohler.puni.persistence.dao.ItemLinksPuniDao.persistItemLinks",
					itemObjs);

			
		} else if(itemObjs instanceof ItemInfo) {
			List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
			itemInfoList=	sqlSessionTemplate
					.selectList(
							"com.kohler.puni.persistence.dao.ItemGroupsDao.getItemInfoByName",
							itemObjs);
			if (itemInfoList != null && itemInfoList.size() > 0 &&  itemInfoList.get(0).getItemId() != null ) {
				System.out.println("Updating item iteminfo puni"+ itemInfoList.get(0).getItemId());
				sqlSessionTemplate
						.update("com.kohler.puni.persistence.dao.ItemGroupsDao.updateItemInfo",
								itemObjs);
			} else {
				System.out.println("Inserting item iteminfo"+((ItemInfo) itemObjs).getItemName());
				sqlSessionTemplate
						.insert("com.kohler.puni.persistence.dao.ItemGroupsDao.persistItemInfo",
								itemObjs);
			}
		}

	}

	public int deleteItemHierarchies(Object itemObjs) {
		return sqlSessionTemplate
				.delete("com.kohler.puni.persistence.dao.ItemGroupsDao.deletehierarchy",
						itemObjs);
	}

	public List<ItemGroups> getParentItemGroupList(ItemGroups itemGrp) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate
		.selectList(
				"com.kohler.puni.persistence.dao.ItemGroupsDao.getItemGroupByParentId",
				itemGrp);
	}

	public ItemInfo getItemInfoByName(ItemInfo itemInfo) {
		// TODO Auto-generated method stub
		List<ItemInfo> itemInfoList= sqlSessionTemplate
				.selectList(
						"com.kohler.puni.persistence.dao.ItemGroupsDao.getItemInfoByName",
						itemInfo);
		return itemInfoList.get(0);
	}
	
	public List<ItemLinks> getParentItemLinkList(ItemLinks itemLnk) {		
		return sqlSessionTemplate
		.selectList(
				"com.kohler.puni.persistence.dao.ItemLinksPuniDao.getItemLinksByParentnChildId",
				itemLnk);
	}
	
	public List<ItemLinks> getItemLinkListByParentId(ItemLinks itemLnk) {		
		return sqlSessionTemplate
		.selectList(
				"com.kohler.puni.persistence.dao.ItemLinksPuniDao.getItemLinkListByParentId",
				itemLnk);
	}	

	public int deleteHierarchyLink(Object itemLinkObjs) {
		return sqlSessionTemplate
				.delete("com.kohler.puni.persistence.dao.ItemLinksPuniDao.deleteHierarchyLink",
						itemLinkObjs);
	}
	
	public ItemLinkTypes getItemLinkIdByLinkTypUc(ItemLinkTypes itemLinkType) {
		
		List<ItemLinkTypes> itemLinkTypeList= sqlSessionTemplate
				.selectList(
						"com.kohler.puni.persistence.dao.ItemLinksPuniDao.getItemLinkIdByLinkTypUc",
						itemLinkType);
		return itemLinkTypeList.get(0);
	}
	
	
	public List<ItemLinks> getItemLinksByCSellRelation(ItemLinks itemLnk) {
		return sqlSessionTemplate
				.selectList(
						"com.kohler.puni.persistence.dao.ItemLinksPuniDao.getItemLinksByCSellRelation",
						itemLnk);
	}
	
	public int deleteItemLinksByParent(Object itemLinkObjs) {
		return sqlSessionTemplate
				.delete("com.kohler.puni.persistence.dao.ItemLinksPuniDao.deleteItemLinksByParent",
						itemLinkObjs);
	}
		
	

}
