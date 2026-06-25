import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class memberships {
    public boolean addMemberships(String membershipType, String duration, Integer price, String features) {
        String sql = "INSERT INTO MEMBERSHIPS (MEMBERSHIPTYPE, DURATION, PRICE, FEATURES) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, membershipType);
            pstmt.setString(2, duration);
            pstmt.setInt(3, price);
            pstmt.setString(4, features);

            pstmt.executeUpdate();
            System.out.println("Membership inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
        return true;
    }

    public List<Membership> getAllMemberships() {
        List<Membership> membershipsList = new ArrayList<>();
        String sql = "SELECT MEMBERSHIPTYPE, DURATION, PRICE, FEATURES FROM MEMBERSHIPS";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Membership membership = new Membership(
                        rs.getString("MEMBERSHIPTYPE"),
                        rs.getString("DURATION"),
                        rs.getInt("PRICE"),
                        rs.getString("FEATURES")
                );
                membershipsList.add(membership);
            }
        } catch (SQLException e) {
            System.out.println("Retrieval failed: " + e.getMessage());
        }
        return membershipsList;
    }

    public boolean removeMembership(String membershipType) {
        String sql = "DELETE FROM MEMBERSHIPS WHERE LOWER(MEMBERSHIPTYPE) = LOWER(?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, membershipType);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Membership removed successfully.");
                return true;
            } else {
                System.out.println("No membership found with the given type.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Deletion failed: " + e.getMessage());
            return false;
        }
    }


}

