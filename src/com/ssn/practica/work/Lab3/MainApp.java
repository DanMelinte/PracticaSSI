package com.ssn.practica.work.Lab3;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class MainApp {
	public static void main(String[] args) throws Exception {
		MainApp demo = new MainApp();
		demo.run();
	}

	private EntityManagerFactory sessionFactory;

	private void run() throws Exception {
		setUp();
		saveEntities();
		queryEntities();
		// updateEntity();
	}

	private void queryEntitity() {
		EntityManager entityManager = sessionFactory.createEntityManager();

		TypedQuery<Course> query = entityManager.createQuery("from Course where name = :nameP", Course.class);
		query.setParameter(0, "Romana");

		List<Course> result = query.getResultList();

		for (Course course : result) {
			System.out.println(course);
		}

		entityManager.close();
	}

	private void queryEntities() {
		EntityManager entityManager = sessionFactory.createEntityManager();
		List<Course> result = entityManager.createQuery("from Course", Course.class).getResultList();

		for (Course c : result) {
			System.out.println(c);
		}
		entityManager.close();
	}

	private void saveEntities() {

		EntityManager entityManager = sessionFactory.createEntityManager();
		entityManager.getTransaction().begin();

		Trainee trainee1 = new Trainee("Dan", 20);
		entityManager.persist(trainee1);

		Trainee trainee2 = new Trainee("Ion", 25);
		entityManager.persist(trainee2);

		Course course1 = new Course("Romana");
		entityManager.persist(course1);
		Course course2 = new Course("Matematica");
		entityManager.persist(course2);

		trainee1.getCourses().add(course1);
		trainee1.getCourses().add(course2);

		Evaluation ev1 = new Evaluation(10, course1, trainee1);
		Evaluation ev2 = new Evaluation(9, course2, trainee1);

		entityManager.persist(ev1);
		entityManager.persist(ev2);

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
