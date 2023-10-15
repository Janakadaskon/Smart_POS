package lk.ijse.dep11.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private ImageView imgCargills;
    @FXML
    private ImageView imgCustomer;
    @FXML
    private Label lblWelcome;

    @FXML
    private Label lblOn;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000),root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }




}

