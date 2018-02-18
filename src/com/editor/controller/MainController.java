package com.editor.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController {

	@FXML
	TextArea textArea;

	@FXML
	public void save() throws Exception {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save file");
		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");
		fileChooser.getExtensionFilters().add(extensionFilter);
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
	public void load() throws IOException {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Load file");
		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");
		fileChooser.getExtensionFilters().add(extensionFilter);
		File file = fileChooser.showOpenDialog(null);
		
		if (null != textArea && null != file) {

			StringBuilder content = new StringBuilder();
			BufferedReader r = Files.newBufferedReader(Paths.get(file.getPath()), StandardCharsets.UTF_8);
			r.lines().forEach(s -> content.append(s).append("\n"));
			textArea.setText(content.toString());
		}

	}

	@FXML
	public void close() {

	}

}
