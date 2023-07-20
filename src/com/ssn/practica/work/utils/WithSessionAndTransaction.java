package com.ssn.practica.work.utils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public abstract class WithSessionAndTransaction<T> {

	private static SessionFactory sessionFactory;
	private T returnValue;

	protected String runtimeExceptionMessage;

	public String getRuntimeExceptionMessage() {
		return runtimeExceptionMessage;
	}

	public T run() {
		try {
			if (sessionFactory == null) {
				setUp();
			}

			Session session = sessionFactory.openSession();
			session.getTransaction().begin();

			doAction(session);

			session.getTransaction().commit();
			session.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			throw new MyRuntimeException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getReturnValue();
	}

	public abstract void doAction(Session session);

	protected void setUp() throws Exception {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			StandardServiceRegistryBuilder.destroy(registry);
			e.printStackTrace();
		}
	}

	public T getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(T returnValue) {
		this.returnValue = returnValue;
	}
}
