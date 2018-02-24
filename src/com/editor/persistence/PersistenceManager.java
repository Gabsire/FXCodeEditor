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

public class PersistenceManager {

	public static final String FILE_PATH = "data/persistence.dat";
	private static DocumentManager documentManager = DocumentManager.getInstance();

	public static void save() throws IOException {
		System.out.println("chocolat");
		File file = getPersistenceFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
		List<Document> documents = documentManager.getDocuments();
		System.out.println(documentManager.getCurrentOpenIndex() + "polo");
		writer.write(documentManager.getCurrentOpenIndex() + "\n");

		for (Document document : documents) {
			writer.write(document.getFilePath() + "\n");
		}
		writer.close();
	}

	public static DocumentManager load() throws IOException {

		boolean firstLine = true;
		BufferedReader reader = Files.newBufferedReader(getPersistenceFile().toPath(), StandardCharsets.UTF_8);
		String line = reader.readLine();

		while (null != line && !"".equals(line.trim())) {

			if (firstLine) {
				int index = Integer.parseInt(line);
				documentManager.setCurrentOpenIndex(index);
				firstLine = false;
			} else {
				StringBuilder content = new StringBuilder();
				String filePath = line;
				BufferedReader documentReader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);
				documentReader.lines().forEach(s -> content.append(s).append("\n"));

				String fileName = filePath.trim().substring(filePath.lastIndexOf("\\"), filePath.length());

				Document document = new Document(fileName, filePath, content.toString());
				documentManager.getDocuments().add(document);

			}
			line = reader.readLine();
		}
		reader.close();
		return documentManager;
	}

	private static File getPersistenceFile() throws IOException {

		File file = new File(FILE_PATH);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

}
