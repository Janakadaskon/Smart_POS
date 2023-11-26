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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.db.ItemDataAccess;
import lk.ijse.dep11.db.OrderDataAccess;
import lk.ijse.dep11.tm.Item;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class ManageItemFormController {
    public AnchorPane root;
    public ImageView imgHome;
    public TextField txtCode;
    public TextField txtDescription;
    public TextField txtQtyOnHand;
    public TextField txtUnitPrice;
    public Button btnSave;
    public Button btnDelete;
    public Button btnNewItem;
    public TableView<Item> tblItem;

    public void initialize(){
        tblItem.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblItem.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItem.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblItem.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        btnDelete.setDisable(true);
        btnSave.setDefaultButton(true);
        try {
            tblItem.getItems().addAll(ItemDataAccess.getAllItems());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load items, try again");
            e.printStackTrace();
        }
        Platform.runLater(txtCode::requestFocus);
        tblItem.getSelectionModel().selectedItemProperty().addListener((ov, prev, cur)->{
            if (cur == null){
                btnSave.setText("Save");
                btnDelete.setDisable(true);
                txtCode.setDisable(false);
            }else {
                btnSave.setText("Update");
                btnDelete.setDisable(false);
                txtCode.setText(cur.getCode());
                txtCode.setDisable(true);
                txtDescription.setText(cur.getDescription());
                txtQtyOnHand.setText(cur.getQty() + "");
                txtUnitPrice.setText(cur.getUnitPrice() + "");
            }
        });

    }



    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        MainFormController.navigateToMain(root);
    }
    public void btnNewItemOnAction(ActionEvent event) {
        for (TextField textField: new TextField[]{txtCode, txtDescription, txtQtyOnHand, txtUnitPrice})
            textField.clear();
        txtCode.requestFocus();
        tblItem.getSelectionModel().clearSelection();
    }

    private boolean isDataValid(){
        String code = txtCode.getText().strip();
        String description = txtDescription.getText().strip();
        String qty = txtQtyOnHand.getText().strip();
        String unitPrice = txtUnitPrice.getText().strip();

        if (!code.matches("\\d{4,}")){
            txtCode.requestFocus();
            txtCode.selectAll();
            return false;
        }else if (!description.matches("[A-Za-z0-9]{4,}")){
            txtDescription.requestFocus();
            txtDescription.selectAll();
            return false;
        }else if (!qty.matches("\\d+") || Integer.parseInt(qty) <= 0){
            txtQtyOnHand.requestFocus();
            txtQtyOnHand.selectAll();
            return false;
        }else if(!isPrice(unitPrice)){
            txtUnitPrice.requestFocus();
            txtUnitPrice.selectAll();
            return false;
        }
        return true;
    }

    private boolean isPrice(String input) {
        try{
            double price = Double.parseDouble(input);
            return price > 0;
        }catch (NumberFormatException e){
            return false;
        }
    }

    public void btnSaveOnAction(ActionEvent event) {
        if (!isDataValid()) return;

        try {
            Item item = new Item(txtCode.getText().strip(), txtDescription.getText().strip(),
                    Integer.parseInt(txtQtyOnHand.getText()), new BigDecimal(txtUnitPrice.getText()).setScale(2));

            if (btnSave.getText().equals("Save")){
                if (ItemDataAccess.existsItem(item.getCode())){
                    new Alert(Alert.AlertType.ERROR, "Item code already exists").show();
                    txtCode.requestFocus();
                    txtCode.selectAll();
                    return;
                }
                ItemDataAccess.saveItem(item);
                tblItem.getItems().add(item);
            }else {
                ItemDataAccess.updateItem(item);
                ObservableList<Item> itemList = tblItem.getItems();
                Item selectedItem = tblItem.getSelectionModel().getSelectedItem();
                itemList.set(itemList.indexOf(selectedItem), item);
                tblItem.refresh();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save the item, try again").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent event) {
        Item selectedItem = tblItem.getSelectionModel().getSelectedItem();
        try {
            if (OrderDataAccess.existsOrderByItemCode(selectedItem.getCode())){
                new Alert(Alert.AlertType.ERROR, "Failed to delete, item already associate with an order").show();
            }else {
                ItemDataAccess.deleteItems(selectedItem.getCode());
                tblItem.getItems().remove(selectedItem);
                if (tblItem.getItems().isEmpty()) btnNewItem.fire();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to delete, try again").show();
        }
    }


}
