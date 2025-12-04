package gui;

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
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import models.MySQL;
import models.Reports;
import models.UserDetails;

public class StockManagement extends javax.swing.JFrame {

    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    public StockManagement() {
        initComponents();
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `users` WHERE `id` = '" + UserDetails.UserId + "' ");
            if (rs.next()) {
                int userType_id = rs.getInt("user_type_id");
                if (userType_id == 2) {
                    Dashboard d = new Dashboard();
                    d.setVisible(true);
                    this.dispose();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Data failed to load", e);

        }
        setSize(screen.width, screen.height);
        Refresh();
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
            timeField.setText(time);
            dateField.setText(day);
        };
        Timer timer = new Timer(1000, timerListener);
        timer.setInitialDelay(0);
        timer.start();
    }

//    Stock Query
    String JasperStockQuerry = "";

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
            jComboBox1.setModel(dfm);

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", "Please Check Your Internet Connection or Please Try again later", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", se);

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Data failed to load", e);

        }
    }

    public void loadLocations(String Locaiton) {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `location`");
            Vector v = new Vector();

            v.add(Locaiton + " Selected");
            while (rs.next()) {
                v.add(String.valueOf(rs.getString("id") + ") " + rs.getString("location_name")));
            }

            DefaultComboBoxModel dfm = new DefaultComboBoxModel<>(v);
            jComboBox3.setModel(dfm);

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", "Please Check Your Internet Connection or Please Try again later", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Data failed to load", se);

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Data failed to load", e);

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
                    v.add(rs.getString("brand_name"));
                    v.add(rs.getString("sub_category"));
                    dtm.addRow(v);

                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.log(Level.WARNING, "Data failed to load", e);

            }
        }
    }

    public void Refresh() {
        jLabel8.setText("");
        LoadStockTable();
        loadLocations();
        AddinProductStock("");
        loadBrands();
        jTextField11.setEnabled(true);

        jTextField11.setText("");
        jTextField6.setText("");
        jTextField5.setText("");
        jDateChooser2.setDate(new Date());

        jTextField4.setText("");
        jTextField13.setText("");
        jTextField15.setText("");

        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);

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
                v.add(String.valueOf(v));

                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Data failed to load", e);

        }
    }

    public void loadBrands() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `brand`");
            Vector v = new Vector();

            v.add("Select Brand");
            while (rs.next()) {
                v.add(String.valueOf(rs.getString("id") + ") " + rs.getString("brand_name")));
            }

            DefaultComboBoxModel dfm = new DefaultComboBoxModel<>(v);
            jComboBox2.setModel(dfm);

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", "Please Check Your Internet Connection or Please Try again later", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Data failed to load", e);

        }
    }

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
        jLabel11 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jTextField7 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel23 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jTextField13 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
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
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
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
        jButton4.setText("Update Stock");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Customer management.png"))); // NOI18N
        jButton6.setText("Stock Adding");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel7.setText("Pages");

        jPanel6.setBackground(new java.awt.Color(206, 206, 206));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel6.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 480, 600, 10));

        jLabel13.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel13.setText("Stock Infomations");
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel11.setText("Stock ID");
        jPanel6.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, -1, -1));
        jPanel6.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 520, 80, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jPanel6.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 410, -1, -1));

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
        jPanel6.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 440, 120, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel15.setText("Stock Date");
        jPanel6.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 410, -1, -1));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "StockID", "Product ID", "Product Brand", "Sub Category", "Qty", "SKU", "Unit Price", "Selling Price", "Date TIme", "Supplier", "Location", "Total Cost"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
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

        jPanel6.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 1040, 190));

        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });
        jPanel6.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 120, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel16.setText("Sub Category");
        jPanel6.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 410, -1, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jLabel17.setText("Search Option is currentylu unavailable");
        jPanel6.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, -1));

        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });
        jPanel6.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 100, 120, -1));

        jLabel18.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel18.setText("SKU ");
        jPanel6.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, -1, -1));

        jLabel19.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel19.setText("Supplier");
        jPanel6.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 70, -1, -1));

        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });
        jPanel6.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 100, 120, -1));

        jLabel20.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel20.setText("Location");
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 70, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel6.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 100, 120, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jPanel6.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 100, 120, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel22.setText("Stock Date");
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 70, -1, -1));
        jPanel6.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 100, 140, -1));

        jLabel23.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel23.setText("Brand");
        jPanel6.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 70, -1, -1));

        jButton5.setText("Search");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 100, 70, -1));

        jLabel24.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel24.setText("Quantity");
        jPanel6.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 490, -1, -1));

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
        jPanel6.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 440, 110, -1));

        jLabel25.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel25.setText("Product ID");
        jPanel6.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 410, -1, -1));
        jPanel6.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 440, 170, -1));
        jPanel6.add(jTextField13, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 520, 130, 30));

        jLabel27.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel27.setText("Buying Unit Price");
        jPanel6.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 490, 130, 20));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        jPanel6.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 440, 180, -1));

        jLabel28.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel28.setText("Size");
        jPanel6.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 410, -1, -1));

        jLabel30.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel30.setText("Selling Price");
        jPanel6.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 490, -1, 20));
        jPanel6.add(jTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 520, 120, 30));
        jPanel6.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 920, 10));

        jLabel29.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel29.setText("Stock Details");
        jPanel6.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 370, -1, -1));

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
        jPanel6.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 440, 110, -1));

        jLabel32.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel32.setText("Product Brand");
        jPanel6.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 410, -1, -1));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Product ID", "Brand", "SubCategory"
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

        jPanel6.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 470, 380, 110));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 0, 0));
        jPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 280, 30));
        jPanel6.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 440, 190, -1));

        jLabel31.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel31.setText("Location");
        jPanel6.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 410, -1, -1));
        jPanel6.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 140, -1));

        jLabel26.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel26.setText("Product ID");
        jPanel6.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, -1, -1));

        jToggleButton1.setText("Report");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7)
                    .addComponent(jSeparator4)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator3)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(44, 44, 44)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jButton3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4)
                        .addGap(24, 24, 24)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jToggleButton1))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel10.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel10.setText("Operator : ");

        jLabel12.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel12.setText("Date       :");

        jLabel21.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel21.setText("Time       :");

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
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateField))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel21)
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
                    .addComponent(jLabel12)
                    .addComponent(dateField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(timeField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        jLabel5.setText("Stock Management");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel5)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 9, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
        // Update Stock Process

        String StockID;

        try {
            StockID = String.valueOf(jTable3.getValueAt(jTable3.getSelectedRow(), 0));

        } catch (ArrayIndexOutOfBoundsException e) {
            StockID = "null";
        }

        String product_id = jTextField11.getText();

        Date chooseDate;
        String formatDate = "";
        try {
            chooseDate = jDateChooser2.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            formatDate = sdf.format(chooseDate);
        } catch (NullPointerException ne) {
            chooseDate = null;
            logger.log(Level.WARNING, "null exception", ne);

        }

        String qty = jTextField4.getText();
        String cost = jTextField13.getText();
        String sellingPrice = jTextField15.getText();
        String Size = jTextField1.getText();

        if (StockID.equals("null")) {
            JOptionPane.showMessageDialog(this, "Please Select a Stock From stock Table", "Empty Stock Id", JOptionPane.ERROR_MESSAGE);
        } else if (product_id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Select a Product From Product Table", "Empty Product Id", JOptionPane.ERROR_MESSAGE);
        } else if (chooseDate == null) {
            JOptionPane.showMessageDialog(this, "Please Select a Date ", "Empty Date ", JOptionPane.ERROR_MESSAGE);
        } else if (qty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Quantity Amount ", "Empty Quantity ", JOptionPane.ERROR_MESSAGE);
        } else if (cost.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Buying  Amount ", "Empty Cost ", JOptionPane.ERROR_MESSAGE);
        } else if (sellingPrice.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Selling  Amount ", "Empty Selling Price ", JOptionPane.ERROR_MESSAGE);
        } else if (jComboBox3.getSelectedIndex() == 0) {
            try {

                MySQL.execute("UPDATE `stock` SET `product_id` = '" + product_id + "' , `stock_date` = '" + formatDate + "' , `qty` = '" + qty + "' , `cost` = '" + cost + "' , `saling_price` = '" + sellingPrice + "' , `frameSize` = '" + Size + "' WHERE `id` = '" + StockID + "' ");
                JOptionPane.showMessageDialog(this, "Stock Adding Success ", "Insert Success", JOptionPane.ERROR_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
                logger.log(Level.WARNING, "Failed to update", e);

            }
        } else if (jComboBox3.getSelectedIndex() != 0) {
            try {

                MySQL.execute("UPDATE `stock` SET `product_id` = '" + product_id + "' , `stock_date` = '" + formatDate + "' , `qty` = '" + qty + "' , `cost` = '" + cost + "' , `saling_price` = '" + sellingPrice + "' , `location_id` = '" + jComboBox3.getSelectedIndex() + "' , `frameSize` = '" + Size + "' WHERE `id` = '" + StockID + "' ");
                JOptionPane.showMessageDialog(this, "Stock Adding Success ", "Insert Success", JOptionPane.ERROR_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
                logger.log(Level.WARNING, "Data failed to update", e);

            }
        }

        Refresh();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

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
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("sub_category"));
                dtm.addRow(v);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Connection. and Please Try again Later");
            logger.log(Level.WARNING, "Data failed to load", se);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Some Thing Wrong Please Try again Later or Contact Devloper");
            logger.log(Level.WARNING, "Data failed to load", e);

        }
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

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
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("sub_category"));
                dtm.addRow(v);

            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Connection. and Please Try again Later");
            logger.log(Level.WARNING, "Data failed to load", se);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Some Thing Wrong Please Try again Later or Contact Devloper");
            logger.log(Level.WARNING, "Data failed to load", e);

        }
    }//GEN-LAST:event_jTextField11KeyReleased

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

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
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("sub_category"));
                dtm.addRow(v);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Connection. and Please Try again Later");
            logger.log(Level.WARNING, "Data failed to load", se);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Some Thing Wrong Please Try again Later or Contact Devloper");
            logger.log(Level.WARNING, "Data failed to load", e);

        }
    }//GEN-LAST:event_jTextField6KeyReleased

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

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // Select Date form Table
        if (evt.getClickCount() == 2) {
            int row = jTable3.getSelectedRow();
            jLabel8.setText("Selected Stock Id = " + jTable3.getValueAt(row, 0));

            jTextField11.setText(String.valueOf(jTable3.getValueAt(row, 1)));
            jTextField11.setEnabled(false);
            jTextField6.setText(String.valueOf(jTable3.getValueAt(row, 2)));
            jTextField5.setText(String.valueOf(jTable3.getValueAt(row, 3)));
            jTextField4.setText(String.valueOf(jTable3.getValueAt(row, 4)));
            jTextField1.setText(String.valueOf(jTable3.getValueAt(row, 5)));
            jTextField13.setText(String.valueOf(jTable3.getValueAt(row, 6)));
            jTextField15.setText(String.valueOf(jTable3.getValueAt(row, 7)));

            jDateChooser2.setDate((Date) jTable3.getValueAt(row, 7));

            loadLocations(String.valueOf(jTable3.getValueAt(row, 9)));

        }
    }//GEN-LAST:event_jTable3MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // refresh
        Refresh();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // 
        StockAdd sa = new StockAdd();
        sa.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("YYYY-MM-dd");
        String ToDate;

        try {
            ToDate = simpleDateformat.format(jDateChooser1.getDate());
            jLabel22.setText(ToDate);
        } catch (NullPointerException ne) {
            ToDate = "null";
        }
        JasperStockQuerry = "";

        String querry = "SELECT * FROM `stock`"
                + " INNER JOIN `product` ON `product`.`intid` = `stock`.`product_intid`"
                + " INNER JOIN `supplier` ON `supplier`.`supplier_id` = `stock`.`supplier_supplier_id` "
                + " INNER JOIN `location` ON `location`.`id` = `stock`.`location_id` "
                + " INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` "
                + " INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id`";

        if (!jTextField7.getText().isEmpty()) {
            querry += " WHERE `stock`.`id` = '" + jTextField7.getText() + "'";
            JasperStockQuerry += " WHERE `stock`.`id` = '" + jTextField7.getText() + "'";
        } else if (!jTextField8.getText().isEmpty()) {
            querry += " WHERE `product`.`id` = '" + jTextField8.getText() + "'";
            JasperStockQuerry += " WHERE `product`.`id` = '" + jTextField8.getText() + "'";
        } else if (!jTextField9.getText().isEmpty()) {
            querry += " WHERE `supplier`.`supplier_id` LIKE '%" + jTextField9.getText() + "%'";
            JasperStockQuerry += " WHERE `supplier`.`supplier_id` LIKE '%" + jTextField9.getText() + "%'";
        } else if (!jTextField2.getText().isEmpty()) {
            querry += " WHERE `stock`.`SKU` = '" + jTextField2.getText() + "'";
            JasperStockQuerry += " WHERE `stock`.`SKU` = '" + jTextField2.getText() + "'";
        }

        if (jComboBox1.getSelectedIndex() != 0) {
            querry += " AND `stock`.`location_id` = '" + jComboBox1.getSelectedIndex() + "'";
            JasperStockQuerry += " AND `stock`.`location_id` = '" + jComboBox1.getSelectedIndex() + "'";

        }

        if (jComboBox2.getSelectedIndex() != 0) {
            String brandText = (String) jComboBox2.getSelectedItem();
            String brandArray[] = brandText.split(" ");
            String brand = brandArray[1];

            querry += " AND `brand`.`brand_name` = '" + brand + "'";
            JasperStockQuerry += " AND `brand`.`brand_name` = '" + brand + "'";

        }

        if (jDateChooser1.getDate() != null) {
            querry += " AND `stock_date` >= '" + ToDate + "' ";
            JasperStockQuerry += " AND `stock_date` >= '" + ToDate + "' ";
        }

//        System.out.println(querry);
        try {

            ResultSet rs = MySQL.execute(querry);

            DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
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
                v.add(String.valueOf(v));

                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Data failed to load", e);

        }


    }//GEN-LAST:event_jButton5ActionPerformed


    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // print Stock Report
        System.out.println(JasperStockQuerry);
//        System.out.println(JasperStockQuerry);
        DefaultTableModel defaultTableModel = (DefaultTableModel) jTable3.getModel();

        Reports.PrintStockReport(defaultTableModel);
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacLightLaf.setup();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StockManagement().setVisible(true);
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
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
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
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel timeField;
    private javax.swing.JLabel userNameField;
    // End of variables declaration//GEN-END:variables
}
