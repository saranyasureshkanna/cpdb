package com.kohler.puni.persistence.dao;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import com.kohler.persistence.domain.ItemLinkTypes;
import com.kohler.persistence.domain.ItemLinks;

public interface ItemLinksPcenDao {
	
	final String itemLinksInsert = "INSERT INTO CB02KPNA.cross_selling(ITEM_INFO_ID, cs_item_info_id, cross_sell_Type_ID,Created_By,Created_Date,Updated_By,TIMESTAMP,priority)"+
		 	"VALUES (#{parentId}, #{itemId},#{crSellItemId},#{createdBy},#{createdDate},#{updatedBy},#{updatedDate},#{priority})";
	
	final String deleteItemLinksByParent = " DELETE FROM CB02KPNA.cross_selling WHERE ITEM_INFO_ID = #{parentId}";
	
    final String deleteItemLinks = " DELETE FROM CB02KPNA.cross_selling WHERE ITEM_INFO_ID = #{parentId} and  cs_item_info_id= #{itemId} and cross_sell_Type_ID= #{crSellItemId}";


    public List<ItemLinks> getItemLinksByParentnChildId(Long parentId, Long itemId);
    
    public List<ItemLinks> getItemLinkListByParentId(Long parentId);
    
    public List<ItemLinks> getItemLinksByCSellRelation(Long parentId, Long itemId, Long itemLinkTypeId);
    
    public List<ItemLinkTypes> getItemLinkIdByLinkTypUc(String itemName);
    
    @Insert(itemLinksInsert)
	public int persistItemLinks(ItemLinks itemLinks) throws Exception;

	@Delete(deleteItemLinks)
	public int deleteHierarchyLink(ItemLinks itemLinks);
	
	@Delete(deleteItemLinksByParent)
	public int deleteItemLinksByParent(ItemLinks itemLinks);
	

}
