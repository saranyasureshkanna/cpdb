package com.kohler.puni.persistence.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kohler.persistence.Util.PimUtil;
import com.kohler.persistence.Util.PimUtil.Attributes;
import com.kohler.persistence.domain.ItemInfo;
import com.kohler.persistence.domain.ItemLinkTypes;
import com.kohler.persistence.domain.ItemLinks;
import com.kohler.persistence.provider.PimDaoMapper;
import com.kohler.persistence.provider.PimDaoProvider;

public class PimServiceCrSellingImpl {
	private static final Logger log = Logger.getLogger(PimServiceCrSellingImpl.class);
	
	@Autowired
	private PimDaoProvider pimDaoProvider;

	
	//TO DO: Testing inprogress for pds14
		public String processCSItemsJSON(JSONObject requestJSON, String schemaName) {
			JSONObject outputJSON = new JSONObject();
			JSONObject actionOutputJSON = null;
			JSONObject ancestorOutputJSON = null, parentOutputJson = null, childOutputJSON = null, crossSellTypeJSON = null, crossSellOutputJSON = null;
			log.info("Request json when processing itemInfo" + requestJSON);
			LinkedHashMap<String, Object> parentsMap = new LinkedHashMap<>();
			LinkedHashMap<String, Object> actionMap = new LinkedHashMap<>();
			Iterator<Entry<String, Object>> itEntry;
			List<String> csMap = new ArrayList<>();
			PimDaoMapper pimDaoMapper = pimDaoProvider.get(schemaName);
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
				for (Iterator<String> keys = requestJSON.keys(); keys.hasNext();) {
					String req = keys.next();
					JSONArray reqArr = (JSONArray) requestJSON.get(req);

					Type mapType = new TypeToken<Map<String, Map<String, Map<String, List<String>>>>>() {
					}.getType();

					for (Object reqObj : reqArr) {

						Gson gson = new Gson();
						Map<String, Map<String, Map<String, List<String>>>> deserializedMap = gson
								.fromJson(reqObj.toString(), mapType);

						Set<Entry<String, Map<String, Map<String, List<String>>>>> entrySet = deserializedMap.entrySet();
						Iterator<Entry<String, Map<String, Map<String, List<String>>>>> it = entrySet.iterator();

						while (it.hasNext()) {
							Map.Entry reqEntry = it.next();
							System.out.println("\nAction : " + reqEntry.getKey());

							actionMap = (LinkedHashMap<String, Object>) reqEntry.getValue();
							Set<Entry<String, Object>> actionSet = actionMap.entrySet();
							itEntry = actionSet.iterator();

							while (itEntry.hasNext()) {
								Map.Entry csEntry = itEntry.next();
								System.out.println("\nParent : " + csEntry.getKey());
								String parentName = csEntry.getKey().toString();
								itemInfoParent = new ItemInfo();
								itemInfoParent.setItemName(parentName);
								itemInfoParent = pimDaoMapper.getItemInfoByName(itemInfoParent);
								if (itemInfoParent == null) {
									parentExist = false;
									//throw new Exception(parentName + " " + Messages.ITEMNOTFOUND.getMessage());
								}
								parentsMap = (LinkedHashMap<String, Object>) csEntry.getValue();
								Set<Entry<String, Object>> csSet = parentsMap.entrySet();
								Iterator<Entry<String, Object>> itCS = csSet.iterator();

								while (itCS.hasNext()) {
									Map.Entry csTypes = itCS.next();
									System.out.println("Cross Selling Type : " + csTypes.getKey());
									String csTypeDesc = csTypes.getKey().toString();
									itemLinkTypes = new ItemLinkTypes();
									itemLinkTypes.setItemLinkTypeUc(csTypeDesc);
									itemLinkTypes = pimDaoMapper.getItemLinkIdByLinkTypUc(itemLinkTypes);
									if (itemLinkTypes == null) {
										csTypeExist = false;
										//throw new Exception(csTypeDesc + " " + Messages.RELATIONDOEANOTEXIST.getMessage());
									}

									csMap = (ArrayList<String>) csTypes.getValue();
									
									for (Object child : csMap) {
										System.out.println("Child : " + child.toString());
										String itemName = child.toString();
										itemInfoChild = new ItemInfo();
										itemInfoChild.setItemName(itemName);
										itemInfoChild = pimDaoMapper.getItemInfoByName(itemInfoChild);
										if (itemInfoChild == null) {
											childExist = false;
											//throw new Exception(itemName + " " + Messages.ITEMNOTFOUND.getMessage());
										}
										try{
											
											itemLinks = new ItemLinks();
											List<ItemLinks> itemLinkList = new ArrayList<>();
											itemLinks.setItemId(itemInfoChild.getItemId());
											itemLinks.setParentId(itemInfoParent.getItemId());
											itemLinks.setItemLinkTypeId(itemLinkTypes.getItemLinkTypeId());

											itemLinks.setLanguageCode(Attributes.languageCodeEN);
											itemLinks.setCreatedDate(new Date());
											itemLinks.setUpdatedDate(new Date());
											itemLinks.setPriority(Attributes.priority);

										if (reqEntry.getKey().toString().equalsIgnoreCase("D")) {
											itemLinks = deleteCrossSellRelation(itemLinks);		
											if (parentExist && childExist && csTypeExist) {
												itemLinkList = pimDaoMapper.getItemLinksByCSellRelation(itemLinks);
												if(itemLinkList.isEmpty()){
												statusMessage = PimUtil.Messages.WARNING.getMessage()
														+ itemInfoParent.getItemName()+":"+itemInfoChild.getItemName();
												statusLevel = Attributes.statusWarning;
												}else{
													pimDaoMapper.deleteHierarchyLink(itemLinks);
													statusMessage = PimUtil.Messages.SUCCESSMESSAGE.getMessage()
															+ itemInfoParent.getItemName()+":"+itemInfoChild.getItemName();
													statusLevel = Attributes.statusSuccess;
												}
												
											} else {
												statusMessage = PimUtil.Messages.VALIDATIONFAILED.getMessage()
														+ itemInfoParent.getItemName()+":"+itemInfoChild.getItemName();
												statusLevel = Attributes.statusError;
											}
											
										} else if(reqEntry.getKey().toString().equalsIgnoreCase("A")) {
											if (parentExist && childExist && csTypeExist) {
												itemLinkList = pimDaoMapper.getItemLinksByCSellRelation(itemLinks);											
												
												if (!itemLinkList.isEmpty()) {														
													pimDaoMapper.deleteHierarchyLink(itemLinks);														
												}
														
												log.info("insertOrUpdateItemObjects item links started:"+itemLinks.getItemLinkTypeId()+":"+itemLinks.getItemId());								
												pimDaoMapper.insertOrUpdateItemObjects(itemLinks);		
												statusMessage = PimUtil.Messages.SUCCESSMESSAGE.getMessage()
														+ itemInfoParent.getItemName()+":"+itemInfoChild.getItemName();
												statusLevel = Attributes.statusSuccess;
											}  else {
												statusMessage = PimUtil.Messages.VALIDATIONFAILED.getMessage()
														+ itemInfoParent.getItemName()+":"+itemInfoChild.getItemName();
												statusLevel = Attributes.statusError;
											}
			
										} else if(reqEntry.getKey().toString().equalsIgnoreCase("M")) {
											if (parentExist && childExist && csTypeExist) {
												itemLinkList = pimDaoMapper.getItemLinkListByParentId(itemLinks);
												if (!itemLinkList.isEmpty()) {

													pimDaoMapper.deleteItemLinksByParent(itemLinks);

												}

												log.info("insertOrUpdateItemObjects item links started:" + itemLinks.getItemLinkTypeId()
														+ ":" + itemLinks.getItemId());
												pimDaoMapper.insertOrUpdateItemObjects(itemLinks);

												statusMessage = PimUtil.Messages.SUCCESSMESSAGE.getMessage()
														+ itemInfoParent.getItemName()+":"+itemInfoChild.getItemName();
												statusLevel = Attributes.statusSuccess;
											} else {
												statusMessage = PimUtil.Messages.VALIDATIONFAILED.getMessage()
														+ itemInfoParent.getItemName()+":"+itemInfoChild.getItemName();
												statusLevel = Attributes.statusError;
											}

										}
										
										}catch(Exception insException){
											log.error("Exception while insert/update item groups" + insException
													+ itemInfoChild.getItemName());
											statusMessage = PimUtil.Messages.ERROR.getMessage() + insException.getMessage()
													+ itemInfoChild.getItemName();
											statusLevel = Attributes.statusError;
										}
										

									}
								}
							}
						}
					}
					
				}
			} catch (Exception insException) {
				log.error("Exception while insert/update item groups" + insException
						+ itemInfoParent.getItemName());
				statusMessage = PimUtil.Messages.ERROR.getMessage() + insException.getMessage()
						+ itemInfoParent.getItemName();
				statusLevel = Attributes.statusError;
			}
			return outputJSON.toString();
		}

	public ItemLinks deleteCrossSellRelation(ItemLinks itemLinks){
		
		return itemLinks;
		
	}
	
	public ItemLinks addCrossSellRelation(ItemLinks itemLinks){
		
		return itemLinks;
		
	}

	public ItemLinks modifyCrossSellRelation(ItemLinks itemLinks){
	
	return itemLinks;
	
	}

}
