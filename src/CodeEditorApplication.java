import java.awt.Insets;
import java.util.function.IntFunction;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
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
import javafx.stage.Stage;

public class CodeEditorApplication extends Application {

	@FXML
	TextArea rowNumbers;

	public static void main(String[] args) {
		Application.launch(CodeEditorApplication.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
			
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editorMainWindow.fxml"));     
		Parent root = (Parent)fxmlLoader.load();   
		
		CodeArea codeArea = new CodeArea();
		MainController controller = fxmlLoader.<MainController>getController();
		controller.setCodeArea(codeArea);
		
		IntFunction<Node> numberFactory = LineNumberFactory.get(codeArea);
        IntFunction<Node> graphicFactory = line -> {
            HBox hbox = new HBox(
                    numberFactory.apply(line));
            hbox.setAlignment(Pos.CENTER_LEFT);
            return hbox;
        };
        codeArea.setParagraphGraphicFactory(graphicFactory);
        codeArea.replaceText("The green arrow will only be on the line where the caret appears.\n\nTry it.");
        codeArea.setStyle("-fx-font-family: consolas; -fx-font-size: 14pt; -fx-padding: 10, 0, 0, 0;");
 
		stage.setTitle("FXCodeEditor");
		Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
		HBox areasBox = (HBox) scene.lookup("#areasBox");
		areasBox.getChildren().add(codeArea);
		
		scene.widthProperty().addListener( 
			    new ChangeListener(){ 

					@Override
					public void changed(ObservableValue observable, Object oldValue, Object newValue) {
						Double width = (Double)newValue;
			            codeArea.setPrefWidth(width);
						
					}
			    });

			scene.heightProperty().addListener(
			    new ChangeListener() {
			    	@Override
			        public void changed(ObservableValue observable, 
			                            Object oldValue, Object newValue) {
			            Double height = (Double)newValue;
			            codeArea.setPrefHeight(height);
			        }
			    });
		
		stage.setScene(scene);
		stage.show();
	}

}
