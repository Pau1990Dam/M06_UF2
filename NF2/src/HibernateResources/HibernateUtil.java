package HibernateResources;
/**
 * Created by pau on 27/01/16.
 */
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil
{
    private static final SessionFactory sessionFactory;
    private static final ServiceRegistry serviceRegistry;

    static
    {
        try
        {
            Configuration configuration = new Configuration();
            configuration.configure();
            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (ConstraintViolationException cve)
        {

            System.out.println("Violación de constraint: "+ cve.getLocalizedMessage());

            throw cve;
        } catch (HibernateException he)
        {

            System.err.println("Ocurrió un error en la inicialización de la SessionFactory: " + he);

            throw new ExceptionInInitializerError(he);

        }
    }

    public static Session getSessionFactory()
    {
        return sessionFactory.openSession();
    }

    public static void closeSession(){  sessionFactory.close(); }
}
