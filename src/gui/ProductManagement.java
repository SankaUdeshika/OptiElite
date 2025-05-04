package gui;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import models.MySQL;
import models.UserDetails;

public class ProductManagement extends javax.swing.JFrame {

    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    public ProductManagement() {
        initComponents();
        setSize(screen.width, screen.height);
        Refresh();
        operater();
        time();
    }

    public void Refresh() {
        LoadStockTable();
        LoadBrand();
        LoadSubCategory();
        LoadCategories();
    }
    
        
    private void operater(){
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

    public void LoadCategories() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `category` ORDER BY `id` ASC");
            Vector v = new Vector();

            v.add("Select Category");
            while (rs.next()) {
                v.add(String.valueOf(rs.getString("id") + ") " + rs.getString("Category")));
            }

            DefaultComboBoxModel dfm = new DefaultComboBoxModel<>(v);
            jComboBox1.setModel(dfm);

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", "Please Check Your Internet Connection or Please Try again later", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadBrand() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `brand` ORDER BY `id` ASC");
            Vector v = new Vector();

            v.add("Select Brand");
            while (rs.next()) {
                v.add(String.valueOf(rs.getString("id") + ") " + rs.getString("brand_name")));
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

    public void LoadSubCategory() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `sub_category` ORDER BY `id` ASC");
            Vector v = new Vector();

            v.add("Select Brand");
            while (rs.next()) {
                v.add(String.valueOf(rs.getString("id") + ") " + rs.getString("sub_category")));
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

    public void LoadStockTable() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `product`  INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` ");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("id"));
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("sub_category"));
                v.add(rs.getString("product_name"));
                v.add(rs.getDouble("whole_sale"));

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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        userNameField = new javax.swing.JLabel();
        dateField = new javax.swing.JLabel();
        timeField = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jTextField2 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel20 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton8 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        jLabel5.setText("Product Management");

        jLabel10.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel10.setText("Operator : ");

        jLabel14.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel14.setText("Date       :");

        jLabel22.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel22.setText("Time       :");

        userNameField.setText("user");

        dateField.setText("date");

        timeField.setText("time");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateField))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(timeField)))
                .addGap(59, 59, 59))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(userNameField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(dateField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(timeField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 601, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel5)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
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

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/upload.png"))); // NOI18N
        jButton5.setText("Update Stock");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel7.setText("Pages");

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel6.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 110, -1));
        jPanel6.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 900, 10));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Product_id", "Brand", "Sub Category", "Product Name", "Whole_Sale"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel6.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 1070, 250));

        jLabel13.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel13.setText("Stock Table");
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 160, -1));

        jButton9.setText("Search");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 90, 90, 20));
        jPanel6.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 440, 940, 10));

        jLabel16.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel16.setText("Quick Search");
        jPanel6.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel11.setText("Product id");
        jPanel6.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, -1, -1));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel6.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 440, 20, 140));

        jLabel15.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel15.setText("Update Control");
        jPanel6.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 450, -1, -1));

        jLabel18.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel18.setText("Category");
        jPanel6.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 60, -1, -1));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Brand" }));
        jPanel6.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 510, 150, -1));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Sub Category" }));
        jPanel6.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 510, 150, -1));
        jPanel6.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 510, 140, -1));
        jPanel6.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 510, 140, -1));

        jLabel12.setText("Brand");
        jPanel6.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 490, -1, -1));

        jLabel17.setText("Whole Sale Price");
        jPanel6.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 490, -1, -1));

        jLabel19.setText("Sub Category");
        jPanel6.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 490, -1, -1));

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel6.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 440, 20, 140));

        jLabel20.setText("Product Name");
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 490, -1, -1));

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jPanel6.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, 280, -1));

        jLabel21.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel21.setText("Brand Name");
        jPanel6.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel6.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, 170, -1));

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/glasses.png"))); // NOI18N
        jButton8.setText("Product Adding");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)))
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(162, 162, 162)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(70, 70, 70))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        // Back
        ProductAdding pa = new ProductAdding();
        pa.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Refresh();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
//        Select Stock Table
        int selectedRow = jTable1.getSelectedRow();
        if (evt.getClickCount() == 2) {

//            Get brand
            String Brand_name = (String) jTable1.getValueAt(selectedRow, 1);
            int brand_id = 0;
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `brand` WHERE `brand_name` = '" + Brand_name + "' ");
                if (rs.next()) {
                    brand_id = rs.getInt("id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            jComboBox3.setSelectedIndex(brand_id);
            //            Get Sub Category
            String Sub_Category_name = (String) jTable1.getValueAt(selectedRow, 2);
            int sub_category_id = 0;
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `sub_category` WHERE `sub_category` = '" + Sub_Category_name + "' ");
                if (rs.next()) {
                    sub_category_id = rs.getInt("id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            jComboBox4.setSelectedIndex(sub_category_id);
//            Get Whole Sale price
            jTextField4.setText(String.valueOf(jTable1.getValueAt(selectedRow, 4)));
//            Get product name
            jTextField2.setText(String.valueOf(jTable1.getValueAt(selectedRow, 3)));
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // Advance Search

        try {

            String Queary = "SELECT * FROM `product`  INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` INNER JOIN `sub_category` ON `sub_category`.`id` = `product`.`sub_category_id` INNER JOIN `category` ON `sub_category`.`category_id` = `category`.`id` ";
            //        Enter Parameter
            if (!jTextField1.getText().isEmpty()) {
                Queary += " WHERE `product`.`id` = '" + jTextField1.getText() + "' ";

            } else if (!jTextField5.getText().isEmpty()) {
                Queary += " WHERE `brand`.`brand_name` LIKE '%" + jTextField5.getText() + "%' ";

                if (jComboBox1.getSelectedIndex() != 0) {
                    Queary += " AND `category`.`id` = '" + jComboBox1.getSelectedIndex() + "' ";
                }

            } else if (jComboBox1.getSelectedIndex() != 0) {
                Queary += " WHERE `category`.`id` = '" + jComboBox1.getSelectedIndex() + "' ";
            }

//             
            //                Execute Query
            System.out.println(Queary);

            ResultSet rs = MySQL.execute(Queary);
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("id"));
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("sub_category"));
                v.add(rs.getString("product_name"));
                v.add(rs.getDouble("whole_sale"));

                dtm.addRow(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Update Stock
        int selectedRow = jTable1.getSelectedRow();
        System.out.println(selectedRow);

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please Select a Stock Row", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
//            Update Data

            String WholeSalePrice = jTextField4.getText();
            String Product_name = jTextField2.getText();

            int brand_id = jComboBox3.getSelectedIndex();
            int sub_category_id = jComboBox4.getSelectedIndex();

            if (WholeSalePrice.equals("")) {
                JOptionPane.showMessageDialog(this, "Please Enter Price", "Empty Price", JOptionPane.ERROR_MESSAGE);
            } else {
                MySQL.execute("UPDATE `product` set  `whole_sale` = '" + WholeSalePrice + "' ,`product_name` = '" + Product_name + "' , `brand_id` = '" + brand_id + "' , `sub_category_id` = '" + sub_category_id + "' WHERE `id` = '" + jTable1.getValueAt(selectedRow, 0) + "' ");
                Refresh();
                JOptionPane.showMessageDialog(this, "Upodate Success", "Success", JOptionPane.ERROR_MESSAGE);
            }

        }


    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // product Management
        ProductAdding pm = new ProductAdding();
        pm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacDarkLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ProductManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dateField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JLabel timeField;
    private javax.swing.JLabel userNameField;
    // End of variables declaration//GEN-END:variables
}
