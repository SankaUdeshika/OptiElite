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

    public static void JobOrderPrints(String id) {
        try {
            // check if prescription have or no
            ResultSet prescription_rs = MySQL.execute("SELECT * FROM `invoice` WHERE `invoice_id` = '" + id + "' AND  `prescription_details_job_no` IS NULL");

            if (prescription_rs.next()) {
                //invoice without Prescription Details
                System.out.println("NO Prescription.");

                try {
                    // get Details
                    HashMap<String, Object> reportmap = new HashMap<>();
                    reportmap.put("id", id);

                    // check if stock is available
                    ResultSet rs = MySQL.execute("SELECT * FROM `invoice_item` WHERE `invoice_id` = '" + id + "' ");

                    if (rs.next()) {
                        System.out.println(" product invoice id is " + reportmap.get("id"));
                        //JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/NewEagleEyeOrderReport.jasper"), reportmap, MySQL.getConnection());
                        JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/Optielite_job_card.jasper"), reportmap, MySQL.getConnection());

                        JasperViewer.viewReport(jasperPrint, false);
                    } else {
                        System.out.println("lens invoice id is " + reportmap.get("id"));
                        JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/JobCard.jasper"), reportmap, MySQL.getConnection());
                        //JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/Blank_A4_Landscape_1.jasper"), reportmap, MySQL.getConnection());
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

            } else {
                System.out.println("With Prescription");

                //invoice with Prescription
                try {
                    // get Details
                    HashMap<String, Object> reportmap = new HashMap<>();
                    reportmap.put("id", id);

                    // check if stock is available
                    ResultSet rs = MySQL.execute("SELECT * FROM `invoice_item` WHERE `invoice_id` = '" + id + "' ");

                    if (rs.next()) {
                        System.out.println(" product invoice id is " + reportmap.get("id"));
                        //JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/NewEagleEyeOrderReport.jasper"), reportmap, MySQL.getConnection());
                        JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/Optielite_job_card.jasper"), reportmap, MySQL.getConnection());

                        JasperViewer.viewReport(jasperPrint, false);
                    } else {
                        System.out.println("lens invoice id is " + reportmap.get("id"));
                        JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/JobCard.jasper"), reportmap, MySQL.getConnection());
                        //JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/Blank_A4_Landscape_1.jasper"), reportmap, MySQL.getConnection());
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

        } catch (Exception e) {
        }
    }

    public static void OrderPurchaceInvoice(String id) {
        try {
//            get Details
            HashMap<String, Object> reportmap = new HashMap<>();
            reportmap.put("id", id);

//            check if stock is  available in invoice item.
            ResultSet rs = MySQL.execute("SELECT * FROM `invoice_item` WHERE `invoice_id` = '" + id + "' ");

            if (rs.next()) {
                // Frame Purhase

                ResultSet invoice_rs = MySQL.execute("SELECT * FROM `invoice` WHERE `invoice_id` ='" + id + "'  ");
                if (invoice_rs.next()) {
                    if (invoice_rs.getInt("lens_stock_lens_id") == 0) {
                        // without Lens
                        System.out.println(" product invoice id is " + reportmap.get("id"));
                        JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/NoLensOptielite_order_invoice.jasper"), reportmap, MySQL.getConnection());
                        JasperViewer.viewReport(jasperPrint, false);
                    } else {
                        // with lens
                        System.out.println(" product invoice id is " + reportmap.get("id"));
                        JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/Optielite_order_invoice.jasper"), reportmap, MySQL.getConnection());
                        JasperViewer.viewReport(jasperPrint, false);
                    }
                }

            } else {
                // lens Purchase
                System.out.println("lens invoice id is " + reportmap.get("id"));
                JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/NoFrameOptielite_order_invoice.jasper"), reportmap, MySQL.getConnection());
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
