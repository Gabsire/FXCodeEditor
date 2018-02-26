package main.java.com.editor.service;

import javafx.scene.control.ToolBar;

public class SearchService {

	public void close(ToolBar searchBar){
		searchBar.managedProperty().bind(searchBar.visibleProperty());
		searchBar.setVisible(false);
	}
	
	public void display(ToolBar searchBar){
		searchBar.managedProperty().bind(searchBar.visibleProperty());
		searchBar.setVisible(true);
	}
}
