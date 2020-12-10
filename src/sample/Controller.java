package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private ListView<String> usersList;

    @FXML
    private TextArea chatField;

    @FXML
    private TextField textField;

    @FXML
    private Button sendButton;

    @FXML
    public void initialize(){
        usersList.setItems(FXCollections.observableArrayList(Main.USERS));
    }
    
    @FXML
    private void sendMessage(ActionEvent actionEvent) {
        chatField.appendText(textField.getText());
        chatField.appendText(System.lineSeparator());
        textField.clear();
    }
}
