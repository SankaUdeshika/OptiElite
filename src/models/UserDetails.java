package models;

public class UserDetails {

    public static String UserName;
    public static String UserId;
    public static String UserLocation_id;
    public static String UserRole;
    public static String sms_api_token;
    public static String sms_sender_id;

    public UserDetails() {
//        no return
    }

    public UserDetails(String firstName, String lastName, String userId, String locationID, String userRole, String api_token, String sms_sender_id) {
        String FirstName = firstName;
        String FLirstName = lastName;
        String UserLocation = locationID;

        this.UserName = FirstName + " " + lastName;

        this.UserId = userId;

        this.UserLocation_id = UserLocation;

        this.UserRole = userRole;
        this.sms_api_token = api_token;
        this.sms_sender_id = sms_sender_id;
        
    }

//    public String getUserName() {
//        return UserName;
//    }
//    public String getUserID() {
//        return UserId;
//    }
//
//    public String getLocaitonID() {
//        return UserLocation_id;
//    }
}
