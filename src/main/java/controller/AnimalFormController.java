package controller;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import entity.Animal;
import entity.Location;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.AnimalModel;
import model.AnimalModelImpl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AnimalFormController implements Initializable {

    @FXML
    private JFXTextField txtSpecies;

    @FXML
    private JFXTextField txtCommonName;

    @FXML
    private JFXTextField txtScientificName;

    @FXML
    private JFXTextField txtGender;

    @FXML
    private JFXTextField txtLifeTime;

    @FXML
    private JFXTextField txtWeight;

    @FXML
    private JFXTextField txtRegion;

    @FXML
    private JFXTextField txtConservationStatus;

    @FXML
    private JFXTextField txtReproduction;

    @FXML
    private JFXTextField txtColor;

    @FXML
    private JFXTextField txtMarkings;

    @FXML
    private JFXTextField txtHabitat;

    @FXML
    private JFXTextField txtBehavior;

    @FXML
    private JFXTextField txtDietaryPreferences;

    @FXML
    private JFXComboBox<String> cmbLocation;

    @FXML
    private JFXTextArea txtAdditionalDetails;

    @FXML
    private ImageView image1;

    @FXML
    private ImageView image2;

    @FXML
    private ImageView image3;

    @FXML
    private JFXButton imageUploadBtn1;

    @FXML
    private JFXButton imageUploadBtn2;

    @FXML
    private JFXButton imageUploadBtn3;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private Label lblCommonName;

    @FXML
    private Label lblSpecies;

    @FXML
    private Label lblScientificName;

    @FXML
    private Label lblGender;

    @FXML
    private Label lblReproduction;

    @FXML
    private Label lblConservationStatus;

    @FXML
    private Label lblRegion;

    @FXML
    private Label lblAverageWeight;

    @FXML
    private Label lblAverageLifeTime;

    @FXML
    private Label lblColor;

    @FXML
    private Label lblMarkings;

    @FXML
    private Label lblBehavior;

    @FXML
    private Label lblHabitat;

    @FXML
    private Label lblDietaryPreferences;

    @FXML
    private Label lblAddMoreDetails;



    @FXML
    private Button saveBtn;

    @FXML
    private WebView mapView;

    private WebEngine engine;

    private List<byte[]> images = new ArrayList<>();

    List<Location> locations = new ArrayList<>();

    private static final Pattern intPattern = Pattern.compile("^[1-9][0-9]?$|^100$");

    private static final Pattern doublePattern = Pattern.compile("^[0-9]+\\.?[0-9]*$");

    private DashboardFormController dashboardFormController;

    AnimalModel animalModel = new AnimalModelImpl();

    private Animal searchedAnimal;

    @FXML
    void ImageUpload1OnAction(ActionEvent event) throws IOException {
        handleImageUpload(image1, imageUploadBtn1, 0);
    }

    @FXML
    void ImageUpload2OnAction(ActionEvent event) throws IOException {
        handleImageUpload(image2, imageUploadBtn2, 1);
    }

    @FXML
    void ImageUpload3OnAction(ActionEvent event) throws IOException {
        handleImageUpload(image3, imageUploadBtn3, 2);
    }

    @FXML
    void saveBtnOnAction(ActionEvent event) {
        if (validateFields(txtSpecies, txtCommonName, txtScientificName, txtGender,
                txtLifeTime, txtWeight, txtRegion, txtConservationStatus, txtReproduction, txtColor,
                txtMarkings, txtHabitat, txtBehavior, txtDietaryPreferences, txtAdditionalDetails)) {
            if (intPattern.matcher(txtLifeTime.getText()).matches()) {
                if (doublePattern.matcher(txtWeight.getText()).matches()) {
                    if (images.size() == 3) {
                        if (!locations.isEmpty()) {

                            Animal animal = new Animal();
                            animal.setSpecies(txtSpecies.getText());
                            animal.setCommon_name(txtCommonName.getText());
                            animal.setScientific_name(txtScientificName.getText());
                            animal.setGender(txtGender.getText());
                            animal.setAverage_life_time(Integer.parseInt(txtLifeTime.getText()));
                            animal.setAverage_weight(Double.parseDouble(txtWeight.getText()));
                            animal.setRegion(txtRegion.getText());
                            animal.setConservation_status(txtConservationStatus.getText());
                            animal.setReproduction(txtReproduction.getText());
                            animal.setColor(txtColor.getText());
                            animal.setMarkings(txtMarkings.getText());
                            animal.setHabitat(txtHabitat.getText());
                            animal.setBehavior(txtBehavior.getText());
                            animal.setDietary_preferences(txtDietaryPreferences.getText());
                            animal.setAdditional_details(txtAdditionalDetails.getText());
                            animal.setImages(images);
                            animal.setLocations(locations);

                            if (searchedAnimal == null) {
                                if (animalModel.saveAnimal(animal)) {

                                    new Alert(Alert.AlertType.CONFIRMATION, "Animal saved successfully!").show();
                                    Stage stage = (Stage) txtCommonName.getScene().getWindow();
                                    stage.close();

                                } else {
                                    new Alert(Alert.AlertType.WARNING, "Animal saved unsuccessfully!").show();
                                }
                            }else {
                                if (animalModel.updateAnimal(animal)) {

                                    new Alert(Alert.AlertType.CONFIRMATION, "Animal updated successfully!").show();
                                    dashboardFormController.searchByAnimalForm(animal.getCommon_name());
                                    Stage stage = (Stage) txtCommonName.getScene().getWindow();
                                    stage.close();

                                } else {
                                    new Alert(Alert.AlertType.WARNING, "Animal update unsuccessfully!").show();
                                }
                            }
                        } else {
                            new Alert(Alert.AlertType.WARNING, "Please add locations.").show();
                        }
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Please upload 3 images.").show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Average Weight should be a number.").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Average Lifetime should be less than 100.").show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please fill in all fields.").show();
        }
    }

    @FXML
    void searchBtnOnAction(ActionEvent event) {
        if (!txtSearch.getText().isEmpty()) {
            dashboardFormController.searchByAnimalForm(txtSearch.getText());
            Stage stage = (Stage) txtCommonName.getScene().getWindow();
            stage.hide();
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter the name of animal").show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        searchBtnOnAction(event);
    }

    @FXML
    void addLocationBtnOnAction(ActionEvent event) {
        String selectedLocation = cmbLocation.getSelectionModel().getSelectedItem();
        if (selectedLocation == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a location").show();
            return;
        }

        boolean locationExists = locations.stream()
                .anyMatch(loc -> loc.getName().equals(selectedLocation));
        if (locationExists) {
            new Alert(Alert.AlertType.WARNING, selectedLocation + " already added to Locations.").show();
            return;
        }

        Location location = geocodeLocations(selectedLocation);
        if (location != null) {
            locations.add(location);
            loadMap();
            new Alert(Alert.AlertType.CONFIRMATION, selectedLocation + " added to Locations.").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to geocode the selected location.").show();
        }
    }

    private Location geocodeLocations(String selectedLocation) {
        /*Dotenv dotenv = Dotenv.configure().load();*/

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBIkRBwLozPBlqhe8xx8rXlUQcpUszeIY0")
                .build();

        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, selectedLocation).await();

            if (results.length > 0) {
                LatLng latLng = results[0].geometry.location;
                double latitude = latLng.lat;
                double longitude = latLng.lng;

                return new Location(selectedLocation, latitude, longitude);
            } else {
                new Alert(Alert.AlertType.WARNING, "Didn't find the location for " + selectedLocation).show();
            }
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Google Maps API error").show();
        }
        return null;
    }

    private void handleImageUpload(ImageView imageView, JFXButton button, int index) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png")
        );
        File selectedFile = fileChooser.showOpenDialog(txtCommonName.getScene().getWindow());

        if (selectedFile != null) {
            byte[] data = Files.readAllBytes(selectedFile.toPath());
            imageView.setImage(new Image(new ByteArrayInputStream(data)));
            button.setVisible(false);

            if (searchedAnimal == null) {
                images.add(data);
            }else {
                if (index >= 0 && index < images.size()) {
                    images.set(index, data);
                }
            }
        }

    }

    private boolean validateFields(TextInputControl... fields) {
        for (TextInputControl field : fields) {
            if (field.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void setController(DashboardFormController dashboardFormController) {
        this.dashboardFormController = dashboardFormController;
    }

    private void loadMap() {
        StringBuilder jsArray = new StringBuilder("[");
        for (Location location : locations) {
            jsArray.append("{lat: ").append(location.getLatitude())
                    .append(", lng: ").append(location.getLongitude())
                    .append("},");
        }
        jsArray.append("]");

        engine.getLoadWorker().stateProperty().addListener((ob, old, newVal) -> {
            if (newVal == Worker.State.SUCCEEDED) {
                engine.executeScript("showLocations(" + jsArray.toString() + ")");
            }
        });
        engine.load(getClass().getResource("/map.html").toExternalForm());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> countries = FXCollections.observableArrayList(
                "United States", "Canada", "Mexico", "Brazil", "Argentina",
                "United Kingdom", "France", "Germany", "Italy", "Spain",
                "China", "Japan", "India", "South Korea", "Australia",
                "Russia", "South Africa", "Egypt", "Nigeria", "Kenya",
                "Saudi Arabia", "United Arab Emirates", "Turkey", "Iran", "Israel",
                "Sweden", "Norway", "Denmark", "Netherlands", "Sri Lanka", "Switzerland"
        );
        cmbLocation.setItems(countries);
        engine = mapView.getEngine();
        loadMap();

        lblCommonName.setVisible(false);
        lblSpecies.setVisible(false);
        lblScientificName.setVisible(false);
        lblGender.setVisible(false);
        lblAverageLifeTime.setVisible(false);
        lblAverageWeight.setVisible(false);
        lblRegion.setVisible(false);
        lblConservationStatus.setVisible(false);
        lblReproduction.setVisible(false);
        lblColor.setVisible(false);
        lblMarkings.setVisible(false);
        lblHabitat.setVisible(false);
        lblBehavior.setVisible(false);
        lblDietaryPreferences.setVisible(false);
        lblAddMoreDetails.setVisible(false);
    }

    public void setSearchedAnimal(Animal searchedAnimal){
        this.searchedAnimal = searchedAnimal;
    }

    public void setAnimalDetails() {
        txtSpecies.setText(searchedAnimal.getSpecies());
        txtSpecies.setPadding(new Insets(15, 10, 5, 0));
        txtSpecies.setLabelFloat(false);
        txtCommonName.setText(searchedAnimal.getCommon_name());
        txtCommonName.setPadding(new Insets(15, 10, 5, 0));
        txtCommonName.setLabelFloat(false);
        txtScientificName.setText(searchedAnimal.getScientific_name());
        txtScientificName.setPadding(new Insets(15, 10, 5, 0));
        txtScientificName.setLabelFloat(false);
        txtGender.setText(searchedAnimal.getGender());
        txtGender.setPadding(new Insets(15, 10, 5, 0));
        txtGender.setLabelFloat(false);
        txtLifeTime.setText(String.valueOf(searchedAnimal.getAverage_life_time()));
        txtLifeTime.setPadding(new Insets(15, 10, 5, 0));
        txtLifeTime.setLabelFloat(false);
        txtWeight.setText(String.valueOf(searchedAnimal.getAverage_weight()));
        txtWeight.setPadding(new Insets(15, 10, 5, 0));
        txtWeight.setLabelFloat(false);
        txtRegion.setText(searchedAnimal.getRegion());
        txtRegion.setPadding(new Insets(15, 10, 5, 0));
        txtRegion.setLabelFloat(false);
        txtConservationStatus.setText(searchedAnimal.getConservation_status());
        txtConservationStatus.setPadding(new Insets(15, 10, 5, 0));
        txtConservationStatus.setLabelFloat(false);
        txtReproduction.setText(searchedAnimal.getReproduction());
        txtReproduction.setPadding(new Insets(15, 10, 5, 0));
        txtReproduction.setLabelFloat(false);
        txtColor.setText(searchedAnimal.getColor());
        txtColor.setPadding(new Insets(15, 10, 5, 0));
        txtColor.setLabelFloat(false);
        txtMarkings.setText(searchedAnimal.getMarkings());
        txtMarkings.setPadding(new Insets(15, 10, 5, 0));
        txtMarkings.setLabelFloat(false);
        txtHabitat.setPadding(new Insets(15, 10, 5, 0));
        txtBehavior.setText(searchedAnimal.getBehavior());
        txtBehavior.setPadding(new Insets(15, 10, 5, 0));
        txtBehavior.setLabelFloat(false);
        txtDietaryPreferences.setText(searchedAnimal.getDietary_preferences());
        txtDietaryPreferences.setPadding(new Insets(15, 10, 5, 0));
        txtDietaryPreferences.setLabelFloat(false);
        txtAdditionalDetails.setText(searchedAnimal.getAdditional_details());
        txtAdditionalDetails.setLabelFloat(false);
        images = searchedAnimal.getImages();
        locations = searchedAnimal.getLocations();

        lblCommonName.setVisible(true);
        lblSpecies.setVisible(true);
        lblScientificName.setVisible(true);
        lblGender.setVisible(true);
        lblAverageLifeTime.setVisible(true);
        lblAverageWeight.setVisible(true);
        lblRegion.setVisible(true);
        lblConservationStatus.setVisible(true);
        lblReproduction.setVisible(true);
        lblColor.setVisible(true);
        lblMarkings.setVisible(true);
        lblHabitat.setVisible(true);
        lblBehavior.setVisible(true);
        lblDietaryPreferences.setVisible(true);
        lblAddMoreDetails.setVisible(true);

        List<ImageView> imageViews = Arrays.asList(image1, image2, image3);

        for (int i = 0; i < images.size(); i++) {
            byte[] imageData = images.get(i);
            imageViews.get(i).setImage(new Image(new ByteArrayInputStream(imageData)));
        }

        loadMap();
        txtCommonName.setEditable(false);
        saveBtn.setText("Update");
    }
}
