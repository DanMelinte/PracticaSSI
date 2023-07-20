package com.ssn.practica.work.hibernate;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateDemo {
	public static void main(String[] args) throws Exception {
		HibernateDemo demo = new HibernateDemo();
		demo.run();
	}

	private EntityManagerFactory sessionFactory;

	private void run() throws Exception {
		setUp();
		saveEntities();
		queryEntities();
	}

	private void queryEntities() {
		EntityManager entityManager = sessionFactory.createEntityManager();
		List<Event> result = entityManager.createQuery("from Event", Event.class).getResultList();
		for (Event event : result) {
			System.out.println("Event (" + event.getDate() + ") : " + event.getTitle());
		}
		entityManager.close();
	}

	private void saveEntities() {
		EntityManager entityManager = sessionFactory.createEntityManager();

		entityManager.getTransaction().begin();

		entityManager.persist(new Event("Our very first event!", new Date()));
		entityManager.persist(new Event("A follow up event", new Date()));

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	protected void setUp() throws Exception {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			StandardServiceRegistryBuilder.destroy(registry);
			e.printStackTrace();
		}
	}
}
