package main.com.editor.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;
import java.util.regex.Matcher;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import org.fxmisc.richtext.TwoDimensional.Position;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.com.editor.bean.Document;
import main.com.editor.bean.DocumentManager;
import main.com.editor.controller.MainController;
import main.com.editor.persistence.PersistenceManager;
import main.com.editor.utils.Constants;

public class CodeEditorApplication extends Application {

	int maxLineNumber = 0;

	public static void main(String[] args) {
		Application.launch(CodeEditorApplication.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		// initialize stage and scene
		stage.setTitle(Constants.STAGE_DEFAULT_TITLE);

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditorMainWindow.fxml"));
		Parent root = (Parent) fxmlLoader.load();
		MainController controller = fxmlLoader.<MainController>getController();

		Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());

		// initialize and attach code area to main controller
		CodeArea codeArea = new CodeArea();
		codeArea.setId("codeArea");
		codeArea.setStyle(Constants.CODE_AREA_DEFAULT_STYLE);

		// file name tabs for each document above code area
		HBox fileNamesBox = (HBox) scene.lookup("#fileNamesBox");
		TreeView treeview = (TreeView) scene.lookup("#projectExplorer");
		VBox.setMargin(fileNamesBox, new Insets(5, 1, 1, treeview.getPrefWidth() / 2));
		fileNamesBox.setSpacing(2);
		DocumentManager documentManager = PersistenceManager.loadDocuments();
		List<Document> documents = documentManager.getDocuments();
		List<Label> fileNames = new ArrayList<>();

		if (!documents.isEmpty()) {

			for (int i = 0; i < documents.size() + 1; i++) {

				Label fileName = new Label();

				if (documents.size() == i) {
					fileName.setId("newtab");
					fileName.setText("New Tab");

				} else {
					fileName.setId("filename" + i);
					fileName.setText(documents.get(i).getFileName());
				}

				fileName.getStyleClass().add("filenames");
				fileNames.add(fileName);

				if (documentManager.getCurrentOpenIndex() == i) {
					fileName.getStyleClass().add("selectedFilename");
				}

				fileNamesBox.getChildren().add(fileName);
				handleFileNameMouseClick(codeArea, documentManager, fileNames, fileName);
			}
			codeArea.replaceText(
					documentManager.getDocuments().get(documentManager.getCurrentOpenIndex()).getContent());
		}

		handleCodeAreaChange(scene, codeArea);

		controller.setCodeArea(codeArea);
		initLineNumbers(codeArea);
		bindCodeAreaDimensionToScene(codeArea, scene);

		// initialize container boxes
		HBox areasBox = (HBox) scene.lookup("#areasBox");
		areasBox.getChildren().add(codeArea);

		applyColorSyntaxToArea(codeArea);
		scene.getStylesheets().add(getClass().getResource("../style/ColorSyntax.css").toExternalForm());

		stage.setScene(scene);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				controller.close();
			}
		});
		stage.show();
	}

	private void handleCodeAreaChange(Scene scene, CodeArea codeArea) {
		codeArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				Position position = getPositionFromCaret(codeArea);
				int line = position.getMajor();
				int character = position.getMinor();
				++line;

				Label currentLineLabel = (Label) scene.lookup("#currentLineLabel");
				Label lineTotalLabel = (Label) scene.lookup("#lineTotalLabel");
				Label currentCharacterLabel = (Label) scene.lookup("#currentCharacterLabel");

				currentLineLabel.setText("Current line: " + line);
				currentCharacterLabel.setText("Current character: " + character);

				if (maxLineNumber < line) {
					lineTotalLabel.setText("Total lines: " + line);
					maxLineNumber = line;
				}
			}

			private Position getPositionFromCaret(CodeArea codeArea) {
				int offset = codeArea.getCaretPosition();
				Position position = codeArea.offsetToPosition(offset, null);
				return position;
			}
		});
	}

	public void applyColorSyntaxToArea(CodeArea codeArea) {
		codeArea.richChanges().filter(character -> !character.getInserted().equals(character.getRemoved())) // XXX
				.subscribe(change -> {
					codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
				});
	}

	private void bindCodeAreaDimensionToScene(CodeArea codeArea, Scene scene) {
		bindCodeAreaWidthToScene(codeArea, scene);
		bindCoreAreaHeightToScene(codeArea, scene);
	}

	private void initLineNumbers(CodeArea codeArea) {

		IntFunction<Node> numberFactory = LineNumberFactory.get(codeArea);
		IntFunction<Node> graphicFactory = line -> {
			HBox hbox = new HBox(numberFactory.apply(line));
			hbox.setAlignment(Pos.CENTER_LEFT);
			hbox.setPrefWidth(30);
			return hbox;
		};
		codeArea.setParagraphGraphicFactory(graphicFactory);
	}

	private void bindCoreAreaHeightToScene(CodeArea codeArea, Scene scene) {

		scene.heightProperty().addListener((ChangeListener<? super Number>) (observable, oldValue, newValue) -> {
			Double height = (Double) newValue;
			codeArea.setPrefHeight(height);
		});
	}

	private void bindCodeAreaWidthToScene(CodeArea codeArea, Scene scene) {

		scene.widthProperty().addListener((ChangeListener<? super Number>) (observable, oldValue, newValue) -> {
			Double width = (Double) newValue;
			codeArea.setPrefWidth(width);

		});
	}

	public static StyleSpans<Collection<String>> computeHighlighting(String text) {

		Matcher matcher = Constants.PATTERN.matcher(text);
		int fontDelimiter = 0;
		StyleSpansBuilder<Collection<String>> styleSpansBuilder = new StyleSpansBuilder<>();

		while (matcher.find()) {
			String styleClass = matcher.group("KEYWORD") != null ? "keyword"
					: matcher.group("PAREN") != null ? "paren"
							: matcher.group("BRACE") != null ? "brace"
									: matcher.group("BRACKET") != null ? "bracket"
											: matcher.group("SEMICOLON") != null ? "semicolon"
													: matcher.group("STRING") != null ? "string"
															: matcher.group("COMMENT") != null ? "comment" : null;
			styleSpansBuilder.add(Collections.emptyList(), matcher.start() - fontDelimiter);
			styleSpansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
			fontDelimiter = matcher.end();
		}
		styleSpansBuilder.add(Collections.emptyList(), text.length() - fontDelimiter);
		return styleSpansBuilder.create();
	}

	private void handleFileNameMouseClick(CodeArea codeArea, DocumentManager documentManager, List<Label> fileNames,
			Label fileName) {
		fileName.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				if (!fileName.getStyleClass().contains("selectedFilename")) {
					fileNames.forEach(filename -> filename.getStyleClass().remove("selectedFilename"));
					selectFileNameLabel(fileName);
				}
			}

			private void selectFileNameLabel(Label fileName) {

				int fileNameIndex = getFileNameIndex(fileName);
				displayDocumentContent(fileNameIndex);
				fileName.getStyleClass().add("selectedFilename");
			}

			private void displayDocumentContent(int fileNameIndex) {
				Document document = documentManager.getDocuments().get(fileNameIndex);
				codeArea.replaceText(document.getContent());
			}

			private int getFileNameIndex(Label fileName) {
				int fileNameIndex = Integer.parseInt(fileName.getId().substring(fileName.getId().length() - 1));
				return fileNameIndex;
			}
		});
	}

}
