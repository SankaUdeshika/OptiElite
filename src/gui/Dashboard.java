package gui;

import gui.StockDashboard;
import models.Navs;
import models.UserDetails;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import gui.CustomerManagement;
import gui.CustomerRegister;
import gui.Login;
import static gui.Login.logger;
import gui.PrescripitonAdding;
import gui.ProductAdding;
import gui.SellAccessories;
import gui.adminDashboard;
import gui.lensOrder;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.Timer;
import models.MySQL;

public class Dashboard extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Navs GoObject;

    public Dashboard() {
        initComponents();
        loadNotificaiton();
        operater();
        time();
        setSize(screen.width, screen.height);

        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `users` WHERE `id` = '" + UserDetails.UserId + "' ");
            if (rs.next()) {
                int userType_id = rs.getInt("user_type_id");
                if (userType_id == 1) {
                    adminBtn.setEnabled(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Data failed to load", e);

        }
    }

    public void loadNotificaiton() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `notificaiton`");
            if (rs.next()) {
                String notification = rs.getString("text");
                jTextPane1.setText(notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            timeField.setText(time);
            dateField.setText(day);
        };
        Timer timer = new Timer(1000, timerListener);
        timer.setInitialDelay(0);
        timer.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        adminBtn = new javax.swing.JButton();
        customerBtn = new javax.swing.JButton();
        productBtn = new javax.swing.JButton();
        orderBtn = new javax.swing.JButton();
        exitBtn = new javax.swing.JButton();
        prescriptionsBtn = new javax.swing.JButton();
        signOutBtn = new javax.swing.JButton();
        accessoriesBtn = new javax.swing.JButton();
        stockBtn = new javax.swing.JButton();
        lensOrderBtn = new javax.swing.JToggleButton();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        userNameField = new javax.swing.JLabel();
        dateField = new javax.swing.JLabel();
        timeField = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel11.setBackground(new java.awt.Color(51, 51, 51));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel14.setText("Main Categories");
        jPanel11.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        adminBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Admin.png"))); // NOI18N
        adminBtn.setText("Admin");
        adminBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminBtnActionPerformed(evt);
            }
        });
        jPanel11.add(adminBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 160, 210, 64));

        customerBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/customer.png"))); // NOI18N
        customerBtn.setText("Customer");
        customerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerBtnActionPerformed(evt);
            }
        });
        jPanel11.add(customerBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 210, 64));

        productBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/product.png"))); // NOI18N
        productBtn.setText("Products");
        productBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productBtnActionPerformed(evt);
            }
        });
        jPanel11.add(productBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 210, 64));

        orderBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Orders.png"))); // NOI18N
        orderBtn.setText("Orders");
        orderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderBtnActionPerformed(evt);
            }
        });
        jPanel11.add(orderBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 210, 64));

        exitBtn.setText("Exit");
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBtnActionPerformed(evt);
            }
        });
        jPanel11.add(exitBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 450, 70, -1));

        prescriptionsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/prescription.png"))); // NOI18N
        prescriptionsBtn.setText("Prescriptions ");
        prescriptionsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prescriptionsBtnActionPerformed(evt);
            }
        });
        jPanel11.add(prescriptionsBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 210, 64));

        signOutBtn.setText("Sign out");
        signOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signOutBtnActionPerformed(evt);
            }
        });
        jPanel11.add(signOutBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 450, 170, -1));

        accessoriesBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/eye-dropper.png"))); // NOI18N
        accessoriesBtn.setText("Sell Accessories");
        accessoriesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accessoriesBtnActionPerformed(evt);
            }
        });
        jPanel11.add(accessoriesBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 210, 64));

        stockBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/product.png"))); // NOI18N
        stockBtn.setText("Stock");
        stockBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockBtnActionPerformed(evt);
            }
        });
        jPanel11.add(stockBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, 210, 64));

        lensOrderBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/lens.png"))); // NOI18N
        lensOrderBtn.setText("Only Lens Order");
        lensOrderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lensOrderBtnActionPerformed(evt);
            }
        });
        jPanel11.add(lensOrderBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 240, 210, 70));

        jButton2.setText("Company Purchases");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 320, 210, 60));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Notificaiton");

        jTextPane1.setBackground(new java.awt.Color(51, 51, 51));
        jTextPane1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jTextPane1.setForeground(new java.awt.Color(255, 51, 0));
        jScrollPane1.setViewportView(jTextPane1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jButton1.setText("GO");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 769, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jLabel10.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel10.setText("Operator : ");

        jLabel11.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel11.setText("Date       :");

        jLabel12.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel12.setText("Time       :");

        userNameField.setText("user");

        dateField.setText("date");

        timeField.setText("time");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateField))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(timeField)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(userNameField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(dateField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(timeField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        titleLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        titleLabel.setText("Dashboard");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 624, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(titleLabel)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(0, 18, Short.MAX_VALUE)
                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 17, Short.MAX_VALUE))
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void adminBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminBtnActionPerformed
        // product and Stocks
        adminDashboard pa = new adminDashboard();
        pa.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_adminBtnActionPerformed

    private void customerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerBtnActionPerformed
        // Customer adding
        CustomerRegister cr = new CustomerRegister();
        cr.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_customerBtnActionPerformed

    private void productBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productBtnActionPerformed
        ProductAdding pa = new ProductAdding();
        pa.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_productBtnActionPerformed

    private void orderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderBtnActionPerformed
        OrderManagement om = new OrderManagement();
        om.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_orderBtnActionPerformed

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitBtnActionPerformed

    private void prescriptionsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prescriptionsBtnActionPerformed
        PrescripitonAdding pa = new PrescripitonAdding();
        pa.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_prescriptionsBtnActionPerformed

    private void signOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signOutBtnActionPerformed
        Login l = new Login();
        l.setVisible(true);
        this.dispose();

        UserDetails.UserId = "";
        UserDetails.UserLocation_id = "";
        UserDetails.UserName = "";
    }//GEN-LAST:event_signOutBtnActionPerformed

    private void accessoriesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accessoriesBtnActionPerformed
        SellAccessories sl = new SellAccessories();
        sl.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_accessoriesBtnActionPerformed

    private void stockBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockBtnActionPerformed
        StockDashboard sd = new StockDashboard();
        sd.setVisible(true);
    }//GEN-LAST:event_stockBtnActionPerformed

    private void lensOrderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lensOrderBtnActionPerformed
        lensOrder lo = new lensOrder();
        lo.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_lensOrderBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Navs.routee = true;
        CustomerManagement cm = new CustomerManagement();
        cm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacDarkLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton accessoriesBtn;
    private javax.swing.JButton adminBtn;
    private javax.swing.JButton customerBtn;
    private javax.swing.JLabel dateField;
    private javax.swing.JButton exitBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JToggleButton lensOrderBtn;
    private javax.swing.JButton orderBtn;
    private javax.swing.JButton prescriptionsBtn;
    private javax.swing.JButton productBtn;
    private javax.swing.JButton signOutBtn;
    private javax.swing.JButton stockBtn;
    private javax.swing.JLabel timeField;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel userNameField;
    // End of variables declaration//GEN-END:variables
}
