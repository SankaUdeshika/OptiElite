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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import models.ActivityLog;
import models.MySQL;
import org.apache.poi.ss.usermodel.*;

public class StockAdd extends javax.swing.JFrame {

    public static Logger logger = Logger.getLogger("egaleEye");

    String JasperStockQuerry; // already exists presumably
    List<Object> JasperStockParams; // add this

    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    HashMap<String, String> intidmap = new HashMap<>();

    boolean saparateBranch = false;
    HashMap<Integer, String> locationMap = new HashMap<>();

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

            String stockTableQuerry = "SELECT * FROM `stock`"
                    + " INNER JOIN `product` ON `product`.`intid` = `stock`.`product_intid`"
                    + " INNER JOIN `supplier` ON `supplier`.`supplier_id` = `stock`.`supplier_supplier_id` "
                    + " INNER JOIN `location` ON `location`.`id` = `stock`.`location_id` "
                    + " INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` "
                    + " INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` ";

            if (saparateBranch == true) {
                stockTableQuerry += " WHERE `location`.`id` = '" + UserDetails.UserLocation_id + "'";
            }

            ResultSet rs = MySQL.execute(stockTableQuerry);
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
                v.add(rs.getDouble("saling_price"));
                v.add(rs.getDate("stock_date"));
                v.add(rs.getString("Supplier_Name"));
                v.add(rs.getString("location_name"));
                v.add(rs.getString("color"));
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

            String locationquerry = "SELECT * FROM `location`  ";
            if (saparateBranch == true) {
                locationquerry += " WHERE id = '" + UserDetails.UserLocation_id + "'";
            }
            locationquerry += " ORDER BY `id` ASC";

            ResultSet rs = MySQL.execute(locationquerry);
            Vector v = new Vector();

            v.add("Select Category");
            while (rs.next()) {
                v.add(String.valueOf(rs.getString("id") + ") " + rs.getString("location_name")));
                locationMap.put(Integer.parseInt(rs.getString("id")), rs.getString("location_name"));
                System.out.println("the size of location map is" + locationMap.size());
            }

            DefaultComboBoxModel dfm = new DefaultComboBoxModel<>(v);
            jComboBox3.setModel(dfm);
            jComboBox1.setModel(dfm);

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
        LoadSettings();
        AddinProductStock("");
        supplierTable();
        loadLocations();
        LoadStockTable();
        loadSuppiers();
        jTextField11.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField10.setText("");
        jTextField4.setText("");
        jTextField15.setText("");
    }

    public void loadSuppiers() {
        try {
            String locationquerry = "SELECT * FROM `supplier`  ";

            ResultSet rs = MySQL.execute(locationquerry);
            Vector v = new Vector();

            v.add("Select Suppliers");
            while (rs.next()) {
                v.add(String.valueOf(rs.getString("Supplier_Name")));
            }

            DefaultComboBoxModel dfm = new DefaultComboBoxModel<>(v);
            jComboBox4.setModel(dfm);

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", "Please Check Your Internet Connection or Please Try again later", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadSettings() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `settings` WHERE `setting_id` =  '1'");
            if (rs.next()) {
                boolean isSeprateBranch = rs.getBoolean("is_saperate_branches");
                System.out.println("Is separate" + isSeprateBranch);

                if (isSeprateBranch == true) {
                    saparateBranch = true;
                } else {
                    saparateBranch = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void importExcelFile() {
        System.out.println("=== importExcelFile() STARTED ===");

        JFileChooser fileChooser = new JFileChooser();
        System.out.println("[1] File chooser opened");

        int result = fileChooser.showOpenDialog(this);
        System.out.println("[2] File chooser result code: " + result);

        if (result == JFileChooser.CANCEL_OPTION) {
            System.out.println("[2a] User CANCELLED file chooser. Exiting.");
            return;
        }

        if (result != JFileChooser.APPROVE_OPTION) {
            System.out.println("[2b] Unexpected result from file chooser: " + result + ". Exiting.");
            return;
        }

        File chooseFile = fileChooser.getSelectedFile();
        System.out.println("[3] File selected: " + chooseFile.getAbsolutePath());

        // Check file extension
        if (!isValidExcelFile(chooseFile)) {
            System.out.println("[3a] INVALID file type selected: " + chooseFile.getName() + ". Exiting.");
            JOptionPane.showMessageDialog(this,
                    "Please Select Valid Excel File (xlsx, xls, xlsm, xlsb, xltx, xltm)",
                    "Invalid File",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.out.println("[3b] File type is VALID: " + chooseFile.getName());

        try {
            System.out.println("[4] Starting database load...");
            JOptionPane.showMessageDialog(this, "Loading data from database...", "Import", JOptionPane.INFORMATION_MESSAGE);

            // Load database data
            System.out.println("[4a] Loading brands...");
            Map<String, String> brandMap = loadBrandsForImport();
            System.out.println("[4a] Brands loaded: " + (brandMap != null ? brandMap.size() : "NULL"));

            System.out.println("[4b] Loading products...");
            Map<String, String[]> productMap = loadProductsForImport();
            System.out.println("[4b] Products loaded: " + (productMap != null ? productMap.size() : "NULL"));

            System.out.println("[4c] Loading locations...");
            Map<String, String> locationMap = loadLocationsForImport();
            System.out.println("[4c] Locations loaded: " + (locationMap != null ? locationMap.size() : "NULL"));

            System.out.println("[4d] Loading sub-categories...");
            Map<String, String> subCategoryMap = loadSubCategoriesForImport();
            System.out.println("[4d] Sub-categories loaded: " + (subCategoryMap != null ? subCategoryMap.size() : "NULL"));

            System.out.println("[4e] Loading suppliers...");
            Map<String, String> supplierMap = loadSuppliersForImport();
            System.out.println("[4e] Suppliers loaded: " + (supplierMap != null ? supplierMap.size() : "NULL"));

            // Null checks
            if (brandMap == null || productMap == null || locationMap == null
                    || subCategoryMap == null || supplierMap == null) {
                System.out.println("[ERROR] One or more database maps returned NULL!");
                System.out.println("  brandMap      -> " + (brandMap == null ? "NULL ❌" : "OK ✅"));
                System.out.println("  productMap    -> " + (productMap == null ? "NULL ❌" : "OK ✅"));
                System.out.println("  locationMap   -> " + (locationMap == null ? "NULL ❌" : "OK ✅"));
                System.out.println("  subCategoryMap-> " + (subCategoryMap == null ? "NULL ❌" : "OK ✅"));
                System.out.println("  supplierMap   -> " + (supplierMap == null ? "NULL ❌" : "OK ✅"));
                return;
            }
            System.out.println("[4f] All database maps loaded successfully.");

            // Build lookup maps
            System.out.println("[5] Building name-to-ID lookup maps...");

            Map<String, String> brandNameToIdMap = new HashMap<>();
            for (Map.Entry<String, String> entry : brandMap.entrySet()) {
                brandNameToIdMap.put(entry.getValue().toLowerCase(), entry.getKey());
            }
            System.out.println("[5a] brandNameToIdMap built: " + brandNameToIdMap.size() + " entries");

            Map<String, String> locationNameToIdMap = new HashMap<>();
            for (Map.Entry<String, String> entry : locationMap.entrySet()) {
                locationNameToIdMap.put(entry.getKey().toLowerCase(), entry.getValue());
            }
            System.out.println("[5b] locationNameToIdMap built: " + locationNameToIdMap.size() + " entries");

            Map<String, String> subCategoryNameToIdMap = new HashMap<>();
            for (Map.Entry<String, String> entry : subCategoryMap.entrySet()) {
                subCategoryNameToIdMap.put(entry.getValue().toLowerCase(), entry.getKey());
            }
            System.out.println("[5c] subCategoryNameToIdMap built: " + subCategoryNameToIdMap.size() + " entries");

            Map<String, String> supplierNameToIdMap = new HashMap<>();
            for (Map.Entry<String, String> entry : supplierMap.entrySet()) {
                supplierNameToIdMap.put(entry.getValue().toLowerCase(), entry.getKey());
            }
            System.out.println("[5d] supplierNameToIdMap built: " + supplierNameToIdMap.size() + " entries");

            // Process Excel
            System.out.println("[6] Starting Excel file processing...");
            JOptionPane.showMessageDialog(this, "Processing Excel file...", "Import", JOptionPane.INFORMATION_MESSAGE);

            ImportResult importResult = processExcelFileForImport(chooseFile, brandMap, brandNameToIdMap,
                    productMap, locationNameToIdMap, subCategoryNameToIdMap, supplierNameToIdMap);

            System.out.println("[6] Excel processing complete.");
            System.out.println("  New Brands   : " + importResult.newBrands.size());
            System.out.println("  New Suppliers: " + importResult.newSuppliers.size());
            System.out.println("  New Products : " + importResult.newProducts.size());
            System.out.println("  Stock Entries: " + importResult.newStocks.size());

            // Confirm dialog
            System.out.println("[7] Showing confirm dialog to user...");
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
                System.out.println("[7a] User chose NOT to proceed with import. Exiting.");
                return;
            }
            System.out.println("[7b] User confirmed import. Proceeding...");

            // Insert data
            System.out.println("[8] Inserting imported data into database...");
            JOptionPane.showMessageDialog(this, "Inserting data into database...", "Import", JOptionPane.INFORMATION_MESSAGE);

            insertImportedData(importResult, brandMap, brandNameToIdMap, productMap,
                    subCategoryMap, subCategoryNameToIdMap, supplierMap, supplierNameToIdMap);

            System.out.println("[8] Data insertion complete.");

            // Refresh UI
            System.out.println("[9] Refreshing UI...");
            refresh();
            System.out.println("[9] UI refreshed.");

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

            System.out.println("=== importExcelFile() COMPLETED SUCCESSFULLY ===");

        } catch (Exception e) {
            System.out.println("[EXCEPTION] Error during import!");
            System.out.println("  Exception type   : " + e.getClass().getName());
            System.out.println("  Exception message: " + e.getMessage());
            e.printStackTrace(); // Full stack trace in console
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

    private void downloadTemplateExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Import Template");
        fileChooser.setSelectedFile(new File("Import_Stock_Template.xlsx"));

        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return; // user cancelled
        }

        File saveFile = fileChooser.getSelectedFile();

        // Make sure it ends with .xlsx
        if (!saveFile.getName().toLowerCase().endsWith(".xlsx")) {
            saveFile = new File(saveFile.getAbsolutePath() + ".xlsx");
        }

        // Warn before overwrite
        if (saveFile.exists()) {
            int overwrite = JOptionPane.showConfirmDialog(this,
                    "File already exists. Overwrite it?",
                    "Confirm Overwrite",
                    JOptionPane.YES_NO_OPTION);
            if (overwrite != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try (Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");

            String[] headers = {
                "Product id", "sub_category", "Brand Name", "Quality ",
                "product_name", "supplie", "location Name", "cost",
                "saling_price", "qty", "color", "SKU"
            };

            // Header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Sample row (matches the format your import expects)
            Row sampleRow = sheet.createRow(1);
            sampleRow.createCell(0).setCellValue(2001);          // Product id
            sampleRow.createCell(1).setCellValue("FULL FRAME");  // sub_category
            sampleRow.createCell(2).setCellValue("Boss");        // Brand Name
            sampleRow.createCell(3).setCellValue("GRADE B");     // Quality
            sampleRow.createCell(4).setCellValue("");            // product_name
            sampleRow.createCell(5).setCellValue("NO ID");       // supplier
            sampleRow.createCell(6).setCellValue("MATHUGAMA");   // location Name
            sampleRow.createCell(7).setCellValue(0);             // cost
            sampleRow.createCell(8).setCellValue(15000);         // saling_price
            sampleRow.createCell(9).setCellValue(1);             // qty
            sampleRow.createCell(10).setCellValue("");           // color
            sampleRow.createCell(11).setCellValue("");           // SKU

            // Auto-size columns for readability
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(saveFile)) {
                workbook.write(fos);
            }

            JOptionPane.showMessageDialog(this,
                    "Template saved successfully:\n" + saveFile.getAbsolutePath(),
                    "Download Complete",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error creating template file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton11 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
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
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButton10 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel52 = new javax.swing.JLabel();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jButton14 = new javax.swing.JButton();
        jTextField21 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jButton16 = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jToggleButton3 = new javax.swing.JToggleButton();
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

        jToggleButton1.setText("Short Inspections");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jButton11.setText("Print New Short Inspection");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(206, 206, 206));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel6.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 520, 402, 10));

        jLabel13.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel13.setText("Stock Infomations");
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));
        jPanel6.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 480, 80, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jPanel6.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 448, -1, -1));

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
        jPanel6.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 430, 120, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel15.setText("Stock Date");
        jPanel6.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 410, -1, -1));

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

        jPanel6.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 460, 230, 160));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Row No", "Stock ID", "Product ID", "Product Brand", "Sub Category", "Qty", "Selling Price", "Date TIme", "Supplier", "Location", "Color", "SKU"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jPanel6.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 1104, 257));

        jLabel16.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel16.setText("Sub Category");
        jPanel6.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 410, -1, -1));

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
        jPanel6.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 430, 230, -1));

        jLabel24.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel24.setText("Quantity");
        jPanel6.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 460, -1, -1));

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
        jPanel6.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 120, -1));

        jLabel25.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel25.setText("Product ID");
        jPanel6.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, -1, -1));

        jLabel26.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel26.setText("Supplier Company");
        jPanel6.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 410, 120, -1));
        jPanel6.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 430, 119, -1));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        jPanel6.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 430, 111, -1));

        jLabel28.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel28.setText("Location");
        jPanel6.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 410, -1, -1));

        jLabel30.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel30.setText("Selling Price");
        jPanel6.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 460, 121, -1));
        jPanel6.add(jTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 480, 176, 30));
        jPanel6.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, 1025, 10));

        jLabel29.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel29.setText("Add Stock");
        jPanel6.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, -1, -1));

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
        jPanel6.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 430, 110, -1));

        jLabel32.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel32.setText("Product Brand");
        jPanel6.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 410, -1, -1));

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

        jPanel6.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 460, 380, 160));

        jLabel31.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel31.setText("SKU No");
        jPanel6.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 410, -1, -1));

        SKUNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SKUNOActionPerformed(evt);
            }
        });
        jPanel6.add(SKUNO, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 430, -1, -1));
        jPanel6.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 480, 124, 30));

        jLabel1.setText("Frame Size (optional)");
        jPanel6.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 460, 120, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jLabel17.setText("Search Option ");
        jPanel6.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        jLabel18.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel18.setText("Stock ID");
        jPanel6.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });
        jPanel6.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 120, -1));
        jPanel6.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 140, -1));

        jLabel19.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel19.setText("SKU ");
        jPanel6.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, -1, -1));

        jLabel27.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel27.setText("Product ID");
        jPanel6.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, -1, -1));

        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });
        jPanel6.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, 120, -1));

        jLabel20.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel20.setText("Supplier");
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, -1, -1));

        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });
        jPanel6.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 70, 120, -1));

        jLabel21.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel21.setText("Location");
        jPanel6.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 50, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel6.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 70, 120, -1));

        jLabel23.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel23.setText("Brand");
        jPanel6.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 50, -1, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel22.setText("Stock Date");
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 50, -1, -1));
        jPanel6.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 70, 140, -1));

        jButton10.setText("Search");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 70, 70, -1));
        jPanel6.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 70, 130, -1));

        jTabbedPane1.addTab("Add Only Stock", jPanel6);

        jPanel7.setBackground(new java.awt.Color(206, 206, 206));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel33.setText("GRN (Purchasing) Infomation");
        jPanel7.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        jLabel34.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jPanel7.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 448, -1, -1));

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable5MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTable5);
        if (jTable5.getColumnModel().getColumnCount() > 0) {
            jTable5.getColumnModel().getColumn(0).setResizable(false);
            jTable5.getColumnModel().getColumn(1).setResizable(false);
        }

        jPanel7.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 170, 320, 200));
        jPanel7.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 389, 1025, 4));

        jLabel45.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jLabel45.setText("Actions");
        jPanel7.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 170, -1, -1));

        jLabel46.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel46.setText("Grn Invoice No");
        jPanel7.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));

        jTextField20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField20ActionPerformed(evt);
            }
        });
        jPanel7.add(jTextField20, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 140, 100, -1));

        jLabel47.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jPanel7.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 71, -1, -1));

        jLabel49.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel49.setText("Supplier");
        jPanel7.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 110, -1, -1));

        jTextField23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField23ActionPerformed(evt);
            }
        });
        jPanel7.add(jTextField23, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 140, 120, -1));

        jLabel50.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel50.setText("Location");
        jPanel7.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 110, -1, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 140, 120, -1));

        jLabel52.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel52.setText("Stock Date");
        jPanel7.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 110, -1, -1));
        jPanel7.add(jDateChooser4, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 140, 140, -1));

        jButton14.setText("Search");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 140, 70, -1));

        jTextField21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField21ActionPerformed(evt);
            }
        });
        jPanel7.add(jTextField21, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 160, -1));

        jLabel2.setText("GRN Date");
        jPanel7.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));
        jPanel7.add(jDateChooser3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 160, -1));

        jLabel3.setText("Supplier");
        jPanel7.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        jLabel4.setText("GRN invoice ID");
        jPanel7.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, -1));
        jPanel7.add(jTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 160, -1));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 130, -1));

        jLabel8.setText("GRN Amount");
        jPanel7.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, -1, -1));

        jButton13.setText("Add GRN invoice");
        jPanel7.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 160, -1));

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/excel-file (1).png"))); // NOI18N
        jButton15.setText("Import Stock Excel for the GRN");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 190, 270, 60));

        jToggleButton2.setText("Delete GRN");
        jPanel7.add(jToggleButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 260, 270, 50));

        jButton16.setText("Update GRN");
        jPanel7.add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 320, 270, 50));

        jLabel48.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jLabel48.setText("Search Purchasing invoices in here");
        jPanel7.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, -1, -1));

        jLabel51.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jLabel51.setText("Add Or Update GRN");
        jPanel7.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/SmallSettings.png"))); // NOI18N
        jPanel7.add(jToggleButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 320, 30, 20));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1131, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 1131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 692, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 692, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Add Stock With GRN", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jSeparator3)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6)
                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSeparator4)
                                .addComponent(jLabel7)
                                .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jSeparator8))
                            .addComponent(jToggleButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                .addGap(18, 18, 18)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
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
                        .addGap(8, 8, 8)
                        .addComponent(jLabel5)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a product from the table first",
                    "No Product Selected",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        String productintid = String.valueOf(jTable4.getValueAt(row, 1));
        System.out.println(productintid);

        Date chooseDate = jDateChooser2.getDate(); // null if nothing picked - no need for try/catch
        String formatDate = null;
        if (chooseDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            formatDate = sdf.format(chooseDate);
        }

        // Location for the actual insert - parsed from jComboBox1 ("id) name" format)
        int locationId = 0;
        if (jComboBox1.getSelectedIndex() != 0) {
            String selected = String.valueOf(jComboBox1.getSelectedItem());
            String idPart = selected.split("\\)")[0].trim(); // e.g. "12) Main Branch" -> "12"
            try {
                locationId = Integer.parseInt(idPart);
            } catch (NumberFormatException nfe) {
                locationId = 0;
            }
        }

        String supplier_ID = null;
        int supplierRow = jTable2.getSelectedRow();
        if (supplierRow != -1) {
            String supplierTableRow = String.valueOf(jTable2.getValueAt(supplierRow, 0));
            String[] supplier_array = supplierTableRow.split("->");
            if (supplier_array.length > 0) {
                supplier_ID = supplier_array[0];
            }
        }

        String Qty = jTextField4.getText();

        double SellingPrice;
        boolean validSellingPrice = true;
        try {
            SellingPrice = Double.parseDouble(jTextField15.getText());
            if (SellingPrice <= 0.0) {
                validSellingPrice = false;
            }
        } catch (NumberFormatException e) {
            SellingPrice = 0.0;
            validSellingPrice = false;
        }

        // Validation - checked before touching the database
        if (Product_id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Select or Type Product ID", "Empty Parameters", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (supplier_ID == null) {
            JOptionPane.showMessageDialog(this, "Please Select Supplier from Table", "Empty Parameters", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (formatDate == null) {
            JOptionPane.showMessageDialog(this, "Please Select a Date", "Empty Parameters", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (locationId == 0) {
            JOptionPane.showMessageDialog(this, "Please Select a Location", "Empty Parameters", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (Qty.isEmpty() || !Qty.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Please Enter a Valid Quantity", "Invalid Data Type", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validSellingPrice) {
            JOptionPane.showMessageDialog(this, "Please Enter a Valid Selling Price", "Invalid Parameters", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Confirm the product actually exists before inserting stock against it
            String checkSql = "SELECT * FROM `product` WHERE `product`.`id` = ? AND `product`.`intid` = ?";
            try (PreparedStatement checkPs = MySQL.getConnection().prepareStatement(checkSql)) {
                checkPs.setString(1, Product_id);
                checkPs.setString(2, productintid);
                ResultSet p_rs = checkPs.executeQuery();
                if (!p_rs.next()) {
                    JOptionPane.showMessageDialog(this, "Invalid Product ID", "Invalid Parameters", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            String insertSql = "INSERT INTO `stock` "
                    + "(`product_id`,`supplier_supplier_id`,`location_id`,`cost`,`saling_price`,`stock_date`,`qty`,`product_intid`,`SKU`,`FrameSize`) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?)";

            try (PreparedStatement ps = MySQL.getConnection().prepareStatement(insertSql)) {
                ps.setString(1, Product_id);
                ps.setString(2, supplier_ID);
                ps.setInt(3, locationId);
                ps.setInt(4, 0); // cost
                ps.setDouble(5, SellingPrice);
                ps.setString(6, formatDate);
                ps.setInt(7, Integer.parseInt(Qty));
                ps.setString(8, productintid);
                ps.setString(9, SKUNO.getText());
                ps.setString(10, FrameSize);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Stock Added Successfully", "Insert Success", JOptionPane.INFORMATION_MESSAGE);
            new Thread(() -> ActivityLog.addLog("New stock entered by " + UserDetails.UserName,3)).start();
            refresh();

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Network or Try Again Later", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An Unexpected Error Occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        // Download sotck import excel sheet ->
        downloadTemplateExcel();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy-MM-dd");
        String toDate;
        try {
            toDate = simpleDateformat.format(jDateChooser1.getDate());
            jLabel22.setText(toDate);
        } catch (NullPointerException ne) {
            toDate = null;
        }

// Explicit column aliases avoid ambiguous "id" collisions across stock/location/brand/sub_category
        String baseQuery = "SELECT "
                + "`stock`.`id` AS stock_id, "
                + "`stock`.`product_intid` AS product_intid, "
                + "`stock`.`product_id` AS product_id, " // fixed: comes from stock, not product
                + "`stock`.`qty` AS qty, "
                + "`stock`.`saling_price` AS saling_price, "
                + "`stock`.`stock_date` AS stock_date, "
                + "`stock`.`SKU` AS SKU, "
                + "`stock`.`color` AS color, "
                + "`brand`.`brand_name` AS brand_name, "
                + "`sub_category`.`sub_category` AS sub_category, "
                + "`supplier`.`Supplier_Name` AS Supplier_Name, "
                + "`location`.`location_name` AS location_name "
                + "FROM `stock` "
                + "INNER JOIN `product` ON `product`.`intid` = `stock`.`product_intid` "
                + "INNER JOIN `supplier` ON `supplier`.`supplier_id` = `stock`.`supplier_supplier_id` "
                + "INNER JOIN `location` ON `location`.`id` = `stock`.`location_id` "
                + "INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` "
                + "INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id`";

        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (!jTextField7.getText().isEmpty()) {
            conditions.add("`stock`.`id` = ?");
            params.add(jTextField7.getText());
        } else if (!jTextField8.getText().isEmpty()) {
            conditions.add("`product`.`id` = ?");
            params.add(jTextField8.getText());
        } else if (!jTextField9.getText().isEmpty()) {
            conditions.add("`supplier`.`supplier_id` LIKE ?");
            params.add("%" + jTextField9.getText() + "%");
        } else if (!jTextField2.getText().isEmpty()) {
            conditions.add("`stock`.`SKU` = ?");
            params.add(jTextField2.getText());
        }

        if (jComboBox1.getSelectedIndex() != 0) {
            // Combo box items are expected in "id - Name" format; take the id token,
            // not just the first character (fixes breakage once IDs reach double digits)
            String selected = String.valueOf(jComboBox1.getSelectedItem());
            String location_id = selected.split(" - ")[0].trim();
            conditions.add("`stock`.`location_id` = ?");
            params.add(location_id);
        } else if (saparateBranch) {
            conditions.add("`stock`.`location_id` = ?");
            params.add(UserDetails.UserLocation_id);
        }

        if (!jTextField3.getText().isEmpty()) {
            // Match the full brand name as typed, instead of only the second word
            String brand = jTextField3.getText().trim();
            conditions.add("`brand`.`brand_name` = ?");
            params.add(brand);
        }

        if (toDate != null) {
            // "as of / up to" this date — flip to >= if this should be a lower bound instead
            conditions.add("`stock`.`stock_date` <= ?");
            params.add(toDate);
        }

        StringBuilder querry = new StringBuilder(baseQuery);
        if (!conditions.isEmpty()) {
            querry.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        System.out.println(querry);
        JasperStockQuerry = querry.toString();
        JasperStockParams = new ArrayList<>(params); // keep params alongside for Jasper if needed

        try (PreparedStatement ps = MySQL.getConnection().prepareStatement(querry.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
                dtm.setRowCount(0);
                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getInt("product_intid"));
                    v.add(rs.getInt("stock_id"));
                    v.add(rs.getString("product_id"));
                    v.add(rs.getString("brand_name"));
                    v.add(rs.getString("sub_category"));
                    v.add(rs.getInt("qty"));
                    v.add(rs.getDouble("saling_price"));
                    v.add(rs.getDate("stock_date"));
                    v.add(rs.getString("Supplier_Name"));
                    v.add(rs.getString("location_name"));
                    v.add(rs.getString("color"));
                    v.add(rs.getString("SKU"));
                    dtm.addRow(v);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Data failed to load", e);
        }

    }//GEN-LAST:event_jButton10ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // Short Inspecitons
        ShortInspection si = new ShortInspection("1");
        si.setVisible(true);
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        ShortInspection si = new ShortInspection("2");
        si.setVisible(true);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable5MouseClicked

    private void jTextField20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField20ActionPerformed

    private void jTextField23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField23ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jTextField21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField21ActionPerformed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

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
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JLabel timeField;
    private javax.swing.JLabel userNameField;
    // End of variables declaration//GEN-END:variables
}
