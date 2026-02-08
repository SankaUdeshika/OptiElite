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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
                        JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/NoFrame_Optielite_job_card.jasper"), reportmap, MySQL.getConnection());
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
                        JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/NoFrame_Optielite_job_card.jasper"), reportmap, MySQL.getConnection());
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
            e.printStackTrace();
        }
    }

    public static void OrderPurchaceInvoice(String id) {
        try {

            //            get Details
            HashMap<String, Object> reportmap = new HashMap<>();
            reportmap.put("id", id);

            // Pyament History
            int count = 0;
            ArrayList<String> list = new ArrayList<>();
            ResultSet paymentResult = MySQL.execute("SELECT * FROM `advance_payment_history` WHERE `invoice_invoice_id` = '" + id + "' ");

            while (paymentResult.next()) {
                count++;
                reportmap.put("payment" + count, paymentResult.getString("date") + " = " + paymentResult.getString("paid_amount") + " Paid");
            }

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }

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

    public static void WithoutPrescriptionOrderPurchaceInvoice(String id) throws SQLException {
        try {
            HashMap<String, Object> reportmap = new HashMap<>();
            reportmap.put("id", id);

            // Pyament History
            int count = 0;
            ArrayList<String> list = new ArrayList<>();
            ResultSet paymentResult = MySQL.execute("SELECT * FROM `advance_payment_history` WHERE `invoice_invoice_id` = '" + id + "' ");

            while (paymentResult.next()) {
                count++;
                reportmap.put("payment" + count, paymentResult.getString("date") + " = " + paymentResult.getString("paid_amount") + " Paid");
            }

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }

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

    public static void printDailyReport(String reportId) {
        try {

            HashMap<String, Object> reportmap = new HashMap<>();
            reportmap.put("id", reportId);
            String reportedDate = "";
            String reportedLocation = "";
            //GET expenses
            try {
                ResultSet reportItems = MySQL.execute("SELECT * FROM `report_item` INNER JOIN `daily_report` ON `daily_report`.`report_id` = `report_item`.`daily_report_report_id` WHERE `daily_report_report_id` = '" + reportId + "'");
                int itemCount = 0;
                double totalExpenses = 0.0;

                while (reportItems.next()) {
                    reportedDate = String.valueOf(reportItems.getDate("date"));
                    System.out.println(reportedDate);
                    reportedLocation = String.valueOf(reportItems.getInt("daily_report.location_id"));
                    itemCount++;
                    reportmap.put("description_Item" + String.valueOf(itemCount), reportItems.getString("description"));
                    reportmap.put("price_Item" + String.valueOf(itemCount), String.valueOf(reportItems.getDouble("amount")));
                    totalExpenses += reportItems.getDouble("amount");
                }
                reportmap.put("total_expenses", String.valueOf(totalExpenses));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                double cashCollection = 0;
                double cardCollection = 0;
                double onlinePaymentCollection = 0;
                double totalSellingCollection = 0;
                double BankDeposit = 0;
                double TotalSale = 0;

                System.out.println(reportedDate);

                ResultSet today_payments = MySQL.execute("SELECT * FROM `advance_payment_history` INNER JOIN `payment_method` ON `payment_method`.`Payment_id` = `advance_payment_history`.`payment_method` WHERE `date` = '" + reportedDate + "' AND `advance_payment_history`.`location_id` = '" + reportedLocation + "'");
                while (today_payments.next()) {

                    if (today_payments.getString("payment_name").equals("Cash")) { // Cash
                        cashCollection += today_payments.getDouble("paid_amount");
                    } else if (today_payments.getString("payment_name").equals("Card")) { // Card
                        cardCollection += today_payments.getDouble("paid_amount");
                    } else if (today_payments.getString("payment_name").equals("Online Bank Transfer")) { // Online Payment 
                        onlinePaymentCollection += today_payments.getDouble("paid_amount");
                    }
                    totalSellingCollection += today_payments.getDouble("paid_amount");
                }

                ResultSet rs = MySQL.execute("SELECT SUM(subtotal) AS total_subtotal FROM invoice WHERE date = '" + reportedDate + "' AND `invoice_location` = '" + reportedLocation + "' ");
                if (rs.next()) {
                    TotalSale = rs.getDouble("total_subtotal");
                }

                BankDeposit = cashCollection - Double.parseDouble(String.valueOf(reportmap.get("total_expenses")));
                reportmap.put("cashCollection", String.valueOf(cashCollection));
                reportmap.put("cardCollection", String.valueOf(cardCollection));
                reportmap.put("onlinePaymentCollection", String.valueOf(onlinePaymentCollection));
                reportmap.put("totalSellingCollection", String.valueOf(totalSellingCollection));
                reportmap.put("BankDeposit", String.valueOf(BankDeposit));
                reportmap.put("TotalSale", String.valueOf(TotalSale));

            } catch (Exception e) {
                e.printStackTrace();
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/daily_report.jasper"), reportmap, MySQL.getConnection());
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void PrintRepairNote(String repairId, String invoiceId) {
        try {

            HashMap<String, Object> reportmap = new HashMap<>();
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `invoice_item` WHERE `invoice_id` = '" + invoiceId + "' ");
                if (rs.next()) {
                    reportmap.put("stock_id", rs.getString("stock_id"));
                } else {
                    reportmap.put("stock_id", "No Frame Details");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            reportmap.put("repairId", repairId);
            JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/RepairNote.jasper"), reportmap, MySQL.getConnection());
//            JasperPrint jasperPrint = JasperFillManager.fillReport("reports/EagleEyeOrders.jasper", reportmap, MySQL.getConnection());
//            JasperPrint jasperPrint = JasperFillManager.fillReport("reports/EagleEyeOrders.jasper", reportmap,new JRTableModelDataSource(model));
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException ex) {
            ex.printStackTrace();
            Logger.getLogger(Reports.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Print Echanneling Reprot
    public static void PrintEchanneling(int AppoinmentNo) {
        try {
            HashMap<String, Object> reportmap = new HashMap<>();
            reportmap.put("appoinmentId", AppoinmentNo);
            JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/EChanneling.jasper"), reportmap, MySQL.getConnection());

//            JasperPrint jasperPrint = JasperFillManager.fillReport("reports/EagleEyeOrders.jasper", reportmap, MySQL.getConnection());
//            JasperPrint jasperPrint = JasperFillManager.fillReport("reports/EagleEyeOrders.jasper", reportmap,new JRTableModelDataSource(model));
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException ex) {
            ex.printStackTrace();
            Logger.getLogger(Reports.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void PrintShortInspectionReport(String location) {
        try {
            HashMap<String, Object> reportmap = new HashMap<>();
            reportmap.put("location_id", location);
            JasperPrint jasperPrint = JasperFillManager.fillReport(Reports.class.getResourceAsStream("/reports/ShortInspectionReport.jasper"), reportmap, MySQL.getConnection());

//            JasperPrint jasperPrint = JasperFillManager.fillReport("reports/EagleEyeOrders.jasper", reportmap, MySQL.getConnection());
//            JasperPrint jasperPrint = JasperFillManager.fillReport("reports/EagleEyeOrders.jasper", reportmap,new JRTableModelDataSource(model));
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException ex) {
            ex.printStackTrace();
            Logger.getLogger(Reports.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
