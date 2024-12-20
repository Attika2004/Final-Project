package com.example.demo10;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    private final Stage stage;
    private final UserManager userManager;
    private Scene loginScene, signUpScene, resetPasswordScene, levelSelectionScene, mediumLevelScene, hardLevelScene, gameScene, gameScene1;
    private Map<String, String> userDatabase = new HashMap<>();

    public SceneManager(Stage stage, UserManager userManager) {
        this.stage = stage;
        this.userManager = userManager;
        initializeScenes();
    }

    private void initializeScenes() {
        loginScene = createLoginScene();
        signUpScene = createSignUpScene();
        resetPasswordScene = createResetPasswordScene();
        levelSelectionScene = createLevelSelectionScene();

    }

    public Scene getLoginScene() {
        return loginScene;
    }

    public Scene getSignUpScene() {
        return signUpScene;
    }

    public Scene getResetPasswordScene() {
        return resetPasswordScene;
    }

    public Scene getLevelSelectionScene() {
        return levelSelectionScene;
    }

    public Scene getMediumLevelScene() {
        return mediumLevelScene;
    }

    public Scene getHardLevelScene() {
        return hardLevelScene;
    }

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public void setGameScene1(Scene gameScene) {
        this.gameScene = gameScene;
    }

    private Scene createLoginScene() {
        GridPane loginPane = createPaneWithBackground("background1.jpg");
        VBox loginBox = createStyledBox();

        TextField loginUsername = new TextField();
        loginUsername.setPromptText("Enter Username");
        styleTextField(loginUsername);

        PasswordField loginPassword = new PasswordField();
        loginPassword.setPromptText("Enter Password");
        styleTextField(loginPassword);

        Button loginButton = createStyledButton("Login");
        Button signUpButton = createStyledButton("Sign Up");
        Button resetPasswordButton = createStyledButton("Reset Password");

        loginButton.setOnAction(e -> {
            String username = loginUsername.getText();
            String password = loginPassword.getText();

            if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
                stage.setScene(getLevelSelectionScene());
            } else {
                showAlert("Error", "Invalid credentials.");
            }
        });

        signUpButton.setOnAction(e -> stage.setScene(getSignUpScene()));
        resetPasswordButton.setOnAction(e -> stage.setScene(getResetPasswordScene()));

        loginBox.getChildren().addAll(
                new Label("Username:"),
                loginUsername,
                new Label("Password:"),
                loginPassword,
                loginButton,
                signUpButton,
                resetPasswordButton
        );

        loginPane.add(loginBox, 0, 0);
        return new Scene(loginPane, 1000, 600);
    }

    private Scene createSignUpScene() {
        GridPane signUpPane = createPaneWithBackground("background1.jpg");
        VBox signUpBox = createStyledBox();

        TextField signUpUsername = new TextField();
        signUpUsername.setPromptText("Choose Username");
        styleTextField(signUpUsername);

        PasswordField signUpPassword = new PasswordField();
        signUpPassword.setPromptText("Choose Password");
        styleTextField(signUpPassword);

        Button registerButton = createStyledButton("Register");
        Button backToLoginButton1 = createStyledButton("Back to Login");

        registerButton.setOnAction(e -> {
            String username = signUpUsername.getText();
            String password = signUpPassword.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Fields cannot be empty.");
            } else if (userDatabase.containsKey(username)) {
                showAlert("Error", "Username already exists.");
            } else {
                userDatabase.put(username, password);
                showAlert("Success", "Registration successful!");
                stage.setScene(getLoginScene());
            }
        });

        backToLoginButton1.setOnAction(e -> stage.setScene(getLoginScene()));

        signUpBox.getChildren().addAll(
                new Label("Username:"),
                signUpUsername,
                new Label("Password:"),
                signUpPassword,
                registerButton,
                backToLoginButton1
        );

        signUpPane.add(signUpBox, 0, 0);
        return new Scene(signUpPane, 1000, 600);
    }

    private Scene createResetPasswordScene() {
        GridPane resetPasswordPane = createPaneWithBackground("background1.jpg");
        VBox resetPasswordBox = createStyledBox();

        TextField resetUsername = new TextField();
        resetUsername.setPromptText("Enter Username");
        styleTextField(resetUsername);

        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("Enter New Password");
        styleTextField(newPassword);

        Button resetButton = createStyledButton("Reset Password");
        Button backToLoginButton2 = createStyledButton("Back to Login");

        resetButton.setOnAction(e -> {
            String username = resetUsername.getText();
            String password = newPassword.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Fields cannot be empty.");
            } else if (!userDatabase.containsKey(username)) {
                showAlert("Error", "Username does not exist.");
            } else {
                userDatabase.put(username, password);
                showAlert("Success", "Password reset successful!");
                stage.setScene(getLoginScene());
            }
        });

        backToLoginButton2.setOnAction(e -> stage.setScene(getLoginScene()));

        resetPasswordBox.getChildren().addAll(
                new Label("Username:"),
                resetUsername,
                new Label("New Password:"),
                newPassword,
                resetButton,
                backToLoginButton2
        );

        resetPasswordPane.add(resetPasswordBox, 0, 0);
        return new Scene(resetPasswordPane, 1000, 600);
    }


    private Scene createLevelSelectionScene() {
        GridPane levelPane = createPaneWithBackground("background1.jpg");
        VBox levelBox = createStyledBox();

        Label selectLevelLabel = new Label("Select a Level:");
        selectLevelLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button easyButton = createStyledButton("Easy");
        Button mediumButton = createStyledButton("Medium");
        Button hardButton = createStyledButton("Hard");

        easyButton.setOnAction(e -> {
            stage.setScene(gameScene); // Assuming gameScene is set up for Easy
        });

        mediumButton.setOnAction(e -> {
            MediumLevelGameManager mediumGame = new MediumLevelGameManager(this, 2);
            stage.setScene(mediumGame.createGameScene1());
            mediumGame.startGame();
        });

        hardButton.setOnAction(e -> {
            MediumLevelGameManager hardGame = new MediumLevelGameManager(this, 3);
            stage.setScene(hardGame.createGameScene1());
            hardGame.startGame();
        });

        levelBox.getChildren().addAll(
                selectLevelLabel,
                easyButton,
                mediumButton,
                hardButton
        );

        levelPane.add(levelBox, 0, 0);
        return new Scene(levelPane, 1000, 600);
    }


    private GridPane createPaneWithBackground(String backgroundImage) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setAlignment(Pos.CENTER);
        Image image = new Image(backgroundImage);
        BackgroundImage bgImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        pane.setBackground(new Background(bgImage));
        return pane;
    }

    private VBox createStyledBox() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-border-color: violet; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-padding: 10px;");
        return box;
    }

    private void styleTextField(TextField textField) {
        textField.setStyle("-fx-padding: 8px; -fx-border-color: violet; -fx-border-width: 2px; -fx-border-radius: 5px;");
        textField.setPrefWidth(200);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: violet; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px 16px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        return button;
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void redirectToLevelSelection() {
        stage.setScene(getLevelSelectionScene());
    }
}