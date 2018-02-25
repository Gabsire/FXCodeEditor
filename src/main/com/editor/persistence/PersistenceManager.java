package main.com.editor.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import main.com.editor.bean.Document;
import main.com.editor.bean.DocumentManager;
import main.com.editor.utils.Constants;
import main.com.editor.utils.Utils;

public class PersistenceManager {

	private static DocumentManager documentManager = DocumentManager.getInstance();

	public static void saveDocuments() {

		File file = getPersistenceFile();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {

			writer.write(documentManager.getCurrentOpenIndex() + Constants.NEWLINE);
			writeDocumentPathsToPersistenceFile(writer);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DocumentManager loadDocuments() {

		boolean firstLine = true;
		File persistenceFile = getPersistenceFile();

		try (BufferedReader reader = Files.newBufferedReader(persistenceFile.toPath(), StandardCharsets.UTF_8)) {

			String line = reader.readLine();

			while (Utils.areNotNull(line) && (!line.isEmpty())) {

				line = line.trim();
				
				if (firstLine) {
					// line contains index
					int index = Integer.parseInt(line);
					documentManager.setCurrentOpenIndex(index);
					firstLine = false;
				} else {
					// line contains file path
					Path path = Paths.get(line);
					File file = path.toFile();
					List<String> contentAsList = Files.readAllLines(path, StandardCharsets.UTF_8);
					Document document = new Document(file.getName(), file.getAbsolutePath(),
							String.join(Constants.NEWLINE, contentAsList));
					
					if(!documentManager.getDocuments().contains(document)){
						documentManager.getDocuments().add(document);
					}
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return documentManager;
	}

	public static File getPersistenceFile() {

		File file = new File(Constants.PERSISTENCE_FILE_PATH);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	private static void writeDocumentPathsToPersistenceFile(BufferedWriter writer) {

		List<Document> documents = documentManager.getDocuments();

		documents.forEach(document -> {
			try {
				writer.write(document.getFilePath() + Constants.NEWLINE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
