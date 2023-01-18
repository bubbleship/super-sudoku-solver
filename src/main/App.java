package main;

import consts.Strings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class App extends Application {


    public static final int WIDTH = 888;
    public static final int HEIGHT = 592;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("solver.fxml"));
        Scene scene = new Scene(loader.load(), WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.setTitle(Strings.APP_NAME);
        stage.show();
	}
}
