package gui;

import Resources.smallProductAdding;
import models.UserDetails;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import models.MySQL;
import org.apache.poi.ss.usermodel.*;

public class StockAdd extends javax.swing.JFrame {

    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    HashMap<String, String> intidmap = new HashMap<>();

    public StockAdd() {
        initComponents();
        setSize(screen.width, screen.height);

        operater();
        time();
        refresh();
    }

    private void operater() {
        String name = UserDetails.UserName;
        userNameField.setText(name);
    }

    private void time() {
        final DateFormat timeFormat = new SimpleDateFormat("HH:mm aa");
        final DateFormat dateFormat = new SimpleDateFormat("yyy MMMM dd");

        ActionListener timerListener = (ActionEvent e) -> {
            Date date = new Date();
            String time = timeFormat.format(date);
            String day = dateFormat.format(date);
            String dayArray[] = day.split(" ");
            String year_string = dayArray[0];
            String month_string = dayArray[1];
            String day_string = dayArray[2];

            String DateString = day_string + " of " + month_string + " " + year_string;
            timeField.setText(time);
            dateField.setText(DateString);

        };
        Timer timer = new Timer(1000, timerListener);
        timer.setInitialDelay(0);
        timer.start();
    }

    public void LoadStockTable() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `stock`"
                    + " INNER JOIN `product` ON `product`.`intid` = `stock`.`product_intid`"
                    + " INNER JOIN `supplier` ON `supplier`.`supplier_id` = `stock`.`supplier_supplier_id` "
                    + " INNER JOIN `location` ON `location`.`id` = `stock`.`location_id` "
                    + " INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` "
                    + " INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` ");
            DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("product_intid"));
                v.add(rs.getInt("stock.id"));
                v.add(rs.getString("stock.product_id"));
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("sub_category"));
                v.add(rs.getInt("qty"));
                v.add(rs.getString("SKU"));
                v.add(rs.getDouble("cost"));
                v.add(rs.getDouble("saling_price"));
                v.add(rs.getDate("stock_date"));
                v.add(rs.getString("Supplier_Name"));
                v.add(rs.getString("location_name"));
                double total = (rs.getDouble("cost") * rs.getInt("qty"));
                v.add(total);
//                v.add(String.valueOf(v));
                v.add(rs.getString("SKU"));

                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AddinProductStock(String Command) {
        if (Command.equals("")) {
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `product` INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id`  ");
                DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
                dtm.setRowCount(0);

                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("product.id"));
                    v.add(rs.getString("product.intid"));
                    intidmap.put(rs.getString("product.id"), String.valueOf(rs.getInt("product.intid")));
                    v.add(rs.getString("brand_name"));
                    v.add(rs.getString("sub_category"));
                    dtm.addRow(v);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadLocations() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `location`");
            Vector v = new Vector();

            v.add("Select Category");
            while (rs.next()) {
                v.add(String.valueOf(rs.getString("id") + ") " + rs.getString("location_name")));
            }

            DefaultComboBoxModel dfm = new DefaultComboBoxModel<>(v);
            jComboBox3.setModel(dfm);

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", "Please Check Your Internet Connection or Please Try again later", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void supplierTable() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `supplier` INNER JOIN `company` ON `company`.`id` = `supplier`.`company_id`  ");
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("supplier_id") + "->" + rs.getString("Supplier_Name"));
                v.add(rs.getString("CompanyName"));
                dtm.addRow(v);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        AddinProductStock("");
        supplierTable();
        loadLocations();
        LoadStockTable();
        jTextField11.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField10.setText("");
        jTextField4.setText("");
        jTextField15.setText("");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void importExcelFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File chooseFile = fileChooser.getSelectedFile();

        // Check file extension
        if (!isValidExcelFile(chooseFile)) {
            JOptionPane.showMessageDialog(this,
                    "Please Select Valid Excel File (xlsx, xls, xlsm, xlsb, xltx, xltm)",
                    "Invalid File",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Show progress dialog
            JOptionPane.showMessageDialog(this, "Loading data from database...", "Import", JOptionPane.INFORMATION_MESSAGE);

            // Load database data
            Map<String, String> brandMap = loadBrandsForImport();
            Map<String, String[]> productMap = loadProductsForImport();
            Map<String, String> locationMap = loadLocationsForImport();
            Map<String, String> subCategoryMap = loadSubCategoriesForImport();
            Map<String, String> supplierMap = loadSuppliersForImport();

            if (brandMap == null || productMap == null || locationMap == null
                    || subCategoryMap == null || supplierMap == null) {
                return; // Error already shown
            }

            // Create brand name to ID map
            Map<String, String> brandNameToIdMap = new HashMap<>();
            for (Map.Entry<String, String> entry : brandMap.entrySet()) {
                brandNameToIdMap.put(entry.getValue().toLowerCase(), entry.getKey());
            }

            // Create location name to ID map
            Map<String, String> locationNameToIdMap = new HashMap<>();
            for (Map.Entry<String, String> entry : locationMap.entrySet()) {
                locationNameToIdMap.put(entry.getKey().toLowerCase(), entry.getValue());
            }

            // Create subcategory name to ID map
            Map<String, String> subCategoryNameToIdMap = new HashMap<>();
            for (Map.Entry<String, String> entry : subCategoryMap.entrySet()) {
                subCategoryNameToIdMap.put(entry.getValue().toLowerCase(), entry.getKey());
            }

            // Create supplier name to ID map
            Map<String, String> supplierNameToIdMap = new HashMap<>();
            for (Map.Entry<String, String> entry : supplierMap.entrySet()) {
                supplierNameToIdMap.put(entry.getValue().toLowerCase(), entry.getKey());
            }

            JOptionPane.showMessageDialog(this, "Processing Excel file...", "Import", JOptionPane.INFORMATION_MESSAGE);

            // Process Excel file
            ImportResult importResult = processExcelFileForImport(chooseFile, brandMap, brandNameToIdMap,
                    productMap, locationNameToIdMap, subCategoryNameToIdMap,
                    supplierNameToIdMap);

            // Show summary before inserting
            int confirm = JOptionPane.showConfirmDialog(this,
                    String.format("Import Summary:\n"
                            + "• New Brands: %d\n"
                            + "• New Suppliers: %d\n"
                            + "• New Products: %d\n"
                            + "• Stock Entries: %d\n\n"
                            + "Do you want to continue with the import?",
                            importResult.newBrands.size(),
                            importResult.newSuppliers.size(),
                            importResult.newProducts.size(),
                            importResult.newStocks.size()),
                    "Confirm Import",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Insert new data
            JOptionPane.showMessageDialog(this, "Inserting data into database...", "Import", JOptionPane.INFORMATION_MESSAGE);
            insertImportedData(importResult, brandMap, brandNameToIdMap, productMap,
                    subCategoryMap, subCategoryNameToIdMap, supplierMap, supplierNameToIdMap);

            // Refresh UI
            refresh();

            JOptionPane.showMessageDialog(this,
                    String.format("Import completed successfully!\n\n"
                            + "Summary:\n"
                            + "• New Brands: %d\n"
                            + "• New Suppliers: %d\n"
                            + "• New Products: %d\n"
                            + "• Stock Entries: %d",
                            importResult.newBrands.size(),
                            importResult.newSuppliers.size(),
                            importResult.newProducts.size(),
                            importResult.newStocks.size()),
                    "Import Complete",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error during import: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidExcelFile(File file) {
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        String[] allowedExtensions = {".xlsx", ".xls", ".xlsm", ".xlsb", ".xltx", ".xltm"};

        for (String ext : allowedExtensions) {
            if (ext.equals(fileExtension)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, String> loadBrandsForImport() {
        Map<String, String> brandMap = new HashMap<>();
        try {
            ResultSet rs = MySQL.execute("SELECT `id`, `brand_name` FROM `brand`");
            while (rs.next()) {
                brandMap.put(rs.getString("id"), cleanStringForImport(rs.getString("brand_name")));
            }
            return brandMap;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading brands from database. Please check your connection.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private Map<String, String[]> loadProductsForImport() {
        Map<String, String[]> productMap = new HashMap<>();
        try {
            ResultSet rs = MySQL.execute("SELECT `intid`, `id`, `brand_id` FROM `product`");
            while (rs.next()) {
                String[] productData = {
                    rs.getString("intid"),
                    cleanStringForImport(rs.getString("id")),
                    rs.getString("brand_id")
                };
                productMap.put(productData[1], productData);
            }
            return productMap;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading products from database. Please check your connection.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private Map<String, String> loadLocationsForImport() {
        Map<String, String> locationMap = new HashMap<>();
        try {
            ResultSet rs = MySQL.execute("SELECT `id`, `location_name` FROM `location`");
            while (rs.next()) {
                String locationName = cleanStringForImport(rs.getString("location_name"));
                String id = rs.getString("id");
                locationMap.put(locationName, id);
            }
            return locationMap;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading locations from database. Please check your connection.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private Map<String, String> loadSubCategoriesForImport() {
        Map<String, String> subCategoryMap = new HashMap<>();
        try {
            ResultSet rs = MySQL.execute("SELECT `id`, `sub_category` FROM `sub_category`");
            while (rs.next()) {
                String subCategoryName = cleanStringForImport(rs.getString("sub_category"));
                String id = rs.getString("id");
                subCategoryMap.put(id, subCategoryName);
            }
            return subCategoryMap;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading sub-categories from database. Please check your connection.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private Map<String, String> loadSuppliersForImport() {
        Map<String, String> supplierMap = new HashMap<>();
        try {
            ResultSet rs = MySQL.execute("SELECT `supplier_id`, `Supplier_Name` FROM `supplier`");
            while (rs.next()) {
                String supplierName = cleanStringForImport(rs.getString("Supplier_Name"));
                String id = rs.getString("supplier_id");
                supplierMap.put(id, supplierName);
            }
            return supplierMap;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading suppliers from database. Please check your connection.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private ImportResult processExcelFileForImport(File excelFile, Map<String, String> brandMap,
            Map<String, String> brandNameToIdMap,
            Map<String, String[]> productMap,
            Map<String, String> locationNameToIdMap,
            Map<String, String> subCategoryNameToIdMap,
            Map<String, String> supplierNameToIdMap) throws Exception {

        ImportResult result = new ImportResult();
        List<String> errors = new ArrayList<>();
        int processedRows = 0;
        int successRows = 0;

        try (FileInputStream fis = new FileInputStream(excelFile); Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            // Use current date for all imports
            String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            // Process rows starting from row 1 (skip header row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                processedRows++;

                try {
                    // Extract data from Excel columns according to your file structure
                    String productId = cleanStringForImport(formatter.formatCellValue(row.getCell(0))); // Column A: Product id
                    String subCategoryName = cleanStringForImport(formatter.formatCellValue(row.getCell(1))); // Column B: sub_category_id
                    String brandName = cleanStringForImport(formatter.formatCellValue(row.getCell(2))); // Column C: brand_id
                    String qualityId = cleanStringForImport(formatter.formatCellValue(row.getCell(3))); // Column D: Quality_id
                    String productName = cleanStringForImport(formatter.formatCellValue(row.getCell(4))); // Column E: product_name
                    String supplierName = cleanStringForImport(formatter.formatCellValue(row.getCell(5))); // Column F: supplier_supplier_id
                    String locationName = cleanStringForImport(formatter.formatCellValue(row.getCell(6))); // Column G: location_id
                    String cost = cleanStringForImport(formatter.formatCellValue(row.getCell(7))); // Column H: cost
                    String sellingPrice = cleanStringForImport(formatter.formatCellValue(row.getCell(8))); // Column I: saling_price
                    String quantity = cleanStringForImport(formatter.formatCellValue(row.getCell(9))); // Column J: qty
                    String color = cleanStringForImport(formatter.formatCellValue(row.getCell(10))); // Column K: color
                    String sku = cleanStringForImport(formatter.formatCellValue(row.getCell(11))); // Column L: SKU

                    // Validate required fields
                    if (productId.isEmpty()) {
                        errors.add("Row " + (i + 1) + ": Product ID is empty");
                        continue;
                    }

                    if (brandName.isEmpty()) {
                        errors.add("Row " + (i + 1) + ": Brand is empty");
                        continue;
                    }

                    if (locationName.isEmpty()) {
                        errors.add("Row " + (i + 1) + ": Location is empty");
                        continue;
                    }

                    if (subCategoryName.isEmpty()) {
                        errors.add("Row " + (i + 1) + ": Sub-Category is empty");
                        continue;
                    }

                    // Get location ID
                    String normalizedLocation = locationName.toLowerCase();
                    String locationId = locationNameToIdMap.get(normalizedLocation);
                    if (locationId == null) {
                        errors.add("Row " + (i + 1) + ": Location '" + locationName + "' not found in database");
                        continue;
                    }

                    // Check if subcategory exists
                    String normalizedSubCategory = subCategoryName.toLowerCase();
                    String subCategoryId = subCategoryNameToIdMap.get(normalizedSubCategory);
                    if (subCategoryId == null) {
                        errors.add("Row " + (i + 1) + ": Sub-Category '" + subCategoryName + "' not found in database");
                        continue;
                    }

                    // Check if brand exists, if not add to new brands
                    String normalizedBrand = brandName.toLowerCase();
                    String brandId = brandNameToIdMap.get(normalizedBrand);

                    if (brandId == null) {
                        // Check if brand already in new brands list (case-insensitive)
                        boolean brandAlreadyAdded = false;
                        for (String newBrand : result.newBrands) {
                            if (newBrand.toLowerCase().equals(normalizedBrand)) {
                                brandAlreadyAdded = true;
                                break;
                            }
                        }

                        if (!brandAlreadyAdded) {
                            result.newBrands.add(brandName);
                        }
                    }

                    // Check if supplier exists, if not add to new suppliers
                    String normalizedSupplier = supplierName.toLowerCase();
                    String supplierId = supplierNameToIdMap.get(normalizedSupplier);

                    if (supplierId == null && !supplierName.isEmpty() && !supplierName.equalsIgnoreCase("NO ID")) {
                        // Check if supplier already in new suppliers list (case-insensitive)
                        boolean supplierAlreadyAdded = false;
                        for (String newSupplier : result.newSuppliers) {
                            if (newSupplier.toLowerCase().equals(normalizedSupplier)) {
                                supplierAlreadyAdded = true;
                                break;
                            }
                        }

                        if (!supplierAlreadyAdded) {
                            result.newSuppliers.add(supplierName);
                        }
                        supplierId = "1"; // Use default supplier ID temporarily
                    } else if (supplierId == null && (supplierName.isEmpty() || supplierName.equalsIgnoreCase("NO ID"))) {
                        supplierId = "1"; // Default supplier ID for "NO ID"
                    }

                    // Check if product exists
                    boolean productExists = productMap.containsKey(productId);

                    if (productExists) {
                        // Product exists
                        String[] existingProduct = productMap.get(productId);
                        String existingBrandId = existingProduct[2];
                        String existingBrandName = brandMap.get(existingBrandId);

                        // Check if brand matches
                        if (brandName.equalsIgnoreCase(existingBrandName)) {
                            // Same brand - add stock
                            String[] stockData = createStockDataForImport(
                                    supplierId != null ? supplierId : "1", // Use supplier ID or default
                                    locationId,
                                    cost.isEmpty() ? "0" : cost,
                                    sellingPrice.isEmpty() ? "0" : sellingPrice,
                                    todayDate,
                                    quantity.isEmpty() ? "1" : quantity,
                                    color,
                                    existingProduct[0], // product intid
                                    productId,
                                    sku
                            );
                            result.newStocks.add(stockData);
                        } else {
                            // Different brand - add as new product
                            addNewProductForImport(result, productId, subCategoryId, brandName, qualityId,
                                    productName, supplierId != null ? supplierId : "1", locationName,
                                    cost, sellingPrice, quantity, color, sku);

                            // Also add stock for the new product (will be linked after insertion)
                            String[] stockData = createStockDataForImport(
                                    supplierId != null ? supplierId : "1",
                                    locationId,
                                    cost.isEmpty() ? "0" : cost,
                                    sellingPrice.isEmpty() ? "0" : sellingPrice,
                                    todayDate,
                                    quantity.isEmpty() ? "1" : quantity,
                                    color,
                                    null, // Will be set after product insertion
                                    productId,
                                    sku
                            );
                            result.newStocks.add(stockData);
                        }
                    } else {
                        // New product - add it
                        addNewProductForImport(result, productId, subCategoryId, brandName, qualityId,
                                productName, supplierId != null ? supplierId : "1", locationName,
                                cost, sellingPrice, quantity, color, sku);

                        // Add stock entry
                        String[] stockData = createStockDataForImport(
                                supplierId != null ? supplierId : "1",
                                locationId,
                                cost.isEmpty() ? "0" : cost,
                                sellingPrice.isEmpty() ? "0" : sellingPrice,
                                todayDate,
                                quantity.isEmpty() ? "1" : quantity,
                                color,
                                null, // Will be set after product insertion
                                productId,
                                sku
                        );
                        result.newStocks.add(stockData);
                    }

                    successRows++;

                } catch (Exception e) {
                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
                }
            }

            // Show errors if any
            if (!errors.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("Processed ").append(processedRows).append(" rows, ")
                        .append(successRows).append(" successful, ").append(errors.size()).append(" error(s):\n\n");

                int maxErrorsToShow = Math.min(10, errors.size());
                for (int i = 0; i < maxErrorsToShow; i++) {
                    errorMessage.append(errors.get(i)).append("\n");
                }

                if (errors.size() > 10) {
                    errorMessage.append("\n... and ").append(errors.size() - 10).append(" more errors");
                }

                JOptionPane.showMessageDialog(this, errorMessage.toString(),
                        "Import Summary", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Processed " + processedRows + " rows successfully!",
                        "Import Complete", JOptionPane.INFORMATION_MESSAGE);
            }

            System.out.println("Processed " + processedRows + " rows from Excel (" + successRows + " successful)");
            System.out.println("New brands to add: " + result.newBrands.size());
            System.out.println("New suppliers to add: " + result.newSuppliers.size());
            System.out.println("New products to add: " + result.newProducts.size());
            System.out.println("New stock entries to add: " + result.newStocks.size());

        }

        return result;
    }

    private void addNewProductForImport(ImportResult result, String productId, String subCategoryId,
            String brandName, String qualityId, String productName,
            String supplierId, String location, String cost, String sellingPrice,
            String quantity, String color, String sku) {

        // Leave productName as is (can be empty)
        String[] productData = {
            productId,
            subCategoryId, // Now passing subcategory ID instead of name
            brandName,
            qualityId.isEmpty() ? "2" : qualityId, // Default to GRADE B (id=2) if empty
            productName, // Can be empty
            supplierId, // Supplier ID
            location,
            cost.isEmpty() ? "0" : cost,
            sellingPrice.isEmpty() ? "0" : sellingPrice,
            quantity.isEmpty() ? "1" : quantity,
            color,
            sku
        };

        result.newProducts.add(productData);
    }

    private String[] createStockDataForImport(String supplierId, String locationId, String cost,
            String sellingPrice, String date, String quantity,
            String color, String productIntId, String productId, String sku) {

        // Clean and validate numeric values
        if (cost == null || cost.isEmpty()) {
            cost = "0";
        } else {
            // Remove any non-numeric characters except decimal point
            cost = cost.replaceAll("[^\\d.]", "");
            if (cost.isEmpty()) {
                cost = "0";
            }
        }

        if (sellingPrice == null || sellingPrice.isEmpty()) {
            sellingPrice = "0";
        } else {
            // Remove any non-numeric characters except decimal point
            sellingPrice = sellingPrice.replaceAll("[^\\d.]", "");
            if (sellingPrice.isEmpty()) {
                sellingPrice = "0";
            }
        }

        if (quantity == null || quantity.isEmpty()) {
            quantity = "1";
        } else {
            // Remove any non-numeric characters
            quantity = quantity.replaceAll("[^\\d]", "");
            if (quantity.isEmpty()) {
                quantity = "1";
            }
        }

        if (supplierId == null || supplierId.isEmpty()) {
            supplierId = "1"; // Default supplier
        }

        return new String[]{
            supplierId,
            locationId,
            cost,
            sellingPrice,
            date,
            quantity,
            color,
            productIntId,
            productId,
            sku
        };
    }

    private void insertImportedData(ImportResult result, Map<String, String> brandMap,
            Map<String, String> brandNameToIdMap,
            Map<String, String[]> productMap,
            Map<String, String> subCategoryMap,
            Map<String, String> subCategoryNameToIdMap,
            Map<String, String> supplierMap,
            Map<String, String> supplierNameToIdMap) {

        // Update maps with new entries as we insert them
        Map<String, String> newBrandIds = new HashMap<>();
        Map<String, String> newSupplierIds = new HashMap<>();

        try {
            // 1. Insert new brands
            if (!result.newBrands.isEmpty()) {
                System.out.println("Inserting " + result.newBrands.size() + " new brands...");

                for (String brandName : result.newBrands) {
                    try {
                        String cleanBrandName = cleanStringForImport(brandName);
                        MySQL.execute("INSERT INTO `brand` (brand_name) VALUES ('" + cleanBrandName + "')");

                        // Get the inserted ID
                        ResultSet rs = MySQL.execute("SELECT LAST_INSERT_ID() as id");
                        if (rs.next()) {
                            String newId = rs.getString("id");
                            brandMap.put(newId, cleanBrandName);
                            brandNameToIdMap.put(cleanBrandName.toLowerCase(), newId);
                            newBrandIds.put(cleanBrandName.toLowerCase(), newId);
                        }

                        System.out.println("Inserted brand: " + cleanBrandName);

                    } catch (Exception e) {
                        System.err.println("Error inserting brand '" + brandName + "': " + e.getMessage());
                    }
                }
            }

            // 2. Insert new suppliers
            if (!result.newSuppliers.isEmpty()) {
                System.out.println("Inserting " + result.newSuppliers.size() + " new suppliers...");

                for (String supplierName : result.newSuppliers) {
                    try {
                        String cleanSupplierName = cleanStringForImport(supplierName);
                        // Insert supplier with default company_id = 1 (you may need to adjust this)
                        MySQL.execute("INSERT INTO `supplier` (company_id, Supplier_Name) VALUES ('1', '" + cleanSupplierName + "')");

                        // Get the inserted ID
                        ResultSet rs = MySQL.execute("SELECT LAST_INSERT_ID() as supplier_id");
                        if (rs.next()) {
                            String newId = rs.getString("supplier_id");
                            supplierMap.put(newId, cleanSupplierName);
                            supplierNameToIdMap.put(cleanSupplierName.toLowerCase(), newId);
                            newSupplierIds.put(cleanSupplierName.toLowerCase(), newId);
                        }

                        System.out.println("Inserted supplier: " + cleanSupplierName);

                    } catch (Exception e) {
                        System.err.println("Error inserting supplier '" + supplierName + "': " + e.getMessage());
                    }
                }
            }

            // 3. Insert new products
            if (!result.newProducts.isEmpty()) {
                System.out.println("Inserting " + result.newProducts.size() + " new products...");
                Map<String, String> newProductIntIds = new HashMap<>();

                for (String[] product : result.newProducts) {
                    try {
                        String productId = cleanStringForImport(product[0]);
                        String brandName = cleanStringForImport(product[2]);
                        String subCategoryId = product[1]; // Already an ID from subCategoryNameToIdMap
                        String supplierId = product[5]; // Supplier ID

                        // Get brand ID
                        String normalizedBrand = brandName.toLowerCase();
                        String brandId = brandNameToIdMap.get(normalizedBrand);
                        if (brandId == null) {
                            // Try to get from new brands
                            brandId = newBrandIds.get(normalizedBrand);
                        }

                        if (brandId == null) {
                            System.err.println("Brand ID not found for: " + brandName);
                            continue;
                        }

                        // Get supplier ID (might need to update from new suppliers)
                        if (supplierId.equals("1") && !brandName.isEmpty()) {
                            // Try to find supplier by brand name
                            String normalizedBrandForSupplier = brandName.toLowerCase();
                            String actualSupplierId = supplierNameToIdMap.get(normalizedBrandForSupplier);
                            if (actualSupplierId != null) {
                                supplierId = actualSupplierId;
                            }
                        }

                        // Build SQL query - handle empty product_name
                        String productNameValue = product[4].isEmpty() ? "NULL" : "'" + escapeSqlForImport(product[4]) + "'";

                        String sql = String.format(
                                "INSERT INTO `product` (id, sub_category_id, brand_id, Quality_id, product_name ) "
                                + "VALUES ('%s', '%s', '%s', '%s', %s)",
                                productId,
                                subCategoryId,
                                brandId,
                                "2",
                                //escapeSqlForImport(product[3]), // quality_id
                                productNameValue // product_name - can be NULL

                        );

                        //                                escapeSqlForImport(supplierId), // supplier ID
                        //                                escapeSqlForImport(product[6]), // location name
                        //                                product[7], // cost
                        //                                product[8], // selling_price
                        //                                product[9], // qty
                        //                                escapeSqlForImport(product[10]), // color
                        //                                escapeSqlForImport(product[11]) // sku
                        MySQL.execute(sql);

                        // Get the inserted intid
                        ResultSet rs = MySQL.execute("SELECT LAST_INSERT_ID() as intid");
                        if (rs.next()) {
                            String intid = rs.getString("intid");
                            newProductIntIds.put(productId, intid);

                            // Update product map
                            String[] productData = {intid, productId, brandId};
                            productMap.put(productId, productData);
                        }

                        System.out.println("Inserted product: " + productId);

                    } catch (Exception e) {
                        System.err.println("Error inserting product '" + product[0] + "': " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                // 4. Update stock entries with product intids
                for (String[] stock : result.newStocks) {
                    String productId = cleanStringForImport(stock[8]);
                    if (stock[7] == null || stock[7].isEmpty()) {
                        // Try to get intid from new products or existing products
                        String intid = newProductIntIds.get(productId);
                        if (intid == null && productMap.containsKey(productId)) {
                            intid = productMap.get(productId)[0];
                        }
                        if (intid != null) {
                            stock[7] = intid;
                        }
                    }
                }
            }

            // 5. Insert stocks
if (!result.newStocks.isEmpty()) {
    System.out.println("Inserting " + result.newStocks.size() + " stock entries...");

    for (String[] stock : result.newStocks) {
        try {
            String productId = cleanStringForImport(stock[8]);
            String intid = stock[7];

            // Skip if no intid
            if (intid == null || intid.isEmpty()) {
                System.err.println("Skipping stock for product " + productId + ": No intid found");
                continue;
            }

            // Parse and validate numeric values
            double costValue;
            double sellingPriceValue;
            int quantityValue;
            
            try {
                costValue = Double.parseDouble(stock[2]);
                sellingPriceValue = Double.parseDouble(stock[3]);
                quantityValue = Integer.parseInt(stock[5]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid numeric value for product " + productId + ": " + e.getMessage());
                continue;
            }

            // Build SQL query with properly formatted numbers
            String sql = String.format(
                    "INSERT INTO `stock` (product_id, supplier_supplier_id, location_id, cost, "
                    + "saling_price, stock_date, qty, product_intid, SKU) "
                    + "VALUES ('%s', '%s', '%s', %.2f, %.2f, '%s', %d, '%s', '%s')",
                    productId,
                    stock[0], // supplier_id
                    stock[1], // location_id
                    costValue,
                    sellingPriceValue,
                    stock[4], // date
                    quantityValue,
                    intid,
                    escapeSqlForImport(stock[9]) // sku
            );

            MySQL.execute(sql);

            System.out.println("Inserted stock for product: " + productId);

        } catch (Exception e) {
            System.err.println("Error inserting stock for product '" + stock[8] + "': " + e.getMessage());
            e.printStackTrace();
        }
    }
}
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error inserting data into database: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

// Helper classes
    class ImportResult {

        List<String> newBrands = new ArrayList<>();
        List<String> newSuppliers = new ArrayList<>();
        List<String[]> newProducts = new ArrayList<>();
        List<String[]> newStocks = new ArrayList<>();
    }

// Helper methods for import
    private String cleanStringForImport(String input) {
        if (input == null) {
            return "";
        }
        return input.trim();
    }

    private String escapeSqlForImport(String input) {
        if (input == null) {
            return "";
        }
        // Simple SQL escaping - replace single quotes
        return input.replace("'", "''");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jButton6 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel29 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel31 = new javax.swing.JLabel();
        SKUNO = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        userNameField = new javax.swing.JLabel();
        dateField = new javax.swing.JLabel();
        timeField = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/home (1).png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/back.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/reload.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel6.setText("Actions");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/diskette (1).png"))); // NOI18N
        jButton4.setText("Add Stock");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Customer management.png"))); // NOI18N
        jButton6.setText("Stock Management");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel7.setText("Pages");

        jPanel6.setBackground(new java.awt.Color(206, 206, 206));

        jLabel13.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel13.setText("Stock Infomations");

        jLabel14.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel15.setText("Stock Date");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Company", "Supplier"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            jTable2.getColumnModel().getColumn(1).setResizable(false);
        }

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Row No", "Stock ID", "Product ID", "Product Brand", "Sub Category", "Qty", "SKU", "Unit Price", "Selling Price", "Date TIme", "Supplier", "Location", "Total Cost", "SKU"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setResizable(false);
            jTable3.getColumnModel().getColumn(1).setResizable(false);
            jTable3.getColumnModel().getColumn(2).setResizable(false);
            jTable3.getColumnModel().getColumn(3).setResizable(false);
            jTable3.getColumnModel().getColumn(4).setResizable(false);
            jTable3.getColumnModel().getColumn(5).setResizable(false);
            jTable3.getColumnModel().getColumn(6).setResizable(false);
            jTable3.getColumnModel().getColumn(7).setResizable(false);
            jTable3.getColumnModel().getColumn(8).setResizable(false);
            jTable3.getColumnModel().getColumn(9).setResizable(false);
            jTable3.getColumnModel().getColumn(10).setResizable(false);
            jTable3.getColumnModel().getColumn(11).setResizable(false);
            jTable3.getColumnModel().getColumn(12).setResizable(false);
            jTable3.getColumnModel().getColumn(13).setResizable(false);
        }

        jLabel16.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel16.setText("Sub Category");

        jTextField10.setText("Search By");
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });
        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField10KeyReleased(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel24.setText("Quantity");

        jTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField11ActionPerformed(evt);
            }
        });
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField11KeyReleased(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel25.setText("Product ID");

        jLabel26.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel26.setText("Supplier Company");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel28.setText("Location");

        jLabel30.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel30.setText("Selling Price");

        jLabel29.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel29.setText("Add Stock");

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel32.setText("Product Brand");

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Product ID", "Row No", "Brand", "SubCategory"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable4);
        if (jTable4.getColumnModel().getColumnCount() > 0) {
            jTable4.getColumnModel().getColumn(0).setResizable(false);
            jTable4.getColumnModel().getColumn(1).setResizable(false);
            jTable4.getColumnModel().getColumn(2).setResizable(false);
            jTable4.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel31.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel31.setText("SKU No");

        SKUNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SKUNOActionPerformed(evt);
            }
        });

        jLabel1.setText("Frame Size (optional)");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator5)
                        .addContainerGap())))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel13)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel25))
                        .addGap(332, 332, 332)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel15)
                                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel28)
                                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jTextField1))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(jLabel31))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(SKUNO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(0, 73, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 1025, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
            .addComponent(jScrollPane3)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel13)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel25)
                                .addComponent(jLabel32)
                                .addComponent(jLabel16)
                                .addComponent(jLabel26))
                            .addComponent(jLabel15))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(10, 10, 10)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(10, 10, 10)
                        .addComponent(SKUNO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel30))
                                .addGap(10, 10, 10)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/spect.png"))); // NOI18N
        jButton5.setText("Add New Product");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setText("Product Management");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/excel-file (1).png"))); // NOI18N
        jButton8.setText("Import Excel");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/office.png"))); // NOI18N
        jButton9.setText("Download Template");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel7)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jSeparator3)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6)
                                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jSeparator7)
                                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(50, 50, 50))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(27, 27, 27)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel10.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel10.setText("Operator : ");

        jLabel11.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel11.setText("Date       :");

        jLabel12.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel12.setText("Time       :");

        userNameField.setText("user");

        dateField.setText("date");

        timeField.setText("time");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateField))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(timeField)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(userNameField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(dateField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(timeField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        jLabel5.setText("StockAdding");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel5)))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)))
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Dashboard d = new Dashboard();
        d.setVisible(true);
        this.dispose();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Add Stock Process
        String FrameSize = jTextField1.getText();
        String Product_id = jTextField11.getText();
        int row = jTable4.getSelectedRow();
        String productintid = String.valueOf(jTable4.getValueAt(row, 1));
        System.out.println(productintid);

        Date chooseDate;
        String formatDate = "";
        try {
            chooseDate = jDateChooser2.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            formatDate = sdf.format(chooseDate);
        } catch (NullPointerException ne) {
            chooseDate = null;
        }

        String SupplierTableRow = "";
        String supplier_ID = "";
        try {
            SupplierTableRow = String.valueOf(jTable2.getValueAt(jTable2.getSelectedRow(), 0));
            String supplier_array[] = SupplierTableRow.split("->");
            supplier_ID = supplier_array[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            SupplierTableRow = "null";
        }

        int Location_id = jComboBox3.getSelectedIndex();

        String Qty = jTextField4.getText();

        double SellingPrice;

        try {
            SellingPrice = Double.parseDouble(jTextField15.getText());

        } catch (NumberFormatException e) {
            SellingPrice = 0.0;
        }

        try {
            ResultSet p_rs = MySQL.execute("SELECT * FROM `product` WHERE `product`.`id` = '" + Product_id + "' AND `product`.`intid` =  '" + productintid + "' ");

//        validation
            if (Product_id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Select or Type Product ID", "Empty Parameters", JOptionPane.ERROR_MESSAGE);
            } else if (!p_rs.next()) {
                JOptionPane.showMessageDialog(this, "Invalid Product ID,", "Invlaid Parameters", JOptionPane.ERROR_MESSAGE);
            } else if (SupplierTableRow == "null") {
                JOptionPane.showMessageDialog(this, "Please Select Supplier from Table,", "Empty Parameters", JOptionPane.ERROR_MESSAGE);
            } else if (chooseDate == null) {
                JOptionPane.showMessageDialog(this, "Please Select a Date", "Empty Parameters", JOptionPane.ERROR_MESSAGE);
            } else if (Location_id == 0) {
                JOptionPane.showMessageDialog(this, "Please Select a Location", "Empty Parameters", JOptionPane.ERROR_MESSAGE);
            } else if (Qty.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter Quantity", "Empty Parameters", JOptionPane.ERROR_MESSAGE);
            } else if (!Qty.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Please Enter Valid Quanity", "Invalid Data Type", JOptionPane.ERROR_MESSAGE);
            } else if (SellingPrice == 0.0) {
                JOptionPane.showMessageDialog(this, "Please Enter selling price", "Empty Parameters", JOptionPane.ERROR_MESSAGE);
            } else if (String.valueOf(SellingPrice).matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Please Enter Valid  Selling Price", "Invalid  Parameters", JOptionPane.ERROR_MESSAGE);
            } else {

                MySQL.execute("INSERT INTO `stock` (`product_id`,`supplier_supplier_id`,`location_id`,`cost`,`saling_price`,`stock_date`,`qty`,`product_intid`,`SKU`,`FrameSize`) "
                        + "VALUES ('" + Product_id + "','" + supplier_ID + "','" + Location_id + "','0','" + SellingPrice + "','" + formatDate + "','" + Qty + "','" + productintid + "','" + SKUNO.getText() + "','" + FrameSize + "')");

                JOptionPane.showMessageDialog(this, "Stock Adding Success ", "Insert Success", JOptionPane.ERROR_MESSAGE);
                refresh();
            }
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(this, "Please Check Your Network or Try again later");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyReleased
        // Key Relesed Search by Product ID
        try {

            jTextField6.setText("");
            jTextField5.setText("");

            ResultSet rs = MySQL.execute("SELECT * FROM `product` INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` WHERE `product`.id LIKE '%" + jTextField11.getText() + "%'  ");
            DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("product.id"));
                v.add(rs.getString("product.intid"));
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("sub_category"));
                dtm.addRow(v);

            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Connection. and Please Try again Later");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Some Thing Wrong Please Try again Later or Contact Devloper");

        }
    }//GEN-LAST:event_jTextField11KeyReleased

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        // Search By Brand Name Key Relesed
        try {

            jTextField11.setText("");
            jTextField5.setText("");

            ResultSet rs = MySQL.execute("SELECT * FROM `product` INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` WHERE `brand`.`brand_name`  LIKE '%" + jTextField6.getText() + "%' ");
            DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("product.id"));
                v.add(rs.getString("product.intid"));
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("sub_category"));
                dtm.addRow(v);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Connection. and Please Try again Later");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Some Thing Wrong Please Try again Later or Contact Devloper");

        }
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        // Search ReleseKey By Product Sub Category
        try {

            jTextField11.setText("");
            jTextField6.setText("");

            ResultSet rs = MySQL.execute("SELECT * FROM `product` INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` WHERE `sub_category`.`sub_category`  LIKE '%" + jTextField5.getText() + "%' ");
            DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("product.id"));
                v.add(rs.getString("product.intid"));
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("sub_category"));
                dtm.addRow(v);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Connection. and Please Try again Later");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Some Thing Wrong Please Try again Later or Contact Devloper");

        }
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // Select Product
        if (evt.getClickCount() == 2) {
            int table2SelectedRow = jTable2.getSelectedRow();
            String SupplierDetails = String.valueOf(jTable2.getValueAt(table2SelectedRow, 0));
            String id[] = SupplierDetails.split("->");

            jTextField10.setText("Supplier id is =" + id[0]);

        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        // Product Table Double Click
        if (evt.getClickCount() == 2) {
            int table2SelectedRow = jTable4.getSelectedRow();
            String Product_id = String.valueOf(jTable4.getValueAt(table2SelectedRow, 0));
            String Brand = String.valueOf(jTable4.getValueAt(table2SelectedRow, 1));
            String SubCategory = String.valueOf(jTable4.getValueAt(table2SelectedRow, 2));

            jTextField11.setText(Product_id);
            jTextField6.setText(Brand);
            jTextField5.setText(SubCategory);

        }
    }//GEN-LAST:event_jTable4MouseClicked

    private void jTextField10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyReleased
        // Search Key From Supplier
        try {

            ResultSet rs = MySQL.execute("SELECT * FROM `supplier` INNER JOIN `company` ON `company`.`id` = `supplier`.`company_id`  WHERE `company`.`CompanyName` LIKE '%" + jTextField10.getText() + "%' ");
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("supplier_id") + "->" + rs.getString("Supplier_Name"));
                v.add(rs.getString("CompanyName"));
                dtm.addRow(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTextField10KeyReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        StockManagement stockManagement = new StockManagement();
        stockManagement.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:\
        smallProductAdding sm = new smallProductAdding();
        sm.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // Go Product Mangagement
        ProductManagement productManagement = new ProductManagement();
        productManagement.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void SKUNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SKUNOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SKUNOActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // Import Excel
        importExcelFile();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacLightLaf.setup();


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StockAdd().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField SKUNO;
    private javax.swing.JLabel dateField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox3;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel timeField;
    private javax.swing.JLabel userNameField;
    // End of variables declaration//GEN-END:variables
}
