package lk.ijse.dep11.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.db.OrderDataAccess;
import lk.ijse.dep11.tm.OrderItem;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;

public class SearchOrderFormController {

    public TableView<OrderItem> tblOrders;
    public TextField txtSearch;
    public AnchorPane root;
    public ImageView imgHome;

    public void initialize() throws IOException, SQLException {
        String[] colNames = {"orderId", "orderDate", "customerId", "customerName", "orderTotal"};
        for (int i = 0; i < colNames.length; i++) {
            tblOrders.getColumns().get(i).setCellValueFactory(new PropertyValueFactory<>(colNames[i]));
        }

        try {
            tblOrders.getItems().addAll(OrderDataAccess.findOrders(""));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Failed to load orders, try again").show();
            e.printStackTrace();
            navigateToHome(null);
        }

        txtSearch.textProperty().addListener((ov, prev, cur) -> {
            tblOrders.getItems().clear();
            try {
                tblOrders.getItems().addAll(OrderDataAccess.findOrders(cur));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    

    public void navigateToHome(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        MainFormController.navigateToMain(root);
    }
}
