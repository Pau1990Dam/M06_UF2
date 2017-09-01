package API_plus_SQLite;

import java.sql.*;
import java.util.Scanner;


public class selectSQLite {

    public static void menuSelect(){

        Scanner entrada=new Scanner(System.in);
        int opcion=0;
        int respuesta;

        do{

            System.out.println("MENU");
            System.out.println("1. Ver peliculas.");
            System.out.println("2. Ver actores.");
            System.out.println("3. Ver personajes");
            System.out.println("4. Ver peliculas en las que haya participado un actor.");
            System.out.println("5. Ver actores de una pelicula.");
            System.out.println("6. Ver personajes interpretados por un actor.");
            System.out.println("7. Ver personajes de una pelicula.");
            System.out.println("8. Ver personajes en sus respectivas peliculas interpretados por un actor.");
            System.out.println("9. Salir. (Vale también con cuaquier otro número)");
            opcion=entrada.nextInt();
            System.out.println();

            switch(opcion){
                case 1:
                    verPeliculas();
                    break;
                case 2:
                    verActores();
                    break;
                case 3:
                    verPersonajes();
                    break;
                case 4:
                    System.out.println("Indica la id del actor para ver el listado de las peliculas en las que ha" +
                            " actuado. ID del actor: ");
                    respuesta=entrada.nextInt();
                    System.out.println();
                    verPeliculasActor(respuesta);
                    break;
                case 5:
                    System.out.println("Indica la id de la pelicula  para ver los actores que ha participado en ella. " +
                            "ID  de la pelicula: ");
                    respuesta=entrada.nextInt();
                    System.out.println();
                    verActoresPelicula(respuesta);
                    break;
                case 6:
                    System.out.println("Indica la id del actor para ver todos los personajes que ha interpretado. " +
                            "ID del actor: ");
                    respuesta=entrada.nextInt();
                    verPersonajesActor(respuesta);
                    System.out.println();
                    break;
                case 7:
                    System.out.println("Indica la id de la pelicula para ver los personajes que aparecen en ella. " +
                            "ID de la Pelicula : ");
                    respuesta=entrada.nextInt();
                    verPersonajesPelicula(respuesta);
                    System.out.println();
                    break;
                case 8:
                    System.out.println("Indica la id del actor para ver todos los personajes que ha interpretado" +
                            "en sus respectivas peliculas. ID actor:");
                    respuesta=entrada.nextInt();
                    verPersonajesPeliculasActor(respuesta);
                    break;
                default:
                    System.out.println("ADEU!!!");
                    break;
            }

        }while(opcion>0&&opcion<9);
    }

    //SELECT 1

    private static void verPeliculas()
    {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM PELICULAS;" );

            while ( rs.next() ) {

                System.out.println("ID: " + rs.getInt("ID"));
                System.out.println("TITULO ORIGINAL: " + rs.getString("TITULO_ORIGINAL"));
                System.out.println("TITULO: " + rs.getString("TITULO"));
                System.out.println("ORIGEN PRODUCCION: " + rs.getString("ORIGEN_PRODUCCION"));
                System.out.println("FECHA ESTRENO: " + rs.getString("FECHA_ESTRENO"));
                System.out.println("GENEROS: "+rs.getString("GENEROS"));
                System.out.println("DURACION: "+rs.getInt("DURACION"));
                System.out.println();
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    //SELECT 2

    private static void verActores()
    {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM ACTORES;" );

            while ( rs.next() ) {

                System.out.println("ID: " + rs.getInt("ID"));
                System.out.println("NOMBRE: " + rs.getString("NOMBRE_ACTOR"));
                System.out.println();
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    //SELECT 3

    private static void verPersonajes()
    {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT DISTINCT (PERSONAJE) FROM ACTORES_PELICULAS;" );

            while ( rs.next() ) {

                System.out.println("PERSONAJE: " + rs.getString("PERSONAJE"));
                System.out.println();
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    //SELECT 4

    private static void verPeliculasActor(int id)
    {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT p.ID, TITULO_ORIGINAL, TITULO FROM PELICULAS p\n" +
                    " JOIN ACTORES_PELICULAS ap\n" +
                    " ON p.ID=ap.ID_PELICULA\n" +
                    " JOIN ACTORES a" +
                    " ON ap.ID_ACTOR=a.ID\n" +
                    " WHERE a.ID="+id+";" );

            while ( rs.next() ) {

                System.out.println("ID: "+rs.getInt("ID"));
                System.out.println("TITULO_ORIGINAL: " + rs.getString("TITULO_ORIGINAL"));
                System.out.println("TITULO: "+rs.getString("TITULO"));
                System.out.println();
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    //SELECT 5

    private static void verActoresPelicula(int id)
    {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT DISTINCT NOMBRE_ACTOR, A.ID FROM \n" +
                    "ACTORES A JOIN ACTORES_PELICULAS AP\n" +
                    "ON A.ID=AP.ID_ACTOR\n" +
                    "JOIN PELICULAS P\n" +
                    "ON AP.ID_PELICULA=P.ID\n" +
                    "WHERE P.ID="+id+";" );

            while ( rs.next() ) {

                System.out.println("ID: "+rs.getInt("ID"));
                System.out.println("ACTOR: "+rs.getString("NOMBRE_ACTOR"));
                System.out.println();
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    //SELECT 6

    private static void verPersonajesActor(int id)
    {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT DISTINCT PERSONAJE FROM ACTORES_PELICULAS\n" +
                    "JOIN ACTORES\n" +
                    "ON ID_ACTOR=ID\n" +
                    "WHERE ID="+id+";" );

            while ( rs.next() ) {

                System.out.println("PERSONAJE: "+rs.getString("PERSONAJE"));
                System.out.println();
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    //SELECT 7

    private static void verPersonajesPelicula(int id)
    {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT PERSONAJE FROM ACTORES_PELICULAS \n" +
                    "JOIN PELICULAS \n" +
                    "ON ID_PELICULA=ID\n" +
                    "WHERE ID="+id+";" );

            while ( rs.next() ) {

                System.out.println("PERSONAJE: "+rs.getString("PERSONAJE"));
                System.out.println();
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    //SELECT 8

    private static void verPersonajesPeliculasActor(int id)
    {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT TITULO_ORIGINAL, PERSONAJE FROM  PELICULAS p\n" +
                    " JOIN ACTORES_PELICULAS ap\n" +
                    " ON p.ID=ap.ID_PELICULA\n" +
                    " JOIN ACTORES a\n" +
                    " on ap.ID_ACTOR=a.ID\n" +
                    " WHERE a.ID="+id+";" );

            while ( rs.next() ) {

                System.out.println("PELICULA: "+rs.getString("TITULO_ORIGINAL"));
                System.out.println("PERSONAJE: "+rs.getString("PERSONAJE"));
                System.out.println("");
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
}
