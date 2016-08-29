package com.kohler.puni.persistence.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kohler.persistence.Util.PimUtil;
import com.kohler.persistence.Util.PimUtil.Attributes;
import com.kohler.persistence.domain.ItemInfo;
import com.kohler.persistence.domain.ItemInfoParent;
import com.kohler.persistence.domain.ItemLinkTypes;
import com.kohler.persistence.domain.ItemLinks;
import com.kohler.persistence.domain.json.Action;
import com.kohler.persistence.domain.json.ActionType;
import com.kohler.persistence.domain.json.CrossSell;
import com.kohler.persistence.domain.json.Item;
import com.kohler.persistence.domain.json.Product;
import com.kohler.persistence.domain.json.Schema;
import com.kohler.persistence.domain.json.StatusDetails;
import com.kohler.persistence.provider.PimDaoMapper;
import com.kohler.persistence.provider.PimDaoProvider;

public class PimServiceCrSellingImpl{
	
	private static final Logger log = Logger
			.getLogger(PimServiceCrSellingImpl.class);

	@Autowired
	private PimDaoProvider pimDaoProvider;

	private Schema getSchema(JSONObject jo,String SchemaName) {

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

		Schema schema = new Schema(SchemaName);
		schema.setActions(actions);

		return schema;
	}

	// TO DO: Testing inprogress for pds14
	public String processCSItemsJSON(JSONObject requestJSON, String schemaName) {
		
		
		PimDaoProvider pimDaoProvider = new PimDaoProvider();
		List<Action> actionList = new ArrayList<Action>();
		List<Product> itemParentList = new ArrayList<Product>();
		List<CrossSell> itemLnkList = new ArrayList<CrossSell>();
		List<Item> itemLnkTypeList = new ArrayList<Item>();
		List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
		List<StatusDetails> statDtlsList = new ArrayList<StatusDetails>();
		
		Schema outputSchema = new Schema();
		Action action = new Action();
		ItemInfoParent itemInfoParent = new ItemInfoParent();
		ItemLinks itemLinks = new ItemLinks();
		ItemLinkTypes itemlnkTyp = new ItemLinkTypes();
		ItemInfo itemInfo = new ItemInfo();
		ItemInfo parent = new ItemInfo();
		StatusDetails statDtls = new StatusDetails();
		
		List<Action> OutputActionList = new ArrayList<Action>();
		List<ItemInfoParent> OutputItemParentList = new ArrayList<ItemInfoParent>();
		List<ItemLinks> OutputItemLnkList = new ArrayList<ItemLinks>();
		List<ItemLinkTypes> OutputItemLnkTypeList = new ArrayList<ItemLinkTypes>();
		List<ItemInfo> OutputItemInfoList = new ArrayList<ItemInfo>();

		PimDaoMapper pimDaoMapper = pimDaoProvider.get(schemaName);
		
		boolean parentExist = true;
		boolean childExist = true;
		boolean csTypeExist = true;
		
		try {
			
			Schema schema = getSchema(requestJSON, schemaName);
			
			System.out.println("inside try");
			actionList = schema.getActions();
			for(Action eachAaction : actionList){
				System.out.println(eachAaction.getAction()+":");
				itemParentList = eachAaction.getProducts();
				action.setAction(eachAaction.getAction());
				for(Product eachParent : itemParentList){
					System.out.println(eachParent.getName()+":");
					itemLnkList = eachParent.getList();
					itemInfoParent.setParentName(eachParent.getName());
					parent.setItemName(eachParent.getName());
					System.out.println(eachParent.getName());
					parent = pimDaoMapper.getItemInfoByName(parent);
					System.out.println(parent+":");
					if (parent == null) {
						parentExist = false;
					//	throw new Exception(eachParent.getName() + " " + PimUtil.Messages.ITEMNOTFOUND.getMessage());
					}
					for(CrossSell eachItemLink : itemLnkList){
						System.out.println(eachItemLink.getName()+":");
						  //  itemLinks.setItemLinkText(eachItemLink.getName());
						    itemlnkTyp.setItemLinkTypeUc(eachItemLink.getName());
						    itemLnkTypeList = eachItemLink.getItems();
						    itemlnkTyp = pimDaoMapper.getItemLinkIdByLinkTypUc(itemlnkTyp);
						    
							if (itemlnkTyp == null) {
								csTypeExist = false;
						//		throw new Exception(eachItemLink.getName() + " " + PimUtil.Messages.RELATIONDOEANOTEXIST.getMessage());
							}

						    
						for(Item eachLinkType : itemLnkTypeList){
							itemInfo = new ItemInfo();
							
							itemInfo.setItemName(eachLinkType.getName());
									itemInfo = pimDaoMapper.getItemInfoByName(itemInfo);
									if (itemInfo == null) {
										childExist = false;
							//			throw new Exception(itemName + " " + Messages.ITEMNOTFOUND.getMessage());
									}
									
									if(parentExist && csTypeExist && childExist){										

										itemLinks.setItemId(itemInfo.getItemId());
										itemLinks.setParentId(parent.getItemId());
										itemLinks.setItemLinkTypeId(itemlnkTyp.getItemLinkTypeId());
										itemLinks.setLanguageCode(Attributes.languageCodeEN);
										itemLinks.setCreatedDate(new Date());
										itemLinks.setUpdatedDate(new Date());
										itemLinks.setPriority(Attributes.priority);
										
										System.out.println(itemLinks.toString());

											if(eachAaction.getAction().equals("D")){			
												
												itemInfo = deleteCrossSellRelation(itemLinks,pimDaoMapper);
												statDtls.setStatusMessage(PimUtil.Messages.SUCCESSDELETEMESSAGE.getMessage() + itemInfo.getItemName());
												
											}else if(eachAaction.getAction().equals("A")){
												itemInfo = addCrossSellRelation(itemLinks,pimDaoMapper);
												statDtls.setStatusMessage(PimUtil.Messages.SUCCESSMESSAGE.getMessage() + itemInfo.getItemName());
											}else if(eachAaction.getAction().equals("M")){
												itemInfo = modifyCrossSellRelation(itemLinks,pimDaoMapper);
												statDtls.setStatusMessage(PimUtil.Messages.SUCCESSMESSAGE.getMessage() + itemInfo.getItemName());
											}
											
									}else{
										statDtls.setStatusLevel(Attributes.statusError);
										statDtls.setStatusMessage(PimUtil.Messages.VALIDATIONFAILED.getMessage() + itemInfo.getItemName());
										statDtlsList.add(statDtls);
										itemInfo.setStatDetails(statDtlsList);
									}
									
									statDtls.setStatusLevel(Attributes.statusSuccess);										
									statDtlsList.add(statDtls);
									itemInfo.setStatDetails(statDtlsList);
									OutputItemInfoList.add(itemInfo);
									
					}
								itemlnkTyp.setItemInfoList(OutputItemInfoList);
								OutputItemLnkTypeList.add(itemlnkTyp);
				}
						//itemLinks.setCsTypesList(OutputItemLnkTypeList);
						OutputItemLnkList.add(itemLinks);
			}
					itemInfoParent.setItemLinks(OutputItemLnkList);
					OutputItemParentList.add(itemInfoParent);
		}
				//action.setItemInfoParent(OutputItemParentList);
				OutputActionList.add(action);
			}
			
			//outputSchema.setActionList(OutputActionList);
		
		
			catch(Exception insException){
				log.error("Exception while creating cross sell relation" + insException
						+ parent.getItemName());
				}
					
													
		return outputSchema.toString();
	}


	public ItemInfo deleteCrossSellRelation(ItemLinks itemLinks, PimDaoMapper pimDaoMapper) {
		ItemInfo itemInfo = new ItemInfo();
		StatusDetails statDtls = new StatusDetails();
		List<ItemLinks> itemLinkList = new ArrayList<ItemLinks>();
		List<StatusDetails> statDtlsList = new ArrayList<StatusDetails>();
		try{
			itemLinkList = pimDaoMapper.getItemLinksByCSellRelation(itemLinks);
			if (itemLinkList.isEmpty()) {
				statDtls.setStatusMessage(PimUtil.Messages.WARNING.getMessage());
				statDtls.setStatusLevel(Attributes.statusWarning);
				
			} else {
				pimDaoMapper.deleteHierarchyLink(itemLinks);
				statDtls.setStatusMessage(PimUtil.Messages.SUCCESSMESSAGE.getMessage());
				statDtls.setStatusLevel(Attributes.statusSuccess);
			}
			
		}catch(Exception ex){
			log.error("Exception while deleteCrossSellRelation" + ex);
			statDtls.setStatusMessage(PimUtil.Messages.ERRORINDELETE.getMessage());
			statDtls.setStatusLevel(Attributes.statusError);

		}
		
		statDtlsList.add(statDtls);
		itemInfo.setStatDetails(statDtlsList);
		
		return itemInfo;

	}

	public ItemInfo addCrossSellRelation(ItemLinks itemLinks,PimDaoMapper pimDaoMapper) {

		ItemInfo itemInfo = new ItemInfo();
		StatusDetails statDtls = new StatusDetails();
		List<ItemLinks> itemLinkList = new ArrayList<ItemLinks>();
		List<StatusDetails> statDtlsList = new ArrayList<StatusDetails>();
		try{
			
			itemLinkList = pimDaoMapper.getItemLinksByCSellRelation(itemLinks);											
			
			if (!itemLinkList.isEmpty()) {														
				pimDaoMapper.deleteHierarchyLink(itemLinks);														
			}
					
			log.info("insertOrUpdateItemObjects item links started:"+itemLinks.getItemLinkTypeId()+":"+itemLinks.getItemId());								
			pimDaoMapper.insertOrUpdateItemObjects(itemLinks);		
			statDtls.setStatusMessage(PimUtil.Messages.SUCCESSMESSAGE.getMessage());
			statDtls.setStatusLevel(Attributes.statusSuccess);			
			
		}catch(Exception ex){
			log.error("Exception while deleteCrossSellRelation" + ex);
			statDtls.setStatusMessage(PimUtil.Messages.ERRORINDELETE.getMessage());
			statDtls.setStatusLevel(Attributes.statusError);

		}
		
		statDtlsList.add(statDtls);
		itemInfo.setStatDetails(statDtlsList);
		
		return itemInfo;

	}

	public ItemInfo modifyCrossSellRelation(ItemLinks itemLinks,PimDaoMapper pimDaoMapper) {

		ItemInfo itemInfo = new ItemInfo();
		StatusDetails statDtls = new StatusDetails();
		List<ItemLinks> itemLinkList = new ArrayList<ItemLinks>();
		List<StatusDetails> statDtlsList = new ArrayList<StatusDetails>();
		try{
				
			itemLinkList = pimDaoMapper.getItemLinkListByParentId(itemLinks);
			if (!itemLinkList.isEmpty()) {

				pimDaoMapper.deleteItemLinksByParent(itemLinks);

			}

			log.info("insertOrUpdateItemObjects item links started:" + itemLinks.getItemLinkTypeId()
					+ ":" + itemLinks.getItemId());
			pimDaoMapper.insertOrUpdateItemObjects(itemLinks);
			statDtls.setStatusMessage(PimUtil.Messages.SUCCESSMESSAGE.getMessage());
			statDtls.setStatusLevel(Attributes.statusSuccess);
			
		}catch(Exception ex){
			log.error("Exception while deleteCrossSellRelation" + ex);
			statDtls.setStatusMessage(PimUtil.Messages.ERROR.getMessage());
			statDtls.setStatusLevel(Attributes.statusError);

		}
		
		statDtlsList.add(statDtls);
		itemInfo.setStatDetails(statDtlsList);
		
		return itemInfo;
 
			
	}	


}
