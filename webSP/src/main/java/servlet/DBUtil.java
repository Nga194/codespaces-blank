package servlet;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
 
   private static final String URL =
"jdbc:mysql://localhost:3306/northwind?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "Vietnam@123"; 
 
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Không tìm thấy MySQL JDBC Driver", e);
        }
    }
 
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
 
    public static String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
