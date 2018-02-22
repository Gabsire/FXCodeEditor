package com.editor.controller;

import java.io.IOException;

import org.controlsfx.control.textfield.CustomTextField;
import org.fxmisc.richtext.CodeArea;

import com.editor.service.FileOperationsService;
import com.editor.service.SearchService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;

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
		try {
			fileOperationsService.save(codeArea);
		} catch (IOException e) {
			handleIOException("Error while saving file: ", e.getStackTrace());
		}
	}

	@FXML
	public void load() {
		try {
			fileOperationsService.load(codeArea);
		} catch (IOException e) {
			handleIOException("Error while loading file: ", e.getStackTrace());
		}
	}

	@FXML
	public void close() {
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

	public void handleIOException(String message, StackTraceElement[] stackTraceElements) {
		System.out.println(message + stackTraceElements.toString());
	}

	public CodeArea getCodeArea() {
		return codeArea;
	}

	public void setCodeArea(CodeArea codeArea) {
		this.codeArea = codeArea;
	}

}
