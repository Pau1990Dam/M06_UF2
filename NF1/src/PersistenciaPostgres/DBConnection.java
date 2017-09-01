package PersistenciaPostgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static String url = "jdbc:postgresql://192.168.1.89:5432/dao";
    private static String user = "postgres";
    private static String pass = "pau";

    public static Connection connect() throws ClassNotFoundException, SQLException {

        Connection c = null;
        Statement stmt = null;

        Class.forName("org.postgresql.Driver");

        return DriverManager.getConnection(
                url,
                user,
                pass);
    }

    public static void showConnectionTargetInfo(){

        System.out.println("PostgreSQL server: " +
                "\n\tEl schema -> "+url+
                "\n\tEl usuario -> "+user+
                "\n\tEl password -> "+pass+"\n");
    }
}
