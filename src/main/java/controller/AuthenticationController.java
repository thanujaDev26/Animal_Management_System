package controller;

import db.DBBackup;
import db.DBRestore;
import entity.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.AdminModel;
import model.AdminModelImpl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class AuthenticationController {
    @FXML
    private TextField upUser,upPass,upEmail,inUser,inPass;
    @FXML
    private Label upAlert,inAlert;
    public Button btnSignIn;
    public Button btnSignUp;
    public Button btnToSignUp;
    public Button btnToSignIn;
    public Button btnToLogin;
    AdminModel adminModel = new AdminModelImpl();

    @FXML
    public void onUpButtonClick() {
        String user = upUser.getText();
        String pass = upPass.getText();
        String email = upEmail.getText();

        if (user.isEmpty()) {
            upAlert.setText("Username field can't be empty");
        } else if (email.isEmpty()) {
            upAlert.setText("Email field can't be empty");
        } else if (pass.isEmpty()) {
            upAlert.setText("Password field can't be empty");
        } else {
            Admin admin = new Admin(user, pass, email);
            if (adminModel.saveAdmin(admin)) {

                new Alert(Alert.AlertType.CONFIRMATION, "Admin registered successfully!").show();
                DBBackup.backup();

            } else {
                new Alert(Alert.AlertType.WARNING, "Admin registration unsuccessful!").show();
            }
        }
        clearUp();
    }
    @FXML
    public void onInButtonClick(ActionEvent event) throws IOException {
        String user = inUser.getText();
        String pass = inPass.getText();

        if (user.isEmpty()) {
            inAlert.setText("Username field can't be empty");
        } else if (pass.isEmpty()) {
            inAlert.setText("Password field can't be empty");
        } else {

            Admin admin = adminModel.searchAdmin(user);
            if(admin != null){
                if(admin.getPassword().equals(pass)){
                    switchToDashBoard(event);
                }else{
                    new Alert(Alert.AlertType.WARNING, "Password is incorrect").show();
                }
            }else{
                new Alert(Alert.AlertType.WARNING, "Admin not found!").show();
            }
        }
        clearIn();
    }

    @FXML
    protected void clearIn(){
        inUser.setText("");
        inPass.setText("");;
    }
    @FXML
    protected void clearUp(){
        upUser.setText("");
        upEmail.setText("");
        upPass.setText("");
    }

    protected void switchToDashBoard(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/view/DashBoardForm.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage) btnSignIn.getScene().getWindow();
        window.close();
        Stage window1 = new Stage();
        window1.setScene(loginScene);
        window1.centerOnScreen();
        // Set the application icon for the dashboard stage
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/icon.png")));
        window1.getIcons().add(image);
        window1.setTitle("Wildlife Management System - Admin Dashboard Page");
        window1.show();
    }

    @FXML
    protected void switchToInPage(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/view/signin-page.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage) btnToSignIn.getScene().getWindow();
        window.close();
        Stage window1 = new Stage();
        window1.setScene(loginScene);
        window1.centerOnScreen();
        // Set the application icon for the dashboard stage
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/icon.png")));
        window1.getIcons().add(image);
        window1.setTitle("Wildlife Management System - Admin Sign In Page");
        window1.show();
    }

    @FXML
    protected void switchToUpPage(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/view/signup-page.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage) btnToSignUp.getScene().getWindow();
        window.close();
        Stage window1 = new Stage();
        window1.setScene(loginScene);
        window1.centerOnScreen();
        // Set the application icon for the dashboard stage
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/icon.png")));
        window1.getIcons().add(image);
        window1.setTitle("Wildlife Management System - Admin Sign Up Page");
        window1.show();
    }
    @FXML
    protected void switchToLogin(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage) btnToLogin.getScene().getWindow();
        window.close();
        Stage window1 = new Stage();
        window1.setScene(loginScene);
        window1.centerOnScreen();
        // Set the application icon for the dashboard stage
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/icon.png")));
        window1.getIcons().add(image);
        window1.setTitle("Wildlife Management System - Login Page");
        window1.show();
    }
}
