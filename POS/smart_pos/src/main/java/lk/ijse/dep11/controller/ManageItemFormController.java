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
    public TableView<Item> tblItems;

    public void initialize(){
        String[] colNames = {"code","description","qty","unitPrice"};
        for (int i = 0; i < colNames.length; i++) {
            tblItems.getColumns().get(i).setCellValueFactory(new PropertyValueFactory<>(colNames[i]));
        }
        btnDelete.setDisable(true);
        btnSave.setDefaultButton(true);
        txtCode.requestFocus();
        btnNewItem.fire();

        tblItems.getSelectionModel().selectedItemProperty().addListener((ov, prev, cur)->{
            if (cur != null){
                btnSave.setText("UPDATE");
                btnDelete.setDisable(false);
                txtCode.setText(cur.getCode());
                txtCode.setDisable(true);
                txtDescription.setText(cur.getDescription());
                txtQtyOnHand.setText(cur.getQty() + "");
                txtUnitPrice.setText(cur.getUnitPrice() + "");

            }else {
                btnSave.setText("SAVE");
                btnDelete.setDisable(true);
                txtCode.setDisable(false);
            }
        });

        try {
            tblItems.getItems().addAll(ItemDataAccess.getAllItems());
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load items, try again");
        }
        Platform.runLater(txtCode::requestFocus);
    }


    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        MainFormController.navigateToMain(root);
    }
    public void btnNewItemOnAction(ActionEvent event) {
        for (TextField textField: new TextField[]{txtCode, txtDescription, txtQtyOnHand, txtUnitPrice})
            textField.clear();
        txtCode.requestFocus();
        tblItems.getSelectionModel().clearSelection();
    }

    private boolean isDataValid() {
        try {
            String code = txtCode.getText().strip();
            String description = txtDescription.getText().strip();
            int qty = Integer.parseInt(txtQtyOnHand.getText().strip());
            BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText().strip()).setScale(2);

            if (!code.matches("\\d{4,}")) {
                txtCode.requestFocus();
                txtCode.selectAll();
                return false;
            } else if (!description.matches("[A-Za-z0-9]{4,}")) {
                txtDescription.requestFocus();
                txtDescription.selectAll();
                return false;
            } else if (qty <= 0) {
                txtQtyOnHand.requestFocus();
                txtQtyOnHand.selectAll();
                return false;
            } else if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
                txtUnitPrice.requestFocus();
                txtUnitPrice.selectAll();
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid number format: " + e.getMessage()).show();
            return false;
        }
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

            if (btnSave.getText().equals("SAVE")){
                if (ItemDataAccess.existsItem(item.getCode())){
                    new Alert(Alert.AlertType.ERROR, "Item code already exists").show();
                    txtCode.requestFocus();
                    txtCode.selectAll();
                    return;
                }
                ItemDataAccess.saveItem(item);
                tblItems.getItems().add(item);
            }else {
                ItemDataAccess.updateItem(item);
                ObservableList<Item> itemList = tblItems.getItems();
                Item selectedItem = tblItems.getSelectionModel().getSelectedItem();
                itemList.set(itemList.indexOf(selectedItem), item);
                tblItems.refresh();
            }
            btnNewItem.fire();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save the item, try again").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent event) {
        Item selectedItem = tblItems.getSelectionModel().getSelectedItem();
        try {
            if (OrderDataAccess.existsOrderByItemCode(selectedItem.getCode())){
                new Alert(Alert.AlertType.ERROR, "Failed to delete, item already associate with an order").show();
            }else {
                ItemDataAccess.deleteItems(selectedItem.getCode());
                tblItems.getItems().remove(selectedItem);
                if (tblItems.getItems().isEmpty()) btnNewItem.fire();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to delete, try again").show();
        }
    }


}
