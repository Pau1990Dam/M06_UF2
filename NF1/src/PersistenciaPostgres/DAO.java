package PersistenciaPostgres;

import java.sql.*;


public class DAO {

    private static Connection c;


    public static int countTableRows(String table) throws SQLException, ClassNotFoundException {

        c = DBConnection.connect();
        c.setAutoCommit(false);

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT count(*) FROM "+table+";" );
        rs.next();
        int rowcount = rs.getInt("count");

        rs.close();
        stmt.close();

        return rowcount;
    }

    public static void insertPelis(int ID, String titulo_org, String titulo, String origen, String fecha,
                                    String genero, int duracion) throws SQLException, ClassNotFoundException {

        Statement stmt = null;
        c = DBConnection.connect();

        String sql_insert= "INSERT INTO PELICULAS"+
                " (ID,TITULO_ORIGINAL,TITULO,ORIGEN_PRODUCCION,FECHA_ESTRENO,GENEROS,DURACION)" +
                " VALUES (?,?,?,?,?,?,?);";

        PreparedStatement preparedStatement = c.prepareStatement(sql_insert);
        preparedStatement.setInt(1, ID);
        preparedStatement.setString(2, titulo_org);
        preparedStatement.setString(3, titulo);
        preparedStatement.setString(4, origen);
        preparedStatement.setString(5, fecha);
        preparedStatement.setString(6, genero);
        preparedStatement.setInt(7, duracion);

        //Ejecutar insert
        preparedStatement.executeUpdate();

    }

    public static void insertActores_Peliculas(int ID_Actor, int ID_Peli, String personaje ) throws SQLException, ClassNotFoundException {

        //Conectar BD
        Statement stmt = null;
        c = DBConnection.connect();

        //Insert
        String sql_insert= "INSERT INTO ACTORES_PELICULAS"+
                " (ID_ACTOR,ID_PELICULA,PERSONAJE)" +
                " VALUES (?,?,?);";

        PreparedStatement preparedStatement = c.prepareStatement(sql_insert);
        preparedStatement.setInt(1, ID_Actor);
        preparedStatement.setInt(2, ID_Peli);
        preparedStatement.setString(3, personaje);

        //Ejecutar insert
        preparedStatement.executeUpdate();

    }

    public static void insertActores(int ID, String nombreActor) throws SQLException, ClassNotFoundException {

        Statement stmt = null;
        c = DBConnection.connect();

        //Insert
        String sql_insert= "INSERT INTO ACTORES"+
                " (ID,NOMBRE_ACTOR)" +
                " VALUES (?,?);";

        PreparedStatement preparedStatement = c.prepareStatement(sql_insert);
        preparedStatement.setInt(1, ID);
        preparedStatement.setString(2, nombreActor);

        //Ejecutar insert
        preparedStatement.executeUpdate();

    }

   /*#######################################################SELECTS#################################################### */

    //SELECT 1

    public static void verPeliculas() throws SQLException, ClassNotFoundException {

        Statement stmt = null;
        c = DBConnection.connect();
        c.setAutoCommit(false);

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM PELICULAS;" );
        System.out.println("  ID  ||      TITULO ORIGINAL      ||        TITULO        || ORIGEN PRODUCCION || FECHA ESTRENO ||        GENEROS        || DURACION ");
        while ( rs.next() ) {
            System.out.printf("%-6d||%-27.27s||%-22.22s||%-19.19s||%-15.15s||%-23.23s||%-4d%n",rs.getInt("ID"),rs.getString("TITULO_ORIGINAL"),
                    rs.getString("TITULO"),rs.getString("ORIGEN_PRODUCCION"),rs.getString("FECHA_ESTRENO"),
                    rs.getString("GENEROS"),rs.getInt("DURACION"));
        }

        System.out.println();

        rs.close();
        stmt.close();
        c.close();

    }

    //SELECT 2

    public static void verActores() throws SQLException, ClassNotFoundException {

        Statement stmt = null;
        c = DBConnection.connect();
        c.setAutoCommit(false);

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM ACTORES;" );
        System.out.println("   ID   ||   NOMBRE   ");
        while ( rs.next() ) {
            System.out.printf("%-8d||%s%n",rs.getInt("ID"),rs.getString("NOMBRE_ACTOR"));
        }

        System.out.println();

        rs.close();
        stmt.close();
        c.close();

    }

    //SELECT 3

    public static void verPersonajes() throws SQLException, ClassNotFoundException {

        Statement stmt = null;
        c = DBConnection.connect();
        c.setAutoCommit(false);

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT DISTINCT (PERSONAJE) FROM ACTORES_PELICULAS;" );
        System.out.println("   PERSONAJE   ");
        while ( rs.next() ) {
            System.out.printf("%s%n",rs.getString("PERSONAJE"));
        }

        System.out.println();

        rs.close();
        stmt.close();
        c.close();

    }

    //SELECT 4

    public static void verPeliculasActor(int id) throws SQLException, ClassNotFoundException {

        Statement stmt = null;
        c = DBConnection.connect();
        c.setAutoCommit(false);

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT p.ID, TITULO_ORIGINAL, TITULO FROM PELICULAS p\n" +
                " JOIN ACTORES_PELICULAS ap\n" +
                " ON p.ID=ap.ID_PELICULA\n" +
                " JOIN ACTORES a" +
                " ON ap.ID_ACTOR=a.ID\n" +
                " WHERE a.ID="+id+";" );

        System.out.println("  ID  ||     TITULO ORIGINAL     ||   TITULO   ");
        while ( rs.next() ) {
            System.out.printf("%-6d||%-24.24s||%s%n",rs.getInt("ID"),rs.getString("TITULO_ORIGINAL"),rs.getString("TITULO"));
        }

        System.out.println();

        rs.close();
        stmt.close();
        c.close();

    }

    //SELECT 5

    public static void verActoresPelicula(int id) throws SQLException, ClassNotFoundException {

        Statement stmt = null;
        c = DBConnection.connect();
        c.setAutoCommit(false);

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT DISTINCT NOMBRE_ACTOR, A.ID FROM \n" +
                "ACTORES A JOIN ACTORES_PELICULAS AP\n" +
                "ON A.ID=AP.ID_ACTOR\n" +
                "JOIN PELICULAS P\n" +
                "ON AP.ID_PELICULA=P.ID\n" +
                "WHERE P.ID="+id+";" );

        System.out.println("   ID   ||  NOMBRE ACTOR  ");
        while ( rs.next() ) {
            System.out.printf("%-8d||%s%n",rs.getInt("ID"),rs.getString("NOMBRE_ACTOR"));
        }

        System.out.println();

        rs.close();
        stmt.close();
        c.close();

    }

    //SELECT 6

    public static void verPersonajesActor(int id) throws SQLException, ClassNotFoundException {

        Statement stmt = null;
        c = DBConnection.connect();
        c.setAutoCommit(false);

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT DISTINCT PERSONAJE FROM ACTORES_PELICULAS\n" +
                "JOIN ACTORES\n" +
                "ON ID_ACTOR=ID\n" +
                "WHERE ID="+id+";" );

        System.out.println("   PERSONAJE   ");
        while ( rs.next() ) {
            System.out.printf("%s%n",rs.getString("PERSONAJE"));
        }

        System.out.println();

        rs.close();
        stmt.close();
        c.close();

    }

    //SELECT 7

    public static void verPersonajesPelicula(int id) throws SQLException, ClassNotFoundException {

        Statement stmt = null;
        c = DBConnection.connect();
        c.setAutoCommit(false);

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT PERSONAJE FROM ACTORES_PELICULAS \n" +
                "JOIN PELICULAS \n" +
                "ON ID_PELICULA=ID\n" +
                "WHERE ID="+id+";" );

        System.out.println("   PERSONAJE   ");
        while ( rs.next() ) {
            System.out.printf("%s%n",rs.getString("PERSONAJE"));
        }

        System.out.println();

        rs.close();
        stmt.close();
        c.close();

    }

    //SELECT 8

    public static void verPersonajesPeliculasActor(int id) throws SQLException, ClassNotFoundException {

        Statement stmt = null;
        c = DBConnection.connect();
        c.setAutoCommit(false);

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT TITULO_ORIGINAL, PERSONAJE FROM  PELICULAS p\n" +
                " JOIN ACTORES_PELICULAS ap\n" +
                " ON p.ID=ap.ID_PELICULA\n" +
                " JOIN ACTORES a\n" +
                " on ap.ID_ACTOR=a.ID\n" +
                " WHERE a.ID="+id+";" );

        System.out.println("     TITULO ORIGINAL     ||   PERSONAJE   ");
        while ( rs.next() ) {
            System.out.printf("%-24.24s||%s%n",rs.getString("TITULO_ORIGINAL"), rs.getString("PERSONAJE"));
        }

        System.out.println();

        rs.close();
        stmt.close();
        c.close();

    }
}
