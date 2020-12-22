package sample.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.ClientChat;
import sample.Network;

import java.io.IOException;

public class Controller {
    @FXML
    private ListView<String> usersList;

    @FXML
    private TextArea chatField;

    @FXML
    private TextField textField;

    @FXML
    private Button sendButton;
    private Network network;

    @FXML
    public void initialize(){
        usersList.setItems(FXCollections.observableArrayList(ClientChat.USERS));
    }

    @FXML
    private void sendMessage() {
        String message = textField.getText();
        appendMessage("Я: " + message);
        textField.clear();

        try {
            network.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            String errorMessage = "Failed to send message";
            ClientChat.showNetworkError(e.getMessage(), errorMessage);
        }
    }
    public void setNetwork(Network network) {
        this.network = network;
    }

    public void appendMessage(String message) {
        chatField.appendText(message);
        chatField.appendText(System.lineSeparator());
    }
}
