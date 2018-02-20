import java.awt.Insets;
import java.util.Collection;
import java.util.Collections;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.ChangeEvent;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import org.fxmisc.richtext.StyledTextArea;

import com.editor.controller.MainController;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CodeEditorApplication extends Application {

	@FXML
	TextArea rowNumbers;

	public static void main(String[] args) {
		Application.launch(CodeEditorApplication.class, args);
	}

	private static final String[] KEYWORDS = new String[] { "abstract", "assert", "boolean", "break", "byte", "case",
			"catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends",
			"final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface",
			"long", "native", "new", "package", "private", "protected", "public", "return", "short", "static",
			"strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
			"volatile", "while" };

	private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
	private static final String PAREN_PATTERN = "\\(|\\)";
	private static final String BRACE_PATTERN = "\\{|\\}";
	private static final String BRACKET_PATTERN = "\\[|\\]";
	private static final String SEMICOLON_PATTERN = "\\;";
	private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
	private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

	private static final Pattern PATTERN = Pattern.compile(
			"(?<KEYWORD>" + KEYWORD_PATTERN + ")" + "|(?<PAREN>" + PAREN_PATTERN + ")" + "|(?<BRACE>" + BRACE_PATTERN
					+ ")" + "|(?<BRACKET>" + BRACKET_PATTERN + ")" + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
					+ "|(?<STRING>" + STRING_PATTERN + ")" + "|(?<COMMENT>" + COMMENT_PATTERN + ")");

	@Override
	public void start(Stage stage) throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editorMainWindow.fxml"));
		Parent root = (Parent) fxmlLoader.load();

		CodeArea codeArea = new CodeArea();
		MainController controller = fxmlLoader.<MainController>getController();
		controller.setCodeArea(codeArea);

		IntFunction<Node> numberFactory = LineNumberFactory.get(codeArea);
		IntFunction<Node> graphicFactory = line -> {
			HBox hbox = new HBox(numberFactory.apply(line));
			hbox.setAlignment(Pos.CENTER_LEFT);
			hbox.setPrefWidth(30);
			return hbox;
		};
		codeArea.setParagraphGraphicFactory(graphicFactory);
		codeArea.replaceText("The green arrow will only be on the line where the caret appears.\n\nTry it.");
		codeArea.setStyle("-fx-font-family: consolas; -fx-font-size: 14pt; -fx-padding: 10, 0, 0, 0;");

		stage.setTitle("FXCodeEditor");
		Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());

		VBox mainContainerBox = (VBox) scene.lookup("#mainContainer");
		mainContainerBox.setSpacing(30);

		HBox areasBox = (HBox) scene.lookup("#areasBox");
		areasBox.getChildren().add(codeArea);

		codeArea.richChanges().filter(ch -> !ch.getInserted().equals(ch.getRemoved())) // XXX
				.subscribe(change -> {
					codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
				});

		scene.widthProperty().addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				Double width = (Double) newValue;
				codeArea.setPrefWidth(width);

			}
		});

		scene.heightProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				Double height = (Double) newValue;
				codeArea.setPrefHeight(height);
			}
		});
        scene.getStylesheets().add(getClass().getResource("colorSyntax.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	private static StyleSpans<Collection<String>> computeHighlighting(String text) {
		Matcher matcher = PATTERN.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		while (matcher.find()) {
			String styleClass = matcher.group("KEYWORD") != null ? "keyword"
					: matcher.group("PAREN") != null ? "paren"
							: matcher.group("BRACE") != null ? "brace"
									: matcher.group("BRACKET") != null ? "bracket"
											: matcher.group("SEMICOLON") != null ? "semicolon"
													: matcher.group("STRING") != null ? "string"
															: matcher.group("COMMENT") != null ? "comment" : null;
			/* never happens */ assert styleClass != null;
			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
			lastKwEnd = matcher.end();
		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}

}
