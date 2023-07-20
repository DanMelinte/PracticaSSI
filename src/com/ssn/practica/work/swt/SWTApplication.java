package com.ssn.practica.work.swt;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.*;

import org.eclipse.swt.widgets.*;

import com.ssn.practica.work.utils.DialogManager;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SWTApplication {

	private DialogManager dialogManager;
	private Set<CTabItem> Tabs = new HashSet<CTabItem>();
	CTabFolder folder;

	public static void main(String[] args) {
		SWTApplication app = new SWTApplication();
		app.run();
	}

	public void run() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		shell.setText("Application");

		folder = new CTabFolder(shell, SWT.BORDER);
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		folder.setSimple(false);

		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);

		MenuItem fileItem = new MenuItem(bar, SWT.CASCADE);
		fileItem.setText("&Administration");

		Menu adminMenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(adminMenu);

		dialogManager = new DialogManager(display, folder);

		MenuItem viewItem = new MenuItem(adminMenu, SWT.CASCADE);
		viewItem.setText("Tables");
		Menu viewItemMenu = new Menu(bar);
		viewItem.setMenu(viewItemMenu);

		MenuItem showArticlesItem = new MenuItem(viewItemMenu, SWT.PUSH);
		showArticlesItem.addListener(SWT.Selection, e -> dialogManager.openArticleDialog(folder));
		showArticlesItem.setText("Articles");

		MenuItem showStoresItem = new MenuItem(viewItemMenu, SWT.PUSH);
		showStoresItem.addListener(SWT.Selection, e -> dialogManager.openStoreDialog(folder));
		showStoresItem.setText("Stores");

		MenuItem showPricesItem = new MenuItem(viewItemMenu, SWT.PUSH);
		showPricesItem.addListener(SWT.Selection, e -> dialogManager.openPriceDialog(folder));
		showPricesItem.setText("Prices");

		shell.setSize(400, 350);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void addNewTab(Control control, String tabTitle) {
		CTabItem item = new CTabItem(folder, SWT.CLOSE);
		item.setText(tabTitle);
		item.setControl(control);
	}
}