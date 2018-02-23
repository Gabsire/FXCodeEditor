package com.editor.persistance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.editor.bean.Document;
import com.editor.bean.DocumentManager;

public class PersistanceManager {

	public static final String FILE_PATH = "persistance.dat";

	public static void save(String filePath) throws IOException {

		File file = new File(FILE_PATH);
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		writer.write("\n" + filePath);
		writer.close();
	}
	

	public static DocumentManager load() throws IOException {

		boolean firstLine = true;
		BufferedReader reader = Files.newBufferedReader(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
		DocumentManager documentManager = DocumentManager.getInstance();
		String line = reader.readLine();
		
		while (null != line && !"".equals(line)) {

			/*if (firstLine) {
				int index = Integer.parseInt(reader.readLine());
				documentManager.setCurrentOpenIndex(index);
				firstLine = false;
			} else {*/
				
				StringBuilder content = new StringBuilder();
				String fileName = line;
				System.out.println(fileName);
				BufferedReader documentReader = Files.newBufferedReader(Paths.get(fileName), StandardCharsets.UTF_8);
				documentReader.lines().forEach(s -> content.append(s).append("\n"));

				Document document = new Document(fileName, content.toString());
				documentManager.getDocuments().add(document);
			//}
			line = reader.readLine();
		}
		reader.close();
		return documentManager;
	}

}
