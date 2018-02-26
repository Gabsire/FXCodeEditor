package test.com.editor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.File;

import org.fxmisc.richtext.CodeArea;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javafx.stage.FileChooser;
import main.java.com.editor.service.FileOperationsService;

public class FileOperationsServiceTest {

	@Rule
	public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	FileOperationsService fileOperationsService = new FileOperationsService();
	CodeArea codeArea;
	File file;

	@Before
	public void setUp() throws Exception {
		codeArea = new CodeArea();
		file = new File("./data/Kitty.txt");
		codeArea.replaceText("Hello");
	}

	@After
	public void tearDown() {
		codeArea = null;
		file = null;
	}

	@Test
	public void testSave() {
		assertThat(fileOperationsService.save(codeArea)).isTrue();
	}

	@Test
	public void testLoad() {
		assertThat(fileOperationsService.load(codeArea)).isTrue();
		assertThat(codeArea.getText()).isNotNull();
		assertThat(codeArea.getText()).isNotEmpty();
	}

	@Test
	public void testInitializeFileChooser() {
		FileChooser fileChooser = fileOperationsService.initializeFileChooser("Save file");
		assertThat(fileChooser).isNotNull();
		assertThat(fileChooser.getExtensionFilters()).isNotEmpty();
		assertThat(fileChooser.getTitle()).isEqualTo("Save file");
	}

	@Test
	public void testCopyCodeAreaContentToFile() {
		assertThatCode(() -> {
			fileOperationsService.copyCodeAreaContentToFile(codeArea, file);
		}).doesNotThrowAnyException();
	}

	@Test
	public void testCopyFileContentToCodeArea() {
		assertThatCode(() -> {
			fileOperationsService.copyFileContentToCodeArea(codeArea, file);
		}).doesNotThrowAnyException();
	}

}
