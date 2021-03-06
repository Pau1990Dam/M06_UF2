package PersistenciaPostgres;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;




public class themovieDBproject {

    private static HashSet<Integer>noIdRepetidas=new HashSet<>();

    public static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    public static void main(String[] args) {

        Connection c = null;

        try {

            System.out.print("Estableciendo conexión con ");
            DBConnection.showConnectionTargetInfo();

            c = DBConnection.connect();

            System.out.println("Conexión establecida");

            createSQLite.crearBD(c);
            System.out.println("Tablas creadas");

            System.out.println("Llenando tablas...");
            if(!areTablasLlenas()){
                inserts();
            }
            System.out.println("Tablas llenas");

            Menu.show();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No me apetece tratar las excepciones, pero igualmente las dejo aquí por si me apetece " +
                    "hacer algo con ellas en un futuro.");

        } catch (Exception x) {
            x.printStackTrace();
        }

    }

    private static boolean areTablasLlenas() throws SQLException, ClassNotFoundException {

        return (DAO.countTableRows("peliculas") > 0) && (DAO.countTableRows("actores") > 0)
                && (DAO.countTableRows("actores_peliculas") > 0);
    }

    public static void inserts(){

        String jsonEnCadena;
        String api_key = "3abc6154c470ac598df9e7d97700f8cd";

        for (int i = 0; i < 10; i++) {
            int peli = 600 + i;
            String film = String.valueOf(peli);
            String peticioPersonatges = "https://api.themoviedb.org/3/movie/" + film + "/credits?api_key=" + api_key;
            String peticioFilm = "https://api.themoviedb.org/3/movie/" + film + "?api_key=" + api_key + "&language=es";

            try {
                jsonEnCadena = getHTML(peticioFilm);
                Pelis(jsonEnCadena, peli);
                jsonEnCadena = getHTML(peticioPersonatges);
                Personajes(jsonEnCadena, peli);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("La peli " + film + " no existeix");
            }
        }
    }

    public static void Pelis (String json, int id_peli) throws SQLException, ClassNotFoundException {

        Object objJson =JSONValue.parse(json);
        JSONObject peli=(JSONObject)objJson;
        JSONArray peliAtributos;


        String titulo_original=(String)peli.get("original_title");
        String titulo_españa=(String)peli.get("title");
        String lanzamiento=(String)peli.get("release_date");
        int duracion=(int)((long)peli.get("runtime"));
        String generos="";
        String produccionOrigen="";

        System.out.println("ID: "+id_peli);
        System.out.println("Título original: "+titulo_original);
        System.out.println("Título: "+titulo_españa);
        System.out.println("Fecha de lanzamiento: "+lanzamiento);
        System.out.println("Duración: "+duracion+" min");

        peliAtributos=(JSONArray ) peli.get("genres");
        Iterator<JSONObject> iterator = peliAtributos.iterator();
        while (iterator.hasNext()) {
            JSONObject genero =  iterator.next();
            generos+=(genero.get("name") + "\t") ;
        }

        System.out.println("Géneros: " + generos);

        peliAtributos=(JSONArray ) peli.get("production_countries");
        iterator=peliAtributos.iterator();
        while (iterator.hasNext()) {
            JSONObject produccionPaises = iterator.next();
            produccionOrigen+=(produccionPaises.get("name") + "\t") ;
        }

        DAO.insertPelis(id_peli,titulo_original,titulo_españa,produccionOrigen,lanzamiento,generos,duracion);

        System.out.println("Paises: "+produccionOrigen);
        System.out.println();
    }

    public static void Personajes (String json, int peli_id) throws SQLException, ClassNotFoundException {

        Object objJson =JSONValue.parse(json);
        JSONObject getCasting=(JSONObject)objJson;
        JSONArray arrCasting=(JSONArray)getCasting.get("cast");

        Iterator<JSONObject> iterator = arrCasting.iterator();
        while (iterator.hasNext()) {

            JSONObject casting = iterator.next();
            String personaje=(String)casting.get("character");
            int actor_id=(int) ((long)casting.get("id"));
            String nombreActor=(String)casting.get("name");
            if(!noIdRepetidas.contains(actor_id)) {
                noIdRepetidas.add((actor_id));
                DAO.insertActores(actor_id,nombreActor);
            }
            DAO.insertActores_Peliculas(actor_id,peli_id,personaje);

        }
        System.out.println();

    }

}
