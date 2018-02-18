package com.editor.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController {
	
	@FXML
	TextArea textArea;
	
	@FXML
	public void save() throws Exception{
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(null);
		
		BufferedReader reader = new BufferedReader(new StringReader(textArea.getText()));
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		reader.lines().forEach(s -> {
				try {
					writer.write(s);
					writer.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
		});	
		reader.close();
		writer.close();
		
		
	}
	
	@FXML
	public void load(){
		
	}
	
	@FXML
	public void close(){
		
	}

}
