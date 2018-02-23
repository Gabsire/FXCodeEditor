package com.editor.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.fxmisc.richtext.CodeArea;

import com.editor.persistance.PersistanceManager;

import javafx.stage.FileChooser;

public class FileOperationsService {

	public Boolean save(CodeArea codeArea) throws IOException {

		FileChooser fileChooser = initializeFileChooser();
		File file = fileChooser.showSaveDialog(null);

		if (null != codeArea && null != file) {

			BufferedReader reader = new BufferedReader(new StringReader(codeArea.getText()));
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			reader.lines().forEach(s -> {
				try {
					writer.write(s);
					writer.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			PersistanceManager.save(file.getPath());
			reader.close();
			writer.close();
			return true;
		}
		return false;
	}

	public Boolean load(CodeArea codeArea) throws IOException {

		FileChooser fileChooser = initializeFileChooser();
		File file = fileChooser.showOpenDialog(null);

		if (null != codeArea && null != file) {

			StringBuilder content = new StringBuilder();
			BufferedReader r = Files.newBufferedReader(Paths.get(file.getPath()), StandardCharsets.UTF_8);
			r.lines().forEach(s -> content.append(s).append("\n"));
			codeArea.replaceText(content.toString());
			return true;
		}
		return false;
	}

	private FileChooser initializeFileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save file");
		FileChooser.ExtensionFilter extensionFilterHTML = new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");
		FileChooser.ExtensionFilter extensionFilterJava = new FileChooser.ExtensionFilter("JAVA files (*.java)", "*.java");

		fileChooser.getExtensionFilters().add(extensionFilterHTML);
		fileChooser.getExtensionFilters().add(extensionFilterJava);
		return fileChooser;
	}
}
