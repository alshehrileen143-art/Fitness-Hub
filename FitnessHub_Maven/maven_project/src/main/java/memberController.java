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

public class memberController implements Initializable {
    UserDAO userDAO = new UserDAO();
    member member = new member();

    @FXML
    private TableView<String> workoutTableView;
    @FXML
    private TableColumn<String, String> workoutColumnView;


    @FXML
    private Label FitnessLabel;
    @FXML
    private TextField addHeightField;
    @FXML
    private TextField addWeightField;
    @FXML
    private TextField addBMIField;
    @FXML
    private Button fitnessCalculator;
    @FXML
    private Button memberLogout;
    @FXML
    private Label slotBookingLabel;
    @FXML
    private Button BookSlotButton;
    @FXML
    private TextField enterTrainerField;
    @FXML
    private TextField enterTimeField;
    @FXML
    private TextField enterDayField;
    @FXML
    private TableView<Booking> bookedTableView;
    @FXML
    private TableColumn<Booking, String> bookedTrainerColumn;
    @FXML
    private TableColumn<Booking, Integer> bookedTimeColumn;
    @FXML
    private TableColumn<Booking, Integer> bookedDayColumn;
    @FXML
    private TableView<Booking> availableTableView;

    @FXML
    private TableColumn<Booking, String> availableTrainerColumn;

    @FXML
    private TableColumn<Booking, Integer> availableTimeColumn;

    @FXML
    private TableColumn<Booking, Integer> availableDayColumn;






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load the add image
        FitnessLabel.setText("");
        slotBookingLabel.setText("");
//        File addFile = new File("Images/icons8_add_folder_32px.png");
//        Image addImage1 = new Image(addFile.toURI().toString());
//        addButtonImage.setImage(addImage1);

        // Configure the TableColumn to display String values
        workoutColumnView.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        bookedTrainerColumn.setCellValueFactory(new PropertyValueFactory<>("trainer"));
        bookedTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        bookedDayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        availableTrainerColumn.setCellValueFactory(new PropertyValueFactory<>("trainer"));
        availableTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        availableDayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));

        // Load available bookings into the table
        loadAvailableBookings();



        // Load booked slots
        loadBookedSlots(member.getUsername());
        // Load workouts into the table (replace "exampleUser" with the actual username)
        loadWorkouts(member.getUsername());
    }
    private void loadAvailableBookings() {
        List<Booking> availableBookings = userDAO.getAllAvailableBookings();
        ObservableList<Booking> bookingsObservableList = FXCollections.observableArrayList(availableBookings);
        availableTableView.setItems(bookingsObservableList);
    }
    public void BookSlotButtonAction(javafx.event.ActionEvent actionEvent) {
        try {
            // Get text inputs
            String timeText = enterTimeField.getText().trim();
            String dayText = enterDayField.getText().trim();

            // Check if fields are empty before parsing
            if (timeText.isBlank() || dayText.isBlank() || enterTrainerField.getText().isBlank()) {
                slotBookingLabel.setText("Fields cannot be empty!");
                return;
            }

            // Parse input values
            int time = Integer.parseInt(timeText);
            int day = Integer.parseInt(dayText);

            // Validate time and day range
            if (time >= 0 && time <= 23 && day >= 1 && day <= 7) {
                boolean check = userDAO.updateSlot( enterTrainerField.getText(),member.getUsername(), time, day);

                if (check) {
                    slotBookingLabel.setText("Slot Added Successfully!");

                    // Clear input fields
                    enterTimeField.clear();
                    enterDayField.clear();

                    // Reload updated bookings
                    loadBookedSlots(member.getUsername()); // Load Function in here
                    loadAvailableBookings();


                } else {
                    slotBookingLabel.setText("Failed to Add Slot.");
                }
            } else {
                slotBookingLabel.setText("Invalid time (0-23) or day (1-7).");
            }
        } catch (NumberFormatException e) {
            slotBookingLabel.setText("Please enter valid numbers.");
        }

    }
    private void loadBookedSlots(String memberName) {
        List<Booking> bookingList = userDAO.getBookingsByMember(memberName);
        ObservableList<Booking> observableList = FXCollections.observableArrayList(bookingList);
        bookedTableView.setItems(observableList);
    }

    private void loadWorkouts(String username) {
        List<String> workouts = userDAO.getWorkouts(username);
        ObservableList<String> workoutList = FXCollections.observableArrayList(workouts);
        workoutTableView.setItems(workoutList);
    }

//    public void addWorkoutButtonOnAction(javafx.event.ActionEvent actionEvent) {
//        if(!addNewTaskField.getText().isBlank()){
//            userDAO.insertWorkout(member.getUsername(), addNewTaskField.getText());
//            addNewTaskField.setText("");
//            loadWorkouts(member.getUsername());
//
//        }
//    }
    public void calculateFitnessButtonOnAction(javafx.event.ActionEvent actionEvent) {
        if(!addWeightField.getText().isBlank() && !addHeightField.getText().isBlank() && !addBMIField.getText().isBlank()){
            FitnessLabel.setText(getFitnessCondition(Double.valueOf(addHeightField.getText()) ,Double.valueOf(addWeightField.getText()),Integer.valueOf(addBMIField.getText())));
        }
    }

    public static String getFitnessCondition(double height, double weight, int bpm) {
        double bmi = weight / (height * height);
        String bmiCategory;
        String heartCategory;
        String overallCondition;

        // Determine BMI Category
        if (bmi < 18.5) bmiCategory = "Underweight";
        else if (bmi < 25) bmiCategory = "Normal";
        else if (bmi < 30) bmiCategory = "Overweight";
        else bmiCategory = "Obese";

        // Determine Heart Rate Category
        if (bpm < 60) heartCategory = "Excellent Cardiovascular Health";
        else if (bpm < 70) heartCategory = "Good Cardiovascular Health";
        else if (bpm < 80) heartCategory = "Average";
        else if (bpm < 90) heartCategory = "Below Average";
        else heartCategory = "Poor Cardiovascular Health";

        // Determine Overall Condition
        if (bmiCategory.equals("Normal") && heartCategory.contains("Excellent")) {
            overallCondition = "Athletic / Very Fit";
        } else if (bmiCategory.equals("Normal") && heartCategory.contains("Good")) {
            overallCondition = "Fit";
        } else if (bmiCategory.equals("Overweight") && heartCategory.contains("Good")) {
            overallCondition = "Moderate Fitness, Needs Weight Management";
        } else if (bmiCategory.equals("Overweight") || heartCategory.contains("Below Average")) {
            overallCondition = "Needs Improvement";
        } else {
            overallCondition = "Health at Risk - Consider Medical Consultation";
        }

        return " Condition: " + overallCondition;
    }

    public void memeberLogoutButtonOnAction(ActionEvent actionEvent) {
        try {
            // Close the current register window
            Stage stage = (Stage) memberLogout.getScene().getWindow();
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
