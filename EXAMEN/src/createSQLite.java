import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class createSQLite {


    public static void crearBD(Connection connection)throws SQLException, Exception{
        {
            PreparedStatement stmt = null;
            try{

                String sql_pokemons = "CREATE TABLE IF NOT EXISTS POKEMONS" +
                        "(POKE_ID INT PRIMARY KEY        NOT NULL," +
                        " NOM                    TEXT    NOT NULL)";
                stmt = connection.prepareStatement(sql_pokemons);
                stmt.executeUpdate();


                String sql_tipus = "CREATE TABLE IF NOT EXISTS TIPUS" +
                        "(TIPUS_ID INT  PRIMARY KEY        NOT NULL,"+
                        " NOM                      TEXT    NOT NULL)";
                stmt = connection.prepareStatement(sql_tipus);
                stmt.executeUpdate();

                String sql_pokemons_tipus = "CREATE TABLE IF NOT EXISTS POKEMONS_TIPUS" +
                        "(POKE_ID                INT     NOT NULL," +
                        " TIPUS_ID               INT     NOT NULL," +
                        "FOREIGN KEY (POKE_ID) REFERENCES POKEMONS ON DELETE CASCADE ,"+
                        "FOREIGN KEY (TIPUS_ID) REFERENCES TIPUS ON DELETE CASCADE ,"+
                        "PRIMARY KEY (POKE_ID,TIPUS_ID))";
                stmt = connection.prepareStatement(sql_pokemons_tipus);
                stmt.executeUpdate();



                System.out.println("Tablas creadas correctamente.");
            }finally {
                stmt.close();
            }

        }
    }
}