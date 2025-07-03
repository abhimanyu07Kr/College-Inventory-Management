package com.collage.inventory.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collage.inventory.Entity.IssueRecord;
import com.collage.inventory.Repository.IssueRecordRepository;

@Service
public class IssueRecordService {
	
	@Autowired
    private IssueRecordRepository issueRecordRepository;

    public List<IssueRecord> getAllRecords() {
        return issueRecordRepository.findAll();
    }

    public IssueRecord issueItem(IssueRecord record) {
        return issueRecordRepository.save(record);
    }

    public void returnItem(Long id, String condition) {
        IssueRecord record = issueRecordRepository.findById(id).orElseThrow();
        record.setReturnDate(java.time.LocalDate.now());
        record.setReturnCondition(condition);
        issueRecordRepository.save(record);
    }
    
    

}
