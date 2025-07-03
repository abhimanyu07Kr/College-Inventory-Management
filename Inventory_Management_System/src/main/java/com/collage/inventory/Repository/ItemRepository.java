package com.collage.inventory.Repository;

import com.collage.inventory.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByItemCategory(String itemCategory);
    
    // ✅ This must match the entity field name (case-sensitive!)
    List<Item> findByItemQuantityLessThan(int threshold);
    
    List<Item> findByItemLocation(String itemLocation);

}

