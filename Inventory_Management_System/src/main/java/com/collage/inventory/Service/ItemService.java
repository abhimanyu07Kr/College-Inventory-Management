package com.collage.inventory.Service;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.collage.inventory.Entity.Item;
import com.collage.inventory.Repository.IssueRecordRepository;
import com.collage.inventory.Repository.ItemRepository;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private IssueRecordRepository issueRecordRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("Item not found with id: " + id)
        );
        boolean isReferenced = issueRecordRepository.existsByItem(item);
        if (isReferenced) {
            throw new IllegalStateException("Cannot delete: Item is currently issued to someone.");
        }
        itemRepository.deleteById(id);
    }

    public List<Item> getLowStockItems(int threshold) {
        return itemRepository.findByItemQuantityLessThan(threshold);
    }

    public List<Item> getItemsByCategory(String category) {
        return itemRepository.findByItemCategory(category);
    }

    public Item increaseItemQuantity(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        item.setItemQuantity(item.getItemQuantity() + 1);
        return itemRepository.save(item);
    }

    public Item decreaseItemQuantity(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        int currentQty = item.getItemQuantity();
        if (currentQty <= 1) {
            itemRepository.deleteById(itemId);
            return null;
        }
        item.setItemQuantity(currentQty - 1);
        return itemRepository.save(item);
    }

    public Item updateItem(Long itemId, Item updatedItem) {
        Item existing = itemRepository.findById(itemId).orElseThrow(() ->
            new IllegalArgumentException("Item not found with id: " + itemId)
        );
        existing.setItemName(updatedItem.getItemName());
        existing.setItemCategory(updatedItem.getItemCategory());
        existing.setItemLocation(updatedItem.getItemLocation());
        existing.setItemCondition(updatedItem.getItemCondition());
        existing.setItemQuantity(updatedItem.getItemQuantity());
        return itemRepository.save(existing);
    }

    public void importFromExcel(MultipartFile file) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Item item = new Item();
                item.setItemName(getCellString(row.getCell(1)));
                item.setItemCategory(getCellString(row.getCell(2)));
                item.setItemLocation(getCellString(row.getCell(3)));
                item.setItemCondition(getCellString(row.getCell(4)));
                item.setItemQuantity((int) row.getCell(5).getNumericCellValue());

                itemRepository.save(item);
            }
        }
    }

    // ✅ Utility method to safely extract string from any cell type
    private String getCellString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
    
    public List<Item> getItemsByLocation(String location) {
        return itemRepository.findByItemLocation(location);
    }

}
