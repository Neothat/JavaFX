package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;

public class EchoClient extends Application {

    public static final List<String> USERS = List.of("Oleg", "Erlan", "Denis");

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(EchoClient.class.getResource("sample.fxml"));

        AnchorPane root = loader.load();
        primaryStage.setTitle("Messenger");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Network network = new Network();
        if (!network.connect()) {
            showNetworkError("", "Failed to connect to server");
        }
        Controller controller = loader.getController();
        controller.setNetwork(network);

        network.waitMessages(controller);

        primaryStage.setOnCloseRequest(event ->
                network.close()
        );
    }

    public static void showNetworkError(String errorDetails, String errorTitle) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Network Error");
        alert.setHeaderText(errorTitle);
        alert.setContentText(errorDetails);
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
