/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import static gui.Login.logger;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import models.MySQL;
import models.UserDetails;
import org.apache.logging.log4j.core.jmx.Server;

/**
 *
 * @author sanka
 */
public class EditOrder extends javax.swing.JFrame {

    /**
     * Creates new form EdiitOrder
     */
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    HashMap<String, String> LensMap = new HashMap<>();
    HashMap<String, String> CortinMap = new HashMap<>();
    HashMap<String, String> BrandMap = new HashMap<>();
    HashMap<String, String> DesingdMap = new HashMap<>();
    HashMap<String, String> TintMap = new HashMap<>();
    HashMap<String, String> warrentyMap = new HashMap<>();
    boolean LensStockAvailable = false;

    public double Total;

    public double RealTotalWithoutDiscount;
    public int qty;

    public double LensTotal = 0;
    public double frame_price = 0;
    public double Discount = 0;
    public String final_discountPercentage = "";
    public double AdvancedPayment = 0;
    public double SubTotal = 0;

    // init datas
    private String customer_mobile;
    private String BaseInvoice_id;
    private String product_id;
    private String frameId;
    private String lensStock_id;
    private String lensCode;

    public EditOrder(String mobile, String invoice_id) {
        initComponents();
        BaseInvoice_id = invoice_id;
        customer_mobile = mobile;
        setSize(screen.width, screen.height);
        findData(invoice_id);
        jTextField2.setEnabled(false);

        jTextField8.setEnabled(false);

        Refresh(mobile);
        operater();
        time();

    }

    private void findData(String invoice_id) {
        try {
            ResultSet rs = MySQL.execute("SELECT product.id AS productID,"
                    + " product.intid AS frameID,"
                    + " invoice.lens_stock_lens_id AS lensID,"
                    + " `lens_stock`.`lens_code` AS lensCode,"
                    + " `invoice`.`lenstotal` AS lensTotal,"
                    + " `invoice`.`frame_total` AS frame_total,"
                    + " `invoice`.`lens_Qty` AS OrderdLensQty, "
                    + " `invoice`.`discount` AS OrderdDiscount, "
                    + " `invoice`.`advance_payment` AS Orderdadvance_payment, "
                    + " `invoice`.`payment_amount` AS OrderPayment_amount, "
                    + " `invoice`.`subtotal` AS subtotal,"
                    + " `invoice`.`discount_percentage` AS discount_percentage "
                    + " FROM `invoice` INNER JOIN invoice_item ON invoice_item.invoice_id  = invoice.invoice_id  INNER JOIN stock ON stock.id = invoice_item.stock_id  LEFT JOIN lens_stock ON lens_stock.lens_id = invoice.lens_stock_lens_id INNER JOIN product ON product.id = stock.product_id INNER JOIN `payment_method` ON `payment_method`.`Payment_id` = `invoice`.`payment_method_Payment_id`  WHERE `invoice`.`invoice_id` = '" + invoice_id + "' ");

            if (rs.next()) {
                product_id = rs.getString("productID");
                frameId = rs.getString("frameID");
                String lensId = rs.getString("lensID");

                // set variable for the summery
                frame_price = rs.getDouble("frame_total");
                Discount = rs.getDouble("OrderdDiscount");
                final_discountPercentage = rs.getString("discount_percentage");
                AdvancedPayment = rs.getDouble("Orderdadvance_payment");
                SubTotal = rs.getDouble("subtotal");

                if (lensId != null) {
                    lensStock_id = lensId;
                    lensCode = rs.getString("lensID");
                    LensTotal = rs.getDouble("lensTotal");

                    // set Variables
                    jTextField5.setText(String.valueOf(rs.getInt("OrderdLensQty")));
                    jTextField5.setText(String.valueOf(rs.getInt("OrderdLensQty")));
                    jTextField3.setText(String.valueOf(rs.getDouble("OrderdDiscount")));
                    jTextField11.setText(String.valueOf(rs.getDouble("Orderdadvance_payment")));
                    jTextField8.setText(String.valueOf(rs.getDouble("OrderPayment_amount")));

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something wrong. please try again later", "Error", JOptionPane.ERROR_MESSAGE);
        }

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
            System.out.println(day);
        };
        Timer timer = new Timer(1000, timerListener);
        timer.setInitialDelay(0);
        timer.start();
    }

    public void Refresh(String mobile) {
        findData(BaseInvoice_id);
        LoadCustomer(mobile);
        jTextField4.setText("");
        jTextField4.setEnabled(false);
        jTable2.setEnabled(false);
        jTextField1.setEnabled(false);
        LoadStockProducts();
        lensLoading();
        LensStockSettings();
        CalculateLensTotal();
        ChangeTotal();
    }

    public void CalculateLensTotal() {
        try {
            // Frame Price
            ResultSet rs = MySQL.execute("SELECT * FROM `stock`  WHERE `id` = '" + jTextField6.getText() + "'  AND `location_id` = '" + UserDetails.UserLocation_id + "' ");
            if (rs.next()) {
                frame_price = rs.getDouble("saling_price");
                System.out.println(frame_price);
                ChangeTotal();
            }

            // Lens Total
            if (!jTextField7.getText().isEmpty()) {
                ResultSet lensRs = MySQL.execute("SELECT * FROM `lens_stock`  WHERE `lens_id` = '" + jTextField7.getText() + "' ");
                if (lensRs.next()) {
                    if (!jTextField7.getText().isEmpty()) {
                        LensTotal = lensRs.getDouble("lens_price") * Double.parseDouble(jTextField5.getText());
                        System.out.println("Lens Price " + LensTotal);
                    }

                    ChangeTotal();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Data failed to load", e);

        }
        ChangeTotal();
    }

    public void ChangeTotal() {

        SubTotal = frame_price + LensTotal;
        Total = SubTotal - Discount;
        Total = Total - AdvancedPayment;

//        output
        jLabel31.setText(String.valueOf(SubTotal));
        jLabel38.setText(String.valueOf(Total));

    }

    public void LoadStockProducts() {
        try {
//          aniwaren Login wenna wenawa
            jTextField6.setText(frameId);
            ResultSet rs = MySQL.execute("SELECT * FROM `stock` INNER JOIN `product` ON `product`.`intid` = `stock`.`product_intid` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` INNER JOIN `category` ON `category`.`id` = `sub_category`.`category_id` INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `location` ON `location`.`id` = `stock`.`location_id`  WHERE `product`.`intid` = '" + frameId + "' ");
            DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {

                Vector v = new Vector();
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("id"));
                v.add(rs.getString("sub_category"));
                v.add(rs.getString("saling_price"));
                v.add(rs.getString("product.id"));

                dtm.addRow(v);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Conneciton", "Connection Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", se);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", e);

        }
    }

    public void loadCustomerPrescription(String mobile) {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `prescription_details` WHERE `customer_mobile` = '" + mobile + "'  ");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("job_no"));
                v.add(rs.getString("prescripiton_date"));
                dtm.addRow(v);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Conneciton", "Connection Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", se);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", e);

        }
    }

    public void LoadCustomer(String mobile) {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `customer` WHERE `mobile` = '" + mobile + "'  ");
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("mobile"));
                v.add(rs.getString("name"));
                v.add(rs.getString("nic"));
                dtm.addRow(v);
            }
            jTextField1.setText(mobile);
            loadCustomerPrescription(mobile);

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Conneciton", "Connection Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", se);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", e);
        }
    }

    public void lensLoading() {
        try {

            if (LensStockAvailable) { // is availabe

            } else { // not available
                jTextField7.setText(lensCode);
                ResultSet rs = MySQL.execute("SELECT * \n"
                        + "FROM lens_stock \n"
                        + "INNER JOIN supplier ON supplier.supplier_id = lens_stock.supplier_id WHERE `lens_id` ='" + lensStock_id + "' ");

                DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
                dtm.setRowCount(0);

                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("lens_id"));
                    v.add(rs.getString("lens_code"));
                    v.add(rs.getString("lens_price"));
                    v.add(rs.getString("Supplier_Name"));
                    dtm.addRow(v);
                }
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Conneciton", "Connection Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void LensStockSettings() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `settings` WHERE `setting_id` = 1 "); // load Lens
            if (rs.next()) {
                if (rs.getInt("lens_stock") == 1) { // lens stock available
                    LensStockAvailable = true;
                } else {
                    LensStockAvailable = false;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Faild to load Settings Data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        userNameField = new javax.swing.JLabel();
        dateField = new javax.swing.JLabel();
        timeField = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jButton7 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jTextField2 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        jTextField11 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jTextField7 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel11.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel11.setText("Operator : ");

        jLabel16.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel16.setText("Date       :");

        jLabel26.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel26.setText("Time       :");

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
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateField))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(timeField)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(userNameField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(dateField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(timeField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        jLabel5.setText("Edit Order");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel5)))
                .addContainerGap(8, Short.MAX_VALUE))
        );

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
        jButton4.setText("Update ");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Customer Prescription.png"))); // NOI18N
        jButton7.setText("Order Management");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel7.setText("Pages");

        jPanel6.setBackground(new java.awt.Color(206, 206, 206));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 83, 250, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel9.setText("Select Prescription");
        jPanel6.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Prescription Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel6.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, 250, 129));
        jPanel6.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 520, 480, 10));

        jLabel15.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel15.setText("Data Serching");
        jPanel6.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mobile", "Name"
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

        jPanel6.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 111, 250, 129));

        jLabel10.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel10.setText("SKU");
        jPanel6.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 50, -1, -1));

        jTextField6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField6MouseClicked(evt);
            }
        });
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField6KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 80, 280, -1));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Brand", "Frame ID", "Frame Name", "Price", "product ID", "Color"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jPanel6.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 110, 450, 129));

        jLabel17.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel17.setText("Customer mobile Or Name");
        jPanel6.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 57, -1, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI Historic", 0, 36)); // NOI18N
        jLabel22.setText("-");
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(666, 495, 20, 20));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel6.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 450, 10, 110));

        jTextField2.setText("1");
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 490, -1, -1));

        jLabel30.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel30.setText("QTY");
        jPanel6.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 460, -1, -1));

        jLabel31.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jLabel31.setText("Rs.0.00");
        jPanel6.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 490, 110, -1));

        jLabel32.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel32.setText("X");
        jPanel6.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 490, 20, -1));

        jLabel33.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel33.setText("Advance Payment");
        jPanel6.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 520, 130, -1));

        jLabel34.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel34.setText("Due Amount");
        jPanel6.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 470, -1, -1));

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, 250, -1));

        jLabel38.setFont(new java.awt.Font("Segoe UI Historic", 1, 24)); // NOI18N
        jLabel38.setText("Rs.0.00");
        jPanel6.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 490, -1, -1));

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 490, 110, -1));

        jLabel39.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel39.setText("Discount");
        jPanel6.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 450, -1, 30));

        jLabel27.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel27.setText("=");
        jPanel6.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 490, 20, -1));

        jButton5.setText("Add Discount");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 490, -1, -1));
        jPanel6.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 410, 1030, 14));

        jTextField11.setText("  ");
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
        jPanel6.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 540, 90, -1));

        jLabel40.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel40.setText("Sub Total");
        jPanel6.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 450, 70, 30));

        jButton6.setText("Add");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 540, 60, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel14.setText("Total Amount ");
        jPanel6.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 420, -1, -1));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Lens id", "Lens Code", "Lens Price", "Lens Supplier"
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

        jPanel6.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 306, 780, 95));

        jTextField7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField7MouseClicked(evt);
            }
        });
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField7KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField7KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 278, 780, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel13.setText("Lense Id or Type");
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 252, -1, -1));

        jButton8.setText("Refresh Total");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 360, -1, -1));

        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 320, 47, -1));

        jLabel1.setText("Lnes Qty");
        jPanel6.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 300, 63, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("X");
        jPanel6.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 300, 48, 44));
        jPanel6.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 540, 170, 32));

        jLabel35.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel35.setText("Pay Amount");
        jPanel6.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 520, -1, -1));

        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField9KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 80, 90, -1));

        jLabel19.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel19.setText("Frame Id Or Brand");
        jPanel6.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 60, -1, -1));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)))
                        .addGap(36, 36, 36))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
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
                        .addComponent(jButton4)
                        .addGap(62, 62, 62)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addGap(31, 31, 31)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(237, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
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
        OrderManagement om = new OrderManagement();
        om.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Refresh(customer_mobile);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String Customer_mobile = jTextField1.getText();
        String Prescription_id = jTextField4.getText();
        String Frame_id = jTextField6.getText();
        String lens_stock_id = jTextField7.getText();
        String discount = jTextField3.getText();
        String advancePayment = jTextField11.getText();

// ── Safe parse for Payamount ──
        double Payamount;
        try {
            Payamount = Double.parseDouble(jTextField8.getText());
        } catch (Exception e) {
            Payamount = 0.0;
        }

// ── Safe parse for Discount ──
        try {
            if (!discount.isEmpty()) {
                Discount = Double.parseDouble(discount);
            } else {
                Discount = 0.0;
            }
        } catch (Exception e) {
            Discount = 0.0;
        }

// ── Safe parse for AdvancedPayment ──
        try {
            if (!advancePayment.isEmpty()) {
                AdvancedPayment = Double.parseDouble(advancePayment);
            } else {
                AdvancedPayment = 0.0;
            }
        } catch (Exception e) {
            AdvancedPayment = 0.0;
        }

// ── Safe parse for lensQty ──
        int lensQty = 0;
        try {
            if (!jTextField5.getText().isEmpty()) {
                lensQty = Integer.parseInt(jTextField5.getText());
            }
        } catch (Exception e) {
            lensQty = 0;
        }

        double InsertSubTotal = SubTotal - Discount;

// ── Get lens stock id from jTextField7 ──
        String lensStock_id = null;
        if (!jTextField7.getText().isEmpty()) {
            lensStock_id = jTextField7.getText();
        }

// ── Build lens_stock_lens_id value safely - use NULL if no lens selected ──
        String lensStockSqlValue = (lensStock_id != null) ? "'" + lensStock_id + "'" : "NULL";

        if (!Frame_id.isEmpty()) {
            try {
                // ── Get the OLD frame that was previously on this invoice ──
                ResultSet oldInvoice_rs = MySQL.execute("SELECT `stock_id` FROM `invoice_item` WHERE `invoice_id` = '" + BaseInvoice_id + "'");

                if (oldInvoice_rs.next()) {
                    String oldStock_id = String.valueOf(oldInvoice_rs.getInt("stock_id"));

                    if (oldStock_id.equals(Frame_id)) {
                        // ── SAME FRAME: skip all stock checks, just update invoice directly ──
                        System.out.println("Same frame selected, skipping stock check");

                        // ── Update invoice_item (no change needed but keep consistent) ──
                        MySQL.execute("UPDATE `invoice_item` SET `stock_id` = '" + Frame_id + "' WHERE `invoice_id` = '" + BaseInvoice_id + "'");

                        // ── Update the invoice record ──
                        MySQL.execute("UPDATE `invoice` SET "
                                + "`total_price` = '" + Total + "', "
                                + "`advance_payment` = '" + AdvancedPayment + "', "
                                + "`discount` = '" + Discount + "', "
                                + "`subtotal` = '" + InsertSubTotal + "', "
                                + "`lenstotal` = '" + LensTotal + "', "
                                + "`lens_stock_lens_id` = " + lensStockSqlValue + ", "
                                + "`lens_Qty` = '" + lensQty + "', "
                                + "`payment_amount` = '" + Payamount + "', "
                                + "`discount_percentage` = '" + final_discountPercentage + "' "
                                + "WHERE `invoice_id` = '" + BaseInvoice_id + "'");

                        // ── Update payment history ──
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String curruntDay = now.format(dateFormatter);
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                        String curruntTime = now.format(timeFormatter);

                       

                        JOptionPane.showMessageDialog(this, "Invoice Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        Refresh(Customer_mobile);

                    } else {
                        // ── DIFFERENT FRAME: check new frame stock availability first ──
                        ResultSet Frame_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + Frame_id + "' AND `qty` > 0 ");
                        if (Frame_rs.next()) {

                            // ── STEP 1: Restock the OLD frame ──
                            ResultSet oldStock_rs = MySQL.execute("SELECT `qty` FROM `stock` WHERE `id` = '" + oldStock_id + "'");
                            if (oldStock_rs.next()) {
                                int restoredQty = oldStock_rs.getInt("qty") + 1;
                                MySQL.execute("UPDATE `stock` SET `qty` = '" + restoredQty + "' WHERE `id` = '" + oldStock_id + "'");
                            }

                            // ── STEP 2: Deduct 1 qty from the NEW frame stock ──
                            int newStockQty = Frame_rs.getInt("qty") - 1;
                            MySQL.execute("UPDATE `stock` SET `qty` = '" + newStockQty + "' WHERE `id` = '" + Frame_id + "'");

                            // ── STEP 3: Update invoice_item to point to the new stock ──
                            MySQL.execute("UPDATE `invoice_item` SET `stock_id` = '" + Frame_id + "' WHERE `invoice_id` = '" + BaseInvoice_id + "'");

                            // ── STEP 4: Update the invoice record ──
                            MySQL.execute("UPDATE `invoice` SET "
                                    + "`total_price` = '" + Total + "', "
                                    + "`advance_payment` = '" + AdvancedPayment + "', "
                                    + "`discount` = '" + Discount + "', "
                                    + "`subtotal` = '" + InsertSubTotal + "', "
                                    + "`lenstotal` = '" + LensTotal + "', "
                                    + "`lens_stock_lens_id` = " + lensStockSqlValue + ", "
                                    + "`lens_Qty` = '" + lensQty + "', "
                                    + "`payment_amount` = '" + Payamount + "', "
                                    + "`discount_percentage` = '" + final_discountPercentage + "' "
                                    + "WHERE `invoice_id` = '" + BaseInvoice_id + "'");

                            // ── STEP 5: Update payment history ──
                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            String curruntDay = now.format(dateFormatter);
                            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                            String curruntTime = now.format(timeFormatter);

                            
                            JOptionPane.showMessageDialog(this, "Invoice Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            Refresh(Customer_mobile);

                        } else {
                            // ── New frame is out of stock ──
                            JOptionPane.showMessageDialog(this, "Selected Frame is out of stock. Please choose another.", "Out of Stock", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Something went wrong. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            // ── No Frame selected - Lens only order update ──
            try {
                // ── STEP 1: Restock the OLD frame if there was one ──
                ResultSet oldInvoice_rs = MySQL.execute("SELECT `stock_id` FROM `invoice_item` WHERE `invoice_id` = '" + BaseInvoice_id + "'");
                if (oldInvoice_rs.next()) {
                    String oldStock_id = String.valueOf(oldInvoice_rs.getInt("stock_id"));
                    ResultSet oldStock_rs = MySQL.execute("SELECT `qty` FROM `stock` WHERE `id` = '" + oldStock_id + "'");
                    if (oldStock_rs.next()) {
                        int restoredQty = oldStock_rs.getInt("qty") + 1;
                        MySQL.execute("UPDATE `stock` SET `qty` = '" + restoredQty + "' WHERE `id` = '" + oldStock_id + "'");
                    }
                    // Remove the invoice item since no frame now
                    MySQL.execute("DELETE FROM `invoice_item` WHERE `invoice_id` = '" + BaseInvoice_id + "'");
                }

                // ── STEP 2: Update the invoice record ──
                MySQL.execute("UPDATE `invoice` SET "
                        + "`total_price` = '" + Total + "', "
                        + "`advance_payment` = '" + AdvancedPayment + "', "
                        + "`discount` = '" + Discount + "', "
                        + "`subtotal` = '" + InsertSubTotal + "', "
                        + "`lenstotal` = '" + LensTotal + "', "
                        + "`lens_stock_lens_id` = " + lensStockSqlValue + ", "
                        + "`lens_Qty` = '" + lensQty + "', "
                        + "`payment_amount` = '" + Payamount + "', "
                        + "`discount_percentage` = '" + final_discountPercentage + "' "
                        + "WHERE `invoice_id` = '" + BaseInvoice_id + "'");

                // ── STEP 3: Update payment history ──
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String curruntDay = now.format(dateFormatter);
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String curruntTime = now.format(timeFormatter);

                ResultSet payHist_rs = MySQL.execute("SELECT * FROM `advance_payment_history` WHERE `invoice_invoice_id` = '" + BaseInvoice_id + "'");
                if (payHist_rs.next()) {
                    MySQL.execute("UPDATE `advance_payment_history` SET "
                            + "`paid_amount` = '" + Payamount + "', "
                            + "`date` = '" + curruntDay + "', "
                            + "`time` = '" + curruntTime + "' "
                            + "WHERE `invoice_invoice_id` = '" + BaseInvoice_id + "'");
                } else {
                    MySQL.execute("INSERT INTO `advance_payment_history` (`invoice_invoice_id`,`paid_amount`,`date`,`time`,`location_id`) "
                            + "VALUES ('" + BaseInvoice_id + "','" + Payamount + "','" + curruntDay + "','" + curruntTime + "','" + UserDetails.UserLocation_id + "')");
                }

                JOptionPane.showMessageDialog(this, "Invoice Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                Refresh(Customer_mobile);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Something went wrong. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        OrderManagement om = new OrderManagement();
        om.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // SEARCH CUSTOMER BY HIS NAME OR ID

        String Customer_details = jTextField1.getText();

        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `customer` WHERE `name` LIKE '%" + Customer_details + "%' OR `mobile` LIKE '%" + Customer_details + "%'  ");
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("mobile"));
                v.add(rs.getString("name"));
                dtm.addRow(v);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Conneciton", "Connection Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", se);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", e);

        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // Get Data from Customer Prescription
        if (evt.getClickCount() == 2) {
            jTextField4.setText(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // Click Jtable2 load Prescription

        if (evt.getClickCount() == 2) {
            int SelectedRow = jTable2.getSelectedRow();
            jTextField1.setText(String.valueOf(jTable2.getValueAt(SelectedRow, 0)));
            int selectedRow = jTable2.getSelectedRow();
            loadCustomerPrescription(String.valueOf(jTable2.getValueAt(selectedRow, 0)));
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jTextField6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField6MouseClicked

    }//GEN-LAST:event_jTextField6MouseClicked

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6KeyPressed

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        // Search Product By his brand name and frame id
        String brand_details = jTextField6.getText();

        try {
            //            aniwaren Login wenna wenawa
            ResultSet rs = MySQL.execute("SELECT * FROM `stock` INNER JOIN `product` ON `product`.`intid` = `stock`.`product_intid` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` INNER JOIN `category` ON `category`.`id` = `sub_category`.`category_id` INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `location` ON `location`.`id` = `stock`.`location_id` "
                    + " WHERE (`category`.`id` =  '1'  OR `category`.`id` =  '4') AND `qty` > 0  AND `stock`.`location_id` = '" + UserDetails.UserLocation_id + "' AND  `brand_name` LIKE '%" + brand_details + "%' OR `product`.`id` LIKE '%" + brand_details + "%' AND (`category`.`id` =  '1' OR  `category`.`id` =  '4') AND `stock`.`location_id` = '" + UserDetails.UserLocation_id + "' AND `qty` > 0  ");

            //                                        SELECT * FROM `stock` INNER JOIN `product` ON `product`.`id` = `stock`.`product_id` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` INNER JOIN `category` ON `category`.`id` = `sub_category`.`category_id` INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `location` ON `location`.`id` = `stock`.`location_id`  WHERE `category`.`id` =  '1'  AND `stock`.`location_id` = '" + UserDetails.UserLocation_id + "'  AND `qty` > 0
            DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("id"));
                v.add(rs.getString("sub_category"));
                v.add(rs.getString("saling_price"));
                v.add(rs.getString("product.id"));

                dtm.addRow(v);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Conneciton", "Connection Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", se);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", e);

        }
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // Select Products
        if (evt.getClickCount() == 2) {
            jTextField6.setText(String.valueOf(jTable3.getValueAt(jTable3.getSelectedRow(), 1)));
            //            System.out.println(String.valueOf(jTable3.getValueAt(jTable3.getSelectedRow(), 1)));
            CalculateLensTotal();
        }
    }//GEN-LAST:event_jTable3MouseClicked

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // multiply by QTY
        //        String qty = jTextField2.getText();
        //        CalculateLensTotal(qty);
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased

    }//GEN-LAST:event_jTextField3KeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Adding Discount

        if (!jTextField3.getText().isEmpty()) {

            String discountText = jTextField3.getText();
            if (discountText.contains("%")) {
                double totalPrice = SubTotal;
                double discount_percentage = Double.parseDouble(discountText.replace("%", ""));
                double discountAmount = (totalPrice * discount_percentage) / 100.0;
                Discount = discountAmount;
                System.out.println("this is Discount Amount " + discountAmount);
                final_discountPercentage = discountText;
                ChangeTotal();
            } else {
                double TextDiscount = Double.parseDouble(jTextField3.getText());
                Discount = TextDiscount;
                final_discountPercentage = "";
                ChangeTotal();
            }
        } else if (jTextField3.getText().isEmpty()) {
            Discount = 0.0;
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

    private void jTextField11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyReleased
        // Copy Advance Payment to Payment Amount
    }//GEN-LAST:event_jTextField11KeyReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // advance Payment
        if (!jTextField11.getText().isEmpty()) {
            double advancePayment = Double.parseDouble(jTextField11.getText());
            AdvancedPayment = advancePayment;
            ChangeTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Please Enter Advance Payment", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        // Select Lens
        if (evt.getClickCount() == 1) {
            jTextField7.setText(String.valueOf(jTable4.getValueAt(jTable4.getSelectedRow(), 0)));
            //            jTextField7.setEnabled(false);
            //            System.out.println(String.valueOf(jTable3.getValueAt(jTable3.getSelectedRow(), 1)));
            CalculateLensTotal();
        }
    }//GEN-LAST:event_jTable4MouseClicked

    private void jTextField7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7MouseClicked

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7KeyPressed

    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased
        // Search Lens By his Lens Code and frame id
        String brand_details = jTextField6.getText();

        try {
            //            aniwaren Login wenna wenawa
            ResultSet rs = MySQL.execute("SELECT * \n"
                    + "FROM lens_stock \n"
                    + "INNER JOIN `supplier` ON `supplier`.`supplier_id` = `lens_stock`.`supplier_id` \n"
                    + " WHERE `lens_code` LIKE '%" + jTextField7.getText() + "%' OR `lens_id` LIKE '%" + jTextField7.getText() + "%' ");

            //                  SELECT * FROM `stock` INNER JOIN `product` ON `product`.`id` = `stock`.`product_id` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` INNER JOIN `category` ON `category`.`id` = `sub_category`.`category_id` INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `location` ON `location`.`id` = `stock`.`location_id`  WHERE `category`.`id` =  '1'  AND `stock`.`location_id` = '" + UserDetails.UserLocation_id + "'  AND `qty` > 0
            DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("lens_id"));
                v.add(rs.getString("lens_code"));
                v.add(rs.getString("lens_price"));
                v.add(rs.getString("Supplier_Name"));

                dtm.addRow(v);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Conneciton", "Connection Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", se);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", e);
        }
    }//GEN-LAST:event_jTextField7KeyReleased

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        //      Refresh Total Price
        CalculateLensTotal();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        // TODO add your handling code here:
        CalculateLensTotal();
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField9KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyReleased
        // Search Product By its SKU

        String brand_details = jTextField9.getText();

        try {
            //            aniwaren Login wenna wenawa
            ResultSet rs = MySQL.execute("SELECT * FROM `stock` INNER JOIN `product` ON `product`.`intid` = `stock`.`product_intid` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` INNER JOIN `category` ON `category`.`id` = `sub_category`.`category_id` INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `location` ON `location`.`id` = `stock`.`location_id` "
                    + " WHERE (`category`.`id` =  '1'  OR `category`.`id` =  '4') AND `qty` > 0  AND `stock`.`location_id` = '" + UserDetails.UserLocation_id + "' AND  `stock`.`SKU` LIKE '%" + brand_details + "%' AND `qty` > 0  ");

            //                                        SELECT * FROM `stock` INNER JOIN `product` ON `product`.`id` = `stock`.`product_id` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` INNER JOIN `category` ON `category`.`id` = `sub_category`.`category_id` INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `location` ON `location`.`id` = `stock`.`location_id`  WHERE `category`.`id` =  '1'  AND `stock`.`location_id` = '" + UserDetails.UserLocation_id + "'  AND `qty` > 0
            DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("id"));
                v.add(rs.getString("sub_category"));
                v.add(rs.getString("saling_price"));
                v.add(rs.getString("product.id"));

                dtm.addRow(v);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Conneciton", "Connection Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", se);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", e);

        }
    }//GEN-LAST:event_jTextField9KeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EditOrder.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditOrder.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditOrder.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditOrder.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dateField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JLabel timeField;
    private javax.swing.JLabel userNameField;
    // End of variables declaration//GEN-END:variables
}
