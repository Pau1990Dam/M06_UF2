package PersistenciaPostgres;

import java.sql.*;
import java.util.ArrayList;


public class createSQLite {


    public static void crearBD(Connection c)throws SQLException, Exception{

        ArrayList<String> tablas = tablasACrear(c);

        if(tablas.size()==0)return;

        Statement stmt = null;

        System.out.println("Base de datos abierta correctamente.");

        stmt = c.createStatement();

        for(int i = 0; i < tablas.size(); i++){
            switch (tablas.get(i)){
                case "actores":
                    String sql_actores = "CREATE TABLE ACTORES" +
                            "(ID           INT PRIMARY KEY     NOT NULL," +
                            " NOMBRE_ACTOR             TEXT    NOT NULL)";
                    stmt.executeUpdate(sql_actores);
                    break;
                case "peliculas":
                    String sql_pelis = "CREATE TABLE PELICULAS" +
                            "(ID INT PRIMARY KEY               NOT NULL," +
                            " TITULO_ORIGINAL          TEXT    NOT NULL, " +
                            " TITULO                   TEXT            ," +
                            " ORIGEN_PRODUCCION        TEXT    NOT NULL,"+
                            " FECHA_ESTRENO            TEXT    NOT NULL," +
                            " GENEROS                  TEXT    NOT NULL," +
                            " DURACION                 INT    NOT NULL)";
                    stmt.executeUpdate(sql_pelis);
                    break;
                case "actores_peliculas":
                    String sql_actores_peliculas = "CREATE TABLE ACTORES_PELICULAS" +
                            "(ID_ACTOR                 INT     NOT NULL," +
                            " ID_PELICULA              INT     NOT NULL," +
                            " PERSONAJE                TEXT    NOT NULL," +
                            " FOREIGN KEY (ID_ACTOR) REFERENCES ACTORES,"+
                            " FOREIGN KEY (ID_PELICULA) REFERENCES PELICULAS,"+
                            " PRIMARY KEY (ID_ACTOR, ID_PELICULA,PERSONAJE))";//puede pasar que un actor haga más de un personaje en una misma peli
                    stmt.executeUpdate(sql_actores_peliculas);
                    break;
            }
        }

        stmt.close();
        c.close();

        System.out.println("Tablas creadas correctamente.");

    }

    private static ArrayList<String> tablasACrear(Connection c) throws SQLException {

        DatabaseMetaData md = c.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", new String[]{"TABLE"});

        ArrayList <String> tablas = new ArrayList<>();
        tablas.add("actores"); tablas.add("peliculas");tablas.add("actores_peliculas");

        while (rs.next()) {
            if (tablas.contains(rs.getString(3))){
                tablas.remove(rs.getString(3));
            }
        }
        return tablas;
    }
}
