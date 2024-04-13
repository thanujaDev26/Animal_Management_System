import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

// Main class for initializing the JavaFX application
public class AppInitializer extends Application {

    // The main method to launch the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }

    // Method called when the application starts
    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file (UI layout) for the loading form
        Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/loadingForm.fxml")));

        // Set the background color of the loaded UI to be transparent
        load.setStyle("-fx-background-color: transparent;");

        // Create a scene with a transparent background
        Scene scene = new Scene(load, 820, 683, Color.TRANSPARENT);

        // Set the scene for the stage
        stage.setScene(scene);

        // Set the stage style to be transparent (no decorations)
        stage.initStyle(StageStyle.TRANSPARENT);

        // Set the application icon
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/icon.png")));
        stage.getIcons().add(image);

        // Set the title of the stage
        stage.setTitle("Wildlife Management System - Loading Page");

        // Center the stage on the screen
        stage.centerOnScreen();

        // Show the stage
        stage.show();
    }
}
