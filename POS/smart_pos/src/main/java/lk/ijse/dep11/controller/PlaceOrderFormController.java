package lk.ijse.dep11.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.db.CustomerDataAccess;
import lk.ijse.dep11.db.OrderDataAccess;
import lk.ijse.dep11.tm.Customer;
import lk.ijse.dep11.tm.Item;
import lk.ijse.dep11.tm.OrderItem;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class PlaceOrderFormController {

    public AnchorPane root;
    public ImageView imgHome;
    public TextField txtName;
    public TextField txtDescription;
    public TextField txtQtyOnHand;
    public TextField txtUnitPrice;
    public TextField txtQty;
    public Button btnAdd;
    public TableView<OrderItem> tblOrder;
    public Button btnOrder;

    public ComboBox<Customer> cmbCustomerId;
    public ComboBox<Item> cmbItemCode;
    public Label lblDate;
    public Label lblTotal;
    public Label lblOrderId;

    public void initialize() throws IOException {
        String[] cols = {"code", "description" ,"qty", "unitPrice", "total", "btnDelete"};
        for (int i = 0; i< cols.length; i++){
            tblOrder.getColumns().get(i).setCellValueFactory(new PropertyValueFactory<>(cols[i]));
        }
        lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        newOrder();
        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((ov, prev, cur) -> {
            if (cur != null){
                txtName.setText(cur.getName());
                txtName.setDisable(false);
                txtName.setEditable(false);
            }else {
                txtName.clear();
                txtName.setDisable(true);
            }
        });
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((ov, prev, cur) -> {
            if (cur != null){
                txtDescription.setText(cur.getDescription());
                txtQtyOnHand.setText(cur.getQty() + "");
                txtUnitPrice.setText(cur.getUnitPrice().toString());

                for (TextField txt : new TextField[]{txtDescription, txtQtyOnHand, txtUnitPrice}){
                    txt.setDisable(false);
                    txt.setEditable(false);
                }
                txtQty.setDisable(cur.getQty() == 0);
            }else {
                for (TextField txt : new TextField[]{txtDescription, txtQtyOnHand, txtUnitPrice, txtQty}){
                    txt.setDisable(true);
                    txt.clear();
                }
            }
        });

        txtQty.textProperty().addListener((ov, prevQty, curQty) -> {
            Item selectedItem = cmbItemCode.getSelectionModel().getSelectedItem();
            btnAdd.setDisable(!(curQty.matches("\\d+") && Integer.parseInt(curQty) <= selectedItem.getQty()
                    && Integer.parseInt(curQty) > 0));
        });
    }

    private void newOrder() throws IOException{
        for (TextField txt: new TextField[]{txtName, txtDescription, txtQty, txtQtyOnHand, txtUnitPrice}){
            txt.clear();
            txt.setDisable(true);
        }

        tblOrder.getItems().clear();
        lblTotal.setText("Total: Rs. 0.00");
        btnAdd.setDisable(true);
        btnOrder.setDisable(true);
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbItemCode.getSelectionModel().clearSelection();
        try {
            cmbCustomerId.getItems().clear();
            cmbCustomerId.getItems().addAll(CustomerDataAccess.getAllCustomer());
            cmbItemCode.getItems().clear();
            cmbItemCode.getSelectionModel().clearSelection();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to establish database connection, try again");
            e.printStackTrace();
            navigateToHome(null);
        }
        Platform.runLater(cmbCustomerId::requestFocus);


    }


    public void btnAddOnAction(ActionEvent event) {
        Item selectedItem = cmbItemCode.getSelectionModel().getSelectedItem();
        Optional<OrderItem> optOrderItem = tblOrder.getItems().stream()
                .filter(item -> selectedItem.getCode().equals(item.getCode())).findFirst();

        if (optOrderItem.isEmpty()){
            JFXButton btnDelete = new JFXButton("Delete");
            OrderItem newOrderItem = new OrderItem(selectedItem.getCode(), selectedItem.getDescription(),
                    Integer.parseInt(txtQty.getText()), selectedItem.getUnitPrice(), btnDelete);
            tblOrder.getItems().add(newOrderItem);
            btnDelete.setOnAction(e -> {
                tblOrder.getItems().remove(newOrderItem);
                selectedItem.setQty(selectedItem.getQty() + newOrderItem.getQty());
                calculateOrderTotal();
            });
            selectedItem.setQty(selectedItem.getQty() + newOrderItem.getQty());
        }else {
            OrderItem orderItem = optOrderItem.get();
            orderItem.setQty(orderItem.getQty() + Integer.parseInt(txtQty.getText()));
            tblOrder.refresh();
            selectedItem.setQty(selectedItem.getQty() - Integer.parseInt(txtQty.getText()));
        }
        cmbItemCode.getSelectionModel().clearSelection();
        cmbItemCode.requestFocus();
        calculateOrderTotal();

    }

    private void calculateOrderTotal() {
        Optional<BigDecimal> orderTotal = tblOrder.getItems().stream()
                .map(oi -> oi.getTotal())
                .reduce((prev, cur) -> prev.add(cur));
        lblTotal.setText("Total: Rs. " + orderTotal.orElseGet(() -> BigDecimal.ZERO).setScale(2));
    }

    public void btnOrderOnAction(ActionEvent event)throws IOException {
        try {
            OrderDataAccess.saveOrder
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to save the order, try again");
        }
    }

    public void navigateToHome(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        MainFormController.navigateToMain(root);
    }
}
