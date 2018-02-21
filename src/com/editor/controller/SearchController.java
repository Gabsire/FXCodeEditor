package com.editor.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.DocumentEvent.EventType;

import org.controlsfx.control.textfield.CustomTextField;
import org.fxmisc.richtext.CodeArea;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;

public class SearchController {

	@FXML
	ToolBar searchBar;

	@FXML
	CustomTextField searchField;

	@FXML
	Label searchLabel;

	private CodeArea codeArea;

	public void initialize() {
	
		searchField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				Pattern pattern = Pattern.compile(newValue);
				Matcher matcher = pattern.matcher(codeArea.getText());

				while(matcher.find()){
					codeArea.moveTo(matcher.start());
				}
			}
		});
	}

	@FXML
	public void close() {
		searchBar.managedProperty().bind(searchBar.visibleProperty());
		searchBar.setVisible(false);
	}

	public CodeArea getCodeArea() {
		return codeArea;
	}

	public void setCodeArea(CodeArea codeArea) {
		this.codeArea = codeArea;
	}

}
