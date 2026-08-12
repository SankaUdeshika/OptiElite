package gui;

import models.UserDetails;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import models.MySQL;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import models.SMS;
import models.SMSCalculator;
import models.SMSInfo;
import models.SmsProfileInfo;

public class BroadCastMessageSystem extends javax.swing.JFrame {

    /**
     * Creates new form BroadCastMessageSystem
     */
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    public BroadCastMessageSystem() {
        initComponents();
        setSize(screen.width, screen.height);
        operater();
        time();
        refresh();
    }

    public void refresh() {
        getSmsProfile();
        LoadBoradCastUserTabel("Everyone");
    }

    public void getSmsProfile() {
        SmsProfileInfo profileInfo = SMS.getBalance();
        RemainingSMSUnits.setText(profileInfo.getRemaining_balance());
        SmsExpireOn.setText(profileInfo.getExpired_on_date());
    }

    // Helper class to make the image transferable
    static class ImageSelection implements Transferable {

        private final Image image;

        public ImageSelection(Image image) {
            this.image = image;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!DataFlavor.imageFlavor.equals(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return image;
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
            String dayArray[] = day.split(" ");
            String year_string = dayArray[0];
            String month_string = dayArray[1];
            String day_string = dayArray[2];

            String DateString = day_string + " of " + month_string + " " + year_string;
            timeField.setText(time);
            dateField.setText(DateString);
        };

        Timer timer = new Timer(1000, timerListener);
        timer.setInitialDelay(0);
        timer.start();
    }

    public void OpenWhatsapp() throws AWTException {
        // Testing
        String applicationNAme = "Whatsapp";

//        Coppy to text board
        StringSelection stringSelection = new StringSelection(applicationNAme);

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);

        Robot robot = new Robot();
//            Press Windows
        robot.keyPress(KeyEvent.VK_WINDOWS);
        robot.keyRelease(KeyEvent.VK_WINDOWS);

//            past Whatsapp text
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
//      press Enter
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void MessageSendingProcess(String PhoneNumber, String Message) {
        try {
            String SubsTringNumber = PhoneNumber.substring(1);
            StringSelection stringSelection = new StringSelection(SubsTringNumber);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);

            Robot r1 = new Robot();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

//            Click New Chat
            r1.keyPress(KeyEvent.VK_CONTROL);
            r1.keyPress(KeyEvent.VK_N);

            r1.keyRelease(KeyEvent.VK_CONTROL);
            r1.keyRelease(KeyEvent.VK_N);

//            Clear currunt text
            r1.keyPress(KeyEvent.VK_CONTROL);
            r1.keyPress(KeyEvent.VK_A);
            r1.keyRelease(KeyEvent.VK_CONTROL);
            r1.keyRelease(KeyEvent.VK_A);
            r1.keyPress(KeyEvent.VK_BACK_SPACE);

            Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
            int HalfWidthSize = screensize.width / 3;
            int HalfHeightSize = screensize.height / 3;

            r1.mouseMove(HalfWidthSize, HalfHeightSize);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

//            Past number
            r1.keyPress(KeyEvent.VK_CONTROL);
            r1.keyPress(KeyEvent.VK_V);
            r1.keyRelease(KeyEvent.VK_CONTROL);
            r1.keyRelease(KeyEvent.VK_V);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
            //            click Mouse Button
            r1.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            r1.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
            //            move the cursor into send button
            Dimension screensize1 = Toolkit.getDefaultToolkit().getScreenSize();
            int HalfWidthSize1 = screensize1.width / 3;
            int HalfHeightSize2 = screensize1.height / 3 - 48; // move the Mouse Cursor into Chat list
            r1.mouseMove(HalfWidthSize1, HalfHeightSize2);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

//            Cick the Selected Chat Box
            r1.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
            r1.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

            StringSelection stringSelectionMessage = new StringSelection(Message);
            Clipboard clipboardMessage = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboardMessage.setContents(stringSelectionMessage, null);
//            past Message
            r1.keyPress(KeyEvent.VK_CONTROL);
            r1.keyPress(KeyEvent.VK_V);
            r1.keyRelease(KeyEvent.VK_CONTROL);
            r1.keyRelease(KeyEvent.VK_V);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

            //            enter
            r1.keyPress(KeyEvent.VK_ENTER);
            r1.keyRelease(KeyEvent.VK_ENTER);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (AWTException ex) {
            Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SendImagesProcess(String PhoneNumber, BufferedImage image) {
        try {
            String SubsTringNumber = PhoneNumber.substring(1);
            StringSelection stringSelection = new StringSelection(SubsTringNumber);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);

            Robot r1 = new Robot();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

//            Click New Chat
            r1.keyPress(KeyEvent.VK_CONTROL);
            r1.keyPress(KeyEvent.VK_N);

            r1.keyRelease(KeyEvent.VK_CONTROL);
            r1.keyRelease(KeyEvent.VK_N);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

//            Clear currunt text
            r1.keyPress(KeyEvent.VK_CONTROL);
            r1.keyPress(KeyEvent.VK_A);
            r1.keyRelease(KeyEvent.VK_CONTROL);
            r1.keyRelease(KeyEvent.VK_A);
            r1.keyPress(KeyEvent.VK_BACK_SPACE);

            Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
            int HalfWidthSize = screensize.width / 3;
            int HalfHeightSize = screensize.height / 3;

            r1.mouseMove(HalfWidthSize, HalfHeightSize);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

            //            Past number
            r1.keyPress(KeyEvent.VK_CONTROL);
            r1.keyPress(KeyEvent.VK_V);
            r1.keyRelease(KeyEvent.VK_CONTROL);
            r1.keyRelease(KeyEvent.VK_V);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

            //            click Mouse Button
            r1.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            r1.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

            //            move the cursor into send button
//            Dimension screensize1 = Toolkit.getDefaultToolkit().getScreenSize();
//            int HalfWidthSize1 = screensize1.width / 3;
//            int HalfHeightSize2 = screensize1.height / 3 - 48; // move the Mouse Cursor into Chat list
//            r1.mouseMove(HalfWidthSize1, HalfHeightSize2);
//
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            Cick the Selected Chat Box
//            r1.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
//            r1.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
//            }
            ImageSelection imgSel = new ImageSelection(image);
            Clipboard clipboardMessage = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboardMessage.setContents(imgSel, null);

//            past Message
            r1.keyPress(KeyEvent.VK_CONTROL);
            r1.keyPress(KeyEvent.VK_V);
            r1.keyRelease(KeyEvent.VK_CONTROL);
            r1.keyRelease(KeyEvent.VK_V);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

            //            enter
            r1.keyPress(KeyEvent.VK_ENTER);
            r1.keyRelease(KeyEvent.VK_ENTER);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (AWTException ex) {
            Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeWhatsapp() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_F4);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_F4);

    }

    public void LoadBoradCastUserTabel(String Type) {
        if (Type == "Everyone") {
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `customer`  ");
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);

                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("mobile"));
                    v.add(rs.getString("name"));
                    v.add(rs.getString("birthday"));
                    v.add(rs.getString("mobile2"));
                    v.add(rs.getString("telephone_land"));
                    v.add(rs.getString("nic"));
                    v.add(rs.getString("register_date"));

                    dtm.addRow(v);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Type == "Only Womens") {
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `customer` INNER JOIN `gender` ON `gender`.`gender_id` = `customer`.`gender_gender_id` INNER JOIN `location` ON `location`.`id` = `customer`.`location_id` WHERE `gender_id` = '2' ");
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);

                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("mobile"));
                    v.add(rs.getString("name"));
                    v.add(rs.getString("birthday"));
                    v.add(rs.getString("mobile2"));
                    v.add(rs.getString("telephone_land"));
                    v.add(rs.getString("nic"));
                    v.add(rs.getString("register_date"));

                    dtm.addRow(v);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Type == "Only Mens") {
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `customer` INNER JOIN `gender` ON `gender`.`gender_id` = `customer`.`gender_gender_id` INNER JOIN `location` ON `location`.`id` = `customer`.`location_id` WHERE `gender_id` = '1' ");
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);

                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("mobile"));
                    v.add(rs.getString("name"));
                    v.add(rs.getString("birthday"));
                    v.add(rs.getString("mobile2"));
                    v.add(rs.getString("telephone_land"));
                    v.add(rs.getString("nic"));
                    v.add(rs.getString("register_date"));

                    dtm.addRow(v);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Type == "People under 18 years Old") {
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `customer` INNER JOIN `gender` ON `gender`.`gender_id` = `customer`.`gender_gender_id` INNER JOIN `location` ON `location`.`id` = `customer`.`location_id` WHERE TIMESTAMPDIFF(YEAR, `customer`.`birthday`, CURDATE()) < 18 ");
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);

                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("mobile"));
                    v.add(rs.getString("name"));
                    v.add(rs.getString("birthday"));
                    v.add(rs.getString("mobile2"));
                    v.add(rs.getString("telephone_land"));
                    v.add(rs.getString("nic"));
                    v.add(rs.getString("register_date"));

                    dtm.addRow(v);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Type == "People over 18 years Old") {
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `customer` INNER JOIN `gender` ON `gender`.`gender_id` = `customer`.`gender_gender_id` INNER JOIN `location` ON `location`.`id` = `customer`.`location_id` WHERE TIMESTAMPDIFF(YEAR, `customer`.`birthday`, CURDATE()) > 18 ");
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);

                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("id"));
                    v.add(rs.getString("Name"));
                    v.add(rs.getString("birthday"));
                    v.add(rs.getString("telepohone_mobile1"));
                    v.add(rs.getString("telephone_mobile2"));
                    v.add(rs.getString("telephone_land"));
                    v.add(rs.getString("nic"));
                    v.add(rs.getString("location_name"));
                    v.add(rs.getString("register_date"));

                    dtm.addRow(v);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Type == "People under 50 years Old") {
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `customer` INNER JOIN `gender` ON `gender`.`gender_id` = `customer`.`gender_gender_id` INNER JOIN `location` ON `location`.`id` = `customer`.`location_id` WHERE TIMESTAMPDIFF(YEAR, `customer`.`birthday`, CURDATE()) < 50 ");
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);

                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("id"));
                    v.add(rs.getString("Name"));
                    v.add(rs.getString("birthday"));
                    v.add(rs.getString("telepohone_mobile1"));
                    v.add(rs.getString("telephone_mobile2"));
                    v.add(rs.getString("telephone_land"));
                    v.add(rs.getString("nic"));
                    v.add(rs.getString("location_name"));
                    v.add(rs.getString("register_date"));

                    dtm.addRow(v);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Type == "People over 50 years Old") {
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `customer` INNER JOIN `gender` ON `gender`.`gender_id` = `customer`.`gender_gender_id` INNER JOIN `location` ON `location`.`id` = `customer`.`location_id` WHERE TIMESTAMPDIFF(YEAR, `customer`.`birthday`, CURDATE()) > 50 ");
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);

                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("id"));
                    v.add(rs.getString("Name"));
                    v.add(rs.getString("birthday"));
                    v.add(rs.getString("telepohone_mobile1"));
                    v.add(rs.getString("telephone_mobile2"));
                    v.add(rs.getString("telephone_land"));
                    v.add(rs.getString("nic"));
                    v.add(rs.getString("location_name"));
                    v.add(rs.getString("register_date"));

                    dtm.addRow(v);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Type == "Custom Numbers") {
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `testing`");
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);

                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("mobile"));

                    dtm.addRow(v);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Type == "Today Birthdays") {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                Date today = new Date();
                String formatedDate = sdf.format(today);
                ResultSet rs = MySQL.execute("SELECT * FROM `customer` WHERE `birthday` LIKE '" + formatedDate + "'");
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);

                while (rs.next()) {
                    Vector v = new Vector();
                    v.add(rs.getString("mobile"));
                    v.add(rs.getString("name"));
                    v.add(rs.getString("birthday"));
                    v.add(rs.getString("mobile2"));
                    v.add(rs.getString("telephone_land"));
                    v.add(rs.getString("nic"));
                    v.add(rs.getString("register_date"));

                    dtm.addRow(v);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

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

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        userNameField = new javax.swing.JLabel();
        dateField = new javax.swing.JLabel();
        timeField = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        lblUnits = new javax.swing.JLabel();
        SmsExpireOn = new javax.swing.JLabel();
        lblCharacters = new javax.swing.JLabel();
        lblRemaining = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblCharactersPerUnit = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblEncoding = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        RemainingSMSUnits = new javax.swing.JLabel();
        jToggleButton2 = new javax.swing.JToggleButton();
        jTextField1 = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel14.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel14.setText("Operator : ");

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
                        .addComponent(jLabel14)
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
                    .addComponent(jLabel14)
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
        jLabel5.setText("Boradcast Message System");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
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

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/reload.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel6.setText("Actions");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/send-data.png"))); // NOI18N
        jButton4.setText("Send Message (UnPublsihed)");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel7.setText("Pages");

        jPanel6.setBackground(new java.awt.Color(206, 206, 206));
        jPanel6.setForeground(new java.awt.Color(0, 0, 0));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel8.setText("Message");
        jPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 350, -1, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel9.setText("Select Audience");
        jPanel6.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, -1, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel12.setText("Update Areas");
        jPanel6.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, -1, -1));
        jPanel6.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 300, 840, 10));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Whatsapp", "Name", "Birthday", "Mobile"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanel6.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 840, 140));

        jLabel13.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel13.setText("Selected Audience Members");
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Everyone", "Only Womens", "Only Mens", "People over 18 years Old", "People under 18 years Old", "People over 50 years Old", "People under 50 years Old", "Today Birthdays", "Custom Numbers" }));
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
        jPanel6.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, 160, -1));

        jLabel19.setFont(new java.awt.Font("Segoe UI Historic", 0, 18)); // NOI18N
        jLabel19.setText("Filter Options");
        jPanel6.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Whatsapp Message", "Send Email " }));
        jPanel6.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 200, -1));

        jLabel20.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jLabel20.setText("Reg_No");
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jTextArea3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextArea3KeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(jTextArea3);

        jPanel6.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 370, 410, 110));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Remaining");
        jPanel6.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 360, -1, -1));

        lblUnits.setForeground(new java.awt.Color(255, 0, 0));
        lblUnits.setText("1");
        jPanel6.add(lblUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 380, 80, -1));

        SmsExpireOn.setFont(new java.awt.Font("Segoe UI Historic", 2, 14)); // NOI18N
        SmsExpireOn.setForeground(new java.awt.Color(255, 0, 51));
        SmsExpireOn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SmsExpireOn.setText("0");
        jPanel6.add(SmsExpireOn, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 50, 190, -1));

        lblCharacters.setForeground(new java.awt.Color(255, 0, 0));
        lblCharacters.setText("0");
        jPanel6.add(lblCharacters, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 380, -1, -1));

        lblRemaining.setForeground(new java.awt.Color(255, 0, 0));
        lblRemaining.setText("160/160");
        jPanel6.add(lblRemaining, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 380, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Characters");
        jPanel6.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 360, -1, -1));

        lblCharactersPerUnit.setForeground(new java.awt.Color(255, 0, 0));
        lblCharactersPerUnit.setText("160");
        jPanel6.add(lblCharactersPerUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 380, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Characters Per Unit");
        jPanel6.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 360, -1, -1));

        lblEncoding.setForeground(new java.awt.Color(255, 0, 0));
        lblEncoding.setText("GSM_7BIT");
        jPanel6.add(lblEncoding, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 380, -1, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Encoding");
        jPanel6.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 360, -1, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Unit Count");
        jPanel6.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 360, -1, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI Historic", 1, 18)); // NOI18N
        jLabel16.setText("Remaining SMS Unit Count");
        jPanel6.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 20, -1, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI Historic", 1, 18)); // NOI18N
        jLabel17.setText("Sms Expire On");
        jPanel6.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 20, 130, -1));

        RemainingSMSUnits.setFont(new java.awt.Font("Segoe UI Historic", 2, 14)); // NOI18N
        RemainingSMSUnits.setForeground(new java.awt.Color(255, 0, 51));
        RemainingSMSUnits.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RemainingSMSUnits.setText("0");
        jPanel6.add(RemainingSMSUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 50, 220, -1));

        jToggleButton2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/send-data.png"))); // NOI18N
        jToggleButton2.setText("Send SMS");
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });
        jPanel6.add(jToggleButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 410, 470, 60));

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
        jPanel6.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 840, -1));

        jToggleButton1.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jToggleButton1.setText("Send Whatsapp (UnPublished)");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton3.setText("Message Templates");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator3)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel7)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jToggleButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(54, 54, 54)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1030, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        String SelectedIteem = String.valueOf(jComboBox1.getSelectedItem());
        System.out.println(SelectedIteem);

        if ("Everyone".equals(SelectedIteem)) {
            LoadBoradCastUserTabel("Everyone");
        } else if ("Only Womens".equals(SelectedIteem)) {
            LoadBoradCastUserTabel("Only Womens");
        } else if ("Only Mens".equals(SelectedIteem)) {
            LoadBoradCastUserTabel("Only Mens");
        } else if ("People under 18 years Old".equals(SelectedIteem)) {
            LoadBoradCastUserTabel("People under 18 years Old");
        } else if ("People over 18 years Old".equals(SelectedIteem)) {
            LoadBoradCastUserTabel("People over 18 years Old");
        } else if ("Only adults (Up to 18)".equals(SelectedIteem)) {
            LoadBoradCastUserTabel("Only adults (Up to 18)");
        } else if ("People over 50 years Old".equals(SelectedIteem)) {
            LoadBoradCastUserTabel("People over 50 years Old");
        } else if ("People under 50 years Old".equals(SelectedIteem)) {
            LoadBoradCastUserTabel("People under 50 years Old");
        } else if ("Custom Numbers".equals(SelectedIteem)) {
            LoadBoradCastUserTabel("Custom Numbers");
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        JOptionPane.showMessageDialog(this, "this Function is Under Maintanance. Please Contact the Developer", "Error", JOptionPane.ERROR_MESSAGE);
//        try {
//            // Send Whatsapp Messages
//
//            String Message = jTextArea3.getText();
//            
//            if (!Message.isEmpty()) {
//                JOptionPane.showMessageDialog(this, "Please Dont Move the Mouse or Keyboard Activity", "WARNING", JOptionPane.WARNING_MESSAGE);
//                
//                OpenWhatsapp();
//                
//                int TableRows = jTable1.getRowCount();
//                ArrayList customerList = new ArrayList();
//                
//                for (int i = 0; i < TableRows; i++) {
//                    customerList.add(jTable1.getValueAt(i, 3));
//                    String PhoneNumber = (String) jTable1.getValueAt(i, 0);
//                    MessageSendingProcess(PhoneNumber, Message);
//                }
//                closeWhatsapp();
//                
//            } else {
//                JOptionPane.showMessageDialog(this, "Please Enter a Message First");
//            }
//            
//        } catch (AWTException ex) {
//            ex.printStackTrace();
//            Logger.getLogger(BroadCastMessageSystem.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Dashboard d = new Dashboard();
        d.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "this Function is under maintenance. please Contact the Developer.", "Error", JOptionPane.ERROR_MESSAGE);
//        JFileChooser filechooser = new JFileChooser();
//        int result = filechooser.showOpenDialog(this);
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File selectedFile = filechooser.getSelectedFile();
//            String path = selectedFile.getAbsolutePath();
//
//            try {
//
//                // Load the image from a file
//                BufferedImage image = ImageIO.read(new File(path));
//
//                JOptionPane.showMessageDialog(this, "Please Dont Move the Mouse or Keyboard Activity", "WARNING", JOptionPane.WARNING_MESSAGE);
//
//                OpenWhatsapp();
//
//                int TableRows = jTable1.getRowCount();
//                ArrayList customerList = new ArrayList();
//
//                for (int i = 0; i < TableRows; i++) {
//                    customerList.add(jTable1.getValueAt(i, 3));
//                    String PhoneNumber = (String) jTable1.getValueAt(i, 0);
//                    SendImagesProcess(PhoneNumber, image);
//                }
//                closeWhatsapp();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            System.out.println("no");
//        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jTextArea3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea3KeyReleased
        // TODO add your handling code here:
        String txtMessage = jTextArea3.getText();
        if (!txtMessage.isEmpty()) {
            SMSInfo info = SMSCalculator.calculate(txtMessage);
            lblUnits.setText(String.valueOf(info.getUnits()));
            lblCharacters.setText(String.valueOf(info.getCharacters()));
            lblRemaining.setText(info.getRemaining());
            lblCharactersPerUnit.setText(String.valueOf(info.getCharactersPerUnit()));
            lblEncoding.setText(info.getEncoding());
        } else {
            lblUnits.setText("1");
            lblCharacters.setText("0");
            lblRemaining.setText("160");
            lblEncoding.setText("_");
        }
    }//GEN-LAST:event_jTextArea3KeyReleased

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        // Send Sms 
        StringBuilder numbers = new StringBuilder();

        if (!jTextArea3.getText().trim().isEmpty()) {

            int[] selectedRows = jTable1.getSelectedRows();

            if (selectedRows.length > 0) {

                for (int row : selectedRows) {
                    numbers.append(jTable1.getValueAt(row, 0).toString()).append(",");
                }

                // Remove the last ", "
                if (numbers.length() > 2) {
                    numbers.setLength(numbers.length() - 1);
                }

                System.out.println(numbers.toString());
                SMS.sendSMS(String.valueOf(numbers), jTextArea3.getText());

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select the customer details in the table.",
                        "Invalid Customer Selection",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the message in the text field.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        String customer_mobile = jTextField1.getText();

        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `customer` WHERE `mobile` LIKE '%" + customer_mobile + "%'  ");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("mobile"));
                v.add(rs.getString("name"));
                v.add(rs.getString("birthday"));
                v.add(rs.getString("mobile2"));
                v.add(rs.getString("telephone_land"));
                v.add(rs.getString("nic"));
                v.add(rs.getString("register_date"));

                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BroadCastMessageSystem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel RemainingSMSUnits;
    private javax.swing.JLabel SmsExpireOn;
    private javax.swing.JLabel dateField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JLabel lblCharacters;
    private javax.swing.JLabel lblCharactersPerUnit;
    private javax.swing.JLabel lblEncoding;
    private javax.swing.JLabel lblRemaining;
    private javax.swing.JLabel lblUnits;
    private javax.swing.JLabel timeField;
    private javax.swing.JLabel userNameField;
    // End of variables declaration//GEN-END:variables
}
