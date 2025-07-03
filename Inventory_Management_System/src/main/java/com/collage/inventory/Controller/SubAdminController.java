package com.collage.inventory.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.collage.inventory.Entity.IssueRecord;
import com.collage.inventory.Entity.Item;
import com.collage.inventory.Service.IssueRecordService;
import com.collage.inventory.Service.ItemService;

//src/main/java/com/collage/inventory/Controller/SubAdminController.java
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/subadmin")
public class SubAdminController {
	final ItemService itemService;
	final IssueRecordService issueService;

	public SubAdminController(ItemService i, IssueRecordService s) {
		this.itemService = i;
		this.issueService = s;
	}

	@PostMapping("/add-item")
	public Item addItem(@RequestBody Item item) {
		return itemService.addItem(item);
	}

	@PostMapping("/issue-item")
	public IssueRecord issueItem(@RequestBody IssueRecord record) {
		return issueService.issueItem(record);
	}

	@PostMapping("/return-item")
	public void returnItem(@RequestParam Long id, @RequestParam String condition) {
		issueService.returnItem(id, condition);
	}
}
