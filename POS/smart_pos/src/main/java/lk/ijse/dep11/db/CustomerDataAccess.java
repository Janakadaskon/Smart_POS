package lk.ijse.dep11.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerDataAccess {
    private static final PreparedStatement STM_INSERT ;
    private static final PreparedStatement STM_UPDATE;
    private static final PreparedStatement STM_DELETE;
    private static final PreparedStatement STM_GET_ALL;
    private static final PreparedStatement STM_GET_LAST_ID;

    static {
        try {
            Connection connection = SingleConnectionDataSource.getInstance().getConnection();
            STM_GET_LAST_ID = connection.prepareStatement("SELECT id FROM customer ORDER BY id DESC FETCH FIRST ROWS ONLY ");
            STM_GET_ALL = connection.prepareStatement("SELECT * FROM customer ORDER BY id");
            STM_INSERT = connection.prepareStatement("INSERT INTO customer (id, name, address) VALUES (?,?,?)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
