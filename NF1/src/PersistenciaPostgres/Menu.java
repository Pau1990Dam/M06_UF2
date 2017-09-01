package PersistenciaPostgres;

import java.sql.SQLException;
import java.util.Scanner;


public class Menu {

    public static void show() throws SQLException, ClassNotFoundException {
        Scanner entrada=new Scanner(System.in);

        int opcion=0;
        int respuesta;
        do{
            System.out.println();
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
            opcion = Integer.parseInt(entrada.nextLine());
            System.out.println();
            switch(opcion){
                case 1:
                    DAO.verPeliculas();
                    System.out.println("Pulse intro para volver al Menú.");
                    entrada.nextLine();
                    break;
                case 2:
                    DAO.verActores();
                    System.out.println("Pulse intro para volver al Menú.");
                    entrada.nextLine();
                    break;
                case 3:
                    DAO.verPersonajes();
                    System.out.println("Pulse intro para volver al Menú.");
                    entrada.nextLine();
                    break;
                case 4:
                    System.out.println("Indica la id del actor para ver el listado de las peliculas en las que ha" +
                            " actuado. ID del actor: ");
                    respuesta=entrada.nextInt();
                    System.out.println();
                    DAO.verPeliculasActor(respuesta);
                    System.out.println("Pulse intro para volver al Menú.");
                    entrada.nextLine();
                    break;
                case 5:
                    System.out.println("Indica la id de la pelicula  para ver los actores que ha participado en ella. " +
                            "ID  de la pelicula: ");
                    respuesta=entrada.nextInt();
                    System.out.println();
                    DAO.verActoresPelicula(respuesta);
                    System.out.println("Pulse intro para volver al Menú.");
                    entrada.nextLine();
                    break;
                case 6:
                    System.out.println("Indica la id del actor para ver todos los personajes que ha interpretado. " +
                            "ID del actor: ");
                    respuesta=entrada.nextInt();
                    DAO.verPersonajesActor(respuesta);
                    System.out.println("Pulse intro para volver al Menú.");
                    entrada.nextLine();
                    break;
                case 7:
                    System.out.println("Indica la id de la pelicula para ver los personajes que aparecen en ella. " +
                            "ID de la Pelicula : ");
                    respuesta=entrada.nextInt();
                    DAO.verPersonajesPelicula(respuesta);
                    System.out.println("Pulse intro para volver al Menú.");
                    entrada.nextLine();
                    break;
                case 8:
                    System.out.println("Indica la id del actor para ver todos los personajes que ha interpretado" +
                            "en sus respectivas peliculas. ID actor:");
                    respuesta=entrada.nextInt();
                    DAO.verPersonajesPeliculasActor(respuesta);
                    System.out.println("Pulse intro para volver al Menú.");
                    entrada.nextLine();
                    break;
                default:
                    System.out.println("ADEU!!!");
                    break;
            }

        }while(opcion>0&&opcion<9);

    }
}
