/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Resources;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme;
import javax.swing.table.DefaultTableModel;
import models.MySQL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author sanka
 */
public class smallProductAdding extends javax.swing.JFrame {

    /**
     * Creates new form smallProductAdding
     */
    public smallProductAdding() {
        FlatSolarizedLightIJTheme.setup();
        initComponents();
        setLocationRelativeTo(null);
        Refresh();
    }

    HashMap<String, Integer> brandMap = new HashMap<>();
    HashMap<String, Integer> CategoryMap = new HashMap<>();
    HashMap<String, Integer> SupplierMap = new HashMap<>();
    HashMap<String, Integer> SubCategoryMap = new HashMap<>();

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
            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", "Please Check Your Internet Connection or Please Try again later", JOptionPane.ERROR_MESSAGE);

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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

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
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane1.setViewportView(jTextArea3);

        jPanel6.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 260, 250, 180));

        jLabel20.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel20.setText("Product Name");
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 160, -1, -1));

        jLabel21.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel21.setText("Product Table");
        jPanel6.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 70, -1, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Police", "Blue Ray", "Polorioid" }));
        jPanel6.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 110, 250, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel22.setText("Quality");
        jPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Quality", "Branded", "Grade A", "Grade B" }));
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

        jPanel6.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 110, 360, 340));

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

        jPanel6.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 250, 100));

        jButton1.setText("Save Product");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 500, 250, 50));

        jButton2.setText("Close");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 500, 160, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1030, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged
        // TODO add your handling code here:
        if (jComboBox5.getSelectedIndex() == 1) { //Frames
            jTextField3.setText("");
            jTextField3.setEnabled(false);
            loadSubCategory("frames");
        } else if (jComboBox5.getSelectedIndex() == 2) { // Lenses
            jTextField3.setText("");
            jTextField3.setEnabled(true);
            loadSubCategory("lenses");

        } else if (jComboBox5.getSelectedIndex() == 3) { // Accessories
            jTextField3.setText("");
            jTextField3.setEnabled(true);
            loadSubCategory("Accessories");
        } else if (jComboBox5.getSelectedIndex() == 0) { // Accessories
            jTextField3.setText("");
            jTextField3.setEnabled(false);
            loadSubCategory("");
        }
    }//GEN-LAST:event_jComboBox5ItemStateChanged

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

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Adding Product Brand
        String brand = jTextField1.getText();
        if (brand.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Plese Enter a brand text");
        } else {
            MySQL.execute("INSERT INTO `brand` (`brand_name`) VALUES ('" + brand + "')");
            JOptionPane.showMessageDialog(this, "Brand Insert Success");
            Refresh();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
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

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(smallProductAdding.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(smallProductAdding.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(smallProductAdding.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(smallProductAdding.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new smallProductAdding().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
