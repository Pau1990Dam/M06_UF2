package API_plus_SQLite;

import java.sql.*;

public class createSQLite {


    public static void crearBD()throws SQLException, Exception{
        {
            Connection c = null;
            Statement stmt = null;

                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:test.db");
                System.out.println("Base de datos abierta correctamente.");

                stmt = c.createStatement();
                String sql_pelis = "CREATE TABLE PELICULAS" +
                        "(ID INT PRIMARY KEY               NOT NULL," +
                        " TITULO_ORIGINAL          TEXT    NOT NULL, " +
                        " TITULO                   TEXT            ," +
                        " ORIGEN_PRODUCCION        TEXT    NOT NULL,"+
                        " FECHA_ESTRENO            TEXT    NOT NULL," +
                        " GENEROS                  TEXT    NOT NULL," +
                        " DURACION                 INT    NOT NULL)";
                stmt.executeUpdate(sql_pelis);
                String sql_actores = "CREATE TABLE ACTORES" +
                        "(ID           INT PRIMARY KEY     NOT NULL," +
                        " NOMBRE_ACTOR             TEXT    NOT NULL)";
                stmt.executeUpdate(sql_actores);
                String sql_actores_peliculas = "CREATE TABLE ACTORES_PELICULAS" +
                        "(ID_ACTOR                 INT     NOT NULL," +
                        " ID_PELICULA              INT     NOT NULL," +
                        " PERSONAJE                TEXT    NOT NULL," +
                        " PRIMARY KEY (ID_ACTOR, ID_PELICULA,PERSONAJE))";//puede pasar que un actor haga más de un personaje en una misma peli
                stmt.executeUpdate(sql_actores_peliculas);
                stmt.close();
                c.close();

            System.out.println("Tablas creadas correctamente.\n");
        }
    }
}
