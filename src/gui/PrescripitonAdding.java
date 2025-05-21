package gui;

import models.UserDetails;
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
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import models.MySQL;
import models.Navs;

public class PrescripitonAdding extends javax.swing.JFrame {

    /**
     * Creates new form PrescripitonAdding
     */
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    public PrescripitonAdding() {
        initComponents();
        setSize(screen.width, screen.height);
        if (!Navs.routee) {
            jButton1.setVisible(false);
            jButton2.setVisible(false);
        }

        if (Navs.routee) {
            jButton4.setVisible(false);
        }

        refresh();
        time();
        operater();
    }

    public PrescripitonAdding(String CustomerID) {
        initComponents();
        setSize(screen.width, screen.height);

        refresh(CustomerID);
        customerIdField.setText(CustomerID);
        customerIdField.setEditable(false);
        customerIdField.setEnabled(false);
        if (!Navs.routee) {
            jButton1.setVisible(false);
            jButton2.setVisible(false);
        }

        if (Navs.routee) {
            jButton4.setVisible(false);
        }
        time();
        operater();
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

    public void refresh() {
        jLabel14.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        loadCustomerTable("");
        customerIdField.setEnabled(true);
        customerIdField.setText("");
        jTextField3.setText("");
        jTextField6.setText("");
        jTextField3.setEnabled(true);
        jTextField6.setEnabled(true);
        jLabel17.setText(UserDetails.UserId);
        jLabel19.setText(UserDetails.UserName);
//        Go Navi Verification
        if (Navs.routee) {
            jButton1.setVisible(true);
            jButton2.setVisible(true);
        } else {
            jButton1.setVisible(false);
            jButton2.setVisible(false);
        }
    }

    public void refresh(String CustomerMobile) {
        jLabel14.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        loadCustomerTable(CustomerMobile);
        customerIdField.setEnabled(true);
        customerIdField.setText("");
        jTextField3.setText("");
        jTextField6.setText("");
        jTextField3.setEnabled(true);
        jTextField6.setEnabled(true);
        jLabel17.setText(UserDetails.UserId);
        jLabel19.setText(UserDetails.UserName);
        jTable1.setEnabled(false);
        jTextField3.setEnabled(false);
        jTextField6.setEnabled(false);
    }

    public void loadCustomerTable(String Command) {
        if (Command.equals("")) {
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `customer`");
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
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
        } else if (!Command.equals("")) {

            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `customer` WHERE `mobile` = '" + Command + "'");
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);

                boolean hasData = false;

                while (rs.next()) {
                    hasData = true; // At least one row exists

                    Vector v = new Vector();
                    v.add(rs.getString("mobile"));

                    v.add(rs.getString("name"));
                    v.add(rs.getString("nic"));
                    dtm.addRow(v);

                }

                if (!hasData) {
                    JOptionPane.showMessageDialog(this, "No customer found!", "Info", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (SQLException se) {
                se.printStackTrace();
                JOptionPane.showMessageDialog(this, "Please Check Your Internet Connection", "Connection Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Something Went Wrong. Please Try Again Later", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
        homeBtn = new javax.swing.JButton();
        backBtn = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        customerIdField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField3 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel25 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jTextField17 = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        jTextField19 = new javax.swing.JTextField();
        jTextField21 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jTextField27 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jTextField28 = new javax.swing.JTextField();
        jTextField29 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jTextField37 = new javax.swing.JTextField();
        jTextField36 = new javax.swing.JTextField();
        jTextField35 = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jTextField32 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jTextField38 = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jTextField39 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        jLabel5.setText("Prescription Adding");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 559, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel5))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28))
        );

        homeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/home (1).png"))); // NOI18N
        homeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeBtnActionPerformed(evt);
            }
        });

        backBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/back.png"))); // NOI18N
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
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
        jButton4.setText("Add Prescription");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel7.setText("Pages");

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel8.setText("Customer Mobile");
        jPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, -1, -1));

        customerIdField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerIdFieldActionPerformed(evt);
            }
        });
        customerIdField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customerIdFieldKeyReleased(evt);
            }
        });
        jPanel6.add(customerIdField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 252, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel9.setText("Customer Name");
        jPanel6.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 50, -1, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel12.setText("Prescription  Details");
        jPanel6.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 420, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mobile", "Name", "Nic"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
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
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
        }

        jPanel6.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 500, 100));

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 252, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel14.setText("2024/06/12");
        jPanel6.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 140, -1, -1));
        jPanel6.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 760, 10));

        jLabel15.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel15.setText("Customer Infomations");
        jPanel6.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });
        jPanel6.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 80, 252, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel10.setText("Observation Date");
        jPanel6.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 120, -1, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel11.setText("Customer Nic");
        jPanel6.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 50, -1, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel17.setText("1");
        jPanel6.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 140, 20, -1));

        jLabel18.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel18.setText("Operator Name");
        jPanel6.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 120, -1, -1));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 80, -1));
        jPanel1.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 90, 80, -1));
        jPanel1.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 90, 80, -1));
        jPanel1.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 90, 80, -1));
        jPanel1.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 90, 80, -1));
        jPanel1.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 650, 10));

        jLabel25.setFont(new java.awt.Font("Segoe UI Historic", 0, 24)); // NOI18N
        jLabel25.setText("Left Eye");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI Historic", 0, 24)); // NOI18N
        jLabel13.setText("Right Eye");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel26.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel26.setText("Va");
        jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 70, 30));

        jTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField12ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 80, -1));

        jLabel27.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel27.setText("PH");
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, 70, 30));
        jPanel1.add(jTextField13, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 50, 80, -1));
        jPanel1.add(jTextField14, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 50, 80, -1));

        jLabel28.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel28.setText("Hub");
        jPanel1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, 80, 30));

        jLabel29.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel29.setText("IOL");
        jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, 70, 30));
        jPanel1.add(jTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 50, 80, -1));
        jPanel1.add(jTextField16, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 50, 80, -1));

        jLabel30.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel30.setText("Sub");
        jPanel1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 20, 80, 30));

        jPanel6.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, 760, 120));

        jLabel20.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel20.setText("Assessment Details");
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, -1, -1));
        jPanel6.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 770, 10));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField17ActionPerformed(evt);
            }
        });
        jTextField17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField17KeyReleased(evt);
            }
        });
        jPanel5.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 80, -1));
        jPanel5.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 750, 10));

        jTextField19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField19ActionPerformed(evt);
            }
        });
        jPanel5.add(jTextField19, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, 80, -1));

        jTextField21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField21ActionPerformed(evt);
            }
        });
        jPanel5.add(jTextField21, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 80, -1));
        jPanel5.add(jTextField23, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 80, 80, -1));

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel38.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel38.setText("Destance");
        jPanel7.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));

        jTextField24.setText("0101");
        jTextField24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField24ActionPerformed(evt);
            }
        });
        jPanel7.add(jTextField24, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 80, -1));

        jTextField25.setText("0101");
        jPanel7.add(jTextField25, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 30, 80, -1));

        jLabel39.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel39.setText("N_PD");
        jPanel7.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, -1, -1));
        jPanel7.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 680, 10));

        jLabel40.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel40.setText("Left Eye");
        jPanel7.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel41.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel41.setText("Addtion");
        jPanel7.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, -1, -1));

        jTextField26.setText("0101");
        jPanel7.add(jTextField26, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 80, -1));

        jLabel42.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel42.setText("VA");
        jPanel7.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, -1));

        jTextField27.setText("0101");
        jTextField27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField27ActionPerformed(evt);
            }
        });
        jPanel7.add(jTextField27, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 80, -1));

        jLabel43.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel43.setText("M_PD");
        jPanel7.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, -1, -1));

        jTextField28.setText("0101");
        jPanel7.add(jTextField28, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, 80, -1));

        jTextField29.setText("0101");
        jPanel7.add(jTextField29, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 30, 80, -1));

        jLabel44.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel44.setText("Cyl.");
        jPanel7.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 10, -1, -1));

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, 810, 100));

        jLabel53.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel53.setText("Left Eye");
        jPanel5.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, -1, -1));

        jLabel31.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel31.setText("Right Eye");
        jPanel5.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, -1, -1));

        jLabel52.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel52.setText("SPH");
        jPanel5.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, -1));

        jTextField37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField37ActionPerformed(evt);
            }
        });
        jPanel5.add(jTextField37, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 80, -1));

        jTextField36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField36ActionPerformed(evt);
            }
        });
        jTextField36.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField36KeyReleased(evt);
            }
        });
        jPanel5.add(jTextField36, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 80, -1));
        jPanel5.add(jTextField35, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 40, 80, -1));

        jLabel48.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel48.setText("PD");
        jPanel5.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, -1, -1));

        jTextField32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField32ActionPerformed(evt);
            }
        });
        jPanel5.add(jTextField32, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 40, 80, -1));

        jLabel47.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel47.setText("Cyl.");
        jPanel5.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, -1, -1));

        jLabel46.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel46.setText("Axis.");
        jPanel5.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, -1, -1));

        jLabel51.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel51.setText("Addtion");
        jPanel5.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, -1, -1));

        jLabel50.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel50.setText("DVA");
        jPanel5.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, -1, -1));

        jLabel49.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel49.setText("NVA");
        jPanel5.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, -1, -1));
        jPanel5.add(jTextField38, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 40, 80, -1));

        jLabel54.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel54.setText("Height");
        jPanel5.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, -1, -1));
        jPanel5.add(jTextField39, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 80, 80, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "6/6", "6/9", "6/12", "6/18", "6/18", "6/24", "6/36", "6/60" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel5.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 40, 80, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "6/6", "6/9", "6/12", "6/18", "6/18", "6/24", "6/36", "6/60" }));
        jPanel5.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 80, 80, -1));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N.6", "N.8", "N.10", "N.12", "N.18", "N.36" }));
        jPanel5.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 40, 80, -1));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N.6", "N.8", "N.10", "N.12", "N.18", "N.36" }));
        jPanel5.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 80, -1));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "+0.75", "+1.00", "+1.25", "+1.50", "+1.75", "+2.00", "+2.25", "+2.50", "+2.75", "+3.00", "+3.25", "+3.50" }));
        jPanel5.add(jComboBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 40, 80, -1));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "+0.75", "+1.00", "+1.25", "+1.50", "+1.75", "+2.00", "+2.25", "+2.50", "+2.75", "+3.00", "+3.25", "+3.50", " " }));
        jPanel5.add(jComboBox6, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 80, 80, -1));

        jPanel6.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 420, -1, 130));

        jLabel19.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel19.setText("1");
        jPanel6.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 140, 140, -1));

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/glasses.png"))); // NOI18N
        jButton9.setText("Prescription Management");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton1.setText("Save & Next");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton2.setText("Next");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(homeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1082, 1082, 1082))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator4)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(139, 139, 139))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1030, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(homeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 20, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_backBtnActionPerformed

    private void jTextField17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField17ActionPerformed
        // Minus CYl
        String CYL = jTextField17.getText();

        if (CYL.startsWith("-")) {
            System.out.println("no Minus CYL");
        } else {
            jTextField17.setText("-" + CYL);
        }
    }//GEN-LAST:event_jTextField17ActionPerformed

    private void jTextField21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField21ActionPerformed

    private void jTextField24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField24ActionPerformed

    private void jTextField27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField27ActionPerformed

    private void jTextField36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField36ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField36ActionPerformed

    private void jTextField37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField37ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField37ActionPerformed

    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtnActionPerformed
        // TODO add your handling code here:
        Dashboard d = new Dashboard();
        d.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_homeBtnActionPerformed

    private void customerIdFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customerIdFieldKeyReleased
        // sEARCH CUSTOMER BY HIS ID
        try {

            String Customer_id = customerIdField.getText();

            jTextField3.setText("");
            jTextField6.setText("");

            ResultSet rs = MySQL.execute("SELECT * FROM `customer` WHERE `mobile` LIKE '%" + Customer_id + "%'  ");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
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
    }//GEN-LAST:event_customerIdFieldKeyReleased

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // SEARCH CUSTOMER BY HIS NAME
        try {

            customerIdField.setText("");
            String customer_name = jTextField3.getText();
            jTextField6.setText("");

            ResultSet rs = MySQL.execute("SELECT * FROM `customer` WHERE `name` LIKE '%" + customer_name + "%'  ");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
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
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        // SEARCH CUSTOMER BY HIS NIC
        try {

            customerIdField.setText("");
            jTextField3.setText("");
            String customer_nic = jTextField6.getText();

            ResultSet rs = MySQL.execute("SELECT * FROM `customer` WHERE `nic` LIKE '%" + customer_nic + "%'  ");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
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
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // CUSTOMER TABLE SELECTED
        if (evt.getClickCount() == 2) {
            int selectedRow = jTable1.getSelectedRow();
            String id = String.valueOf(jTable1.getValueAt(selectedRow, 0));
            customerIdField.setText(id);
            customerIdField.setEnabled(false);

            String name = String.valueOf(jTable1.getValueAt(selectedRow, 1));
            jTextField3.setText(name);
            jTextField3.setEnabled(false);

            String nic = String.valueOf(jTable1.getValueAt(selectedRow, 2));
            jTextField6.setText(nic);
            jTextField6.setEnabled(false);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        // REFRESH BUTTON
        refresh();
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // INSERT PRESCRIPTION
//        validate Assessment Details

        String Customer_mobile = customerIdField.getText();
        String datechooser = jLabel14.getText();
        String user_id = jLabel17.getText();

        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `customer` WHERE `mobile` = '" + Customer_mobile + "' ");

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Please Select Valid Customer form Table", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else if (Customer_mobile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Select Customer from Customer table", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String R_VA = jTextField12.getText();
                String R_PH = jTextField13.getText();
                String R_HUB = jTextField14.getText();
                String R_IOL = jTextField15.getText();
                String R_Sub = jTextField16.getText();

                String L_VA = jTextField7.getText();
                String L_PH = jTextField8.getText();
                String L_HUB = jTextField9.getText();
                String L_IOL = jTextField10.getText();
                String L_Sub = jTextField11.getText();

//        prescription Details
                String R_SPH = jTextField37.getText();
                String R_Cyl = jTextField36.getText();
                String R_Axis = jTextField35.getText();
                String PR_DVa = String.valueOf(jComboBox1.getSelectedItem());
                String R_NVA = String.valueOf(jComboBox3.getSelectedItem());
                String R_M_PD = jTextField32.getText();
                String R_Addiiton = String.valueOf(jComboBox5.getSelectedItem());
                String R_Height = jTextField38.getText();

                String L_SPH = jTextField21.getText();
                String L_Cyl = jTextField17.getText();
                String L_Axis = jTextField19.getText();
                String PL_DVa = String.valueOf(jComboBox2.getSelectedItem());
                String L_NVA = String.valueOf(jComboBox4.getSelectedItem());
                String L_M_PD = jTextField23.getText();
                String L_Addiiton = String.valueOf(jComboBox6.getSelectedItem());
                String L_Height = jTextField39.getText();

                MySQL.execute("INSERT INTO `prescription_details` (`L_SPH`,`L_Addition`,`L_DVA`,`L_NVA`,`L_M_PD`,`L_HEIGHT`,`customer_mobile`,`users_id`,"
                        + "`R_DVA`,`R_NVA`,`R_M_PD`,`R_SPH`,`R_Addition`,`prescripiton_date`,`L_CYL`,`R_CYL`,`L_Axis`,`R_Axis`,`R_HEIGHT`) "
                        + "VALUES ('" + L_SPH + "','" + L_Addiiton + "','" + PL_DVa + "','" + L_NVA + "','" + L_M_PD + "','" + L_Height + "','" + Customer_mobile + "','" + user_id + "','" + PR_DVa + "','" + R_NVA + "','" + R_M_PD + "','" + R_SPH + "','" + R_Addiiton + "','" + datechooser + "','" + L_Cyl + "','" + R_Cyl + "','" + L_Axis + "','" + R_Axis + "','" + R_Height + "')  ");

                JOptionPane.showMessageDialog(this, "Prescription Adding Success", "Insert Success", JOptionPane.WARNING_MESSAGE);

            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Conneciton", "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "ERROR", JOptionPane.ERROR_MESSAGE);

        }


    }//GEN-LAST:event_jButton4ActionPerformed

    private void customerIdFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerIdFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerIdFieldActionPerformed

    private void jTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField12ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField32ActionPerformed

    private void jTextField19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField19ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // Order management
        PrescriptionManager om = new PrescriptionManager();
        om.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // INSERT PRESCRIPTION
//        validate Assessment Details

        String Customer_mobile = customerIdField.getText();
        String datechooser = jLabel14.getText();
        String user_id = jLabel17.getText();

        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `customer` WHERE `mobile` = '" + Customer_mobile + "' ");

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Please Select Valid Customer form Table", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else if (Customer_mobile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Select Customer from Customer table", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String R_VA = jTextField12.getText();
                String R_PH = jTextField13.getText();
                String R_HUB = jTextField14.getText();
                String R_IOL = jTextField15.getText();
                String R_Sub = jTextField16.getText();

                String L_VA = jTextField7.getText();
                String L_PH = jTextField8.getText();
                String L_HUB = jTextField9.getText();
                String L_IOL = jTextField10.getText();
                String L_Sub = jTextField11.getText();

//        prescription Details
                String R_SPH = jTextField37.getText();
                String R_Cyl = jTextField36.getText();
                String R_Axis = jTextField35.getText();
                String PR_DVa = String.valueOf(jComboBox1.getSelectedItem());
                String R_NVA = String.valueOf(jComboBox3.getSelectedItem());
                String R_M_PD = jTextField32.getText();
                String R_Addiiton = String.valueOf(jComboBox5.getSelectedItem());
                String R_Height = jTextField38.getText();

                String L_SPH = jTextField21.getText();
                String L_Cyl = jTextField17.getText();
                String L_Axis = jTextField19.getText();
                String PL_DVa = String.valueOf(jComboBox2.getSelectedItem());
                String L_NVA = String.valueOf(jComboBox4.getSelectedItem());
                String L_M_PD = jTextField23.getText();
                String L_Addiiton = String.valueOf(jComboBox6.getSelectedItem());
                String L_Height = jTextField39.getText();

                MySQL.execute("INSERT INTO `prescription_details` (`L_SPH`,`L_Addition`,`L_DVA`,`L_NVA`,`L_M_PD`,`L_HEIGHT`,`customer_mobile`,`users_id`,"
                        + "`R_DVA`,`R_NVA`,`R_M_PD`,`R_SPH`,`R_Addition`,`prescripiton_date`,`L_CYL`,`R_CYL`,`L_Axis`,`R_Axis`,`R_HEIGHT`) "
                        + "VALUES ('" + L_SPH + "','" + L_Addiiton + "','" + PL_DVa + "','" + L_NVA + "','" + L_M_PD + "','" + L_Height + "','" + Customer_mobile + "','" + user_id + "','" + PR_DVa + "','" + R_NVA + "','" + R_M_PD + "','" + R_SPH + "','" + R_Addiiton + "','" + datechooser + "','" + L_Cyl + "','" + R_Cyl + "','" + L_Axis + "','" + R_Axis + "','" + R_Height + "')  ");
                JOptionPane.showMessageDialog(this, "Prescription Adding Success", "Insert Success", JOptionPane.WARNING_MESSAGE);

                OrderMaking chooseOrderTypes = new OrderMaking(Customer_mobile);
                chooseOrderTypes.setVisible(true);
                this.dispose();

            }

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Conneciton", "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "ERROR", JOptionPane.ERROR_MESSAGE);

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // without saving

        OrderMaking chooseOrderTypes = new OrderMaking(customerIdField.getText().toString());
        chooseOrderTypes.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField36KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField36KeyReleased
        // minus added

        String CYL = jTextField36.getText();

        if (CYL.startsWith("-")) {
            System.out.println("yess");
        } else {
            System.out.println("NO");
            jTextField36.setText("-" + CYL);
        }


    }//GEN-LAST:event_jTextField36KeyReleased

    private void jTextField17KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField17KeyReleased
        // TODO add your handling code here:
        String CYL = jTextField17.getText();

        if (CYL.startsWith("-")) {
            System.out.println("yess");
        } else {
            System.out.println("NO");
            jTextField17.setText("-" + CYL);
        }
    }//GEN-LAST:event_jTextField17KeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacDarkLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrescripitonAdding().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JTextField customerIdField;
    private javax.swing.JLabel dateField;
    private javax.swing.JButton homeBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
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
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JLabel timeField;
    private javax.swing.JLabel userNameField;
    // End of variables declaration//GEN-END:variables
}
