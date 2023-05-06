package database;

// Import modules
import java.io.File;
import java.sql.*;

// Subroutine to control all database related classes
public class database {

    // Return the connection to be used in other classes
    public static Connection connection() throws SQLException {

        // Location of the database file
        String url = "jdbc:sqlite:src/database/database.db";

        // Return the connection of the database
        return DriverManager.getConnection(url);
    }

    // Checking if database exists
    public static void checkDatabase() {

        // Location of the database
        File file = new File("src/database/database.db");

        // Location of the backup database
        File backup = new File("src/database/databaseBACKUP.db");

        // Check if database exists
        if (!file.exists()) {

            // Try creating all the tables if they dont exist
            try {

                // Create a statement and establish a connection
                Statement state = connection().createStatement();

                // Execute the query to create a Customer table
                state.executeUpdate("""
                    CREATE TABLE Customer (
                        customer_Id 	INTEGER PRIMARY KEY AUTOINCREMENT,
                        name 			VARCHAR(255),
                        phone_number 	VARCHAR(255),
                        email_address 	VARCHAR(255),
                        address 		VARCHAR(255),
                        postcode 		VARCHAR(10),
                        fax 			VARCHAR(255),
                        date 			DATE
                    );
                """);

                // Execute the query to create a Stock table
                state.executeUpdate("""
                    CREATE TABLE Stock (
                        part_ID			INTEGER PRIMARY KEY AUTOINCREMENT,
                        partName		VARCHAR(255),
                        code			VARCHAR(255),
                        manufacturer	VARCHAR(255),
                        vehicleType		VARCHAR(255),
                        year			VARCHAR(255),
                        price			VARCHAR(255),
                        stockLevel		INTEGER
                    );
                """);

                // Close the current connection
                state.close();
            }

            // Catch any SQL related errors
            catch (SQLException exception) {
                System.out.println("[ERROR]#~ " + exception.getMessage());
            }
        }
    }
}
