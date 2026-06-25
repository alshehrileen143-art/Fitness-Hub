import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertAdminUser {
    public static void main(String[] args) {
        InsertAdminUser.insertAdmin();
    }

    public static void insertAdmin() {
        String sql = "INSERT INTO users (username, password, is_trainer, is_client, is_admin) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "admin");
            pstmt.setString(2, "admin");
            pstmt.setInt(3, 0); // Not a trainer
            pstmt.setInt(4, 0); // Not a client
            pstmt.setInt(5, 1); // Is an admin

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Admin user inserted successfully.");
            } else {
                System.out.println("Failed to insert admin user.");
            }
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }
}

