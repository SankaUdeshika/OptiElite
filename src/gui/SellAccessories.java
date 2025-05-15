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
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import models.MySQL;
import models.Reports;
import models.UserDetails;

public class SellAccessories extends javax.swing.JFrame {

    /**
     * Creates new form SellAccessories
     */
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    public SellAccessories() {
        initComponents();
        setSize(screen.width, screen.height);
        operater();
        time();
        Refresh();
    }

    double subTotal = 0.0;
    double Discount = 0.0;
    double total = 0.0;
    double advanced = 0.0;

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
            timeField.setText(time);
            dateField.setText(day);
        };
        Timer timer = new Timer(1000, timerListener);
        timer.setInitialDelay(0);
        timer.start();
    }

    public void Refresh() {
        LoadCustomer();
        LoadStockProducts();
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
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void LoadStockProducts() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `stock` INNER JOIN `product` ON `product`.`intid` = `stock`.`product_intid` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` INNER JOIN `category` ON `category`.`id` = `sub_category`.`category_id` INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `location` ON `location`.`id` = `stock`.`location_id`  WHERE  `stock`.`location_id` = '" + UserDetails.UserLocation_id + "'  AND `qty` > 0 ");
            DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("id"));
                v.add(rs.getString("sub_category"));
                v.add(rs.getString("saling_price"));
                v.add(rs.getString("qty"));
                v.add(rs.getString("stock_date"));

                dtm.addRow(v);
            }

        } catch (SQLException se) {
            logger.log(Level.WARNING, "Data failed to load", se);
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Conneciton", "Connection Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Data failed to load", e);

            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void CalculateSubTotal(String action, double price) {
        if (action == "add") {
            subTotal = subTotal + price;
            total = total + price;

            jLabel31.setText("Rs" + String.valueOf(subTotal));
            jLabel38.setText("Rs." + String.valueOf(total));

        } else if (action == "min") {
            subTotal = subTotal - price;
            total = total - price;

            jLabel31.setText("Rs" + String.valueOf(subTotal));
            jLabel38.setText("Rs." + String.valueOf(total));

        } else if (action == "Des") {
            Discount = price;
            total = total - Discount;
            jLabel31.setText("Rs" + String.valueOf(subTotal));
            jLabel38.setText("Rs." + String.valueOf(total));
        } else if (action == "advanced") {
            advanced = price;
            total = total - advanced;
            jLabel31.setText("Rs" + String.valueOf(subTotal));
            jLabel38.setText("Rs." + String.valueOf(total));
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        userNameField = new javax.swing.JLabel();
        dateField = new javax.swing.JLabel();
        timeField = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
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
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        jLabel5.setText("Accessories  Order");

        jLabel16.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel16.setText("Operator : ");

        jLabel32.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel32.setText("Date       :");

        jLabel36.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel36.setText("Time       :");

        userNameField.setText("user");

        dateField.setText("date");

        timeField.setText("time");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateField))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(timeField)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(userNameField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(dateField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(timeField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 585, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel5)))
                .addContainerGap(28, Short.MAX_VALUE))
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

        refreshBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/reload.png"))); // NOI18N
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
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

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
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
        jPanel6.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 80, 250, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel12.setText("Total Amount ");
        jPanel6.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 340, -1, -1));
        jPanel6.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 460, 420, 10));

        jLabel15.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel15.setText("Data Serching");
        jPanel6.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

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

        jPanel6.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 100, 250, 80));

        jLabel10.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel10.setText("Frame Id Or Brand");
        jPanel6.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, -1, -1));

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
        jPanel6.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 600, -1));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Brand", "Stock Id", "Stock Category", "Price", "Qty", "Stock Date"
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
            jTable3.getColumnModel().getColumn(2).setResizable(false);
            jTable3.getColumnModel().getColumn(3).setResizable(false);
            jTable3.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel6.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 600, 180));

        jLabel17.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel17.setText("Customer mobile Or Name");
        jPanel6.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 50, -1, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI Historic", 0, 36)); // NOI18N
        jLabel22.setText("-");
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 410, 20, 20));

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Bank Deposit");
        jPanel6.add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 440, -1, -1));

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Cash");
        jPanel6.add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, -1, -1));

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("Card");
        jPanel6.add(jRadioButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 400, -1, -1));

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("Online Payment");
        jPanel6.add(jRadioButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 420, -1, -1));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel6.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 360, 10, 100));

        jLabel23.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel23.setText("Pending Bill Items");
        jPanel6.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 340, -1, -1));

        jTextField2.setText("1");
        jPanel6.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 50, -1, -1));

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel6.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 370, 10, 100));

        jLabel30.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel30.setText("Qty");
        jPanel6.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 50, -1, -1));

        jLabel31.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jLabel31.setText("Rs.0.00");
        jPanel6.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 410, 110, 30));

        jLabel33.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel33.setText("Sub Total");
        jPanel6.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 370, 70, 30));

        jLabel34.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel34.setText("Total Price");
        jPanel6.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 470, -1, -1));

        jLabel38.setFont(new java.awt.Font("Segoe UI Historic", 1, 24)); // NOI18N
        jLabel38.setText("Rs.0.00");
        jPanel6.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 490, -1, -1));
        jPanel6.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 410, 110, -1));

        jLabel39.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel39.setText("Discount");
        jPanel6.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 370, -1, 30));

        jLabel27.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel27.setText("=");
        jPanel6.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 490, 30, 30));

        jButton5.setText("Add Discount");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 410, -1, -1));
        jPanel6.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 930, 10));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock Id", "Product", "Qty"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
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
        }

        jPanel6.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 370, 350, 120));

        jLabel24.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel24.setText("Payment Method");
        jPanel6.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, -1, -1));

        jButton6.setText("Delete Item");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 500, 150, -1));

        jButton10.setText("Add Items");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 50, 150, -1));

        jLabel35.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel35.setText("Advance Payment");
        jPanel6.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 470, 130, 20));
        jPanel6.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 500, 90, -1));

        jButton9.setText("Add");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 500, 60, -1));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select JobType", "Shop Orders", "EyeCamp Orders" }));
        jPanel6.add(jComboBox6, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 260, 260, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel13.setText("Order Types");
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 210, -1, -1));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1030, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1322, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 752, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (jTable4.getRowCount() > 0) {
            for (int x = 0; x < jTable4.getRowCount(); x++) {
                DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
                int selectRow = x;

                String stock_id = String.valueOf(jTable4.getValueAt(selectRow, 0));
                int itemSqty = (Integer) (jTable4.getValueAt(selectRow, 2));

                try {
                    ResultSet rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + stock_id + "' ");
                    if (rs.next()) {
                        int curruntQty = rs.getInt("qty");
                        int PlusQty = curruntQty + itemSqty;
                        MySQL.execute("UPDATE `stock` SET `qty` = '" + PlusQty + "' WHERE `id` = '" + stock_id + "' ");

                        double unitPrice = rs.getDouble("saling_price");
                        double subtotal = itemSqty * unitPrice;
                        CalculateSubTotal("min", subtotal);

                        JOptionPane.showMessageDialog(this, "Item Restore", "Success", JOptionPane.OK_OPTION);
                        dtm.removeRow(selectRow);
                        Refresh();

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }

        Dashboard d = new Dashboard();
        d.setVisible(true);
        this.dispose();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (jTable4.getRowCount() > 0) {
            for (int x = 0; x < jTable4.getRowCount(); x++) {
                DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
                int selectRow = x;

                String stock_id = String.valueOf(jTable4.getValueAt(selectRow, 0));
                int itemSqty = (Integer) (jTable4.getValueAt(selectRow, 2));

                try {
                    ResultSet rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + stock_id + "' ");
                    if (rs.next()) {
                        int curruntQty = rs.getInt("qty");
                        int PlusQty = curruntQty + itemSqty;
                        MySQL.execute("UPDATE `stock` SET `qty` = '" + PlusQty + "' WHERE `id` = '" + stock_id + "' ");

                        double unitPrice = rs.getDouble("saling_price");
                        double subtotal = itemSqty * unitPrice;
                        CalculateSubTotal("min", subtotal);

                        JOptionPane.showMessageDialog(this, "Item Restore", "Success", JOptionPane.OK_OPTION);
                        dtm.removeRow(selectRow);
                        Refresh();

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }

        Dashboard d = new Dashboard();
        d.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Bill The Order
        String Customer_mobile = jTextField1.getText();
        int paymentMethodSelecetd = 0;

        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String OrderDate = sdf.format(today);
        double Discount = 0.0;
        if (!jTextField3.getText().isEmpty()) {
            try {
                Discount = Double.parseDouble(jTextField3.getText());
            } catch (NumberFormatException e) {
                Discount = 0.0; // default value if invalid input
            }
        }

        double InsertSubTotal = subTotal;

        if (buttonGroup1.getSelection() != null) {
            //                                        payment Assign
            //                                   payment Method Validation
            if (jRadioButton2.isSelected()) {
                paymentMethodSelecetd = 1;
            } else if (jRadioButton3.isSelected()) {
                paymentMethodSelecetd = 2;
            } else if (jRadioButton4.isSelected()) {
                paymentMethodSelecetd = 3;
            } else if (jRadioButton1.isSelected()) {
                paymentMethodSelecetd = 4;
            }

            int jobType = jComboBox6.getSelectedIndex();
            if (jobType == 0) {
                JOptionPane.showMessageDialog(this, "Please Select ORder Type");
            } else {
                try {
                    if (!Customer_mobile.isEmpty()) {
                        ResultSet cust_rs = MySQL.execute("SELECT * FROM `customer` WHERE `mobile` = '" + Customer_mobile + "'");
                        if (cust_rs.next()) {

                            int paymentStatus = 0;
                            if (advanced == 0.0) {
                                paymentStatus = 2;
                            } else {
                                paymentStatus = 1;
                            }

                            //                                    Invoice INSERT PROCESS
                            ResultSet Inser_rs = MySQL.execute("INSERT INTO `invoice` (`date`,`total_price`,`customer_mobile`,`payment_method_Payment_id`,`discount`,`subtotal`,`advance_payment`,`JobType_job_id`, payment_status_id)"
                                    + " VALUES ('" + OrderDate + "','" + total + "','" + Customer_mobile + "','" + paymentMethodSelecetd + "','" + Discount + "','" + InsertSubTotal + "','" + advanced + "','" + jobType + "', '" + paymentStatus + "') ");

                            int invoiceId;
                            if (Inser_rs.next()) {
                                invoiceId = Inser_rs.getInt(1);

//                            Loop
                                int rowCount = jTable4.getRowCount();

                                for (int x = 0; x < rowCount; x++) {
                                    int selectRow = x;
                                    System.out.println(x);
                                    String stock_id = String.valueOf(jTable4.getValueAt(selectRow, 0));

                                    //                            Frame Id Validation
                                    if (!stock_id.isEmpty()) {
//                                        ResultSet Frame_rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + stock_id + "' AND `qty` > 0 ");
//                                        if (Frame_rs.next()) {
                                        //                                      Add Invoice Items
                                        MySQL.execute("INSERT INTO `invoice_item` (`invoice_id`,`stock_id`,`qty`)"
                                                + " VALUES ('" + invoiceId + "','" + stock_id + "','" + jTextField2.getText() + "') ");
                                        JOptionPane.showMessageDialog(this, "Order Adding Success", "Success", JOptionPane.OK_OPTION);

//                                        } else {
//                                            JOptionPane.showMessageDialog(this, "Please Select a Valid Stock", "InValid  Frame id", JOptionPane.ERROR_MESSAGE);
//                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(this, "Please Select a Stock", "Empty  Frame id", JOptionPane.ERROR_MESSAGE);
                                    }
                                }

                                Refresh();
//                            Print Bill
                                Reports.WithoutPrescriptionOrderPurchaceInvoice(String.valueOf(invoiceId));
                                Refresh();
                            } else {
                                JOptionPane.showMessageDialog(this, "Some thing went Wrong, Please Try again later", "Invalid Customer", JOptionPane.ERROR_MESSAGE);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please Select a Payment Method", "Database Conneciton Error", JOptionPane.ERROR_MESSAGE);
        }

//

    }//GEN-LAST:event_jButton4ActionPerformed

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
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // Click Jtable2 load Prescription

        if (evt.getClickCount() == 2) {
            int SelectedRow = jTable2.getSelectedRow();
            jTextField1.setText(String.valueOf(jTable2.getValueAt(SelectedRow, 0)));
            int selectedRow = jTable2.getSelectedRow();
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
            ResultSet rs = MySQL.execute("SELECT * FROM `product` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `category` ON `category`.`id` = `sub_category`.`category_id` INNER JOIN `stock` ON `stock`.`product_id` = `product`.`id` INNER JOIN `location` ON `stock`.`location_id`  = `location`.`id` "
                + " WHERE `category`.`id` =  '1'  AND `stock`.`location_id` = '" + UserDetails.UserLocation_id + "' AND  `brand_name` LIKE '%" + brand_details + "%' OR `product`.`id` LIKE '%" + brand_details + "%' AND `category`.`id` =  '1' AND `stock`.`location_id` = '" + UserDetails.UserLocation_id + "' ");
            DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("id"));
                v.add(rs.getString("sub_category"));
                v.add(rs.getString("saling_price"));
                v.add(rs.getString("qty"));
                v.add(rs.getString("stock_date"));
                dtm.addRow(v);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Conneciton", "Connection Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // Select Products
//        if (evt.getClickCount() == 2) {
//            jTextField6.setText(String.valueOf(jTable3.getValueAt(jTable3.getSelectedRow(), 1)));
//            CalculateLensTotal();
//        }
    }//GEN-LAST:event_jTable3MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Adding Discount
        double TextDiscount = Double.parseDouble(jTextField3.getText());

        CalculateSubTotal("Des", TextDiscount);

//        double DiscountTotal = SubTotal - TextDiscount;
//        //        System.out.println(DiscountTotal + " - " + SubTotal);
//        jLabel38.setText(String.valueOf(DiscountTotal));
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable4MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Delete Item
        int selectRow = jTable4.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
        if (selectRow == -1) {
            JOptionPane.showMessageDialog(this, "Please Select a Item Row", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            String stock_id = String.valueOf(jTable4.getValueAt(selectRow, 0));
            int itemSqty = (Integer) (jTable4.getValueAt(selectRow, 2));

            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + stock_id + "' ");
                if (rs.next()) {
                    int curruntQty = rs.getInt("qty");
                    int PlusQty = curruntQty + itemSqty;
                    MySQL.execute("UPDATE `stock` SET `qty` = '" + PlusQty + "' WHERE `id` = '" + stock_id + "' ");

                    double unitPrice = rs.getDouble("saling_price");
                    double subtotal = itemSqty * unitPrice;
                    CalculateSubTotal("min", subtotal);

                    JOptionPane.showMessageDialog(this, "Item Restore", "Success", JOptionPane.WARNING_MESSAGE);
                    dtm.removeRow(selectRow);
                    Refresh();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // Add Items to the Invoice Items
        int selectRow = jTable3.getSelectedRow();

        if (selectRow != -1) {
            int qty = Integer.parseInt(jTextField2.getText());

            if (qty > 0) {
                System.out.println(qty);
                String Stock_id = String.valueOf(jTable3.getValueAt(selectRow, 1));
                String brand_name = String.valueOf(jTable3.getValueAt(selectRow, 0));
                String sub_category = String.valueOf(jTable3.getValueAt(selectRow, 2));

                try {
                    ResultSet rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + Stock_id + "' ");
                    if (rs.next()) {
                        int curruntQty = rs.getInt("qty");
                        int minusQty = curruntQty - qty;

                        if (minusQty < 0) {
                            JOptionPane.showMessageDialog(this, "Invalid stock", "Error", JOptionPane.OK_OPTION);
                        } else {
                            double unitPrice = rs.getDouble("saling_price");
                            double subtotal = qty * unitPrice;
                            MySQL.execute("UPDATE `stock` SET `qty` = '" + minusQty + "' WHERE `id` = '" + Stock_id + "' ");
                            CalculateSubTotal("add", subtotal);
                            JOptionPane.showMessageDialog(this, "Item Added", "Success", JOptionPane.WARNING_MESSAGE);

                            DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
                            Vector v = new Vector();
                            v.add(Stock_id);
                            v.add(brand_name + " " + sub_category);
                            v.add(qty);

                            dtm.addRow(v);
                            Refresh();

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please enter valid qty", "Error", JOptionPane.ERROR_MESSAGE);
            }

//         
        } else {
            JOptionPane.showMessageDialog(this, "Please Select Invoice Row First", "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_jButton10ActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        LoadCustomer();
        LoadStockProducts();
        subTotal = 0.0;
        Discount = 0.0;
        total = 0.0;

        jLabel31.setText(String.valueOf(subTotal));
        jLabel38.setText(String.valueOf(total));
        jTextField3.setText("");

        if (jTable4.getRowCount() > 0) {
            for (int x = 0; x < jTable4.getRowCount(); x++) {
                DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
                int selectRow = x;

                String stock_id = String.valueOf(jTable4.getValueAt(selectRow, 0));
                int itemSqty = (Integer) (jTable4.getValueAt(selectRow, 2));

                try {
                    ResultSet rs = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + stock_id + "' ");
                    if (rs.next()) {
                        int curruntQty = rs.getInt("qty");
                        int PlusQty = curruntQty + itemSqty;
                        MySQL.execute("UPDATE `stock` SET `qty` = '" + PlusQty + "' WHERE `id` = '" + stock_id + "' ");

                        double unitPrice = rs.getDouble("saling_price");
                        double subtotal = itemSqty * unitPrice;
                        CalculateSubTotal("min", subtotal);

                        JOptionPane.showMessageDialog(this, "Item Restore", "Success", JOptionPane.OK_OPTION);
                        dtm.removeRow(selectRow);
                        Refresh();

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // advance Payment
        double advancePayment = Double.parseDouble(jTextField11.getText());
        CalculateSubTotal("advanced", advancePayment);


    }//GEN-LAST:event_jButton9ActionPerformed

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
            java.util.logging.Logger.getLogger(SellAccessories.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SellAccessories.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SellAccessories.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SellAccessories.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SellAccessories().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel dateField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JLabel timeField;
    private javax.swing.JLabel userNameField;
    // End of variables declaration//GEN-END:variables
}
