/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.cj.protocol.Resultset;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author sanka
 */
public class SMS {

    private static final String API_TOKEN = "6409|cBTGmFq0ZDeqdYVS1bFvVitieHtQBwebApEGbtOS574a9734";
    private static final String BALANCE_URL = "https://app.text.lk/api/v3/balance";
    // Replace with your approved Sender ID
    private static final String SENDER_ID = "Vayonova";
    private static final String API_URL = "https://app.text.lk/api/v3/sms/send";

    public static SmsProfileInfo getBalance() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BALANCE_URL)) // ✅ Correct
                    .header("Authorization", "Bearer " + API_TOKEN)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response
                    = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject responseObject = new Gson().fromJson(response.body(), JsonObject.class);
            if (responseObject.has("data")) {
                JsonObject dataObject = responseObject.get("data").getAsJsonObject();
                String remaining_balance = dataObject.get("remaining_balance").getAsString();
                String expired_on = dataObject.get("expired_on").getAsString();
                return new SmsProfileInfo(remaining_balance, expired_on);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static void sendSMS(String mobile, String message) {

        try {
            String json = "{"
                    + "\"recipient\":\"" + mobile + "\","
                    + "\"sender_id\":\"" + SENDER_ID + "\","
                    + "\"type\":\"plain\","
                    + "\"message\":\"" + message.replace("\"", "\\\"") + "\""
                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Authorization", "Bearer " + API_TOKEN)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("HTTP Status : " + response.statusCode());
            System.out.println("Response : ");
            System.out.println(response.body());
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    public static void invoiceThankYou_sms(String invoice_id) {

        try {
            ResultSet rs = MySQL.execute("SELECT `customer_mobile` FROM `invoice` WHERE `invoice_id` = '" + invoice_id + "'");
            ResultSet ThankyouMsg_rs = MySQL.execute("SELECT `message` FROM `sms_templates` WHERE `message_name` = 'invoice_thankyou' ");

            if (rs.next() && ThankyouMsg_rs.next()) {
                String mobile = rs.getString("customer_mobile");
                String message = ThankyouMsg_rs.getString("message"); 

                String json = "{"
                        + "\"recipient\":\"" + mobile + "\","
                        + "\"sender_id\":\"" + SENDER_ID + "\","
                        + "\"type\":\"plain\","
                        + "\"message\":\"" + message.replace("\"", "\\\"") + "\""
                        + "}";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL))
                        .header("Authorization", "Bearer " + API_TOKEN)
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                HttpClient client = HttpClient.newHttpClient();

                HttpResponse<String> response = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

                System.out.println("HTTP Status : " + response.statusCode());
                System.out.println("Response : ");
                System.out.println(response.body());
            } else {
                System.out.println("Something Wrong, Please Try again later");
            }

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(SMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
