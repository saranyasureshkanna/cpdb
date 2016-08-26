package com.kohler.puni.persistence.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.kohler.persistence.Util.PimUtil;
import com.kohler.persistence.Util.PimUtil.Attributes;
import com.kohler.persistence.Util.PimUtil.Messages;
import com.kohler.persistence.domain.ItemGroups;
import com.kohler.persistence.domain.ItemInfo;
import com.kohler.persistence.domain.ItemLinkTypes;
import com.kohler.persistence.domain.ItemLinks;
import com.kohler.persistence.provider.PimDaoMapper;
import com.kohler.persistence.provider.PimDaoProvider;
import com.kohler.puni.persistence.dao.ItemGroupsDao;
import com.kohler.puni.persistence.dao.ItemInfoDao;
import com.kohler.puni.persistence.validations.PIMValidations;

public class PimServiceImpl {
	private static final Logger log = Logger.getLogger(PimServiceImpl.class);

	@Autowired
	private ItemGroupsDao itemGroupsDao;

	@Autowired
	private ItemInfoDao itemInfoDao;

	@Autowired
	private PimDaoProvider pimDaoProvider;

	private static Random rn = new Random(2000);

	/**
	 * 
	 * @param requestJSON
	 * @param schemaName
	 * @return
	 */
	// This method inserts into item hierarchies
	public String processItemHierarchiesJSON(JSONObject requestJSON, String schemaName) {
		// System.out.println("processRequestJSONItems");
		ItemGroups itemGroups = null;
		ItemLinks itemLinks = null;
		ItemInfo itemInfos = null;
		JSONObject outputJSON = new JSONObject();
		JSONObject ancestorOutputJSON = null, parentOutputJson = null, childOutputJSON = null, actionOutputJSON = null;
		PimDaoMapper pimDaoMapper = pimDaoProvider.get(schemaName);
		String statusMessage = null, statusLevel = null;
		try {
			log.info("Request JSON Item Groups" + requestJSON.toString());
			for (Iterator keys = requestJSON.keys(); keys.hasNext();) {
				// getting the action
				String action = (String) keys.next();
				JSONObject actionJSON = (JSONObject) requestJSON.getJSONObject(action);
				parentOutputJson = new JSONObject();
				// getting the parent
				for (Iterator parents = actionJSON.keys(); parents.hasNext();) {
					try {
						String parentName = (String) parents.next();
						ItemInfo itemInfo = new ItemInfo();
						itemInfo.setItemName(parentName);
						itemInfo = pimDaoMapper.getItemInfoByName(itemInfo);
						if (itemInfo.getItemId() == null) {
							throw new Exception(parentName + " " + Messages.ITEMNOTFOUND.getMessage());
						}

						JSONObject parentJSON = (JSONObject) actionJSON.getJSONObject((String) parentName);

						JSONArray ancestors = (JSONArray) parentJSON.get(Attributes.ancestors);
						ancestorOutputJSON = new JSONObject();
						childOutputJSON = new JSONObject();

						for (int i = 0; i < ancestors.length(); i++) {
							itemGroups = new ItemGroups();
							itemGroups.setParentId(itemInfo.getItemId());
							itemLinks = new ItemLinks();
							itemLinks.setParentId(itemInfo.getItemId());
							JSONObject childJSONObj = (JSONObject) ancestors.getJSONObject(i);
							JSONObject innerchildOutputJSON = new JSONObject();
							// childJSONObj.getString(Attributes.itemNo)
							ItemInfo itemInfoChild = new ItemInfo();

							try {
								String childItemName = childJSONObj.getString(Attributes.itemNo);
								itemInfoChild.setItemName(childItemName);
								itemInfoChild = pimDaoMapper.getItemInfoByName(itemInfoChild);

								if (itemInfoChild.getItemId() == null) {
									throw new Exception(childItemName + " " + Messages.ITEMNOTFOUND.getMessage());
								}

								itemGroups.setItemId(itemInfoChild.getItemId());
								itemGroups.setLanguageCode(Attributes.languageCodeEN);
								itemGroups.setItemKind(childJSONObj.getString(Attributes.itemType));
								List<ItemGroups> itemgrpList = pimDaoMapper.getParentItemGroupList(itemGroups);
								// To insert Item Links
								itemLinks.setItemId(itemInfoChild.getItemId());
								itemLinks.setItemLinkTypeId(Attributes.itemLinkTypeId);
								itemLinks.setLanguageCode(Attributes.languageCodeEN);
								itemLinks.setCreatedDate(new Date());
								itemLinks.setUpdatedDate(new Date());
								itemLinks.setPriority(Attributes.priority);
								itemLinks.setCrSellItemId(Attributes.crSellItemId);
								List<ItemLinks> itemLnkList = pimDaoMapper.getParentItemLinkList(itemLinks);
								if (itemLnkList != null && itemLnkList.size() > 0) {

									pimDaoMapper.deleteHierarchyLink(itemLinks);

								}

								log.info("insertOrUpdateItemObjects item links started:" + itemLinks.getItemLinkTypeId()
										+ ":" + itemLinks.getItemId());
								pimDaoMapper.insertOrUpdateItemObjects(itemLinks);
								// To insert Item Groups
								if (itemgrpList != null && itemgrpList.size() > 0) {
									pimDaoMapper.deleteItemHierarchies(itemGroups);
								}

								pimDaoMapper.insertOrUpdateItemObjects(itemGroups);

								statusMessage = PimUtil.Messages.SUCCESSMESSAGE.getMessage() + "ItemHierarchies "
										+ itemGroups.getItemId();
								statusLevel = Attributes.statusSuccess;

							} catch (Exception insException) {
								log.error("Exception while insert/update item groups" + insException
										+ itemGroups.getItemId());
								statusMessage = PimUtil.Messages.ERROR.getMessage() + insException.getMessage()
										+ itemGroups.getItemId();
								statusLevel = Attributes.statusError;
							}
							// getting children
							innerchildOutputJSON.put(Attributes.statusLevel, statusLevel);
							innerchildOutputJSON.put(Attributes.statusMessage, statusMessage);
							innerchildOutputJSON.put(Attributes.itemType, itemGroups.getItemKind());
							// childOutputJSON.accumulate(itemGroups.getItemId(),
							// innerchildOutputJSON);
							childOutputJSON.put(childJSONObj.getString(Attributes.itemNo), innerchildOutputJSON);

						}

						ancestorOutputJSON.put(Attributes.ancestors, childOutputJSON);
						parentOutputJson.put(parentName, ancestorOutputJSON);
					} catch (Exception ex) {
						log.error("Exception while insert/update item groups" + ex + itemGroups.getItemId());
						statusMessage = PimUtil.Messages.ERROR.getMessage() + ex.getMessage() + itemGroups.getItemId();
						statusLevel = Attributes.statusError;
					}

				}
				// sqlSessionTemplate.flushStatements();
				actionOutputJSON = new JSONObject();
				actionOutputJSON.put(action, parentOutputJson);
				outputJSON.accumulate("response", actionOutputJSON);
			}

		} catch (Exception ex) {
			log.error("exception in processItemHierarchiesJSON" + ex.getMessage());
			return outputJSON.toString();
		}
		log.info("Output json for item hierarchies" + outputJSON);
		return outputJSON.toString();

	}

	/**
	 * 
	 * @param requestJSON
	 * @param schemaName
	 * @return
	 */
	public String processItemsJSON(JSONObject requestJSON, String schemaName) {
		// System.out.println("processRequestJSONItems");
		ItemGroups itemGroups = null;
		ItemInfo itemInfo = null;
		JSONObject outputJSON = new JSONObject();
		JSONObject actionOutputJSON = null, childOutputJSON = null;
		String statusMessage = null, statusLevel = null;
		PimDaoMapper pimDaoMapper = pimDaoProvider.get(schemaName);
		log.info("Request json when processing itemInfo" + requestJSON);
		for (Iterator keys = requestJSON.keys(); keys.hasNext();) {
			// getting the action
			String action = (String) keys.next();
			JSONObject itemsListJSON = (JSONObject) requestJSON.getJSONObject(action);
			JSONObject innerchildOutputJSON = null;
			childOutputJSON = new JSONObject();
			// getting the parent
			for (Iterator items = itemsListJSON.keys(); items.hasNext();) {
				innerchildOutputJSON = new JSONObject();
				String itemSeq = (String) items.next();
				// itemGroups.setParentId((parentId));
				JSONObject itemJSONObj = (JSONObject) itemsListJSON.getJSONObject((String) itemSeq);
				itemInfo = new ItemInfo();
				actionOutputJSON = new JSONObject();

				try {
					itemInfo.setItemId(rn.nextLong());
					itemInfo.setStatusId(itemJSONObj.getString(Attributes.status));
					itemInfo.setItemName((itemJSONObj.getString(Attributes.itemNo)).toString());
					itemInfo.setItemKind(itemJSONObj.getString(Attributes.itemType));
					itemInfo.setKeepWith(Attributes.keepWith);
					itemInfo.setDefaultItemStyleId(Attributes.defaultStyleItemId);
					if (PIMValidations.isChildJSONValid(itemJSONObj)) {
						pimDaoMapper.insertOrUpdateItemObjects(itemInfo);
						statusMessage = PimUtil.Messages.SUCCESSMESSAGE.getMessage() + itemInfo.getItemName();
						statusLevel = Attributes.statusSuccess;
					} else {
						statusMessage = PimUtil.Messages.VALIDATIONFAILED.getMessage() + itemInfo.getItemName();
						statusLevel = Attributes.statusError;
					}
				} catch (Exception insException) {
					log.error("Exception while insert/update iteminfo" + insException + itemInfo.getItemName());
					statusMessage = PimUtil.Messages.ERROR.getMessage() + insException.getMessage()
							+ itemInfo.getItemName();
					statusLevel = Attributes.statusError;
				}
				innerchildOutputJSON.put(Attributes.statusLevel, statusLevel);
				innerchildOutputJSON.put(Attributes.statusMessage, statusMessage);
				innerchildOutputJSON.put(Attributes.itemNo, itemInfo.getItemName());
				innerchildOutputJSON.put(Attributes.itemType, itemInfo.getItemKind());
				// childOutputJSON.accumulate(childId, innerchildOutputJSON);
				childOutputJSON.put(itemSeq, innerchildOutputJSON);
			}

			actionOutputJSON.put(action, childOutputJSON);
		}

		outputJSON.accumulate("response", actionOutputJSON);
		log.info("Process Item Info outputJSON" + outputJSON);

		return outputJSON.toString();

	}

}