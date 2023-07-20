package com.ssn.practica.work.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import javax.management.RuntimeErrorException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.ssn.practica.work.App.Article;
import com.ssn.practica.work.App.Store;
import com.ssn.practica.work.App.Price;

public class DataManager {

	public void newArticle(String name) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {
				Article aricle = getArticleByName(name, session);

				if (aricle != null) {
					throw new RuntimeException("Article already exists: " + name);
				}

				Article art = new Article(name);
				session.persist(art);
			}
		}.run();
	}

	public void newStore(String name) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {
				Store store = getStoreByName(name, session);
				if (store != null) {
					throw new RuntimeException("Store already exists: " + name);
				}
				Store mag = new Store(name);
				session.persist(mag);
			}
		}.run();
	}

	public void newPrice(int price, String storeName, String artName) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {

				Store store = getStoreByName(storeName, session);
				if (store == null) {
					throw new RuntimeException("Store does not exist: " + storeName);
				}

				Article article = getArticleByName(artName, session);
				if (article == null) {
					throw new RuntimeException("Article does not exist: " + artName);
				}

				Price priceExistence = getPriceByNames(artName, storeName, session);

				if (priceExistence == null) {
					Price p = new Price(price, store, article);
					session.persist(p);
					System.out.println(p);
				} else {
					throw new RuntimeException("Price already exist: " + artName);
				}
			}
		}.run();
	}

	//

	public void updateStore(String name, StringBuilder[] selectedCells) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {
				Store store = getStoreByName(selectedCells[0].toString(), session);
				if (getStoreByName(name, session) != null) {
					throw new RuntimeException("Store already exists: " + name);
				}
				store.setName(name);
				session.update(store);
			}
		}.run();
	}

	public void updateArticle(String name, StringBuilder[] selectedCells) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {
				Article article = getArticleByName(selectedCells[0].toString(), session);
				if (getArticleByName(name, session) != null) {
					throw new RuntimeException("Article already exists: " + name);
				}
				article.setName(name);
				session.update(article);
			}
		}.run();
	}

	public void updatePrice(int value, String newStoreName, String newArtName, StringBuilder[] oldData) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {
				Price price = getPriceByNames(oldData[0].toString(), oldData[1].toString(), session);

				System.out.println(price);

				if (value == price.getValue()) {
					throw new RuntimeException(
							"Price already exists: " + newArtName + ", " + newStoreName + ", " + value);
				}

				price.setValue(value);
				session.update(price);
				System.out.println("Price was updated [" + oldData[2] + " -> " + price.getValue() + "]");
			}
		}.run();
	}

	//

	public List<Price> getLowCostArticles() {
		return new WithSessionAndTransaction<List<Price>>() {
			@Override
			public void doAction(Session session) {

				List<Price> finalResults = new ArrayList<Price>();

				for (Article a : getAllArticles()) {
					TypedQuery<Price> query = session.createQuery("SELECT p "
							+ "FROM Article a JOIN Price p ON a.id = p.article.id "
							+ "JOIN Store s ON s.id = p.store.id "
							+ "WHERE p.value = (SELECT MIN(price.value) FROM Price price WHERE price.article.id = a.id) "
							+ "AND a.name = :artName", Price.class);

					query.setParameter("artName", a.getName());

					List<Price> results = query.getResultList();
					for (Price price : results) {
						finalResults.add(price);
					}
				}

				setReturnValue(finalResults);
			}
		}.run();
	}

	//

	public Store getStoreByName(String name, Session session) {
		Query<Store> query = session.createQuery("from Store where name = :name", Store.class);
		query.setParameter("name", name);
		Store store = query.uniqueResult();
		return store;
	}

	public Article getArticleByName(String articleName, Session session) {
		Query<Article> query = session.createQuery("from Article where name = :name", Article.class);
		query.setParameter("name", articleName);
		Article article = query.uniqueResult();
		return article;
	}

	public Price getPriceByNames(String articleName, String storeName, Session session) {
		TypedQuery<Price> query = session.createQuery("SELECT p FROM Price p " + "JOIN p.article a " + "JOIN p.store s "
				+ "WHERE a.name = :articleName AND s.name = :storeName", Price.class);

		query.setParameter("articleName", articleName);
		query.setParameter("storeName", storeName);

		try {
			Price result = query.getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
		}
	}

	//

	private List<Article> getArticles(Session session) {
		Query<Article> query = session.createQuery("from Article", Article.class);
		return query.list();
	}

	public List<Article> getAllArticles() {
		return new WithSessionAndTransaction<List<Article>>() {
			@Override
			public void doAction(Session session) {
				setReturnValue(getArticles(session));
			}
		}.run();
	}

	private List<Store> getStores(Session session) {
		Query<Store> query = session.createQuery("from Store", Store.class);
		return query.list();
	}

	public List<Store> getAllStores() {
		return new WithSessionAndTransaction<List<Store>>() {
			@Override
			public void doAction(Session session) {
				setReturnValue(getStores(session));
			}
		}.run();

	}

	private List<Price> getPrices(Session session) {
		Query<Price> query = session.createQuery("from Price", Price.class);
		return query.list();
	}

	public List<Price> getAllPrices() {
		return new WithSessionAndTransaction<List<Price>>() {
			@Override
			public void doAction(Session session) {
				setReturnValue(getPrices(session));
			}
		}.run();
	}

}