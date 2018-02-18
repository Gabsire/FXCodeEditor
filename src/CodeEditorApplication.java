import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CodeEditorApplication extends Application {

	public static void main(String[] args) {
		Application.launch(CodeEditorApplication.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("editorMainWindow.fxml"));

		stage.setTitle("FXCodeEditor");
		stage.setScene(new Scene(root, 1000, 800));
		stage.show();

	}

}
