package main.java.com.editor.app;

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
import main.java.com.editor.bean.Document;
import main.java.com.editor.bean.DocumentManager;
import main.java.com.editor.controller.MainController;
import main.java.com.editor.persistence.PersistenceManager;
import main.java.com.editor.utils.Constants;

public class CodeEditorApplication extends Application {

	private static FXMLLoader fxmlLoader;
	private static Parent root;
	private static MainController controller;

	private static Scene scene;
	private static HBox fileNamesBox, areasBox;
	private static DocumentManager documentManager = PersistenceManager.loadDocuments();
	private static CodeArea codeArea = new CodeArea();

	private int maxLineNumber = 0;

	public static void main(String[] args) {
		Application.launch(CodeEditorApplication.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		// initialize stage and scene
		stage.setTitle(Constants.STAGE_DEFAULT_TITLE);

		fxmlLoader = new FXMLLoader(getClass().getResource("EditorMainWindow.fxml"));
		root = (Parent) fxmlLoader.load();
		controller = fxmlLoader.<MainController>getController();

		scene = new Scene(root, stage.getWidth(), stage.getHeight());

		// initialize and attach code area to main controller
		codeArea.setId("codeArea");
		codeArea.setStyle(Constants.CODE_AREA_DEFAULT_STYLE);

		// file name tabs for each document above code area
		fileNamesBox = (HBox) scene.lookup("#fileNamesBox");
		TreeView treeview = (TreeView) scene.lookup("#projectExplorer");
		VBox.setMargin(fileNamesBox, new Insets(5, 1, 1, treeview.getPrefWidth() / 2));
		fileNamesBox.setSpacing(2);

		initializeComponentsFromDocuments(codeArea);

		handleCodeAreaChange(scene, codeArea);
		controller.setCodeArea(codeArea);
		initLineNumbers(codeArea);
		bindCodeAreaDimensionToScene(codeArea, scene);

		// initialize container boxes
		areasBox = (HBox) scene.lookup("#areasBox");
		areasBox.getChildren().add(codeArea);

		applySyntaxColoringToArea(codeArea);
		scene.getStylesheets().add(getClass().getResource("../style/ColorSyntax.css").toExternalForm());

		stage.setScene(scene);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				controller.close();
			}
		});
		stage.show();
	}

	public static void initializeComponentsFromDocuments(CodeArea codeArea) {

		// get documents for tabs and texts
		List<Document> documents = documentManager.getDocuments();

		// put new tab to get to quick empty document
		Document newTabDocument = new Document(Constants.NEW_TAB_LABEL, Constants.EMPTY_TEXT, Constants.EMPTY_TEXT);
		documents.add(newTabDocument);
		List<Label> fileNames = new ArrayList<>();

		// remove all the children from a previous initialization if applies
		fileNamesBox.getChildren().removeAll(fileNamesBox.getChildren());

		if (!documents.isEmpty()) {

			for (int i = 0; i < documents.size(); i++) {

				Label fileName = new Label();
				fileName.setId("filename" + i);
				fileName.setText(documents.get(i).getFileName());
				fileName.getStyleClass().add(Constants.TAB_STYLECLASS);
				fileNames.add(fileName);

				if (documentManager.getCurrentOpenIndex() == i) {
					fileName.getStyleClass().add(Constants.SELECTED_TAB_STYLECLASS);
				}

				fileNamesBox.getChildren().add(fileName);
				handleFileNameMouseClick(codeArea, documentManager, fileNames, fileName);
			}
			codeArea.replaceText(
					documentManager.getDocuments().get(documentManager.getCurrentOpenIndex()).getContent());
		}
	}

	private void handleCodeAreaChange(Scene scene, CodeArea codeArea) {

		codeArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				Position position = getPositionFromCaret(codeArea);
				int line = position.getMajor();
				int character = position.getMinor();
				++line;

				Label currentLineLabel = (Label) scene.lookup("#currentLineLabel"),
						lineTotalLabel = (Label) scene.lookup("#lineTotalLabel"),
						currentCharacterLabel = (Label) scene.lookup("#currentCharacterLabel");

				currentLineLabel.setText(Constants.INFO_CURRENT_LINE + line);
				currentCharacterLabel.setText(Constants.INFO_CURRENT_CHARACTER + character);

				if (maxLineNumber < line) {
					lineTotalLabel.setText(Constants.INFO_TOTAL_LINES + line);
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

	private void applySyntaxColoringToArea(CodeArea codeArea) {

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

	private static void handleFileNameMouseClick(CodeArea codeArea, DocumentManager documentManager,
			List<Label> fileNames, Label fileName) {

		fileName.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				if (!fileName.getStyleClass().contains(Constants.SELECTED_TAB_STYLECLASS)) {
					fileNames.forEach(filename -> filename.getStyleClass().remove(Constants.SELECTED_TAB_STYLECLASS));
					selectFileNameLabel(fileName);
				}
			}

			private void selectFileNameLabel(Label fileName) {

				int fileNameIndex = getFileNameIndex(fileName);
				displayDocumentContent(fileNameIndex);
				fileName.getStyleClass().add(Constants.SELECTED_TAB_STYLECLASS);
			}

			private void displayDocumentContent(int fileNameIndex) {
				Document document = documentManager.getDocuments().get(fileNameIndex);
				codeArea.replaceText(document.getContent());
			}

			private int getFileNameIndex(Label fileName) {
				return Integer.parseInt(fileName.getId().substring(fileName.getId().length() - 1));
			}
		});
	}

}
