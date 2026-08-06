/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author sanka
 */
public class SmsProfileInfo {

    String remaining_balance = "";
    String expired_on = "";

    public SmsProfileInfo(String Recieved_remaining_balance, String Recieverd_expired_on) {
        this.remaining_balance = Recieved_remaining_balance;
        this.expired_on = Recieverd_expired_on;
    }
    
    public String getRemaining_balance(){
        return remaining_balance;
    }
    
    public String getExpired_on_date(){
        return expired_on;
    }
}
