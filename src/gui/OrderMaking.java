package gui;

import gui.LensStockAdding;
import gui.Printsouts;
import gui.TypeLensBrandChange;
import gui.TypeLensChange;
import gui.TypeLensCortinChange;
import gui.TypeLensDesignChange;
import gui.TypeLensTintChange;
import models.Reports;
import models.UserDetails;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
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

public class OrderMaking extends javax.swing.JFrame {

    /**
     * Creates new form OrderMaking
     */
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    HashMap<String, String> LensMap = new HashMap<>();
    HashMap<String, String> CortinMap = new HashMap<>();
    HashMap<String, String> BrandMap = new HashMap<>();
    HashMap<String, String> DesingdMap = new HashMap<>();
    HashMap<String, String> TintMap = new HashMap<>();
    HashMap<String, String> warrentyMap = new HashMap<>();

    public OrderMaking() {
        initComponents();
        setSize(screen.width, screen.height);
        jTextField2.setEnabled(false);
        Refresh();
        operater();
        time();
    }

    public OrderMaking(String mobile) {
        initComponents();
        setSize(screen.width, screen.height);
        jTextField2.setEnabled(false);
        Refresh(mobile);
        operater();
        time();
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

            String DateString = day_string+ " of "+ month_string+" "+year_string;
            timeField.setText(time);
            dateField.setText(DateString);
            System.out.println(day);
        };
        Timer timer = new Timer(1000, timerListener);
        timer.setInitialDelay(0);
        timer.start();
    }

    public double Total;

    public double RealTotalWithoutDiscount;
    public int qty;

    public double LensTotal = 0;
    public double frame_price = 0;
    public double Discount = 0;
    public String final_discountPercentage = "";
    public double AdvancedPayment = 0;
    public double SubTotal = 0;

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
                    LensTotal = lensRs.getDouble("lens_price") * Double.parseDouble(jTextField5.getText());
                    System.out.println("Lens Price " + LensTotal);
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

    public void Refresh() {
        LoadCustomer();
        LoadStockProducts();
        LoadWarrenty();
        jTextField4.setText("");
        jTextField4.setEnabled(false);
        lensLoading();
        jTextField7.setEnabled(true);
    }

    public void Refresh(String mobile) {
        LoadCustomer(mobile);

        LoadStockProducts();
        LoadWarrenty();
        jTextField4.setText("");
        jTextField4.setEnabled(false);
        jTable2.setEnabled(false);
        jTextField1.setEnabled(false);
        lensLoading();
    }

    public void LoadStockProducts() {
        try {

//            aniwaren Login wenna wenawa
            ResultSet rs = MySQL.execute("SELECT * FROM `stock` INNER JOIN `product` ON `product`.`intid` = `stock`.`product_intid` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` INNER JOIN `category` ON `category`.`id` = `sub_category`.`category_id` INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `location` ON `location`.`id` = `stock`.`location_id`  WHERE (`category`.`id` =  '1' OR `category`.`id` = '4')  AND `stock`.`location_id` = '" + UserDetails.UserLocation_id + "'  AND `qty` > 0 ");
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

    public void LoadCustomer() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `customer`  ");
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("mobile"));
                v.add(rs.getString("name"));
                v.add(rs.getString("nic"));
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

    public void LoadWarrenty() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `job_warrenty`");
            Vector v = new Vector();

            v.add("Select Warrenty Period ");

            while (rs.next()) {
                System.out.println("working");
                v.add(String.valueOf(rs.getString("warrenty_id") + ") " + rs.getString("warrenty")));
                TintMap.put(rs.getString("warrenty"), rs.getString("warrenty_id"));
            }

            DefaultComboBoxModel dfm = new DefaultComboBoxModel<>(v);
            jComboBox7.setModel(dfm);

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", "Please Check Your Internet Connection or Please Try again later", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", se);

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Data failed to load", e);

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
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
        jLabel12 = new javax.swing.JLabel();
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
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel23 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jSeparator7 = new javax.swing.JSeparator();
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
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox<>();
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
        jSeparator5 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jTextField9 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

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
        jLabel5.setText("Create Order");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 972, Short.MAX_VALUE)
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
        jButton4.setText("Bill the Order");
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

        jLabel12.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel12.setText("Accessories");
        jPanel6.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 440, -1, -1));

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
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
        }

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
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            jTable2.getColumnModel().getColumn(1).setResizable(false);
        }

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
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setResizable(false);
            jTable3.getColumnModel().getColumn(1).setResizable(false);
            jTable3.getColumnModel().getColumn(2).setResizable(false);
            jTable3.getColumnModel().getColumn(3).setResizable(false);
            jTable3.getColumnModel().getColumn(4).setResizable(false);
            jTable3.getColumnModel().getColumn(5).setResizable(false);
        }

        jPanel6.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 110, 450, 129));

        jLabel17.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel17.setText("Customer mobile Or Name");
        jPanel6.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 57, -1, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI Historic", 0, 36)); // NOI18N
        jLabel22.setText("-");
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(666, 495, 20, 20));

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Bank Deposit");
        jPanel6.add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 540, -1, -1));

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Cash");
        jPanel6.add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 480, -1, -1));

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("Card");
        jPanel6.add(jRadioButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, -1, -1));

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("Online Payment");
        jPanel6.add(jRadioButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 520, -1, -1));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel6.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 450, 10, 110));

        jLabel23.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel23.setText("Payment Method");
        jPanel6.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, -1, -1));

        jTextField2.setText("1");
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 490, -1, -1));

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel6.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 450, 10, 110));

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
        jPanel6.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 530, 130, -1));

        jLabel34.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel34.setText("Total Price");
        jPanel6.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 530, -1, -1));

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
        jPanel6.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 550, -1, -1));

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
        jPanel6.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 550, 20, -1));

        jButton5.setText("Add Discount");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 490, -1, -1));
        jPanel6.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 410, 1030, 14));

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
        jPanel6.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 550, 90, -1));

        jLabel40.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel40.setText("Sub Total");
        jPanel6.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 450, 70, 30));

        jButton6.setText("Add");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(557, 553, 60, -1));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select JobType", "Shop Orders", "EyeCamp Orders" }));
        jPanel6.add(jComboBox6, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 490, -1, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel14.setText("Total Amount ");
        jPanel6.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 420, -1, -1));

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Warrenty" }));
        jPanel6.add(jComboBox7, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 540, 122, -1));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Lens id", "Lens Code", "Lens Price", "Lens Supplier"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false
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
        }

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

        jTextField5.setText("2");
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
        jPanel6.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 550, 170, 32));

        jLabel35.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel35.setText("Pay Amount");
        jPanel6.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 530, -1, -1));

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel6.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 450, 10, 110));

        jLabel18.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel18.setText("Order Types");
        jPanel6.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 440, -1, -1));

        jRadioButton5.setText("Bag");
        jPanel6.add(jRadioButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 480, -1, -1));

        jRadioButton6.setText("Box");
        jPanel6.add(jRadioButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 510, -1, -1));

        jRadioButton7.setText("Clothing");
        jPanel6.add(jRadioButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 540, -1, -1));

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
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Dashboard d = new Dashboard();
        d.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // Click Jtable2 load Prescription

        if (evt.getClickCount() == 2) {
            int SelectedRow = jTable2.getSelectedRow();
            jTextField1.setText(String.valueOf(jTable2.getValueAt(SelectedRow, 0)));
            int selectedRow = jTable2.getSelectedRow();
            loadCustomerPrescription(String.valueOf(jTable2.getValueAt(selectedRow, 0)));
        }

    }//GEN-LAST:event_jTable2MouseClicked

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // Get Data from Customer Prescription
        if (evt.getClickCount() == 2) {
            jTextField4.setText(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // multiply by QTY
//        String qty = jTextField2.getText();
//        CalculateLensTotal(qty);
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased

    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

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

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        // check if accessorie items are available
        boolean bag = false;
        boolean clothing = false;
        boolean box = false;

        boolean go = false;

        // box stock id
        String box_stock_id = null;
        // bag stock id
        String bag_stock_id = null;
        // clothing stock id
        String clothing_stock_id = null;

        if (jRadioButton5.isSelected()) { // bag
            try {
                ResultSet bag_rs = MySQL.execute("SELECT * FROM `stock` INNER JOIN `product` ON `product`.`intid` = `stock`.`product_intid` WHERE `product`.`sub_category_id` = '15' AND `qty` > 0 ");
                if (bag_rs.next()) {
                    System.out.println("stock have");
                    bag = true;
                    bag_stock_id = String.valueOf(bag_rs.getInt("stock.id"));
                } else {
                    System.out.println("stock NO");
                    JOptionPane.showMessageDialog(this, "You dont have enough Bag Quantity. Please add Bag Stock and Try again later", "Empty Bag Quantity", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Please check your connection or Something Wrong please try again later", "Something Worng", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        if (jRadioButton6.isSelected()) { // box
            try {
                ResultSet box_rs = MySQL.execute("SELECT * FROM `stock` INNER JOIN `product` ON `product`.`intid` = `stock`.`product_intid` WHERE `product`.`sub_category_id` = '10' AND `qty` > 0 ");
                if (box_rs.next()) {
                    box = true;
                    box_stock_id = String.valueOf(box_rs.getInt("stock.id"));
                } else {
                    JOptionPane.showMessageDialog(this, "You dont have enough Box Quantity. Please add Box Stock and Try again later", "Empty Box Quantity", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Please check your connection or Something Wrong please try again later", "Something Worng", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
//        
        if (jRadioButton7.isSelected()) { // clothing
            try {
                ResultSet clothing_rs = MySQL.execute("SELECT * FROM `stock` INNER JOIN `product` ON `product`.`intid` = `stock`.`product_intid` WHERE `product`.`sub_category_id` = '14' AND `qty` > 0 ");
                if (clothing_rs.next()) {
                    clothing = true;
                    clothing_stock_id = String.valueOf(clothing_rs.getInt("stock.id"));
                } else {
                    JOptionPane.showMessageDialog(this, "You dont have enough Clothing Quantity. Please add Clothing Stock and Try again later", "Empty Clothing Quantity", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Please check your connection or Something Wrong please try again later", "Something Worng", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
//
        if (jRadioButton5.isSelected() == true && bag == false) { // bag
            go = false;
        } else if (jRadioButton6.isSelected() == true && box == false) { // box
            go = false;
        } else if (jRadioButton7.isSelected() == true && clothing == false) { // clothing
            go = false;
        } else {
            go = true;
        }

        //   process Start if Accesssories items are available
        if (jTable4.getSelectedRow() != -1 && jTable1.getSelectedRow() == -1) { //check Prescription is Selected?
            JOptionPane.showMessageDialog(this, "Lens Selected, But Prescription is not selected. check and try again", "Empty Prescripiton", JOptionPane.WARNING_MESSAGE);
        } else {
            if (go) {
                ChangeTotal(); //refresh the Total
                // Bill The Order
                String Customer_mobile = jTextField1.getText();
                String Prescription_id = jTextField4.getText();
                String Frame_id = jTextField6.getText();
                double Payamount;
                try {
                    Payamount = Double.parseDouble(jTextField8.getText());
                } catch (Exception e) {
                    Payamount = 0.0;
                }

                String product_intid;
                int lensQty = Integer.parseInt(jTextField5.getText());
                int JoBtype = jComboBox6.getSelectedIndex();
                int paymentMethodSelecetd = 0;

//          Calcaulate Toda Date and Time
                Date today = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String OrderDate = sdf.format(today);
                SimpleDateFormat sdt = new SimpleDateFormat("HH:mm:ss");
                String orderTime = sdt.format(today);
//                double Discount = 0.0;

//                if (!jTextField3.getText().isEmpty()) {
//                    try {
//                        Discount = Double.parseDouble(jTextField3.getText());
//                    } catch (NumberFormatException e) {
//                        Discount = 0.0; // default value if invalid input
//                    }
//                }
                double InsertSubTotal = SubTotal - Discount;

                try {
                    if (!Customer_mobile.isEmpty()) {
                        ResultSet cust_rs = MySQL.execute("SELECT * FROM `customer` WHERE `mobile` = '" + Customer_mobile + "'");
                        if (cust_rs.next()) {

                            if (jTextField8.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Please don't let empty Payment Amount", "Empty Payment Amount", JOptionPane.ERROR_MESSAGE);
                            } else {

//                        Frame Id Validation
                                if (!Frame_id.isEmpty()) {
                                    ResultSet Frame_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + Frame_id + "' AND `qty` > 0 ");
                                    if (Frame_rs.next()) {
                                        product_intid = String.valueOf(Frame_rs.getInt("product_intid"));
//                                   payment Method Validation
                                        if (buttonGroup1.getSelection() != null) {
//                                        payment Assign
                                            if (jRadioButton2.isSelected()) {
                                                paymentMethodSelecetd = 1;
                                            } else if (jRadioButton3.isSelected()) {
                                                paymentMethodSelecetd = 2;
                                            } else if (jRadioButton4.isSelected()) {
                                                paymentMethodSelecetd = 3;
                                            } else if (jRadioButton1.isSelected()) {
                                                paymentMethodSelecetd = 4;
                                            }

                                            if (JoBtype == 0) {
                                                JOptionPane.showMessageDialog(this, "Please Sekect Job Type");
                                            } else {
//                                        INSERT PROCESS
                                                if (Prescription_id.matches("-?\\d+(\\.\\d+)?")) { // prescription selected
                                                    System.out.println("Prescription Selected");
                                                    int paymentStatus = 0;
                                                    if (AdvancedPayment == 0) {
                                                        paymentStatus = 2;
                                                    } else {
                                                        paymentStatus = 1;
                                                    }

                                                    //        Warrenty Details 
                                                    String WarrentyPeriod = "1";
                                                    if (jComboBox7.getSelectedIndex() != 0) {
                                                        String warrentyType = (String) jComboBox7.getSelectedItem();
                                                        String warrentyArray[] = warrentyType.split("\\)");
                                                        WarrentyPeriod = warrentyArray[1].trim();
                                                        WarrentyPeriod = TintMap.get(WarrentyPeriod);
                                                        System.out.println("Warrenty id is :- " + WarrentyPeriod);
                                                    }

                                                    ResultSet Inser_rs;
                                                    //     Add Lens Properties
                                                    if (jTable4.getSelectedRow() != -1) {

                                                        if (jTextField5.getText().isEmpty()) {
                                                            JOptionPane.showMessageDialog(this, "Please Enter Lens Amount", "Empty Lenses Quantity", JOptionPane.ERROR_MESSAGE);
                                                        } else if (lensQty > 2) {
                                                            JOptionPane.showMessageDialog(this, "Please Enter Valid Lens Amount", "Invalid Lenses Quantity", JOptionPane.ERROR_MESSAGE);
                                                        } else {
                                                            String lensStock_id = String.valueOf(jTable4.getValueAt(jTable4.getSelectedRow(), 0));
                                                            ResultSet lensResultSet = MySQL.execute("SELECT * FROM `lens_stock` WHERE `lens_id` = '" + lensStock_id + "'");

                                                            // if lens select
                                                            if (lensResultSet.next()) {
                                                                // INSERT PROCESS
                                                                Inser_rs = MySQL.execute("INSERT INTO `invoice` (`date`,`total_price`,`customer_mobile`,`payment_method_Payment_id`,`prescription_details_job_no`,`discount`,`subtotal`,`advance_payment`,`JobType_job_id`,`lenstotal`,`payment_status_id`,`job_warrenty_warrenty_id`,`lens_stock_lens_id`,`lens_Qty`,`payment_amount`,`clothing`,`box`,`bag`,`invoice_location`,`discount_percentage`,`order_time`)"
                                                                        + " VALUES ('" + OrderDate + "','" + Double.valueOf(jLabel38.getText()) + "','" + Customer_mobile + "','" + paymentMethodSelecetd + "','" + Prescription_id + "','" + Discount + "','" + InsertSubTotal + "','" + AdvancedPayment + "','" + JoBtype + "','" + LensTotal + "','" + paymentStatus + "','" + WarrentyPeriod + "','" + lensStock_id + "','" + lensQty + "','" + Payamount + "','" + clothing + "','" + box + "','" + bag + "','" + UserDetails.UserLocation_id + "','" + final_discountPercentage + "','"+orderTime+"') ");

                                                                int invoiceId = 0;
                                                                if (Inser_rs.next()) {

                                                                    invoiceId = Inser_rs.getInt(1);

                                                                    System.out.println("this is invoiceId -" + invoiceId);

                                                                    // payment history
                                                                    LocalDateTime now = LocalDateTime.now();
                                                                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                                                    String curruntDay = now.format(dateFormatter);
                                                                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                                                    String curruntTime = now.format(timeFormatter);
                                                                    MySQL.execute("INSERT INTO `advance_payment_history` (`invoice_invoice_id`,`paid_amount`,`date`,`time`,`payment_method`,`location_id`) VALUES ('" + invoiceId + "','" + Payamount + "','" + curruntDay + "','" + curruntTime + "','" + paymentMethodSelecetd + "','" + UserDetails.UserLocation_id + "') ");

                                                                    //add Invoice Items
                                                                    MySQL.execute("INSERT INTO `invoice_item` (`invoice_id`,`stock_id`,`qty`)"
                                                                            + " VALUES ('" + invoiceId + "','" + Frame_id + "','" + jTextField2.getText() + "') ");
                                                                    JOptionPane.showMessageDialog(this, "Order Adding Success", "Success", JOptionPane.OK_OPTION);

                                                                    // reduce the stock qty
                                                                    ResultSet stock_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + Frame_id + "' ");
                                                                    if (stock_rs.next()) {
                                                                        int stockQty = stock_rs.getInt("qty");

                                                                        stockQty = stockQty - 1;
                                                                        MySQL.execute("UPDATE `stock` SET `qty` = '" + stockQty + "' WHERE `id` = '" + Frame_id + "' ");
                                                                    }

                                                                    // reduse the other stocks ---
                                                                    if (jRadioButton5.isSelected()) { // box
                                                                        ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + box_stock_id + "' ");
                                                                        if (reduceBox_rs.next()) {
                                                                            int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                            MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + box_stock_id + "' ");
                                                                        }
                                                                    }

                                                                    if (jRadioButton6.isSelected()) { // bag
                                                                        ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + bag_stock_id + "' ");
                                                                        if (reduceBox_rs.next()) {
                                                                            int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                            MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + bag_stock_id + "' ");
                                                                        }
                                                                    }

                                                                    if (jRadioButton7.isSelected()) { // clothing
                                                                        ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + clothing_stock_id + "' ");
                                                                        if (reduceBox_rs.next()) {
                                                                            int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                            MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + clothing_stock_id + "' ");
                                                                        }
                                                                    }

                                                                    //  Order Successs. -> prints
                                                                    Printsouts printsouts = new Printsouts(invoiceId);
                                                                    printsouts.setVisible(true);
                                                                    // Reports.OrderPurchaceInvoice(String.valueOf(invoiceId));
                                                                    Refresh();

                                                                } else {
                                                                    JOptionPane.showMessageDialog(this, "Unable to process Your Request, Please Try again later", "Error", JOptionPane.ERROR_MESSAGE);
                                                                }

                                                            } else {
                                                                Inser_rs = MySQL.execute("INSERT INTO `invoice` (`date`,`total_price`,`customer_mobile`,`payment_method_Payment_id`,`prescription_details_job_no`,`discount`,`subtotal`,`advance_payment`,`JobType_job_id`,`lenstotal`,`payment_status_id`,`job_warrenty_warrenty_id`,`lens_Qty`,`payment_amount`,`clothing`,`box`,`bag`,`invoice_location`,`discount_percentage`,`order_time`)"
                                                                        + " VALUES ('" + OrderDate + "','" + Double.valueOf(jLabel38.getText()) + "','" + Customer_mobile + "','" + paymentMethodSelecetd + "','" + Prescription_id + "','" + Discount + "','" + InsertSubTotal + "','" + AdvancedPayment + "','" + JoBtype + "','" + LensTotal + "','" + paymentStatus + "','" + WarrentyPeriod + "','" + lensQty + "','" + Payamount + "','" + clothing + "','" + box + "','" + bag + "','" + UserDetails.UserLocation_id + "','" + final_discountPercentage + "','"+orderTime+"') ");

                                                                int invoiceId = 0;
                                                                if (Inser_rs.next()) {

                                                                    invoiceId = Inser_rs.getInt(1);

                                                                    // payment history
                                                                    LocalDateTime now = LocalDateTime.now();
                                                                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                                                    String curruntDay = now.format(dateFormatter);
                                                                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                                                    String curruntTime = now.format(timeFormatter);
                                                                    MySQL.execute("INSERT INTO `advance_payment_history` (`invoice_invoice_id`,`paid_amount`,`date`,`time`,`payment_method`,`location_id`) VALUES ('" + invoiceId + "','" + Payamount + "','" + curruntDay + "','" + curruntTime + "','" + paymentMethodSelecetd + "','" + UserDetails.UserLocation_id + "') ");
                                                                    // add Invoice Items
                                                                    MySQL.execute("INSERT INTO `invoice_item` (`invoice_id`,`stock_id`,`qty`)"
                                                                            + " VALUES ('" + invoiceId + "','" + Frame_id + "','" + jTextField2.getText() + "') ");
                                                                    JOptionPane.showMessageDialog(this, "Order Adding Success", "Success", JOptionPane.OK_OPTION);

                                                                    // reduce the stock qty
                                                                    ResultSet stock_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + Frame_id + "' ");
                                                                    if (stock_rs.next()) {
                                                                        int stockQty = stock_rs.getInt("qty");

                                                                        stockQty = stockQty - 1;
                                                                        MySQL.execute("UPDATE `stock` SET `qty` = '" + stockQty + "' WHERE `id` = '" + Frame_id + "' ");
                                                                    }

                                                                    // reduse the other stocks ---
                                                                    if (jRadioButton5.isSelected()) { // box
                                                                        ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + box_stock_id + "' ");
                                                                        if (reduceBox_rs.next()) {
                                                                            int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                            MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + box_stock_id + "' ");
                                                                        }
                                                                    }

                                                                    if (jRadioButton6.isSelected()) { // bag
                                                                        ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + bag_stock_id + "' ");
                                                                        if (reduceBox_rs.next()) {
                                                                            int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                            MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + bag_stock_id + "' ");
                                                                        }
                                                                    }

                                                                    if (jRadioButton7.isSelected()) { // clothing
                                                                        ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + clothing_stock_id + "' ");
                                                                        if (reduceBox_rs.next()) {
                                                                            int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                            MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + clothing_stock_id + "' ");
                                                                        }
                                                                    }

                                                                    // Order Successs. -> prints
                                                                    Printsouts printsouts = new Printsouts(invoiceId);
                                                                    printsouts.setVisible(true);
                                                                    //  Reports.OrderPurchaceInvoice(String.valueOf(invoiceId));
                                                                    Refresh();
                                                                    Printsouts p = new Printsouts(invoiceId);
                                                                    p.setVisible(true);

                                                                } else {
                                                                    JOptionPane.showMessageDialog(this, "Unable to process Your Request, Please Try again later", "Error", JOptionPane.ERROR_MESSAGE);
                                                                }
                                                            }
                                                        }

                                                    } else {

                                                        Inser_rs = MySQL.execute("INSERT INTO `invoice` (`date`,`total_price`,`customer_mobile`,`payment_method_Payment_id`,`prescription_details_job_no`,`discount`,`subtotal`,`advance_payment`,`JobType_job_id`,`lenstotal`,`payment_status_id`,`job_warrenty_warrenty_id`,`lens_Qty`,`lens_Qty`,`payment_amount`,`clothing`,`box`,`bag`,`invoice_location`,`discount_percentage`,`order_time`)"
                                                                + " VALUES ('" + OrderDate + "','" + Double.valueOf(jLabel38.getText()) + "','" + Customer_mobile + "','" + paymentMethodSelecetd + "','" + Prescription_id + "','" + Discount + "','" + InsertSubTotal + "','" + AdvancedPayment + "','" + JoBtype + "','" + LensTotal + "','" + paymentStatus + "','" + WarrentyPeriod + "','" + lensQty + "','" + lensQty + "','" + Payamount + "','" + clothing + "','" + box + "','" + bag + "','" + UserDetails.UserLocation_id + "','" + final_discountPercentage + "','"+orderTime+"') ");

                                                        int invoiceId = 0;
                                                        if (Inser_rs.next()) {

                                                            invoiceId = Inser_rs.getInt(1);

                                                            // payment history
                                                            LocalDateTime now = LocalDateTime.now();
                                                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                                            String curruntDay = now.format(dateFormatter);
                                                            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                                            String curruntTime = now.format(timeFormatter);
                                                            MySQL.execute("INSERT INTO `advance_payment_history` (`invoice_invoice_id`,`paid_amount`,`date`,`time`,`payment_method`,`location_id`) VALUES ('" + invoiceId + "','" + Payamount + "','" + curruntDay + "','" + curruntTime + "','" + paymentMethodSelecetd + "','" + UserDetails.UserLocation_id + "') ");

                                                            // add Invoice Items
                                                            MySQL.execute("INSERT INTO `invoice_item` (`invoice_id`,`stock_id`,`qty`)"
                                                                    + " VALUES ('" + invoiceId + "','" + Frame_id + "','" + jTextField2.getText() + "') ");
                                                            JOptionPane.showMessageDialog(this, "Order Adding Success", "Success", JOptionPane.OK_OPTION);

                                                            // reduce the stock qty
                                                            ResultSet stock_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + Frame_id + "' ");
                                                            if (stock_rs.next()) {
                                                                int stockQty = stock_rs.getInt("qty");

                                                                stockQty = stockQty - 1;
                                                                MySQL.execute("UPDATE `stock` SET `qty` = '" + stockQty + "' WHERE `id` = '" + Frame_id + "' ");
                                                            }

                                                            // reduse the other stocks ---
                                                            if (jRadioButton5.isSelected()) { // box
                                                                ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + box_stock_id + "' ");
                                                                if (reduceBox_rs.next()) {
                                                                    int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                    MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + box_stock_id + "' ");
                                                                }
                                                            }

                                                            if (jRadioButton6.isSelected()) { // bag
                                                                ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + bag_stock_id + "' ");
                                                                if (reduceBox_rs.next()) {
                                                                    int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                    MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + bag_stock_id + "' ");
                                                                }
                                                            }

                                                            if (jRadioButton7.isSelected()) { // clothing
                                                                ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + clothing_stock_id + "' ");
                                                                if (reduceBox_rs.next()) {
                                                                    int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                    MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + clothing_stock_id + "' ");
                                                                }
                                                            }
                                                            // Order Successs. -> prints
                                                            Printsouts printsouts = new Printsouts(invoiceId);
                                                            printsouts.setVisible(true);
                                                            //  Reports.OrderPurchaceInvoice(String.valueOf(invoiceId));
                                                            Refresh();
                                                            Printsouts p = new Printsouts(invoiceId);
                                                            p.setVisible(true);

                                                        } else {
                                                            JOptionPane.showMessageDialog(this, "Unable to process Your Request, Please Try again later", "Error", JOptionPane.ERROR_MESSAGE);
                                                        }
                                                    }

                                                } else {

                                                    System.out.println("Prescription not Selected");

                                                    int paymentStatus = 0;
                                                    if (AdvancedPayment == 0) {
                                                        paymentStatus = 2;
                                                    } else {
                                                        paymentStatus = 1;
                                                    }

                                                    //        Warrenty Details 
                                                    String WarrentyPeriod = "1";
                                                    if (jComboBox7.getSelectedIndex() != 0) {
                                                        String warrentyType = (String) jComboBox7.getSelectedItem();
                                                        String warrentyArray[] = warrentyType.split("\\)");
                                                        WarrentyPeriod = warrentyArray[1].trim();
                                                        WarrentyPeriod = TintMap.get(WarrentyPeriod);
                                                        System.out.println("Warrenty id is :- " + WarrentyPeriod);
                                                    }

                                                    //         Add Lens Properties
                                                    ResultSet Inser_rs;
                                                    //   Add Lens Properties
                                                    if (jTable4.getSelectedRow() != -1) {
                                                        String lensStock_id = String.valueOf(jTable4.getValueAt(jTable4.getSelectedRow(), 0));
                                                        ResultSet lensResultSet = MySQL.execute("SELECT * FROM `lens_stock` WHERE `lens_id` = '" + lensStock_id + "'");

                                                        // if lens select
                                                        if (lensResultSet.next()) {
                                                            Inser_rs = MySQL.execute("INSERT INTO `invoice` (`date`,`total_price`,`customer_mobile`,`payment_method_Payment_id`,`discount`,`subtotal`,`advance_payment`,`JobType_job_id`,`lenstotal`,`payment_status_id`,`job_warrenty_warrenty_id`,`lens_stock_lens_id`,`lens_Qty`,`payment_amount`,`clothing`,`box`,`bag`,`invoice_location`,`discount_percentage`,`order_time`)"
                                                                    + " VALUES ('" + OrderDate + "','" + Double.valueOf(jLabel38.getText()) + "','" + Customer_mobile + "','" + paymentMethodSelecetd + "','" + Discount + "','" + InsertSubTotal + "','" + AdvancedPayment + "','" + JoBtype + "','" + LensTotal + "','" + paymentStatus + "','" + WarrentyPeriod + "','" + lensStock_id + "','" + lensQty + "','" + Payamount + "','" + clothing + "','" + box + "','" + bag + "','" + UserDetails.UserLocation_id + "','" + final_discountPercentage + "','"+orderTime+"') ");

                                                            int invoiceId = 0;
                                                            if (Inser_rs.next()) {

                                                                invoiceId = Inser_rs.getInt(1);

                                                                // payment history
                                                                LocalDateTime now = LocalDateTime.now();
                                                                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                                                String curruntDay = now.format(dateFormatter);
                                                                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                                                String curruntTime = now.format(timeFormatter);
                                                                MySQL.execute("INSERT INTO `advance_payment_history` (`invoice_invoice_id`,`paid_amount`,`date`,`time`,`payment_method`,`location_id`) VALUES ('" + invoiceId + "','" + Payamount + "','" + curruntDay + "','" + curruntTime + "','" + paymentMethodSelecetd + "','" + UserDetails.UserLocation_id + "') ");

                                                                //   add Invoice Items
                                                                MySQL.execute("INSERT INTO `invoice_item` (`invoice_id`,`stock_id`,`qty`)"
                                                                        + " VALUES ('" + invoiceId + "','" + Frame_id + "','" + jTextField2.getText() + "') ");
                                                                JOptionPane.showMessageDialog(this, "Order Adding Success", "Success", JOptionPane.OK_OPTION);

                                                                //   reduce the stock qty
                                                                ResultSet stock_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + Frame_id + "' ");
                                                                if (stock_rs.next()) {
                                                                    int stockQty = stock_rs.getInt("qty");

                                                                    stockQty = stockQty - 1;
                                                                    MySQL.execute("UPDATE `stock` SET `qty` = '" + stockQty + "' WHERE `id` = '" + Frame_id + "' ");
                                                                }

                                                                // reduse the other stocks ---
                                                                if (jRadioButton5.isSelected()) { // box
                                                                    ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + box_stock_id + "' ");
                                                                    if (reduceBox_rs.next()) {
                                                                        int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                        MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + box_stock_id + "' ");
                                                                    }
                                                                }

                                                                if (jRadioButton6.isSelected()) { // bag
                                                                    ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + bag_stock_id + "' ");
                                                                    if (reduceBox_rs.next()) {
                                                                        int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                        MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + bag_stock_id + "' ");
                                                                    }
                                                                }

                                                                if (jRadioButton7.isSelected()) { // clothing
                                                                    ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + clothing_stock_id + "' ");
                                                                    if (reduceBox_rs.next()) {
                                                                        int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                        MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + clothing_stock_id + "' ");
                                                                    }
                                                                }

//
                                                                Printsouts printsouts = new Printsouts("OrderPurchaseInvoice", invoiceId);
                                                                printsouts.setVisible(true);
                                                                //  Reports.OrderPurFchaceInvoice(String.valueOf(invoiceId));
                                                                Refresh();
                                                                Printsouts p = new Printsouts(invoiceId);
                                                                p.setVisible(true);
                                                            } else {
                                                                JOptionPane.showMessageDialog(this, "Unable to process Your Request, Please Try again later", "Error", JOptionPane.ERROR_MESSAGE);
                                                            }

                                                        } else {

                                                            Inser_rs = MySQL.execute("INSERT INTO `invoice` (`date`,`total_price`,`customer_mobile`,`payment_method_Payment_id`,`discount`,`subtotal`,`advance_payment`,`JobType_job_id`,`lenstotal`,`payment_status_id`,`job_warrenty_warrenty_id`,`lens_Qty`,`payment_amount`,`clothing`,`box`,`bag`,`invoice_location`,'discount_percentage',`order_time`)"
                                                                    + " VALUES ('" + OrderDate + "','" + Double.valueOf(jLabel38.getText()) + "','" + Customer_mobile + "','" + paymentMethodSelecetd + "','" + Discount + "','" + InsertSubTotal + "','" + AdvancedPayment + "','" + JoBtype + "','" + LensTotal + "','" + paymentStatus + "','" + WarrentyPeriod + "','" + lensQty + "','" + Payamount + "','" + clothing + "','" + box + "','" + bag + "','" + UserDetails.UserLocation_id + "','" + final_discountPercentage + "','"+orderTime+"') ");

                                                            int invoiceId = 0;
                                                            if (Inser_rs.next()) {

                                                                invoiceId = Inser_rs.getInt(1);

                                                                // payment history
                                                                LocalDateTime now = LocalDateTime.now();
                                                                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                                                String curruntDay = now.format(dateFormatter);
                                                                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                                                String curruntTime = now.format(timeFormatter);
                                                                MySQL.execute("INSERT INTO `advance_payment_history` (`invoice_invoice_id`,`paid_amount`,`date`,`time`,`payment_method`,`location_id`) VALUES ('" + invoiceId + "','" + Payamount + "','" + curruntDay + "','" + curruntTime + "','" + paymentMethodSelecetd + "','" + UserDetails.UserLocation_id + "') ");

                                                                //   add Invoice Items
                                                                MySQL.execute("INSERT INTO `invoice_item` (`invoice_id`,`stock_id`,`qty`)"
                                                                        + " VALUES ('" + invoiceId + "','" + Frame_id + "','" + jTextField2.getText() + "') ");
                                                                JOptionPane.showMessageDialog(this, "Order Adding Success", "Success", JOptionPane.OK_OPTION);

                                                                //   reduce the stock qty
                                                                ResultSet stock_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + Frame_id + "' ");
                                                                if (stock_rs.next()) {
                                                                    int stockQty = stock_rs.getInt("qty");

                                                                    stockQty = stockQty - 1;
                                                                    MySQL.execute("UPDATE `stock` SET `qty` = '" + stockQty + "' WHERE `id` = '" + Frame_id + "' ");
                                                                }

                                                                // reduse the other stocks ---
                                                                if (jRadioButton5.isSelected()) { // box
                                                                    ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + box_stock_id + "' ");
                                                                    if (reduceBox_rs.next()) {
                                                                        int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                        MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + box_stock_id + "' ");
                                                                    }
                                                                }

                                                                if (jRadioButton6.isSelected()) { // bag
                                                                    ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + bag_stock_id + "' ");
                                                                    if (reduceBox_rs.next()) {
                                                                        int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                        MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + bag_stock_id + "' ");
                                                                    }
                                                                }

                                                                if (jRadioButton7.isSelected()) { // clothing
                                                                    ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + clothing_stock_id + "' ");
                                                                    if (reduceBox_rs.next()) {
                                                                        int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                        MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + clothing_stock_id + "' ");
                                                                    }
                                                                }

//
                                                                Printsouts printsouts = new Printsouts("OrderPurchaseInvoice", invoiceId);
                                                                printsouts.setVisible(true);
                                                                //  Reports.OrderPurFchaceInvoice(String.valueOf(invoiceId));
                                                                Refresh();
                                                                Printsouts p = new Printsouts(invoiceId);
                                                                p.setVisible(true);
                                                            } else {
                                                                JOptionPane.showMessageDialog(this, "Unable to process Your Request, Please Try again later", "Error", JOptionPane.ERROR_MESSAGE);
                                                            }

                                                        }

                                                    } else {

                                                        Inser_rs = MySQL.execute("INSERT INTO `invoice` (`date`,`total_price`,`customer_mobile`,`payment_method_Payment_id`,`discount`,`subtotal`,`advance_payment`,`JobType_job_id`,`lenstotal`,`payment_status_id`,`job_warrenty_warrenty_id`,`lens_Qty`,`payment_amount`,`clothing`,`box`,`bag`,`invoice_location`,`discount_percentage`,`order_time`)"
                                                                + " VALUES ('" + OrderDate + "','" + Double.valueOf(jLabel38.getText()) + "','" + Customer_mobile + "','" + paymentMethodSelecetd + "','" + Discount + "','" + InsertSubTotal + "','" + AdvancedPayment + "','" + JoBtype + "','" + LensTotal + "','" + paymentStatus + "','" + WarrentyPeriod + "','" + lensQty + "','" + Payamount + "','" + clothing + "','" + box + "','" + bag + "','" + UserDetails.UserLocation_id + "','" + final_discountPercentage + "','"+orderTime+"') ");

                                                        int invoiceId = 0;
                                                        if (Inser_rs.next()) {

                                                            invoiceId = Inser_rs.getInt(1);

                                                            // payment history
                                                            LocalDateTime now = LocalDateTime.now();
                                                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                                            String curruntDay = now.format(dateFormatter);
                                                            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                                            String curruntTime = now.format(timeFormatter);
                                                            MySQL.execute("INSERT INTO `advance_payment_history` (`invoice_invoice_id`,`paid_amount`,`date`,`time`,`payment_method`,`location_id`) VALUES ('" + invoiceId + "','" + Payamount + "','" + curruntDay + "','" + curruntTime + "','" + paymentMethodSelecetd + "','" + UserDetails.UserLocation_id + "') ");

                                                            // add Invoice Items
                                                            MySQL.execute("INSERT INTO `invoice_item` (`invoice_id`,`stock_id`,`qty`)"
                                                                    + " VALUES ('" + invoiceId + "','" + Frame_id + "','" + jTextField2.getText() + "') ");
                                                            JOptionPane.showMessageDialog(this, "Order Adding Success", "Success", JOptionPane.OK_OPTION);

                                                            // reduce the stock qty
                                                            ResultSet stock_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + Frame_id + "' ");
                                                            if (stock_rs.next()) {
                                                                int stockQty = stock_rs.getInt("qty");

                                                                stockQty = stockQty - 1;
                                                                MySQL.execute("UPDATE `stock` SET `qty` = '" + stockQty + "' WHERE `id` = '" + Frame_id + "' ");
                                                            }

                                                            // reduse the other stocks ---
                                                            if (jRadioButton5.isSelected()) { // box
                                                                ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + box_stock_id + "' ");
                                                                if (reduceBox_rs.next()) {
                                                                    int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                    MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + box_stock_id + "' ");
                                                                }
                                                            }

                                                            if (jRadioButton6.isSelected()) { // bag
                                                                ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + bag_stock_id + "' ");
                                                                if (reduceBox_rs.next()) {
                                                                    int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                    MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + bag_stock_id + "' ");
                                                                }
                                                            }

                                                            if (jRadioButton7.isSelected()) { // clothing
                                                                ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + clothing_stock_id + "' ");
                                                                if (reduceBox_rs.next()) {
                                                                    int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                    MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + clothing_stock_id + "' ");
                                                                }
                                                            }

                                                            // Order Successs. -> prints
                                                            Printsouts printsouts = new Printsouts(invoiceId);
                                                            printsouts.setVisible(true);
                                                            //  Reports.OrderPurchaceInvoice(String.valueOf(invoiceId));
                                                            Refresh();
                                                            Printsouts p = new Printsouts(invoiceId);
                                                            p.setVisible(true);
                                                        } else {
                                                            JOptionPane.showMessageDialog(this, "Unable to process Your Request, Please Try again later", "Error", JOptionPane.ERROR_MESSAGE);
                                                        }
                                                    }

                                                }
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(this, "Please Select a Payment Method", "InValid  Payment Method", JOptionPane.ERROR_MESSAGE);
                                        }

                                    } else {
                                        JOptionPane.showMessageDialog(this, "Please Select a Valid Frame", "InValid  Frame id", JOptionPane.ERROR_MESSAGE);
                                    }
                                } else {
                                    //only lens order
                                    int OptionResult = JOptionPane.showConfirmDialog(this, "Are You Sure Make Only Lens Purchase?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                                    if (OptionResult == 0) {

                                        //   payment Method Validation
                                        if (buttonGroup1.getSelection() != null) {
                                            //    payment Assign
                                            if (jRadioButton2.isSelected()) {
                                                paymentMethodSelecetd = 1;
                                            } else if (jRadioButton3.isSelected()) {
                                                paymentMethodSelecetd = 2;
                                            } else if (jRadioButton4.isSelected()) {
                                                paymentMethodSelecetd = 3;
                                            } else if (jRadioButton1.isSelected()) {
                                                paymentMethodSelecetd = 4;
                                            }

                                            if (JoBtype == 0) {
                                                JOptionPane.showMessageDialog(this, "Please Sekect Job Type");
                                            } else {
                                                //  INSERT PROCESS
                                                int payment_status_id = 1;

                                                if (jTextField11.getText().isEmpty()) {
                                                    payment_status_id = 2;
                                                }

                                                if (Prescription_id.matches("-?\\d+(\\.\\d+)?")) {
                                                    ResultSet Inser_rs = MySQL.execute("INSERT INTO `invoice` (`date`,`total_price`,`customer_mobile`,`payment_method_Payment_id`,`prescription_details_job_no`,`discount`,`subtotal`,`advance_payment`,`JobType_job_id`,`lenstotal`,`payment_status_id`,`lens_stock_lens_id`,`lens_Qty`,`clothing`,`box`,`bag`,`invoice_location`,`payment_amount`,`discount_percentage`,`order_time`)"
                                                            + " VALUES ('" + OrderDate + "','" + Double.valueOf(jLabel38.getText()) + "','" + Customer_mobile + "','" + paymentMethodSelecetd + "','" + Prescription_id + "','" + Discount + "','" + InsertSubTotal + "','" + AdvancedPayment + "','" + JoBtype + "','" + LensTotal + "','" + payment_status_id + "','" + jTextField7.getText() + "','" + jTextField5.getText() + "','" + clothing + "','" + box + "','" + bag + "','" + UserDetails.UserLocation_id + "','" + Payamount + "','" + final_discountPercentage + "','"+orderTime+"') ");
                                                    int invoiceId = 0;
                                                    if (Inser_rs.next()) {

                                                        invoiceId = Inser_rs.getInt(1);

                                                        // payment history
                                                        LocalDateTime now = LocalDateTime.now();
                                                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                                        String curruntDay = now.format(dateFormatter);
                                                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                                        String curruntTime = now.format(timeFormatter);
                                                        MySQL.execute("INSERT INTO `advance_payment_history` (`invoice_invoice_id`,`paid_amount`,`date`,`time`,`payment_method`,`location_id`) VALUES ('" + invoiceId + "','" + Payamount + "','" + curruntDay + "','" + curruntTime + "','" + paymentMethodSelecetd + "','" + UserDetails.UserLocation_id + "') ");

                                                        // reduse the other stocks ---
                                                        if (jRadioButton5.isSelected()) { // box
                                                            ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + box_stock_id + "' ");
                                                            if (reduceBox_rs.next()) {
                                                                int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + box_stock_id + "' ");
                                                            }
                                                        }

                                                        if (jRadioButton6.isSelected()) { // bag
                                                            ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + bag_stock_id + "' ");
                                                            if (reduceBox_rs.next()) {
                                                                int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + bag_stock_id + "' ");
                                                            }
                                                        }

                                                        if (jRadioButton7.isSelected()) { // clothing
                                                            ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + clothing_stock_id + "' ");
                                                            if (reduceBox_rs.next()) {
                                                                int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + clothing_stock_id + "' ");
                                                            }
                                                        }

                                                        Printsouts printsouts = new Printsouts(invoiceId);
                                                        printsouts.setVisible(true);
                                                        Refresh();
                                                        Printsouts p = new Printsouts(invoiceId);
                                                        p.setVisible(true);

                                                    } else {
                                                        JOptionPane.showMessageDialog(this, "Unable to process Your Request, Please Try again later", "Error", JOptionPane.ERROR_MESSAGE);
                                                    }

                                                } else {

                                                    if (jTextField11.getText().isEmpty()) {
                                                        payment_status_id = 2;
                                                    }

                                                    ResultSet Inser_rs = MySQL.execute("INSERT INTO `invoice` (`date`,`total_price`,`customer_mobile`,`payment_method_Payment_id`,`discount`,`subtotal`,`advance_payment`,`JobType_job_id`,`lenstotal`,`payment_status_id`,`lens_stock_lens_id`,`lens_Qty`,`clothing`,`box`,`bag`,`invoice_location`,`payment_amount`,`discount_percentage`,`order_time`)"
                                                            + " VALUES ('" + OrderDate + "','" + Double.valueOf(jLabel38.getText()) + "','" + Customer_mobile + "','" + paymentMethodSelecetd + "','" + Discount + "','" + InsertSubTotal + "','" + AdvancedPayment + "','" + JoBtype + "','" + LensTotal + "','" + payment_status_id + "','" + jTextField7.getText() + "','" + jTextField5.getText() + "','" + clothing + "','" + box + "','" + bag + "','" + UserDetails.UserLocation_id + "','" + Payamount + "','" + final_discountPercentage + "','"+orderTime+"') ");
                                                    //
                                                    int invoiceId = 0;
                                                    if (Inser_rs.next()) {

                                                        invoiceId = Inser_rs.getInt(1);

                                                        // payment history
                                                        LocalDateTime now = LocalDateTime.now();
                                                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                                        String curruntDay = now.format(dateFormatter);
                                                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                                        String curruntTime = now.format(timeFormatter);
                                                        MySQL.execute("INSERT INTO `advance_payment_history` (`invoice_invoice_id`,`paid_amount`,`date`,`time`,`payment_method`,`location_id`) VALUES ('" + invoiceId + "','" + Payamount + "','" + curruntDay + "','" + curruntTime + "','" + paymentMethodSelecetd + "','" + UserDetails.UserLocation_id + "') ");

                                                        // reduse the other stocks ---
                                                        if (jRadioButton5.isSelected()) { // box
                                                            ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + box_stock_id + "' ");
                                                            if (reduceBox_rs.next()) {
                                                                int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + box_stock_id + "' ");
                                                            }
                                                        }

                                                        if (jRadioButton6.isSelected()) { // bag
                                                            ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + bag_stock_id + "' ");
                                                            if (reduceBox_rs.next()) {
                                                                int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + bag_stock_id + "' ");
                                                            }
                                                        }

                                                        if (jRadioButton7.isSelected()) { // clothing
                                                            ResultSet reduceBox_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + clothing_stock_id + "' ");
                                                            if (reduceBox_rs.next()) {
                                                                int CurruntStockQty = reduceBox_rs.getInt("qty") - 1;
                                                                MySQL.execute("UPDATE `stock` SET `qty` = '" + CurruntStockQty + "' WHERE `id` = '" + clothing_stock_id + "' ");
                                                            }
                                                        }

                                                        JOptionPane.showMessageDialog(this, "Order Adding Success", "Success", JOptionPane.OK_OPTION);
                                                        Printsouts printsouts = new Printsouts(invoiceId);
                                                        printsouts.setVisible(true);
                                                        Refresh();
                                                        Printsouts p = new Printsouts(invoiceId);
                                                        p.setVisible(true);

                                                    } else {
                                                        JOptionPane.showMessageDialog(this, "Unable to process Your Request, Please Try again later", "Error", JOptionPane.ERROR_MESSAGE);
                                                    }
                                                }
                                            }

                                        } else {
                                            JOptionPane.showMessageDialog(this, "Please Select a Payment Method", "InValid  Payment Method", JOptionPane.ERROR_MESSAGE);
                                        }

                                    } else {
                                        System.out.println("NO");
                                    }

                                }

                            }

                        } else {
                            JOptionPane.showMessageDialog(this, "Please Select Valid Customer", "Invalid Customer", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Please Select Customer", "Empty Customer", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Please Check Your Connection", "Database Conneciton Error", JOptionPane.ERROR_MESSAGE);
                    logger.log(Level.WARNING, "Data failed to load", se);

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.WARNING, "Data failed to load", e);

                }

            }
        }


    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        OrderManagement om = new OrderManagement();
        om.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

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

    private void jTextField6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6KeyPressed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField6MouseClicked

    }//GEN-LAST:event_jTextField6MouseClicked

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // Select Products
        if (evt.getClickCount() == 2) {
            jTextField6.setText(String.valueOf(jTable3.getValueAt(jTable3.getSelectedRow(), 1)));
//            System.out.println(String.valueOf(jTable3.getValueAt(jTable3.getSelectedRow(), 1)));
            CalculateLensTotal();
        }
    }//GEN-LAST:event_jTable3MouseClicked

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Refresh();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        // TODO add your handling code here:
        CalculateLensTotal();
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyReleased
        // Copy Advance Payment to Payment Amount
        String advancePayment = jTextField11.getText();
        jTextField8.setText(advancePayment);
    }//GEN-LAST:event_jTextField11KeyReleased

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

    public void lensLoading() {
        try {
            ResultSet rs = MySQL.execute("SELECT * \n"
                    + "FROM lens_stock \n"
                    + "INNER JOIN supplier ON supplier.supplier_id = lens_stock.supplier_id ");

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
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacLightLaf.setup();


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OrderMaking().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel dateField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
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
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
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
