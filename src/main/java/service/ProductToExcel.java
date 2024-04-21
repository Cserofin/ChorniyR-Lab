package service;

import model.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import storage.Storage;

public class ProductToExcel {
    private static final String FILE_NAME = "results.xlsx";

    public static void convertIntoFile() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");

        int rowNum = 0;
        for (Product product : Storage.productMap.values()) {
            Row row = sheet.createRow(rowNum++);
            int cellNum = 0;
            row.createCell(cellNum++).setCellValue(product.getName());
            row.createCell(cellNum++).setCellValue(product.getBrand());
            row.createCell(cellNum++).setCellValue(product.getBasePrice());
            row.createCell(cellNum++).setCellValue(product.getSalePrice());
            row.createCell(cellNum++).setCellValue(product.getColor());
        }

        try (FileOutputStream outputStream = new FileOutputStream(new File(FILE_NAME))) {
            workbook.write(outputStream);
            System.out.println("Result file successfully created: " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error trying to convert data into Excel file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addProductToStorage(Product product) {

        if (product != null) {
            Storage.productMap.put(product.getArticleID(), product);
        } else {
            System.out.println("Error: Product is null.");
        }
    }
}
