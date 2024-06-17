package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

// Controller class for the LoadingForm.fxml
public class LoadingFormController implements Initializable {

    // Reference to the ProgressBar defined in the FXML file
    @FXML
    private ProgressBar progressBar;

    // Method called during the initialization of the controller
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Create a background Task to simulate loading progress
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i <= 100; i++) {
                    // Update the progress property (value, maximum) for the ProgressBar
                    updateProgress(i, 100);
                    // Simulate a delay
                    Thread.sleep(55);
                }
                return null;
            }
        };

        // Bind the ProgressBar's progressProperty to the task's progressProperty
        progressBar.progressProperty().bind(task.progressProperty());

        // Set an event handler for when the task is succeeded (loading is complete)
        task.setOnSucceeded(event -> {
            try {
                // Load the dashboard FXML file
                Parent loginParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/loginForm.fxml")));
                // Create a new scene for the dashboard
                Scene loginScene = new Scene(loginParent);
                // Create a new stage for the dashboard
                Stage loginStage = new Stage();
                loginStage.setResizable(false);
                // Set the application icon for the dashboard stage
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/icon.png")));
                loginStage.getIcons().add(image);
                // Set the title for the dashboard stage
                loginStage.setTitle("Wildlife Management System - Login Page");
                // Set the scene for the dashboard stage
                loginStage.setScene(loginScene);
                // Show the dashboard stage

                loginStage.show();

                // Close the current stage (loading form)
                ((Stage) progressBar.getScene().getWindow()).close();

            } catch (IOException e) {
                // Throw a runtime exception if an error occurs during loading the dashboard FXML
                throw new RuntimeException(e);
            }
        });

        // Start the background task in a new thread
        new Thread(task).start();
    }
}
