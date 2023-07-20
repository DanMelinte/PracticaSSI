package com.ssn.practica.work.utils;

import java.awt.Checkbox;
import java.util.List;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;
import javax.persistence.TypedQuery;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.hibernate.Session;

import com.ssn.practica.work.App.Article;
import com.ssn.practica.work.App.Store;
import com.ssn.practica.work.App.Price;

public class DialogManager {

	private DataManager dataManager = new DataManager();
	private Display display;
	private Table table;
	private StringBuilder[] selectedCells = new StringBuilder[3];
	private boolean lowCost = false;
	private boolean exactMatch = false;
	private String currentStatus = "Whisper Your Desires :";

	public DialogManager(Display display, CTabFolder folder) {
		this.display = display;
	}

	public void openPriceDialog(CTabFolder folder) {
//		Shell shell = new Shell(display);
//		shell.setText("Article Dialog");
//		shell.setLayout(new GridLayout(4, false));

		CTabItem tabItem = new CTabItem(folder, SWT.CLOSE);
		tabItem.setText("Prices");

		Composite tabComposite = new Composite(folder, SWT.SHEET | SWT.APPLICATION_MODAL);
		tabComposite.setLayout(new GridLayout(4, false));
		tabItem.setControl(tabComposite);

		Text searchBox = new Text(tabComposite, SWT.BORDER);
		GridData searchBoxData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		searchBoxData.horizontalSpan = 3;
		searchBox.setLayoutData(searchBoxData);
		searchBox.setMessage("Whisper Your Desires :");

		Button searchButton = new Button(tabComposite, SWT.PUSH);
		searchButton.setText("search");
		searchButton.addListener(SWT.Selection, e -> {
			if (lowCost == true)
				searchInLowPrices(searchBox.getText());
			else
				searchInPrices(searchBox.getText());
		});

		searchButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Button lowCostArticle = new Button(tabComposite, SWT.CHECK);
		lowCostArticle.setText("low cost");
		lowCostArticle.addListener(SWT.Selection, e -> {
			if (lowCostArticle.getSelection()) {
				lowCost = true;
				searchBox.setMessage("low cost article :");
			} else {
				lowCost = false;
				searchBox.setMessage("Whisper Your Desires :");
			}
		});

		Button exactMatchButton = new Button(tabComposite, SWT.CHECK);
		exactMatchButton.setText("exact match");
		exactMatchButton.addListener(SWT.Selection, e -> {
			if (exactMatchButton.getSelection()) {
				exactMatch = true;
			} else {
				exactMatch = false;
			}
		});

		table = new Table(tabComposite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 100;
		data.horizontalSpan = 4;
		table.setLayoutData(data);

		String[] titles = { "Article", "Store", "Value" };
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		DataManager dataManager = new DataManager();

		List<Price> prices = dataManager.getAllPrices();

		for (int i = 0; i < prices.size(); i++) {
			Price price = prices.get(i);

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, price.getArticle().getName());
			item.setText(1, price.getStore().getName());
			item.setText(2, "" + price.getValue());
		}

		for (int i = 0; i < titles.length; i++) {
			table.getColumn(i).pack();
		}

		GridData textBoxData = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Text articleNameTXTBox = new Text(tabComposite, SWT.BORDER);
		textBoxData.horizontalSpan = 1;
		articleNameTXTBox.setLayoutData(textBoxData);
		articleNameTXTBox.setMessage("article");

		Text storeNameTXTBox = new Text(tabComposite, SWT.BORDER);
		textBoxData.horizontalSpan = 1;
		storeNameTXTBox.setLayoutData(textBoxData);
		storeNameTXTBox.setMessage("store");

		Text valueTXTBox = new Text(tabComposite, SWT.BORDER);
		textBoxData.horizontalSpan = 1;
		valueTXTBox.setLayoutData(textBoxData);
		valueTXTBox.setMessage("value");

		Button applyButton = new Button(tabComposite, SWT.PUSH);
		applyButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		GridData radioButtonsData = new GridData(SWT.FILL, SWT.LEFT, true, false);

		Button deleteButton = new Button(tabComposite, SWT.RADIO);
		deleteButton.setText("Delete");
		deleteButton.addListener(SWT.Selection, e -> {
			articleNameTXTBox.setEnabled(false);
			storeNameTXTBox.setEnabled(false);
			valueTXTBox.setEnabled(false);
		});
		deleteButton.setLayoutData(radioButtonsData);

		Button modifyButton = new Button(tabComposite, SWT.RADIO);
		modifyButton.setText("Modify");
		modifyButton.addListener(SWT.Selection, e -> {
			articleNameTXTBox.setEnabled(false);
			storeNameTXTBox.setEnabled(false);
			valueTXTBox.setEnabled(true);
			articleNameTXTBox.setText(selectedCells[0].toString());
			storeNameTXTBox.setText(selectedCells[1].toString());
			valueTXTBox.setText(selectedCells[2].toString());
		});
		modifyButton.setLayoutData(radioButtonsData);

		Button addButton = new Button(tabComposite, SWT.RADIO);
		addButton.setText("Add");
		addButton.addListener(SWT.Selection, e -> {
			articleNameTXTBox.setEnabled(true);
			storeNameTXTBox.setEnabled(true);
			valueTXTBox.setEnabled(true);
		});
		addButton.setLayoutData(radioButtonsData);

		applyButton.addListener(SWT.Selection, e -> {
			currentStatus = "Whisper Your Desires :";
			if (deleteButton.getSelection()) {
				deletePriceAction(articleNameTXTBox.getText(), storeNameTXTBox.getText(), valueTXTBox.getText());
			} else if (modifyButton.getSelection()) {
				modifyPriceAction(articleNameTXTBox.getText(), storeNameTXTBox.getText(), valueTXTBox.getText());
			} else if (addButton.getSelection()) {
				addPriceAction(articleNameTXTBox.getText(), storeNameTXTBox.getText(), valueTXTBox.getText());
			} else {
				currentStatus = "Select Procedure :";
			}
			searchBox.setMessage(currentStatus.toString());
		});
		applyButton.setText("Apply");

		SelectionCellListener(new Text[] { articleNameTXTBox, storeNameTXTBox, valueTXTBox });
	}

	public void openArticleDialog(CTabFolder folder) {
		// Shell shell = new Shell(display, );
		// shell.setText("Article Dialog");
		// shell.setLayout();

		CTabItem tabItem = new CTabItem(folder, SWT.CLOSE);
		tabItem.setText("Articles");

		Composite tabComposite = new Composite(folder, SWT.SHEET | SWT.APPLICATION_MODAL);
		tabComposite.setLayout(new GridLayout(3, false));
		tabItem.setControl(tabComposite);

		// =========================================================

		Text searchBox = new Text(tabComposite, SWT.BORDER);
		GridData textBoxData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		textBoxData.horizontalSpan = 2;
		searchBox.setLayoutData(textBoxData);
		searchBox.setMessage("Whisper Your Desires :");

		Button searchButton = new Button(tabComposite, SWT.PUSH);
		searchButton.setText("search");
		searchButton.addListener(SWT.Selection, e -> searchInArticles(searchBox.getText()));
		searchButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		table = new Table(tabComposite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 100;
		data.horizontalSpan = 3;
		table.setLayoutData(data);

		String[] titles = { "Name" };
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}
		
		DataManager dataManager = new DataManager();
		List<Article> articles = dataManager.getAllArticles();

		for (Article a : articles) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, a.getName());
		}

		for (int i = 0; i < titles.length; i++) {
			table.getColumn(i).pack();
		}

		Text textBox = new Text(tabComposite, SWT.BORDER);
		textBoxData.horizontalSpan = 2;
		textBox.setLayoutData(textBoxData);
		textBox.setMessage("article name : ");

		Button applyButton = new Button(tabComposite, SWT.PUSH);

		GridData radioButtonsData = new GridData(SWT.FILL, SWT.LEFT, true, false);

		Button deleteButton = new Button(tabComposite, SWT.RADIO);
		deleteButton.setText("Delete");
		deleteButton.addListener(SWT.Selection, e -> textBox.setEnabled(false));
		deleteButton.setLayoutData(radioButtonsData);

		Button modifyButton = new Button(tabComposite, SWT.RADIO);
		modifyButton.setText("Modify");
		modifyButton.addListener(SWT.Selection, e -> textBox.setEnabled(true));
		modifyButton.setLayoutData(radioButtonsData);

		Button addButton = new Button(tabComposite, SWT.RADIO);
		addButton.setText("Add");
		addButton.addListener(SWT.Selection, e -> textBox.setEnabled(true));
		addButton.setLayoutData(radioButtonsData);

		applyButton.addListener(SWT.Selection, e -> {
			if (deleteButton.getSelection()) {
				deleteArticleAction(textBox.getText());
			} else if (modifyButton.getSelection()) {
				modifyArticleAction(textBox.getText());
			} else if (addButton.getSelection()) {
				addArticleAction(textBox.getText());
			}
		});
		applyButton.setText("Apply");

		SelectionCellListener(new Text[] { textBox });
	}

	public void openStoreDialog(CTabFolder folder) {
//		Shell shell = new Shell(display);
//		shell.setText("Stores Dialog");
//		shell.setLayout(new GridLayout(3, false));

		CTabItem tabItem = new CTabItem(folder, SWT.CLOSE);
		tabItem.setText("Stores");

		Composite tabComposite = new Composite(folder, SWT.SHEET | SWT.APPLICATION_MODAL);
		tabComposite.setLayout(new GridLayout(3, false));
		tabItem.setControl(tabComposite);

		Text searchBox = new Text(tabComposite, SWT.BORDER);
		GridData textBoxData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		textBoxData.horizontalSpan = 2;
		searchBox.setLayoutData(textBoxData);

		Button searchButton = new Button(tabComposite, SWT.PUSH);
		searchButton.setText("search");
		searchButton.addListener(SWT.Selection, e -> searchInStores(searchBox.getText()));
		searchButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		searchBox.setMessage("Whisper Your Desires :");

		table = new Table(tabComposite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 100;
		data.horizontalSpan = 3;
		table.setLayoutData(data);

		String[] titles = { "Name" };
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		DataManager dataManager = new DataManager();
		List<Store> stores = dataManager.getAllStores();

		for (Store m : stores) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, m.getName());
		}

		for (int i = 0; i < titles.length; i++) {
			table.getColumn(i).pack();
		}

		Text textBox = new Text(tabComposite, SWT.BORDER);
		textBox.setLayoutData(textBoxData);
		textBox.setMessage("store name : ");

		Button applyButton = new Button(tabComposite, SWT.PUSH);

		GridData radioButtonsData = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Button deleteButton = new Button(tabComposite, SWT.RADIO);
		deleteButton.setText("Delete");
		deleteButton.addListener(SWT.Selection, e -> textBox.setEnabled(false));
		deleteButton.setLayoutData(radioButtonsData);

		Button modifyButton = new Button(tabComposite, SWT.RADIO);
		modifyButton.setText("Modify");
		modifyButton.addListener(SWT.Selection, e -> textBox.setEnabled(true));
		modifyButton.setLayoutData(radioButtonsData);

		Button addButton = new Button(tabComposite, SWT.RADIO);
		addButton.setText("Add");
		addButton.addListener(SWT.Selection, e -> textBox.setEnabled(true));
		addButton.setLayoutData(radioButtonsData);

		applyButton.addListener(SWT.Selection, e -> {
			if (deleteButton.getSelection()) {
				deleteStoreAction(textBox.getText());
			} else if (modifyButton.getSelection()) {
				modifyStoreAction(textBox.getText());
			} else if (addButton.getSelection()) {
				addStoreAction(textBox.getText());
			}
		});
		applyButton.setText("Apply");
		applyButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		SelectionCellListener(new Text[] { textBox });
	}

	public void SelectionCellListener(Text[] textBoxes) {
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				TableItem[] columns = table.getSelection();

				if (columns.length > 0) {
					for (int i = 0; i < table.getColumnCount(); i++) {
						String cellText = columns[0].getText(i);
						textBoxes[i].setText(cellText);
						selectedCells[i] = new StringBuilder(cellText);
					}
				}
			}
		});
	}

	// Price

	public void deletePriceAction(String articleName, String storeName, String value) {
		try {
			new WithSessionAndTransaction() {
				@Override
				public void doAction(Session session) {
					Price price = dataManager.getPriceByNames(articleName, storeName, session);
					if (price != null)
						session.delete(price);
					else
						currentStatus = "Select entity to delete";
				}
			}.run();
		} catch (MyRuntimeException e) {
			currentStatus = e.getMessage();
		}
		refreshPricesTable();
	}

	public void addPriceAction(String articleName, String storeName, String value) {
		try {
			new WithSessionAndTransaction() {
				@Override
				public void doAction(Session session) {

					if (articleName.isEmpty() || storeName.isEmpty() || value.isEmpty())
						throw new RuntimeException("Complete all boxes");

					dataManager.newPrice(Integer.parseInt(value), storeName, articleName);
				}

			}.run();
		} catch (MyRuntimeException e) {
			currentStatus = e.getMessage();
		}
		refreshPricesTable();
	}

	public void modifyPriceAction(String articleName, String storeName, String value) {
		try {
			new WithSessionAndTransaction() {
				@Override
				public void doAction(Session session) {
					dataManager.updatePrice(Integer.parseInt(value), storeName, articleName, selectedCells);
				}
			}.run();
		} catch (MyRuntimeException e) {
			currentStatus = e.getMessage();
		}
		refreshPricesTable();
	}

	public void searchInPrices(String searchedText) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {

				TypedQuery<Price> query;

				query = session.createQuery("select p from Price p " + "join Article a on a.id = p.article.id "
						+ "join Store s on s.id = p.store.id "
						+ "where p.value = :searchedValue or a.name like :searchedName or s.name like :searchedName",
						Price.class);

				try {
					int a = Integer.parseInt(searchedText);
					query.setParameter("searchedValue", a);
				} catch (NumberFormatException e) {
					query.setParameter("searchedValue", null);
				}

				if (exactMatch)
					query.setParameter("searchedName", searchedText);
				else
					query.setParameter("searchedName", "%" + searchedText + "%");

				List<Price> results = query.getResultList();
				table.removeAll();
				for (Price p : results) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, p.getArticle().getName());
					item.setText(1, p.getStore().getName());
					item.setText(2, p.getValue() + "");
				}
			}
		}.run();
	}

	public void searchInLowPrices(String searchedText) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {

				List<Price> lowCostArticles = dataManager.getLowCostArticles();

				if (exactMatch) {
					lowCostArticles.removeIf(e -> !e.getArticle().getName().equals(searchedText));
				} else {
					lowCostArticles.removeIf(e -> !e.getArticle().getName().contains(searchedText));
				}

				table.removeAll();
				for (Price p : lowCostArticles) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, p.getArticle().getName());
					item.setText(1, p.getStore().getName());
					item.setText(2, p.getValue() + "");

				}
			}
		}.run();
	}

	public void refreshPricesTable() {
		table.removeAll();
		List<Price> prices = dataManager.getAllPrices();
		for (Price p : prices) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, p.getArticle().getName());
			item.setText(1, p.getStore().getName());
			item.setText(2, "" + p.getValue());
		}
	}

	// Article

	public void deleteArticleAction(String text) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {
				Article article = dataManager.getArticleByName(text, session);
				if (article != null) {
					session.delete(article);
				}
			}
		}.run();
		refreshArticleTable();
	}

	public void modifyArticleAction(String text) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {
				dataManager.updateArticle(text, selectedCells);
			}
		}.run();
		refreshArticleTable();
	}

	public void addArticleAction(String text) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {
				try {

					dataManager.newArticle(text);
				} catch (MyRuntimeException e) {
					currentStatus = e.getMessage();
				}
			}
		}.run();
		refreshArticleTable();
	}

	public void searchInArticles(String searchedText) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {
				TypedQuery<Article> query = session.createQuery("from Article where name like :name", Article.class);
				query.setParameter("name", "%" + searchedText + "%");
				List<Article> results = query.getResultList();

				table.removeAll();
				for (Article a : results) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, a.getName());
				}
			}
		}.run();
	}

	public void refreshArticleTable() {
		table.removeAll();
		List<Article> articles = dataManager.getAllArticles();
		for (Article m : articles) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, m.getName());
		}
	}

	// Store

	public void deleteStoreAction(String text) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {
				Store store = dataManager.getStoreByName(text, session);
				if (store != null) {
					session.delete(store);
				}
			}
		}.run();
		refreshStoreTable();
	}

	public void modifyStoreAction(String text) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {
				dataManager.updateStore(text, selectedCells);
			}
		}.run();
		refreshStoreTable();
	}

	public void addStoreAction(String text) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {
				dataManager.newStore(text);
			}
		}.run();
		refreshStoreTable();
	}

	public void searchInStores(String searchedText) {
		new WithSessionAndTransaction() {
			@Override
			public void doAction(Session session) {
				TypedQuery<Store> query = session.createQuery("from Store where name like :name", Store.class);
				query.setParameter("name", "%" + searchedText + "%");
				List<Store> results = query.getResultList();

				table.removeAll();
				for (Store s : results) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, s.getName());
				}
			}
		}.run();
	}

	public void refreshStoreTable() {
		table.removeAll();
		List<Store> stores = dataManager.getAllStores();
		for (Store m : stores) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, m.getName());
		}
	}

}
