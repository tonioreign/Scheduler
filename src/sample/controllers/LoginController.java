package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.models.User;
import sample.utils.DBUser;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class that provides control logic for the login form of the application.
 *
 * @author Antonio Jenkins
 */

public class LoginController implements Initializable {
    /**The login main plane*/
    @FXML
    private AnchorPane LoginAnchor;
    /**The login user field*/
    @FXML
    private TextField UserField;
    /**The login title*/
    @FXML
    private Label LoginLabel;
    /**The login username label*/
    @FXML
    private Label UserLabel;
    /**The login password field*/
    @FXML
    private PasswordField PassField;
    /**The login password label*/
    @FXML
    private Label PassLabel;
    /**The login button*/
    @FXML
    private Button LoginButton;
    /**The login exit button*/
    @FXML
    private Button ExitButton;
    /**The login time zone label*/
    @FXML
    private Label TimeLabel;
    /**Setting the login time zone label*/
    @FXML
    private Label SetTimeLabel;

    Parent parent;
    Stage stage;
    Scene scene;

    /**Button that exits the login form - closes the program*/
    @FXML
    void onExitButton(ActionEvent event) {
        System.exit(0);
    }

    /**Button that opens " " form after confirmation of the login details*/
    @FXML
    void onLoginButton(ActionEvent event) throws IOException {
        String userName = UserField.getText();
        String password = PassField.getText();
        int Id = DBUser.validateUser(userName, password);
        if(Id!=-1){
            Parent root = FXMLLoader.load(getClass().getResource("../view/MainMenu.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
    /**Resets username and password field to empty*/
    @FXML
    void onReset(ActionEvent event){
        UserField.setText("");
        PassField.setText("");
    }

    /***/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
