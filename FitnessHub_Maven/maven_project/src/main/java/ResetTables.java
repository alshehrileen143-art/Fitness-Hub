import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ResetTables {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "sys as sysdba";
    private static final String PASSWORD = "1234";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            System.out.println("Connected to Oracle Database...");

            // Drop tables if they exist
            String[] tables = {"USERS", "BOOKINGS", "FEES", "MEMBERSHIPS", "WORKOUTS"};
            for (String table : tables) {
                try {
                    stmt.executeUpdate("DROP TABLE " + table + " CASCADE CONSTRAINTS PURGE");
                    System.out.println("Dropped table: " + table);
                } catch (SQLException e) {
                    System.out.println("Table " + table + " does not exist or cannot be dropped.");
                }
            }

            // Create tables again
            String[] createTableQueries = {
                    "CREATE TABLE users (" +
                            "    user_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                            "    username VARCHAR2(100) NOT NULL," +
                            "    password VARCHAR2(255) NOT NULL," +
                            "    is_trainer NUMBER(1) DEFAULT 0 NOT NULL CHECK (is_trainer IN (0, 1))," +
                            "    is_client NUMBER(1) DEFAULT 0 NOT NULL CHECK (is_client IN (0, 1))," +
                            "    is_admin NUMBER(1) DEFAULT 0 NOT NULL CHECK (is_admin IN (0, 1))" +
                            ")",

                    "CREATE TABLE bookings (" +
                            "    booking_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                            "    trainer VARCHAR2(100) NOT NULL," +
                            "    booked VARCHAR2(100) NOT NULL," +
                            "    time NUMBER NOT NULL," +
                            "    day NUMBER NOT NULL," +
                            "    member VARCHAR2(100) NOT NULL" +
                            ")",

                    "CREATE TABLE fees (" +
                            "    fee_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                            "    name VARCHAR2(100)," +
                            "    fee_paid VARCHAR2(100)" +
                            ")",

                    "CREATE TABLE memberships (" +
                            "    MembershipType VARCHAR2(100) PRIMARY KEY," +
                            "    Duration VARCHAR2(50) NOT NULL," +
                            "    price NUMBER NOT NULL," +
                            "    Features VARCHAR2(500) NOT NULL" +
                            ")",

                    "CREATE TABLE workouts (" +
                            "    workout_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                            "    username VARCHAR2(100) NOT NULL," +
                            "    workout VARCHAR2(500) NOT NULL" +
                            ")"
            };

            for (String query : createTableQueries) {
                stmt.executeUpdate(query);
                System.out.println("Table created: " + query.split(" ")[2]);
            }

            System.out.println("Tables reset successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
