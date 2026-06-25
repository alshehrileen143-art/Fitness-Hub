import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableMetadata {

    public static void printColumnNames(String tableName) {
        try (Connection conn = DatabaseConnection.connect()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, tableName, null);

            System.out.println("Columns in table: " + tableName);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");
                System.out.println(columnName + " (" + columnType + ")");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving column names: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String tableName = "users"; // Change table name if needed
        printColumnNames(tableName);
    }
}
