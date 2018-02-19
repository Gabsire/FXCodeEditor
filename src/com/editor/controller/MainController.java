package com.editor.controller;

import java.io.IOException;

import com.editor.service.FileOperationsService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MainController {

	@FXML
	TextArea codeText;
	
	@FXML
	TextArea rowNumbers;
	
	RowNumbersController rowNumbersController = new RowNumbersController();

	FileOperationsService fileOperationsService = new FileOperationsService();
	
	@FXML
    public void initialize() {
		rowNumbersController.initialize(rowNumbers, codeText);
    }

	@FXML
	public void save() {
		try {
			fileOperationsService.save(codeText);
		} catch (IOException e) {
			handleIOException("Error while saving file: ", e.getStackTrace());
		}
	}

	@FXML
	public void load() {
		try {
			fileOperationsService.load(codeText);
		} catch (IOException e) {
			handleIOException("Error while loading file: ", e.getStackTrace());
		}
	}

	@FXML
	public void close() {
		Platform.exit();
	}

	public void handleIOException(String message, StackTraceElement[] stackTraceElements) {
		System.out.println(message + stackTraceElements.toString());
	}

}
