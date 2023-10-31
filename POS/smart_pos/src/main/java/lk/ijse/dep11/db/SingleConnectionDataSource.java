package lk.ijse.dep11.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class SingleConnectionDataSource {
    private static SingleConnectionDataSource instace;

    private final Connection connection;


    public SingleConnectionDataSource() {
        try {
            Properties properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/application.properties"));
            String username = properties.getProperty("app.datasource.username");
            String password = properties.getProperty("app.datasource.password");
            String url = properties.getProperty("app.datasource.url");
            connection = DriverManager.getConnection(url, username, password);
            generateSchema();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        

    }

    private void generateSchema() {
    }
}
