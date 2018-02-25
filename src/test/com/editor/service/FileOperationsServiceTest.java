package test.com.editor.service;

import static org.assertj.core.api.Assertions.assertThatCode;

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
		assertThatCode(() -> { fileOperationsService.save(codeArea); }).doesNotThrowAnyException();
	}

}

