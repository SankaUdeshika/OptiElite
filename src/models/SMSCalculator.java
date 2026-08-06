package models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sanka
 */
public class SMSCalculator {

    public static SMSInfo calculate(String message) {

        boolean unicode = !isGSM7(message);

        int length = message.length();

        int singleLimit;
        int multiLimit;
        String encoding;

        if (unicode) {

            singleLimit = 70;
            multiLimit = 67;
            encoding = "UNICODE";

        } else {

            singleLimit = 160;
            multiLimit = 153;
            encoding = "GSM_7BIT";

        }

        int units;
        int charactersPerUnit;
        int totalCapacity;

        if (length <= singleLimit) {

            units = 1;
            charactersPerUnit = singleLimit;
            totalCapacity = singleLimit;

        } else {

            units = (int) Math.ceil((double) length / multiLimit);
            charactersPerUnit = multiLimit;
            totalCapacity = units * multiLimit;

        }

        int remaining = totalCapacity - length;

        return new SMSInfo(
                units,
                length,
                remaining + " / " + totalCapacity,
                charactersPerUnit,
                encoding
        );

    }

    private static boolean isGSM7(String text) {

        String gsm
                = "@£$¥èéùìòÇ\nØø\rÅåΔ_ΦΓΛΩΠΨΣΘΞ"
                + "ÆæßÉ !\"#¤%&'()*+,-./0123456789:;<=>?"
                + "¡ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§¿"
                + "abcdefghijklmnopqrstuvwxyzäöñüà";

        for (char c : text.toCharArray()) {
            if (gsm.indexOf(c) == -1) {
                return false;
            }
        }

        return true;
    }

}
