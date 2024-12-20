package com.example.demo10;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {

        UserManager userManager = new UserManager();


        SceneManager sceneManager = new SceneManager(primaryStage, userManager);


        GameManager gameManager = new GameManager(sceneManager);
        Scene gameScene = gameManager.createGameScene();
        sceneManager.setGameScene(gameScene);


        primaryStage.setTitle("Fruit Catcher Game");
        primaryStage.setScene(sceneManager.getLoginScene());
        primaryStage.setResizable(false);
        primaryStage.show();


        primaryStage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene == gameScene) {
                gameManager.startGame();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
