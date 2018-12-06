package org.upgrad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.upgrad.models.Category;
import org.upgrad.models.Item;
import org.upgrad.models.Restaurant;

@Repository
public interface ItemRepository  extends CrudRepository<Item, Integer> {

   // @Query(nativeQuery = true,value = "SELECT Item.id, Item.item_name, Item.price, Item.type from Item INNER JOIN CATEGORY_item ON item.id=CATEGORY_item.item_id WHERE category_item.CATEGORY_ID= ?1")
    @Query(nativeQuery = true,value="SELECT * from ITEM INNER JOIN CATEGORY_ITEM on ITEM.id=CATEGORY_ITEM.ITEM_ID WHERE CATEGORY_ITEM.CATEGORY_ID=?1")
    Iterable<Item> getItemsByCategory(Integer categoryId);
}
