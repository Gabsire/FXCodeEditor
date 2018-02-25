package test.com.editor.persistence;

import org.junit.After;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import main.com.editor.bean.DocumentManager;
import main.com.editor.persistence.PersistenceManager;

public class PersistenceManagerTest {

	File file;
	List<String> linesOfPersistenceFile;
	
	@Before
	public void setUp() throws IOException{
		file = PersistenceManager.getPersistenceFile();
		linesOfPersistenceFile = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
	}
	
	@After
	public void tearDown(){
		file = null;
		linesOfPersistenceFile = null;
	}
	
	@Test
	@Ignore
	public void testSaveDocuments(){
		assertThatCode(() -> { PersistenceManager.saveDocuments(); }).doesNotThrowAnyException();
	}
	
	@Test
	public void testLoadDocuments(){
		DocumentManager documentManager = PersistenceManager.loadDocuments();
		assertThat(documentManager.getDocuments().size()).isEqualTo(linesOfPersistenceFile.size()-1);
		assertThat(documentManager.getDocuments()).isNotEmpty();
		assertThat(documentManager.getDocuments().get(0).getFilePath()).isEqualTo(linesOfPersistenceFile.get(1));
	}
	
	
}
