import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class trainerController implements Initializable {
    UserDAO userDAO = new UserDAO();
    trainer trainer1 = new trainer();

    @FXML
    private TextField usernameField;
    @FXML
    private TextField workoutField;
    @FXML
    private TextField enterTimeField;
    @FXML
    private TextField enterDayField;
    @FXML
    private Button addNewWorkout;
    @FXML
    private Button trainerLogout;
    @FXML
    private Label workoutLabel;
    @FXML
    private Label createSlotLabel;
    @FXML
    private TableView<Booking> slotBookingTable;

    @FXML
    private TableColumn<Booking, Integer> timeColumn;

    @FXML
    private TableColumn<Booking, Integer> dayColumn;

    @FXML
    private TableColumn<Booking, String> memberColumn;
    @FXML
    private TableView<String> membersListTable;
    @FXML
    private TableColumn<String, String> memberNameColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        memberColumn.setCellValueFactory(new PropertyValueFactory<>("member"));
        memberNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

        // Load members into the table
        loadMembers();

        // Fetch trainer name (you might get it from session or pass it in)
        String trainerName = trainer.getUsername(); // Change this dynamically as needed

        // Load bookings from database
        loadBookings(trainerName);
         workoutLabel.setText("");
         createSlotLabel.setText("");
    }
    private void loadMembers() {
        List<String> memberNames = userDAO.getMembersName();
        ObservableList<String> observableList = FXCollections.observableArrayList(memberNames);
        membersListTable.setItems(observableList);
    }
    private void loadBookings(String trainerName) {
        List<Booking> bookingList = userDAO.getBookingsByTrainer(trainerName);
        ObservableList<Booking> observableList = FXCollections.observableArrayList(bookingList);
        slotBookingTable.setItems(observableList);
    }

    public void createSlotButtonOnAction(javafx.event.ActionEvent event) {
        try {
            // Get text inputs
            String timeText = enterTimeField.getText().trim();
            String dayText = enterDayField.getText().trim();

            // Check if fields are empty before parsing
            if (timeText.isBlank() || dayText.isBlank()) {
                createSlotLabel.setText("Fields cannot be empty!");
                return;
            }

            // Parse input values
            int time = Integer.parseInt(timeText);
            int day = Integer.parseInt(dayText);

            // Validate time and day range
            if (time >= 0 && time <= 23 && day >= 1 && day <= 7) {
                boolean check = userDAO.insertSlot(trainer.getUsername(), time, day);

                if (check) {
                    createSlotLabel.setText("Slot Added Successfully!");

                    // Clear input fields
                    enterTimeField.clear();
                    enterDayField.clear();

                    // Reload updated bookings
                    loadBookings(trainer.getUsername());
                } else {
                    createSlotLabel.setText("Failed to Add Slot.");
                }
            } else {
                createSlotLabel.setText("Invalid time (0-23) or day (1-7).");
            }
        } catch (NumberFormatException e) {
            createSlotLabel.setText("Please enter valid numbers.");
        }
    }


    public void addWorkoutButtonOnAction(javafx.event.ActionEvent actionEvent) {

        if(!workoutField.getText().isBlank() && !usernameField.getText().isBlank()){
            boolean check = userDAO.insertWorkout(usernameField.getText(), workoutField.getText());
            if(check){
                workoutLabel.setText("Workout Added");
                workoutField.setText("");
                usernameField.setText("");

            }else{
                workoutLabel.setText("Workout Not Added");
            }



        }
    }



    public void trainerLogoutButtonOnAction(ActionEvent actionEvent) {
        try {
            // Close the current register window
            Stage stage = (Stage) trainerLogout.getScene().getWindow();
            stage.close();

            // Open the login window
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent loginRoot = fxmlLoader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(loginRoot));
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
