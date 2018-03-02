package main.java.com.editor.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.stage.FileChooser;
import main.java.com.editor.app.CodeEditorApplication;
import main.java.com.editor.bean.Document;
import main.java.com.editor.bean.DocumentManager;
import main.java.com.editor.utils.Utils;

public class FileOperationsService {

	private static final Logger logger = LoggerFactory.getLogger(FileOperationsService.class);

	DocumentManager documentManager = DocumentManager.getInstance();

	public Boolean save(CodeArea codeArea) {

		FileChooser fileChooser = initializeFileChooser("Save file");
		File file = fileChooser.showSaveDialog(null);

		if (Utils.areNotNull(codeArea, file)) {

			try {
				copyCodeAreaContentToFile(codeArea, file);

				Document document = documentManager.getDocuments().get(documentManager.getCurrentOpenIndex());
				document.setFileName(file.getName());
				document.setFilePath(file.getPath());
				document.setContent(codeArea.getText());
				CodeEditorApplication.loadLabelsFromDocuments(codeArea);
				return true;
			} catch (IOException e) {
				logger.error("IOException occurred while saving code area content to file: ", e);
			}
		}
		return false;
	}

	public Boolean load(CodeArea codeArea) {

		FileChooser fileChooser = initializeFileChooser("Open file");
		File file = fileChooser.showOpenDialog(null);

		if (Utils.areNotNull(codeArea, file)) {

			try {
				copyFileContentToCodeArea(codeArea, file);
				return true;
			} catch (IOException e) {
				logger.error("IOException occurred while loading file: ", e);
			}
		}
		return false;
	}

	public FileChooser initializeFileChooser(String title) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		FileChooser.ExtensionFilter extensionFilterHTML = new FileChooser.ExtensionFilter("HTML files (*.html)",
				"*.html");
		FileChooser.ExtensionFilter extensionFilterJava = new FileChooser.ExtensionFilter("JAVA files (*.java)",
				"*.java");

		fileChooser.getExtensionFilters().add(extensionFilterHTML);
		fileChooser.getExtensionFilters().add(extensionFilterJava);
		return fileChooser;
	}

	public void copyCodeAreaContentToFile(CodeArea codeArea, File file) throws IOException {

		String content = codeArea.getText();
		byte[] strToBytes = content.getBytes();
		Files.write(Paths.get(file.getPath()), strToBytes);
	}

	public void copyFileContentToCodeArea(CodeArea codeArea, File file) throws IOException {

		List<String> contentAsList = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
		String content = String.join("\n", contentAsList);
		codeArea.replaceText(content);
	}

}
