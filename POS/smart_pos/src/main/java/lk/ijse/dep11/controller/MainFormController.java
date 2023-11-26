package lk.ijse.dep11.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.dep11.AppInitializer;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {
    public ImageView imgCustomer;
    public ImageView imgStock;
    public ImageView imgSearch;
    public ImageView imgOrder;
    @FXML
    private AnchorPane root;
    @FXML
    private Label lblMenu;
    @FXML
    private Label lblDescription;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000),root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    @FXML
    private void playMouseExitAnimation(javafx.scene.input.MouseEvent event){
        if (event.getSource() instanceof ImageView){
            ImageView icon =(ImageView) event.getSource();
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), icon);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();

            icon.setEffect(null);
            lblMenu.setText("Welcome..!");
            lblDescription.setText("Please select one of above Option to proceed");

        }
    }


    @FXML
    private void playMouseEnterAnimation(javafx.scene.input.MouseEvent event) {
        if (event.getSource() instanceof ImageView){
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()){
                case "imgCustomer":
                    lblMenu.setText("Manage Customers");
                    lblDescription.setText("Click to add ,edit ,delete ,search or view Customers");
                    break;

                case "imgStock":
                    lblMenu.setText("Manage Stock");
                    lblDescription.setText("Click to add ,edit ,delete ,search or view Items");
                    break;

                case "imgOrder":
                    lblMenu.setText("Manage Orders");
                    lblDescription.setText("Click here to place a Order");
                    break;

                case "imgSearch":
                    lblMenu.setText("Search Items");
                    lblDescription.setText("Click here to search items");
                    break;
            }
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200),icon);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }

    @FXML
    private void navigate(javafx.scene.input.MouseEvent event) throws IOException{
        if (event.getSource() instanceof ImageView){
            ImageView icon = (ImageView) event.getSource();

            Parent root = null;

            switch (icon.getId()){
                case "imgCustomer":
                    root = FXMLLoader.load(this.getClass().getResource("/view/ManageCustomerForm.fxml"));
                    break;
                case "imgStock":
                    root = FXMLLoader.load(this.getClass().getResource("/view/ManageItemForm.fxml"));
                    break;
                case "imgOrder":
                    root = FXMLLoader.load(this.getClass().getResource("/view/PlaceOrderForm.fxml"));
                    break;
                case "imgSearch":
                    root = FXMLLoader.load(this.getClass().getResource("/view/SearchOrderForms.fxml"));
                    break;
            }

            if (root != null){

                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setResizable(true);
                primaryStage.setScene(subScene);
                primaryStage.sizeToScene();
                primaryStage.centerOnScreen();
                primaryStage.setOnCloseRequest(Event::consume);

                TranslateTransition tt = new TranslateTransition(Duration.millis(250), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();

                Platform.runLater(()-> primaryStage.setResizable(false));
            }

        }
    }

    public static void navigateToMain(Node rootNode) throws IOException {
        Parent root = FXMLLoader.load(AppInitializer.class.getResource("/view/MainForm.fxml"));
        Scene scene = new Scene(root);
        Stage primaryStage =(Stage) (rootNode.getScene().getWindow());
        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(null);
        Platform.runLater(()-> primaryStage.setResizable(false));
    }



}




