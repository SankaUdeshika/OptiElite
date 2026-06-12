/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.ResultSet;

/**
 *
 * @author sanka
 */
public class generateInvoiceId {

    public String generateInvoiceId(int jobTypeId, int locationId) {
        // Search Settings file
        ResultSet rs = MySQL.execute("SELECT * FROM `settings` INNER JOIN `invoice_type` ON `invoice_type`.`id` = `settings`.`invoice_type_id` WHERE `setting_id` = '1' ");
        try {
            if (rs.next()) {
                String type = rs.getString("type");
                if (type.equals("Type A Long")) { // Ares Model

                } else if (type.equals("Type B Short")) { // Lensamte Model
                    String invoice_id = Type_b_short(jobTypeId, locationId);
                    System.out.println(invoice_id + " is the invoice Id");
                    return invoice_id;
                } else if (type.equals("Normal No")) { // Dasanayaka Model
                    String invoice_id = normalNo();
                    System.out.println(invoice_id + "is the Invoice Id ");
                    return invoice_id;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String Type_b_short(int jobTypeId, int locationId) throws Exception {
        System.out.println("Type B Short Invoice is Generating");
        // Step 1: Get Job Type name (first 2 letters)
        String jobTypePrefix = "";
        ResultSet jobType_rs = MySQL.execute("SELECT * FROM `jobtype` WHERE `job_id` = '" + jobTypeId + "'");
        if (jobType_rs.next()) {
            String jobTypeName = jobType_rs.getString("jobType"); // adjust column name if different
            jobTypePrefix = jobTypeName.substring(0, Math.min(2, jobTypeName.length())).toUpperCase();
        } else {
            throw new Exception("Job Type not found for ID: " + jobTypeId);
        }
        // Step 2: Get Location name (first 2 letters)
        String locationPrefix = "";
        ResultSet location_rs = MySQL.execute("SELECT * FROM `location` WHERE `id` = '" + locationId + "'");
        if (location_rs.next()) {
            String locationName = location_rs.getString("location_name"); // adjust column name if different
            locationPrefix = locationName.substring(0, Math.min(2, locationName.length())).toUpperCase();
        } else {
            throw new Exception("Location not found for ID: " + locationId);
        }

        // Step 3: Build the prefix  e.g., "SHDH"
        String prefix = jobTypePrefix + locationPrefix;

        // Step 4: Find the latest invoice number with this prefix
        int nextNumber = 1;
        ResultSet invoice_rs = MySQL.execute(
                "SELECT `invoice_id` FROM `invoice` WHERE `invoice_id` LIKE '" + prefix + "-%' "
                + "ORDER BY CAST(SUBSTRING(`invoice_id`, " + (prefix.length() + 2) + ") AS UNSIGNED) DESC LIMIT 1"
        );

        if (invoice_rs.next()) {
            String lastInvoiceId = invoice_rs.getString("invoice_id");
            // Extract the number part after the dash e.g "SHDH-35" -> "35"
            String numberPart = lastInvoiceId.substring(prefix.length() + 1);
            nextNumber = Integer.parseInt(numberPart) + 1;
        }

        // Step 5: Build and return the full invoice ID e.g., "SHDH-1"
        String invoiceId = prefix + "-" + nextNumber;

        // Step 6: Check if this invoice ID already exists (safety check)
        ResultSet check_rs = MySQL.execute("SELECT `invoice_id` FROM `invoice` WHERE `invoice_id` = '" + invoiceId + "'");
        if (check_rs.next()) {
            throw new Exception("Invoice ID already exists: " + invoiceId);
        }

        return invoiceId;
    }

    private String normalNo() throws Exception {
        // Get all invoice IDs that are purely numeric
        ResultSet rs = MySQL.execute(
                "SELECT `invoice_id` FROM `invoice` "
                + "WHERE `invoice_id` REGEXP '^[0-9]+$' "
                + "ORDER BY CAST(`invoice_id` AS UNSIGNED) DESC LIMIT 1"
        );

        if (rs.next()) {
            int lastId = Integer.parseInt(rs.getString("invoice_id"));
            return String.valueOf(lastId + 1);
        } else {
            // No numeric invoice exists yet, start from 1
            return "1";
        }
    }

}
