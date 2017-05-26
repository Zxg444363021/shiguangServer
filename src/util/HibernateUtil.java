package util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
	private static final SessionFactory sessionFactory=buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try{
			//根据hibernate.cfg.xml创建SessionFactory对象
			Configuration configuration=new Configuration();
			SessionFactory sessionFactory=configuration.configure("hibernate.cfg.xml").buildSessionFactory();
			//创建ServiceRegistry实例
			
			return sessionFactory;
		}catch(Throwable ex){
			throw new ExceptionInInitializerError(ex);
		}
	}
	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}
}
