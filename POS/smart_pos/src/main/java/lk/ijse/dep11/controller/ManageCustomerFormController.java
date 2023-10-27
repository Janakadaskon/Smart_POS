package lk.ijse.dep11.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class ManageCustomerFormController {

    public AnchorPane root;
    public Button btnNewCustomer;
    public TextField txtID;
    public TextField txtName;
    public TextField txtAddress;
    public Button btnSave;
    public Button btnDelete;
    public TableView tblCusomer;


    public void navigateToHome(javafx.scene.input.MouseEvent mouseEvent)throws IOException{
        MainFormController.navigateToMain(root);
    }

    public void btnNewCustomerOnAction(ActionEvent event) {
    }

    public void btnSaveOnAction(ActionEvent event) {
    }

    public void btnDeleteOnAction(ActionEvent event) {
    }


}
