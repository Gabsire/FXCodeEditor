package test.com.editor.service;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.fxmisc.richtext.CodeArea;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import javafx.stage.Stage;
import main.com.editor.app.CodeEditorApplication;
import main.com.editor.bean.DocumentManager;
import main.com.editor.persistence.PersistenceManager;
import main.com.editor.service.FileOperationsService;

public class FileOperationsServiceTest {

	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	FileOperationsService fileOperationsService = new FileOperationsService();
	CodeArea codeArea;
	
	@Before
	public void setUp() throws Exception{
		codeArea = new CodeArea();
		codeArea.replaceText("Hello");
	}
	
	@After
	public void tearDown(){
		codeArea = null;
	}
	
	@Test
	public void testSave(){
		DocumentManager documentManager = fileOperationsService.save(codeArea);
		assertThatCode(() -> { fileOperationsService.save(codeArea); }).doesNotThrowAnyException();
		assertThat(documentManager.getInstance().getDocuments()).isNotEmpty();
	}
	
	@Test
	public void testLoad(){
		codeArea = fileOperationsService.load(codeArea);
		assertThatCode(() -> { fileOperationsService.load(codeArea); }).doesNotThrowAnyException();
		assertThat(codeArea.getText()).isNotNull();
		assertThat(codeArea.getText()).isNotEmpty();
	}

}

