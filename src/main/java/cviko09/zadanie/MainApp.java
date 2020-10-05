package cviko09.zadanie;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;


public class MainApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
        BorderPane rootPane = new BorderPane();
        cviko09.zadanie.RedGreenPane redGreenPane = new cviko09.zadanie.RedGreenPane();
        TextArea textArea = new TextArea();
        rootPane.setCenter(textArea);
        rootPane.setBottom(redGreenPane);
        rootPane.setPrefSize(640, 480);
        Scene scene = new Scene(rootPane);

        primaryStage.setScene(scene);
		primaryStage.setTitle("KOPR editor");
		primaryStage.show();
		
		textArea.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				cviko09.zadanie.SpellChecker spellChecker = new cviko09.zadanie.SpellChecker();
				final List<cviko09.zadanie.SpellChecker.SpellcheckBoundary> kontrola = spellChecker.check(newValue);
				boolean isOK = kontrola.isEmpty();
				System.out.println(newValue + " kontrola ok: " + isOK);		
				redGreenPane.setGreenState(isOK);
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
}
