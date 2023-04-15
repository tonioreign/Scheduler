package sample.misc;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/** This class is used to change the screen.
 *
 * @author Antonio Jenkins
 * */
public abstract class AccessMethod {

    private static Stage stage;
    private static Scene scene;

    /** This method is used to change the screen.
     *
     * @param event The event that is passed in.
     * @param newScene The new scene that is passed in.
     * @param title The title of the new scene.
     * @throws IOException Throws an exception if the file is not found.
     * */
    public static void changeScreen(ActionEvent event, String newScene, String title) throws IOException {
        Parent root = FXMLLoader.load(AccessMethod.class.getResource("../view/" + newScene));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
