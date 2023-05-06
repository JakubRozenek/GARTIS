package main;

// Importing essential modules
import database.database;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.io.File;

// Main subroutine controlling the report
public class report {

    public void createStockReport() {

        // Try to execute everything inside
        try {

            // Get the absolute file path for the invoice jasper report
            File file = new File("src/fxml/stock.jrxml").getAbsoluteFile();
            String url = file.getAbsolutePath();

            Class.forName("org.sqlite.JDBC");

            // Load the file and extract the data from the database
            JasperDesign jasperDesign = JRXmlLoader.load(url);
            JRDesignQuery jrDesignQuery = new JRDesignQuery();

            // Selecting what database to extract the data from
            jrDesignQuery.setText("SELECT * FROM Stock;");
            jasperDesign.setQuery(jrDesignQuery);

            // Creating the actual report
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, database.connection());

            // Display the report
            JasperViewer.viewReport(jasperPrint, false);
        }

        // Catch any related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    public void createReport(Integer id, Integer id2) {

        // Try to execute everything inside
        try {

            // Get the absolute file path for the invoice jasper report
            File file = new File("src/fxml/invoice.jrxml").getAbsoluteFile();
            String url = file.getAbsolutePath();

            // Load the file and extract the data from the database
            JasperDesign jasperDesign = JRXmlLoader.load(url);
            JRDesignQuery jrDesignQuery = new JRDesignQuery();

            // Selecting what database to extract the data from
            jrDesignQuery.setText("SELECT * FROM Invoice, Jobs, JobParts WHERE Invoice.customerID == " + id + " AND Jobs.customerID == " + id);
            jasperDesign.setQuery(jrDesignQuery);

            // Creating the actual report
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, database.connection());

            // Display the report
            JasperViewer.viewReport(jasperPrint, false);
        }

        // Catch any related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }
}
