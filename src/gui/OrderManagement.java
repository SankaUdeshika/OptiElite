package gui;

//import GUI.DIALOG.CompletePayment;
import gui.CompletePayments;
import gui.Printsouts;
import models.Reports;
import models.UserDetails;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import static gui.Login.logger;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import models.MySQL;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class OrderManagement extends javax.swing.JFrame {

    int actualProfit = 0;
    int estimateProfit = 0;
    double ReportTotal = 0;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    public OrderManagement() {
        initComponents();
        setSize(screen.width, screen.height);

        operater();
        time();
        Refresh();
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

    public void Refresh() {
        LoadOrderTable();
        loadLocations();
        jTextField1.setText("");
        jTextField3.setText("");
        jComboBox1.setSelectedIndex(0);
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        completeBtn.setEnabled(false);
        billBtn.setEnabled(false);
        LoadDeleteButton();
    }

    public void LoadDeleteButton() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `users` WHERE `id` =  '" + UserDetails.UserId + "'");
            if (rs.next()) {
                String userID = rs.getString("user_type_id");
                System.out.println("User id is = " + userID);

                if (userID.equals("1")) {
                    jToggleButton1.setVisible(true);
                } else {
                    jToggleButton1.setVisible(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            jComboBox1.setModel(dfm);

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", "Please Check Your Internet Connection or Please Try again later", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadOrderTable() {
        estimateProfit = 0;
        actualProfit = 0;
        ReportTotal = 0;
        try {
            ResultSet rs = MySQL.execute("SELECT DISTINCT "
                    + "`subtotal`,`invoice`.`invoice_id`,`name`,`nic`,`date`,`location_name`,`advance_payment`,`discount`,`total_price`,`status_name`, `payment_status`.`status_name`, `payment_status_id` "
                    + "FROM `invoice` "
                    + "INNER JOIN `customer` ON `customer`.`mobile` = `invoice`.`customer_mobile` "
                    + "LEFT JOIN `invoice_item` ON `invoice_item`.`invoice_id` = `invoice`.`invoice_id` "
                    + "LEFT JOIN `stock` ON `stock`.`id` = `invoice_item`.`stock_id` "
                    + "INNER JOIN `location` ON `location`.`id` = `invoice_location` "
                    + "INNER JOIN `payment_status` ON `invoice`.`payment_status_id` = `payment_status`.`id` "
                    + "ORDER BY `date` DESC "
            );
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("invoice_id"));
                v.add(rs.getString("name"));
                v.add(rs.getString("date"));
                v.add(rs.getString("location_name"));
                v.add(rs.getString("status_name"));
                v.add(rs.getDouble("discount"));
                v.add(rs.getDouble("advance_payment"));
                v.add(rs.getDouble("total_price"));
                v.add(rs.getDouble("subtotal"));
                double payedAmount = rs.getDouble("total_price") - rs.getDouble("subtotal");
                v.add(payedAmount);
                estimateProfit += rs.getDouble("subtotal");

                if (rs.getString("payment_status_id").equals("2")) {
                    actualProfit += rs.getDouble("subtotal");
                    ReportTotal++;
                } else {
                    actualProfit += rs.getDouble("subtotal") - rs.getDouble("total_price");
//                    advanceTotal += rs.getDouble("advance_payment");

                    ReportTotal++;
                }
                dtm.addRow(v);
            }

            esProfitCountLable.setText("Rs." + String.valueOf(estimateProfit));
            acCountLable.setText("Rs." + String.valueOf(actualProfit));
            jLabel10.setText("Rs." + String.valueOf(ReportTotal));

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Conneciton", "Connection Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "Connection Error", se);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "load table error", e);

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        homeBtn = new javax.swing.JButton();
        previousBtn = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jButton6 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField3 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        searchBtn = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        billBtn = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        esProfitCountLable = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        esProfitLable = new javax.swing.JLabel();
        acLable = new javax.swing.JLabel();
        acCountLable = new javax.swing.JLabel();
        fullReportBtn = new javax.swing.JButton();
        completeBtn = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        userNameField = new javax.swing.JLabel();
        dateField = new javax.swing.JLabel();
        timeField = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        homeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/home (1).png"))); // NOI18N
        homeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeBtnActionPerformed(evt);
            }
        });

        previousBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/back.png"))); // NOI18N
        previousBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousBtnActionPerformed(evt);
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

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Customer management.png"))); // NOI18N
        jButton6.setText("Make Order");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel7.setText("Pages");

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));

        jLabel8.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel8.setText("Locaiton");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel9.setText("From Date");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Inovice id", "Customer Name", "Date", "Location", "Status", "Discount", "Advancement ", "Due Payment", "Sub Total", "Payed"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
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

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel13.setText("Order Table");

        searchBtn.setText("Search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel10.setText(".......................");

        jLabel16.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel16.setText("Quick Search");

        jLabel11.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel11.setText("Invoice ID");

        jLabel14.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel14.setText("To Date");

        billBtn.setText("View Bill");
        billBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                billBtnActionPerformed(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel15.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel15.setText("Order Control");

        esProfitCountLable.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        esProfitCountLable.setText("..................................");

        jLabel18.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel18.setText("Customer Name/mobile");

        jLabel19.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel19.setText("Order Count");

        jLabel20.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel20.setText("From Date");

        jLabel21.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel21.setText(".......................");

        jLabel22.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel22.setText(".......................");

        jLabel23.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel23.setText("To Date");

        jLabel24.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel24.setText("Report Details");

        esProfitLable.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        esProfitLable.setText("Total Sale");

        acLable.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        acLable.setText("Cash Collection");

        acCountLable.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        acCountLable.setText("..................................");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(billBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(210, 210, 210)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(jLabel19)
                                                .addGap(12, 12, 12)
                                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                                        .addComponent(jLabel23)
                                                        .addGap(42, 42, 42)
                                                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                                        .addComponent(jLabel20)
                                                        .addGap(25, 25, 25)
                                                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(114, 114, 114)
                                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                                        .addComponent(esProfitLable)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(esProfitCountLable, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                                        .addComponent(acLable)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(acCountLable, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                            .addComponent(jLabel16)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(69, 69, 69)
                                        .addComponent(jLabel18)
                                        .addGap(24, 24, 24)
                                        .addComponent(jLabel8)))
                                .addGap(38, 38, 38)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel14)))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 1006, Short.MAX_VALUE)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 968, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1))))
                .addContainerGap(163, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel16)
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel18)
                    .addComponent(jLabel8)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(jLabel9)))
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1)
                    .addComponent(jTextField3)
                    .addComponent(jComboBox1)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(5, 5, 5)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(25, 25, 25)
                                .addComponent(billBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addGap(15, 15, 15)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel10))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel22))))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(esProfitLable)
                            .addComponent(esProfitCountLable))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(acCountLable)
                            .addComponent(acLable))))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        fullReportBtn.setText("Finance Report");
        fullReportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullReportBtnActionPerformed(evt);
            }
        });

        completeBtn.setText("Complete Payment");
        completeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                completeBtnActionPerformed(evt);
            }
        });

        jToggleButton1.setForeground(new java.awt.Color(204, 0, 0));
        jToggleButton1.setText("Delete Order");
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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel6))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addComponent(fullReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(homeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(previousBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel7))
                            .addComponent(completeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(177, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(previousBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(homeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(fullReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(completeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel12.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel12.setText("Operator : ");

        jLabel26.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel26.setText("Date       :");

        jLabel27.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel27.setText("Time       :");

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
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateField))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(timeField)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(userNameField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(dateField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(timeField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        jLabel5.setText("Order Management");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 837, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel5)))
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1600, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 823, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void previousBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_previousBtnActionPerformed

    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtnActionPerformed
        // TODO add your handling code here:
        Dashboard d = new Dashboard();
        d.setVisible(true);
        this.dispose();

    }//GEN-LAST:event_homeBtnActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        // Advance Search
        estimateProfit = 0;
        actualProfit = 0;
        ReportTotal = 0;
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("YYYY-MM-dd");

        String ToDate;
        String FromDate;

        try {
            ToDate = simpleDateformat.format(jDateChooser1.getDate());
            jLabel22.setText(ToDate);
        } catch (NullPointerException ne) {
            ToDate = "null";
        }

        try {
            FromDate = simpleDateformat.format(jDateChooser2.getDate());
            jLabel21.setText(ToDate);
        } catch (NullPointerException ne) {
            FromDate = "null";
        }

        try {
            String Queary = "SELECT * FROM `invoice` "
                    + "INNER JOIN `customer` ON `customer`.`mobile` = `invoice`.`customer_mobile`   "
                    + "LEFT JOIN `invoice_item` ON `invoice_item`.`invoice_id` = `invoice`.`invoice_id` "
                    + "LEFT JOIN `stock` ON `stock`.`id` = `invoice_item`.`stock_id` "
                    + "INNER JOIN `location` ON `location`.`id` = `invoice_location` "
                    + "INNER JOIN `payment_status` ON `invoice`.`payment_status_id` = `payment_status`.`id` ";

//        Enter Parameter
            if (!jTextField1.getText().isEmpty()) {
                Queary += " WHERE `invoice`.`invoice_id` = '" + jTextField1.getText() + "' ";

                if (!jTextField3.getText().isEmpty()) {
                    ResultSet rs = MySQL.execute("SELECT * FROM `customer` WHERE `mobile` = '" + jTextField3.getText() + "' OR `name` = '" + jTextField3.getText() + "'");
                    if (rs.next()) {
                        String customer_id = rs.getString("mobile");

                        Queary += " AND `invoice`.`customer_mobile` = '" + customer_id + "' ";
                    }
                }

                if (jComboBox1.getSelectedIndex() != 0) {
                    Queary += " AND `customer`.`location_id` = '" + jComboBox1.getSelectedIndex() + "' ";
                }

                if (ToDate != "null" && FromDate != "null") {
                    Queary += " AND `date` BETWEEN '" + ToDate + "' AND '" + FromDate + "' ";
                } else if (ToDate != "null") {
                    Queary += " AND `invoice`.`date` >= '" + ToDate + "' ";
                } else if (FromDate != "null") {
                    Queary += " AND `invoice`.`date` <= '" + FromDate + "' ";
                }

            } else if (!jTextField3.getText().isEmpty()) {
                if (!jTextField3.getText().isEmpty()) {
                    ResultSet rs = MySQL.execute("SELECT * FROM `customer` WHERE `mobile` LIKE '%" + jTextField3.getText() + "%' OR `name` LIKE '%" + jTextField3.getText() + "%' ");
                    if (rs.next()) {
                        String customer_id = rs.getString("mobile");
                        Queary += " WHERE `invoice`.`customer_mobile` = '" + customer_id + "' ";
                    }
                    if (jComboBox1.getSelectedIndex() != 0) {
                        Queary += " AND `customer`.`location_id` = '" + jComboBox1.getSelectedIndex() + "' ";
                    }

                    if (ToDate != "null" && FromDate != "null") {
                        Queary += " AND `date` BETWEEN '" + ToDate + "' AND '" + FromDate + "' ";
                    } else if (ToDate != "null") {
                        Queary += " AND `invoice`.`date` = '" + ToDate + "' ";
                    } else if (FromDate != "null") {
                        Queary += " AND `invoice`.`date` = '" + FromDate + "' ";
                    }
                }
            } else if (jComboBox1.getSelectedIndex() != 0) {
                Queary += " WHERE `invoice_location` = '" + jComboBox1.getSelectedIndex() + "' ";

                if (ToDate != "null" && FromDate != "null") {
                    Queary += " AND `date` BETWEEN '" + ToDate + "' AND '" + FromDate + "' ";
                } else if (ToDate != "null") {
                    Queary += " AND `invoice`.`date` = '" + ToDate + "' ";
                } else if (FromDate != "null") {
                    Queary += " AND `invoice`.`date` = '" + FromDate + "' ";
                }
            } else if (ToDate != "null" && FromDate != "null") {
                Queary += " WHERE `date` BETWEEN '" + ToDate + "' AND '" + FromDate + "' ";
            } else if (ToDate != "null") {
                Queary += " WHERE `invoice`.`date` = '" + ToDate + "' ";
            } else if (FromDate != "null") {
                Queary += " WHERE `invoice`.`date` = '" + FromDate + "' ";
            }

            ResultSet rs = MySQL.execute(Queary);
            System.out.println(Queary);
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("invoice_id"));
                v.add(rs.getString("name"));
                v.add(rs.getString("date"));
                v.add(rs.getString("location_name"));
                v.add(rs.getString("status_name"));
                v.add(rs.getDouble("discount"));
                v.add(rs.getDouble("advance_payment"));
                v.add(rs.getDouble("total_price"));
                v.add(rs.getDouble("subtotal"));

                double payedAmount = rs.getDouble("subtotal") - rs.getDouble("total_price");
                v.add(payedAmount);

                estimateProfit += rs.getDouble("subtotal");

                if (rs.getString("payment_status_id").equals("2")) {
                    actualProfit += rs.getDouble("subtotal");
                    ReportTotal++;
                } else {
                    actualProfit += rs.getDouble("subtotal") - rs.getDouble("total_price");
//                    advanceTotal += rs.getDouble("advance_payment");

                    ReportTotal++;
                }

                dtm.addRow(v);
            }
            esProfitCountLable.setText(String.valueOf(estimateProfit));
            acCountLable.setText(String.valueOf(actualProfit));
            jLabel10.setText(String.valueOf(ReportTotal));

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please Check Your Internet Conneciton", "Connection Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something Wrong Please Try again Later", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_searchBtnActionPerformed

    private void billBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_billBtnActionPerformed
        // View Report
        int selectedRow = jTable1.getSelectedRow();
        String Ivoice_id = (String) jTable1.getValueAt(selectedRow, 0);
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `invoice_item` INNER JOIN `invoice` ON `invoice`.`invoice_id` = `invoice_item`.`invoice_id`  WHERE `invoice`.`invoice_id` = '" + Ivoice_id + "'");
            int count = 0;
            boolean lens = false;
            while (rs.next()) {
                count = count + 1;
            }
            if (count == 0) {
                lens = true;
            }

            if (count == 1 || lens) {
                Printsouts p = new Printsouts(invoiceID);
                p.setVisible(true);
//                Reports.OrderPurchaceInvoice(Ivoice_id);
            } else {
                Reports.WithoutPrescriptionOrderPurchaceInvoice(Ivoice_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_billBtnActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        Refresh();
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private int invoiceID;

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // View Bill
        if (evt.getClickCount() == 2) {
            billBtn.setEnabled(true);

            String status = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 5));

            if (!status.equals("complete")) {
                completeBtn.setEnabled(true);
                String id = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0));
                invoiceID = Integer.parseInt(id);
            } else {
                completeBtn.setEnabled(false);
            }

        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Make Order
        OrderMaking orderMaking = new OrderMaking();
        orderMaking.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void completeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_completeBtnActionPerformed
//        CompletePayment dialog = new GUI.DIALOG.CompletePayment(this, true, invoiceID);
//        dialog.setVisible(true);
//
//        dialog.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosed(WindowEvent e) {
//                Refresh();
//            }
//        });

        CompletePayments completePayments = new CompletePayments(this, rootPaneCheckingEnabled, invoiceID);
        completePayments.setVisible(true);
        completePayments.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Refresh();
            }
        });


    }//GEN-LAST:event_completeBtnActionPerformed

    private void fullReportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullReportBtnActionPerformed
        try {
            String ToDate;
            String FromDate;
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("YYYY-MM-dd");

            try {
                ToDate = simpleDateformat.format(jDateChooser1.getDate());
                jLabel22.setText(ToDate);
            } catch (NullPointerException ne) {
                ToDate = "null";
            }

            try {
                FromDate = simpleDateformat.format(jDateChooser2.getDate());
                jLabel21.setText(ToDate);
            } catch (NullPointerException ne) {
                FromDate = "null";
            }

            String Queary = "SELECT "
                    + "invoice.invoice_id, "
                    + "invoice.payment_status_id, "
                    + "invoice.total_price, "
                    + "invoice.advance_payment, "
                    + "invoice.subtotal, "
                    + "invoice.lenstotal, "
                    + "stock.cost, "
                    + "stock.saling_price, "
                    + "invoice_item.qty, "
                    + "invoice.date FROM invoice "
                    + "INNER JOIN invoice_item ON invoice_item.invoice_id = invoice.invoice_id "
                    + "INNER JOIN stock ON invoice_item.stock_id = stock.id ";

            if (ToDate != "null" && FromDate != "null") {
                Queary += "WHERE invoice.date BETWEEN '" + ToDate + "' AND '" + FromDate + "' ";
            } else if (ToDate != "null") {
                Queary += " WHERE invoice.date >= '" + ToDate + "' ";
            } else if (FromDate != "null") {
                Queary += " WHERE invoice.date <= '" + FromDate + "' ";
            }

            ResultSet rs = MySQL.execute(Queary);

            DecimalFormat df = new DecimalFormat("0.00");

            double totalIncome = 0;
            double estimatePayment = 0;
            double advancement = 0;
            double completePayment = 0;
            double costOfGoods = 0;
            double profit = 0;

            while (rs.next()) {
                double ep = Double.parseDouble(rs.getString("subTotal"));
                estimatePayment = +ep;
                costOfGoods = +rs.getInt("cost");
                if (rs.getInt("payment_status_id") == 1) {//not paid
                    advancement = +rs.getInt("advance_payment");
                } else {
                    completePayment = +Double.parseDouble(rs.getString("subTotal"));
                }

            }

            totalIncome = advancement + completePayment;
            profit = totalIncome - costOfGoods;

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("from_date", String.valueOf(FromDate));
            parameters.put("to_date", String.valueOf(ToDate));

            parameters.put("total_income", String.valueOf(df.format(totalIncome)));

            parameters.put("estimate_payment", String.valueOf(df.format(estimatePayment)));
            parameters.put("advancement", String.valueOf(df.format(advancement)));
            parameters.put("complete_payment", String.valueOf(df.format(completePayment)));
            parameters.put("cost_of_goods", String.valueOf(df.format(costOfGoods)));
            parameters.put("profit", String.valueOf(df.format(profit)));

            try {
                JREmptyDataSource eds = new JREmptyDataSource();

                JasperPrint jasperPrint = JasperFillManager.fillReport(getClass().getResourceAsStream("/reports/EagleEyeIncome.jasper"), parameters, eds);
                JasperViewer.viewReport(jasperPrint, false);
            } catch (Exception e) {
                logger.log(Level.WARNING, OrderManagement.class.getName(), e);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_fullReportBtnActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // Delete Order

        if (jTable1.getSelectedRow() != -1) {

            int selectROw = jTable1.getSelectedRow();
            String invoiceId = String.valueOf(jTable1.getValueAt(selectROw, 0));
            System.out.println(invoiceId);

//            Delete From Advance Payemnts
            try {
                MySQL.execute("DELETE FROM `advance_payment_history` WHERE `invoice_invoice_id`  ='" + invoiceId + "' ");
            } catch (Exception e) {
            }
//            Delete From Invoice Item
            try {
                MySQL.execute("DELETE FROM `invoice_item` WHERE `invoice_id`  ='" + invoiceId + "' ");
            } catch (Exception e) {
                System.out.println("Invoice ItemF Exception");
            }
//            Delete From Invoice 
            try {
                MySQL.execute("DELETE FROM `invoice` WHERE `invoice_id`  ='" + invoiceId + "' ");
            } catch (Exception e) {
                System.out.println("Invoice  Exception");
            }

            Refresh();

        } else {
            JOptionPane.showMessageDialog(this, "please Select a Order row", "Empty Row", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacDarkLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OrderManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel acCountLable;
    private javax.swing.JLabel acLable;
    private javax.swing.JButton billBtn;
    private javax.swing.JButton completeBtn;
    private javax.swing.JLabel dateField;
    private javax.swing.JLabel esProfitCountLable;
    private javax.swing.JLabel esProfitLable;
    private javax.swing.JButton fullReportBtn;
    private javax.swing.JButton homeBtn;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JButton previousBtn;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JButton searchBtn;
    private javax.swing.JLabel timeField;
    private javax.swing.JLabel userNameField;
    // End of variables declaration//GEN-END:variables

}
