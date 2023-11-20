package lk.ijse.dep11.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.tm.Customer;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class ManageCustomerFormController {

    public AnchorPane root;
    public Button btnNewCustomer;
    public TextField txtCustomerId;
    public TextField txtName;
    public TextField txtAddress;
    public Button btnSave;
    public Button btnDelete;
    public ImageView imgHome;
    public TableView<Customer> tblCustomers;


    public void navigateToHome(javafx.scene.input.MouseEvent mouseEvent)throws IOException{
        MainFormController.navigateToMain(root);
    }

    public void initialize() {
        tblCustomers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomers.getColumns().get(0).setStyle("-fx-alignment: center;");
        tblCustomers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        txtCustomerId.setEditable(false);
        btnDelete.setDisable(true);
        btnSave.setDefaultButton(true);
        btnNewCustomer.fire();


    }

    public void btnNewCustomerOnAction(ActionEvent event) {

    }

    public void btnSaveOnAction(ActionEvent event) {
    }

    public void btnDeleteOnAction(ActionEvent event) {
    }


}
