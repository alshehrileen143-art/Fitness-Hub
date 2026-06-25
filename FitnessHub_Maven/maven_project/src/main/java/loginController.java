import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {
    UserDAO userDAO = new UserDAO();

    @FXML
    private Button cancelButton;
    @FXML
    private Button RegUserButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField enterPasswordField;
    @FXML
    private RadioButton memberOption;
    @FXML
    private RadioButton trainerOption;
    @FXML
    private RadioButton adminOption;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginMessageLabel.setText("") ;
        File brandingFile = new File("Images/1.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("Images/llock.jpg");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }
    public void loginButtonOnAction(ActionEvent event) {
        if (!usernameTextField.getText().isBlank() &&
                !enterPasswordField.getText().isBlank() &&
                (adminOption.isSelected() || memberOption.isSelected() || trainerOption.isSelected())) {

            validateLogin(usernameTextField.getText(),enterPasswordField.getText() ,adminOption.isSelected() ,memberOption.isSelected() ,trainerOption.isSelected());
        } else {
            loginMessageLabel.setText("Please enter username, password, and select a role");
        }
    }



    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin(String username, String password, Boolean admin, Boolean client, Boolean trainer) {
        System.out.println(username);
        System.out.println(password);
        System.out.println(trainer);
        System.out.println(client);
        System.out.println(admin);

        boolean isValid = userDAO.validateUser(username, password, trainer, client, admin);
        if (isValid) {
            member newMember = new member();
            trainer trainer1 = new trainer();
            if(client) {
                member.setUsername(username);
            }else if(trainer){
                trainer1.setUsername(username);
            }


            loginMessageLabel.setText("Login Successful");

            try {
                Stage currentStage = (Stage) loginMessageLabel.getScene().getWindow(); // Get current stage
                currentStage.close(); // Close the login window

                Stage newStage = new Stage(); // Create new stage
                Parent root;

                if (admin) {
                    root = FXMLLoader.load(getClass().getResource("admin.fxml"));
                    newStage.setTitle("Admin Dashboard");
                } else if (trainer) {
                    root = FXMLLoader.load(getClass().getResource("trainer.fxml"));
                    newStage.setTitle("Trainer Dashboard");
                } else { // Assume client
                    root = FXMLLoader.load(getClass().getResource("member.fxml"));
                    newStage.setTitle("Member Dashboard");
                }

                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            loginMessageLabel.setText("Login Failed");
        }
        System.out.println("Login result: " + isValid);
    }

    public void RegUser(ActionEvent event) {
        try {
            // Close the current login window
            Stage loginStage = (Stage) RegUserButton.getScene().getWindow();
            loginStage.close();

            // Load and open the register window
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register.fxml"));
            Parent registerRoot = fxmlLoader.load();

            Stage registerStage = new Stage();
            registerStage.setTitle("Register");
            registerStage.setScene(new Scene(registerRoot));
            registerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}