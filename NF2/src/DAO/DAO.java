package DAO;

import HibernateResources.HibernateUtil;
import Pojos.Llibre;
import Pojos.Prestec;
import Pojos.PrestecPK;
import Pojos.Soci;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.lang.reflect.Field;
import java.util.List;

public class DAO {


    private static Session sesion;
    private static Transaction tx;

    /*############################################LLIBRE##############################################################*/

    public static String[] getLlibreColums(){
        Field [] fields = new Llibre().getClass().getDeclaredFields();
        String columns [] = new String[fields.length-1];


        for(int i = 0; i < fields.length-1; i++){
            columns[i] = fields[i].getName();
        }

        return columns;
    }

    public static long persistLlibre(Llibre llibre)throws HibernateException, ConstraintViolationException {

        long id = 0;
        iniciaOperacion();
        id = (Long) sesion.save(llibre);

        tx.commit();
        sesion.close();

        updateExemplars(llibre);

        return id;
    }

    public static void updateExemplars(Llibre llibre){

        int count = (int) getNumExemplars(llibre);

        iniciaOperacion();

        //Actualitzaem el número de exemplars existents a la base de dades
        String hql ="UPDATE Llibre set nombre_exemplars=:exemplars WHERE  titol=:titol and editorial=:editorial and "+
                "nombre_pagines=:pags and any_edicio=:any_edicio and autor=:autor";
        Query query = sesion.createQuery(hql);
        query.setParameter("exemplars",count);
        query.setString("titol",llibre.getTitol());
        query.setString("editorial",llibre.getEditorial());
        query.setInteger("pags",llibre.getNombre_pagines());
        query.setDate("any_edicio",llibre.getAny_edicio());
        query.setString("autor",llibre.getAutor());
        int result=query.executeUpdate();
        System.out.println("Rows affected: "+result);

        sesion.getTransaction().commit();
        sesion.close();

    }

    public static long getNumExemplars(Llibre llibre){
        iniciaOperacion();
        //Contem el número de llibres que hi ha en la base de dades
        String hql ="SELECT count(*) FROM Llibre llibre WHERE llibre.titol=:titol and llibre.editorial=:editorial and" +
                " llibre.nombre_pagines=:pags and llibre.any_edicio=:any_edicio and llibre.autor=:autor";
        Query query=sesion.createQuery(hql);
        query.setString("titol",llibre.getTitol());
        query.setString("editorial",llibre.getEditorial());
        query.setInteger("pags",llibre.getNombre_pagines());
        query.setDate("any_edicio",llibre.getAny_edicio());
        query.setString("autor",llibre.getAutor());
        long count=(Long)query.uniqueResult();
        System.out.println("Resultado del select count: "+(count));

        sesion.getTransaction().commit();
        sesion.close();

        return count;
    }

    public static Llibre obtenirLlibre(long llibreid) throws HibernateException, ConstraintViolationException{

        Llibre contacto = null;


        iniciaOperacion();


        contacto = (Llibre) sesion.get(Llibre.class, llibreid);

        sesion.flush();
        sesion.clear();

        sesion.close();

        return contacto;
    }

    public static List<Llibre> obtenirLlibres() throws HibernateException, ConstraintViolationException {

        List <Llibre>  llibres = null;

        iniciaOperacion();
        llibres = sesion.createQuery("from Llibre order by titol").list();

        tx.commit();
        sesion.close();

        return llibres;
    }

    public static List<Llibre> obtenirLlibresPrestables() throws HibernateException, ConstraintViolationException {

        List <Llibre>  llibres = null;
        String hql = "Select l FROM Llibre l WHERE l.id NOT IN ( SELECT p.llibre FROM Prestec p WHERE " +
                "p.data_Entrega_Efectiva IS NULL)";

        iniciaOperacion();
        Query query = sesion.createQuery(hql);


        llibres = query.list();

        tx.commit();
        sesion.close();

        return llibres;
    }

    public static boolean esUnLlibrePrestable(Llibre llibre){

        String hql = "SELECT count(*) FROM Prestec p WHERE " +
                "p.data_Entrega_Efectiva IS NULL AND p.llibre=:llibreID";

        iniciaOperacion();
        Query query = sesion.createQuery(hql);


        query.setLong("llibreID",llibre.getId());
        long count  = (Long) query.uniqueResult();
        System.out.println("Count "+count);

        tx.commit();
        sesion.close();

        return count == 0;

    }

    public static void updateLlibre(Llibre llibre) throws HibernateException , ConstraintViolationException{

        iniciaOperacion();

        sesion.update(llibre);
        tx.commit();

        sesion.close();

        updateExemplars(llibre);

    }


    public static void deleteLlibre(Llibre llibre)throws HibernateException , ConstraintViolationException{

        deletePrestecsFromLlibre(llibre);

        iniciaOperacion();

        sesion.delete(llibre);

        tx.commit();

        sesion.close();

        updateExemplars(llibre);

    }

    /*############################################LLIBRE##############################################################*/

    /*############################################SOCI##############################################################*/

    public static String [] getSociColumns(){

        Field [] fields = new Soci().getClass().getDeclaredFields();
        String columns [] =new String[fields.length-1];


        for(int i = 0; i < fields.length-1; i++){
            columns[i] = fields[i].getName();
        }

        return columns;
    }

    public static long persistSoci(Soci soci)throws HibernateException, ConstraintViolationException{

        long id = 0;

        iniciaOperacion();
        id= (Long)sesion.save(soci);

        tx.commit();
        sesion.close();

        return id;
    }

    public static Soci obtenirSoci (long SociId) throws HibernateException, ConstraintViolationException {

        Soci socio = null;

        iniciaOperacion();

        socio = (Soci) sesion.get(Soci.class, SociId);

        sesion.flush();
        sesion.clear();

        sesion.close();

        return socio;
    }

    public static List<Soci> obtenirSocis() throws HibernateException, ConstraintViolationException {

        List <Soci>  socis = null;


        iniciaOperacion();
        socis = sesion.createQuery("from Soci ").list();

        tx.commit();
        sesion.close();

        return socis;
    }

    public static void updateSoci(Soci soci) throws HibernateException , ConstraintViolationException{

        iniciaOperacion();

        sesion.update(soci);
        tx.commit();

        sesion.close();

    }

    public static void deleteSoci(Soci soci)throws HibernateException , ConstraintViolationException{

        deletePrestecsFromSoci(soci);

        iniciaOperacion();

        sesion.delete(soci);

        tx.commit();

        sesion.close();

    }

    /*############################################SOCI##############################################################*/

    /*############################################PRESTEC##############################################################*/

    public static String [] getPrestecColumns(){

        String columns [] = {"LLIBRE_ID","TITOL","AUTOR","SOCI_ID","COGNOM","DIRECCIO","INICI_PRESTEC","FI_PRESTEC",
        "ENTREGA_PRESTEC"};

        return columns;
    }

    public static PrestecPK persistPrestec(Prestec prestec)throws HibernateException, ConstraintViolationException{

        PrestecPK pk;

        iniciaOperacion();

        pk = (PrestecPK)sesion.save(prestec);
        //System.out.println("Aqui la info -> "+sesion.save(prestec).getClass().getName());

        tx.commit();
        sesion.close();

        return pk;
    }

    public static Prestec obtenirPrestec(PrestecPK pk){

        Prestec prestec = null;
        iniciaOperacion();

        prestec = (Prestec) sesion.get(Prestec.class, pk);
        tx.commit();
        sesion.close();

        return prestec;

    }

    public static List<Prestec> obtenirPrestecs(){

        List<Prestec> prestecs = null;

        iniciaOperacion();

        prestecs = sesion.createQuery("from Prestec ").list();

        tx.commit();
        sesion.close();

        return prestecs;
    }

    public static void updatePrestec(Prestec prestec)throws HibernateException , ConstraintViolationException{

        iniciaOperacion();

        sesion.update(prestec);
        tx.commit();

        sesion.close();

    }

    public static void deteletPrestec(Prestec prestec){

        iniciaOperacion();

        sesion.delete(prestec);
        tx.commit();

        sesion.close();
    }

    public static void deletePrestecsFromSoci(Soci soci){

        String hql = "DELETE FROM Prestec prestecs WHERE prestecs.soci=:soci";

        iniciaOperacion();
        Query query = sesion.createQuery(hql);

        query.setParameter("soci",soci);
        query.executeUpdate();

        tx.commit();

        sesion.close();
    }

    public static void deletePrestecsFromLlibre(Llibre llibre){

        String hql = "DELETE FROM Prestec prestecs WHERE prestecs.llibre=:llibre";

        iniciaOperacion();
        Query query = sesion.createQuery(hql);

        query.setParameter("llibre",llibre);
        query.executeUpdate();

        tx.commit();

        sesion.close();
    }

  /*############################################PRESTEC##############################################################*/


    private static void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory();
        tx = sesion.beginTransaction();
    }

    private static void manejaExcepcion(HibernateException he) throws HibernateException
    {
        tx.rollback();
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he.getCause());
    }


}
