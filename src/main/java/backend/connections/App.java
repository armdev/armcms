package backend.connections;

/**
 *
 * @author armen
 */
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class App {

    public static void main(String[] args) {
        Properties prop = new Properties();

        try {
            //set the properties value
            prop.setProperty("database", "localhost");
            prop.setProperty("dbuser", "mkyongfgnfn");
            prop.setProperty("dbpassword", "password");
            //save properties to project root folder
            prop.store(new FileOutputStream("src/main/resources/config.properties"), null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
