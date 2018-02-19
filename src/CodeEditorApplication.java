import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CodeEditorApplication extends Application {

	@FXML
	TextArea rowNumbers;

	public static void main(String[] args) {
		Application.launch(CodeEditorApplication.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("editorMainWindow.fxml"));

		stage.setTitle("FXCodeEditor");
		Scene scene = new Scene(root, 1000, 800);
		stage.setScene(scene);
		stage.show();
	}

}
