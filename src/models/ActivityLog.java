package models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author sanka
 */
public class ActivityLog {

    public ActivityLog() {

    }

    public static void addLog(String message,int piority) {
        Date todayDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatTodayDate = dateFormat.format(todayDate);
        System.out.println("activity Log writtend :" + message );
        try {
            MySQL.execute("INSERT INTO `activity_log` (`log_text`,`users_id`,date,activity_priority_id) VALUES ('"+message+"','"+UserDetails.UserId+"','"+formatTodayDate+"','"+piority+"')");
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
    }
}
