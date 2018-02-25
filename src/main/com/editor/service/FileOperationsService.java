package main.com.editor.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.fxmisc.richtext.CodeArea;

import javafx.stage.FileChooser;
import main.com.editor.bean.Document;
import main.com.editor.bean.DocumentManager;
import main.com.editor.utils.Utils;

public class FileOperationsService {

	DocumentManager documentManager = DocumentManager.getInstance();

	public void save(CodeArea codeArea) {

		FileChooser fileChooser = initializeFileChooser();
		File file = fileChooser.showSaveDialog(null);

		if (Utils.areNotNull(codeArea, file)) {

			try {
				copyCodeAreaContentToFile(codeArea, file);
				appendNewDocumentToManager(file);			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Boolean load(CodeArea codeArea) {

		FileChooser fileChooser = initializeFileChooser();
		File file = fileChooser.showOpenDialog(null);

		if (Utils.areNotNull(codeArea, file)) {

			try {
				copyFileContentToCodeArea(codeArea, file);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public FileChooser initializeFileChooser() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save file");
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

	public void appendNewDocumentToManager(File file) {
		Document document = new Document(file.getName(), file.getPath(), null);
		documentManager.getDocuments().add(document);
		documentManager.setCurrentOpenIndex(documentManager.getDocuments().indexOf(document));
	}

}
