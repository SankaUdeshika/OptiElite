package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class MySQL {

    private static Connection connection;

    static {
        try {
            jdbc:
            mysql://your-database-host:3306/your-database-name?autoReconnect=true&useSSL=false
            Class.forName("com.mysql.cj.jdbc.Driver");

//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eagle_eye_database", "root", "12345678"); // My localhost
              connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/optielite", "root", "12345678"); // My localhost

//            connection = DriverManager.getConnection("jdbc:mysql://118.139.181.184:3306/EagleEye_DB?autoReconnect=true&useSSL=false", "fitnessfirstadmin", "fitnessfirst_db"); // FitnessFirst DB
//            connection = DriverManager.getConnection("jdbc:mysql://77.37.44.88:3306/eagle_eye_database?autoReconnect=true&useSSL=false", "user", "8891$l£Pu9"); // Sadev VPS
//            connection = DriverManager.getConnection("jdbc:mysql://sankadb123-do-user-18315839-0.g.db.ondigitalocean.com:25060/eagle_eye_database?autoReconnect=true&useSSL=false", "doadmin", "AVNS_7pHVUsxjCv1YxiLhmst"); // Digital Ocean
//            connection = DriverManager.getConnection("jdbc:mysql://sankadb123-do-user-18315839-0.g.db.ondigitalocean.com:25060/optielite?autoReconnect=true&useSSL=false", "doadmin", "AVNS_7pHVUsxjCv1YxiLhmst"); // Digital Ocean
//            connection = DriverManager.getConnection("jdbc:mysql://31.97.61.250:3306/optielite?autoReconnect=true&useSSL=false", "sanka", "Sanka123!@"); // Sanka VPS 
//            connection = DriverManager.getConnection("jdbc:mysql://31.97.61.250:3306/dasanahyaka_optielite?autoReconnect=true&useSSL=false", "sanka", "Sanka123!@"); // Sanka VPS  Dasanayaka Opticals
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {

        return connection;
    }

    public static ResultSet execute(String query) {
        try {
            Statement statement = connection.createStatement();

            if (query.startsWith("SELECT")) {
                ResultSet resultSet = statement.executeQuery(query);
                return resultSet;
            } else {
//                int result = statement.executeUpdate(query);
                PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                stmt.executeUpdate();
                ResultSet resultSet = stmt.getGeneratedKeys();
                return resultSet;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
