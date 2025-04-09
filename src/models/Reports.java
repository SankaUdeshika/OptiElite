package models;

//import gui.StockManagement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.MySQL;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;


public class Reports {

    public static void OrderPurchaceInvoice(String id) {
        try {
            String tintName = "";

            String coatingName = "";

            String DesignName = "";

            String lensTypeName = "";

            String lens_brandName = "";

            try {
                //          Search Lens Tint 
                ResultSet Tint_Rs = MySQL.execute("SELECT * FROM `invoice_tint` INNER JOIN `tint` ON `tint`.`l_tint_id` = `invoice_tint`.`tint_l_tint_id` WHERE `invoice_invoice_id` = '" + id + "'");
                if (Tint_Rs.next()) {
                    tintName = Tint_Rs.getString("l_tint");

                } else {
                    tintName = "-";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                //          Search Lens Coating 
                ResultSet Coating_Rs = MySQL.execute("SELECT * FROM `invoice_coating` INNER JOIN `coating` ON `coating`.`l_coating_id` = `invoice_coating`.`coating_l_coating_id` WHERE `invoice_invoice_id` = '" + id + "'");
                if (Coating_Rs.next()) {
                    coatingName = Coating_Rs.getString("l_coating");

                } else {
                    coatingName = "-";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                //          Search Lens Design 
                ResultSet Coating_Rs = MySQL.execute("SELECT * FROM `invoice_design` INNER JOIN `design` ON `design`.`l_design_id` = `invoice_design`.`design_l_design_id` WHERE `invoice_invoice_id` = '" + id + "'");
                if (Coating_Rs.next()) {
                    DesignName = Coating_Rs.getString("l_design");

                } else {
                    DesignName = "-";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                //          Search Lens Type 
                ResultSet Coating_Rs = MySQL.execute("SELECT * FROM `invoice_lens_type` INNER JOIN `lens_type` ON `lens_type`.`l_type_id` = `invoice_lens_type`.`lens_type_l_type_id` WHERE `invoice_invoice_id` = '" + id + "'");
                if (Coating_Rs.next()) {
                    lensTypeName = Coating_Rs.getString("l_type");

                } else {
                    lensTypeName = "-";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                //          Search Lens band 
                ResultSet Coating_Rs = MySQL.execute("SELECT * FROM `invoice_lens_brand` INNER JOIN `lens_brand` ON `invoice_lens_brand`.`lens_brand_l_brand_id` = `lens_brand`.`l_brand_id` WHERE `invoice_invoice_id` = '" + id + "'");
                if (Coating_Rs.next()) {
                    lens_brandName = Coating_Rs.getString("l_brand");

                } else {
                    lens_brandName = "-";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//            get Details
            HashMap<String, Object> reportmap = new HashMap<>();

            reportmap.put("id", id);
            reportmap.put("tintName", tintName);
            reportmap.put("coatingName", coatingName);
            reportmap.put("DesignName", DesignName);
            reportmap.put("lensTypeName", lensTypeName);
            reportmap.put("lens_brandName", lens_brandName);

//            check if stock is available
            ResultSet rs = MySQL.execute("SELECT * FROM `invoice_item` WHERE `invoice_id` = '" + id + "' ");

            if (rs.next()) {
                System.out.println(" product invoice id is " + reportmap.get("id"));
                JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/NewEagleEyeOrderReport.jasper"), reportmap, MySQL.getConnection());
                JasperViewer.viewReport(jasperPrint, false);
            } else {
                System.out.println("lens invoice id is " + reportmap.get("id"));
                JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/Blank_A4_Landscape_1.jasper"), reportmap, MySQL.getConnection());
                JasperViewer.viewReport(jasperPrint, false);
            }

//            JasperPrint jasperPrint = JasperFillManager.fillReport("reports/EagleEyeOrders.jasper", reportmap, MySQL.getConnection());
//            JasperPrint jasperPrint = JasperFillManager.fillReport("reports/EagleEyeOrders.jasper", reportmap,new JRTableModelDataSource(model));
        } catch (JRException ex) {
            ex.printStackTrace();
            Logger.getLogger(Reports.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Reports.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void WithoutPrescriptionOrderPurchaceInvoice(String id) {
        try {
            HashMap<String, Object> reportmap = new HashMap<>();
            reportmap.put("id", id);
            JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/Withoutprescription.jasper"), reportmap, MySQL.getConnection());

//            JasperPrint jasperPrint = JasperFillManager.fillReport("reports/EagleEyeOrders.jasper", reportmap, MySQL.getConnection());
//            JasperPrint jasperPrint = JasperFillManager.fillReport("reports/EagleEyeOrders.jasper", reportmap,new JRTableModelDataSource(model));
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException ex) {
            ex.printStackTrace();
            Logger.getLogger(Reports.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void PrintPrescription(String jobno) {
        try {
            HashMap<String, Object> reportmap = new HashMap<>();
            reportmap.put("jobno", jobno);
            JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/NewPrescription.jasper"), reportmap, MySQL.getConnection());

//            JasperPrint jasperPrint = JasperFillManager.fillReport("reports/EagleEyeOrders.jasper", reportmap, MySQL.getConnection());
//            JasperPrint jasperPrint = JasperFillManager.fillReport("reports/EagleEyeOrders.jasper", reportmap,new JRTableModelDataSource(model));
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException ex) {
            ex.printStackTrace();
            Logger.getLogger(Reports.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void PrintStockReport(DefaultTableModel TableModel) {
        try {

            HashMap<String, Object> reportmap = new HashMap<>();

//            JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/StockReport.jasper"), reportmap, MySQL.getConnection());
//            JasperPrint jasperPrint = JasperFillManager.fillReport("reports/EagleEyeOrders.jasper", reportmap, MySQL.getConnection());
            JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/StockReport.jasper"), reportmap, new JRTableModelDataSource(TableModel));
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException ex) {
            ex.printStackTrace();
            Logger.getLogger(Reports.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void printCustomerReport(DefaultTableModel tableModel) {
        try {
            HashMap<String, Object> reportmap = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/CustomerReport.jasper"), reportmap, new JRTableModelDataSource(tableModel));
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printUserReport(DefaultTableModel tableModel) {
        try {
            HashMap<String, Object> reportmap = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/userReport.jasper"), reportmap, new JRTableModelDataSource(tableModel));
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
