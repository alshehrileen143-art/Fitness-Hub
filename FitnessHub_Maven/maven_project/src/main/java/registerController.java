import javafx.animation.PauseTransition;
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
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class registerController implements Initializable {
    UserDAO userDAO = new UserDAO();
    @FXML
    private Button CloseRegisterButton;
    @FXML
    private Label registerLabelMessage;
    @FXML
    private ImageView brandingImage;
    @FXML
    private TextField newUsernameField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField ConfirmPassField;
    @FXML
    private RadioButton trainerRadioButton;
    @FXML
    private RadioButton memberRadioButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerLabelMessage.setText("") ;
        File BrandingFile = new File("Images/1.jpg");
        Image BrandingImage = new Image(BrandingFile.toURI().toString());
        brandingImage.setImage(BrandingImage);
    }
    // we will change this in future
    public void cancelButtonOnAction(ActionEvent event) {
        try {
            // Close the current register window
            Stage stage = (Stage) CloseRegisterButton.getScene().getWindow();
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

    public void registerButtonOnAction(ActionEvent event) {
        if (!newUsernameField.getText().isBlank() &&
                !newPasswordField.getText().isBlank() && !ConfirmPassField.getText().isBlank() &&
                (memberRadioButton.isSelected() || trainerRadioButton.isSelected()) && newPasswordField.getText().equals(ConfirmPassField.getText())) {

            RegisterUser(newUsernameField.getText(),newPasswordField.getText() ,memberRadioButton.isSelected() ,trainerRadioButton.isSelected());
        } else {
            registerLabelMessage.setText("Invalid registration");
        }

    }
    public void RegisterUser(String username, String password, Boolean client, Boolean trainer) {
        boolean admin = false;
        boolean reg = userDAO.insertUser(username, password, trainer, client, admin);
        boolean meb = userDAO.insertmember(username);
        System.out.println("Member inserted : "+ meb);

        if (reg) {
            registerLabelMessage.setText("User registered successfully");

            // Pause for 2 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                try {
                    // Close the register window
                    Stage registerStage = (Stage) registerLabelMessage.getScene().getWindow();
                    registerStage.close();

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
            });
            pause.play();
        } else {
            registerLabelMessage.setText("User not registered");
        }
    }
}
