package lk.ijse.dep11.db;

import lk.ijse.dep11.tm.Item;
import lk.ijse.dep11.tm.OrderItem;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class OrderDataAccess {
    private static final PreparedStatement STM_EXISTS_BY_CUSTOMER_ID;
    private static final PreparedStatement STM_EXISTS_BY_ITEM_CODE;
    private static final PreparedStatement STM_GET_LAST_ID;
    private static final PreparedStatement STM_INSERT_ORDER;
    private static final PreparedStatement STM_INSERT_ORDER_ITEM;
    private static final PreparedStatement STM_UPDATE_STOCK;

//    private static final PreparedStatement STM_FIND;




    static {
        try{
            Connection connection = SingleConnectionDataSource.getInstance().getConnection();
            STM_EXISTS_BY_CUSTOMER_ID = connection
                    .prepareStatement("SELECT * FROM \"order\" WHERE customer_id=?");
            STM_EXISTS_BY_ITEM_CODE = connection
                    .prepareStatement("SELECT * FROM order_item WHERE item_code = ?");
            STM_GET_LAST_ID = connection
                    .prepareStatement("SELECT * FROM \"order\" ORDER BY id DESC FETCH FIRST ROWS ONLY ");
            STM_INSERT_ORDER = connection.prepareStatement
                    ("INSERT INTO  \"order\" (id,date,customer_id)VALUES(?,?,?)");
            STM_INSERT_ORDER_ITEM = connection.prepareStatement
                    ("INSERT INTO order_item(order_id, item_code, qty, unit_price) VALUES (?,?,?,?)");
            STM_UPDATE_STOCK = connection.prepareStatement
                    ("UPDATE item SET qty  = qty - ? WHERE code = ?");
//            STM_FIND = connection.prepareStatement
//                    ("SELECT o.*,c.name");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveOrder(String orderId, Date orderDate, String customerId,
                                   List<OrderItem> orderItemList)throws SQLException{
        SingleConnectionDataSource.getInstance().getConnection().setAutoCommit(false);
        try {
            STM_INSERT_ORDER.setString(1,orderId);
            STM_INSERT_ORDER.setDate(2, (java.sql.Date) orderDate);
            STM_INSERT_ORDER.setString(3,customerId);
            STM_INSERT_ORDER.executeUpdate();
            for (OrderItem orderItem : orderItemList){
                STM_INSERT_ORDER_ITEM.setString(1, orderId);
                STM_INSERT_ORDER_ITEM.setString(2, orderItem.getCode());
                STM_INSERT_ORDER_ITEM.setInt(3, orderItem.getQty());
                STM_INSERT_ORDER_ITEM.setBigDecimal(4, orderItem.getUnitPrice());
                STM_INSERT_ORDER_ITEM.executeUpdate();

                STM_UPDATE_STOCK.setInt(1, orderItem.getQty());
                STM_UPDATE_STOCK.setString(2, orderItem.getCode());
                STM_UPDATE_STOCK.executeUpdate();
            }
            SingleConnectionDataSource.getInstance().getConnection().commit();
            
        }catch (Throwable t){
            SingleConnectionDataSource.getInstance().getConnection().rollback();
            throw new SQLException();
        }finally {
            SingleConnectionDataSource.getInstance().getConnection().setAutoCommit(true);
        }
    }



//    public static List<OrderItem> findOrders(String query) throws SQLException{
//        for (int i = 0; i <= 4; i++) {
//
//        }
//    }

    public static String getLastOrderId()throws SQLException{
        ResultSet rst = STM_GET_LAST_ID.executeQuery();
        return (rst.next()) ? rst.getString(1): null;
    }

    public static boolean existOrderByCustomerId(String customerId)throws SQLException{
        STM_EXISTS_BY_CUSTOMER_ID.setString(1, customerId);
        return STM_EXISTS_BY_CUSTOMER_ID.executeQuery().next();
    }

    public static boolean existsOrderByItemCode(String code)throws SQLException{
        STM_EXISTS_BY_ITEM_CODE.setString(1,code);
        return STM_EXISTS_BY_ITEM_CODE.executeQuery().next();
    }
}
