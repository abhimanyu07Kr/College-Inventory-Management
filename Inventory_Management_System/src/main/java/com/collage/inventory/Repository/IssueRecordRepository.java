package com.collage.inventory.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collage.inventory.Entity.IssueRecord;
import com.collage.inventory.Entity.Item;

public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long>{

	 boolean existsByItem(Item item);
}
