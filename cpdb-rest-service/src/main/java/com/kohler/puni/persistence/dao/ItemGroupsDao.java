package com.kohler.puni.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import com.kohler.persistence.domain.ItemGroups;
import com.kohler.persistence.domain.ItemInfo;
import com.kohler.persistence.domain.ItemLinks;

public interface ItemGroupsDao {
	final String itemGroupsInsert = "INSERT INTO CB02RESI.item_chains(Parent_ID, Item_ID,Item_Kind,Created_By,Created_Date,Updated_By,TIMESTAMP,Language_Code)"+
							 	"VALUES (#{parentId}, #{itemId},#{itemKind},#{createdBy},#{createdDate},#{updatedBy},#{updatedDate},#{languageCode})";
	
	final String itemGroupsUpdate= " UPDATE CB02RESI.item_chains SET Parent_ID=#{parentId},Item_Kind=#{itemKind},Created_By=#{createdBy},Created_Date=#{createdDate},Updated_By=#{updatedBy},TIMESTAMP=#{updatedDate},Language_Code=#{languageCode} where Item_ID=#{itemId} ";

	final String insertItemInfo = "INSERT INTO CB02RESI.ITEMS(Item_Id,Event_ID,Item,Status_ID,Item_Region_ID,Status_Date,Item_Kind,Default_Item_Style_ID,Keep_With,Priority,Sync_Group_ID,Display_Order,Display_Text,Created_By,Created_Date,Updated_By,TIMESTAMP,Language_Code) "+
			" VALUES(ITEMS_SEQ.nextval,#{eventId},#{itemName},#{statusId},#{itemRegionId},#{statusDate},#{itemKind},#{defaultItemStyleId},#{keepWith},#{priority},#{syncGroupId},#{displayOrder},#{displayText},#{createdBy},#{createdDate},#{updatedBy},#{updatedDate},#{languageCode})";
		
	final String updateItemInfo = "UPDATE CB02RESI.ITEMS SET Status_ID = #{statusId},"+
								  " Status_Date = #{statusDate},Item_Kind = #{itemKind}, "+
								  " Updated_By = #{updatedBy},TIMESTAMP = #{updatedDate} WHERE Item = #{itemName}";
	
    final String deleteItemGroups = " DELETE FROM CB02RESI.item_chains WHERE PARENT_ID = #{parentId} and  Item_ID= #{itemId}";
    
       
	public List<ItemGroups> getItemChildInfo();
	
    public List<ItemGroups> getItemGroupByParentId(Long parentId, Long itemId);
    
    public List<ItemInfo> getItemInfoByName(String itemName);
	
	@Insert(itemGroupsInsert)
	public int persistItemGroups(ItemGroups itemGroups) throws Exception;
	
	@Update(itemGroupsUpdate)
	public int updateItemGroups(ItemGroups itemGroups) throws Exception;		

	@Insert(insertItemInfo)
	public int persistItemInfo(ItemInfo itemInfo) throws Exception;
	
	@Update(updateItemInfo)	
	public int updateItemInfo(ItemInfo itemInfo) throws Exception;	
	
	@Delete(deleteItemGroups)
	public int deletehierarchy(ItemGroups itemGroups);
	
	
	
}
