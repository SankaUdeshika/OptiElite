/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import static gui.Login.logger;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.MySQL;
import models.Reports;
import models.UserDetails;

/**
 *
 * @author sanka
 */
public class DoctorChanneling extends javax.swing.JFrame {

    /**
     * Creates new form DoctorChanneling
     */
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    public DoctorChanneling() {
        initComponents();
        setSize(screen.width, screen.height);
        refresh();
    }

    public void clearFields() {
        jDateChooser1.setDate(null);
        jTextField1.setText("");
        jTextField7.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
    }

    public void refresh() {
        LoadChannelList();
        generete_refNo();
        CustomerLoadingTable();
        DoctorLoadingTable();
        loadLocations();
        calculatetotal();
        clearFields();
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
            jComboBox4.setModel(dfm);
            jComboBox5.setModel(dfm);

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", "Please Check Your Internet Connection or Please Try again later", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadChannelList() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `channeling_appoinment` INNER JOIN `customer` ON `customer`.`mobile` = `channeling_appoinment`.`customer_mobile` ");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("ref_no"));
                v.add(rs.getString("appoinment_id"));
                v.add(rs.getString("date"));
                v.add(rs.getString("Name"));
                if (rs.getInt("payment_status_id") == 2) {
                    v.add("Complete");
                } else {
                    v.add("Pending");
                }
                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calculatetotal() {
        double doctorFee = Double.parseDouble(jTextField6.getText());
        double channelingFee = Double.parseDouble(jTextField10.getText());

        double total = doctorFee + channelingFee;
        jLabel34.setText(String.valueOf(total));
        jLabel38.setText(String.valueOf(total));
    }

    public void generete_refNo() {
        try {
            ResultSet rs;

            do {
                Random random = new Random();
                int randomNumber1 = 100000 + random.nextInt(900000);
                rs = MySQL.execute("SELECT * FROM `channeling_appoinment` WHERE `ref_no` = '" + randomNumber1 + "' ");
                jTextField4.setText(String.valueOf(randomNumber1));
            } while (rs.next());

        } catch (Exception e) {

        }
    }

    public void CustomerLoadingTable() {
        try {
            jTextField1.setText("");
            ResultSet rs = MySQL.execute("SELECT * FROM `customer` INNER JOIN `location` ON `location`.`id` = `customer`.`location_id` ");
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("mobile"));
                v.add(rs.getString("Name"));

                dtm.addRow(v);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DoctorLoadingTable() {
        try {
            jTextField1.setText("");
            ResultSet rs = MySQL.execute("SELECT * FROM `doctor` ");
            DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("doc_id"));
                v.add(rs.getString("doc_name"));
//                v.add(rs.getString("birthday"));
//                v.add(rs.getString("mobile2"));
//                v.add(rs.getString("telephone_land"));
//                v.add(rs.getString("nic"));
//                v.add(rs.getString("location_name"));
//                v.add(rs.getString("register_date"));
//                v.add(rs.getString("email"));
                dtm.addRow(v);

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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        userNameField4 = new javax.swing.JLabel();
        dateField4 = new javax.swing.JLabel();
        timeField4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jButton8 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTextField6 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel34 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jTextField7 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        jComboBox4 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel15.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel15.setText("Operator : ");

        jLabel20.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel20.setText("Date       :");

        jLabel30.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel30.setText("Time       :");

        userNameField4.setText("user");

        dateField4.setText("date");

        timeField4.setText("time");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userNameField4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateField4))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(timeField4)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(userNameField4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(dateField4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(timeField4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        jLabel8.setText("Doctor Channeling");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel8))
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/home (1).png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/back.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/reload.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel9.setText("Actions");

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/diskette (1).png"))); // NOI18N
        jButton7.setText("Add Appoinment");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Customer Prescription.png"))); // NOI18N
        jButton8.setText("Appoinment Manager");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel10.setText("Pages");

        jPanel21.setBackground(new java.awt.Color(206, 206, 206));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jPanel21.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 83, 250, -1));
        jPanel21.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 540, 480, 10));

        jLabel23.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel23.setText("Data Serching");
        jPanel21.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));

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

        jPanel21.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 111, 250, 129));

        jTextField6.setText("2000");
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
        jPanel21.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 310, 110, -1));

        jLabel25.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel25.setText("Customer mobile Or Name");
        jPanel21.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 57, -1, -1));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel21.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 420, 20, 150));

        jLabel34.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jLabel34.setText("Rs.0.00");
        jPanel21.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 510, 110, -1));

        jLabel37.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel37.setText("Total Price");
        jPanel21.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 480, -1, -1));

        jLabel38.setFont(new java.awt.Font("Segoe UI Historic", 1, 24)); // NOI18N
        jLabel38.setText("Rs.0.00");
        jPanel21.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 500, -1, -1));

        jLabel40.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel40.setText("=");
        jPanel21.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 500, 20, -1));
        jPanel21.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 410, 640, 14));

        jLabel41.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel41.setText("Sub Total");
        jPanel21.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 470, 70, 30));

        jLabel42.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel42.setText("Total Amount ");
        jPanel21.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 440, -1, -1));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Doctor ID", "Doctor Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
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

        jPanel21.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 306, 250, 95));

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
        jPanel21.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 278, 250, -1));

        jLabel43.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel43.setText("Lense Id or Type");
        jPanel21.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 252, -1, -1));

        jButton12.setText("Add Doctor");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel21.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 360, 160, -1));

        jLabel3.setText("Reference Number");
        jPanel21.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, -1, -1));
        jPanel21.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 140, -1));

        jLabel4.setText("Location");
        jPanel21.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 290, -1, -1));
        jPanel21.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 150, 220, -1));

        jLabel5.setText("Time");
        jPanel21.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 180, -1, -1));

        jLabel6.setText("Appoinment Date");
        jPanel21.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 130, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60" }));
        jPanel21.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 200, -1, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        jPanel21.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 200, -1, -1));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AM", "PM" }));
        jPanel21.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 200, 60, -1));

        jLabel7.setText("Doctor Fee");
        jPanel21.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 290, -1, -1));

        jLabel11.setText("Channeling fee");
        jPanel21.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 340, -1, -1));

        jTextField10.setText("500");
        jTextField10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField10MouseClicked(evt);
            }
        });
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });
        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField10KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField10KeyReleased(evt);
            }
        });
        jPanel21.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 360, 110, -1));

        jButton13.setText("Add New Customer");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel21.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 200, 140, -1));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel21.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 310, 160, 30));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Ref_No", "Appo_No", "Date", "Patient Name", "payment Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jLabel1.setText("Ref_No");

        jLabel2.setText("Name");

        jLabel14.setText("Appoinment No");

        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });

        jButton3.setText("Search");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel16.setText("Date");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel13.setText("Location");

        jButton2.setText("Refresh");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel20Layout.createSequentialGroup()
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)))
                        .addGap(36, 36, 36))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, 724, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel20Layout.createSequentialGroup()
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel20Layout.createSequentialGroup()
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(90, 90, 90)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel20Layout.createSequentialGroup()
                                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(12, 12, 12)
                                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton1))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField5)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel20Layout.createSequentialGroup()
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton2))))))
                .addGap(26, 26, 26))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2))))
                        .addGap(19, 19, 19)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel16))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton3)
                                .addComponent(jButton1)))
                        .addGap(44, 44, 44)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton7)
                            .addGap(62, 62, 62)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel10)
                            .addGap(31, 31, 31)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        Dashboard d = new Dashboard();
        d.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
//        Add Apppoinment

        //validation
        if (jTextField4.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Reference Number", "Empty Refrence Number", JOptionPane.ERROR_MESSAGE);
        } else if (jTable2.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please Select a Customer", "Empty Customer", JOptionPane.ERROR_MESSAGE);
        } else if (jTable4.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please Select a Doctor", "Empty Doctor", JOptionPane.ERROR_MESSAGE);
        } else if (jDateChooser1.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please Enter a Appoinment Date", "Empty Date", JOptionPane.ERROR_MESSAGE);
        } else if (jComboBox2.getSelectedItem().toString() == "00" && jComboBox1.getSelectedItem().toString() == "00") {
            JOptionPane.showMessageDialog(this, "Please Enter a Appoinment Time", "Invalid Date", JOptionPane.ERROR_MESSAGE);
        } else if (jComboBox4.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please Enter a Locaiton", "Empty Locaiton", JOptionPane.ERROR_MESSAGE);
        } else if (jTextField6.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Doctor Fee", "Empty Doctor Fee", JOptionPane.ERROR_MESSAGE);
        } else if (jTextField10.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Channeling Fee", "Empty Channeling Fee", JOptionPane.ERROR_MESSAGE);
        } else {

            // get Date 
            Date getDate = jDateChooser1.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String AppoinmentDate = sdf.format(getDate);

            // get Time
            String hr = String.valueOf(jComboBox2.getSelectedItem());
            String min = String.valueOf(jComboBox1.getSelectedItem());

            String timeString = hr + ":" + min + ":00";
            Time AppoinmentTime = Time.valueOf(timeString); // java.sql.Time

            // Total
            String total = jLabel38.getText();

            // fees
            String doctorFee = jTextField6.getText();
            String ChannelFee = jTextField10.getText();

            // Doctor Details
            String doctor_id = String.valueOf(jTable4.getValueAt(jTable4.getSelectedRow(), 0));

            // Customer Details
            String customer_id = String.valueOf(jTable2.getValueAt(jTable2  .getSelectedRow(), 0));
            System.out.println("this is Selected ID" + customer_id);

            // User Details
            String userID = UserDetails.UserId;

            // Reference 
            String ref_no = jTextField4.getText();
            
            // location 
            int locationId = jComboBox4.getSelectedIndex();

//            try {
//                ResultSet insert_rs = MySQL.execute("INSERT INTO `channeling_appoinment` (`ref_no`,`date`, `time`, `total`, `doctor_fee`, `channeling_fee`, `Doctor_doc_id`, `customer_mobile`,  `users_id`,`payment_status_id`,`location_id`)"
//                        + " VALUES ( '" + ref_no + "','" + AppoinmentDate + "', '" + AppoinmentTime + "', '" + total + "', '" + doctorFee + "', '" + ChannelFee + "', '" + doctor_id + "', '" + customer_id + "', '" + userID + "','1','"+locationId+"')");
//                JOptionPane.showMessageDialog(this, "Apppoinment Adding Success", "Success", JOptionPane.OK_OPTION);
//
//                if (insert_rs.next()) {
//                    int AppoinmentID = insert_rs.getInt(1);
//                    ChannelingPaymentStatus channelingPaymentStatus = new ChannelingPaymentStatus(AppoinmentID);
//                    channelingPaymentStatus.setVisible(true);
//                    refresh();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }


    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        OrderManagement om = new OrderManagement();
        om.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

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

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked

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
        calculatetotal();
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked

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

    }//GEN-LAST:event_jTextField7KeyReleased

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // Doctor 
        AddDoctor addDoctor = new AddDoctor(this);
        addDoctor.setVisible(true);

    }//GEN-LAST:event_jButton12ActionPerformed

    private void jTextField10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField10MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10MouseClicked

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void jTextField10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10KeyPressed

    private void jTextField10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyReleased
        calculatetotal();
    }//GEN-LAST:event_jTextField10KeyReleased

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        CustomerRegisterWindow customerRwindow = new CustomerRegisterWindow(this);
        customerRwindow.setVisible(true);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        // Seardch Cannneling Details form the Appoinment iD
        String appoinmentID = jTextField5.getText();
        if (jTextField5.getText().isEmpty()) {
            LoadChannelList();
        } else {
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `channeling_appoinment` INNER JOIN `customer` ON `customer`.`mobile` = `channeling_appoinment`.`customer_mobile` WHERE `appoinment_id` LIKE '%" + appoinmentID + "%' ");
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);

                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("ref_no"));
                    v.add(rs.getString("appoinment_id"));
                    v.add(rs.getString("date"));
                    v.add(rs.getString("Name"));
                    if (rs.getInt("payment_status_id") == 2) {
                        v.add("Complete");
                    } else {
                        v.add("Pending");
                    }

                    dtm.addRow(v);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Pirnt Chennel Appoinemnt
        if (jTable1.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please Select a Channling Raw", "Empty Selection", JOptionPane.ERROR_MESSAGE);
        } else {
            int appoinmentID = Integer.parseInt(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 1)));
            Reports.PrintEchanneling(appoinmentID);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        refresh();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // Seardch Cannneling Details form the Ref NO 
        String ref_no = jTextField3.getText();

        if (jTextField3.getText().isEmpty()) {
            LoadChannelList();
        } else {
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `channeling_appoinment` INNER JOIN `customer` ON `customer`.`mobile` = `channeling_appoinment`.`customer_mobile` WHERE `ref_no` LIKE '%" + ref_no + "%' ");
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);

                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("ref_no"));
                    v.add(rs.getString("appoinment_id"));
                    v.add(rs.getString("date"));
                    v.add(rs.getString("Name"));
                    if (rs.getInt("payment_status_id") == 2) {
                        v.add("Complete");
                    } else {
                        v.add("Pending");
                    }
                    dtm.addRow(v);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // Seardch Cannneling Details form the Patient Name
        String name = jTextField2.getText();
        if (jTextField2.getText().isEmpty()) {
            LoadChannelList();
        } else {
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `channeling_appoinment` INNER JOIN `customer` ON `customer`.`mobile` = `channeling_appoinment`.`customer_mobile` WHERE `name` LIKE '%" + name + "%' ");
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);

                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("ref_no"));
                    v.add(rs.getString("appoinment_id"));
                    v.add(rs.getString("date"));
                    v.add(rs.getString("Name"));
                    if (rs.getInt("payment_status_id") == 2) {
                        v.add("Complete");
                    } else {
                        v.add("Pending");
                    }

                    dtm.addRow(v);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Search with location and Date.
        // get variable.

        int selectedIndex = jComboBox5.getSelectedIndex();
        String selectedItem = String.valueOf(jComboBox5.getSelectedItem());

        Date chooseDate;
        String querry = "SELECT * FROM `channeling_appoinment` INNER JOIN `customer` ON `customer`.`mobile` = `channeling_appoinment`.`customer_mobile`";

        try {
            chooseDate = jDateChooser2.getDate();

        } catch (Exception e) {
            chooseDate = null;
        }

        if (selectedIndex == 0) {
            if (chooseDate != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date_String = simpleDateFormat.format(chooseDate);
                querry += " WHERE `date` = '" + date_String + "'";
            }
        } else {
            querry += " WHERE `channeling_appoinment`.`location_id` = '" + selectedIndex + "'";
            if (chooseDate != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date_String = simpleDateFormat.format(chooseDate);
                querry += " AND `date` = '" + date_String + "'";
            }
        }

        try {
            ResultSet rs = MySQL.execute(querry);
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("ref_no"));
                v.add(rs.getString("appoinment_id"));
                v.add(rs.getString("date"));
                v.add(rs.getString("Name"));
                if (rs.getInt("payment_status_id") == 2) {
                    v.add("Complete");
                } else {
                    v.add("Pending");
                }

                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton3ActionPerformed
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       FlatMacLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DoctorChanneling().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel dateField4;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JLabel timeField4;
    private javax.swing.JLabel userNameField4;
    // End of variables declaration//GEN-END:variables
}
