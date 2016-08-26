package com.kohler.puni.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.kohler.persistence.domain.ItemGroups;
import com.kohler.persistence.domain.ItemInfo;
import com.kohler.persistence.domain.ItemLinks;

public interface ItemGroupsPcenDao {
	final String itemGroupsInsert = "INSERT INTO CB02KPNA.ITEM_GROUPS(ITEM_GROUP_ID, ITEM_INFO_ID,Created_By,Created_Date,Updated_By,TIMESTAMP)"+
							 	"VALUES (#{parentId}, #{itemId},#{createdBy},#{createdDate},#{updatedBy},#{updatedDate})";
	
	final String itemGroupsUpdate= " UPDATE CB02KPNA.ITEM_GROUPS SET ITEM_GROUP_ID=#{parentId},Created_By=#{createdBy},Created_Date=#{createdDate},Updated_By=#{updatedBy},TIMESTAMP=#{updatedDate} where Item_INFO_ID=#{itemId} and ITEM_TYPE=#{itemKind} ";

	final String insertItemInfo = "INSERT INTO CB02KPNA.ITEM_INFO(ITEM_INFO_ID,ITEM_NO,Status_ID,Item_Region_ID,Status_Date,ITEM_TYPE,KEEP_WITH,PRIORITY,Sync_Group_ID,Created_By,Created_Date,Updated_By,TIMESTAMP) "+
			" VALUES(ITEM_INFO_SEQ.nextval,#{itemName},#{statusId},#{itemRegionId},#{statusDate},#{itemKind},#{keepWith},#{priority},#{syncGroupId},#{createdBy},#{createdDate},#{updatedBy},#{updatedDate})";
		
	final String updateItemInfo = "UPDATE CB02KPNA.ITEM_INFO SET Status_ID = #{statusId},"+
								  " Status_Date = #{statusDate},ITEM_TYPE = #{itemKind},   "+
								  " Updated_By = #{updatedBy},TIMESTAMP = #{updatedDate} WHERE ITEM_NO = #{itemName}";
	
    final String deleteItemGroups = " DELETE FROM CB02KPNA.item_groups WHERE ITEM_GROUP_ID=#{parentId} and ITEM_INFO_ID = #{itemId}";
    
    	
	public List<ItemGroups> getItemChildInfo();
	
    public List<ItemGroups> getItemGroupByParentId(@Param("parentId") Integer parentId, @Param("itemId")Integer itemId);
    
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
