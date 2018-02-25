package main.com.editor.controller;

import java.io.IOException;

import org.controlsfx.control.textfield.CustomTextField;
import org.fxmisc.richtext.CodeArea;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import main.com.editor.persistence.PersistenceManager;
import main.com.editor.service.FileOperationsService;
import main.com.editor.service.SearchService;

public class MainController {

	private CodeArea codeArea;

	@FXML
	private TextArea rowNumbers;

	@FXML
	private Button closeSearchBtn;

	@FXML
	private Label itemsFoundLabel;

	@FXML
	private ToolBar searchBar;

	@FXML
	private CustomTextField searchField;

	@FXML
	private Label searchLabel;

	FileOperationsService fileOperationsService = new FileOperationsService();

	SearchService searchService = new SearchService();

	@FXML
	public void save() {
		fileOperationsService.save(codeArea);
	}

	@FXML
	public void load() {
		fileOperationsService.load(codeArea);
	}

	@FXML
	public void close() {
		PersistenceManager.saveDocuments();
		Platform.exit();
	}

	@FXML
	public void displaySearchBar() {
		searchService.display(searchBar);
	}

	@FXML
	public void closeSearchBar() {
		searchService.close(searchBar);
	}

	public void handleIOException(String message, IOException exception) {
		System.out.println(message + exception.getStackTrace());
	}

	public CodeArea getCodeArea() {
		return codeArea;
	}

	public void setCodeArea(CodeArea codeArea) {
		this.codeArea = codeArea;
	}

}
