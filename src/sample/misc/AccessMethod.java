package sample.misc;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class AccessMethod {

    private static Stage stage;
    private static Scene scene;

    public static void changeScreen(ActionEvent event, String newScene) throws IOException {
        Parent root = FXMLLoader.load(AccessMethod.class.getResource("../view/" + newScene));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
