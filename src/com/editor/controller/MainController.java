package com.editor.controller;

import java.io.IOException;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.StyledTextArea;

import com.editor.service.FileOperationsService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MainController {

	private CodeArea codeArea;

	@FXML
	TextArea rowNumbers;

	FileOperationsService fileOperationsService = new FileOperationsService();

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
