import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class Database {

    private static String driver = "org.sqlite.JDBC";
    private static String dbName;
    private static Database INSTANCE = null;
    private static Connection connect;

    public static Connection getInstance() throws SQLException, ClassNotFoundException {

        if(INSTANCE == null)
            createInstance();

        return connect;
    }

    private synchronized static void createInstance() throws SQLException, ClassNotFoundException {

        if(INSTANCE == null){
            INSTANCE = new Database();
            connect();
        }
    }

    private static void connect() throws ClassNotFoundException, SQLException {

        Class.forName(driver);
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        connect = DriverManager.getConnection("jdbc:sqlite:"+dbName,config.toProperties());

        System.out.println("Conexión establecida ...");
    }

    public static void setDataBaseName(String dataBaseName){
        dbName = dataBaseName;
    }

}
/*
       SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        connection = DriverManager.getConnection(DB_URL,config.toProperties());
 */