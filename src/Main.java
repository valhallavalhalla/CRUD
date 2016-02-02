import Model.*;g
import View.Frame;
import com.mysql.jdbc.Driver;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try {
            DatabaseConnection DBC =
                    new DatabaseConnection(new Driver(), "jdbc:mysql://localhost:3306/company", "root", "root");
            DatabaseTableModel customersTableModel = new DatabaseTableModel(DBC.getConnection(), "employees");
            new Frame(customersTableModel, "Employees");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
