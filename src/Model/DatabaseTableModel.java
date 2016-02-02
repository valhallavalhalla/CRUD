package Model;

import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseTableModel extends AbstractTableModel {

    private Statement statement;
    private String sourceTableName;
    private ArrayList<String> columnNames = new ArrayList<>();
    private ArrayList columnTypes = new ArrayList();
    private ArrayList<ArrayList<String>> data = new ArrayList<>();

    public DatabaseTableModel(Connection databaseConnection, String sourceTableName) {
        // get name of source table
        this.sourceTableName = sourceTableName;
        try {
            // create statement
            this.statement = databaseConnection.createStatement();
            // get data from database
            setDataSource(statement.executeQuery("select * from " + sourceTableName));
        } catch (SQLException e) {
            System.err.println("Can't create or execute statement");
            e.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames.get(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex).get(columnIndex);
    }

    // get data from database
    public void setDataSource(ResultSet rs) {
        try {
            // clear data arrays
            data.clear();
            columnNames.clear();
            columnTypes.clear();
            // get info about columns types and names
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                // column name
                columnNames.add(rsmd.getColumnName(i + 1));
                // column type
                Class type = Class.forName(rsmd.getColumnClassName(i + 1));
                columnTypes.add(type);
            }
            // report about changes
            fireTableStructureChanged();
            // get data
            while (rs.next()) {
                // saving cells of row
                ArrayList row = new ArrayList();
                for (int i = 0; i < columnCount; i++) {
                    if (columnTypes.get(i) == String.class)
                        row.add(rs.getString(i + 1));
                    else
                        row.add(rs.getObject(i + 1));
                }
                synchronized (data) {
                    data.add(row);
                    // report about adding row
                    fireTableRowsInserted(data.size() - 1, data.size() - 1);
                }
            }
        } catch (Exception e) {
            System.err.println("Can't get data from database");
            e.printStackTrace();
        }

    }

    // add new entry
    public boolean addEmployee(String lastName, String firstName, String jobTitle, String phone, String email) {
        String statementString =
                "insert into " + sourceTableName +
                        " (lastName, firstName, jobTitle, phone, email)" +
                        " values ('" + lastName + "', '" + firstName + "', '" + jobTitle + "', '" + phone + "', '" +
                        email + "')";
        try {
            statement.executeUpdate(statementString);
            refreshData();
            return true;
        } catch (SQLException e) {
            System.err.println("Can't add new employee");
            return false;
        }

    }

    // update entry
    public boolean updateEmployeeInfo(
            Integer id, String lastName, String firstName, String jobTitle, String phone, String email) {
        String statementString =
                "update " + sourceTableName + " set " +
                        "lastName='" + lastName + "', " +
                        "firstName='" + firstName + "', " +
                        "jobTitle='" + jobTitle + "', " +
                        "phone='" + phone + "', " +
                        "email='" + email + "' " +
                        "where id=" + id;
        try {
            statement.executeUpdate(statementString);
            refreshData();
            return true;
        } catch (SQLException e) {
            System.err.println("Can't update employee information");
            return false;
        }

    }

    // delete entry
    public boolean deleteEmployee(Integer employeeID) {
        String statementString = "delete from " + sourceTableName + " where id = " + employeeID;
        try {
            statement.executeUpdate(statementString);
            refreshData();
            return true;
        } catch (SQLException e) {
            System.err.println("Can't delete employee");
            return false;
        }
    }

    // refresh data from database
    private void refreshData() {
        try {
            setDataSource(statement.executeQuery("select * from " + sourceTableName));
        } catch (SQLException e) {
            System.err.println("Can't execute statement");
            e.printStackTrace();
        }
    }

    //other algorithms to check
/*    public ArrayList<ArrayList<String>> getSourceTableData(ResultSet resultSet) {
        ArrayList<ArrayList> data = new ArrayList<>();
        try {
            int rowCounter = 0;
            while (resultSet.next()) {
                data.add(new ArrayList<>());
                int columnCounter = 1;
                while (true) {
                    try {
                        data.get(rowCounter).add(resultSet.getString(columnCounter));
                        columnCounter++;
                    } catch (Exception ex) {
                        break;
                    }
                }
                rowCounter++;
            }
            return data;
        } catch (SQLException e) {
            System.err.println("Error: Can't get access to table");
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getColumnNames(ResultSet resultSet) {
        ArrayList<String> columnsNames;
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            columnsNames = new ArrayList<>(columnCount);
            for (int i = 1; i < columnCount; i++) {
                columnNames.add(resultSetMetaData.getColumnName(i + 1));
            }
            return columnsNames;
        } catch (SQLException e) {
            System.err.println("Error: Can't get access to table");
            e.printStackTrace();
            return null;
        }
    }*/
}
