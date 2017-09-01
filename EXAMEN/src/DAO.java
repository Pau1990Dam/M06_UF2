import java.sql.*;
import java.util.HashSet;


public class DAO {

    private static Connection c;
    private static HashSet<Integer> poke_ids;
    private static HashSet<Integer> tipus_ids;


    public static void loadIdsFromDatabase() throws SQLException, ClassNotFoundException {
        poke_ids = getIds("poke_id","pokemons");
        tipus_ids = getIds("tipus_id","tipus");
    }

    public static boolean updatePoke_id(int id){
        if(poke_ids.contains(id))
            return false;
        else {
            poke_ids.add(id);
            return true;
        }
    }

    public static boolean updateTipus_id(int id){
        if(tipus_ids.contains(id))
            return false;
        else {
            tipus_ids.add(id);
            return true;
        }
    }

    public static boolean isTipusAlreadyInTheDB(int id){
        return tipus_ids.contains(id);
    }

    public static boolean isPokemonAlreadyInTheDB(int id){
        return poke_ids.contains(id);
    }


    private static HashSet<Integer> getIds(String colum, String tableName) throws SQLException, ClassNotFoundException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashSet<Integer> ids = new HashSet<>();

        try{

            c = Database.getInstance();

            String sql_select ="SELECT "+colum+" FROM "+tableName;
            stmt = c.prepareStatement(sql_select);

            rs  = stmt.executeQuery();

            while (rs.next()) {
                ids.add(rs.getInt(colum));
            }

            return ids;

        }finally {

            stmt.close();
            rs.close();
        }


    }

    public static void inserirPokemon(Pk pokemon) throws SQLException, ClassNotFoundException {

        PreparedStatement preparedStatement = null;
        try{

            c = Database.getInstance();
            String sql_insert= "INSERT INTO POKEMONS "+
                    " (poke_id,nom)" +
                    " VALUES (?,?);";

            int poke_id = pokemon.getIdentificador();

            preparedStatement = c.prepareStatement(sql_insert);
            preparedStatement.setInt(1,poke_id);
            preparedStatement.setString(2,pokemon.getNombre());

            preparedStatement.executeUpdate();

            for(Tipo t: pokemon.getTipos()){


                if(!updateTipus_id(t.getIdty())){
                    inserirPokemon_Tipus(poke_id, t.getIdty());
                    continue;
                }
                inserirTipus(t);
                inserirPokemon_Tipus(poke_id, t.getIdty());

            }

        }finally {
            preparedStatement.close();
        }
    }

    public static void inserirTipus(Tipo tipus) throws SQLException, ClassNotFoundException {

        PreparedStatement preparedStatement = null;

        try {
            c = Database.getInstance();
            String sql_insert = "INSERT INTO TIPUS" +
                    " (tipus_id, nom)" +
                    " VALUES (?,?);";

            preparedStatement = c.prepareStatement(sql_insert);
            preparedStatement.setInt(1, tipus.getIdty());
            preparedStatement.setString(2, tipus.getTipus());

            preparedStatement.executeUpdate();

        }finally {
            preparedStatement.close();
        }

    }

    public static void inserirPokemon_Tipus(int poke_id, int tipus_id) throws SQLException, ClassNotFoundException {

        PreparedStatement preparedStatement = null;

        try {
            c = Database.getInstance();
            String sql_insert = "INSERT INTO POKEMONS_TIPUS " +
                    " (poke_id,tipus_id)" +
                    " VALUES (?,?);";

            preparedStatement = c.prepareStatement(sql_insert);
            preparedStatement.setInt(1, poke_id);
            preparedStatement.setInt(2, tipus_id);

            preparedStatement.executeUpdate();

        }finally {
            preparedStatement.close();
        }
    }

    public static void mostrarPokemons() throws SQLException, ClassNotFoundException {

        int i = 0;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            c = Database.getInstance();
            String sql_select = "SELECT * FROM POKEMONS";

            preparedStatement = c.prepareStatement(sql_select);
            rs = preparedStatement.executeQuery();

            while (rs.next())
                System.out.print("\n[" + (++i) + "]|| " + rs.getInt("poke_id") + " || " + rs.getString("nom") + " ||");

            System.out.println();
        }finally {
            preparedStatement.close();
            rs.close();
        }

    }

    /**
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void mostrarTipus() throws SQLException, ClassNotFoundException {

        int i = 0;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            c = Database.getInstance();
            String sql_select = "SELECT * FROM TIPUS";

            preparedStatement = c.prepareStatement(sql_select);
            rs = preparedStatement.executeQuery();

            while (rs.next())
                System.out.print("\n[" + (++i) + "]|| " + rs.getInt("tipus_id") + " || " + rs.getString("nom") + " ||");

            System.out.println();
        }finally {
            preparedStatement.close();
            rs.close();
        }

    }

    public static void mostrarPokemonsTipus(int tipus_id) throws SQLException, ClassNotFoundException {

        int i = 0;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try{

        c = Database.getInstance();
        String sql_select = "SELECT p.poke_id, p.nom FROM TIPUS t\n" +
                " JOIN POKEMONS_TIPUS pt\n" +
                " ON t.tipus_id = pt.tipus_id\n" +
                "JOIN POKEMONS p\n" +
                "ON pt.poke_id = p.poke_id\n" +
                "WHERE t.tipus_id = ?";

        preparedStatement = c.prepareStatement(sql_select);
        preparedStatement.setInt(1,tipus_id);
        rs = preparedStatement.executeQuery();


        while (rs.next())
            System.out.print("\n["+(++i)+"]|| "+rs.getInt("poke_id")+
                    " || "+rs.getString("nom")+" ||");

        System.out.println();

        }finally {
            preparedStatement.close();
            rs.close();
        }

    }

    public static void mostrarPokeMonInfo(int poke_id) throws SQLException, ClassNotFoundException {

        int i = 0;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {

            c = Database.getInstance();
            String sql_select ="SELECT p.poke_id, p.nom AS pokemon, t.nom FROM POKEMONS p " +
                    " JOIN POKEMONS_TIPUS pt " +
                    " ON p.poke_id = pt.poke_id " +
                    " JOIN TIPUS t " +
                    " ON pt.tipus_id = t.tipus_id " +
                    " WHERE p.poke_id = ?";

            preparedStatement = c.prepareStatement(sql_select);
            preparedStatement.setInt(1,poke_id);

            rs = preparedStatement.executeQuery();



            while (rs.next()) {
                if(i == 0)
                    System.out.print("\n|| " + rs.getInt("poke_id") +
                            " || " + rs.getString("pokemon") +
                            " || " + rs.getString("nom"));
                else
                    System.out.print(", "+rs.getString("nom"));
                i++;
            }
            System.out.print(" ||");
            System.out.println();

        }finally {
            preparedStatement.close();
            rs.close();
        }

    }

    public static void deleteTipusCascade(int tipoID) throws SQLException, ClassNotFoundException {

        PreparedStatement preparedStatement = null;
        try {
            c = Database.getInstance();

            //Borramos todos los registros cuyo poke_id este enparejado con en alguna tupla con el tipo que queremos borrar
            // de la tabla tipos_pokemones.

            String sql_delete_relations = "DELETE FROM POKEMONS_TIPUS WHERE poke_id IN (SELECT poke_id FROM POKEMONS_TIPUS" +
                    " WHERE tipus_id = ?)";
            //Borramos todos los pokemos cuya id no tienen correspondencia con ningún registro de la tabla pokemons_tipus
            String sql_delete_orphan_pokemons = "DELETE FROM POKEMONS WHERE poke_id NOT IN (SELECT DISTINCT(poke_id) FROM POKEMONS_TIPUS)";
            //Borramos todos los tipos cya id no tenga correspondencia con ningún registro de la tabla pokemon_tipus, ya que
            //se ha podido borrar algún pokemón que contenia más tipos dejando huerfanó de relación algún registro de la tabla
            //tipos
            String sql_delete_orphan_tipus = "DELETE FROM TIPUS WHERE tipus_id NOT IN (SELECT DISTINCT(tipus_id) FROM POKEMONS_TIPUS)";

            preparedStatement = c.prepareStatement(sql_delete_relations);
            preparedStatement.setInt(1, tipoID);

            int pokemons_tipus_rows_affected = preparedStatement.executeUpdate();

            preparedStatement = c.prepareStatement(sql_delete_orphan_pokemons);
            int pokemons_rows_affected = preparedStatement.executeUpdate();

            preparedStatement = c.prepareStatement(sql_delete_orphan_tipus);
            int tipus_rows_affected = preparedStatement.executeUpdate();

            System.out.println(pokemons_tipus_rows_affected + " registres afectats a la taula pokemons_tipus");
            System.out.println(pokemons_rows_affected + " registres afectats a la taula pokemons");
            System.out.println(tipus_rows_affected + " registres afectats a la taula tipus");

        }finally {

            preparedStatement.close();
            loadIdsFromDatabase();
        }
    }

    public static void deletePokemonCascade(int poke_id) throws SQLException, ClassNotFoundException {

        PreparedStatement preparedStatement = null;
        try {
            c = Database.getInstance();

            //Borramos todos los registros cuyo poke_id este enparejado con en alguna tupla con el tipo que queremos borrar
            // de la tabla tipos_pokemones.

            String sql_delete_relations = "DELETE FROM POKEMONS_TIPUS WHERE poke_id = ?";
            //Borramos todos los pokemos cuya id no tienen correspondencia con ningún registro de la tabla pokemons_tipus
            String sql_delete_orphan_pokemons = "DELETE FROM POKEMONS WHERE poke_id = ?";
            //Borramos todos los tipos cya id no tenga correspondencia con ningún registro de la tabla pokemon_tipus, ya que
            //se ha podido borrar algún pokemón que contenia más tipos dejando huerfanó de relación algún registro de la tabla
            //tipos
            String sql_delete_orphan_tipus = "DELETE FROM TIPUS WHERE tipus_id NOT IN (SELECT DISTINCT(tipus_id) FROM POKEMONS_TIPUS)";

            preparedStatement = c.prepareStatement(sql_delete_relations);
            preparedStatement.setInt(1, poke_id);

            int pokemons_tipus_rows_affected = preparedStatement.executeUpdate();

            preparedStatement = c.prepareStatement(sql_delete_orphan_pokemons);
            preparedStatement.setInt(1, poke_id);
            int pokemons_rows_affected = preparedStatement.executeUpdate();

            preparedStatement = c.prepareStatement(sql_delete_orphan_tipus);
            int tipus_rows_affected = preparedStatement.executeUpdate();

            System.out.println(pokemons_tipus_rows_affected + " registres afectats a la taula pokemons_tipus");
            System.out.println(pokemons_rows_affected + " registres afectats a la taula pokemons");
            System.out.println(tipus_rows_affected + " registres afectats a la taula tipus");

        }finally {
            preparedStatement.close();
            loadIdsFromDatabase();
        }

    }



/*
SELECT t.tipus_id, t.nom, p.poke_id, p.nom FROM TIPUS t
 JOIN POKEMONS_TIPUS pt
 ON t.tipus_id = pt.tipus_id
JOIN POKEMONS p
ON pt.poke_id = p.poke_id
WHERE t.tipus_id = 4;
 */
}
