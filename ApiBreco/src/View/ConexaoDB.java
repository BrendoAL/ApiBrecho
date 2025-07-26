package View;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    public static Connection getConexao() {

        String user = "root";
        String password = "root";
        String url = "jdbc:mysql://localhost:3306/brecho";

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
