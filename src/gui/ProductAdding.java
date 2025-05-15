package gui;

import gui.LensStockAdding;
import models.UserDetails;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.MySQL;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class ProductAdding extends javax.swing.JFrame {

    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    public ProductAdding() {
        initComponents();
        setSize(screen.width, screen.height);

        Refresh();
        operater();
        time();
    }

    HashMap<String, Integer> brandMap = new HashMap<>();
    HashMap<String, Integer> CategoryMap = new HashMap<>();
    HashMap<String, Integer> SupplierMap = new HashMap<>();
    HashMap<String, Integer> SubCategoryMap = new HashMap<>();

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
        jTextField1.setText("");
        jTextField2.setText("");

        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
        jComboBox5.setSelectedIndex(0);

        jTextField3.setText("");
        jTextField3.setEnabled(false);

//        jButton9.setEnabled(false);
        LoadBrands();
        LoadCategory();
        LoadSupplier();
        loadSubCategory("");
        loadProduct();
    }

    public void loadProduct() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `product` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id`  INNER JOIN `brand` ON `product`.`brand_id` = `brand`.`id` ");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("id"));
                v.add(rs.getString("sub_category"));
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("product_name"));

                dtm.addRow(v);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadCategory() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `category`");
            Vector v = new Vector();
            CategoryMap.clear();
            v.add("Select Category");
            while (rs.next()) {
                v.add(String.valueOf(rs.getString("Category")));
                CategoryMap.put(rs.getString("Category"), rs.getInt("id"));
            }

            DefaultComboBoxModel dfm = new DefaultComboBoxModel<>(v);
            jComboBox5.setModel(dfm);

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", "Please Check Your Internet Connection or Please Try again later", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadBrands() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `brand` ");
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("id"));
                v.add(rs.getString("brand_name"));
                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadSupplier() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `supplier` INNER JOIN `company` ON `company`.`id` = `supplier`.`company_id`");
            Vector v = new Vector();
            v.add("Select Supplier");

            SupplierMap.clear();

            while (rs.next()) {
                v.add(String.valueOf(rs.getString("CompanyName")) + "-->" + String.valueOf(rs.getString("Supplier_Name")));
                SupplierMap.put(rs.getString("Supplier_Name"), rs.getInt("supplier_id"));
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

    public void loadSubCategory(String category) {
        try {

            if (category == "") {
//              null Category
                ResultSet rs = MySQL.execute("SELECT * FROM `sub_category` ");
                Vector v = new Vector();
                v.add("Select Sub Category");

                SupplierMap.clear();

                while (rs.next()) {
                    v.add(String.valueOf(rs.getString("sub_category")));
                    SubCategoryMap.put(rs.getString("sub_category"), rs.getInt("id"));
                }

                DefaultComboBoxModel dfm = new DefaultComboBoxModel<>(v);
                jComboBox2.setModel(dfm);

            } else if (category == "frames") {
//                Frame Category
                ResultSet rs = MySQL.execute("SELECT * FROM `sub_category` WHERE `category_id`  = '1'");
                Vector v = new Vector();
                v.add("Select Category");

                SupplierMap.clear();

                while (rs.next()) {
                    v.add(String.valueOf(rs.getString("sub_category")));
                    SubCategoryMap.put(rs.getString("sub_category"), rs.getInt("id"));
                }

                DefaultComboBoxModel dfm = new DefaultComboBoxModel<>(v);
                jComboBox2.setModel(dfm);

            } else if (category == "lenses") {
//                Lenses Category
                ResultSet rs = MySQL.execute("SELECT * FROM `sub_category` WHERE `category_id`  = '2'");
                Vector v = new Vector();
                v.add("Select Category");

                SupplierMap.clear();

                while (rs.next()) {
                    v.add(String.valueOf(rs.getString("sub_category")));
                    SubCategoryMap.put(rs.getString("sub_category"), rs.getInt("id"));
                }

                DefaultComboBoxModel dfm = new DefaultComboBoxModel<>(v);
                jComboBox2.setModel(dfm);
            } else if (category == "Accessories") {
//                Accessories Category
                ResultSet rs = MySQL.execute("SELECT * FROM `sub_category` WHERE `category_id`  = '3'");
                Vector v = new Vector();
                v.add("Select Category");
                SupplierMap.clear();
                while (rs.next()) {
                    v.add(String.valueOf(rs.getString("sub_category")));
                    SubCategoryMap.put(rs.getString("sub_category"), rs.getInt("id"));

                }
                DefaultComboBoxModel dfm = new DefaultComboBoxModel<>(v);
                jComboBox2.setModel(dfm);
            } else if (category == "SunGlass") {
//                Accessories Category
                ResultSet rs = MySQL.execute("SELECT * FROM `sub_category` WHERE `category_id`  = '4'");
                Vector v = new Vector();
                v.add("Select Category");
                SupplierMap.clear();
                while (rs.next()) {
                    v.add(String.valueOf(rs.getString("sub_category")));
                    SubCategoryMap.put(rs.getString("sub_category"), rs.getInt("id"));

                }
                DefaultComboBoxModel dfm = new DefaultComboBoxModel<>(v);
                jComboBox2.setModel(dfm);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", "Please Check Your Internet Connection or Please Try again later", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        userNameField = new javax.swing.JLabel();
        dateField = new javax.swing.JLabel();
        timeField = new javax.swing.JLabel();
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
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton7 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        jLabel5.setText("Product Adding");

        jLabel16.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel16.setText("Operator : ");

        jLabel32.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel32.setText("Date       :");

        jLabel33.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel33.setText("Time       :");

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
                        .addComponent(jLabel33)
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
                    .addComponent(jLabel33)
                    .addComponent(timeField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel5))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/reload.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel6.setText("Actions");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/diskette (1).png"))); // NOI18N
        jButton4.setText("Save Item");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Customer management.png"))); // NOI18N
        jButton6.setText("Product Management");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel7.setText("Pages");

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel8.setText("Product Code");
        jPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 170, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel9.setText("Product Brand");
        jPanel6.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, -1, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel10.setText("Description");
        jPanel6.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 230, 100, 30));
        jPanel6.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 190, 252, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel13.setText("Static Item Infomations");
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane1.setViewportView(jTextArea3);

        jPanel6.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 260, 250, 180));

        jLabel20.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel20.setText("Product Name");
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 160, -1, -1));

        jLabel21.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel21.setText("Product Table");
        jPanel6.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 80, -1, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Police", "Blue Ray", "Polorioid" }));
        jPanel6.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 110, 250, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel22.setText("Quality");
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Quality", "Branded", "High Copy", "Fake" }));
        jPanel6.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 250, -1));

        jLabel23.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel23.setText("Suplier ");
        jPanel6.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, 20));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Police", "Blue Ray", "Polorioid" }));
        jPanel6.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 250, 20));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Police", "Blue Ray", "Polorioid" }));
        jComboBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox5ItemStateChanged(evt);
            }
        });
        jPanel6.add(jComboBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 250, -1));

        jLabel24.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel24.setText("Category");
        jPanel6.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, -1, -1));

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 252, -1));

        jButton5.setText("Add");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 300, 70, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Product Id", "Sub Category", "Brand", "Product Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setHeaderValue("Brand");
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setHeaderValue("Product Name");
        }

        jPanel6.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 110, 450, 340));

        jLabel25.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel25.setText("Sub Category");
        jPanel6.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, -1, -1));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel6.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 110, 10, 340));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Brand Id", "Brand Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            jTable2.getColumnModel().getColumn(1).setResizable(false);
        }

        jPanel6.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 250, 100));

        jButton7.setText("Change");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 500, 228, 42));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(134, 134, 134)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(46, 46, 46))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel7)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1030, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(69, Short.MAX_VALUE))
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
            .addGap(0, 800, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged
        // TODO add your handling code here:
        if (jComboBox5.getSelectedIndex() == 1) { //Frames
            jTextField3.setText("");
            jTextField3.setEnabled(false);
            loadSubCategory("frames");
        } else if (jComboBox5.getSelectedIndex() == 2) { // Lenses
            jTextField3.setText("");
            jTextField3.setEnabled(false);
            loadSubCategory("lenses");

        } else if (jComboBox5.getSelectedIndex() == 3) { // Accessories
            jTextField3.setText("");
            jTextField3.setEnabled(true);
            loadSubCategory("Accessories");
        } else if (jComboBox5.getSelectedIndex() == 4) { // Accessories
            jTextField3.setText("");
            jTextField3.setEnabled(false);
            loadSubCategory("SunGlass");
        } else if (jComboBox5.getSelectedIndex() == 0) { // Accessories
            jTextField3.setText("");
            jTextField3.setEnabled(false);
            loadSubCategory("");
        }

    }//GEN-LAST:event_jComboBox5ItemStateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Adding Product Brand
        String brand = jTextField1.getText();

        try {
            ResultSet brand_rs = MySQL.execute("SELECT * FROM `brand` WHERE `brand_name` = '" + brand + "' ");
            if (brand_rs.next()) {
                JOptionPane.showMessageDialog(this, "Already Registerd this Brand", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (brand.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Plese Enter a brand text");
                } else {
                    MySQL.execute("INSERT INTO `brand` (`brand_name`) VALUES ('" + brand + "')");
                    JOptionPane.showMessageDialog(this, "Brand Insert Success");
                    Refresh();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Something Worng, ", "Please Try again later", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }


    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Product Adding Process
        String Productcode = jTextField2.getText();
        String ProductName = jTextField3.getText();
        String Category_name = String.valueOf(jComboBox5.getSelectedItem());
        String Sub_Category_name = String.valueOf(jComboBox2.getSelectedItem());
        String supplier_name = String.valueOf(jComboBox4.getSelectedItem());
        String Quality = String.valueOf(jComboBox3.getSelectedItem());
        String Description = jTextArea3.getText();
//        String brand_name = String.valueOf(jComboBox1.getSelectedItem());

        int select = jTable2.getSelectedRow();
        String brandid = "null";

        if (select == -1) {
            brandid = "null";
        } else {
            brandid = (String) jTable2.getValueAt(select, 0);
        }

        try {
            if (!Productcode.isEmpty()) {
                ResultSet rs = MySQL.execute("SELECT * FROM `product` WHERE `id` = '" + Productcode + "' ");

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Already Enterd Product Id", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
                    int chooseResult = JOptionPane.showConfirmDialog(this, "Already have a product. are you sure to add another?", "Dupolicate product code", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (chooseResult == JOptionPane.YES_OPTION) {
                        if (jComboBox5.getSelectedIndex() == 0) {// Check Category is Empty
                            JOptionPane.showMessageDialog(this, "Please Select Product Category", "Empty Inputs", JOptionPane.ERROR_MESSAGE);
                        } else if (brandid == "null") { // Check Brand is Empty
                            JOptionPane.showMessageDialog(this, "Please Select Product Brand", "Empty Inputs", JOptionPane.ERROR_MESSAGE);
                        } else if (jComboBox2.getSelectedIndex() == 0) { //Check SubCategorie Null
                            JOptionPane.showMessageDialog(this, "Please Select Product Sub Category", "Empty User Inputs", JOptionPane.ERROR_MESSAGE);
                        } else if (supplier_name.equals("Select Supplier")) {
                            JOptionPane.showMessageDialog(this, "Please Select Suplier", "Empty User Inputs", JOptionPane.ERROR_MESSAGE);
                        } else if (jComboBox3.getSelectedIndex() == 0) { //Check Quality Null
                            JOptionPane.showMessageDialog(this, "Please Select Quality", "Empty USer Inputs", JOptionPane.ERROR_MESSAGE);
                        } else {

                            MySQL.execute("INSERT INTO `product` (`id`,`sub_category_id`,`brand_id`,`description`,`Quality_id`,`product_name`) "
                                    + "VALUES('" + Productcode + "','" + SubCategoryMap.get(Sub_Category_name) + "','" + brandid + "','" + Description + "','" + jComboBox3.getSelectedIndex() + "','" + ProductName + "')");
                            JOptionPane.showMessageDialog(this, "Insert Success", "Success", JOptionPane.OK_OPTION);
                            Refresh();
                        }
                    }

                } else {
//                Validation
                    if (jComboBox5.getSelectedIndex() == 0) {// Check Category is Empty
                        JOptionPane.showMessageDialog(this, "Please Select Product Category", "Empty Inputs", JOptionPane.ERROR_MESSAGE);
                    } else if (brandid == "null") { // Check Brand is Empty
                        JOptionPane.showMessageDialog(this, "Please Select Product Brand", "Empty Inputs", JOptionPane.ERROR_MESSAGE);
                    } else if (jComboBox2.getSelectedIndex() == 0) { //Check SubCategorie Null
                        JOptionPane.showMessageDialog(this, "Please Select Product Sub Category", "Empty User Inputs", JOptionPane.ERROR_MESSAGE);
                    } else if (supplier_name.equals("Select Supplier")) {
                        JOptionPane.showMessageDialog(this, "Please Select Suplier", "Empty User Inputs", JOptionPane.ERROR_MESSAGE);
                    } else if (jComboBox3.getSelectedIndex() == 0) { //Check Quality Null
                        JOptionPane.showMessageDialog(this, "Please Select Quality", "Empty USer Inputs", JOptionPane.ERROR_MESSAGE);
                    } else {

                        MySQL.execute("INSERT INTO `product` (`id`,`sub_category_id`,`brand_id`,`description`,`Quality_id`,`product_name`) "
                                + "VALUES('" + Productcode + "','" + SubCategoryMap.get(Sub_Category_name) + "','" + brandid + "','" + Description + "','" + jComboBox3.getSelectedIndex() + "','" + ProductName + "')");
                        JOptionPane.showMessageDialog(this, "Insert Success", "Success", JOptionPane.OK_OPTION);
                        Refresh();
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Empty product code ", "Please Enter Product Code", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", "Please Check Your Internet Connection and  Try again later", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // refresh
        Refresh();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // product Management
        ProductManagement pm = new ProductManagement();
        pm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        try {

            String brand_name = jTextField1.getText();

            ResultSet rs = MySQL.execute("SELECT * FROM `brand` WHERE `brand_name` LIKE '%" + brand_name + "%' ");
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("id"));
                v.add(rs.getString("brand_name"));
                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // Searech By Product ID
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `product` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id`  INNER JOIN `brand` ON `product`.`brand_id` = `brand`.`id` WHERE `product`.`id` LIKE '%" + jTextField2.getText() + "%' ");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("id"));
                v.add(rs.getString("sub_category"));
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("product_name"));

                dtm.addRow(v);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        LensStockAdding lenseAdding = new LensStockAdding();
        lenseAdding.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacDarkLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ProductAdding().setVisible(true);
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
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel timeField;
    private javax.swing.JLabel userNameField;
    // End of variables declaration//GEN-END:variables
}
