package com.collage.inventory.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import com.collage.inventory.Entity.Item;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

public class PdfReportGenerator {

    public static ByteArrayInputStream generateItemReport(List<Item> items, String title) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(out));
        Document document = new Document(pdf); // ✅ Now this matches iText's Document

        document.add(new Paragraph(title)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(16));

        Table table = new Table(UnitValue.createPercentArray(6)).useAllAvailableWidth();
        table.addHeaderCell("ID");
        table.addHeaderCell("Name");
        table.addHeaderCell("Category");
        table.addHeaderCell("Location");
        table.addHeaderCell("Condition");
        table.addHeaderCell("Quantity");

        for (Item item : items) {
            table.addCell(String.valueOf(item.getItemId()));
            table.addCell(item.getItemName());
            table.addCell(item.getItemCategory());
            table.addCell(item.getItemLocation());
            table.addCell(item.getItemCondition());
            table.addCell(String.valueOf(item.getItemQuantity()));
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
