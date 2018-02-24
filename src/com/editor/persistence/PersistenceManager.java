package com.editor.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.editor.bean.Document;
import com.editor.bean.DocumentManager;
import com.editor.utils.Constants;

public class PersistenceManager {

	private static DocumentManager documentManager = DocumentManager.getInstance();

	public static void save() throws IOException {

		File file = getPersistenceFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
		List<Document> documents = documentManager.getDocuments();
		writer.write(documentManager.getCurrentOpenIndex() + Constants.NEWLINE);

		for (Document document : documents) {
			writer.write(document.getFilePath() + Constants.NEWLINE);
		}
		writer.close();
	}

	public static DocumentManager load() throws IOException {

		boolean firstLine = true;
		BufferedReader reader = Files.newBufferedReader(getPersistenceFile().toPath(), StandardCharsets.UTF_8);
		String line = reader.readLine().trim();

		while (null != line && (!line.isEmpty())) {

			if (firstLine) {
				// line contains index
				int index = Integer.parseInt(line);
				documentManager.setCurrentOpenIndex(index);
				firstLine = false;
			} else {
				// line contains file path
				StringBuilder content = new StringBuilder();
				BufferedReader documentReader = Files.newBufferedReader(Paths.get(line), StandardCharsets.UTF_8);
				documentReader.lines().forEach(s -> content.append(s).append(Constants.NEWLINE));

				String fileName = getFileNameFromPath(line);
				Document document = new Document(fileName, line, content.toString());
				documentManager.getDocuments().add(document);
			}
			line = reader.readLine();
		}
		reader.close();
		return documentManager;
	}

	public static String getFileNameFromPath(String filePath) {

		String fileName;
		if (filePath.contains(Constants.PATH_SEPARATOR)) {
			fileName = filePath.substring(filePath.lastIndexOf(Constants.PATH_SEPARATOR)+1, filePath.length());
		} else {
			fileName = filePath;
		}
		return fileName;
	}

	public static File getPersistenceFile() throws IOException {

		File file = new File(Constants.PERSISTENCE_FILE_PATH);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}
}
