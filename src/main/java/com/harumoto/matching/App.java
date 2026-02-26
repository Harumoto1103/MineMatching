package com.harumoto.matching;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import com.harumoto.matching.MatchingGame.Backup;
import com.harumoto.matching.MatchingGame.GameCanvas;
import com.harumoto.matching.MatchingGame.Levels.Level;

public class App extends Application {

    // Adopted from non-gui version.
    public static ArrayList<Level> levels = new ArrayList<>();
    /* The amount of the hammer tool. */
    public static Integer hammer = 2;
    /* The array recording whethe a level is passed. */
    public static Boolean[] passed;
    /* The level index. */
    public static Integer levelIndex = 0;
    Exception INVALID_CMD = new Exception("Invalid command.");
    /* Whether the hammer tool button is pressed. */
    public static Boolean onHammer = false;

    /* Basic scene settings. */
    private static Scene scene;
    private final Double appHeight = 1920 / 3.0;
    final Double appWidth = appHeight * (1080.0 / 1920.0);

    /* The stack used for storing every move in a Level. */
    public static Stack<Backup> backupStack = new Stack<>();

    /* The amount of the roll back tool. */
    public static Integer rollBacks = 0;

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws Exception {
        this.initLevels("Presets/level-0.txt");
        this.initLevels("Presets/level-1.txt");
        this.initLevels("Presets/level-2.txt");
        this.initLevels("Presets/level-3.txt");
        this.initLevels("Presets/level-4.txt");
        this.initLevels("Presets/level-5.txt");

        /* Initializes the passed array. */
        App.passed = new Boolean[App.levels.size()];
        for (Integer i = 0; i < App.passed.length; i++)
            App.passed[i] = false;

        scene = new Scene(loadFXML("primary"), appWidth, appHeight);
        stage.setTitle("MineMatching");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.getIcons().add(new Image(App.class.getResourceAsStream("Images/grass-texture.png")));
        stage.show();

    }

    private void initLevels(String resourcePath) throws Exception {
        String tempFilePath = ResourceUtil.copyResourceToTempFile(resourcePath);
        App.levels.add(new Level(tempFilePath));
    }

    public static Level currentLevel() {
        return App.levels.get(levelIndex);
    }

    public static GameCanvas currentCanvas() {
        return App.currentLevel().getCanvas();
    }

    /**
     * Switching to a specific scene with fade animation.
     * 
     * @param fxml       The name of the scene file (without ".fxml").
     * @param onFinished Callback function.
     * @throws IOException
     */
    static void setRoot(String fxml, Runnable onFinished) throws IOException {
        Parent newRoot = loadFXML(fxml);

        // Create fade out transition for current root.
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), scene.getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // When fade out is finished, set new root and start fade in.
        fadeOut.setOnFinished(event -> {
            scene.setRoot(newRoot);

            // Create fade in transition for new root.
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newRoot);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            fadeIn.setOnFinished(e -> {
                onFinished.run();
            });

            fadeIn.play();

        });

        fadeOut.play();
    }

    /**
     * Loads and converts an fxml file into an instance of the Parent class.
     * 
     * @param fxml The name of an fxml file (without ".fxml").
     * @return An instance of the Parent class.
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
