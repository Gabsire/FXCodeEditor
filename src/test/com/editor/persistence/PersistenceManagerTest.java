package test.com.editor.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import main.java.com.editor.bean.DocumentManager;
import main.java.com.editor.persistence.PersistenceManager;
import main.java.com.editor.utils.Constants;

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
	
	@Test
	public void testGetPersistenceFile(){
		File file = PersistenceManager.getPersistenceFile();
		assertThat(file).isNotNull();
		assertThat(file.getPath()).isEqualTo(Constants.PERSISTENCE_FILE_PATH);
		assertThat(file.exists()).isTrue();
	}
	
	
}
