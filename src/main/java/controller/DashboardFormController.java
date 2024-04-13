package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entity.Animal;
import entity.Location;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.AnimalModel;
import model.AnimalModelImpl;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;




// Controller class for the DashboardForm.fxml
public class DashboardFormController implements Initializable {

    public ImageView imgDashboardLogo;
    public Button btnLogout;
    public AnchorPane dashboardForm;
    public Label lblDetails;
    public Label lblAnimalName;
    public AnchorPane detailsPane;
    public HBox mapHBox;
    public HBox CommonLocationHBox;
    public HBox imgHBox;
    public VBox mainVBox;
    public ScrollPane mainScrollPane;
    public ImageView imageuploadview;

    @FXML
    private ImageView dashboardImage1;

    @FXML
    private ImageView dashboardImage2;

    @FXML
    private ImageView dashboardImage3;

    @FXML
    private ImageView dashboardImage4;

    @FXML
    private Text dashboardAnimalName1;

    @FXML
    private Text dashboardAnimalName2;

    @FXML
    private Text dashboardAnimalName3;

    @FXML
    private Text dashboardAnimalName4;

    @FXML
    private Text dashboardAnimalSname1;

    @FXML
    private Text dashboardAnimalSname2;

    @FXML
    private Text dashboardAnimalSname3;

    @FXML
    private Text dashboardAnimalSname4;


    @FXML
    private JFXTextField txtSearch;
    // Reference to the AnchorPane defined in the FXML file
    @FXML
    private AnchorPane animalDetailsAnchorPane;

    // Reference to the "Manage Animal" button defined in the FXML file
    @FXML
    private Button btnManageAnimal;

    @FXML
    private ImageView imageView1;

    @FXML
    private ImageView imageView2;

    @FXML
    private ImageView imageView3;

    @FXML
    private JFXButton uploadImgBtn;

    @FXML
    private JFXButton uploadImgBtn1;

    @FXML
    private WebView mapView;

    private WebEngine engine;

    AnimalModel animalModel = new AnimalModelImpl();

    private Animal searchedAnimal;

    // Flag to track whether the animal form is opened or not
    private boolean formOpened = false;

    // Method called when the "Manage Animal" button is clicked
    @FXML
    void manageAnimalBtnOnAction(ActionEvent event) throws IOException {
        // Check if the form is not already opened
        if (!formOpened) {
            // Open the Animal Form
            openAnimalForm();
        }
    }

    // Method to open the Animal Form
    private void openAnimalForm() throws IOException {
        // Load the Animal Form FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/animalForm.fxml"));
        AnchorPane anchorPane = loader.load();
        AnimalFormController animalFormController = loader.getController();
        animalFormController.setController(this);
        if (searchedAnimal != null){
            animalFormController.setSearchedAnimal(searchedAnimal);
            animalFormController.setAnimalDetails();
        }

        // Create a new stage for the Animal Form
        Stage stage = new Stage();
        // Set the scene for the Animal Form stage
        stage.setScene(new Scene(anchorPane));
        // Show the Animal Form stage
        stage.show();
        // Center the Animal Form stage on the screen
//        stage.centerOnScreen();
        stage.setY(-10);
        // Set the title for the Animal Form stage
        stage.setTitle("Wildlife Management System - Animal Page");

        // Set the application icon for the dashboard stage
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/icon.png")));
        stage.getIcons().add(image);

        // Update the formOpened flag and set an event handler for when the Animal Form stage is closed
        formOpened = true;
        btnManageAnimal.setDisable(true);

        stage.setOnCloseRequest(e -> {
            formOpened = false;
            // Re-enable the "Manage Animal" button when the Animal Form stage is closed
            btnManageAnimal.setDisable(false);
        });
        stage.setOnHiding(e -> {
            formOpened = false;
            // Re-enable the "Manage Animal" button when the Animal Form stage is hide
            btnManageAnimal.setDisable(false);
        });
    }

    // Method called when the "Upload Image" button is clicked
    @FXML
    void uploadImgBtnOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Image image = new Image(new FileInputStream(selectedFile));
                imageuploadview.setImage(image);
                imageuploadview.setCursor(Cursor.HAND);
                String absolutePath = selectedFile.getAbsolutePath();
                imagescanrun(absolutePath);
                uploadImgBtn1.setVisible(true);
                uploadImgBtn.setVisible(false);
//
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private void imagescanrun(String absolutePath) throws IOException {
        String credentialsToEncode = "acc_1df6e21c9636d8b" + ":" + "359ba138f5867d2d1d3756434378c8da";
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        File fileToUpload = new File(absolutePath);

        String endpoint = "/tags";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "Image Upload";

        URL urlObject = new URL("https://api.imagga.com/v2" + endpoint);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + basicAuth);
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        DataOutputStream request = new DataOutputStream(connection.getOutputStream());
        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileToUpload.getName() + "\"" + crlf);
        request.writeBytes(crlf);

        InputStream inputStream = new FileInputStream(fileToUpload);
        int bytesRead;
        byte[] dataBuffer = new byte[1024];
        while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
            request.write(dataBuffer, 0, bytesRead);
        }

        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
        request.flush();
        request.close();


        InputStream responseStream = new BufferedInputStream(connection.getInputStream());
        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
        String line;
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        responseStreamReader.close();

        String response = stringBuilder.toString();
        responseStream.close();
        connection.disconnect();
//        System.out.println(response);

        Set<String> birdNames = extractBirdNames(response);
        birdNames.remove("bird"); // Remove the generic term "bird" from the array(array eken ain krnw)
        /*birdNames.remove("feline");
        birdNames.remove("predator");
        birdNames.remove("big cat");*/
        if (!birdNames.isEmpty()) {
            String firstBirdName = birdNames.iterator().next();
            searchbyimageOutput(firstBirdName);
            System.out.println(
                    "Bird names extracted with confidence 100%: " + birdNames
            );
            System.out.println("\uD83D\uDC26\uD83D\uDC26\uD83D\uDC26\uD83D\uDC26");
        } else {
            new Alert(Alert.AlertType.WARNING,"No bird found in the image").show();
            imageuploadview.setImage(new Image("image/updateLocationImg.png"));
        }
    }

    private void searchbyimageOutput(String firstBirdName) {
        Animal animal = animalModel.searchAnimal(firstBirdName);
        System.out.println(firstBirdName);
        if (animal!= null) {
            showSearchResult(animal);
        } else {
            new Alert(Alert.AlertType.WARNING, "Couldn't find '"+ firstBirdName + "'.").show();
            imageuploadview.setImage(new Image("image/updateLocationImg.png"));
        }

    }

    private static Set<String> extractBirdNames(String jsonResponse) {
        Set<String> birdNames = new HashSet<>();
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray tagsArray = jsonObject.getJSONObject("result").getJSONArray("tags");

        for (int i = 0; i < tagsArray.length(); i++) {
            JSONObject tagObject = tagsArray.getJSONObject(i);
            double confidence = tagObject.getDouble("confidence");
            if (confidence == 100) {
                String tagName = tagObject.getJSONObject("tag").getString("en").toLowerCase();
                birdNames.add(tagName);
            }
        }
        return birdNames;
    }

    @FXML
    void searchBtnOnAction(ActionEvent event) {
        if (!txtSearch.getText().isEmpty()) {
            Animal animal = animalModel.searchAnimal(txtSearch.getText());
            if (animal != null) {
                showSearchResult(animal);
            } else {
                new Alert(Alert.AlertType.WARNING, "Couldn't find the animal.").show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter the name of animal").show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        searchBtnOnAction(event);
    }

    // Method called during the initialization of the controller
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the animalDetailsAnchorPane to be initially invisible
        setImageViews();
        setDashboardAnimalDetails();
        animalDetailsAnchorPane.setVisible(false);
        engine = mapView.getEngine();
        uploadImgBtn.setVisible(true);
        uploadImgBtn1.setVisible(false);
    }

    private void setImageViews() {
        List<ImageView> imageViews = Arrays.asList(dashboardImage1, dashboardImage2, dashboardImage3, dashboardImage4);

        for (ImageView imageView : imageViews) {
            double size = Math.min(imageView.getFitWidth(), imageView.getFitHeight());
            double radius = size / 2.0;

            Circle clip = new Circle(radius);
            clip.setCenterX(radius);
            clip.setCenterY(radius);
            imageView.setClip(clip);
        }
    }

    private void setDashboardAnimalDetails() {
        List<Animal> animals = animalModel.getAnimals();

        if (animals.size() >= 4) {
            List<ImageView> imageViews = Arrays.asList(dashboardImage1, dashboardImage2, dashboardImage3, dashboardImage4);
            List<Text> names = Arrays.asList(dashboardAnimalName1, dashboardAnimalName2, dashboardAnimalName3, dashboardAnimalName4);
            List<Text> sciName = Arrays.asList(dashboardAnimalSname1, dashboardAnimalSname2, dashboardAnimalSname3, dashboardAnimalSname4);

            for (int i = 0; i < animals.size(); i++) {
                Animal animal = animals.get(i);
                imageViews.get(i).setImage(new Image(new ByteArrayInputStream(animal.getImages().get(0)), 176, 176, false, true));
                names.get(i).setText(animal.getCommon_name());
                sciName.get(i).setText(animal.getScientific_name());
            }
        }
    }

    public void setUser() {
        btnManageAnimal.setVisible(false);
        imgDashboardLogo.setY(30);
    }

    public void btnLogoutOnAction(ActionEvent actionEvent) throws IOException {
        Parent loginForm = FXMLLoader.load(getClass().getResource("/view/loginForm.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loginForm));
        stage.centerOnScreen();
        stage.show();
        // Set the application icon for the dashboard stage
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/icon.png")));
        stage.getIcons().add(image);
        stage.setTitle("Wildlife Management System - Login Page");
        dashboardForm.getScene().getWindow().hide();
    }

    public void closeOnMouseClicked(MouseEvent mouseEvent) {
        animalDetailsAnchorPane.setVisible(false);
        txtSearch.clear();
        imageuploadview.setImage(new Image("image/updateLocationImg.png"));
        btnManageAnimal.setText("Add Animal");
        searchedAnimal = null;
        uploadImgBtn1.setVisible(false);
        uploadImgBtn.setVisible(true);
    }

    public void searchByAnimalForm(String term) {
        txtSearch.setText(term);
        Animal animal = animalModel.searchAnimal(term);
        if (animal != null) {
            showSearchResult(animal);
        } else {
            new Alert(Alert.AlertType.WARNING, "Couldn't find the animal.").show();
        }
    }

    private void showSearchResult(Animal animal) {
        animalDetailsAnchorPane.setVisible(true);
        mainVBox.getChildren().clear();
        btnManageAnimal.setText("Update Animal");
        searchedAnimal = animal;

        List<ImageView> imageViews = Arrays.asList(imageView1, imageView2, imageView3);

        for (int i = 0; i < animal.getImages().size(); i++) {
            byte[] imageData = animal.getImages().get(i);
            imageViews.get(i).setImage(new Image(new ByteArrayInputStream(imageData)));
        }

        Label title = new Label(animal.getCommon_name());
        title.setFont(new Font("Poppins", 28));
        title.setStyle("-fx-font-weight: bold;");

        Label detail = new Label("The "+ animal.getCommon_name() +" is a fascinating creature found in "+ animal.getRegion() +" and various regions around the world." +
                " Known for its "+ animal.getColor() +" "+ animal.getMarkings() + ", this "+ animal.getGender()+" " +
                animal.getSpecies() +" exhibits remarkable "+ animal.getBehavior()+". With an average lifespan of " +
                animal.getAverage_life_time() +" years and an average weight of "+ animal.getAverage_weight() +"kg, " +
                "these "+ animal.getConservation_status() +" animals play a crucial role in their ecosystems. Their " +
                "preferred habitat includes "+ animal.getHabitat() +" and main dietary preferences is "+ animal.getDietary_preferences() +
                ". "+ animal.getReproduction() +" contribute to the perpetuation of their species. "+ animal.getAdditional_details() +".");
        detail.setWrapText(true);
        detail.setTextAlignment(TextAlignment.JUSTIFY);
        detail.setFont(new Font("Calibri", 16));
        VBox.setMargin(detail, new Insets(10, 0, 0, 0));
        VBox detailWrapper = new VBox();
        detailWrapper.getChildren().add(detail);

        HBox paragrph = new HBox();
        mainVBox.setPadding(new Insets(20, 30, 20, 20));
        paragrph.getChildren().add(detailWrapper);

        mainVBox.getChildren().addAll(
                imgHBox, CommonLocationHBox, mapHBox,
                title, paragrph
        );

        loadMap(animal.getLocations());
    }

    private void loadMap(List<Location> locations) {

        StringBuilder jsArray = new StringBuilder("[");
        for (Location location : locations) {
            jsArray.append("{lat: ").append(location.getLatitude())
                    .append(", lng: ").append(location.getLongitude())
                    .append("},");
        }
        jsArray.append("]");

        engine.getLoadWorker().stateProperty().addListener((ob, old, newVal) -> {
            if (newVal == Worker.State.SUCCEEDED){
                engine.executeScript("showLocations("+ jsArray.toString() +")");
            }
        });
        engine.load(getClass().getResource("/map.html").toExternalForm());
    }
}
