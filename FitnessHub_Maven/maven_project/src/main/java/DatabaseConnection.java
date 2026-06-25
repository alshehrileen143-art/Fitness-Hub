import java.sql.*;

public class DatabaseConnection {
    // Database connection details
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe"; // Use your working IP
    private static final String USER = "sys as sysdba"; // Using SYSDBA
    private static final String PASSWORD = "1234"; // Your password

    // Method to establish a connection
    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(false);
            System.out.println("Database Connected Successfully!");
            createTablesIfNotExist(conn);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // Method to check and create tables if they don't exist
    private static void createTablesIfNotExist(Connection conn) {
        String[] tableCheckQueries = {
                "SELECT table_name FROM user_tables WHERE table_name = 'USERS'",
                "SELECT table_name FROM user_tables WHERE table_name = 'BOOKINGS'",
                "SELECT table_name FROM user_tables WHERE table_name = 'FEES'",
                "SELECT table_name FROM user_tables WHERE table_name = 'MEMBERSHIPS'",
                "SELECT table_name FROM user_tables WHERE table_name = 'WORKOUTS'"
        };

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

        try (Statement stmt = conn.createStatement()) {
            for (int i = 0; i < tableCheckQueries.length; i++) {
                try (ResultSet rs = stmt.executeQuery(tableCheckQueries[i])) {
                    if (!rs.next()) {
                        stmt.executeUpdate(createTableQueries[i]);
                        System.out.println("Table created: " + createTableQueries[i].split(" ")[2]);
                    } else {
                        System.out.println("Table already exists: " + createTableQueries[i].split(" ")[2]);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }
}
