package com.collage.inventory.Controller;

import java.io.ByteArrayInputStream;
import org.springframework.http.HttpHeaders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.collage.inventory.Entity.Item;
import com.collage.inventory.Service.ItemService;
import com.collage.inventory.util.PdfReportGenerator;


@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/")
    public List<Item> getItems() {
        return itemService.getAllItems();
    }

    @PostMapping("/")
    public Item createItem(@RequestBody Item item) {
        return itemService.addItem(item);
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }

    @GetMapping("/low-stock")
    public List<Item> getLowStockItems() {
        return itemService.getLowStockItems(5); // default threshold
    }

    @GetMapping("/category/{category}")
    public List<Item> getItemsByCategory(@PathVariable String category) {
        return itemService.getItemsByCategory(category);
    }
    
    @PostMapping("/increase/{itemId}")
    public Item increaseItem(@PathVariable Long itemId) {
        return itemService.increaseItemQuantity(itemId);
    }
    
    @PostMapping("/decrease/{itemId}")
    public Item decreaseItem(@PathVariable Long itemId) {
        return itemService.decreaseItemQuantity(itemId);
    }
    
    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item updatedItem) {
        return itemService.updateItem(id, updatedItem);
    }
    
    @PostMapping("/import")
    public ResponseEntity<String> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            itemService.importFromExcel(file);
            return ResponseEntity.ok("Import successful!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Import failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/report/category/{category}")
    public List<Item> getReportByCategory(@PathVariable String category) {
        return itemService.getItemsByCategory(category);
    }

    @GetMapping("/report/location/{location}")
    public List<Item> getReportByLocation(@PathVariable String location) {
        return itemService.getItemsByLocation(location);
    }

    @GetMapping("/report/low-stock")
    public List<Item> getLowStockReport(@RequestParam(defaultValue = "5") int threshold) {
        return itemService.getLowStockItems(threshold);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportAllItemsToPdf() throws Exception {
        List<Item> items = itemService.getAllItems();
        ByteArrayInputStream pdfStream = PdfReportGenerator.generateItemReport(items, "Full Inventory Report");

        byte[] pdfBytes = pdfStream.readAllBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventory_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
    
    @GetMapping("/export/pdf/category/{category}")
    public ResponseEntity<byte[]> exportByCategory(@PathVariable String category) throws Exception {
        List<Item> items = itemService.getItemsByCategory(category);
        ByteArrayInputStream pdfStream = PdfReportGenerator.generateItemReport(items, "Inventory Report - Category: " + category);
        byte[] pdfBytes = pdfStream.readAllBytes();

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventory_category_" + category + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes);
    }

    @GetMapping("/export/pdf/location/{location}")
    public ResponseEntity<byte[]> exportByLocation(@PathVariable String location) throws Exception {
        List<Item> items = itemService.getItemsByLocation(location);
        ByteArrayInputStream pdfStream = PdfReportGenerator.generateItemReport(items, "Inventory Report - Location: " + location);
        byte[] pdfBytes = pdfStream.readAllBytes();

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventory_location_" + location + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes);
    }

    @GetMapping("/export/pdf/low-stock")
    public ResponseEntity<byte[]> exportLowStockPdf(@RequestParam(defaultValue = "5") int threshold) throws Exception {
        List<Item> items = itemService.getLowStockItems(threshold);
        ByteArrayInputStream pdfStream = PdfReportGenerator.generateItemReport(items, "Low Stock Report (Threshold: " + threshold + ")");
        byte[] pdfBytes = pdfStream.readAllBytes();

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=low_stock_report.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes);
    }

    
    
}
