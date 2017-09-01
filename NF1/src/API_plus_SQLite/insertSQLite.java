package API_plus_SQLite;

    import java.sql.*;

public class insertSQLite {

    public static void instertPelis(int ID, String titulo_org, String titulo, String origen, String fecha,
                                    String genero, int duracion) {
        {
            Connection c = null;
            Statement stmt = null;

            try {
                //Conectar BD
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:test.db");
                //Insert

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
            } catch ( ClassNotFoundException e ) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }catch (SQLException x){
                x.printStackTrace();
            }
        }
    }

    public static void instertActores_Peliculas(int ID_Actor, int ID_Peli, String personaje ) {
        {
            Connection c = null;
            Statement stmt = null;

            try {

                //Conectar BD
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:test.db");

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
            } catch ( ClassNotFoundException e ) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }catch (SQLException x){
                x.printStackTrace();
            }
        }
    }

    public static void instertActores(int ID, String nombreActor) {
        {
            Connection c = null;
            Statement stmt = null;

            try {

                //Conectar BD
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:test.db");

                //Insert

                String sql_insert= "INSERT INTO ACTORES"+
                        " (ID,NOMBRE_ACTOR)" +
                        " VALUES (?,?);";

                PreparedStatement preparedStatement = c.prepareStatement(sql_insert);
                preparedStatement.setInt(1, ID);
                preparedStatement.setString(2, nombreActor);

                //Ejecutar insert
                preparedStatement.executeUpdate();
            } catch ( ClassNotFoundException e ) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }catch (SQLException x){
                x.printStackTrace();
            }
        }
    }



}
