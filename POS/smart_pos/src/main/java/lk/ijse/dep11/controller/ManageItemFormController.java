package lk.ijse.dep11.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.tm.Item;

import java.io.IOException;

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


    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        MainFormController.navigateToMain(root);
    }

    public void btnSaveOnAction(ActionEvent event) {
    }

    public void btnDeleteOnAction(ActionEvent event) {
    }

    public void btnNewItemOnAction(ActionEvent event) {
    }
}
