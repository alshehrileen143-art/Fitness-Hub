import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
public class adminController implements Initializable {
    UserDAO userDAO = new UserDAO();
    memberships m = new memberships();
    // 2 buttons
    @FXML
    private Button addMembershipButton;
//    @FXML
//    private Button exportButton;
    @FXML
    private Button adminlogoutButton;
    @FXML
    private Button markStatusButton;
    @FXML
    private TextField usernameStatusField;



    // all the labels
//    @FXML
//    private Label exportLabel;
    @FXML
    private Label totalRegistrationsField;
    @FXML
    private Label totalMembersField;
    @FXML
    private Label fillDetailsLabel;
    @FXML
    private Label fillStatusLabel;



    // background image
    @FXML
    private ImageView backgroundImage;

    // ALL the text fields
    @FXML
    private TextField membershipTypeField;
    @FXML
    private TextField DurationField;
    @FXML
    private TextField PriceField;
    @FXML
    private TextField FeaturesField;

    // making table view
    @FXML
    private TableView<Membership> tableView ;
    @FXML
    private TableColumn<Membership, String> membershipTypeColumn;
    @FXML
    private TableColumn<Membership, String> durationColumn;
    @FXML
    private TableColumn<Membership, Integer> priceColumn;
    @FXML
    private TableColumn<Membership, String> featuresColumn;

    private void loadMemberships() {
        List<Membership> membershipList = m.getAllMemberships();
        ObservableList<Membership> observableMemberships = FXCollections.observableArrayList(membershipList);
        tableView.setItems(observableMemberships);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillDetailsLabel.setText("");
//        exportLabel.setText("");
        fillStatusLabel.setText("");
//
//        File BackgroundFile = new File("Images/2.jpg");
//        Image BackgroundImage = new Image(BackgroundFile.toURI().toString());
//        backgroundImage.setImage(BackgroundImage);
        // we will set the labels in here
        totalMembersField.setText(String.valueOf(userDAO.getMemberCount()));
        totalRegistrationsField.setText(String.valueOf(userDAO.getTotalUserCount()));
        // we will set the table in here
        // Set up TableView columns
        membershipTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMembershipType()));
        durationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDuration()));
        priceColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPrice()).asObject());
        featuresColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFeatures()));

        // Load data into the table
        loadMemberships();
    }
    public void markStatusButtonAction(ActionEvent event) {
        if(!usernameStatusField.getText().isBlank()){
            boolean check = userDAO.updateFee(usernameStatusField.getText());
            if(check){
                fillStatusLabel.setText("Fee Status update successfully");
                usernameStatusField.setText("");

            }else{
                fillStatusLabel.setText("unable to update fee status");

            }

        }else{
            fillStatusLabel.setText("Above field can't be empty");
        }
    }

    public void addMembership(ActionEvent actionEvent) {
        if (!membershipTypeField.getText().isBlank() && !DurationField.getText().isBlank() && !PriceField.getText().isBlank() && !FeaturesField.getText().isBlank()) {
            boolean check = m.addMemberships(membershipTypeField.getText(), DurationField.getText(), Integer.valueOf(PriceField.getText()), FeaturesField.getText());
            if (check) {
                membershipTypeField.setText("");
                DurationField.setText("");
                PriceField.setText("");
                FeaturesField.setText("");

            }
            fillDetailsLabel.setText("Membership inserted successfully");
            loadMemberships();

        } else {
            fillDetailsLabel.setText("Cant add membership");
        }
    }
    public void adminLogoutButtonOnAction(ActionEvent actionEvent) {
        try {
            // Close the current register window
            Stage stage = (Stage) adminlogoutButton.getScene().getWindow();
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

//    public void exportButtonOnAction(ActionEvent actionEvent) {
//        boolean check = userDAO.exportMembersToCSV();
//        if (check) {
//            exportLabel.setText("Successfully exported");
//
//        } else {
//            exportLabel.setText("Failed to export");
//        }
//
//
//    }

}