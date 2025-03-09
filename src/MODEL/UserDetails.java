package model;

public class UserDetails {

    public static String UserName;
    public static String UserId;
    public static String UserLocation_id;

    public UserDetails() {
//        no return
    }

    public UserDetails(String firstName, String lastName, String userId, String locationID) {
        String FirstName = firstName;
        String FLirstName = lastName;
        String UserLocation = locationID;

        this.UserName = FirstName + " " + lastName;

        this.UserId = userId;

        this.UserLocation_id = UserLocation;

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
