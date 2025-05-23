package gui;

import models.Navs;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import models.UserDetails;
import java.awt.Color;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.sql.ResultSet;  
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import models.MySQL;

public class Login extends javax.swing.JFrame {

    public static Logger logger = Logger.getLogger("egaleEye");

    public Login() {
        initComponents();
        setLocationRelativeTo(null);
        loadLocations();
        Navs.routee = false;
    }

    public void loadLocations() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `location` ORDER BY `id` ASC");
            Vector v = new Vector();
            v.add("Select Locaiton");
            while (rs.next()) {
                v.add(String.valueOf(rs.getString("id") + ") " + rs.getString("location_name")));
            }
            DefaultComboBoxModel dfm = new DefaultComboBoxModel<>(v);
            locationComboBox.setModel(dfm);
        } catch (SQLException se) {
            se.printStackTrace();
            logger.log(Level.WARNING, "Please Check Your Internet Connection or Please Try again later", se);
            JOptionPane.showMessageDialog(this, "Error", "Please Check Your Internet Connection or Please Try again later", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Please Check Your Internet Connection or Please Try again later", e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        username_lable = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        password_lable = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        location_lable = new javax.swing.JLabel();
        locationComboBox = new javax.swing.JComboBox<>();
        signInBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel6.setBackground(new java.awt.Color(28, 28, 28));

        jPanel1.setBackground(new java.awt.Color(28, 28, 28));

        username_lable.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        username_lable.setText("Username");

        password_lable.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        password_lable.setText("Password");

        location_lable.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        location_lable.setText("Location");

        locationComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        signInBtn.setText("Sign In");
        signInBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signInBtnActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel2.setText("Sign In");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(signInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(location_lable)
                        .addComponent(password_lable)
                        .addComponent(username_lable)
                        .addComponent(usernameField)
                        .addComponent(passwordField)
                        .addComponent(locationComboBox, 0, 242, Short.MAX_VALUE)))
                .addGap(61, 61, 61))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel2)
                .addGap(28, 28, 28)
                .addComponent(username_lable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(password_lable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(location_lable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(signInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/background.jpg"))); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 533, 657));

        jButton1.setText("Exit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/optieliteIcon.png"))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(120, 120, 120))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void signInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signInBtnActionPerformed
        // Log In Button

        if (locationComboBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please Select a Location", "Invalid Location", JOptionPane.ERROR_MESSAGE);
        } else {
            if (!usernameField.getText().isEmpty()) {
                if (passwordField.getPassword().length != 0) {

                    String Password = String.valueOf(passwordField.getPassword());
                    String UserName = usernameField.getText();

                    try {
                        ResultSet rs = MySQL.execute("SELECT * FROM `users` "
                                + "INNER JOIN `user_type` ON `user_type`.`id` = `users`.`user_type_id` "
                                + "INNER JOIN `user_status` ON `user_status`.`status_id` = `users`.`user_status_status_id`  "
                                + "WHERE `username` = '" + UserName + "' AND `password` = '" + Password + "' AND `status_id` = '1' ");
                        if (rs.next()) {
                            MySQL.execute(" UPDATE `users` SET `location_id` = '" + locationComboBox.getSelectedIndex() + "'  WHERE `username` = '" + UserName + "' ");
                            String ResultFirstname = rs.getString("fname");
                            String ResultLastname = rs.getString("lname");
                            String id = rs.getString("id");
                            String locaiton_id = String.valueOf(locationComboBox.getSelectedIndex());

                            UserDetails ud = new UserDetails(ResultFirstname, ResultLastname, id, locaiton_id);
                            System.out.println(locaiton_id);
                            logger.info("user has logged succesfully");
//                            Rederect Tempory Jframe. (because still No Dashboard in project)
                            Dashboard cr = new Dashboard();
                            cr.setVisible(true);
                            this.dispose();

                        } else {
                            JOptionPane.showMessageDialog(this, "Your Login Details are invalid, please check and try again", "Invalid User Details", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "An error occurred while trying to log in", e);
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Please Enter Password", "Empty Password", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Enter UserName", "Empty UserName", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_signInBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Color myWhite = new Color(250, 250, 250);
        Color myBlack = new Color(9, 9, 11);
//        FlatMacDarkLaf.setup();
        FlatDarkLaf.registerCustomDefaultsSource("resources.theme");
        FlatDarkLaf.setup();

        UIManager.put("PasswordField.foreground", myWhite);
        UIManager.put("PasswordField.background", myBlack);

        UIManager.put("Button.arc", 8);
        UIManager.put("Button.background", myWhite);
        UIManager.put("Button.foreground", myBlack);
        UIManager.put("background", myBlack);

        UIManager.put("Button", "borderColor: #09090B; background: #1c1c1c; foreground: #fff");

        try {
            FileHandler fileHandler = new FileHandler("logs.txt", true);
            logger.addHandler(fileHandler);

            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            fileHandler.setEncoding("UTF-8");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred while setting up FileHandler", e);
            System.out.println(e);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JComboBox<String> locationComboBox;
    private javax.swing.JLabel location_lable;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel password_lable;
    private javax.swing.JButton signInBtn;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel username_lable;
    // End of variables declaration//GEN-END:variables
}
