package lk.ijse.dep11.controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.tm.Customer;
import lk.ijse.dep11.tm.Item;
import lk.ijse.dep11.tm.OrderItem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PlaceOrderFormController {
    public ImageView imgHome;
    public TextField txtName;
    public TextField txtDescription;
    public TextField txtQtyOnHand;
    public TextField txtUnitPrice;
    public TextField txtQty;
    public Button btnAdd;
    public TableView<OrderItem> tblOrder;
    public Button btnOrder;
    public AnchorPane root;
    public ComboBox<Customer> cmbCustomerId;
    public ComboBox<Item> cmbItemCode;
    public Label lblDate;
    public Label lblTotal;
    public Label lblOrderId;

    public void initialize(){
        String[] cols = {"code", "description" ,"qty", "unitPrice", "total", "btnDelete"};
        for (int i = 0; i< cols.length;i++){
            tblOrder.getColumns().get(i).setCellValueFactory(new PropertyValueFactory<>(cols[i]));
        }
        lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        newOrder();

    }

    private void newOrder() {
        for (TextField txt: new TextField[]{txtName, txtDescription, txtQty, txtQtyOnHand, txtUnitPrice}){
            txt.clear();
            txt.setDisable(true);
        }

        tblOrder.getItems().clear();
        lblTotal.setText("Total: Rs. 0.00");
        btnAdd.setDisable(true);
        btnOrder.setDisable(true);


    }


    public void btnAddOnAction(ActionEvent event) {
    }

    public void btnOrderOnAction(ActionEvent event) {
    }



}
