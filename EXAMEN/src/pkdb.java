import org.apache.http.client.fluent.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.Scanner;

public class pkdb {


    public static String getHTML(String urlAddress) throws Exception{
        String data = "";
        try {
            data = Request.Get("http://pokeapi.co/api/v1/" + urlAddress).execute().returnContent().asString();
          //  System.out.println(data);
        } catch (Exception e){
            e.printStackTrace();
        }

        return data;

    }

    /*
            MAIN: Tria la funció que vols realitzar de manera pulida.
            Un cop i sortir.
     */

    public static void main(String[] args){

        try {
            int opcio;
            Database.setDataBaseName("Pokemons2.db");
            Connection connection = null;

            connection = Database.getInstance();


            createSQLite.crearBD(connection);

            DAO.loadIdsFromDatabase();

            insertFirst15Pokemons();

        do{
            System.out.println();
            System.out.println("Tria una opció:");
            System.out.println("1. Insereix pokemon");
            System.out.println("2. Mostra pokemons");
            System.out.println("3. Mostra tipus");
            System.out.println("4. Mostra pokemons d'un tipus");
            System.out.println("5. Mostra info d'un pokemon");
            System.out.println("6. Elimina tots els pokemons d'un tipus.");
            System.out.println("7. Elimina un pokemon de la base de dades.");
            System.out.println("Altres: Sortir");

            Scanner sc = new Scanner(System.in);
            opcio = Integer.parseInt(sc.nextLine());


                switch (opcio) {
                    case 1:
                        insereix_poke();
                        System.out.println("\nPrem la tecla intro per tornar a menú...");
                        sc.nextLine();
                        break;
                    case 2:
                        mostraPokes();
                        System.out.println("\nPrem la tecla intro per tornar a menú...");
                        sc.nextLine();
                        break;
                    case 3:
                        mostraTipus();
                        System.out.println("\nPrem la tecla intro per tornar a menú...");
                        sc.nextLine();
                        break;
                    case 4:
                        mostraPokesperTipus();
                        System.out.println("\nPrem la tecla intro per tornar a menú...");
                        sc.nextLine();
                        break;
                    case 5:
                        mostrarPokeInfo();
                        System.out.println("\nPrem la tecla intro per tornar a menú...");
                        sc.nextLine();
                        break;
                    case 6:
                        deleteTipus();
                        System.out.println("\nPrem la tecla intro per tornar a menú...");
                        sc.nextLine();
                        break;
                    case 7:
                        deletePokemon();
                        System.out.println("\nPrem la tecla intro per tornar a menú...");
                        sc.nextLine();
                        break;
                    default:
                        connection.close();
                        System.out.println("Que tingui un bon dia!");

                }
        }while (opcio >=1 && opcio <8);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Implementaada la funció que insereix pokemons del 400 al 450
     * @throws Exception
     */

    public static void insereix_poke() throws Exception{

        int poke_id;

        Scanner sc = new Scanner(System.in);
        System.out.println("Introdueix la Id, de 1 a 718, del pokemón que vulguis inserir a la bd: ");

        poke_id = sc.nextInt();

        if(!DAO.updatePoke_id(poke_id)) {
            System.out.println("Aquest pokemón ja está a la bd");
            return;
        }

        Pk pk2 = getPoke(poke_id);
        DAO.inserirPokemon(pk2);


        pk2.imprimir();


    }

    public static Pk getPoke(int k) throws Exception {

        String url = "pokemon/"+k+"/";
        String jsonurl = getHTML(url);
        return SJPokeApi(jsonurl);

    }

    public static void mostraPokes() throws Exception{
        DAO.mostrarPokemons();
    }

    public static void mostraTipus() throws Exception{
        DAO.mostrarTipus();
    }

    public static void mostraPokesperTipus() throws Exception{

        Scanner entrada = new Scanner(System.in);
        int id_tipus;
        System.out.println("Tipus enmagatzemats a la bd");
        DAO.mostrarTipus();
        System.out.print("\nInsereix la id del tipus(2na columna ) , per veure els pokemons corresponents -> ");
        id_tipus = entrada.nextInt();
        entrada.nextLine();

        if(!DAO.isTipusAlreadyInTheDB(id_tipus)) {
            System.out.println("Aquest tipus no esta en la bd");
            return;
        }

        DAO.mostrarPokemonsTipus(id_tipus);

    }

    private static void mostrarPokeInfo() throws SQLException, ClassNotFoundException {

        Scanner entrada = new Scanner(System.in);
        int poke_id;

        System.out.println("Pokemons enmagatzemats a la bd:");
        DAO.mostrarPokemons();
        System.out.print("\nInsereix la id del pokemon(2na columna), per veure al seva informació al complet -> ");
        poke_id = entrada.nextInt();

        if(!DAO.isPokemonAlreadyInTheDB(poke_id)){
            System.out.println("Aquest pokemon no esta en la bd");
            return;
        }

        DAO.mostrarPokeMonInfo(poke_id);
    }

    private static void insertFirst15Pokemons() throws Exception {
        for(int i = 1; i<16; i++){

            if(DAO.isPokemonAlreadyInTheDB(i))continue;

            Pk pk = getPoke(i);
            DAO.updatePoke_id(i);

            pk.imprimir();

            DAO.inserirPokemon(pk);
        }
    }

    private static void deleteTipus() throws SQLException, ClassNotFoundException {

        Scanner entrada = new Scanner(System.in);
        int id_tipus;
        System.out.println("Tipus enmagatzemats a la bd");
        DAO.mostrarTipus();
        System.out.print("\nInsereix la id del tipus(2na columna ) , per eliminar-la conjuntament amb els pokemons" +
                " que hi pertanyen a aquest -> ");
        id_tipus = entrada.nextInt();
        entrada.nextLine();

        if(DAO.isTipusAlreadyInTheDB(id_tipus)){
            DAO.deleteTipusCascade(id_tipus);
        }else{
            System.out.println("No existeix a la base de dades cap tipus amb id "+id_tipus);
        }
    }

    private static void deletePokemon() throws SQLException, ClassNotFoundException {

        Scanner entrada = new Scanner(System.in);
        int poke_id;

        System.out.println("Pokemons enmagatzemats a la bd:");
        DAO.mostrarPokemons();
        System.out.print("\nInsereix la id del pokemon que vols eliminar -> ");
        poke_id = entrada.nextInt();

        if(DAO.isPokemonAlreadyInTheDB(poke_id)) {
            DAO.deletePokemonCascade(poke_id);
        }else
            System.out.println("No existeix a la base de dades cap pokemon amb id "+poke_id);

    }



    public static Pk SJPokeApi(String cadena){
        Object obj = JSONValue.parse(cadena);
        JSONObject jobj = (JSONObject)obj; // Contiene toda la información del JSON
        String pknom = (String)jobj.get("name");
        String id = Long.toString((Long)jobj.get("national_id"));
        JSONArray jarray = (JSONArray)jobj.get("types");
        String mmg[] = new String[jarray.size()];

        int mmg_id[] = new int[jarray.size()];

        for (int i = 0; i < jarray.size(); i++) {
            JSONObject jobjda = (JSONObject)jarray.get(i);
            mmg[i] = (String)jobjda.get("name");
            mmg_id[i] = tratar_cadena_id_tipo((String)jobjda.get("resource_uri"));
        }
        return new Pk(pknom,id,mmg,mmg_id);
    }

    private static int tratar_cadena_id_tipo(String cadena){
        String definitiva = cadena.substring(13,cadena.length()-1);
       // System.out.println(definitiva);
        return  Integer.parseInt(definitiva);
    }


}
