package com.collage.inventory.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.collage.inventory.Entity.IssueRecord;
import com.collage.inventory.Service.IssueRecordService;


@RestController
@RequestMapping("/api/issues")
public class IssueController {
	
	 @Autowired
	    private IssueRecordService issueRecordService;

	    @GetMapping("/")
	    public List<IssueRecord> getAll() {
	        return issueRecordService.getAllRecords();
	    }

	    @PostMapping("/issue")
	    public IssueRecord issueItem(@RequestBody IssueRecord record) {
	        return issueRecordService.issueItem(record);
	    }

	    @PostMapping("/return/{id}")
	    public void returnItem(@PathVariable Long id, @RequestParam String condition) {
	        issueRecordService.returnItem(id, condition);
	    }
	    
	    @GetMapping("/report/movement")
	    public List<IssueRecord> getItemMovementHistory() {
	        return issueRecordService.getAllRecords();  // Optional: add filtering by date or returned status
	    }

}
