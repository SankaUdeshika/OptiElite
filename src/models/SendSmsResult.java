/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author sanka
 */
public class SendSmsResult {

    boolean success = false;
    String remainingUnits = "";
    String message = "";

    public SendSmsResult(boolean isSuccess, String recivedRemainingUnits, String message) {
        this.success = isSuccess;
        this.remainingUnits = recivedRemainingUnits;
        this.message = message;
    }

    public Boolean getisSuccess() {
        return success;
    }

    public String getRemainingUnits() {
        return remainingUnits;
    }

    public String getMessage() {
        return message;
    }

}
