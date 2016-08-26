package com.kohler.puni.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import com.kohler.persistence.domain.ItemLinkTypes;
import com.kohler.persistence.domain.ItemLinks;

public interface ItemLinksPuniDao {
	
	final String itemLinksInsert = "INSERT INTO CB02RESI.item_links(Item_Id, Linked_Item_Id,Item_Link_Type_ID,Created_By,Created_Date,Updated_By,TIMESTAMP,Language_Code)"+
		 	"VALUES (#{parentId}, #{itemId},#{itemLinkTypeId},#{createdBy},#{createdDate},#{updatedBy},#{updatedDate},#{languageCode})";
	
	final String deleteItemLinksByParent = " DELETE FROM CB02RESI.item_links WHERE Item_Id = #{parentId}";
	
    final String deleteItemLinks = " DELETE FROM CB02RESI.item_links WHERE Item_Id = #{parentId} and  Linked_Item_Id= #{itemId} and Item_Link_Type_ID= #{itemLinkTypeId}";

    public List<ItemLinks> getItemLinksByParentnChildId(Long parentId, Long itemId);
    
    public List<ItemLinks> getItemLinkListByParentId(Long parentId);    
    
    public List<ItemLinks> getItemLinksByCSellRelation(Long parentId, Long itemId, Long itemLinkTypeId);
    
    public List<ItemLinkTypes> getItemLinkIdByLinkTypUc(String itemType);
    
	@Insert(itemLinksInsert)
	public int persistItemLinks(ItemLinks itemLinks) throws Exception;

	@Delete(deleteItemLinks)
	public int deleteHierarchyLink(ItemLinks itemLinks);
	
	@Delete(deleteItemLinksByParent)
	public int deleteItemLinksByParent(ItemLinks itemLinks);
	

}
