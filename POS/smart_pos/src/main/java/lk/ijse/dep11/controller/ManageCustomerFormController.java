package lk.ijse.dep11.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.db.CustomerDataAccess;
import lk.ijse.dep11.db.OrderDataAccess;
import lk.ijse.dep11.tm.Customer;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;

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
        try {
            tblCustomers.getItems().addAll(CustomerDataAccess.getAllCustomer());
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load customers, try again");
        }
        tblCustomers.getSelectionModel().selectedItemProperty().addListener((ov, prev, cur)->{
            if (cur != null){
                btnSave.setText("UPDATE");
                btnDelete.setDisable(false);
                txtCustomerId.setText(cur.getId());
                txtName.setText(cur.getName());
                txtAddress.setText(cur.getAddress());
            }else {
                btnSave.setText("SAVE");
                btnDelete.setDisable(true);
            }
        });
        Platform.runLater(txtName::requestFocus);


    }

    public void btnNewCustomerOnAction(ActionEvent event) throws IOException {
        for (TextField textField : new TextField[]{txtCustomerId,txtName,txtAddress})
            textField.clear();
        tblCustomers.getSelectionModel().clearSelection();
        txtName.requestFocus();
        try{
            String lastCustomerId = CustomerDataAccess.getLastCustomerId();
            if (lastCustomerId == null){
                txtCustomerId.setText("C001");
            }else {
                int newId = Integer.parseInt(lastCustomerId.substring(1)) + 1;
                txtCustomerId.setText(String.format("C%03d" , newId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to establish the database connection, try again");
            navigateToHome(null);

        }
    }


    public void btnSaveOnAction(ActionEvent event) {
        if (!isDataValid()) return;

        Customer customer = new Customer(txtCustomerId.getText(),
                txtName.getText().strip(),txtAddress.getText().strip());
        try{
            if (btnSave.getText().equals("SAVE")){
                CustomerDataAccess.saveCustomer(customer);
                tblCustomers.getItems().add(customer);
            }else {
                CustomerDataAccess.updateCustomer(customer);
                ObservableList<Customer> customerList = tblCustomers.getItems();
                Customer selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
                customerList.set(customerList.indexOf(selectedCustomer),customer);
                tblCustomers.refresh();

            }
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to save the customer, please try again").show();
        }
    }

    private boolean isDataValid() {
        String name = txtName.getText().strip();
        String address = txtAddress.getText().strip();

        if (!name.matches("[A-Za-z ]{2,}")){
            txtName.requestFocus();
            txtName.selectAll();
            return false;
        }else if(address.length()<3){
            txtAddress.requestFocus();
            txtAddress.selectAll();
            return false;
        }
        return true;
    }

    public void btnDeleteOnAction(ActionEvent event) {
        try{
            Object OrdeDataAccess;
            if (OrderDataAccess.existOrderByCustomerId(txtCustomerId.getText())){
                new Alert(Alert.AlertType.ERROR,
                        "Unable to delete this customer, already associated with an order").show();
            }else {
                CustomerDataAccess.deleteCustomer(txtCustomerId.getText());
                ObservableList<Customer> customerList = tblCustomers.getItems();
                Customer selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
                customerList.remove(selectedCustomer);
                if(customerList.isEmpty()) btnNewCustomer.fire();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
