import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Insert a new user
    public boolean insertUser(String username, String password, boolean isTrainer, boolean isClient, boolean isAdmin) {
        String sql = "INSERT INTO users (username, password, is_trainer, is_client ,is_admin) " +
                "VALUES (?, ?, ?, ? ,?)";  // Remove user_id from INSERT

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // Store hashed passwords in real applications
            pstmt.setInt(3, isTrainer ? 1 : 0);
            pstmt.setInt(4, isClient ? 1 : 0);
            pstmt.setInt(5, isAdmin ? 1 : 0);

            pstmt.executeUpdate();
            System.out.println("User inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
        return true;
    }


    // Retrieve all users
    public void getAllUsers() {
        String sql = "SELECT user_id, username, is_trainer, is_client, is_admin FROM users";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("user_id") +
                        ", Username: " + rs.getString("username") +
                        ", Is Trainer: " + (rs.getInt("is_trainer") == 1) +
                        ", Is Client: " + (rs.getInt("is_client") == 1) +
                        ", Is Admin: " + (rs.getInt("is_admin") == 1));
            }
        } catch (SQLException e) {
            System.out.println("Retrieval failed: " + e.getMessage());
        }
    }

    public boolean validateUser(String username, String password, boolean isTrainer, boolean isClient, boolean isAdmin) {
        String sql = "SELECT COUNT(*) AS user_count FROM users " +
                "WHERE username = ? AND password = ? " +
                "AND is_trainer = ? AND is_client = ? AND is_admin = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, isTrainer ? 1 : 0);
            pstmt.setInt(4, isClient ? 1 : 0);
            pstmt.setInt(5, isAdmin ? 1 : 0);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_count") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Validation failed: " + e.getMessage());
        }
        return false;
    }

    // Function to insert member fee status
    public boolean insertmember(String username) {
        String sql = "INSERT INTO fees (name, fee_paid) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, "not paid");

            pstmt.executeUpdate();
            System.out.println("Member inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
        return true;
    }

    // Function to update fee status
    public boolean updateFee(String username) {
        String sql = "UPDATE fees SET fee_paid = ? WHERE LOWER(name) = LOWER(?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "paid");
            pstmt.setString(2, username);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Fee updated successfully for user: " + username);
                return true;
            } else {
                System.out.println("User not found or update failed.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }


    // Retrieve all members' fee status
    public List<MemberFee> getAllMembers() {
        String sql = "SELECT name, CASE WHEN fee_paid = 'paid' THEN 1 ELSE 0 END AS fee_paid FROM fees";
        List<MemberFee> membersList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                boolean feePaid = rs.getInt("fee_paid") == 1; // Convert Oracle NUMBER(1) to boolean

                membersList.add(new MemberFee(name, feePaid));
            }
        } catch (SQLException e) {
            System.out.println("Retrieval failed: " + e.getMessage());
        }

        return membersList;
    }

    // Export members' fee data to CSV
    public boolean exportMembersToCSV() {
        String filePath = System.getProperty("user.dir") + File.separator + "Payments" + File.separator + "members_fees.csv";

        // Ensure Payments folder exists
        File paymentsFolder = new File(System.getProperty("user.dir") + File.separator + "Payments");
        if (!paymentsFolder.exists()) {
            paymentsFolder.mkdir(); // Create the folder if it doesn't exist
        }

        List<MemberFee> members = getAllMembers();

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Name,Fee Paid\n"); // Write CSV headers

            for (MemberFee member : members) {
                writer.append(member.getName()).append(",");
                writer.append(member.isFeePaid() ? "Paid" : "Not Paid").append("\n");
            }

            System.out.println("CSV Exported Successfully: " + filePath);
        } catch (IOException e) {
            System.out.println("CSV Export Failed: " + e.getMessage());
            return false;
        }
        return true;
    }

    // Get count of members who have paid fees
    public int getMemberCount() {
        String sql = "SELECT COUNT(*) FROM fees WHERE LOWER(fee_paid) = 'paid'";
        int count = 0;

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Count retrieval failed: " + e.getMessage());
        }
        return count;
    }

    // Get list of members' usernames
    public List<String> getMembersName() {
        List<String> members = new ArrayList<>();
        String sql = "SELECT username FROM users WHERE is_client = 1"; // Oracle uses NUMBER(1) for booleans

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                members.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            System.out.println("Retrieval failed: " + e.getMessage());
        }

        return members;
    }

    // Get total count of users
    public int getTotalUserCount() {
        String sql = "SELECT COUNT(*) FROM users";
        int count = 0;

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("User count retrieval failed: " + e.getMessage());
        }
        return count;
    }

    // Insert workout entry
    public boolean insertWorkout(String username, String workout) {
        String sql = "INSERT INTO workouts (username, workout) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, workout);

            pstmt.executeUpdate();
            System.out.println("Workout inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
        return true;
    }
    // Get workouts for a user
    public List<String> getWorkouts(String username) {
        List<String> workouts = new ArrayList<>();
        String sql = "SELECT workout FROM workouts WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                workouts.add(rs.getString("workout"));
            }
        } catch (SQLException e) {
            System.out.println("Fetch failed: " + e.getMessage());
        }
        return workouts;
    }

    // Insert slot function
    public boolean insertSlot(String trainer, Integer time, Integer day) {
        int booked = 0; // Use 0 for 'no' instead of a string
        String member = "---";

        String sql = "INSERT INTO bookings (trainer, booked, time, day, member) VALUES (?, ?, ?, ?, ?)";


        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trainer);
            pstmt.setInt(2, booked);
            pstmt.setInt(3, time);
            pstmt.setInt(4, day);
            pstmt.setString(5, member);

            pstmt.executeUpdate();
            System.out.println("Slot inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
        return true;
    }

    // Update slot when a user books it
    public boolean updateSlot(String trainerName, String memberName, int time, int day) {
        String sql = "UPDATE bookings SET booked = ?, member = ? WHERE LOWER(trainer) = LOWER(?) AND time = ? AND day = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, 1); // Use 1 for 'yes' instead of a string
            pstmt.setString(2, memberName);
            pstmt.setString(3, trainerName);
            pstmt.setInt(4, time);
            pstmt.setInt(5, day);

            int updatedRows = pstmt.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Slot updated successfully.");
                return true;
            } else {
                System.out.println("No matching slot found for the given trainer, time, and day.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    // Get all bookings for a trainer
    public List<Booking> getBookingsByTrainer(String trainerName) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT booked, time, day, member FROM bookings WHERE LOWER(trainer) = LOWER(?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trainerName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("time"),
                        rs.getInt("day"),
                        rs.getString("member"),
                        rs.getInt("booked") == 1 ? "yes" : "no"
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.out.println("Retrieval failed: " + e.getMessage());
        }

        return bookings;
    }

    // Get all confirmed bookings
    public List<Booking> getConfirmedBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT trainer, time, day FROM bookings WHERE booked = 1";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getString("trainer"),
                        rs.getInt("time"),
                        rs.getInt("day"),
                        "yes"
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.out.println("Retrieval failed: " + e.getMessage());
        }

        return bookings;
    }

    // Get all available (unbooked) slots
    public List<Booking> getAllAvailableBookings() {
        List<Booking> availableBookings = new ArrayList<>();
        String sql = "SELECT trainer, time, day FROM bookings WHERE booked = 0"; // 0 represents "no"

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getString("trainer"),
                        rs.getInt("time"),
                        rs.getInt("day")
                );
                availableBookings.add(booking);
            }
        } catch (SQLException e) {
            System.out.println("Retrieval failed: " + e.getMessage());
        }

        return availableBookings;
    }

    // Get all bookings made by a specific member
    public List<Booking> getBookingsByMember(String memberName) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT trainer, time, day FROM bookings WHERE LOWER(member) = LOWER(?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, memberName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getString("trainer"),
                        rs.getInt("time"),
                        rs.getInt("day")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.out.println("Retrieval failed: " + e.getMessage());
        }

        return bookings;
    }












}
