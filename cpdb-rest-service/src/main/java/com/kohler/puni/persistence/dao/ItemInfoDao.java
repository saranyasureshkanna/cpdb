package com.kohler.puni.persistence.dao;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import com.kohler.persistence.domain.ItemGroups;


public interface ItemInfoDao {
	final String insertItemInfo = "INSERT INTO ITEM_INFO(Item_Id,Event_ID,Item,Status_ID,Item_Region_ID,Status_Date,Item_Kind,Default_Item_Style_ID,Keep_With,Priority,Sync_Group_ID,Display_Order,Display_Text,Created_By,Created_Date,Updated_By,Updated_Date,Language_Code) "+
	" VALUES(ITEM_INFO_SEQ.nextval,#{eventId},#{itemInfoId},#{statusId},#{itemRegionId},#{statusDate},#{itemKind},#{defaultItemStyleId},#{keepWith},#{priority},#{syncGroupId},#{displayOrder},#{displayText},#{createdBy},#{createdDate},#{updatedBy},#{updatedDate},#{languageCode})";
	
   // public List<ItemInfo> getItemInfoList();
    
	public List<ItemGroups> getItemGroups();
   // public int persistItemInfo(ItemInfo itemInfo);
    
}
