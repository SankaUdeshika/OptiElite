/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.swing.JOptionPane;

/**
 *
 * @author sanka
 */
public class SMS {

    private static final String API_TOKEN = "6409|cBTGmFq0ZDeqdYVS1bFvVitieHtQBwebApEGbtOS574a9734";
    private static final String BALANCE_URL = "https://app.text.lk/api/v3/balance";

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

}
