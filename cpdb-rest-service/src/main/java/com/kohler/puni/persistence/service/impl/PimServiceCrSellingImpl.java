package com.kohler.puni.persistence.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.kohler.persistence.Util.PimUtil;
import com.kohler.persistence.Util.PimUtil.Attributes;
import com.kohler.persistence.domain.ItemInfo;
import com.kohler.persistence.domain.ItemLinkTypes;
import com.kohler.persistence.domain.ItemLinks;
import com.kohler.persistence.domain.json.Action;
import com.kohler.persistence.domain.json.ActionType;
import com.kohler.persistence.domain.json.CrossSell;
import com.kohler.persistence.domain.json.Item;
import com.kohler.persistence.domain.json.Product;
import com.kohler.persistence.domain.json.Schema;
import com.kohler.persistence.provider.PimDaoMapper;
import com.kohler.persistence.provider.PimDaoProvider;

public class PimServiceCrSellingImpl {
	private static final Logger log = Logger
			.getLogger(PimServiceCrSellingImpl.class);

	@Autowired
	private PimDaoProvider pimDaoProvider;

	private Schema getSchema(JSONObject jo) {

		List<Action> actions = new ArrayList<Action>();

		Item itemAdd = new Item("", "1251K");
		CrossSell csAdd = new CrossSell("", "AvailableFinish");
		Product productAdd = new Product("", "6955C");

		List<Item> items = new ArrayList<Item>();
		items.add(itemAdd);

		List<CrossSell> css = new ArrayList<CrossSell>();
		csAdd.setItems(items);
		css.add(csAdd);
		productAdd.setList(css);

		List<Product> productsAdd = new ArrayList<Product>();
		productsAdd.add(productAdd);

		Action actionAdd = new Action(ActionType.ADD);
		actionAdd.setProducts(productsAdd);

		Item itemMod1 = new Item("", "1252K");
		Item itemMod2 = new Item("", "1251K");
		
		CrossSell csMod = new CrossSell("", "AvailableFinish");
		Product productMod = new Product("", "6959C");

		List<Item> itemsMod = new ArrayList<Item>();
		itemsMod.add(itemMod1);
		itemsMod.add(itemMod2);
		

		List<CrossSell> cssMod = new ArrayList<CrossSell>();
		csMod.setItems(itemsMod);
		cssMod.add(csMod);
		productMod.setList(cssMod);

		List<Product> productsMod = new ArrayList<Product>();
		productsMod.add(productMod);

		Action actionMod = new Action(ActionType.MODIFY);
		actionMod.setProducts(productsMod);

		actions.add(actionAdd);
		actions.add(actionMod);

		Schema schema = new Schema();
		schema.setActions(actions);

		return schema;
	}

	// TO DO: Testing inprogress for pds14
	public String processCSItemsJSON(JSONObject requestJSON, String schemaName) {
		JSONObject outputJSON = new JSONObject();
		JSONObject actionOutputJSON = null;
		JSONObject ancestorOutputJSON = null, parentOutputJson = null, childOutputJSON = null, crossSellTypeJSON = null, crossSellOutputJSON = null;
		log.info("Request json when processing itemInfo" + requestJSON);
		LinkedHashMap<String, Object> parentsMap = new LinkedHashMap<>();
		LinkedHashMap<String, Object> actionMap = new LinkedHashMap<>();
		Iterator<Entry<String, Object>> itEntry;
		List<String> csMap = new ArrayList<>();
		// PimDaoMapper pimDaoMapper = pimDaoProvider.get(schemaName);
		String statusMessage = null, statusLevel = null;

		ItemInfo itemInfoParent = null;
		ItemLinkTypes itemLinkTypes = null;
		ItemInfo itemInfoChild = null;
		ItemLinks itemLinks = null;

		boolean parentExist = true;
		boolean childExist = true;
		boolean csTypeExist = true;

		try {

			System.out.println("inside try");

			Schema schema = getSchema(new JSONObject());

			for (Action action : schema.getActions()) {

				List<Product> products = new ArrayList<Product>();

				for (Product pro : products) {
					itemInfoParent = new ItemInfo();
					itemInfoParent.setItemName(pro.getName());
					// itemInfoParent =
					// pimDaoMapper.getItemInfoByName(itemInfoParent);
					if (itemInfoParent == null) {
						parentExist = false;
						// throw new Exception(parentName + " " +
						// Messages.ITEMNOTFOUND.getMessage());
					}

					for (CrossSell csObj : pro.getList()) {

						itemLinkTypes = new ItemLinkTypes();
						itemLinkTypes.setItemLinkTypeUc(csObj.getName());
						// itemLinkTypes =
						// pimDaoMapper.getItemLinkIdByLinkTypUc(itemLinkTypes);
						if (itemLinkTypes == null) {
							// csTypeExist = false;
							// throw new Exception(csTypeDesc + " " +
							// Messages.RELATIONDOEANOTEXIST.getMessage());
						}

						List<Item> childList = csObj.getItems();

						for (Item i : childList) {

							String itemName = i.getName();
							itemInfoChild = new ItemInfo();
							itemInfoChild.setItemName(itemName);
							// itemInfoChild =
							// pimDaoMapper.getItemInfoByName(itemInfoChild);
							if (itemInfoChild == null) {
								childExist = false;
								// throw new Exception(itemName + " " +
								// Messages.ITEMNOTFOUND.getMessage());
							}
							try {

								itemLinks = new ItemLinks();
								List<ItemLinks> itemLinkList = new ArrayList<>();
								itemLinks.setItemId(itemInfoChild.getItemId());
								itemLinks.setParentId(itemInfoParent
										.getItemId());
								itemLinks.setItemLinkTypeId(itemLinkTypes
										.getItemLinkTypeId());

								itemLinks
										.setLanguageCode(Attributes.languageCodeEN);
								itemLinks.setCreatedDate(new Date());
								itemLinks.setUpdatedDate(new Date());
								itemLinks.setPriority(Attributes.priority);

								if (action.getAction() == ActionType.DELETE) {
									itemLinks = deleteCrossSellRelation(itemLinks);
									if (parentExist && childExist
											&& csTypeExist) {
										// itemLinkList =
										// pimDaoMapper.getItemLinksByCSellRelation(itemLinks);
										if (itemLinkList.isEmpty()) {
											statusMessage = PimUtil.Messages.WARNING
													.getMessage()
													+ itemInfoParent
															.getItemName()
													+ ":"
													+ itemInfoChild
															.getItemName();
											statusLevel = Attributes.statusWarning;
										} else {
											// pimDaoMapper.deleteHierarchyLink(itemLinks);
											statusMessage = PimUtil.Messages.SUCCESSMESSAGE
													.getMessage()
													+ itemInfoParent
															.getItemName()
													+ ":"
													+ itemInfoChild
															.getItemName();
											statusLevel = Attributes.statusSuccess;
										}

									} else {
										statusMessage = PimUtil.Messages.VALIDATIONFAILED
												.getMessage()
												+ itemInfoParent.getItemName()
												+ ":"
												+ itemInfoChild.getItemName();
										statusLevel = Attributes.statusError;
									}

								} else if (action.getAction() == ActionType.ADD) {
									if (parentExist && childExist
											&& csTypeExist) {
										// itemLinkList =
										// pimDaoMapper.getItemLinksByCSellRelation(itemLinks);

										if (!itemLinkList.isEmpty()) {
											// pimDaoMapper.deleteHierarchyLink(itemLinks);
										}

										log.info("insertOrUpdateItemObjects item links started:"
												+ itemLinks.getItemLinkTypeId()
												+ ":" + itemLinks.getItemId());
										// pimDaoMapper.insertOrUpdateItemObjects(itemLinks);
										statusMessage = PimUtil.Messages.SUCCESSMESSAGE
												.getMessage()
												+ itemInfoParent.getItemName()
												+ ":"
												+ itemInfoChild.getItemName();
										statusLevel = Attributes.statusSuccess;
									} else {
										statusMessage = PimUtil.Messages.VALIDATIONFAILED
												.getMessage()
												+ itemInfoParent.getItemName()
												+ ":"
												+ itemInfoChild.getItemName();
										statusLevel = Attributes.statusError;
									}

								} else if (action.getAction() == ActionType.ADD) {
									if (parentExist && childExist
											&& csTypeExist) {
										// itemLinkList =
										// pimDaoMapper.getItemLinkListByParentId(itemLinks);
										if (!itemLinkList.isEmpty()) {

											// pimDaoMapper.deleteItemLinksByParent(itemLinks);

										}

										log.info("insertOrUpdateItemObjects item links started:"
												+ itemLinks.getItemLinkTypeId()
												+ ":" + itemLinks.getItemId());
										// pimDaoMapper.insertOrUpdateItemObjects(itemLinks);

										statusMessage = PimUtil.Messages.SUCCESSMESSAGE
												.getMessage()
												+ itemInfoParent.getItemName()
												+ ":"
												+ itemInfoChild.getItemName();
										statusLevel = Attributes.statusSuccess;
									} else {
										statusMessage = PimUtil.Messages.VALIDATIONFAILED
												.getMessage()
												+ itemInfoParent.getItemName()
												+ ":"
												+ itemInfoChild.getItemName();
										statusLevel = Attributes.statusError;
									}

								}

							} catch (Exception insException) {
								log.error("Exception while insert/update item groups"
										+ insException
										+ itemInfoChild.getItemName());
								statusMessage = PimUtil.Messages.ERROR
										.getMessage()
										+ insException.getMessage()
										+ itemInfoChild.getItemName();
								statusLevel = Attributes.statusError;
							}

						}
					}

				}
			}

		} catch (Exception insException) {
			log.error("Exception while insert/update item groups"
					+ insException + itemInfoParent.getItemName());
			statusMessage = PimUtil.Messages.ERROR.getMessage()
					+ insException.getMessage() + itemInfoParent.getItemName();
			statusLevel = Attributes.statusError;
		}
		return outputJSON.toString();
	}

	public ItemLinks deleteCrossSellRelation(ItemLinks itemLinks) {

		return itemLinks;

	}

	public ItemLinks addCrossSellRelation(ItemLinks itemLinks) {

		return itemLinks;

	}

	public ItemLinks modifyCrossSellRelation(ItemLinks itemLinks) {

		return itemLinks;

	}

}
