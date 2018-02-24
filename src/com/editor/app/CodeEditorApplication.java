package com.editor.app;

import java.util.Collection;
import java.util.Collections;
import java.util.function.IntFunction;
import java.util.regex.Matcher;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import org.fxmisc.richtext.TwoDimensional.Position;

import com.editor.bean.DocumentManager;
import com.editor.controller.MainController;
import com.editor.persistence.PersistenceManager;
import com.editor.utils.Constants;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CodeEditorApplication extends Application {

	@FXML
	TextArea rowNumbers;

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
		DocumentManager documentManager = PersistenceManager.load();
		if(-1 != documentManager.getCurrentOpenIndex() && !documentManager.getDocuments().isEmpty()){
			codeArea.replaceText(documentManager.getDocuments().get(documentManager.getCurrentOpenIndex()).getContent());
		}

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
		
		controller.setCodeArea(codeArea);
		initLineNumbers(codeArea);
		bindCodeAreaDimensionToScene(codeArea, scene);

		// initialize container boxes
		VBox mainContainerBox = (VBox) scene.lookup("#mainContainer");

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

}
